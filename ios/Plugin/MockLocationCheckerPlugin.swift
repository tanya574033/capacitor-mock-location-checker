import Foundation
import Capacitor

@objc(MockLocationCheckerPlugin)
public class MockLocationCheckerPlugin: CAPPlugin {
    private let mockLocationChecker = MockLocationChecker()

    @objc func isMockLocationEnabled(_ call: CAPPluginCall) {
        mockLocationChecker.checkMockLocation { isMocked in
            if isMocked {
                call.resolve(["isLocationMocked": true])
            } else {
                call.resolve(["isLocationMocked": false])
            }
        }
    }
}
