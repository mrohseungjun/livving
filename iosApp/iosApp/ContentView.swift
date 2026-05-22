import UIKit
import SwiftUI
import ComposeApp
import KakaoSDKAuth

struct ComposeView: UIViewControllerRepresentable {
    let inviteCode: String?

    func makeUIViewController(context: Context) -> UIViewController {
        MainViewControllerKt.MainViewController(initialInviteCode: inviteCode)
    }

    func updateUIViewController(_ uiViewController: UIViewController, context: Context) {}
}

struct ContentView: View {
    @State private var inviteCode: String?

    var body: some View {
        ComposeView(inviteCode: inviteCode)
            .id(inviteCode)
            .ignoresSafeArea()
            .onOpenURL { url in
                if AuthApi.isKakaoTalkLoginUrl(url) {
                    _ = AuthController.handleOpenUrl(url: url)
                    return
                }
                inviteCode = url.livvingInviteCode
            }
    }
}

private extension URL {
    var livvingInviteCode: String? {
        if scheme == "livving", host == "join" {
            return pathComponents.dropFirst().first
        }
        if scheme == "https", host == "livving.app", pathComponents.dropFirst().first == "join" {
            return pathComponents.dropFirst(2).first
        }
        return nil
    }
}
