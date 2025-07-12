# GuriDocPat - Doctor-Patient Appointment Management System

A modern Android application built with Jetpack Compose that facilitates seamless communication and appointment management between doctors and patients.

## 📱 Overview

GuriDocPat is a comprehensive healthcare platform that connects doctors and patients, enabling efficient appointment booking, management, and communication. The app provides separate dashboards for doctors and patients with role-specific features and functionalities.

## ✨ Features

### 🔐 Authentication & User Management
- Secure login and signup functionality
- Role-based user selection (Doctor/Patient)
- Firebase Authentication integration
- User profile management

### 👨‍⚕️ Doctor Features
- **Dashboard**: Overview of appointments and patient information
- **Appointment Management**: View, confirm, and manage patient appointments
- **Availability Management**: Set and update availability schedules
- **Patient List**: Access to registered patients
- **Profile Management**: Update professional information and qualifications

### 👤 Patient Features
- **Dashboard**: Overview of upcoming appointments and medical history
- **Appointment Booking**: Browse doctors and book appointments
- **Doctor Search**: Find and view doctor profiles and availability
- **Appointment History**: Track past appointments and medical records
- **Profile Management**: Update personal information

### 📅 Appointment System
- Real-time appointment scheduling
- Multiple consultation types (In-person, Online)
- Appointment status tracking (Pending, Confirmed, Cancelled)
- Follow-up appointment scheduling
- Payment status tracking
- Rating and feedback system

### 💬 Communication
- Chat functionality between doctors and patients
- Appointment notifications
- Real-time updates

## 🏗️ Architecture

The application follows Clean Architecture principles with MVVM pattern:

```
app/src/main/java/com/guri/guridocpat/
├── appnavgraph/          # Navigation configuration
├── auth/                 # Authentication module
├── appointment/          # Appointment management
├── availability/         # Doctor availability management
├── chats/               # Chat functionality
├── common/              # Shared components and data models
├── doctordashboard/     # Doctor-specific features
├── doctordetails/       # Doctor profile management
├── doctorhome/          # Doctor home screen
├── doctorslist/         # Patient's doctor browsing
├── patientdashboard/    # Patient-specific features
├── profile/             # User profile management
├── splash/              # Splash screen
├── ui/                  # UI theme and styling
└── userselection/       # Role selection screen
```

### Key Technologies

- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM with Clean Architecture
- **Dependency Injection**: Hilt
- **Navigation**: Jetpack Navigation Compose
- **Backend**: Firebase (Authentication, Firestore, Storage)
- **Language**: Kotlin
- **Build System**: Gradle with Kotlin DSL

## 🚀 Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Android SDK 23 or higher
- Kotlin 1.8+
- Google Services account for Firebase

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/GuriDocPat.git
   cd GuriDocPat
   ```

2. **Set up Firebase**
   - Create a new Firebase project
   - Enable Authentication, Firestore, and Storage
   - Download `google-services.json` and place it in the `app/` directory

3. **Configure Firebase**
   - Enable Email/Password authentication in Firebase Console
   - Set up Firestore database with appropriate security rules
   - Configure Firebase Storage for file uploads

4. **Build and Run**
   ```bash
   ./gradlew build
   ```
   - Open the project in Android Studio
   - Connect an Android device or start an emulator
   - Click "Run" to install and launch the app

## 📱 App Flow

1. **Splash Screen** → App initialization and user session check
2. **Login/Signup** → User authentication
3. **User Selection** → Choose role (Doctor/Patient)
4. **Dashboard** → Role-specific main interface
   - **Doctor**: Manage appointments, view patients, set availability
   - **Patient**: Book appointments, view doctors, manage profile

## 📊 Data Models

### User
- Basic user information (ID, email, phone, role)
- Creation and update timestamps

### Doctor
- Professional information
- Qualifications and specializations
- Availability schedules

### Patient
- Personal information
- Medical history
- Appointment preferences

### Appointment
- Scheduling information
- Status tracking
- Consultation details
- Payment and rating information

## 📦 Dependencies

### Core Dependencies
- **AndroidX Core KTX**: Core Android functionality
- **Jetpack Compose**: Modern UI toolkit
- **Navigation Compose**: Navigation between screens
- **Material 3**: Material Design components

### Firebase Dependencies
- **Firebase Auth**: User authentication
- **Firebase Firestore**: Cloud database
- **Firebase Storage**: File storage
- **Firebase Analytics**: App analytics

### Architecture Dependencies
- **Hilt**: Dependency injection
- **ViewModel**: MVVM architecture
- **Lifecycle**: Lifecycle-aware components

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.