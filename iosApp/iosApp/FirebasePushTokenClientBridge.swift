import ComposeApp
import FirebaseMessaging
import UIKit

final class FirebasePushTokenClientBridge: NSObject, IosPushTokenProvider {
    static let shared = FirebasePushTokenClientBridge()

    private var cachedToken: String?
    private var apnsTokenReady = false
    private var tokenUnavailableReason: String?
    private var pendingCompletions: [(PlatformPlatformPushToken?, Error?) -> Void] = []

    func updateToken(_ token: String?) {
        cachedToken = token
        guard let token, !token.isEmpty else { return }
        print("LivvingPushToken iOS FCM token received prefix=\(token.prefix(12))")
        completePending(with: platformToken(token), error: nil)
    }

    func markApnsTokenReady() {
        apnsTokenReady = true
        tokenUnavailableReason = nil
        print("LivvingPushToken iOS APNs token ready")
        resolvePendingCompletions()
    }

    func markTokenUnavailable(_ reason: String) {
        tokenUnavailableReason = reason
        print("LivvingPushToken iOS token unavailable: \(reason)")
        completePending(with: nil, error: PushTokenError.unavailable(reason))
    }

    func getToken(completionHandler: @escaping (PlatformPlatformPushToken?, Error?) -> Void) {
        if let cachedToken, !cachedToken.isEmpty {
            completionHandler(platformToken(cachedToken), nil)
            return
        }

        if let tokenUnavailableReason {
            completionHandler(nil, PushTokenError.unavailable(tokenUnavailableReason))
            return
        }

        guard apnsTokenReady else {
            pendingCompletions.append(completionHandler)
            scheduleApnsReadinessTimeout()
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
                print("LivvingPushToken iOS FCM token fetch failed: \(error.localizedDescription)")
                completionHandler(nil, error)
                return
            }

            guard let token, !token.isEmpty else {
                print("LivvingPushToken iOS FCM token fetch returned empty token")
                completionHandler(nil, PushTokenError.missingToken)
                return
            }

            self?.cachedToken = token
            print("LivvingPushToken iOS FCM token fetched prefix=\(token.prefix(12))")
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

    private func scheduleApnsReadinessTimeout() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 10) { [weak self] in
            guard let self else { return }
            guard !self.apnsTokenReady, !self.pendingCompletions.isEmpty else { return }
            self.markTokenUnavailable("APNs token was not ready within 10 seconds")
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
    case unavailable(String)

    var errorDescription: String? {
        switch self {
        case .missingToken:
            return "iOS FCM 토큰을 받을 수 없습니다."
        case .unavailable(let reason):
            return reason
        }
    }
}
