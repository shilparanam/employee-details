
# EmployeeDetails API

A Spring Boot application that exposes a **secure REST API** for fetching employee details.  
It demonstrates:

- JWT-based authentication
- Role-based authorization
- HTTPS using a self-signed PKCS12 keystore
- Clean, testable REST endpoints
---

## 1. High-level overview

### What this app does

- Provides a `/auth/login` endpoint to authenticate users with username/password
- Issues a **JWT token** on successful login
- Protects `/api/employees` behind JWT authentication
- Uses **roles** to control which fields are visible:
  - `ROLE_MANAGER`: can see sensitive fields (e.g., Salary, SSN)
  - `ROLE_ANALYST`: cannot see sensitive fields

### Main tech stack

- Java 17
- Spring Boot (Web, Security)
- JWT-based auth
- HTTPS (self-signed certificate via PKCS12 keystore)
- Maven
- Swagger / OpenAPI for API documentation

---

## 2. Prerequisites

To run this project locally, you need:

- **Java JDK 17+**
- **Maven 3.8+**
- **keytool**  
  - Comes bundled with the JDK (no separate install required)
  - Should be available as `keytool` in your terminal (CMD)

On Windows, make sure your `JAVA_HOME` and `PATH` point to the JDK (e.g. `C:\Program Files\Java\jdk-17\bin`).

---

## 3. Getting started

### 3.1. Clone the repository

```bash
git clone <your-repo-url>
cd employeedetails


Replace <your-repo-url> with your actual Git repository URL.

4. HTTPS keystore setup
The application runs on HTTPS (port 8443) and expects a PKCS12 keystore at:
src/main/resources/keystore.p12


4.1. Generate the keystore (Windows CMD.exe)
Run this command from the project root (employeedetails directory) in Command Prompt (cmd.exe):
keytool -genkeypair -alias tomcat -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore src/main/resources/keystore.p12 -validity 3650 -storepass changeit -dname "CN=localhost, OU=Dev, O=Demo, L=City, S=State, C=US" -noprompt


What this does:
- Creates a file: src/main/resources/keystore.p12
- Uses alias: tomcat
- Password: changeit
- Valid for: 10 years
- Subject: CN=localhost (for local HTTPS)
If you use IntelliJ and the file doesn’t show up, right-click the project and select:
Reload from Disk

or ensure src/main/resources is marked as a Resources Root.
4.2. Optional: Verify the keystore
keytool -list -v -keystore src/main/resources/keystore.p12 -storetype PKCS12 -storepass changeit


You should see the alias tomcat and certificate details.

5. Build and run the application
From the project root:
5.1. Build
mvn clean install


5.2. Run
mvn spring-boot:run


The application starts on:
https://localhost:8443


Because a self-signed certificate is used, browsers and tools will consider it “untrusted”. For curl, we use -k to ignore certificate verification during local testing.

6. Authentication and roles
This demo uses in-memory users for simplicity.
6.1. Users
|  |  |  | 
|  |  |  | 
|  |  |  | 


6.2. Flow
- Client sends POST /auth/login with username/password
- App validates credentials
- App returns a JWT token
- Client sends this JWT in the Authorization: Bearer <TOKEN> header for protected endpoints like /api/employees

7. API documentation (Swagger)
Swagger UI is available at:
https://localhost:8443/swagger-ui.html


or
https://localhost:8443/swagger-ui/index.html


From there you can:
- Explore endpoints
- See request/response models
- Try out calls (with a token)

8. Testing the API from CMD (curl examples)
All examples below are CMD.exe friendly (Windows Command Prompt).
8.1. Step 1 — Obtain a JWT token
Use the manager account:
curl -k -s -X POST https://localhost:8443/auth/login -H "Content-Type: application/json" -d "{\"username\":\"manager\",\"password\":\"password\"}"


Example response:
{"token":"<JWT_TOKEN_HERE>"}


Copy the value of "token" (without quotes).

8.2. Step 2 — Call the Employee Search API
Use the token you copied and replace <TOKEN>:
curl -k -s "https://localhost:8443/api/employees?firstName=John" -H "Authorization: Bearer <TOKEN>"


Example (structure only):
curl -k -s "https://localhost:8443/api/employees?firstName=John" -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."


If the token has not expired and is valid:
- A ROLE_MANAGER user will see employee details including sensitive fields
- A ROLE_ANALYST user will see a limited view

9. Useful URLs summary
|  |  |  | 
|  |  |  | 
|  |  |  | 
|  |  |  | 
|  |  |  | 


All API calls must be made over HTTPS on port 8443.

10. Project structure (high-level)
A simplified view of the important parts:
employeedetails/
 ├─ src/main/java/.../controller/     # REST controllers
 ├─ src/main/java/.../service/        # Business logic
 ├─ src/main/java/.../model/          # Domain models / DTOs
 ├─ src/main/java/.../security/       # Security config, filters, JWT logic
 ├─ src/main/resources/
 │    ├─ application.properties       # App configuration (ports, SSL, etc.)
 │    └─ keystore.p12                 # HTTPS keystore (generated locally)
 └─ pom.xml                           # Maven build configuration





