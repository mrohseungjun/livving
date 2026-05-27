# kmp-feature-implementation

Use this skill when implementing or modifying a Compose Multiplatform screen or feature.

## Inputs

- Approved plan
- Target feature module
- Required domain usecases
- UI state and user intents

## Workflow

1. Confirm target module dependency boundaries.
2. Keep business state in `XxxState`.
3. Route user input through `XxxIntent`.
4. Expose `StateFlow<XxxState>` from `XxxViewModel`.
5. Use `Channel<XxxEffect>` for one-shot side effects.
6. Render UI using core-ui components and tokens.
7. Keep platform API behind `core:platform`, `composeApp`, or expect/actual boundaries.

## Output

- Implementation summary
- Changed files
- Rule compliance reasoning
- Verification commands

## Forbidden

- Do not expose `MutableStateFlow`.
- Do not add feature-local colors, theme, typography, or spacing tokens.
- Do not import Android/iOS APIs from `commonMain`.
- Do not introduce new dependencies without approval.
