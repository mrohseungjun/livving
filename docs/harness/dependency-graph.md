# Dependency Graph

현재 Gradle 모듈 그래프 기준입니다.

```text
composeApp
 ├─ core:platform
 ├─ core:ui
 ├─ data:network
 ├─ data:supabase
 ├─ domain:livving
 └─ feature:main

data:network
 └─ external Ktor/Ktorfit/Supabase libraries

data:supabase
 ├─ core:platform
 ├─ data:network
 └─ domain:livving

feature:main
 ├─ core:platform
 ├─ core:ui
 ├─ domain:livving
 ├─ feature:auth
 ├─ feature:home
 ├─ feature:notifications
 ├─ feature:relations
 ├─ feature:splash
 ├─ feature:settings
 └─ feature:setup

feature:auth
 ├─ core:ui
 └─ domain:livving

feature:relations
 ├─ core:ui
 └─ domain:livving

feature:home / notifications / settings / setup / splash
 └─ core:ui
```

## 허용 예외

`:feature:main`은 현재 Navigation 3 앱 셸이므로 screen feature 의존을 허용합니다. 이 예외는 신규 screen feature가 `:feature:main`을 참조해도 된다는 뜻이 아닙니다.

## 금지

- 일반 feature -> data
- 일반 feature -> 다른 feature
- domain -> data
- core -> domain/data/feature/composeApp
- data -> feature/composeApp
