# livving Clean MVI Harness

이 저장소는 Kotlin Compose Multiplatform(Android + iOS) 기반 livving 앱입니다. 모든 코드 작업, 리뷰, 문서화는 아래 규칙을 기본값으로 따릅니다.

## 프로젝트 기준

- Architecture: Clean Architecture + MVI
- UI: Compose Multiplatform
- DI: Koin
- Network: Ktorfit + Ktor Client
- Backend: Supabase Auth / Database / Edge Functions / Cron
- Async: Kotlin Coroutines + Flow
- Notification: FCM Push Notification

## 모듈 의존성 규칙

- Gradle 모듈 의존성은 순환 없는 DAG여야 합니다.
- `domain:livving`은 domain model, repository contract, usecase를 소유합니다.
- `data:network`는 `domain:livving` repository contract를 구현합니다.
- 일반 screen feature는 `core:ui`와 필요한 경우 `domain:livving`에만 의존합니다.
- `:feature:main`은 현재 Navigation 3 앱 셸이므로 screen feature 조립 의존을 예외로 허용합니다.
- `:feature:main` 외 일반 feature는 다른 feature 또는 data 모듈을 직접 참조하지 않습니다.
- `composeApp`은 composition root로서 feature, domain, data, core 구현을 조립합니다.
- `core` 모듈은 feature/domain/data/app을 참조하지 않습니다.

## build-logic 규칙

- 모듈 `build.gradle.kts`에는 `livving.*` convention plugin만 적용합니다.
- AGP, KMP, Compose, KSP, Google Services 등 raw plugin id 또는 plugin version을 모듈에서 직접 선언하지 않습니다.
- 새 feature 모듈이 필요하면 먼저 필요한 convention plugin이 build-logic에 있는지 확인합니다.
- 필요한 convention plugin이 없으면 feature 모듈보다 build-logic 설계를 먼저 제안합니다.

## MVI 규칙

- 화면 비즈니스 상태는 immutable `XxxState` data class로 표현합니다.
- ViewModel 외부 노출은 `StateFlow<XxxState>`만 허용합니다.
- `MutableStateFlow`는 ViewModel 내부 `private`으로만 사용합니다.
- 사용자 입력은 `XxxIntent`로 전달합니다.
- 일회성 navigation, snackbar, permission, toast 등 side-effect는 `Channel<XxxEffect>` 기반 계약을 표준으로 합니다.
- State에 `MutableList`, `MutableMap`, `SnapshotStateList`, `SnapshotStateMap` 등 mutable collection을 포함하지 않습니다.
- collection 안정성은 현재 `List` 유지 + mutable collection 금지를 기본으로 합니다. `kotlinx.collections.immutable` 도입은 별도 승인 후 진행합니다.

## Compose 규칙

- Composable은 state 렌더링과 intent 전달에 집중합니다.
- `remember`와 `rememberSaveable`은 UI local state에만 사용합니다.
- `derivedStateOf`는 입력 변경 빈도보다 실제 UI 업데이트 필요 빈도가 낮은 경우에만 사용합니다.
- `LaunchedEffect`, `DisposableEffect`, `SideEffect`는 생명주기 목적과 key가 명확할 때만 사용합니다.

## Multiplatform 경계 규칙

- `commonMain`에서 Android/iOS 전용 API를 직접 import하지 않습니다.
- `androidMain`에는 Android 구현만 둡니다.
- `iosMain`에는 iOS 구현만 둡니다.
- 플랫폼 기능은 현재 프로젝트 스타일에 맞게 interface 또는 expect/actual 경계로 분리합니다.

## 디자인 시스템 규칙

- 색상, typography, spacing, theme, 공통 UI component는 `core:ui`에서만 정의합니다.
- feature 모듈은 `core:ui` token/component만 사용합니다.
- feature 모듈에서 `Color(...)`, `Brush.*`, `MaterialTheme`, `Typography`, `lightColorScheme` 같은 자체 디자인 토큰을 만들지 않습니다.

## 테스트 규칙

- domain 로직은 JVM unit test를 기본으로 합니다.
- ViewModel/Flow 상태 검증은 `kotlinx-coroutines-test` 기반을 우선 사용합니다.
- data 테스트는 실제 네트워크 대신 fake datasource 또는 fixture를 사용합니다.
- UI screenshot test는 현재 하네스 범위에서 제외합니다.

## 필수 응답 형식

구현 또는 리뷰 완료 시 반드시 아래를 포함합니다.

1. 변경된 파일 diff 요약
2. 각 규칙 준수 근거
3. 변경 전후 의존성 그래프 영향
4. 실행한 테스트/검증 명령과 결과
5. 검증하지 못한 항목
6. 규칙 위반 요청을 받았을 때의 거부 사유와 규칙을 지키는 대안

## 검증 명령

```shell
./scripts/harness/verification-suite.sh
```

규칙만 빠르게 확인하려면 아래 명령을 사용합니다.

```shell
./scripts/harness/clean-mvi-check.sh
```
