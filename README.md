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
