# PREP TRACK

> **Plan • Focus • Track • Achieve**  
> A universal, offline-first study planner, syllabus manager, lecture tracker, focus timer, and progress analytics application for Android.

---

## 🎯 About PREP TRACK

PREP TRACK is built to help students systematically prepare for high-stakes competitive examinations and board tests. Rather than relying on static or generic planners, PREP TRACK implements an adaptive, lecture-based planning engine with strict separation between theoretical progress, estimated watch time, and actual measured focus time.

### Supported Exam Pathways:
- **Medical:** NEET 2027, NEET 2028 (Physics, Chemistry, Biology)
- **Engineering:** JEE Main, JEE Advanced (Physics, Chemistry, Mathematics)
- **School / Boards:** CBSE Class 10 (Science, Maths, Social Science, English), CBSE Class 12, ICSE, State Boards
- **University:** CUET (Domain Subjects, General Test, Language)
- **Defence:** NDA, CDS, AFCAT, Agniveer
- **Government Exams:** UPSC CSE, SSC CGL, Banking, Railway, State PSC
- **Custom Goal:** User-defined exams and custom curricula

---

## 🔑 Core Features & Architectural Principles

### 1. Invariant 2-Hour Lecture Reference Rule
Every lecture carries an invariant reference duration of **2.0 Hours (120 minutes)**. This reference standardizes workload calculations across subjects and never changes with playback speed.
- $10\text{ Lectures} \times 2.0\text{h} = 20\text{ Reference Hours}$

### 2. Differentiated Estimated Watch Time
Playback speed ($1.0\times, 1.25\times, 1.5\times, 1.75\times, 2.0\times$) adjusts *only* the estimated actual screen watch time:
$$\text{Estimated Watch Time} = \frac{\text{Reference Hours}}{\text{Playback Speed}}$$
For example, 10 lectures at $1.5\times$ speed = $13\text{h } 20\text{m}$ estimated watch time, while the reference progress remains $20\text{ hours}$.

### 3. Dynamic Daily Workload Calculation
The daily study requirement is dynamically computed across remaining days:
$$\text{Daily Workload} = \frac{\text{Remaining Watch Time} + \text{Remaining Notes}}{\text{Remaining Days}} + \text{Daily Practice} + \text{Daily Revision}$$

### 4. Missed-Day Adjustments
If planned sessions are missed, workload is preserved. Students can distribute remaining workload across future days or extend deadline milestones with one click.

### 5. Multi-Mode Study Patterns
- **Mode A (Weekly Timetable):** Assigns specific subjects to days of the week (Monday through Sunday).
- **Mode B (One Subject at a Time):** Sequential syllabus pacing prioritizing one subject before advancing.

### 6. Interactive Lecture Tracking
- Individual lecture completion checkmarks
- Per-lecture notes completion toggle
- Practice & DPP (Daily Practice Problem) completion toggle
- Priority bookmarking (Important stars)
- Personal formula and lecture notes

### 7. Pomodoro Focus Timer & Distraction Control
- Presets ($25\text{m}/5\text{m}$, $50\text{m}/10\text{m}$, custom)
- Subject, chapter, and topic tagging
- Strict metric separation:
  - **Metric A:** Lecture Reference Progress ($2\text{h}/\text{lecture}$)
  - **Metric B:** Estimated Watch Time (calculated by speed)
  - **Metric C:** Actual Focus Time (measured through live timer sessions)

### 8. Progress Analytics & Streak System
- Syllabus completion rate with progress indicators
- Subject-by-subject lecture and hour distribution
- Daily, weekly, and monthly time breakdowns
- Activity-based study streak tracking

---

## 🛠️ Tech Stack & Architecture

- **Platform:** Android (Min SDK 24, Target SDK 36)
- **Language:** Kotlin 2.2
- **UI Toolkit:** Jetpack Compose with Material 3 Design System
- **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel)
- **Local Persistence:** Room Database with KSP (Kotlin Symbol Processing)
- **Asynchronous Execution:** Kotlin Coroutines & StateFlow
- **Offline-First:** Zero dependency on external APIs for core tracking

---

## 🚀 Building & Running

### Prerequisites
- Android Studio Ladybug (or newer)
- JDK 17 or JDK 21
- Android SDK 36

### Build Debug APK:
```bash
./gradlew assembleDebug
```
The output APK will be located at:
`app/build/outputs/apk/debug/app-debug.apk`

### Run Unit & Robolectric Tests:
```bash
./gradlew testDebugUnitTest
```

### Build Release APK / App Bundle (AAB):
```bash
# Build Release APK
./gradlew assembleRelease

# Build Google Play App Bundle
./gradlew bundleRelease
```
Outputs:
- APK: `app/build/outputs/apk/release/app-release-unsigned.apk`
- AAB: `app/build/outputs/bundle/release/app-release.aab`

---

## 📤 Publishing to GitHub from Google AI Studio

To publish this project to your GitHub account directly from Google AI Studio:

1. Click on the **Settings** / **Project Menu** icon (gear or top-right menu in the AI Studio editor header).
2. Select **"Push to GitHub"** (or **"Export to GitHub"**).
3. Connect / Authorize your GitHub account when prompted (`nobitanobi7209`).
4. Select your target repository or enter a new repository name (e.g. `prep-track-app`).
5. Choose **Public** or **Private**, then confirm **Push**.
6. AI Studio will commit and push the entire codebase directly to your repository!

Alternatively, you can export the project as a **ZIP** from the same menu, extract it locally, and push via git CLI:
```bash
git init
git add .
git commit -m "Initial commit of PREP TRACK Android app"
git branch -M main
git remote add origin https://github.com/nobitanobi7209/prep-track-app.git
git push -u origin main
```

---

## 📄 License
This project is open-source under the Apache License 2.0.
