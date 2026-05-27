#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

./scripts/harness/clean-mvi-check.sh >/tmp/livving-module-graph-check.log
rg 'Clean 계층 의존성 방향이 허용된 범위 안에 있습니다.' /tmp/livving-module-graph-check.log >/dev/null
cat /tmp/livving-module-graph-check.log
