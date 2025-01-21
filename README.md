# DSLR Portrait - AI Blur

![App Icon](https://raw.githubusercontent.com/thezayin/DSLR-Blur-Ai-Portrait/master/common/values/src/main/res/drawable/ic_main.png)

A sleek, user-friendly Android app designed to apply AI-based background blur to portrait images using TensorFlow. The app is built with a multi-module architecture and utilizes Jetpack Compose for smooth UI, Clean Architecture for scalability, and Koin for dependency injection.

## Features

- **TensorFlow-Based Segmentation**: Automatically detects the subject and creates a mask for background blur.
- **Optimized Gaussian Blur**: Uses GPUImage to apply a smooth, customizable blur effect to the background.
- **Smooth UI/UX**: The app provides a clean, seamless experience with smooth transitions and animations.
- **Gallery Integration**: Built-in gallery to view, select, and edit images.
- **Onboarding Screens**: Includes onboarding to guide new users through the app's features.
- **Editing Tools**: Easily apply AI-based blur effects, adjust intensity, and smooth edges for a natural look.
- **Image Saving**: Save edited images with ease, and share them on your social platforms.

## UI Flow

1. **Splash Screen**: A sleek splash screen introduces the app.
2. **Onboarding**: Guide the user through the core features.
3. **Home Screen**: Main interface where users can start editing or upload images.
4. **Gallery Screen**: Displays the user’s gallery of uploaded images for easy access.
5. **Editing Screen**: Users can apply the AI blur effect and adjust settings.
6. **Save Image BottomSheet**: Allows users to save and share the final image.

## Architecture

The app follows the **MVI (Model-View-Intent)** architecture pattern with clear separation of concerns:

- **Model**: Holds data and business logic.
- **View**: Represents the UI, built using Jetpack Compose.
- **Intent**: Represents user actions (e.g., applying blur, adjusting intensity).

The app’s state is managed via **StateFlow**, and all UI changes are triggered by **events** to ensure consistency and maintainability.

## Code Explanation

This app employs **ML Kit's Subject Segmentation API** for segmentation, leveraging **TensorFlow** to detect and create a subject mask. The mask is then processed using **GPUImage**'s Gaussian blur filter for a smooth, high-quality blur effect. The app optimizes the image processing pipeline by leveraging GPU acceleration and applies the background blur efficiently. Additionally, **Kotlin Coroutines** and **Firebase Crashlytics** are used for smooth background tasks and error tracking. The overall approach is to achieve a polished, seamless user experience while maintaining app performance.

## Dependencies

- **Jetpack Compose**: For a modern, declarative UI.
- **Koin**: For dependency injection.
- **Ktor**: For handling network requests.
- **TensorFlow**: For AI-based segmentation and processing.
- **GPUImage**: For optimized Gaussian blur effects.
- **ML Kit**: For segmentation tasks.

## How to Use

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an Android device.
4. Use the app to select an image, apply background blur, and save your edited image.

## Customization

- Adjust the **blur intensity** and smooth edges for a more polished result.
- You can update the segmentation model or modify the `GPUImageGaussianBlurFilter` parameters for different effects.

## Getting Started

- Clone the repository.
- Set up the necessary dependencies (refer to the `build.gradle` files for details).
- Make sure to replace the icon with the rounded version (see the link below) to match the app’s aesthetic.

## Contact

- **Email**: [zainshahidbuttt@gmail.com](mailto:zainshahidbuttt@gmail.com)
- **WhatsApp**: [+923033009802](https://wa.me/923033009802)

## License

This project is licensed under the MIT License.
