# Compose Rules

- Composable은 state 렌더링과 intent 전달에 집중합니다.
- business state는 ViewModel의 `StateFlow<XxxState>`가 단일 진실 공급원입니다.
- `remember`와 `rememberSaveable`은 UI local state에만 사용합니다.
- `derivedStateOf`는 입력 변경 빈도가 실제 UI 업데이트 필요 빈도보다 높은 경우에만 사용합니다.
- `LaunchedEffect`는 effect collection, navigation bridge, lifecycle-bound 작업처럼 목적과 key가 명확할 때만 사용합니다.
- feature 화면은 core-ui component/token을 사용하고 자체 theme/token을 만들지 않습니다.
