# clean-mvi-architecture

Use this skill when a task touches module structure, build-logic, source sets, project dependencies, or README dependency diagrams.

## Inputs

- User request
- Current Gradle modules
- Target files
- Whether new modules or dependencies are requested

## Workflow

1. Inspect `settings.gradle.kts`.
2. Inspect affected `build.gradle.kts` files.
3. Inspect `build-logic/convention`.
4. Compare project dependencies against `docs/harness/dependency-graph.md`.
5. If a new module is requested, verify a matching convention plugin exists before proposing files.
6. Require README dependency diagram update for new module or dependency graph changes.

## Output

- Allowed or rejected architecture plan
- Dependency graph impact
- build-logic impact
- README update requirement

## Forbidden

- Do not add raw AGP/KMP/Compose/KSP plugin ids to module build files.
- Do not allow general feature modules to depend on data or other feature modules.
- Do not hide circular dependencies.
- Do not create a new feature module before build-logic needs are checked.
