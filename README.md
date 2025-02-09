# Jetpack Compose QR Code Scanner

A modern Android QR code scanner application built with Jetpack Compose, featuring a clean UI and robust functionality. This application provides real-time QR code scanning capabilities with additional features like camera controls, gallery integration, and automatic URL handling.

## Features

- **Real-time QR Code Scanning**: Fast and accurate QR code detection using the device's camera
- **Camera Controls**:
  - Flash toggle (torch mode)
  - Camera flip (switch between front and back cameras)
  - Zoom control with slider interface
- **Gallery Integration**: Import QR codes from existing images
- **URL Handling**: Automatic browser opening for URL QR codes
- **Dynamic UI**: Modern interface with proper system UI integration
- **Error Handling**: Comprehensive error management for various scenarios

## Technical Stack

- **UI Framework**: Jetpack Compose
- **Camera API**: CameraX
- **QR Processing**: ZXing
- **Minimum SDK**: Android API level 31 (Android 12)
- **Theme**: Dynamic color scheme support (Material You)

## Prerequisites

- Android Studio Arctic Fox or newer
- Android SDK with minimum API level 31
- Kotlin 1.8.0 or newer

## Dependencies

Add these dependencies to your `build.gradle` file:

```gradle
dependencies {
    // Jetpack Compose
    implementation "androidx.compose.ui:ui:${compose_version}"
    implementation "androidx.compose.material3:material3:${material3_version}"
    
    // CameraX
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-view:${camerax_version}"
    
    // ZXing for QR code processing
    implementation "com.google.zxing:core:3.4.1"
}
```

## Setup

1. Add camera permission to your `AndroidManifest.xml`:
```xml
<uses-permission android:name="android.permission.CAMERA" />
```

2. Ensure you handle runtime permissions in your application before accessing the camera.

## Usage

### Basic Implementation

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QrCodeScannerScreen(
                onQrCodeScanned = { qrCode ->
                    // Handle the scanned QR code
                }
            )
        }
    }
}
```

### Handling QR Code Results

The application automatically handles URLs by opening them in the device's default browser. For other types of QR codes, you can implement custom handling logic in the `onQrCodeScanned` callback.

## Key Components

### QrCodeScannerScreen

The main composable that handles the camera preview and UI controls. It provides:
- Camera preview
- Control bar with gallery, flash, and camera flip buttons
- Zoom control slider
- QR code processing

### QrCodeAnalyzer

Handles the image analysis for QR code detection using ZXing library. It processes camera frames in real-time to detect and decode QR codes.

### CameraZoomControl

A custom composable that provides a slider interface for controlling camera zoom levels.

## Error Handling

The application includes comprehensive error handling for:
- Camera initialization failures
- QR code processing errors
- Image processing errors from gallery
- Browser launching failures

## Contributing

Feel free to submit issues and enhancement requests.


## Credits

This project uses the following open-source libraries:
- ZXing for QR code processing
- CameraX for camera functionality
- Jetpack Compose for UI
