# The Cholli App

The Cholli App is a JavaFX-based children's entertainment hub developed by Group 44 at NTNU for the IDATT2003 course. Inspired by the "Pengebyen" concept, this app serves as a central portal for interactive children's entertainment, featuring five mini-apps and customizable experiences.

## Table of Contents

* [Features](#features)
* [Installation](#installation)
* [Configuration](#configuration)
* [Usage](#usage)
* [Project Structure](#project-structure)
* [Technologies](#technologies)
* [Acknowledgments](#acknowledgments)

## Features

* **Snakes & Ladders**: A classic board game with customizable layouts via JSON files.
* **Star Game**: Navigate a field of stars in a simple collection challenge.
* **Memory Game**: Flip cards to find matching pairs and sharpen memory skills.
* **Paint Canvas**: A freehand drawing area where kids can unleash their creativity.
* **Interactive Gadgets**: Fun, animated widgets (e.g., animals) on the home screen for exploration.
* **Player Profiles**: Create and manage multiple profiles for personalized progress tracking.
* **Save & Load**: Persist game states and resume play at any time.

## Installation

1. **Prerequisites**:

   * Java 21 or higher
   * Maven 3.6+

2. **Clone the repository**:

   ```bash
   git clone https://github.com/your-org/board-game-hub.git
   cd board-game-hub
   ```

3. **Build the project**:

   ```bash
   mvn clean install
   ```

4. **Run the application**:

   ```bash
   mvn javafx:run
   ```

## Configuration

* Board games can be customized by editing or adding JSON files in the `config/boards` directory.
* Player data and saved games are stored under `data/` by default.

## Usage

* Launch the app via Maven or the packaged JAR.
* On the home screen, select any of the five entertainment modules.
* Use the menu options to create or switch player profiles.
* Save your current game state anytime; load it later to continue.

> **Note:** There is no in-game pause menu; to pause play, simply save your progress.

## Project Structure

The application follows an **MVC architecture**:

* `model/`: Data classes, game logic, and JSON configurations.
* `view/`: JavaFX FXML layouts and UI components for each module.
* `controller/`: Event handlers and controllers linking UI and model.

Each package contains modular components to ensure clear separation of concerns and maintainability.

## Technologies

* JavaFX for UI
* Maven for build and dependency management
* JSON for configuration

## Acknowledgments

* Inspired by the Norwegian children’s app concept **Pengebyen**.
* Developed by **Group 44** as part of NTNU’s **IDATT2003** course.
