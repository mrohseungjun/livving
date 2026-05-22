import ComposeApp
import Foundation
import KakaoSDKAuth
import KakaoSDKUser

final class KakaoAuthClientBridge: NSObject, LivvingKakaoAuthClient {
    func login(completionHandler: @escaping (LivvingKakaoLoginToken?, Error?) -> Void) {
        let tokenHandler: (OAuthToken?, Error?) -> Void = { token, error in
            if let error {
                completionHandler(nil, error)
                return
            }

            guard let token else {
                completionHandler(nil, KakaoAuthError.missingToken)
                return
            }

            guard let idToken = token.idToken, !idToken.isEmpty else {
                completionHandler(nil, KakaoAuthError.missingIdToken)
                return
            }

            completionHandler(
                LivvingKakaoLoginToken(
                    accessToken: token.accessToken,
                    idToken: idToken
                ),
                nil
            )
        }

        if UserApi.isKakaoTalkLoginAvailable() {
            UserApi.shared.loginWithKakaoTalk { token, error in
                if error != nil {
                    UserApi.shared.loginWithKakaoAccount(completion: tokenHandler)
                    return
                }

                tokenHandler(token, nil)
            }
        } else {
            UserApi.shared.loginWithKakaoAccount(completion: tokenHandler)
        }
    }

    func logout(completionHandler: @escaping (Error?) -> Void) {
        UserApi.shared.logout { error in
            completionHandler(error)
        }
    }
}

enum KakaoAuthError: LocalizedError {
    case missingToken
    case missingIdToken

    var errorDescription: String? {
        switch self {
        case .missingToken:
            return "카카오 로그인 토큰을 받을 수 없습니다."
        case .missingIdToken:
            return "카카오 OpenID Connect id_token을 받을 수 없습니다."
        }
    }
}
