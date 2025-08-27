# FocusMode for Android

FocusMode is a simple yet powerful productivity application for Android designed to help you eliminate distractions and concentrate on what matters. By blocking selected applications and their notifications, it creates a focused environment, allowing you to be more productive.



---

## ✨ Features

* **App Blocking:** Select any user-installed applications to block. When Focus Mode is active, any attempt to open a blocked app will redirect you back to your home screen.
* **Notification Blocking:** All notifications from your selected apps are automatically dismissed before they can reach you.
* **User-Selectable App List:** A dynamic and user-friendly screen that lists all your apps, allowing you to create a personalized blocklist.
* **Emergency "Break Glass" Timer:** To disable Focus Mode, there is a 15-second countdown, giving you a moment to reconsider breaking your focus session.
* **Cancelable Timer:** Accidentally hit the stop button? You can cancel the disabling countdown and stay in Focus Mode.
* **Professional UI with Dark Theme:** A clean, modern user interface built with Jetpack Compose that automatically adapts to your system's light or dark theme.

---

## 🛠️ Technologies Used

* **Kotlin:** The primary programming language.
* **Jetpack Compose:** For building the modern, declarative UI.
* **Accessibility Service:** The core Android service used to identify and block foreground apps.
* **Notification Listener Service:** The core Android service used to intercept and dismiss notifications.
* **Material 3:** For UI components and theming.

---

## ⚙️ Setup

To build and run this project yourself:

1.  Clone the repository: `git clone https://github.com/YOUR_USERNAME/FocusMode.git`
2.  Open the project in Android Studio.
3.  Let Gradle sync and build the project.
4.  Run on an emulator or a physical Android device.
5.  **Important:** You must manually grant **Accessibility** and **Notification Access** permissions through the device's Settings app for the core features to function. The app will guide you to the correct settings pages upon first use.

---

## 🤝 Contributing

Contributions from other developers are welcome! If you have an idea for a new feature, a bug fix, or an improvement to the code, please feel free to collaborate.

**How to contribute:**
1.  **Fork the repository** on GitHub.
2.  **Create a new branch** for your feature or fix (`git checkout -b feature/AmazingFeature`).
3.  **Make your changes** and commit them (`git commit -m 'Add some AmazingFeature'`).
4.  **Push to your branch** (`git push origin feature/AmazingFeature`).
5.  **Open a Pull Request** and describe the changes you've made.

Let's make this app even better together!
