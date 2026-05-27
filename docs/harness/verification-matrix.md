# Verification Matrix

## 빠른 규칙 검증

```shell
./scripts/harness/clean-mvi-check.sh
```

## 엄격한 domain 테스트 검증

```shell
STRICT_DOMAIN_TESTS=1 ./scripts/harness/clean-mvi-check.sh
```

## 전체 하네스 검증

```shell
./scripts/harness/verification-suite.sh
```

## Gradle 검증 후보

```shell
./gradlew :domain:livving:jvmTest
./gradlew :composeApp:assembleDebug
./gradlew :composeApp:compileKotlinIosSimulatorArm64
```

## 리뷰 출력 요구사항

모든 구현/리뷰 완료 보고는 아래를 포함합니다.

1. 변경된 파일 diff 요약
2. 규칙 준수 근거
3. 의존성 그래프 영향
4. 실행한 테스트/검증 명령과 결과
5. 검증하지 못한 항목
6. 규칙 위반 요청 거부 사유와 대안
