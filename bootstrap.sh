#!/usr/bin/env bash
set -e

chmod +x gradlew
./gradlew assembleDebug

echo "APK ready:"
echo "app/build/outputs/apk/debug/app-debug.apk"
