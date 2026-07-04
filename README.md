# Factorio Calculator (KMP)

A Factorio production calculator built with Kotlin Multiplatform and Compose Multiplatform.

**Live web app:** https://hungyanbin.github.io/factorioCalculator-KMP/

## Supported platforms

Single module (`:composeApp`, package `com.yanbin.factoriocalc`) targeting:

| Platform | Entry point | Source set | Run |
|---|---|---|---|
| Desktop (JVM) | `MainKt` | `composeApp/src/desktopMain` | `./gradlew :composeApp:run` |
| Android | `MainActivity.kt` | `composeApp/src/androidMain` | `./gradlew :composeApp:assembleDebug` |
| iOS (x64 / arm64 / simulatorArm64) | `MainViewController.kt` | `composeApp/src/iosMain` | open `iosApp/` in Xcode |
| Web (Kotlin/Wasm) | `main.kt` | `composeApp/src/wasmJsMain` | `./gradlew :composeApp:wasmJsBrowserRun` |

Shared UI and logic live in `composeApp/src/commonMain/kotlin/com/yanbin/factoriocalc/`.

## Building

```
./gradlew :composeApp:run                     # desktop
./gradlew :composeApp:assembleDebug           # Android APK
./gradlew :composeApp:wasmJsBrowserRun        # web dev server
./gradlew :composeApp:allTests                # multiplatform tests
./gradlew build                               # full cross-target build
```

## Web deployment

The web target is deployed to GitHub Pages automatically on every push to `main`
via `.github/workflows/deploy-web.yml`:

1. Build the production Wasm distribution: `./gradlew :composeApp:wasmJsBrowserDistribution`.
2. Upload `composeApp/build/dist/wasmJs/productionExecutable` as a Pages artifact.
3. Deploy that artifact to GitHub Pages (`actions/deploy-pages`).

No manual steps are required — merging to `main` is enough to publish the site.
