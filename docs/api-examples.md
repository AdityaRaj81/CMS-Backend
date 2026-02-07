# API Examples - Legal CMS Backend

Complete collection of API request/response examples for testing.

## 🔐 Authentication

### 1. Login

**Request:**
```bash
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@legalcms.com",
  "password": "admin123"
}
```

**Response:**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbkBsZWdhbGNtcy5jb20iLCJpYXQiOjE3MDcyOTQ0MDAsImV4cCI6MTcwNzM4MDgwMH0.xyz",
  "role": "ADMIN",
  "userId": 1,
  "fullName": "System Admin",
  "email": "admin@legalcms.com"
}
```

### 2. Register New User (Admin Only)

**Request:**
```bash
POST http://localhost:8080/api/auth/register
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "fullName": "Amit Verma",
  "email": "amit@legalcms.com",
  "password": "password123",
  "role": "ADVOCATE",
  "isActive": true
}
```

**Response:**
```json
{
  "id": 4,
  "fullName": "Amit Verma",
  "email": "amit@legalcms.com",
  "role": "ADVOCATE",
  "isActive": true,
  "createdAt": "2024-02-07T10:30:00"
}
```

## 👤 User Management

### 3. Get Current User Profile

**Request:**
```bash
GET http://localhost:8080/api/users/me
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
{
  "id": 1,
  "fullName": "System Admin",
  "email": "admin@legalcms.com",
  "role": "ADMIN",
  "isActive": true,
  "createdAt": "2024-02-07T09:00:00"
}
```

### 4. Get All Users (Admin Only)

**Request:**
```bash
GET http://localhost:8080/api/users/all
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
[
  {
    "id": 1,
    "fullName": "System Admin",
    "email": "admin@legalcms.com",
    "role": "ADMIN",
    "isActive": true,
    "createdAt": "2024-02-07T09:00:00"
  },
  {
    "id": 2,
    "fullName": "Rajesh Kumar",
    "email": "rajesh@legalcms.com",
    "role": "ADVOCATE",
    "isActive": true,
    "createdAt": "2024-02-07T09:05:00"
  }
]
```

## 📋 Case Management

### 5. Create New Case

**Request:**
```bash
POST http://localhost:8080/api/cases/create
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "caseTitle": "Ram Kumar vs State of Bihar",
  "caseType": "Title Suit",
  "caseNumber": "101/2024",
  "cnrNumber": "BIHC01-101-2024",
  "courtName": "Patna High Court",
  "nextHearingDate": "2024-03-15",
  "caseStage": "Arguments Stage",
  "status": "ACTIVE",
  "assignedAdvocateId": 2,
  "petitionerName": "Ram Kumar",
  "respondentName": "State of Bihar"
}
```

**Response:**
```json
{
  "id": 1,
  "caseTitle": "Ram Kumar vs State of Bihar",
  "caseType": "Title Suit",
  "caseNumber": "101/2024",
  "cnrNumber": "BIHC01-101-2024",
  "courtName": "Patna High Court",
  "nextHearingDate": "2024-03-15",
  "caseStage": "Arguments Stage",
  "status": "ACTIVE",
  "assignedAdvocate": {
    "id": 2,
    "fullName": "Rajesh Kumar",
    "email": "rajesh@legalcms.com",
    "role": "ADVOCATE"
  },
  "parties": [
    {
      "id": 1,
      "petitionerName": "Ram Kumar",
      "respondentName": "State of Bihar"
    }
  ],
  "createdAt": "2024-02-07T10:45:00",
  "updatedAt": "2024-02-07T10:45:00"
}
```

### 6. Get My Cases (Advocate)

**Request:**
```bash
GET http://localhost:8080/api/cases/my
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
[
  {
    "id": 1,
    "caseTitle": "Ram Kumar vs State of Bihar",
    "caseNumber": "101/2024",
    "courtName": "Patna High Court",
    "nextHearingDate": "2024-03-15",
    "status": "ACTIVE",
    "createdAt": "2024-02-07T10:45:00"
  }
]
```

### 7. Get Case by ID

**Request:**
```bash
GET http://localhost:8080/api/cases/1
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
{
  "id": 1,
  "caseTitle": "Ram Kumar vs State of Bihar",
  "caseType": "Title Suit",
  "caseNumber": "101/2024",
  "cnrNumber": "BIHC01-101-2024",
  "courtName": "Patna High Court",
  "nextHearingDate": "2024-03-15",
  "caseStage": "Arguments Stage",
  "status": "ACTIVE",
  "assignedAdvocate": {
    "id": 2,
    "fullName": "Rajesh Kumar",
    "email": "rajesh@legalcms.com",
    "role": "ADVOCATE"
  },
  "parties": [
    {
      "id": 1,
      "petitionerName": "Ram Kumar",
      "respondentName": "State of Bihar"
    }
  ],
  "createdAt": "2024-02-07T10:45:00",
  "updatedAt": "2024-02-07T10:45:00"
}
```

### 8. Search Cases

**By Case Number:**
```bash
GET http://localhost:8080/api/cases/search?caseNumber=101/2024
Authorization: Bearer YOUR_JWT_TOKEN
```

**By CNR Number:**
```bash
GET http://localhost:8080/api/cases/search?cnrNumber=BIHC01-101-2024
Authorization: Bearer YOUR_JWT_TOKEN
```

**By Party Name:**
```bash
GET http://localhost:8080/api/cases/search?partyName=Ram Kumar
Authorization: Bearer YOUR_JWT_TOKEN
```

## ⚖️ Court Integration

### 9. Fetch CNR Number

**Request:**
```bash
POST http://localhost:8080/api/court/fetch-cnr
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: application/json

