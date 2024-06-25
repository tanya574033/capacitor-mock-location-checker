#import "MockLocationRequest.h"

@implementation MockLocationRequest

- (instancetype)initWithResolve:(ResolveBlock)resolve reject:(RejectBlock)reject {
    self = [super init];
    if (self) {
        _m_resolve = [resolve copy];
        _m_reject = [reject copy];
    }
    return self;
}

@end
