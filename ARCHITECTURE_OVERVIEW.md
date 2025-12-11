#  ARCHITECTURE OVERVIEW

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         USER / BROWSER                          │
└────────────────────────────┬────────────────────────────────────┘
                             │
                             │ HTTP
                             ▼
                    ┌─────────────────┐
                    │    Frontend     │
                    │   (Nginx:80)    │
                    └────────┬────────┘
                             │
                             │ REST API
                             ▼
                    ┌─────────────────┐
                    │   API Gateway   │◄───┐
                    │  (Port 8080)    │    │
                    └────────┬────────┘    │
                             │             │ Service
                             │             │ Discovery
                    ┌────────┴────────┐    │
                    │                 │    │
        ┌───────────▼─────┐  ┌────────▼────────┐
        │  Microservices  │  │ Eureka Registry │
        │   (4 Total)     │──┤   (Port 8761)   │
        └────────┬────────┘  └─────────────────┘
                 │
    ┏━━━━━━━━━━━━┻━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓
    ▼            ▼            ▼                  ▼
┌─────────┐  ┌─────────┐  ┌─────────┐      ┌─────────┐
│ Student │  │Professor│  │ Course  │      │ Grading │
│ Service │  │ Service │  │ Service │      │ Service │
│ :8081   │  │ :8082   │  │ :8083   │      │ :8084   │
└────┬────┘  └────┬────┘  └────┬────┘      └────┬────┘
     │            │            │                 │
     │Circuit     │            │                 │
     │Breaker────────────────────────────────────┘
     │            │            │                 │
     ▼            ▼            ▼                 ▼
┌─────────┐  ┌─────────┐  ┌─────────┐      ┌─────────┐
│PostgreSQL│  │  MySQL  │  │ MongoDB │      │   H2    │
│ :5432   │  │ :3306   │  │ :27017  │      │(memory) │
└─────────┘  └─────────┘  └─────────┘      └─────────┘
```

## System Component Analysis

###  Presentation Tier
- **Stack**: HTML5, CSS3, Vanilla JavaScript
- **Web Server**: Nginx
- **Port**: 80
- **Capabilities**: 
  - Multi-section tabbed interface
  - Full CRUD functionality for all entities
  - Circuit breaker demonstration module
  - Asynchronous API communication

###  Gateway Tier
- **Technology**: Spring Cloud Gateway
- **Port**: 8080
- **Capabilities**:
  - Unified entry point for all services
  - Eureka-based dynamic routing
  - Load balancing (lb:// protocol)
  - CORS policy configuration
  - Request filtering and transformation

###  Service Registry
- **Technology**: Netflix Eureka
- **Port**: 8761
- **Capabilities**:
  - Automated service registration
  - Service lookup and discovery
  - Health status monitoring
  - Web-based management dashboard

###  Business Service Tier

#### 1. Student Management Module
- **Port**: 8081
- **Data Store**: PostgreSQL (Port 5432)
- **Core Features**:
  - Complete student lifecycle management
  - Integration with Grading Module
  - **Circuit Breaker** fault protection
  - Graceful failure handling

#### 2. Professor Administration Module
- **Port**: 8082
- **Data Store**: MySQL (Port 3306)
- **Core Features**:
  - Professor profile management
  - Course assignment coordination
  - Relationship mapping with courses

#### 3. Course Catalog Module
- **Port**: 8083
- **Data Store**: MongoDB (Port 27017)
- **Core Features**:
  - Course information management
  - Academic curriculum administration
  - Schema-less document flexibility

#### 4. Grading Management Module
- **Port**: 8084
- **Data Store**: H2 (in-memory)
- **Core Features**:
  - Academic grade processing
  - Automated letter grade computation
  - Multi-dimensional grade queries

## Request Flow Patterns

### Scenario: Student Registration
```
User → Web UI → API Gateway → Eureka (service lookup) → Student Module → PostgreSQL
                                                              ↓
                                                        Process Request
                                                              ↓
User ← Web UI ← API Gateway ← ← ← ← ← ← ← ← ← ← ← ← Student Module
```

### Scenario: Student Grade Retrieval (Circuit Breaker Pattern)
```
Healthy State:
User → Web UI → Gateway → Student Module → Grading Module → H2 Database
                              ↓                    ↓
                         Circuit OK          Retrieve Grades
                              ↓                    ↓