{
  "caseNumber": "101",
  "year": "2024",
  "courtName": "Patna High Court"
}
```

**Response:**
```json
{
  "cnrNumber": "BIHC01-101-2024",
  "caseNumber": "101/2024",
  "courtName": "Patna High Court"
}
```

### 10. Get Case Status by CNR

**Request:**
```bash
GET http://localhost:8080/api/court/case-status/BIHC01-101-2024
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
{
  "cnrNumber": "BIHC01-101-2024",
  "nextHearingDate": "2024-03-15",
  "caseStage": "Arguments Stage",
  "lastOrderUrl": "https://example.com/orders/BIHC01-101-2024.pdf"
}
```

## 📄 Document Management

### 11. Upload Document

**Request:**
```bash
POST http://localhost:8080/api/documents/upload
Authorization: Bearer YOUR_JWT_TOKEN
Content-Type: multipart/form-data

file: [PDF file]
caseId: 1
documentType: PETITION
```

**Response:**
```json
{
  "id": 1,
  "caseId": 1,
  "fileName": "petition.pdf",
  "fileUrl": "uploads/documents/a1b2c3d4-e5f6-4789-1011-121314151617.pdf",
  "documentType": "PETITION",
  "uploadedByName": "Rajesh Kumar",
  "uploadedAt": "2024-02-07T11:00:00"
}
```

### 12. Get Documents by Case

**Request:**
```bash
GET http://localhost:8080/api/documents/case/1
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
[
  {
    "id": 1,
    "caseId": 1,
    "fileName": "petition.pdf",
    "fileUrl": "uploads/documents/a1b2c3d4-e5f6-4789-1011-121314151617.pdf",
    "documentType": "PETITION",
    "uploadedByName": "Rajesh Kumar",
    "uploadedAt": "2024-02-07T11:00:00"
  }
]
```

### 13. View Document

**Request:**
```bash
GET http://localhost:8080/api/documents/view/1
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:** PDF file stream (opens in browser)

## 📅 Hearing Management

### 14. Get Hearing History

**Request:**
```bash
GET http://localhost:8080/api/hearings/case/1
Authorization: Bearer YOUR_JWT_TOKEN
```

**Response:**
```json
[
  {
    "id": 1,
    "caseId": 1,
    "hearingDate": "2024-02-07",
    "stage": "First Hearing",
    "remarks": "Case admitted. Next date for arguments."
  },
  {
    "id": 2,
    "caseId": 1,
    "hearingDate": "2024-03-15",
    "stage": "Arguments Stage",
    "remarks": "Scheduled for arguments presentation."
  }
]
```

## ❌ Error Responses

### Validation Error (400)
```json
{
  "timestamp": "2024-02-07T11:30:00",
  "status": 400,
  "error": "Validation Failed",
  "errors": {
    "email": "Invalid email format",
    "password": "Password is required"
  },
  "path": "/api/auth/login"
}
```

### Unauthorized (401)
```json
{
  "timestamp": "2024-02-07T11:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid email or password",
  "path": "/api/auth/login"
}
```

### Forbidden (403)
```json
{
  "timestamp": "2024-02-07T11:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "You don't have permission to access this resource",
  "path": "/api/auth/register"
}
```

### Not Found (404)
```json
{
  "timestamp": "2024-02-07T11:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with email: test@example.com",
  "path": "/api/users/me"
}
```

### Internal Server Error (500)
```json
{
  "timestamp": "2024-02-07T11:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "An unexpected error occurred. Please try again later.",
  "path": "/api/cases/create"
}
```

## 🧪 Testing Workflow

1. **Login** to get JWT token
2. **Create** an advocate user (if testing as admin)
3. **Create** a new case
4. **Upload** documents for the case
5. **View** case details
6. **Search** for cases
7. **Get** hearing history

## 📝 Notes

- Replace `YOUR_JWT_TOKEN` with actual token from login response
- All dates are in `YYYY-MM-DD` format
- File uploads require `multipart/form-data` content type
- Tokens expire after 24 hours (86400000 ms)

---

**For interactive testing, use Swagger UI:** http://localhost:8080/swagger-ui.html
