# Architecture Rules

## build-logic

모듈 build 파일은 `livving.*` convention plugin만 적용합니다. raw AGP/KMP/Compose/KSP plugin id 또는 version 직접 선언은 금지합니다.

## 신규 모듈

새 feature 모듈을 만들 때는 아래 순서로 진행합니다.

1. 필요한 convention plugin 존재 여부 확인
2. 모듈 의존성 방향 설계
3. README dependency diagram 갱신 계획 수립
4. 구현
5. 하네스 실행

## Source Set

- `commonMain`: 공통 Kotlin/Compose 코드만 둡니다.
- `androidMain`: Android SDK, Firebase Android, Kakao Android 구현만 둡니다.
- `iosMain`: UIKit/Foundation/iOS bridge 구현만 둡니다.
- 플랫폼 기능은 interface 또는 expect/actual 경계로 분리합니다.
