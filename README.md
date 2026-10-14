# Smart Pantry Manager

## Overview
Smart Pantry Manager is an Android application that helps users track ingredients available at home and discover recipes they can prepare without shopping for missing ingredients.

## Features
- Add, view, edit, and delete pantry ingredients.
- Record ingredient names, quantities, units, and optional expiry dates.
- Browse suggested recipes based on pantry contents.
- Enforce strict recipe matching: every required ingredient and sufficient quantity must be available.
- View recipe ingredients and cooking instructions.
- Store pantry data locally so it remains available after the app is closed.
- Provide a Settings screen with application information.
- Include 20 sample recipes.

## Technology Stack
- Java
- Android SDK
- XML layouts
- SQLite database
- AndroidX AppCompat
- AndroidX RecyclerView
- Gradle build system

## Database Choice
SQLite was selected because it is built into Android, stores data locally, works without an internet connection, and supports structured tables and queries. It is suitable for a small pantry application.

## Main Screens
1. Home
2. Pantry List
3. Add/Edit Ingredient
4. Suggested Recipes
5. Recipe Details
6. Settings

## Requirements
- Java 17-compatible Android build configuration
- Android SDK Platform 35
- Android Build Tools
- Gradle wrapper included in this repository

## Build Instructions
1. Clone or open this repository in a development environment with the Android SDK installed.
2. Make sure the Android SDK path is configured in `local.properties` for your computer. Do not commit this local machine file.
3. From the project root, run:

   ```bash
   chmod +x gradlew
   ./gradlew assembleDebug
   ```

4. The debug APK will be generated at:

   `app/build/outputs/apk/debug/app-debug.apk`

## Recipe Matching
A recipe is suggested only when every required ingredient is present in the pantry in a sufficient quantity. The matcher normalizes common ingredient names and compatible units. If any requirement is missing or insufficient, that recipe is excluded.

## Testing
The application was installed on an Android phone. Manual checks included adding, editing, and deleting ingredients, strict recipe matching, reopening the app to check data persistence, opening Settings, and viewing recipe details.

## Repository
GitHub: https://github.com/Rhoozie/SmartPantryManager
