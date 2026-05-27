#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/../.." && pwd)"
cd "$ROOT_DIR"

./scripts/harness/clean-mvi-check.sh
./gradlew :domain:livving:jvmTest :composeApp:assembleDebug :composeApp:compileKotlinIosSimulatorArm64
