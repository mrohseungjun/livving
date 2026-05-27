# kmp-verification

Use this skill before final reporting, commit, or release.

## Inputs

- Changed files
- Intended behavior
- Available Gradle tasks
- Harness script results

## Workflow

1. Run `./scripts/harness/clean-mvi-check.sh`.
2. Run focused Gradle tests/builds based on changed files.
3. Prefer:
   - `./gradlew :domain:livving:jvmTest`
   - `./gradlew :composeApp:assembleDebug`
   - `./gradlew :composeApp:compileKotlinIosSimulatorArm64`
4. Report commands, results, and skipped checks.

## Output

- Diff summary
- Rule compliance
- Dependency graph impact
- Test/verification result
- Unverified items

## Forbidden

- Do not claim tests passed if they were not run.
- Do not omit failing commands.
- Do not bury dependency graph changes.
