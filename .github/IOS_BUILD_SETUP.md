# iOS Build Setup for GitHub Actions

This document explains how to set up and use the GitHub Actions workflows for building your iOS app.

## Workflows Overview

### 1. `ios-build.yml` - Development Builds
- Triggers on push to `main`/`develop` branches and pull requests
- Builds unsigned IPA files for testing
- Can be manually triggered with Debug/Release configuration choice
- Suitable for development and testing purposes

### 2. `ios-release.yml` - Production Builds
- Triggers on version tags (e.g., `v1.0.0`)
- Builds signed IPA files for distribution
- Supports App Store, Ad Hoc, Enterprise, and Development distribution
- Can upload directly to App Store Connect
- Creates GitHub releases automatically

## Quick Start (Unsigned Builds)

The basic workflow will work immediately without any setup:

1. Push code to `main` or `develop` branch
2. The workflow will automatically build an unsigned IPA
3. Download the artifact from the Actions tab

## Setup for Signed Builds (Required for Distribution)

To build signed IPAs for distribution, you need to configure the following secrets in your GitHub repository:

### Required Secrets

Go to your GitHub repository → Settings → Secrets and Variables → Actions, and add:

#### Basic Certificate Setup
```
IOS_CERTIFICATE_BASE64     # Base64 encoded .p12 certificate file
IOS_CERTIFICATE_PASSWORD   # Password for the .p12 certificate
IOS_TEAM_ID                # Your Apple Developer Team ID
IOS_BUNDLE_ID              # Your app's bundle identifier (e.g., com.yourcompany.appname)
```

#### App Store Connect API (for automatic uploads)
```
APPSTORE_KEY_ID            # App Store Connect API Key ID
APPSTORE_ISSUER_ID         # App Store Connect API Issuer ID
APPSTORE_PRIVATE_KEY       # App Store Connect API Private Key (base64 encoded)
```

#### Provisioning Profile (Alternative to API)
```
IOS_PROVISIONING_PROFILE_BASE64  # Base64 encoded provisioning profile
```

### How to Get These Values

#### 1. Apple Developer Certificate (.p12 file)
1. Open Keychain Access on your Mac
2. Find your iOS Distribution certificate
3. Export it as a .p12 file with a password
4. Convert to base64: `base64 -i certificate.p12 | pbcopy`
5. Use this as `IOS_CERTIFICATE_BASE64`
6. Use the password as `IOS_CERTIFICATE_PASSWORD`

#### 2. Team ID
1. Go to [Apple Developer Portal](https://developer.apple.com/account)
2. Go to Membership section
3. Copy the Team ID

#### 3. Bundle ID
- Use the same bundle identifier as configured in your Xcode project
- Example: `com.yourcompany.careHome`

#### 4. App Store Connect API Keys (Optional, for automatic uploads)
1. Go to [App Store Connect](https://appstoreconnect.apple.com)
2. Users and Access → Keys → App Store Connect API
3. Generate a new key with Developer access
4. Download the .p8 file and convert to base64: `base64 -i AuthKey_XXXXXXXXXX.p8 | pbcopy`

## Usage Instructions

### For Development Builds (Unsigned)
1. Push to `main` or `develop` branch
2. Or manually trigger the "Build iOS App" workflow from Actions tab
3. Download the IPA from the workflow artifacts

### For Release Builds (Signed)
1. Create and push a version tag:
   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```
2. Or manually trigger the "Build and Release iOS App" workflow
3. Choose export method:
   - **development**: For testing on registered devices
   - **ad-hoc**: For testing on specific devices (TestFlight alternative)
   - **app-store**: For App Store submission
   - **enterprise**: For enterprise distribution

### Manual Workflow Triggers
Both workflows can be triggered manually from the Actions tab:
1. Go to your repository on GitHub
2. Click "Actions" tab
3. Select the workflow you want to run
4. Click "Run workflow"
5. Choose your options and click "Run workflow"

## Troubleshooting

### Common Issues

#### Build Fails with Code Signing Errors
- Ensure your certificate and provisioning profile match
- Verify the bundle ID in your Xcode project matches `IOS_BUNDLE_ID` secret
- Check that your certificate hasn't expired

#### Shared Framework Build Fails
- Ensure your KMP shared module builds locally
- Check that all Kotlin/Native dependencies are properly configured
- Verify Gradle wrapper is committed to the repository

#### Archive Export Fails
- Verify your export method matches your provisioning profile type
- For App Store builds, ensure you have a Distribution certificate
- For Ad Hoc builds, ensure devices are registered in your developer account

### Build Logs
If a build fails, you can:
1. Check the workflow logs in the Actions tab
2. Download the build logs artifact (uploaded automatically on failure)
3. Review the specific error messages in the Xcode build output

## Project Structure Requirements

Your project should have this structure:
```
├── shared/                 # KMP shared module
├── androidApp/            # Android app
├── iosApp/                # iOS app
│   ├── iosApp.xcodeproj/  # Xcode project
│   ├── iosApp/            # iOS source files
│   └── Configuration/     # Build configuration
├── .github/
│   └── workflows/         # GitHub Actions workflows
└── gradlew                # Gradle wrapper
```

## Security Notes

- Never commit certificates or private keys to your repository
- Use GitHub repository secrets for all sensitive information
- Regularly rotate your App Store Connect API keys
- Consider using environment-specific certificates for different build types

## Additional Configuration

### Customizing Build Settings
You can modify the workflow files to:
- Change iOS deployment target
- Add custom build phases
- Include additional testing steps
- Configure different Xcode versions
- Add notification webhooks

### Environment Variables
Both workflows use these environment variables that can be customized:
- `XCODE_VERSION`: Xcode version to use (default: '15.0')
- `IOS_DEPLOYMENT_TARGET`: Minimum iOS version (default: '13.0')

## Support

If you encounter issues:
1. Check the GitHub Actions logs for detailed error messages
2. Verify all secrets are correctly configured
3. Ensure your local Xcode project builds successfully
4. Check that your certificates and provisioning profiles are valid