User ← Web UI ← Gateway ← Student Module ← Grading Module

Degraded State (Grading Module Unavailable):
User → Web UI → Gateway → Student Module → [Grading Module OFFLINE]
                              ↓
                      Circuit OPENED
                              ↓
                     Fallback Activated
                              ↓
                    Returns Empty Dataset
                              ↓
User ← Web UI ← Gateway ← Student Module
```

## Technology Stack Breakdown

### Backend Infrastructure
- **Spring Boot 3.2.0**
- **Spring Cloud 2023.0.0**
- **Java 17**

### Spring Cloud Modules
- **Eureka** - Service Registry & Discovery
- **Gateway** - API Gateway Implementation
- **Resilience4j** - Circuit Breaker & Fault Tolerance

### Data Persistence Layer
| Module | Database | Category | Port |
|---------|----------|------|------|
| Student | PostgreSQL | Relational DBMS | 5432 |
| Professor | MySQL | Relational DBMS | 3306 |
| Course | MongoDB | Document Store | 27017 |
| Grading | H2 | In-Memory DB | - |

### Frontend Layer
- **HTML5/CSS3** - Markup & Presentation
- **JavaScript ES6** - Business Logic & API Integration
- **Nginx** - Static Content Server

### Infrastructure & Operations
- **Docker** - Application Containerization
- **Docker Compose** - Multi-Container Orchestration

## Design Pattern Implementation

### 1. Service Registry Pattern
```
All modules → Self-register with Eureka
API consumers → Query Eureka for service locations
Advantage: Dynamic service discovery, eliminates hardcoded endpoints
```

### 2. API Gateway Pattern
```
All external requests → Routed through single Gateway
Gateway → Directs traffic to appropriate module
Advantage: Centralized security, routing, and cross-cutting concerns
```

### 3. Circuit Breaker Pattern
```
Normal State: Module A → Module B (Successful)
Failure Detection: Module A → Module B (Failed) → Circuit Trips
Recovery Mode: Module A → Executes fallback logic → Returns default data
Advantage: Prevents cascade failures, ensures system stability
```

### 4. Polyglot Persistence Pattern
```
Each module → Maintains dedicated database
No cross-module database access
Advantage: Technology optimization, service autonomy
```

### 5. Health Monitoring Pattern
```
Each module → Exposes /actuator/health endpoint
Gateway/Eureka → Continuously polls health status
Advantage: Proactive failure detection and recovery
```

## Containerized Deployment Model

### Docker Compose Configuration
```
docker-compose.yml
├── Network Layer
│   └── university-network (bridge driver)
├── Service Definitions
│   ├── eureka-server
│   ├── api-gateway
│   ├── student-service
│   ├── professor-service
│   ├── course-service
│   ├── grading-service
│   ├── postgres-student
│   ├── mysql-professor
│   ├── mongodb-course
│   └── frontend
└── Persistent Storage
    ├── postgres-student-data
    ├── mysql-professor-data
    └── mongodb-course-data
