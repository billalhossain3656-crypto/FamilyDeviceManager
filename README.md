# Family Device Manager

A safe, consent-based Android app for family device management and SMS notifications.

This project is intentionally limited to transparency and consent:
- no hidden camera or microphone access
- no secret Gmail takeover
- no SMS spying or unauthorized monitoring
- only visible, user-approved permissions and commands

## Features
- Register a device to a trusted user
- Send safe commands: ring, lock, location request, status check
- Show SMS notifications only for the device owner
- Receive explicit user consent before remote actions
- Firebase Firestore backend for device and command management

## Tech stack
- Android Kotlin
- Firebase Firestore
- Firebase Cloud Messaging
- Android notification APIs

## Project structure
- `app/` – Android app
- `README.md` – setup and usage guide

## Setup
1. Create a Firebase project at https://console.firebase.google.com
2. Add an Android app with package name `com.familydevicemanager.app`
3. Download `google-services.json` and place it in `app/`
4. Enable Firestore and Cloud Messaging in Firebase
5. Open the project in Android Studio and sync Gradle

## Run
- Open the project in Android Studio
- Select an Android emulator or device
- Press Run

## Notes
This is a starter app for a consent-based device manager. It is not intended for stealth monitoring or hidden access.
