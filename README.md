# Expense Tracker Android App

This repository contains a starter Android application for an Expense Tracker app.

## Features implemented
- Splash screen shown for 2 seconds
- Login screen with email/password authentication
- Register screen with username, email, password, and age
- Firebase email verification flow for account activation
- Dashboard screen after successful login

## Required setup
1. Open the project in Android Studio.
2. Download the Firebase configuration file:
   - Create a Firebase project at https://console.firebase.google.com/
   - Add an Android app with package name `com.example.expensetracker`
   - Download `google-services.json`
   - Place it in the `app/` directory.
3. Enable Email/Password sign-in in Firebase Authentication.
4. Enable Firestore if you want to store usernames and age.
5. Build and run the app.

## Firebase email verification flow
- The app creates the account using Firebase Auth.
- It sends a verification email with a confirmation link.
- The user confirms the account from the email.
- Once `isEmailVerified` is true, the user can log in successfully.

## Notes
The app is built as a clean starting point, with Firebase integration ready for real email verification and authentication.
