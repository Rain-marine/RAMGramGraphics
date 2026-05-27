# RAMGram Client

A Java-based social media client application inspired by a mix of Twitter and Telegram functionality.  
This project was developed as part of a university course project.

The client connects to a separate server repository using a custom socket-based communication protocol over TCP/IP.

## Features

- User authentication
- Tweet posting
- Like and retweet system
- Direct messaging
- Group chats
- Timeline/feed system
- Local caching for offline/online access
- Persistent logging using Gradle
- JavaFX graphical user interface
- Scene management using FXML

## Technologies Used

- Java
- JavaFX
- FXML
- Gradle
- TCP Socket Communication

## Architecture

The client follows a modular architecture separating:

- UI / Graphics layer
- Controllers
- Networking
- Local storage and caching
- Models

Most graphical scenes were designed using FXML and connected through JavaFX controllers.

## Networking

The client communicates with the server using a custom protocol built on top of TCP sockets.

The protocol handles:
- Authentication requests
- Messaging
- Feed synchronization
- Tweet interactions
- Group communication

## Local Data Storage

The client stores logs and cached data locally to improve responsiveness and allow partial offline access.

Gradle is used for project management and build automation.

## Running the Project

### Prerequisites

- Java 17+
- MySQL 
- Gradle

### Run

Start from the Main.java in src/main -> java -> Main.java
