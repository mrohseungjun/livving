#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep(rg)가 필요합니다." >&2
  exit 127
fi

bad="$(rg -n 'import androidx\.compose\.ui\.graphics\.Color|Color\(|Brush\.|lightColorScheme\(|darkColorScheme\(|Typography\(' feature || true)"

if [[ -n "$bad" ]]; then
  echo "feature 모듈에서 자체 디자인 토큰을 사용했습니다." >&2
  echo "$bad" >&2
  exit 1
fi

echo "design system check passed"
