# CareHome iOS

This folder contains the native SwiftUI host for CareHome.

## Run in Xcode

1. Open `iosApp.xcodeproj` on macOS with Xcode 16 or newer.
2. Set a development team in the `iosApp` target if code signing is required.
3. Select an iPhone simulator and run the `iosApp` scheme.

The Xcode build phase runs
`:sharedLogic:embedAndSignAppleFrameworkForXcode` from the repository root,
so the host links the generated `SharedLogic` framework before compiling the
SwiftUI app.

## Included flows

- Sign in and persisted session state
- Home dashboard and popular services
- Searchable service catalog and service details
- Date/address selection and simulated secure payment confirmation
- Booking list, detail view, nurse assignment timeline, and call action
- Native nurse profile, visit tracking, and notification center
- Profile, sign out, support ticket form, and support call action
- AI care advisor with loading, guidance, and safety-disclaimer states

The app keeps booking/session data locally with `UserDefaults` so the flows
remain usable without a backend. Replace the store methods with the production
API and shared persistence layer when the backend contract is available.