# testApiProject - LMS REST API

Production-ready Spring Boot REST API for a Learning Management System (LMS) with JWT authentication, role-based authorization, and MongoDB Atlas.

## Base Information

- Base URL: `http://localhost:8080`
- Auth type: `Bearer JWT`
- Default DB collections: `users`, `courses`, `enrollments`, `materials`
- Roles: `STUDENT`, `TEACHER`, `ADMIN`

## Authentication Header

Use this header for protected endpoints:

`Authorization: Bearer <JWT_TOKEN>`

## API Endpoints

### 1) Auth Controller (`/api/auth`)

#### Register
- Method: `POST`
- Path: `/api/auth/register`
- Access: Public
- Description: Create a user account (role defaults to `STUDENT`)

Request JSON:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "password": "Pass1234"
}
```

Success response (`201 Created`):
```json
{
  "token": "<jwt_token>",
  "role": "STUDENT",
  "userId": "65f05a3e6d30f06f0898ac11"
}
```

#### Login
- Method: `POST`
- Path: `/api/auth/login`
- Access: Public

Request JSON:
```json
{
  "email": "john@example.com",
  "password": "Pass1234"
}
```

Success response (`200 OK`):
```json
{
  "token": "<jwt_token>",
  "role": "STUDENT",
  "userId": "65f05a3e6d30f06f0898ac11"
}
```

---

### 2) User Controller (`/api/users`)

#### Get My Profile
- Method: `GET`
- Path: `/api/users/me`
- Access: `STUDENT`, `TEACHER`, `ADMIN`

Success response (`200 OK`):
```json
{
  "id": "65f05a3e6d30f06f0898ac11",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "enrolledYear": 2026,
  "createdAt": "2026-03-06T10:15:30.000Z",
  "role": "STUDENT"
}
```

---

### 3) Course Controller (`/api/courses`)

#### Create Course
- Method: `POST`
- Path: `/api/courses`
- Access: `ADMIN`

Request JSON:
```json
{
  "courseCode": "CS101",
  "courseName": "Introduction to Computer Science",
  "year": 2026
}
```

Success response (`201 Created`):
```json
{
  "id": "65f061f06d30f06f0898ac22",
  "courseCode": "CS101",
  "courseName": "Introduction to Computer Science",
  "year": 2026,
  "createdBy": "65f05a3e6d30f06f0898ac12",
  "createdAt": "2026-03-06T11:00:00.000Z"
}
```

#### Get Courses
- Method: `GET`
- Path: `/api/courses`
- Access: `STUDENT`, `TEACHER`, `ADMIN`
- Notes:
  - `STUDENT`: sees only courses matching `enrolledYear`
  - `TEACHER`/`ADMIN`: sees all courses

Success response (`200 OK`):
```json
[
  {
    "id": "65f061f06d30f06f0898ac22",
    "courseCode": "CS101",
    "courseName": "Introduction to Computer Science",
    "year": 2026,
    "createdBy": "65f05a3e6d30f06f0898ac12",
    "createdAt": "2026-03-06T11:00:00.000Z"
  }
]
```

---

### 4) Enrollment Controller (`/api/enrollments`)

#### Enroll to Course
- Method: `POST`
- Path: `/api/enrollments/{courseCode}`
- Access: `STUDENT`, `TEACHER`
- Example path: `/api/enrollments/CS101`
- Request body: None

Success response (`201 Created`):
```json
{
  "message": "Enrolled successfully"
}
```

#### Get My Enrolled Courses
- Method: `GET`
- Path: `/api/enrollments/my-courses`
- Access: `STUDENT`, `TEACHER`, `ADMIN`

Success response (`200 OK`):
```json
[
  "CS101",
  "CS102"
]
```

---

### 5) Material Controller (`/api/materials`)

#### Upload Course Material
- Method: `POST`
- Path: `/api/materials/upload`
- Access: `TEACHER`, `ADMIN`

Request JSON:
```json
{
  "courseCode": "CS101",
  "fileName": "Week1-Intro.pdf",
  "fileUrl": "https://cdn.example.com/materials/Week1-Intro.pdf"
}
```

Success response (`201 Created`):
```json
{
  "id": "65f0698a6d30f06f0898ac34",
  "courseCode": "CS101",
  "fileName": "Week1-Intro.pdf",
  "fileUrl": "https://cdn.example.com/materials/Week1-Intro.pdf",
  "uploadedBy": "65f05a3e6d30f06f0898ac12",
  "uploadDate": "2026-03-06T11:20:00.000Z"
}
```

#### Get Materials by Course
- Method: `GET`
- Path: `/api/materials/{courseCode}`
- Access: `STUDENT`, `TEACHER`, `ADMIN`
- Example path: `/api/materials/CS101`

Success response (`200 OK`):
```json
[
  {
    "id": "65f0698a6d30f06f0898ac34",
    "courseCode": "CS101",
    "fileName": "Week1-Intro.pdf",
    "fileUrl": "https://cdn.example.com/materials/Week1-Intro.pdf",
    "uploadedBy": "65f05a3e6d30f06f0898ac12",
    "uploadDate": "2026-03-06T11:20:00.000Z"
  }
]
```

---

### 6) Admin Controller (`/api/admin`)

All endpoints below require `ADMIN`.

#### Get All Users
- Method: `GET`
- Path: `/api/admin/users`

Success response (`200 OK`):
```json
[
  {
    "id": "65f05a3e6d30f06f0898ac11",
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "enrolledYear": 2026,
    "createdAt": "2026-03-06T10:15:30.000Z",
    "role": "STUDENT"
  }
]
```

#### Get All Students
- Method: `GET`
- Path: `/api/admin/students`

#### Get All Teachers
- Method: `GET`
- Path: `/api/admin/teachers`

#### Delete User
- Method: `DELETE`
- Path: `/api/admin/users/{id}`
- Example path: `/api/admin/users/65f05a3e6d30f06f0898ac11`
- Request body: None
- Success response: `204 No Content`

#### Reset User Password
- Method: `PATCH`
- Path: `/api/admin/users/{id}/reset-password`

Request JSON:
```json
{
  "newPassword": "NewPass1234"
}
```

Success response (`200 OK`):
```json
{
  "message": "Password reset successful"
}
```

#### Update User Role
- Method: `PATCH`
- Path: `/api/admin/users/{id}/role`
- Allowed values in request: `TEACHER`, `ADMIN`, `STUDENT`

Request JSON (promote to `TEACHER`):
```json
{
  "role": "TEACHER"
}
```

Request JSON (promote to `ADMIN`):
```json
{
  "role": "ADMIN"
}
```

Success response (`200 OK`):
```json
{
  "id": "65f05a3e6d30f06f0898ac11",
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "enrolledYear": 2026,
  "createdAt": "2026-03-06T10:15:30.000Z",
  "role": "TEACHER"
}
```

---

## Common Error Response

Example (`400`, `403`, `404`, `500`):
```json
{
  "timestamp": "2026-03-06T11:45:00.000Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "validationErrors": {
    "email": "must be a well-formed email address"
  }
}
```

## Quick Start

1. Configure MongoDB URI and JWT secret in `src/main/resources/application.properties`
2. Run:

```bash
./mvnw spring-boot:run
```

For Windows PowerShell:
```powershell
.\mvnw.cmd spring-boot:run
```
