# Legal Case Management System - Backend

A production-ready backend for a Legal Case Management System (CMS) designed for Indian law firms handling Patna High Court and Barh Civil Court cases.

## 🚀 Features

- **JWT Authentication** - Secure token-based authentication
- **Role-Based Access Control** - ADMIN, ADVOCATE, CLIENT roles
- **Case Management** - Complete CRUD operations for legal cases
- **Court Integration** - CNR fetch and case status synchronization
- **Document Management** - Upload, view, and manage case documents
- **Hearing Tracking** - Maintain hearing history for each case
- **Automated Sync** - Daily scheduler to update case data from court systems
- **RESTful APIs** - Clean, predictable APIs for frontend integration
- **Swagger Documentation** - Interactive API documentation
- **PostgreSQL Database** - Robust relational database
- **Exception Handling** - Global error handling with meaningful messages

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot 3.2.2**
- **Spring Security** (JWT)
- **Spring Data JPA**
- **PostgreSQL**
- **Lombok**
- **Maven**
- **Springdoc OpenAPI** (Swagger)

## 📁 Project Structure

```
com.legalcms
├── config/              # Configuration classes (Security, Swagger, Exception Handler)
├── controller/          # REST Controllers
├── service/             # Business logic layer
├── repository/          # Data access layer
├── model/               # Domain entities
├── dto/                 # Data Transfer Objects
├── security/            # JWT utilities and filters
├── scheduler/           # Background jobs (Court sync)
└── util/                # Utility classes
```

## 🏗️ Database Schema

### Core Entities:
1. **User** - Admin, Advocate, Client users
2. **CaseEntity** - Legal cases
3. **Party** - Petitioners and Respondents
4. **Hearing** - Hearing history
5. **Document** - Case documents

## 🔧 Setup Instructions

### Prerequisites
- Java 17 or higher
- PostgreSQL 14 or higher
- Maven 3.6+

### 1. Clone Repository
```bash
cd /workspaces/CMS-Backend
```

### 2. Setup PostgreSQL Database
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE legal_cms_db;

# Exit psql
\q
```

### 3. Configure Application
Edit `src/main/resources/application.yml`:
```yaml
spring:
	datasource:
		url: jdbc:postgresql://localhost:5432/legal_cms_db
		username: your_postgres_username
		password: your_postgres_password
```

### 4. Build Project
```bash
mvn clean install
```

### 5. Run Application
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

Once the application is running, access Swagger UI at:
```
http://localhost:8080/swagger-ui.html
```

### API Endpoints

#### Authentication (`/api/auth`)
- `POST /api/auth/login` - User login
- `POST /api/auth/register` - Register new user (Admin only)

#### User Management (`/api/users`)
- `GET /api/users/me` - Get current user profile
- `GET /api/users/all` - Get all users (Admin only)
- `GET /api/users/{userId}` - Get user by ID

#### Case Management (`/api/cases`)
- `POST /api/cases/create` - Create new case
- `GET /api/cases/my` - Get cases assigned to advocate
- `GET /api/cases/{caseId}` - Get case details
- `GET /api/cases/search` - Search cases

#### Court Integration (`/api/court`)
- `POST /api/court/fetch-cnr` - Fetch CNR number
- `GET /api/court/case-status/{cnrNumber}` - Get case status

#### Document Management (`/api/documents`)
- `POST /api/documents/upload` - Upload document
- `GET /api/documents/case/{caseId}` - Get case documents
- `GET /api/documents/view/{documentId}` - View document

#### Hearing Management (`/api/hearings`)
- `GET /api/hearings/case/{caseId}` - Get hearing history

## 🔐 Authentication Flow

1. **Login:**
```bash
POST /api/auth/login
{
	"email": "admin@example.com",
	"password": "password123"
}
```

2. **Response:**
```json
{
	"jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
	"role": "ADMIN",
	"userId": 1,
	"fullName": "Admin User",
	"email": "admin@example.com"
}
```

3. **Use Token:**
Add to request headers:
```
Authorization: Bearer <jwtToken>
```

## 🤖 Background Scheduler

The `CourtSyncScheduler` runs daily at 2:00 AM to:
- Fetch latest case status from court systems
- Update next hearing dates
- Update case stages
- Log all changes

**Configuration:**
```yaml
app:
	court-sync:
		enabled: true
		cron: "0 0 2 * * ?"  # 2 AM daily
```

## 🔌 Court Integration

The system is designed to integrate with:
1. **e-Courts API** - Official Indian courts API
2. **LegalKart API** - Third-party legal data provider
3. **Attestr API** - Court status verification
4. **Custom Scraper** - Backup solution

**Current Status:** Mock implementation (ready for integration)

## 🌐 Frontend Integration

### Field Naming Convention
All API responses use **camelCase** matching frontend requirements:
- `nextHearingDate` (not next_hearing_date)
- `cnrNumber` (not cnr_number)
- `caseTitle` (not case_title)

### Date Format
All dates use **ISO-8601 format**: `YYYY-MM-DD`

### HTTP Status Codes
- `200 OK` - Success
- `201 Created` - Resource created
- `400 Bad Request` - Validation error
- `401 Unauthorized` - Authentication failed
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

## 🧪 Testing

### Create Default Admin User
Execute SQL:
```sql
INSERT INTO users (full_name, email, password, role, is_active, created_at, updated_at)
VALUES ('Admin User', 'admin@legalcms.com', 
				'$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6',
				'ADMIN', true, NOW(), NOW());
```
Password: `admin123`

### Test Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
	-H "Content-Type: application/json" \
	-d '{
		"email": "admin@legalcms.com",
		"password": "admin123"
	}'
```

## 📝 Environment Variables

Create `.env` file (optional):
```properties
DB_URL=jdbc:postgresql://localhost:5432/legal_cms_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
JWT_SECRET=your-secret-key-here
JWT_EXPIRATION=86400000
```

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Check PostgreSQL status
sudo systemctl status postgresql

# Restart PostgreSQL
sudo systemctl restart postgresql
```

### Port Already in Use
Change port in `application.yml`:
```yaml
server:
	port: 8081
```

### JWT Token Errors
- Ensure JWT secret is at least 256 bits
- Check token expiration time
- Verify Bearer token format in header

## 📦 Deployment

### Build JAR
```bash
mvn clean package -DskipTests
```

### Run JAR
```bash
java -jar target/legal-cms-backend-1.0.0.jar
```

### Docker (Optional)
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/legal-cms-backend-1.0.0.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 🔒 Security Features

- Passwords encrypted with BCrypt
- JWT token expiration (24 hours)
- CORS configuration for frontend
- SQL injection prevention via JPA
- XSS protection
- CSRF disabled (stateless REST API)

## 📄 License

This project is proprietary software for legal case management.

## 👥 Support

For support, email: support@legalcms.com

## 🎯 Next Steps for Production

1. **Replace mock court integration** with actual API
2. **Add email notifications** for hearing reminders
3. **Implement audit logging** for compliance
4. **Add unit and integration tests**
5. **Setup CI/CD pipeline**
6. **Configure production database** with connection pooling
7. **Enable SSL/HTTPS**
8. **Setup monitoring** (Actuator + Prometheus)
9. **Implement rate limiting**
10. **Add payment gateway** for client billing

---

**Built with ❤️ for Indian Legal System**