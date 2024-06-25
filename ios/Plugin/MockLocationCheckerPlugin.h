#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>
#import <CoreLocation/CoreLocation.h>
#import "MockLocationRequest.h"

CAP_PLUGIN(MockLocationCheckerPlugin, "MockLocationCheckerPlugin",
           CAP_PLUGIN_METHOD(isMockLocationEnabled, CAPPluginReturnPromise);
)

@interface MockLocationCheckerPlugin : CAPPlugin <CLLocationManagerDelegate>
@property CLLocationManager *m_locationManager;
@property NSMutableArray<MockLocationRequest *> *m_requests;
@end
