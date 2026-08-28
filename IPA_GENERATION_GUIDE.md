# 📱 iOS IPA Generation for Windows KMP Developers

**Perfect for:** Windows developers with Kotlin Multiplatform projects who want to test their apps on iOS devices.

## 🚀 Quick Start

### Step 1: Generate Your IPA
1. Go to [GitHub Actions](https://github.com/AnuragDev04/KmpTesting/actions/workflows/generate-ios-ipa.yml)
2. Click **"Run workflow"**
3. Fill in your app details:
   ```
   App Name: MyAwesomeApp
   Bundle ID: com.yourname.myapp
   Version: 1.0.0
   ```
4. Click **"Run workflow"**
5. Wait ~15-20 minutes

### Step 2: Download Your IPA
1. Go to the completed workflow run
2. Scroll to **"Artifacts"** section
3. Download `📱 YourAppName-iOS-v1.0.0-buildXXX`
4. Extract ZIP to get your `.ipa` file

### Step 3: Install on iPhone
Choose your method based on your OS:

#### 🪟 **Windows Users (Recommended)**
**Sideloadly** (Free & Easy):
1. Download from [sideloadly.io](https://sideloadly.io/)
2. Connect iPhone via USB
3. Sign in with your Apple ID
4. Drag & drop IPA to install

**3uTools** (Alternative):
1. Download 3uTools
2. Connect iPhone
3. Use "Install App" feature

#### 🍎 **Mac Users**
**Xcode** (If you have Mac access):
1. Window → Devices and Simulators
2. Select your device
3. Click "+" → Choose IPA file

#### 📱 **iOS Users**
**AltStore** (On-device):
1. Install AltStore on iPhone
2. Use AltStore to sideload IPA

## ✨ What You Get

- ✅ **Working iOS IPA** from your KMP project
- ✅ **Unsigned** - No Apple Developer account needed
- ✅ **Sideloadable** - Install on real devices
- ✅ **Automatic** - Triggers on code changes
- ✅ **Customizable** - Your app name, bundle ID, version

## 🔧 Technical Details

### What the Workflow Does:
1. **Builds** your Kotlin Multiplatform shared framework
2. **Compiles** iOS app with your configuration
3. **Creates** proper IPA structure (ZIP with Payload folder)
4. **Uploads** IPA as downloadable artifact

### Requirements:
- ✅ **GitHub repository** with KMP project (you have this)
- ✅ **Internet connection** (GitHub Actions does the work)
- ✅ **Sideloading tool** for installation
- ❌ **No macOS required** (runs on GitHub's macOS runners)
- ❌ **No Apple Developer account required**

### File Output:
- **IPA File:** `YourApp_v1.0.0_TIMESTAMP.ipa`
- **Size:** ~20-50MB (typical)
- **Type:** Unsigned (for sideloading)
- **Compatible:** iOS 13.0+ devices

## 🎯 Perfect For:

- ✅ **Testing** your KMP app on iOS
- ✅ **Demonstrating** to clients/stakeholders  
- ✅ **Development** iterations
- ✅ **Personal use** on your devices

## ❓ FAQ

**Q: Do I need a Mac?**
A: No! GitHub Actions provides macOS runners.

**Q: Do I need Apple Developer account?**
A: No! This creates unsigned IPAs for sideloading.

**Q: Will this work on the App Store?**
A: No. For App Store, you need Apple Developer account and proper signing.

**Q: Is this free?**
A: Yes! Uses GitHub Actions free tier.

**Q: How often can I generate IPAs?**
A: As often as needed. Workflow runs on every code push + manual triggers.

## 🚀 Ready to Start?

**Click here to generate your first IPA:**
👉 [Generate iOS IPA](https://github.com/AnuragDev04/KmpTesting/actions/workflows/generate-ios-ipa.yml)

Your Kotlin Multiplatform iOS app will be ready in ~15 minutes! 🎉