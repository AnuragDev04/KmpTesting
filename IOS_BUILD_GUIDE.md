# iOS Build Guide

This guide explains how to build iOS apps from this Kotlin Multiplatform project using GitHub Actions.

## 🚀 Quick Start

### Unsigned Builds (CI/Testing)
The `ios-build.yml` workflow creates unsigned builds for testing purposes:
- Builds the KMP shared framework
- Compiles iOS app for simulator and device
- Creates .xcarchive file
- No code signing required

**Trigger:** Push to main/develop or manual trigger

### Signed Builds (Distribution)
The `ios-release.yml` workflow creates signed IPAs for distribution:
- Requires iOS certificates and provisioning profiles
- Creates signed .ipa files ready for distribution
- Supports App Store, Ad Hoc, and Development exports

**Trigger:** Git tags (v*.*.*) or manual trigger

## 🔧 Setup for Signed Builds

To create signed IPAs, you need to configure the following GitHub Secrets:

### Required Secrets
```
IOS_CERTIFICATE_BASE64     # Base64 encoded .p12 certificate
IOS_CERTIFICATE_PASSWORD   # Password for .p12 certificate
IOS_PROVISIONING_PROFILE_BASE64  # Base64 encoded .mobileprovision file
IOS_BUNDLE_ID              # Your app's bundle identifier
IOS_TEAM_ID                # Apple Developer Team ID
APPSTORE_ISSUER_ID         # App Store Connect API Issuer ID
APPSTORE_KEY_ID            # App Store Connect API Key ID  
APPSTORE_PRIVATE_KEY       # App Store Connect API Private Key
```

### How to Get Certificates

1. **Create Certificate in Apple Developer Console**
   - Go to developer.apple.com
   - Certificates, Identifiers & Profiles
   - Create iOS Distribution Certificate
   - Download and export as .p12

2. **Base64 Encode Certificate**
   ```bash
   base64 -i certificate.p12 | pbcopy
   ```

3. **Create Provisioning Profile**
   - Create App ID with your bundle identifier
   - Create Provisioning Profile (Distribution)
   - Download .mobileprovision file
   - Base64 encode: `base64 -i profile.mobileprovision | pbcopy`

## 📱 Workflow Outputs

### Unsigned Build (`ios-build.yml`)
- **Output**: iOS Archive (`.xcarchive`)
- **Use Case**: CI verification, testing compilation
- **Artifacts**: `ios-archive-{run-number}`

### Signed Build (`ios-release.yml`)  
- **Output**: Signed IPA (`.ipa`)
- **Use Case**: Distribution, App Store submission
- **Artifacts**: `ios-release-{run-number}`

## 🛠 Local Development

### Prerequisites
- macOS with Xcode installed
- Java 17 installed
- Kotlin Multiplatform project

### Build Commands
```bash
# Build shared framework
./gradlew :sharedLogic:assembleXCFramework

# Build iOS app (from iosApp directory)
cd iosApp
xcodebuild -project iosApp.xcodeproj -scheme iosApp -configuration Debug -destination 'generic/platform=iOS Simulator' build
```

## 🐛 Common Issues

### Exit Code 70 (Export Failed)
**Cause**: Code signing issues during IPA export
**Solution**: 
- For CI: Use unsigned builds (current workflow)
- For distribution: Configure proper certificates

### Missing Scheme Error
**Cause**: Xcode scheme not shared or not found
**Solution**: 
- Open project in Xcode
- Go to Product → Scheme → Manage Schemes
- Check "Shared" for iosApp scheme

### Framework Not Found
**Cause**: SharedLogic framework not built
**Solution**: Run `./gradlew :sharedLogic:assembleXCFramework` first

## 📋 Workflow Comparison

| Feature | `ios-build.yml` | `ios-release.yml` |
|---------|----------------|------------------|
| Code Signing | ❌ No | ✅ Yes |
| Output Format | .xcarchive | .ipa |
| Use Case | CI/Testing | Distribution |
| Certificates Required | ❌ No | ✅ Yes |
| App Store Ready | ❌ No | ✅ Yes |

## 🎯 Next Steps

1. **For Testing**: The current unsigned workflow should work out of the box
2. **For Distribution**: Configure the secrets above and use `ios-release.yml`
3. **For App Store**: Set up App Store Connect API keys for automated upload

The current fix resolves the exit code 70 error by avoiding problematic code signing in CI environments while maintaining the ability to create proper signed builds when certificates are configured.