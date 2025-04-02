# 📋 Taskify - Advanced Jetpack Compose To-Do App

**Taskify** is a powerful and modern To-Do list app built using Jetpack Compose and MVVM architecture. It focuses on intuitive interactions and clean UI while being fully open-source and developer-friendly.

---

## 📦 Download Taskify APK

Try out the app without cloning or building — just click below:

[![Download APK](https://img.shields.io/badge/Download-APK-blue?style=for-the-badge&logo=android)](https://github.com/DakshPatel40/Taskify/releases/download/v1.0.0/app-debug.apk)

OR

[click here to download the APK directly](https://github.com/DakshPatel40/Taskify/releases/download/v1.0.0/app-debug.apk).

---

## ⚙️ Features

- ✅ Add, Edit, and Delete Tasks
- ✍️ Task title, optional description, priority (Low/Medium/High), and due date
- ✅ Long Press to mark tasks as completed (toggles)
- 🗑️ Swipe left to delete with confirmation dialog
- 📅 Due date validation: can't be empty or before today
- 🗂️ Two tabs: All Tasks and Completed Tasks
- 🎯 Priority-based filter in both tabs
- 📆 Shows creation date and completed date (if done)
- 👨‍🏫 First-time tutorial popup & Help button in the top app bar
- 🎨 Material You UI, fully responsive

---

## 🧑‍💻 Developer Notes

### 📂 Architecture

- MVVM (Model-View-ViewModel)
- Room Database
- StateFlow + Jetpack Compose
- Material 3 design
- Navigation Compose for screen routing

### 🛡 Validations

- Title can't be empty
- Due date is required and must be today or later

### 💡 UX Details

- Swipe to delete doesn't instantly remove the task; shows confirmation popup
- Long press toggles completion
- Tasks shown dynamically under respective tabs (All vs Completed)
- Description shows only if not empty (with ellipsis for long text)

---

## 🧪 How to Build

1. Clone the repo  
   `git clone https://github.com/your-username/taskify.git`

2. Open in any ide like **Android Studio** 

3. Run the app or build the APK  
   `Build > Build Bundle(s) / APK(s) > Build APK`

4. You'll find the debug APK here:  
   `app/build/outputs/apk/debug/app-debug.apk`

---


## 👤 Author

**Daksh Faldu**  

---

## 📫 Contact

- 📧 Email: [dakshfaldu2007@gmail.com](mailto:dakshfaldu2007@gmail.com)
- 🐙 GitHub: [DakshPatel40](https://github.com/DakshPatel40)
- 💼 LinkedIn: [Daksh Patel](https://www.linkedin.com/in/daksh-patel40)


---

> Feel free to fork, star, and improve Taskify! If you're a developer learning Compose or MVVM – this project is a goldmine 💎
