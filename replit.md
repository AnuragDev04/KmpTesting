# CareHome project notes

## Current setup

- This is a Kotlin Multiplatform project with `androidApp`, `sharedLogic`,
  `sharedUI`, and an iOS/Xcode host.
- The product UI intentionally remains Android-only in
  `sharedUI/src/androidMain`; `sharedUI` is retained as the module boundary,
  but no Compose UI is shared to `commonMain`.
- The Android app targets SDK 36 and requires a local Android SDK plus Java 11
  compatibility.

## Run the Android build

From the project root:

```bash
./gradlew :androidApp:assembleDebug
```

The imported Replit Linux environment currently does not include an Android
SDK, so the build cannot run here until an Android SDK is provided. Android
Studio with API 36 is the supported local setup. The iOS host requires
macOS/Xcode and is not buildable in this Linux environment.

The optional Gemini advisor also requires `GEMINI_API_KEY`; see
`.env.example`. Do not commit the key.