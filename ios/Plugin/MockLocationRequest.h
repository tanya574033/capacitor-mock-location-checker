#import <Foundation/Foundation.h>

@interface MockLocationRequest : NSObject

typedef void (^ResolveBlock)(NSDictionary *result);
typedef void (^RejectBlock)(NSString *code, NSString *message, NSError *error);

@property (nonatomic, copy) ResolveBlock m_resolve;
@property (nonatomic, copy) RejectBlock m_reject;

- (instancetype)initWithResolve:(ResolveBlock)resolve reject:(RejectBlock)reject;

@end
