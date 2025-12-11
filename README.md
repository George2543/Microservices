# Academic Portal System - Enterprise Microservices Platform

An advanced microservices ecosystem for university management, leveraging Spring Boot, Spring Cloud Netflix, and containerization technologies.

##  Core Capabilities

### System Components
- **Eureka Service Registry** - Dynamic service discovery and health monitoring
- **Spring Cloud Gateway** - Centralized API routing and load distribution  
- **Four Independent Microservices**:
  - Student Management Module (PostgreSQL database)
  - Professor Administration (MySQL database)
  - Course Catalog System (MongoDB database)
  - Academic Grading Platform (H2 in-memory database)
- **Resilience4j Circuit Breaker** - Fault tolerance in Student Module
- **Responsive Web Interface** - Modern UI for comprehensive system interaction

### Implemented Design Patterns
 Dynamic Service Discovery (Netflix Eureka)
 Centralized API Gateway (Spring Cloud Gateway)
 Fault Tolerance & Circuit Breaking (Resilience4j)
 Polyglot Persistence (PostgreSQL, MySQL, MongoDB, H2)
 RESTful Synchronous Communication

##  Quick Deployment Guide

### System Requirements
- Docker Desktop (running)
- Minimum 8GB RAM allocation for Docker
- Available ports: 80, 8080-8084, 8761, 3306, 5432, 27017

### Launch Complete Infrastructure

```powershell
# Initialize and launch all services
docker-compose up --build -d
```

This command orchestrates:
1. Database layer provisioning (PostgreSQL, MySQL, MongoDB)
2. Eureka Service Registry initialization
3. API Gateway deployment
4. All four microservice instances
5. Frontend web application
6. Network configuration and health checks

### System Access Points

- **Web Portal**: http://localhost
- **Gateway Endpoint**: http://localhost:8080
- **Service Registry Console**: http://localhost:8761
- **Student Module**: http://localhost:8081
- **Professor Module**: http://localhost:8082
- **Course Module**: http://localhost:8083
- **Grading Module**: http://localhost:8084

##  Microservice Specifications

### 1. Student Administration Module
- **Port**: 8081
- **Database**: PostgreSQL (port 5432)
- **API Endpoints**:
  - GET /api/students - Retrieve all students
  - GET /api/students/{id} - Retrieve specific student
  - POST /api/students - Register new student
  - PUT /api/students/{id} - Modify student details
  - DELETE /api/students/{id} - Remove student record
  - GET /api/students/{id}/grades - Fetch grades (Circuit Breaker protected)

### 2. Professor Administration Module
- **Port**: 8082
- **Database**: MySQL (port 3306)
- **API Endpoints**:
  - GET /api/professors - Retrieve all professors
  - GET /api/professors/{id} - Retrieve specific professor
  - POST /api/professors - Add new professor
  - PUT /api/professors/{id} - Modify professor details
  - DELETE /api/professors/{id} - Remove professor record
  - GET /api/professors/{id}/assignments - View course assignments
  - POST /api/professors/assignments - Create course assignment

### 3. Course Catalog Module
- **Port**: 8083
- **Database**: MongoDB (port 27017)
- **API Endpoints**:
  - GET /api/courses - Retrieve all courses
  - GET /api/courses/{id} - Retrieve specific course
  - POST /api/courses - Register new course
  - PUT /api/courses/{id} - Modify course information
  - DELETE /api/courses/{id} - Remove course record

### 4. Academic Grading Module
- **Port**: 8084
- **Database**: H2 (in-memory)
- **API Endpoints**:
  - GET /api/grades - Retrieve all grade records
  - GET /api/grades/{id} - Retrieve specific grade
  - GET /api/grades/student/{studentId} - Query grades by student
  - GET /api/grades/course/{courseId} - Query grades by course
  - POST /api/grades - Submit new grade
  - PUT /api/grades/{id} - Modify grade record
  - DELETE /api/grades/{id} - Remove grade record

