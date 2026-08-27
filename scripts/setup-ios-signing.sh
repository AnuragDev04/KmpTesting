#!/bin/bash
# iOS Signing Setup Script
# Run this script to help set up iOS certificates for GitHub Actions

echo "🍎 iOS Certificate Setup Helper"
echo "=============================="
echo ""

# Check if we're on macOS
if [[ "$OSTYPE" != "darwin"* ]]; then
    echo "⚠️  This script is designed for macOS."
    echo "   For Windows/Linux, you'll need to use a Mac or use online tools."
    echo ""
    echo "📖 Alternative approaches:"
    echo "   1. Use GitHub Codespaces with macOS runner"
    echo "   2. Ask someone with a Mac to help generate certificates"
    echo "   3. Use online iOS certificate generators (be cautious!)"
    echo ""
    exit 1
fi

echo "📋 Prerequisites Check"
echo "---------------------"

# Check for Xcode
if ! command -v xcodebuild &> /dev/null; then
    echo "❌ Xcode not found. Please install Xcode from App Store."
    exit 1
else
    echo "✅ Xcode found: $(xcodebuild -version | head -1)"
fi

# Check for security command
if ! command -v security &> /dev/null; then
    echo "❌ Security tools not found."
    exit 1
else
    echo "✅ Security tools available"
fi

echo ""
echo "🔐 Certificate Information"
echo "-------------------------"

# List available certificates
echo "Available signing certificates:"
security find-identity -v -p codesigning | grep "iPhone\|iOS\|Apple Development\|Apple Distribution" || echo "❌ No iOS certificates found"

echo ""
echo "📱 Bundle ID Configuration"
echo "--------------------------"

read -p "Enter your desired Bundle ID (e.g., com.yourname.careHome): " BUNDLE_ID
if [[ -z "$BUNDLE_ID" ]]; then
    BUNDLE_ID="com.example.careHome"
    echo "Using default: $BUNDLE_ID"
fi

echo ""
echo "🚀 GitHub Actions Setup"
echo "----------------------"

echo "To enable automatic IPA generation, you need these GitHub Secrets:"
echo ""
echo "Repository Secrets to Add:"
echo "- IOS_BUNDLE_ID = '$BUNDLE_ID'"

if security find-identity -v -p codesigning | grep -q "iPhone\|iOS\|Apple Development\|Apple Distribution"; then
    echo ""
    echo "✅ You have iOS certificates! Here's what to do:"
    echo ""
    echo "1. Export your certificate as .p12:"
    echo "   - Open Keychain Access"
    echo "   - Find your iOS certificate"
    echo "   - Right-click → Export"
    echo "   - Save as .p12 file with password"
    echo ""
    echo "2. Base64 encode the .p12 file:"
    echo "   base64 -i YourCertificate.p12 | pbcopy"
    echo ""
    echo "3. Add to GitHub Secrets:"
    echo "   - IOS_CERTIFICATE_BASE64 = (paste the base64 string)"
    echo "   - IOS_CERTIFICATE_PASSWORD = (your .p12 password)"
    echo ""
    echo "4. For provisioning profiles:"
    echo "   - Download from developer.apple.com"
    echo "   - Base64 encode: base64 -i YourProfile.mobileprovision | pbcopy"
    echo "   - Add as IOS_PROVISIONING_PROFILE_BASE64"
    
else
    echo ""
    echo "❌ No iOS certificates found."
    echo ""
    echo "🛠️  To create certificates:"
    echo "1. Get Apple Developer Account ($99/year)"
    echo "2. Go to developer.apple.com"
    echo "3. Certificates, Identifiers & Profiles"
    echo "4. Create iOS App Development/Distribution certificate"
    echo "5. Download and install in Keychain"
    echo ""
    echo "Alternative: Use automatic signing in GitHub Actions"
    echo "(May work without certificates for testing)"
fi

echo ""
echo "🎯 Quick Test"
echo "------------"

echo "After setting up certificates, test with:"
echo "1. Push code to GitHub"
echo "2. Go to Actions → 'Generate iOS IPA File'"  
echo "3. Click 'Run workflow'"
echo "4. Select signing method and export type"
echo "5. Download IPA from artifacts"

echo ""
echo "📖 For detailed instructions, see: IOS_BUILD_GUIDE.md"
echo ""
echo "✨ Setup complete! Good luck with your iOS build! 🚀"