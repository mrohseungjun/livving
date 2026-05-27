#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep(rg)가 필요합니다." >&2
  exit 127
fi

bad="$(
  rg --files -g 'build.gradle.kts' \
    | grep -v '^build.gradle.kts$' \
    | grep -v '^build-logic/' \
    | xargs rg -n 'id\("(com\.android|org\.jetbrains|com\.google\.devtools|com\.google\.gms)|kotlin\("|alias\(libs\.plugins' || true
)"

if [[ -n "$bad" ]]; then
  echo "모듈 build 파일에서 build-logic 컨벤션이 아닌 플러그인을 직접 선언했습니다." >&2
  echo "$bad" >&2
  exit 1
fi

echo "build-logic check passed"
