# System Monitoring System (Java MVC)

![Screen](screenshots/screen.png)

This is a system monitoring application developed in Java using the Model-View-Controller (MVC) architectural pattern. It allows monitoring and displaying information about system memory usage, CPU usage, and processes.

## Project Structure

The project is organized into three main packages:

- **org.example.controller:** Contains classes responsible for the system's control logic.
- **org.example.model:** Contains classes representing the system's data model.
- **org.example.view:** Contains classes responsible for the user interface.

## System Requirements

To run the system, you need the following:

- JDK (Java Development Kit) installed.
- OSHI library for system information. (The dependency can be included in the Maven project or downloaded manually.)

## How to Run

1. Clone the repository to your local machine:

   git clone <REPOSITORY_URL>


2. Open the project in your favorite Java IDE.


3. Run the `org.example.controller.SystemController` class to start the application.

## Features

The system offers the following features:

- **Real-time Monitoring:** Continuous updates on memory usage, CPU usage, and system processes.
- **Graphical Views:** Bar charts representing memory and CPU usage.
- **Process Table:** Lists major running processes, displaying information such as PID, process name, memory usage, and CPU usage.
- **System Information:** Displays detailed information about total memory, used memory, free memory, and percentage of memory used.

