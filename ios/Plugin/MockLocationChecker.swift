import Foundation

@objc public class MockLocationChecker: NSObject {
    @objc public func echo(_ value: String) -> String {
        print(value)
        return value
    }
}
