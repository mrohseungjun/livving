import ComposeApp
import FirebaseMessaging
import UIKit

final class FirebasePushTokenClientBridge: NSObject, IosPushTokenProvider {
    static let shared = FirebasePushTokenClientBridge()

    private var cachedToken: String?

    func updateToken(_ token: String?) {
        cachedToken = token
    }

    func getToken(completionHandler: @escaping (PlatformPlatformPushToken?, Error?) -> Void) {
        if let cachedToken, !cachedToken.isEmpty {
            completionHandler(platformToken(cachedToken), nil)
            return
        }

        Messaging.messaging().token { [weak self] token, error in
            if let error {
                completionHandler(nil, error)
                return
            }

            guard let token, !token.isEmpty else {
                completionHandler(nil, PushTokenError.missingToken)
                return
            }

            self?.cachedToken = token
            completionHandler(self?.platformToken(token), nil)
        }
    }

    private func platformToken(_ token: String) -> PlatformPlatformPushToken {
        PlatformPlatformPushToken(
            token: token,
            platform: "ios",
            deviceId: UIDevice.current.identifierForVendor?.uuidString
        )
    }
}

enum PushTokenError: LocalizedError {
    case missingToken

    var errorDescription: String? {
        switch self {
        case .missingToken:
            return "iOS FCM 토큰을 받을 수 없습니다."
        }
    }
}
