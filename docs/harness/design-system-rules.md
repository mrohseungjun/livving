# Design System Rules

디자인 시스템은 `core:ui`만 소유합니다.

## core-ui 소유 항목

- color token
- typography token
- spacing/radius/elevation token
- common component
- app theme

## feature 금지 항목

- `Color(...)`
- `Brush.*`
- `MaterialTheme` 직접 theme 구성
- `Typography(...)`
- `lightColorScheme(...)`
- `darkColorScheme(...)`

feature에서 새 시각 표현이 필요하면 먼저 `core:ui`에 token/component를 추가한 뒤 사용합니다.
