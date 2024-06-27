#import "MockLocationCheckerPlugin.h"
#import "MockLocationRequest.h"
#import <CoreLocation/CoreLocation.h>

@implementation MockLocationCheckerPlugin

- (instancetype)init {
    self = [super init];
    if (self) {
        _m_requests = [NSMutableArray new];
    }
    return self;
}

- (void)dealloc {
    [self cleanUp];
}

- (void)cleanUp {
    [self.m_requests removeAllObjects];
    self.m_locationManager = nil;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

- (BOOL)checkIfGPSIsEnabled {
    return [CLLocationManager locationServicesEnabled];
}

- (BOOL)hasLocationPermission {
    CLAuthorizationStatus status = [CLLocationManager authorizationStatus];
    return status == kCLAuthorizationStatusAuthorizedAlways || status == kCLAuthorizationStatusAuthorizedWhenInUse;
}

- (void)locationManager:(CLLocationManager *)manager didUpdateLocations:(NSArray<CLLocation *> *)locations {
    CLLocation *currentLocation = [locations firstObject];
    if (@available(iOS 15, *)) {
        CLLocationSourceInformation *sourceInformation = [currentLocation sourceInformation];
        NSDictionary *dict = @{@"isLocationMocked": @(sourceInformation.isSimulatedBySoftware)};
        for (MockLocationRequest *request in self.m_requests) {
            request.m_resolve(dict);
        }
    } else {
        for (MockLocationRequest *request in self.m_requests) {
            request.m_reject(@"2", @"Couldn't determine if location is mocked", nil);
        }
    }
    [self cleanUp];
}

- (void)locationManager:(CLLocationManager *)manager didFailWithError:(NSError *)error {
    for (MockLocationRequest *request in self.m_requests) {
        request.m_reject(@"2", @"Couldn't determine if location is mocked", error);
    }
    [self cleanUp];
}

CAP_PLUGIN_METHOD(isMockLocationEnabled, CAPPluginReturnPromise) {
    if (![self checkIfGPSIsEnabled]) {
        call.reject(@"0", @"GPS is not enabled", nil);
        return;
    }
    if (![self hasLocationPermission]) {
        call.reject(@"1", @"You have no permission to access location", nil);
        return;
    }

    self.m_locationManager = [CLLocationManager new];
    self.m_locationManager.delegate = self;
    self.m_locationManager.distanceFilter = kCLDistanceFilterNone;
    self.m_locationManager.desiredAccuracy = kCLLocationAccuracyBest;

    MockLocationRequest *request = [[MockLocationRequest alloc] initWithPromise:^(id result) {
        [call resolve:result];
    } reject:^(NSString *code, NSString *message, NSError *error) {
        [call reject:message code:code error:error];
    }];

    [self.m_requests addObject:request];
    [self.m_locationManager requestLocation];
}

@end
