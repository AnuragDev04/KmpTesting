<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# CareHome Kotlin Multiplatform project

CareHome is now organized using the Kotlin Multiplatform structure from the
supplied `KmpTest` project. The existing Android experience remains the
product implementation while platform-independent code can be extracted into
the shared source sets incrementally.

View your app in AI Studio: https://ai.studio/apps/e22b1b6d-9662-444c-9cf1-9fe45cbee0ba

## Project structure

- `androidApp` — Android launcher, manifest, app resources, and Android tests.
- `sharedUI` — shared UI module boundary; the current Compose implementation
  is in `androidMain` because it still uses Android navigation, resources,
  intents, and Coil.
- `sharedLogic` — shared logic module boundary, Android data implementation,
  Room persistence, and `commonMain`/platform samples ready for extraction.
- `iosApp` — Xcode host application connected to the shared framework.

## Run Android locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Install Android Studio and an Android SDK (API 36).
2. Open the project root in Android Studio.
3. Allow Android Studio to sync the Gradle project.
4. Create `.env` in the project root and set `GEMINI_API_KEY` (see
   `.env.example`) if you want the AI advisor to use Gemini.
5. Run the `androidApp` configuration on an emulator or physical device.

The iOS host is under `iosApp` and requires macOS/Xcode to build. iOS targets
are declared in both KMP modules; the Linux environment cannot compile them.
