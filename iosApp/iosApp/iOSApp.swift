import ComposeApp
import KakaoSDKCommon
import SwiftUI

@main
struct iOSApp: App {
    init() {
        let nativeAppKey = Bundle.main.object(forInfoDictionaryKey: "KAKAO_NATIVE_APP_KEY") as? String
        KakaoSDK.initSDK(appKey: nativeAppKey ?? "")
        IosKakaoAuthClientKt.installIosKakaoAuthClient(client: KakaoAuthClientBridge())
    }

    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
