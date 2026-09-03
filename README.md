# MAD Practical 4: Alarm Manager & Foreground Service Application

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Gradle](https://img.shields.io/badge/Gradle-AGP%209.3.1-02303A?logo=gradle&logoColor=white)](https://gradle.org)
[![API](https://img.shields.io/badge/API-24%2B%20(Target%2036)-brightgreen)](https://developer.android.com/tools/releases/platforms)

## 🎯 Aim
> **Create an Android Alarm application by using service & Broadcast Receiver**



## 📱 Project Overview

**MAD Practical 4** is an Android application designed to demonstrate the complete lifecycle of scheduling, triggering, and controlling background system alarms. 

The application integrates core Android framework components:
- **`AlarmManager`** for scheduling precise time-based wakeups.
- **`BroadcastReceiver`** to capture scheduled alarm intents even when the application is not actively running in the foreground.
- **`Foreground Service`** with an ongoing persistent notification and audio playback via **`MediaPlayer`** (`alarm.mp3`).
- **Material 3 UI** featuring interactive cards, live digital clock, time picker, and dynamic card visibility.

---

## 📸 Screenshots & UI Flow

> **Note:** Place your application screenshots inside the [`screenshots/`](file:///d:/AndroidStudioProjects/24012011115_MAD_Practical4/screenshots) directory with matching filenames (`screen1_main.png`, `screen2_timepicker.png`, `screen3_alarm_set.png`, `screen4_notification.png`).

| 1. Home / Main Screen | 2. Time Picker Dialog | 3. Alarm Active State | 4. Foreground Notification |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/screen1_main.png" alt="Home Screen" width="220"/> | <img src="screenshots/screen2_timepicker.png" alt="Time Picker" width="220"/> | <img src="screenshots/screen3_alarm_set.png" alt="Alarm Set" width="220"/> | <img src="screenshots/screen4_notification.png" alt="Alarm Notification" width="220"/> |
| Displays current live digital clock & "Create Alarm" button | Interactive 12-hour AM/PM dialog for user time selection | Reveals bottom card with scheduled time & Cancel button | Ongoing low-priority notification during audio playback |

---



### Detailed Component Roles

1. **`MainActivity.kt`**:
   - **Permission Request**: Checks and requests `android.permission.POST_NOTIFICATIONS` at runtime on Android 13+ (API 33+).
   - **Clock & Time Picker**: Binds `TimePickerDialog` to calculate exact millisecond timestamps.
   - **Smart Date Rollover**: Automatically detects if the chosen time has already passed for the current day; if so, increments the calendar by 1 day (`calendar.add(Calendar.DAY_OF_YEAR, 1)`).
   - **AlarmManager Integration**: Creates an immutable `PendingIntent` targeted to `AlarmBroadcastReceiver` and triggers `alarmManager.setExact(AlarmManager.RTC_WAKEUP, ...)` with fallback to `set()` if exact alarm privileges are unavailable.

2. **`AlarmBroadcastReceiver.kt`**:
   - Extends `BroadcastReceiver` and acts as the bridge between `AlarmManager` and `AlarmService`.
   - Reads the intent extra (`"Service1"`).
   - On `"Start"`, invokes `ContextCompat.startForegroundService()` to bring up `AlarmService`.
   - On `"Stop"`, forwards the stop command using `context.stopService()`.

3. **`AlarmService.kt`**:
   - Extends `Service`.
   - Creates a dedicated `NotificationChannel` (`AlarmChannel`) for Android 8.0+.
   - Promotes the service to a foreground service using `startForeground(...)` with type `FOREGROUND_SERVICE_MEDIA_PLAYBACK` (Android 14+ compatible).
   - Plays the alarm audio file located in `res/raw/alarm.mp3` with `isLooping = true`.
   - Cleans up resources, stops audio playback, and unregisters notifications in `onDestroy()` and `stopAlarm()`.

4. **`activity_main.xml`**:
   - Encapsulated inside `ScrollView` and `ConstraintLayout` for responsive sizing.
   - Built with Material 3 components: `MaterialCardView`, `MaterialButton`, `ImageView` banners, and native `TextClock`.

---

## 🔒 Android Permissions & Compliance

| Permission | Android Level | Reason for Use |
| :--- | :--- | :--- |
| `android.permission.SCHEDULE_EXACT_ALARM` | API 31+ (Android 12+) | Enables exact timestamp triggers using `AlarmManager.setExact()`. |
| `android.permission.USE_EXACT_ALARM` | API 33+ (Android 13+) | Allows calendar and clock apps to schedule exact alarms automatically. |
| `android.permission.FOREGROUND_SERVICE` | API 28+ (Android 9+) | Required to run foreground services for background execution. |
| `android.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK` | API 34+ (Android 14+) | Explicit foreground service type for uninterrupted audio playback. |
| `android.permission.POST_NOTIFICATIONS` | API 33+ (Android 13+) | Required for displaying the foreground notification to the user. |

---


## 🚀 Getting Started & Installation

### 1. Clone the Repository
Open your terminal or command prompt and run:

```bash
git clone https://github.com/Nishit079/24012011115_MAD_Practical-4.git
```

### 2. Open in Android Studio
1. Launch **Android Studio** (Koala / Ladybug or newer recommended).
2. Select **Open** and navigate to the cloned folder `24012011115_MAD_Practical4`.
3. Allow Android Studio to complete Gradle sync and download required dependencies.

### 3. Build and Run
1. Connect an Android device (via USB or Wi-Fi debugging) or start an Android Virtual Device (AVD with API 24+).
2. Click the **Run** (`Shift + F10`) button in Android Studio.
3. Grant notification permissions when prompted on Android 13+.

---

## 🛠️ Tech Stack & Dependencies

* **Language:** Kotlin
* **UI Framework:** Android XML Layouts with Material Components (Material 3)
* **Minimum SDK:** API 24 (Android 7.0 Nougat)
* **Target SDK:** API 36 / 37
* **Android Gradle Plugin (AGP):** 9.3.1
* **Key Libraries:**
  - `androidx.core:core-ktx:1.19.0`
  - `androidx.appcompat:appcompat:1.7.1`
  - `com.google.android.material:material:1.14.0`
  - `androidx.activity:activity-ktx:1.13.0`
  - `androidx.constraintlayout:constraintlayout:2.2.2`

---

## 👤 Author & Submission Information

* **Submitted by:** Nishit Patel
* **Enrollment Number:** 24012011115
* **Repository Link:** [https://github.com/Nishit079/24012011115_MAD_Practical-4](https://github.com/Nishit079/24012011115_MAD_Practical-4)
