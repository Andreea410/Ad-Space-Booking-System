# Ad Space Booking System

A full-stack application for managing advertising space bookings, built with Spring Boot (backend) and React + TypeScript (frontend).

---

## 📋 Table of Contents

- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [1. Clone the Repository](#1-clone-the-repository)
  - [2. Database Setup](#2-database-setup)
  - [3. Backend Setup](#3-backend-setup)
  - [4. Frontend Setup](#4-frontend-setup)
- [Running the Application](#running-the-application)
- [Running Tests](#running-tests)
- [API Documentation](#api-documentation)
- [Project Structure](#project-structure)
- [Design Decisions](#design-decisions)
- [Assumptions](#assumptions)
- [Future Improvements](#future-improvements)

---

## ✨ Features

### Backend
- RESTful API for ad spaces and booking requests
- Complete CRUD operations for ad spaces
- Booking request creation with validation
- Approve/Reject booking workflow
- Filtering by type, city, and status
- Sorting support for ad spaces
- Business rule validation (date validation, overlap detection)
- Comprehensive unit and integration tests
- Global exception handling with friendly error messages

### Frontend
- Browse available ad spaces with filters
- Create booking requests with validation
- View all booking requests with status filtering
- Approve/Reject pending bookings
- Edit and delete ad spaces
- Real-time cost calculation
- Form validation with Zod
- Responsive Material-UI design
- State management with Zustand
- Clean architecture with feature-based structure

---

## 🛠️ Tech Stack

### Backend
- **Framework**: Spring Boot 3.2
- **Language**: Java 17
- **Database**: PostgreSQL 15
- **Build Tool**: Gradle
- **Testing**: JUnit 5, Mockito
- **Validation**: Jakarta Validation
- **ORM**: Spring Data JPA (Hibernate)

### Frontend
- **Framework**: React 18
- **Language**: TypeScript 4.9
- **UI Library**: Material-UI (MUI) v5
- **State Management**: Zustand
- **Form Validation**: Zod
- **Date Handling**: Day.js
- **HTTP Client**: Fetch API
- **Build Tool**: Create React App

---

## 📦 Prerequisites

Before you begin, make sure you have the following installed on your computer:

### Required Software

1. **Java 17 or higher**
   - Download from: https://adoptium.net/temurin/releases/
   - Verify installation: Open terminal/command prompt and type:
     ```bash
     java -version
     ```
   - You should see something like: `openjdk version "17.0.x"`

2. **Node.js 16+ and npm**
   - Download from: https://nodejs.org/ (choose LTS version)
   - Verify installation:
     ```bash
     node -v
     npm -v
     ```
   - You should see version numbers like `v18.x.x` and `9.x.x`

3. **PostgreSQL 15**
   - **Option A: Install PostgreSQL directly**
     - Windows: Download from https://www.postgresql.org/download/windows/
     - Mac: Download from https://www.postgresql.org/download/macosx/ or use `brew install postgresql@15`
     - Linux: `sudo apt-get install postgresql-15`
   
   - **Option B: Use Docker (Recommended)**
     - Install Docker Desktop from: https://www.docker.com/products/docker-desktop/
     - Verify: `docker --version`

4. **Git**
   - Download from: https://git-scm.com/downloads
   - Verify: `git --version`

5. **A Code Editor** (Optional but recommended)
   - VS Code: https://code.visualstudio.com/
   - IntelliJ IDEA: https://www.jetbrains.com/idea/

---

## 🚀 Getting Started

### 1. Clone the Repository

#### Step 1.1: Open Terminal/Command Prompt

- **Windows**: Press `Win + R`, type `cmd`, press Enter
- **Mac**: Press `Cmd + Space`, type `terminal`, press Enter
- **Linux**: Press `Ctrl + Alt + T`

#### Step 1.2: Navigate to where you want to save the project

```bash
# Example: Go to your Documents folder
cd Documents

# Or create a new folder for projects
mkdir Projects
cd Projects
```

#### Step 1.3: Clone the repository

```bash
git clone <repository-url>
cd Ad-Space-Booking-System
```

**Note**: Replace `<repository-url>` with the actual repository URL (e.g., `https://github.com/Andreea410/Ad-Space-Booking-System.git`)

#### Step 1.4: Verify the files are there

```bash
# List all files
dir    # On Windows
ls     # On Mac/Linux
```

You should see folders like `backend`, `frontend`, and files like `README.md`.

---

### 2. Database Setup

You have two options: **Docker (easier)** or **Local PostgreSQL installation**.

#### Option A: Using Docker (Recommended - Easier!)

##### Step 2A.1: Start PostgreSQL container

Open a terminal and run:

```bash
docker run --name postgres-booking-system ^
  -e POSTGRES_DB=booking_system ^
  -e POSTGRES_USER=postgres ^
  -e POSTGRES_PASSWORD=password ^
  -p 5432:5432 ^
  -d postgres:15
```

**Mac/Linux users**: Replace `^` with `\`:
```bash
docker run --name postgres-booking-system \
  -e POSTGRES_DB=booking_system \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15
```

##### Step 2A.2: Verify the container is running

```bash
docker ps
```

You should see a container named `postgres-booking-system` with status "Up".

##### Step 2A.3: (Optional) View database logs

```bash
docker logs postgres-booking-system
```

##### Step 2A.4: Stop the database (when you're done)

```bash
docker stop postgres-booking-system
```

##### Step 2A.5: Start it again later

```bash
docker start postgres-booking-system
```

##### Step 2A.6: Remove the database (if you want to start fresh)

```bash
docker stop postgres-booking-system
docker rm postgres-booking-system
```

---

#### Option B: Using Local PostgreSQL Installation

##### Step 2B.1: Start PostgreSQL service

- **Windows**: 
  1. Open Services (Win + R, type `services.msc`)
  2. Find "postgresql-x64-15" 
  3. Right-click → Start

- **Mac**: 
  ```bash
  brew services start postgresql@15
  ```

- **Linux**: 
  ```bash
  sudo systemctl start postgresql
  ```

##### Step 2B.2: Connect to PostgreSQL

Open a terminal and run:

```bash
# Windows (if psql is in PATH)
psql -U postgres

# Linux
sudo -u postgres psql

# Mac
psql postegres
```

You'll see a prompt like: `postgres=#`

##### Step 2B.3: Create the database

Type these commands one by one:

```sql
CREATE DATABASE booking_system;
\l
```

You should see `booking_system` in the list.

##### Step 2B.4: Exit psql

```sql
\q
```

##### Step 2B.5: Update backend configuration (if using different credentials)

**Option 1: Edit application.properties directly**

Open `backend/src/main/resources/application.properties` and update the database credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booking_system
spring.datasource.username=postgres
spring.datasource.password=password
```

**Option 2: Use environment variables (recommended for production)**

You can also create a `.env` file in the backend folder, or copy the .env.example or set environment variables:

```bash
# Example environment variables
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=booking_system
export DB_USERNAME=postgres
export DB_PASSWORD=your_password
```

**Note**: The default configuration uses:
- **Database**: `booking_system`
- **Username**: `postgres`
- **Password**: `password`
- **Port**: `5432`

If you're using these defaults, you don't need to change anything!

---

### 3. Backend Setup

#### Step 3.1: Navigate to the backend folder

```bash
cd backend
```

#### Step 3.2: Make the run script executable (Mac/Linux only)

```bash
chmod +x run_backend.sh
```

#### Step 3.3: Run the backend

**Windows**:
```powershell
.\run_backend.ps1
```

**Mac/Linux**:
```bash
./run_backend.sh
```


#### Step 3.4: Wait for the backend to start

You'll see lots of text scrolling. Wait until you see:

```
Started BookingSystemApplication in X.XXX seconds
```

The backend is now running on **http://localhost:8080**

#### Step 3.5: Test the backend (Optional)

Open a new terminal (keep the backend running) and test:

```bash
curl http://localhost:8080/api/v1/ad-spaces
```

Or open your browser and go to: http://localhost:8080/api/v1/ad-spaces

You should see a JSON response with ad spaces.

#### Step 3.6: Stop the backend

Press `Ctrl + C` in the terminal where the backend is running.

---

### 4. Frontend Setup

#### Step 4.1: Open a NEW terminal

**Important**: Keep the backend terminal running! Open a new one.

#### Step 4.2: Navigate to the frontend folder

```bash
# If you're in the backend folder
cd ..
cd frontend

# Or from the project root
cd frontend
```

#### Step 4.3: Install dependencies

This will download all the required packages (might take 2-5 minutes):

```bash
npm install
```

You'll see a progress bar and lots of package names.

#### Step 4.4: Start the frontend

```bash
npm start
```

#### Step 4.5: Wait for the browser to open

The terminal will show:

```
Compiled successfully!

You can now view frontend in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.x.x:3000
```

Your browser should automatically open to **http://localhost:3000**

If it doesn't, manually open your browser and go to: http://localhost:3000

#### Step 4.6: Stop the frontend

Press `Ctrl + C` in the terminal where the frontend is running.

---

## 🎮 Running the Application

### Quick Start (After initial setup)

1. **Start the database** (if using Docker):
   ```bash
   docker start postgres-booking-system
   ```

2. **Start the backend** (in one terminal):
   ```bash
   cd backend
   .\run-backend.ps1        # Windows
   ./gradlew bootRun        # Mac/Linux
   ```

3. **Start the frontend** (in another terminal):
   ```bash
   cd frontend
   npm start
   ```

4. **Open your browser** to: http://localhost:3000

### Using the Application

#### Viewing Ad Spaces
1. The main page shows all available ad spaces
2. Use the **Type** dropdown to filter by ad space type (Billboard, Bus Stop, etc.)
3. Use the **City** dropdown to filter by city
4. Click column headers to sort (Name, City, Price, Type)
5. Click **Refresh** to reload the data

#### Creating a Booking
1. Click the **"Book Now"** button on any ad space
2. Fill in the form:
   - **Advertiser Name**: Your name or company (min 2 characters)
   - **Advertiser Email**: Valid email address
   - **Start Date**: Click the calendar icon or type DD/MM/YYYY (must be today or future)
   - **End Date**: Click the calendar icon or type DD/MM/YYYY (must be at least 7 days after start)
3. See the **Total Cost** calculated automatically
4. Click **"Submit Booking Request"**
5. You'll see a success message at the bottom of the screen

#### Managing Bookings
1. Click the **"Booking Requests"** tab at the top
2. View all booking requests in a table
3. Use the **Status** dropdown to filter (All, Pending, Approved, Rejected)
4. For **Pending** bookings, you'll see:
   - **Approve** button (green) - Click to approve the booking
   - **Reject** button (red) - Click to reject the booking
5. Confirm your action in the popup dialog
6. The list updates automatically

#### Editing Ad Spaces
1. Go to the **"Ad Spaces"** tab
2. Click the **Edit** icon (pencil) on any ad space
3. Change the name
4. Click **"Save"**

#### Deleting Ad Spaces
1. Go to the **"Ad Spaces"** tab
2. Click the **Delete** icon (trash) on any ad space
3. Confirm the deletion
4. **Note**: You cannot delete an ad space that has booking requests

---

## 🧪 Running Tests

### Backend Tests

#### Run all tests

```bash
cd backend

# Windows (PowerShell or Command Prompt)
.\gradlew.bat test

# Mac/Linux
./gradlew test
```

#### Run specific test class

```bash
# Windows (PowerShell or Command Prompt)
.\gradlew.bat test --tests "AdSpaceServiceTest"

# Mac/Linux
./gradlew test --tests "AdSpaceServiceTest"
```

#### View test report

After running tests, open:
```
backend/build/reports/tests/test/index.html
```

### Test Coverage

The backend includes:
- ✅ **Unit Tests**: 20+ tests for service layer business logic
- ✅ **Integration Tests**: 15+ tests for API endpoints
- ✅ **Test Coverage**: ~85% of business logic

**Key Test Files**:
- `AdSpaceServiceTest.java` - Tests for ad space operations
- `BookingRequestServiceTest.java` - Tests for booking logic
- `AdSpaceControllerTest.java` - Tests for ad space API endpoints
- `BookingRequestControllerTest.java` - Tests for booking API endpoints

### Frontend Tests

**Note**: Frontend tests were not implemented due to time constraints, but the structure supports adding Jest/React Testing Library tests.

---

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/v1
```

### Ad Spaces Endpoints

#### 1. List Ad Spaces
```http
GET /ad-spaces
```

**Query Parameters**:
- `type` (optional): Filter by type (BILLBOARD, BUS_STOP, MALL_DISPLAY, TRANSIT_AD)
- `city` (optional): Filter by city (case-insensitive, partial match)
- `sortBy` (optional): Sort field (name, city, pricePerDay, type)
- `sortOrder` (optional): Sort direction (asc, desc)

**Example**:
```
GET /ad-spaces?type=BILLBOARD&city=Bucharest&sortBy=pricePerDay&sortOrder=asc
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "name": "City Center Billboard",
    "type": "BILLBOARD",
    "city": "Bucharest",
    "address": "Piata Universitatii",
    "pricePerDay": 150.00,
    "status": "AVAILABLE"
  }
]
```

---

#### 2. Get Ad Space by ID
```http
GET /ad-spaces/{id}
```

**Response**: `200 OK` or `404 Not Found`

---

#### 3. Update Ad Space
```http
PATCH /ad-spaces/{id}
```

**Request Body**:
```json
{
  "name": "Updated Billboard Name"
}
```

**Response**: `200 OK` with updated ad space

---

#### 4. Delete Ad Space
```http
DELETE /ad-spaces/{id}
```

**Response**: `204 No Content` or `409 Conflict` (if has bookings)

---

### Booking Requests Endpoints

#### 1. Create Booking Request
```http
POST /booking-requests
```

**Request Body**:
```json
{
  "adSpaceId": 1,
  "advertiserName": "Acme Corp",
  "advertiserEmail": "contact@acme.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08"
}
```

**Validation Rules**:
- Start date must be today or in the future
- End date must be after start date
- Minimum duration: 7 days
- Ad space must be available
- No overlapping approved bookings

**Response**: `201 Created`
```json
{
  "id": 1,
  "adSpaceId": 1,
  "adSpaceName": "City Center Billboard",
  "advertiserName": "Acme Corp",
  "advertiserEmail": "contact@acme.com",
  "startDate": "2025-12-01",
  "endDate": "2025-12-08",
  "totalCost": 1050.00,
  "status": "PENDING"
}
```

**Error Response**: `400 Bad Request`
```json
{
  "status": 400,
  "error": "Booking validation failed",
  "message": "Start date must be in the future",
  "path": "/api/v1/booking-requests",
  "timestamp": "2025-11-30T12:00:00Z"
}
```

---

#### 2. List Booking Requests
```http
GET /booking-requests
```

**Query Parameters**:
- `status` (optional): Filter by status (PENDING, APPROVED, REJECTED)

**Example**:
```
GET /booking-requests?status=PENDING
```

**Response**: `200 OK` with array of bookings

---

#### 3. Get Booking by ID
```http
GET /booking-requests/{id}
```

**Response**: `200 OK` or `404 Not Found`

---

#### 4. Approve Booking
```http
PATCH /booking-requests/{id}/approve
```

**Response**: `200 OK` with updated booking

**Validation**:
- Booking must be in PENDING status
- No overlapping approved bookings for the same ad space

---

#### 5. Reject Booking
```http
PATCH /booking-requests/{id}/reject
```

**Response**: `200 OK` with updated booking

**Validation**:
- Booking must be in PENDING status

---

### Error Response Format

All errors follow this format:

```json
{
  "status": 400,
  "error": "Error Title",
  "message": "Detailed error message",
  "path": "/api/v1/endpoint",
  "timestamp": "2025-11-30T12:00:00Z"
}
```

**Common HTTP Status Codes**:
- `200 OK` - Success
- `201 Created` - Resource created
- `204 No Content` - Success with no response body
- `400 Bad Request` - Validation error
- `404 Not Found` - Resource not found
- `409 Conflict` - Business rule violation
- `500 Internal Server Error` - Server error

---

## 📁 Project Structure

```
Ad-Space-Booking-System/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/bookingsystem/
│   │   │   │   ├── controllers/        # REST API endpoints
│   │   │   │   ├── dto/                # Data Transfer Objects
│   │   │   │   ├── exception/          # Global exception handling
│   │   │   │   ├── model/              # JPA entities
│   │   │   │   ├── repository/         # Database repositories
│   │   │   │   ├── service/            # Business logic
│   │   │   │   └── config/             # Configuration classes
│   │   │   └── resources/
│   │   │       ├── application.yml     # App configuration
│   │   │       └── data.sql            # Sample data
│   │   └── test/                       # Unit & integration tests
│   ├── build.gradle                    # Gradle dependencies
│   └── run-backend.ps1                 # Backend startup script
│
├── frontend/
│   ├── src/
│   │   ├── api/                        # API client layer
│   │   │   ├── httpClient.ts           # HTTP utilities
│   │   │   ├── adSpaces.ts             # Ad space API calls
│   │   │   ├── bookings.ts             # Booking API calls
│   │   │   └── types.ts                # TypeScript interfaces
│   │   ├── components/
│   │   │   └── layout/                 # Layout components
│   │   ├── features/
│   │   │   ├── adSpaces/               # Ad spaces feature
│   │   │   │   ├── components/         # UI components
│   │   │   │   ├── hooks/              # Custom hooks
│   │   │   │   └── store/              # Zustand store
│   │   │   └── bookings/               # Bookings feature
│   │   │       ├── components/         # UI components
│   │   │       ├── hooks/              # Custom hooks
│   │   │       └── store/              # Zustand store
│   │   ├── shared/
│   │   │   ├── components/             # Reusable components
│   │   │   ├── hooks/                  # Reusable hooks
│   │   │   ├── utils/                  # Utility functions
│   │   │   └── validation/             # Zod schemas
│   │   ├── App.tsx                     # Main app component
│   │   └── index.tsx                   # Entry point
│   ├── package.json                    # npm dependencies
│   └── tsconfig.json                   # TypeScript config
│
└── README.md                           # This file
```

---

## 🎨 Design Decisions

### Backend Architecture

#### 1. **Layered Architecture**
- **Controllers**: Handle HTTP requests/responses, input validation
- **Services**: Contain business logic and orchestration
- **Repositories**: Data access layer (Spring Data JPA)
- **Entities**: Domain models with JPA annotations
- **DTOs**: Separate request/response objects to decouple API from entities

**Rationale**: Clear separation of concerns, easier testing, maintainability

#### 2. **Global Exception Handling**
- Centralized error handling with `@RestControllerAdvice`
- Consistent error response format
- Friendly error messages for clients

**Rationale**: DRY principle, consistent API responses, better client experience

#### 3. **Domain-Driven Validation**
- Business rules enforced in entity constructors and methods
- Services throw domain-specific exceptions
- Controllers stay thin

**Example**:
```java
public void approve() {
    if (this.status != BookingStatus.PENDING) {
        throw new IllegalStateException("Only pending bookings can be approved");
    }
    this.status = BookingStatus.APPROVED;
}
```

**Rationale**: Domain logic stays with domain objects, easier to test

#### 4. **Eager Fetching with JOIN FETCH**
- Used `@Query` with `JOIN FETCH` to avoid N+1 query problem
- Loads related entities in a single query

**Rationale**: Better performance, avoids lazy loading issues during JSON serialization

#### 5. **Audit Fields**
- All entities have `createdAt` and `updatedAt` timestamps
- Automatically managed with `@PrePersist` and `@PreUpdate`

**Rationale**: Track record history, useful for debugging and auditing

### Frontend Architecture

#### 1. **Feature-Based Structure**
- Code organized by feature (adSpaces, bookings) not by type
- Each feature has its own components, hooks, and store

**Rationale**: Scalability, easier to find related code, better encapsulation

#### 2. **Zustand for State Management**
- Lightweight, simple API compared to Redux
- No boilerplate, no context providers
- TypeScript-friendly

**Rationale**: Simplicity, performance, developer experience

#### 3. **Custom Hooks Pattern**
- Business logic extracted to custom hooks (e.g., `useAdSpaces`, `useBookings`)
- Components stay focused on UI

**Example**:
```typescript
export function useAdSpaces() {
  const store = useAdSpacesStore();
  
  useEffect(() => {
    store.loadAdSpaces();
  }, []);
  
  return {
    adSpaces: store.adSpaces,
    loading: store.loading,
    // ... handlers
  };
}
```

**Rationale**: Reusability, testability, separation of concerns

#### 4. **Shared Component Library**
- Reusable components: `AsyncContent`, `PageHeader`, `StatusChip`, `SortableTableHead`
- Consistent UI patterns across features

**Rationale**: DRY principle, consistency, faster development

#### 5. **Form Validation with Zod**
- Schema-based validation
- Type inference from schemas
- Clear error messages

**Rationale**: Type safety, declarative validation, better DX

#### 6. **Optimistic Updates**
- UI updates immediately on approve/reject
- Rollback on error (via store error handling)

**Rationale**: Better perceived performance, responsive UI

---

## 📝 Assumptions

1. **Single Tenant System**
   - No user authentication or multi-tenancy
   - Anyone can approve/reject bookings
   - In production, would add role-based access control

2. **Simple Availability Model**
   - Ad spaces have a global status (Available/Booked/Maintenance)
   - Doesn't track availability per date range
   - Approved bookings prevent overlaps but don't auto-update space status

3. **Date Handling**
   - All dates are in the server's timezone
   - Frontend displays dates in DD/MM/YYYY format
   - Backend stores dates as LocalDate (no time component)

4. **Price Calculation**
   - Total cost = (end date - start date) × price per day
   - Simplified calculation (no taxes, discounts, or complex pricing)

5. **Booking Workflow**
   - Linear workflow: Pending → Approved/Rejected
   - No cancellation or modification after approval
   - No payment processing

6. **Data Validation**
   - Email validation is format-only (doesn't verify existence)
   - City/address are free text (no geocoding or validation)
   - Minimum booking duration is 7 days (business requirement)

7. **Concurrent Bookings**
   - No locking mechanism for concurrent booking attempts
   - Database constraints prevent double-booking
   - In production, would add optimistic locking

8. **Sample Data**
   - `data.sql` provides initial ad spaces for testing
   - Database is recreated on each restart (for development)

---

## 🚀 Future Improvements

### If I had more time, I would add:

#### Backend
1. **Authentication & Authorization**
   - JWT-based authentication
   - Role-based access (Admin, Advertiser)
   - Only admins can approve/reject bookings

2. **Advanced Booking Features**
   - Booking modification/cancellation
   - Recurring bookings
   - Booking history and audit trail

3. **Payment Integration**
   - Payment processing (Stripe/PayPal)
   - Invoice generation
   - Payment status tracking

4. **Notifications**
   - Email notifications for booking status changes
   - Reminder emails before booking start date
   - Admin alerts for new bookings

5. **Advanced Search**
   - Full-text search for ad spaces
   - Geolocation-based search
   - Price range filtering
   - Availability calendar view

6. **Performance Optimizations**
   - Redis caching for frequently accessed data
   - Database query optimization
   - API rate limiting

7. **API Documentation**
   - Swagger/OpenAPI integration
   - Interactive API documentation
   - Request/response examples

8. **Monitoring & Logging**
   - Application performance monitoring (APM)
   - Structured logging with ELK stack
   - Health check endpoints

#### Frontend
1. **Enhanced UX**
   - Calendar view for bookings
   - Drag-and-drop booking creation
   - Image uploads for ad spaces
   - Map integration for location display

2. **Advanced Features**
   - Booking analytics dashboard
   - Revenue reports
   - Booking conflicts visualization
   - Bulk operations

3. **Testing**
   - Unit tests with Jest
   - Component tests with React Testing Library
   - E2E tests with Cypress/Playwright

4. **Performance**
   - Code splitting and lazy loading
   - Image optimization
   - Virtual scrolling for large lists
   - Service worker for offline support

5. **Accessibility**
   - ARIA labels and roles
   - Keyboard navigation
   - Screen reader support
   - WCAG 2.1 AA compliance

6. **Internationalization**
   - Multi-language support
   - Currency localization
   - Date/time format localization

#### DevOps
1. **CI/CD Pipeline**
   - Automated testing on commit
   - Automated deployment
   - Docker containerization
   - Kubernetes orchestration

2. **Environment Management**
   - Separate dev/staging/prod environments
   - Environment-specific configurations
   - Secrets management (Vault)

3. **Database**
   - Migration scripts (Flyway/Liquibase)
   - Database backups
   - Read replicas for scaling

---

## 🐛 Troubleshooting

### Backend Issues

#### Problem: "Port 8080 is already in use"
**Solution**: Another application is using port 8080. Either:
1. Stop the other application
2. Change the port in `application.yml`:
   ```yaml
   server:
     port: 8081
   ```

#### Problem: "Could not connect to database"
**Solution**: 
1. Check if PostgreSQL is running: `docker ps` or check Services
2. Verify database credentials in `application.yml`
3. Ensure database `booking_system` exists

#### Problem: "Gradle build failed"
**Solution**:
1. Clean the build: `.\gradlew.bat clean` (Windows) or `./gradlew clean` (Mac/Linux)
2. Rebuild: `.\gradlew.bat build` (Windows) or `./gradlew build` (Mac/Linux)
3. Check Java version: `java -version` (must be 17+)

### Frontend Issues

#### Problem: "npm install fails"
**Solution**:
1. Delete `node_modules` folder and `package-lock.json`
2. Run `npm install` again
3. Try `npm install --legacy-peer-deps` if there are dependency conflicts

#### Problem: "Cannot connect to backend"
**Solution**:
1. Ensure backend is running on port 8080
2. Check browser console for CORS errors
3. Verify `proxy` in `package.json` is set to `http://localhost:8080`

#### Problem: "Page shows blank screen"
**Solution**:
1. Check browser console for errors (F12)
2. Clear browser cache and reload
3. Ensure all dependencies are installed

### Database Issues

#### Problem: "Docker container won't start"
**Solution**:
1. Check if port 5432 is already in use
2. Remove old container: `docker rm postgres-booking-system`
3. Try again with the `docker run` command

#### Problem: "Permission denied" errors
**Solution**:
1. On Mac/Linux, use `sudo` for Docker commands
2. Ensure your user is in the `docker` group

---

## 📞 Support

If you encounter any issues:

1. Check the [Troubleshooting](#troubleshooting) section
2. Review the error messages carefully
3. Check if all prerequisites are installed
4. Ensure the database is running
5. Verify both backend and frontend are running

---

## 🎉 Thank You!

Thank you for reviewing this project! I hope this README makes it easy to set up and run the application. If you have any questions or feedback, I'd love to hear them during the interview!
