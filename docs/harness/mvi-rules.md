# MVI Rules

## Naming

- `XxxViewModel`
- `XxxIntent`
- `XxxState`
- `XxxEffect`

## State

State는 immutable data class여야 합니다.

```kotlin
data class LoginState(
    val loading: Boolean = false,
    val errorMessage: String? = null,
)
```

State에 mutable collection을 포함하지 않습니다. 현재 정책은 `List` 유지 + mutable collection 금지입니다.

## ViewModel

```kotlin
private val _state = MutableStateFlow(LoginState())
val state: StateFlow<LoginState> = _state.asStateFlow()
```

`MutableStateFlow` 외부 노출은 금지합니다.

## Intent

사용자 입력은 `XxxIntent`로 전달합니다.

```kotlin
sealed interface LoginIntent {
    data object ClickKakao : LoginIntent
}
```

## Effect

일회성 이벤트는 `Channel<XxxEffect>`를 표준으로 합니다.

```kotlin
private val effectChannel = Channel<LoginEffect>(Channel.BUFFERED)
val effects: Flow<LoginEffect> = effectChannel.receiveAsFlow()
```

navigation, snackbar, permission 요청, toast 같은 이벤트는 State에 오래 보관하지 않습니다.
