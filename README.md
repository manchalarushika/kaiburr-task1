#  Task Executor Management API (Kaiburr Assessment - Task 1)

##  Project Overview
This project implements a **secure and robust RESTful API** using **Spring Boot** and **MongoDB**.  
The core functionality is to manage a list of shell commands (Tasks) and securely execute them on the host operating system.  
This submission fulfills the requirements of **Task 1** of the **Kaiburr Technical Assessment**.

---

##  Features Implemented

###  Task Management (CRUD)
- Create (via `PUT`), Read (single/all/search), and Delete tasks.

###  Secure Command Execution
- Executes the stored shell command using a separate, isolated process (`ProcessBuilder`).

###  Security Validation
- Implements strict pre-execution validation to prevent **Command Injection vulnerabilities** by blocking dangerous characters and system-critical commands (`rm`, `sudo`, `bash`, etc.).

###  Execution Logging
- Records the start time, end time, exit code, and captured standard output/error (`stdout`/`stderr`) for every command execution, storing them directly within the Task record.

###  Persistence
- All Task data, including execution history, is persisted using **MongoDB**.

---

##  Technology Stack

| Component | Technology |
|------------|-------------|
| **Backend Framework** | Java 21 / Spring Boot 3 |
| **Database** | MongoDB (via Spring Data MongoDB) |
| **Build Tool** | Maven |
| **Execution Engine** | Java `ProcessBuilder` |
| **Security** | CommandValidator (custom) |

---

##  Getting Started

###  Prerequisites
Before you begin, ensure you have the following installed:
- **Java Development Kit (JDK) 21** or later  
- **Apache Maven**  
- **MongoDB** (Local instance or MongoDB Atlas)  
- (Optional) **Docker** — to easily run MongoDB locally

---

###  Setup and Run

####  Clone the Repository
```bash
git clone [YOUR_REPOSITORY_URL]
cd [repository-name]
```
#### Configure MongoDB
Edit the 
```css
src/main/resources/application.properties
```
file and set your MongoDB connection URI.
Replace with your actual MongoDB connection string (including credentials and database name)
```properties
spring.data.mongodb.uri=mongodb+srv://<USERNAME>:<PASSWORD>@<CLUSTER-URL>/taskdb?retryWrites=true&w=majority
```

#### Build and Run
Use Maven to package and run the application.
```bash
# Build the JAR file
mvn clean install

# Run the application
java -jar target/[your-app-name].jar
```
The application will start on http://localhost:8080

#### API Endpoints
All endpoints use the base URL: http://localhost:8080/tasks
| Method     | Endpoint                      | Description                                                                                                     | Request Body Example                                                  |
| ---------- | ----------------------------- | --------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| **PUT**    | `/tasks`                      | Creates a new Task or updates an existing one (upsert behavior).                                                | `{"name": "Check Time", "owner": "CandidateName", "command": "date"}` |
| **GET**    | `/tasks/{id}`                 | Retrieves a single Task by its MongoDB ID.                                                                      | -                                                                     |
| **GET**    | `/tasks`                      | Retrieves a list of all Tasks in the database.                                                                  | -                                                                     |
| **GET**    | `/tasks/search?name={string}` | Searches for Tasks whose name contains the provided string.                                                     | -                                                                     |
| **PUT**    | `/tasks/execute/{id}`         | Securely executes the command stored in the Task with the given ID. Updates the Task with the execution result. | -                                                                     |
| **DELETE** | `/tasks/{id}`                 | Deletes a Task by its MongoDB ID.                                                                               | -                                                                     |

#### Security Implementation
The application incorporates a critical security layer via the CommandValidator component, which performs pre-execution static analysis on the input command string. This robust filtering ensures that the task executor environment remains safe and confined, mitigating common command injection risks.
The validator rejects any command containing:

-Injection Characters: &&, ;, |, `, $(), <, >, etc.

-Dangerous System Calls: Keywords like rm, sudo, chown, mv, bash, ssh, killall, and attempts at file viewing like cat /etc/passwd.

-Directory Traversal: ../ sequences.

#### Output
1.create
<img width="1600" height="889" alt="image" src="https://github.com/user-attachments/assets/e027ad07-9654-4da9-8aec-23ab90c2c66b" />

2.Get all data
<img width="1595" height="897" alt="image" src="https://github.com/user-attachments/assets/12d834c2-4078-4b17-89e6-abcf1eaad75d" />

3.Get by id
<img width="1606" height="895" alt="image" src="https://github.com/user-attachments/assets/1e70275a-4252-44f4-86aa-c0c505927748" />

4.Get task by name
<img width="1611" height="901" alt="image" src="https://github.com/user-attachments/assets/50d8dbef-6ee8-4005-a80e-e39b277b6c60" />












