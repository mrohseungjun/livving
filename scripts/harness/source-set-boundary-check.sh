#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

if ! command -v rg >/dev/null 2>&1; then
  echo "ripgrep(rg)가 필요합니다." >&2
  exit 127
fi

common_bad="$(rg -n 'import android\.|import platform\.|com\.google\.firebase|com\.kakao\.sdk' . --glob '*/commonMain/**/*.kt' || true)"
android_bad="$(rg -n 'platform\.UIKit|platform\.Foundation|cocoapods|UIApplication|NSURL|UNUserNotificationCenter' . --glob '*/androidMain/**/*.kt' || true)"
ios_bad="$(rg -n 'android\.|com\.google\.firebase|com\.kakao\.sdk|androidx\.activity' . --glob '*/iosMain/**/*.kt' || true)"

if [[ -n "$common_bad$android_bad$ios_bad" ]]; then
  echo "source set boundary violation" >&2
  [[ -n "$common_bad" ]] && echo "$common_bad" >&2
  [[ -n "$android_bad" ]] && echo "$android_bad" >&2
  [[ -n "$ios_bad" ]] && echo "$ios_bad" >&2
  exit 1
fi

echo "source set boundary check passed"
