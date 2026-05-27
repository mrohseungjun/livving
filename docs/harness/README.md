# livving Clean MVI Harness

이 디렉터리는 livving의 Clean Architecture + MVI 개발 하네스 문서를 보관합니다.

## 핵심 결정

- `:feature:main`은 현재 Navigation 3 앱 셸/aggregator로 공식 예외 허용합니다.
- 일반 screen feature는 다른 feature 또는 data 모듈을 직접 참조하지 않습니다.
- `core-network`, `core-database`, `core-model`은 지금 당장 추가하지 않습니다.
- State collection은 현재 `List`를 유지하고 mutable collection만 금지합니다.
- 일회성 side-effect 표준은 `Channel<XxxEffect>`입니다.
- domain UseCase 테스트 누락은 기본 warning, `STRICT_DOMAIN_TESTS=1`일 때 failure입니다.

## 문서 구성

- [`architecture-rules.md`](./architecture-rules.md): 모듈, build-logic, source set 규칙
- [`mvi-rules.md`](./mvi-rules.md): State, Intent, ViewModel, Effect 규칙
- [`compose-rules.md`](./compose-rules.md): Compose state와 side-effect 규칙
- [`design-system-rules.md`](./design-system-rules.md): core-ui 경계 규칙
- [`verification-matrix.md`](./verification-matrix.md): 검증 명령과 책임 매트릭스
- [`dependency-graph.md`](./dependency-graph.md): 현재 의존성 그래프와 허용 예외

## 실행

```shell
./scripts/harness/clean-mvi-check.sh
./scripts/harness/verification-suite.sh
```
