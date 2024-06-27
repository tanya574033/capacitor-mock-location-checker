import Foundation
import Capacitor
import CoreLocation

@objc(MockLocationChecker)
public class MockLocationChecker: CAPPlugin, CLLocationManagerDelegate {
    private var locationManager: CLLocationManager?
    private var requests: [MockLocationRequest] = []

    @objc func isMockLocationEnabled(_ call: CAPPluginCall) {
        if !CLLocationManager.locationServicesEnabled() {
            call.reject("GPS is not enabled")
            return
        }
        let status = CLLocationManager.authorizationStatus()
        if status != .authorizedAlways && status != .authorizedWhenInUse {
            call.reject("You have no permission to access location")
            return
        }

        locationManager = CLLocationManager()
        locationManager?.delegate = self
        locationManager?.distanceFilter = kCLDistanceFilterNone
        locationManager?.desiredAccuracy = kCLLocationAccuracyBest

        let request = MockLocationRequest(resolve: { result in
            call.resolve(result)
        }, reject: { code, message, error in
            call.reject(message, code, error)
        })

        requests.append(request)
        locationManager?.requestLocation()
    }

    public func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        guard let currentLocation = locations.first else { return }
        if #available(iOS 15.0, *) {
            let sourceInformation = currentLocation.sourceInformation
            let dict: [String: Any] = ["isLocationMocked": sourceInformation?.isSimulatedBySoftware ?? false]
            for request in requests {
                request.resolve(dict)
            }
        } else {
            for request in requests {
                request.reject("2", "Couldn't determine if location is mocked", nil)
            }
        }
        cleanUp()
    }

    public func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        for request in requests {
            request.reject("2", "Couldn't determine if location is mocked", error as NSError)
        }
        cleanUp()
    }

    private func cleanUp() {
        requests.removeAll()
        locationManager = nil
    }
}

class MockLocationRequest {
    let resolve: ([String: Any]) -> Void
    let reject: (String, String, NSError?) -> Void

    init(resolve: @escaping ([String: Any]) -> Void, reject: @escaping (String, String, NSError?) -> Void) {
        self.resolve = resolve
        self.reject = reject
    }
}
