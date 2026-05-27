#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep(rg)가 필요합니다." >&2
  exit 127
fi

bad_stateflow="$(
  rg -n '^\s*(val|var)\s+[A-Za-z0-9_]+\s*(:[^=]+)?=\s*MutableStateFlow|^\s*(val|var)\s+[A-Za-z0-9_]+\s*:\s*MutableStateFlow' feature core data domain composeApp/src/commonMain/kotlin -g '*ViewModel*.kt' || true
)"
bad_stateflow="$(printf '%s\n' "$bad_stateflow" | rg -v '^\S+:\d+:\s*private\s+' || true)"

bad_state_collections="$(
  rg -l 'data class \w*State\b' feature domain data core composeApp/src/commonMain/kotlin \
    | xargs rg -n '\b(MutableList|MutableMap|MutableSet|ArrayList|HashMap|HashSet|SnapshotStateList|SnapshotStateMap)\b|mutableListOf\(|mutableMapOf\(|mutableSetOf\(' || true
)"

if [[ -n "$bad_stateflow$bad_state_collections" ]]; then
  echo "MVI contract violation" >&2
  [[ -n "$bad_stateflow" ]] && echo "$bad_stateflow" >&2
  [[ -n "$bad_state_collections" ]] && echo "$bad_state_collections" >&2
  exit 1
fi

echo "MVI contract check passed"
