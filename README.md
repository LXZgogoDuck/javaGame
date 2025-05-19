# 🦆 DuckDuckGo Game

A Java-based 2D action game featuring multiple levels, player animations, background effects, and a cheating mode! Includes a user-friendly GUI for login and registration, backed by a database.

---

## 🚀 Getting Started

### ✅ **Prerequisites**
- Java 8+  
- JavaFX  
- MySQL or compatible database  
- Maven  

### 📦 **Setup Instructions**
1. **Database Configuration**
   - Open `kernel/utils/jdbcUtils.java`.
   - Update the `PASSWORD` variable with your own database password.
   - Ensure your database server is running.

2. **Maven Dependencies**
   - Run the following to download and update dependencies:
     ```bash
     mvn clean install
     ```

3. **Run the Game**
   - Execute the `Main` class to start the game.

---

## 🎮 Game Controls

| Action  | Keys        |
|---------|-------------|
| Move    | `W`, `A`, `S`, `D` or Arrow Keys |
| Attack  | `Space` or Mouse Click |
| Quit    | `Quit Button` or Window Close |

---

## 📖 Game Rules

1. The game features **3 levels**. Defeat all enemies to progress to the next level.
2. Health potions are hidden inside boxes/containers. Smash them to collect!
3. Drinking potions restores health.
4. A **cheating mode** is available to become invincible.
5. Enjoy the atmospheric background effects and rain!

---

## ✨ Features

### 📌 **Basic Features**
- Functional Game Logic
- Login & Registration System (JavaFX GUI)
- Database Connectivity

### 🌟 **Advanced Features**
- Integrated Audio for Gameplay
- Player Animation Effects
- Cheating Mode with Infinite Health
- Visual Rain and Background Effects

---

## 📂 Project Structure

---

## 🛠️ Implementation Highlights

### GUI (Login & Registration)
- Built using JavaFX, CSS, and FXML.
- Follows MVC pattern with `loginViewController`.
- User credentials stored securely in a MySQL database.

### Game Engine
- Multithreaded game loop using `Runnable`.
- Four Game States: `MENU`, `PLAYING`, `OPTION`, `QUIT`.
- Components like `audioPlayer`, `Menu`, `Playing` initialized during startup.

### Gameplay Mechanics
- Player and Enemy Entities with collision detection.
- Player animations using 2D sprite arrays.
- Health and Attack stats for the player.

### Utilities
- `LoadSave`: Efficient image asset loading.
- `Constants`: Centralized game constants (gravity, speed, image sizes).
- `HelpMethods`: Movement logic and collision detection.

### Game UI
- Dynamic buttons with mouse events (`PauseButton` class).
- Smooth transitions and overlays between game states.

### Visual & Audio Effects
- Background rain and moving clouds for a vibrant atmosphere.
- Integrated sound effects for immersive gameplay.

---

## 📚 Credits & Inspiration
- JavaFX Official Documentation  
- Tutorials on Multithreading & Game Loop Design  
- Sample Projects from Course Demos  

---

Enjoy the game and happy coding! 🎉  
