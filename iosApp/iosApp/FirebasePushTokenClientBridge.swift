import ComposeApp
import FirebaseMessaging
import UIKit

final class FirebasePushTokenClientBridge: NSObject, IosPushTokenProvider {
    static let shared = FirebasePushTokenClientBridge()

    private var cachedToken: String?
    private var apnsTokenReady = false
    private var pendingCompletions: [(PlatformPlatformPushToken?, Error?) -> Void] = []

    func updateToken(_ token: String?) {
        cachedToken = token
        guard let token, !token.isEmpty else { return }
        completePending(with: platformToken(token), error: nil)
    }

    func markApnsTokenReady() {
        apnsTokenReady = true
        resolvePendingCompletions()
    }

    func getToken(completionHandler: @escaping (PlatformPlatformPushToken?, Error?) -> Void) {
        if let cachedToken, !cachedToken.isEmpty {
            completionHandler(platformToken(cachedToken), nil)
            return
        }

        guard apnsTokenReady else {
            pendingCompletions.append(completionHandler)
            return
        }

        fetchToken(completionHandler: completionHandler)
    }

    private func resolvePendingCompletions() {
        guard !pendingCompletions.isEmpty else { return }
        if let cachedToken, !cachedToken.isEmpty {
            completePending(with: platformToken(cachedToken), error: nil)
            return
        }

        fetchToken { [weak self] token, error in
            self?.completePending(with: token, error: error)
        }
    }

    private func fetchToken(completionHandler: @escaping (PlatformPlatformPushToken?, Error?) -> Void) {
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

    private func completePending(with token: PlatformPlatformPushToken?, error: Error?) {
        let completions = pendingCompletions
        pendingCompletions.removeAll()
        completions.forEach { completion in
            completion(token, error)
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
