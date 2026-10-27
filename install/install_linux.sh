#!/bin/sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"

fail() {
    echo "Error: $1"
    exit 1
}

fetch_text() {
    url="$1"
    if command -v curl >/dev/null 2>&1; then
        curl -fsSL "$url"
    elif command -v wget >/dev/null 2>&1; then
        wget -qO- "$url"
    else
        fail "Neither curl nor wget is installed. Please install one of them and retry."
    fi
}

download_file() {
    url="$1"
    output="$2"
    if command -v curl >/dev/null 2>&1; then
        curl -fL --retry 3 -o "$output" "$url"
    elif command -v wget >/dev/null 2>&1; then
        wget -O "$output" "$url"
    else
        fail "Neither curl nor wget is installed. Please install one of them and retry."
    fi
}

if [ ! -f "platform-tools/adb" ]; then
    echo "Downloading and unzipping Android Platform Tools. Please wait..."
    if ! command -v unzip >/dev/null 2>&1; then
        fail "unzip is required but not installed."
    fi
    download_file "https://dl.google.com/android/repository/platform-tools-latest-linux.zip" "platform-tools-latest-linux.zip"
    unzip -o platform-tools-latest-linux.zip >/dev/null
fi

if [ ! -x "platform-tools/adb" ]; then
    fail "platform-tools/adb was not found after setup."
fi

APK_FILE=""
# Check for a local APK file first
for f in *.apk; do
    if [ -f "$f" ]; then
        APK_FILE="$f"
        echo "Using local APK: $APK_FILE"
        break
    fi
done

if [ -z "$APK_FILE" ]; then
    echo "Checking for latest release on GitHub..."
    RELEASE_JSON=$(fetch_text "https://api.github.com/repos/mizan989/getabreak-focus_app/releases/latest" 2>/dev/null || true)
    APK_URL=$(
        echo "$RELEASE_JSON" \
            | grep -Eo '"browser_download_url":[[:space:]]*"[^"]*\.apk"' \
            | head -n 1 \
            | sed -E 's/^"browser_download_url":[[:space:]]*"([^"]+)"$/\1/'
    )
    if [ -n "$APK_URL" ]; then
        APK_FILE=$(basename "$APK_URL")
        if [ ! -f "$APK_FILE" ]; then
            echo "Downloading latest getabreak APK ($APK_FILE)..."
            download_file "$APK_URL" "$APK_FILE"
        fi
    fi
fi

if [ -z "$APK_FILE" ] || [ ! -f "$APK_FILE" ]; then
    echo ""
    echo "Error: No getabreak APK was found."
    echo "Either:"
    echo "  1. Copy a getabreak APK file (*.apk) into this directory, or"
    echo "  2. Publish a release with an attached APK asset at:"
    echo "     https://github.com/mizan989/getabreak-focus_app/releases"
    echo ""
    exit 1
fi

echo "Installing getabreak on your device"
platform-tools/adb install -r -t "$APK_FILE"

echo "Granting Permissions"
platform-tools/adb shell pm grant com.getabreak.app android.permission.WRITE_SECURE_SETTINGS
platform-tools/adb shell "dpm set-device-owner com.getabreak.app/.system_integration.DetoxDroidDeviceAdminReceiver"

echo "Starting App"
platform-tools/adb shell monkey -p com.getabreak.app 1

echo "Done."