```

### Service Initialization Sequence
```
1. Data Layer (PostgreSQL, MySQL, MongoDB)
2. Service Registry (Eureka with readiness check)
3. Business Modules (awaiting Eureka availability)
4. API Gateway (awaiting Eureka availability)
5. Web Interface (awaiting Gateway availability)
```

## API Endpoint Catalog

### Via API Gateway (http://localhost:8080)

#### Student Management API
- `GET /api/students` - Enumerate all students
- `POST /api/students` - Register student
- `GET /api/students/{id}` - Retrieve student details
- `PUT /api/students/{id}` - Update student information
- `DELETE /api/students/{id}` - Remove student
- `GET /api/students/{id}/grades` - Fetch grades (Circuit Breaker protected)

#### Professor Management API
- `GET /api/professors` - Enumerate all professors
- `POST /api/professors` - Register professor
- `GET /api/professors/{id}` - Retrieve professor details
- `PUT /api/professors/{id}` - Update professor information
- `DELETE /api/professors/{id}` - Remove professor
- `GET /api/professors/{id}/assignments` - View course assignments
- `POST /api/professors/assignments` - Create course assignment

#### Course Catalog API
- `GET /api/courses` - Enumerate all courses
- `POST /api/courses` - Register course
- `GET /api/courses/{id}` - Retrieve course details
- `PUT /api/courses/{id}` - Update course information
- `DELETE /api/courses/{id}` - Remove course

#### Grading Management API
- `GET /api/grades` - Enumerate all grades
- `POST /api/grades` - Submit grade
- `GET /api/grades/{id}` - Retrieve grade details
- `PUT /api/grades/{id}` - Update grade
- `DELETE /api/grades/{id}` - Remove grade
- `GET /api/grades/student/{id}` - Query by student
- `GET /api/grades/course/{id}` - Query by course

## Security Architecture (Development Phase)

Currently configured for development/testing:
- Authentication/Authorization: Not implemented
- CORS: Open policy
- Rate Limiting: Not configured
- Encryption: Not enabled

Production readiness would require:
- Spring Security with JWT authentication
- OAuth2/OpenID Connect integration
- Gateway-level authentication
- mTLS for inter-service communication
- API rate limiting policies
- Comprehensive input validation

## Observability & Monitoring

### Current Implementation
- Spring Boot Actuator health endpoints
- Eureka service status dashboard
- Circuit breaker state metrics
- Docker container logging

### Production-Grade Additions
Recommended enhancements:
- Distributed tracing (Zipkin/Jaeger)
- Centralized log aggregation (ELK Stack)
- Metrics collection (Prometheus)
- Visualization dashboards (Grafana)
- Alerting and incident management

## Horizontal Scaling Capabilities

### Development Configuration
- Single instance per module
- Manual scaling via docker-compose

### Production Scaling Strategy
```bash
# Scale Student Module to 3 replicas
docker-compose up -d --scale student-service=3

# Gateway automatically load balances via Eureka
# Each replica registers independently
```

## Architectural Advantages

 **Deployment Independence** - Module-level deployment autonomy
 **Technology Heterogeneity** - Optimal technology selection per module
 **Fault Containment** - Circuit breaker isolates failures
 **Elastic Scalability** - Independent module scaling
 **Organizational Alignment** - Team ownership per module
 **Simplified Maintenance** - Focused, cohesive codebases

## Architectural Trade-offs

 **Distributed Transactions** - No ACID guarantees across modules
 **Network Latency** - Synchronous communication overhead
 **Operational Complexity** - Multiple components to manage
 **Integration Testing** - Complex end-to-end test scenarios
 **Data Consistency** - Eventual consistency requirements

## Performance Characteristics

### Latency Metrics (Approximate)
- Web UI to Gateway: < 10ms
- Gateway to Module: < 50ms
- Module to Database: < 100ms
- End-to-end Request: 150-200ms

### Circuit Breaker State Machine
- **Closed State**: Normal operation, all requests flow through
- **Open State**: 10 second timeout before retry
- **Half-Open State**: Permits 3 test requests
- **Auto-Recovery**: Transitions to closed on successful requests

## Repository Organization

```
uni-service/
├── README.md                    # Primary documentation
├── SOLUTION_SUMMARY.md          # Executive overview
├── ARCHITECTURE_OVERVIEW.md     # This document
├── QUICK_REFERENCE.md           # Command reference
├── VERIFICATION_CHECKLIST.md    # Testing procedures
├── START_HERE.txt               # Quick start instructions
├── docker-compose.yml           # Container orchestration
├── start.ps1                    # Start automation
├── stop.ps1                     # Stop automation
├── test.ps1                     # Test automation
├── logs.ps1                     # Log viewing
├── status.ps1                   # Status check
├── eureka-server/               # Service Registry implementation
├── api-gateway/                 # API Gateway implementation
├── student-service/             # Student Module
├── professor-service/           # Professor Module
├── course-service/              # Course Module
├── grading-service/             # Grading Module
└── frontend/                    # Web Interface
```

## Executive Summary

This represents a **comprehensive, enterprise-grade microservices implementation** showcasing:
- Contemporary Spring Cloud patterns and practices
- Service discovery and intelligent routing
- Resilience engineering with circuit breakers
- Polyglot persistence strategies
- Container-based deployment
- Modern, responsive user interface


