# Ktor Betclic Tournament App

This is a Kotlin-based Ktor application for managing tournament players. The app provides RESTful APIs to create, retrieve, update scores, and delete player data.

## Features  
- **Player Management**: Create, retrieve, and update player scores.  
- **Bulk Operations**: Retrieve or delete all players.  
- **MongoDB**: Integration with MongoDB for data storage.

## Requirements  
- **JDK**: JDK 22 or later  
- **Gradle**: 7.x or later  
- **Docker**: 20.x or later (optional for containerized deployment and testing)

---

## Running with Docker

1. **Build Docker Images**  
   ```bash
   docker-compose build
   ```

2. **Start Services**  
   ```bash
   docker-compose up -d
   ```

3. **Access Application**  
   The app will be available at [http://localhost:8080](http://localhost:8080).

---

## Running Without Docker

### Steps  
1. **Export MongoDB URI**  
   ```bash
   export MONGO_URI="mongodb://localhost:27017"
   ```

2. **Build the Application**  
   ```bash
   ./gradlew buildFatJar
   ```

3. **Run the Application**  
   ```bash
   java -jar build/libs/*-all.jar
   ```
---

## Endpoints

### `/player`  
- **POST** `/player/{username}`: Create a player.  
- **GET** `/player/{id}`: Get a player by ID.  
- **PATCH** `/player/{id}/score`: Update a player’s score.

### `/players`  
- **GET** `/players`: Retrieve all players.  
- **DELETE** `/players`: Delete all players.

## Swagger
Swagger is available at [http://localhost:8080/swagger](http://localhost:8080/swagger).