##  Circuit Breaker Validation

The Circuit Breaker pattern protects the Student Module when communicating with the Grading Module.

### Testing Procedure:
1. Navigate to http://localhost
2. Register a student (Students section)
3. Create grade entries for the student (Grades section)
4. Access "Circuit Breaker" section
5. Select student and initiate "Test Circuit Breaker" - Expected: Normal response
6. Halt the Grading Module:
   ```powershell
   docker stop grading-service
   ```
7. Re-test Circuit Breaker - Expected: Fallback behavior activated
8. Restart Grading Module:
   ```powershell
   docker start grading-service
   ```

### Circuit Breaker Parameters
- **Sliding Window**: 10 requests
- **Failure Threshold**: 50%
- **Open State Duration**: 10 seconds
- **Half-Open Test Calls**: 3

##  System Monitoring

### Eureka Registry Dashboard
Access http://localhost:8761 for:
- Registered service inventory
- Service instance health metrics
- Heartbeat monitoring timestamps

### Spring Boot Actuator Endpoints
Each module exposes monitoring endpoints:
- http://localhost:8081/actuator/health - Student Module status
- http://localhost:8081/actuator/circuitbreakers - Circuit breaker metrics
- Equivalent endpoints available on respective module ports

##  Development Environment

### Repository Structure
```
uni-service/
├── eureka-server/          # Service Registry
├── api-gateway/            # API Gateway
├── student-service/        # Student Module
├── professor-service/      # Professor Module
├── course-service/         # Course Module
├── grading-service/        # Grading Module
├── frontend/               # Web Interface
└── docker-compose.yml      # Container Orchestration
```

### Technology Foundation
- **Core Framework**: Spring Boot 3.2.0
- **Service Discovery**: Spring Cloud Netflix Eureka
- **Gateway**: Spring Cloud Gateway
- **Resilience**: Resilience4j
- **Data Stores**: PostgreSQL, MySQL, MongoDB, H2
- **Frontend**: HTML5, CSS3, Vanilla JavaScript
- **Containers**: Docker, Docker Compose

### Individual Service Build

```powershell
# Compile specific module
cd student-service
mvn clean package

# Create container image
docker build -t student-service .
```

##  API Testing via Postman

Access all endpoints through Gateway at http://localhost:8080

Sample API Calls:

**Student Registration:**
```
POST http://localhost:8080/api/students
Content-Type: application/json

{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@university.edu",
  "phone": "555-9876",
  "address": "456 Oak Avenue"
}
```

**Grade Submission:**
**Grade Submission:**
```
POST http://localhost:8080/api/grades
Content-Type: application/json

{
  "studentId": 1,
  "courseId": 1,
  "gradeValue": 92.3
}
```

##  System Teardown

Terminate and remove all containers:
```powershell
docker-compose down
```

Complete cleanup (containers, networks, volumes):
```powershell
docker-compose down -v
```

##  Architectural Benefits

### System Strengths:
- **Independent Scalability**: Scale each module based on demand
- **Technology Flexibility**: Optimal database selection per module
- **System Resilience**: Circuit breaker prevents cascading failures
- **Maintainability**: Loosely coupled, cohesive services
- **Dynamic Discovery**: Automatic service registration and lookup

### Considerations:
- **Distributed Transactions**: No cross-service ACID guarantees
- **Network Overhead**: Inter-service communication latency
- **Data Consistency**: Eventual consistency model
- **Operational Complexity**: Multiple components to orchestrate
- **Observability**: Requires distributed tracing for debugging

##  Applied Microservice Patterns

1. **Service Registry**: Eureka-based dynamic service discovery
2. **API Gateway**: Centralized routing and load balancing
3. **Circuit Breaker**: Fault isolation and graceful degradation
4. **Database per Service**: Independent data ownership
5. **Externalized Configuration**: Environment-based settings
6. **Health Check**: Actuator-based service monitoring

