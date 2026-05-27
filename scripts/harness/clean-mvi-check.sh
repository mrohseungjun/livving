#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

failures=0

fail() {
  failures=$((failures + 1))
  printf '✗ %s\n' "$1" >&2
}

pass() {
  printf '✓ %s\n' "$1"
}

require_rg() {
  if ! command -v rg >/dev/null 2>&1; then
    echo "ripgrep(rg)가 필요합니다." >&2
    exit 127
  fi
}

module_build_files() {
  rg --files -g 'build.gradle.kts' \
    | grep -v '^build.gradle.kts$' \
    | grep -v '^build-logic/'
}

check_build_logic_only() {
  local bad
  bad="$(module_build_files | xargs rg -n 'id\("(com\.android|org\.jetbrains|com\.google\.devtools|com\.google\.gms)|kotlin\("|alias\(libs\.plugins' || true)"
  if [[ -n "$bad" ]]; then
    fail "모듈 build 파일에서 build-logic 컨벤션이 아닌 플러그인을 직접 선언했습니다."
    echo "$bad" >&2
    return
  fi
  pass "모듈 build 파일은 build-logic 컨벤션 플러그인만 사용합니다."
}

check_dependency_direction() {
  local bad=""
  local file
  while IFS= read -r file; do
    case "$file" in
      core/*)
        bad+="$(
          rg -n 'projects\.(feature|domain|data|composeApp)' "$file" || true
        )"
        ;;
      domain/*)
        bad+="$(
          rg -n 'projects\.' "$file" || true
        )"
        ;;
      data/*)
        bad+="$(
          rg -n 'projects\.(feature|composeApp)' "$file" || true
        )"
        ;;
      feature/main/*)
        bad+="$(
          rg -n 'projects\.(data|composeApp)' "$file" || true
        )"
        ;;
      feature/*)
        bad+="$(
          rg -n 'projects\.(data|composeApp|feature\.main)' "$file" || true
        )"
        ;;
    esac
  done < <(module_build_files)

  if [[ -n "$bad" ]]; then
    fail "Clean 계층 의존성 방향을 벗어난 project dependency가 있습니다."
    echo "$bad" >&2
    return
  fi
  pass "Clean 계층 의존성 방향이 허용된 범위 안에 있습니다."
}

check_viewmodel_stateflow() {
  local bad
  bad="$(rg -n '^\s*(val|var)\s+[A-Za-z0-9_]+\s*(:[^=]+)?=\s*MutableStateFlow|^\s*(val|var)\s+[A-Za-z0-9_]+\s*:\s*MutableStateFlow' feature core data domain composeApp/src/commonMain/kotlin -g '*ViewModel*.kt' || true)"
  bad="$(printf '%s\n' "$bad" | rg -v '^\S+:\d+:\s*private\s+' || true)"
  if [[ -n "$bad" ]]; then
    fail "ViewModel에서 MutableStateFlow를 외부로 노출했습니다."
    echo "$bad" >&2
    return
  fi
  pass "ViewModel은 MutableStateFlow를 private으로만 사용합니다."
}

check_state_immutability() {
  local state_files
  local bad=""
  state_files="$(rg -l 'data class \w*State\b' feature domain data core composeApp/src/commonMain/kotlin || true)"
  if [[ -n "$state_files" ]]; then
    bad="$(printf '%s\n' "$state_files" | xargs rg -n '\b(MutableList|MutableMap|MutableSet|ArrayList|HashMap|HashSet|SnapshotStateList|SnapshotStateMap)\b|mutableListOf\(|mutableMapOf\(|mutableSetOf\(' || true)"
  fi

  if [[ -n "$bad" ]]; then
    fail "State에 unstable mutable 컬렉션 또는 snapshot 컬렉션이 포함되어 있습니다."
    echo "$bad" >&2
    return
  fi
  pass "State는 mutable 컬렉션 없이 선언되어 있습니다."
}

check_feature_uses_core_ui_tokens() {
  local bad
  bad="$(rg -n 'import androidx\.compose\.ui\.graphics\.Color|Color\(|Brush\.|lightColorScheme\(|darkColorScheme\(|Typography\(' feature || true)"
  if [[ -n "$bad" ]]; then
    fail "feature 모듈에서 자체 색상/테마 토큰을 선언하거나 직접 사용했습니다."
    echo "$bad" >&2
    return
  fi
  pass "feature 모듈은 색상/테마 토큰을 core-ui에 위임합니다."
}

check_platform_boundaries() {
  local bad_android
  local bad_ios
  bad_android="$(rg -n 'platform\.UIKit|platform\.Foundation|cocoapods|UIApplication|NSURL|UNUserNotificationCenter' . --glob '*/androidMain/**' || true)"
  bad_ios="$(rg -n 'android\.|com\.google\.firebase|com\.kakao\.sdk|androidx\.activity' . --glob '*/iosMain/**' || true)"

  if [[ -n "$bad_android$bad_ios" ]]; then
    fail "플랫폼 sourceSet 경계가 깨졌습니다."
    [[ -n "$bad_android" ]] && echo "$bad_android" >&2
    [[ -n "$bad_ios" ]] && echo "$bad_ios" >&2
    return
  fi
  pass "Android/iOS sourceSet 경계가 유지됩니다."
}

check_domain_tests() {
  local strict="${STRICT_DOMAIN_TESTS:-0}"
  local missing=()
  local usecase
  while IFS= read -r usecase; do
    local name
    name="$(basename "$usecase" .kt)"
    if [[ "$name" == *UseCase && ! -f "domain/livving/src/jvmTest/kotlin/kr/osj/livving/domain/livving/usecase/${name}Test.kt" ]]; then
      missing+=("$name")
    fi
  done < <(find domain/livving/src/commonMain/kotlin/kr/osj/livving/domain/livving/usecase -name '*UseCase.kt' -type f)

  if (( ${#missing[@]} > 0 )); then
    if [[ "$strict" == "1" ]]; then
      fail "domain UseCase JVM 테스트가 아직 없는 항목: ${missing[*]}"
      return
    fi
    printf '△ domain UseCase JVM 테스트가 아직 없는 항목: %s\n' "${missing[*]}"
    return
  fi
  pass "domain UseCase JVM 테스트 파일이 모두 존재합니다."
}

main() {
  require_rg
  check_build_logic_only
  check_dependency_direction
  check_viewmodel_stateflow
  check_state_immutability
  check_feature_uses_core_ui_tokens
  check_platform_boundaries
  check_domain_tests

  if (( failures > 0 )); then
    echo "Clean MVI 하네스 실패: ${failures}개 규칙 위반" >&2
    exit 1
  fi

  echo "Clean MVI 하네스 통과"
}

main "$@"
