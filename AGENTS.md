# AGENTS.md — factorioCalculatorKMP

Guidance for AI agents working in this repository. Read this before making changes.

## Project

Kotlin Multiplatform + Compose Multiplatform app (a Factorio production calculator).
Single module: **`:composeApp`**, package `com.yanbin.factoriocalc`.

Targets and entry points:

| Target | Entry point | Source set |
|---|---|---|
| Desktop (JVM, named `desktop`) | `com.yanbin.factoriocalc.MainKt` | `composeApp/src/desktopMain` |
| Android | `MainActivity.kt` | `composeApp/src/androidMain` |
| iOS (x64 / arm64 / simulatorArm64) | `MainViewController.kt` | `composeApp/src/iosMain` |
| Web (wasmJs) | `main.kt` | `composeApp/src/wasmJsMain` |

Shared UI/logic lives in `composeApp/src/commonMain/kotlin/com/yanbin/factoriocalc/` —
`App.kt` is the root composable; `ui/` holds composables, `domain/` holds game/asset logic.
**Put changes in `commonMain` unless a change is genuinely platform-specific.**

## Toolchain (pinned — do not bump casually)

Versions in `gradle/libs.versions.toml`:

- Kotlin **2.2.20**, Compose Multiplatform **1.10.3**, AGP 8.5.2, compileSdk 34 / minSdk 24
- Compose Hot Reload **1.2.0-beta01** (plugin `org.jetbrains.compose.hot-reload`)

Constraints: project must run on **Java 21 or earlier**; Compose Hot Reload requires the
**JetBrains Runtime** (auto-provisioned via `compose.reload.jbr.autoProvisioningEnabled=true`
in `gradle.properties`). Kotlin and Compose MP are coupled — Compose MP 1.10.x needs Kotlin 2.2.x.
Bumping one usually forces the other; verify against the Compose↔Kotlin compatibility table first.

## Compose Hot Reload MCP (primary dev loop for UI work)

This repo has a Model Context Protocol server for driving the **running desktop app** —
screenshot it, inspect/click UI, and see code edits apply live without a restart.
Config: `.mcp.json` → task `:composeApp:hotMcpServerDesktop`.

**Use it whenever you change UI and want to verify the result visually.** Workflow:

1. Ensure the app is running with hot reload (creates the PID file the MCP attaches to):
   ```
   ./gradlew :composeApp:hotRunDesktop
   ```
2. The MCP server (`hotMcpServerDesktop`) is spawned by your MCP client over stdio — it is
   **not** run manually. It attaches to the running app via
   `composeApp/build/run/desktopMain/desktopMain.pid`.
3. Edit composables (usually in `commonMain`). Saved changes **hot-reload into the live window** —
   no rebuild/restart. Then use the MCP tools (screenshot / inspect / click / scroll) to confirm.

Notes:
- Hot reload handles composable body edits live; changing top-level declarations, adding
  dependencies, or non-Compose code may require restarting `hotRunDesktop`.
- CHR 1.2.0 and its MCP are **experimental (beta)** — if the MCP misbehaves, restart the app.

## Build / run / test

```
./gradlew :composeApp:hotRunDesktop          # desktop, hot reload (preferred for dev)
./gradlew :composeApp:run                     # desktop, plain run
./gradlew :composeApp:wasmJsBrowserRun        # web (wasmJs) dev server
./gradlew :composeApp:assembleDebug           # Android APK
./gradlew :composeApp:compileKotlinDesktop    # fast source check for the migration/target
./gradlew :composeApp:allTests                # multiplatform tests (commonTest)
./gradlew build                               # full cross-target build
```

After changes that touch shared code, prefer a quick `compileKotlinDesktop` (fast) and run
`allTests` before declaring done. Web is deployed via `.github/workflows/deploy-web.yml`, so
keep the `wasmJs` target compiling.

## Conventions

- Kotlin official code style (`kotlin.code.style=official`).
- Compose resources are generated (`com.yanbin.factoriocalc.resources`, `generateResClass = always`);
  reference them through the generated `Res` class, don't hardcode paths.
- Don't commit or push unless asked. This repo's default branch is `main`.
