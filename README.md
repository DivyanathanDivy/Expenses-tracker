# WrkSpot App

## Overview

This project is an Android app built for Android 14 using Jetpack Compose. It demonstrates modern Android development practices and technologies. The app follows the MVVM pattern and applies the Clean Architecture approach.
## Tech Stack
- ✅ Kotlin
- ✅ MVVM
- ✅ Jetpack Compose – For modern, declarative UI development
- ✅ Hilt – For dependency injection
- ✅ Flow / StateFlow – For reactive state management
- ✅ Room – For local database handling 
- ✅ Coroutines – For async programming

## Architecture

### MVVM

The app uses the Model-View-ViewModel (MVVM) architecture. This separates the business logic and state management (handled by the ViewModels) from the UI (handled by the Views), making the app easier to test and maintain.

### SOLID Principles

The code follows SOLID principles, which helps keep the system scalable, maintainable, and robust. For example, `CountryFetcher` and `CountrySearcher` components demonstrate these principles.
