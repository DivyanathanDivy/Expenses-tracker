# Expenses App

## Overview

This project is an Android app built for Android 14 using Jetpack Compose. It demonstrates modern Android development practices and technologies. The app follows the MVVM pattern and applies the Clean Architecture approach.
## Tech Stack
- ✅ Kotlin
- ✅ MVVM
- ✅ Jetpack Compose – For modern, declarative UI development
- ✅ Nav Compose 
- ✅ Hilt – For dependency injection
- ✅ Flow / StateFlow – For reactive state management
- ✅ Room – For local database handling 
- ✅ Coroutines – For async programming

## Architecture

### MVVM

The app uses the Model-View-ViewModel (MVVM) architecture. This separates the business logic and state management (handled by the ViewModels) from the UI (handled by the Views), making the app easier to test and maintain.
## Features

### User Balance 
The user's balance is calculated from the transaction database by subtracting the total debited amount from the total credited amount.

### Chart to show Transaction for period
The chart shows the user's transactions, allowing them to choose different 
periods such as 1 day, 5 days, 1 month, 3 months, 5 months, and 1 year. The values are observed in real-time, so any new entries in the database will automatically update the UI.

### Recipient List
The Recipient section displays a list of 5 recipients, along with a +X indicator showing the number of additional recipients. When the user clicks on the + icon, they can view all recipients and scroll through them horizontally.

### Transaction History
Shows the user's transaction history, including payment details indicating whether the amount is credited or debited, along with the merchant name. A credited amount is indicated with a + and a debited amount with a -. The values are observed in real-time.

### Home Screen

![Home_Screen](https://github.com/user-attachments/assets/bb46c2f7-b97d-43f7-9457-a8a61acce337)

### Demo Video



https://github.com/user-attachments/assets/4c191ba2-0b36-4468-8fdd-9908584c026d



