# kmp-mvi-review

Use this skill for review of ViewModel, State, Intent, Effect, and Compose state handling.

## Inputs

- Diff or target files
- Related ViewModel/State/Intent files
- Screen Composables

## Review Checklist

- `XxxState` is an immutable data class.
- State contains no mutable collections.
- `MutableStateFlow` is private.
- ViewModel exposes `StateFlow<XxxState>`.
- User actions are represented as `XxxIntent`.
- One-shot side effects use `Channel<XxxEffect>`.
- Composables render state and send intents.
- `remember` is UI-local only.
- `LaunchedEffect` keys and purpose are clear.

## Output

- Findings ordered by severity
- Minimal fix suggestions
- Residual risk

## Forbidden

- Do not approve stateful business logic inside Composables.
- Do not approve ViewModel mutable state exposure.
- Do not approve long-lived one-shot events stored in State without explicit reason.
