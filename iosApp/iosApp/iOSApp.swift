import ComposeApp
import FirebaseCore
import FirebaseMessaging
import KakaoSDKCommon
import SwiftUI
import UIKit
import UserNotifications

@main
struct iOSApp: App {
    @UIApplicationDelegateAdaptor(AppDelegate.self) private var appDelegate

    init() {
        let nativeAppKey = Bundle.main.object(forInfoDictionaryKey: "KAKAO_NATIVE_APP_KEY") as? String
        KakaoSDK.initSDK(appKey: nativeAppKey ?? "")
        IosKakaoAuthClientKt.installIosKakaoAuthClient(client: KakaoAuthClientBridge())
        IosPushTokenClientKt.installIosPushTokenProvider(provider: FirebasePushTokenClientBridge.shared)
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}

final class AppDelegate: NSObject, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {
    func application(
        _ application: UIApplication,
        didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey: Any]? = nil
    ) -> Bool {
        configureFirebaseIfPossible()
        UNUserNotificationCenter.current().delegate = self
        Messaging.messaging().delegate = self
        requestPushAuthorization(application)
        return true
    }

    func application(
        _ application: UIApplication,
        didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data
    ) {
        Messaging.messaging().setAPNSToken(deviceToken, type: apnsTokenType())
        let tokenPrefix = deviceToken.map { String(format: "%02.2hhx", $0) }.joined().prefix(12)
        print("LivvingPushToken iOS APNs token registered prefix=\(tokenPrefix)")
        FirebasePushTokenClientBridge.shared.markApnsTokenReady()
    }

    func application(
        _ application: UIApplication,
        didFailToRegisterForRemoteNotificationsWithError error: Error
    ) {
        print("Failed to register APNs token: \(error.localizedDescription)")
        FirebasePushTokenClientBridge.shared.markTokenUnavailable("APNs registration failed: \(error.localizedDescription)")
    }

    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String?) {
        FirebasePushTokenClientBridge.shared.updateToken(fcmToken)
    }

    func userNotificationCenter(
        _ center: UNUserNotificationCenter,
        willPresent notification: UNNotification
    ) async -> UNNotificationPresentationOptions {
        [.banner, .list, .sound, .badge]
    }

    private func configureFirebaseIfPossible() {
        guard FirebaseApp.app() == nil else { return }
        guard Bundle.main.path(forResource: "GoogleService-Info", ofType: "plist") != nil else {
            print("GoogleService-Info.plist is missing. iOS FCM token registration is disabled.")
            FirebasePushTokenClientBridge.shared.markTokenUnavailable("GoogleService-Info.plist is missing")
            return
        }
        FirebaseApp.configure()
        print("LivvingPushToken Firebase configured for iOS")
    }

    private func requestPushAuthorization(_ application: UIApplication) {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { granted, error in
            if let error {
                FirebasePushTokenClientBridge.shared.markTokenUnavailable("Push authorization failed: \(error.localizedDescription)")
                return
            }
            guard granted else {
                FirebasePushTokenClientBridge.shared.markTokenUnavailable("Push authorization was denied")
                return
            }
            print("LivvingPushToken iOS push authorization granted")
            DispatchQueue.main.async {
                application.registerForRemoteNotifications()
            }
        }
    }

    private func apnsTokenType() -> MessagingAPNSTokenType {
        #if DEBUG
        return .sandbox
        #else
        return .prod
        #endif
    }
}
