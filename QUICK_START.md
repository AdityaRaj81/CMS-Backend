# Quick Start Guide - Legal CMS Backend

This guide will help you get the backend running in just a few minutes.

## 🚀 Quick Start (5 Minutes)

### Step 1: Install Prerequisites

**Check Java:**
```bash
java -version
# Should show Java 17 or higher
```

**Check PostgreSQL:**
```bash
psql --version
# Should show PostgreSQL 14 or higher
```

**Check Maven:**
```bash
mvn --version
# Should show Maven 3.6 or higher
```

### Step 2: Setup Database

```bash
# Start PostgreSQL (if not running)
sudo systemctl start postgresql

# Create database
psql -U postgres -c "CREATE DATABASE legal_cms_db;"

# Run schema (optional - Hibernate will create tables)
psql -U postgres -d legal_cms_db -f database/schema.sql
```

### Step 3: Configure Application

The default configuration in `application.yml` uses:
- Database: `legal_cms_db`
- Username: `postgres`
- Password: `postgres`
- Port: `8080`

If your PostgreSQL credentials are different, update `src/main/resources/application.yml`.

### Step 4: Build and Run

```bash
# Build the project
mvn clean install -DskipTests

# Run the application
mvn spring-boot:run
```

### Step 5: Verify It's Running

Open your browser and visit:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **API Docs:** http://localhost:8080/api-docs

You should see the Swagger documentation with all available endpoints.

## 🧪 Test the API

### 1. Login as Admin

**Using curl:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@legalcms.com",
    "password": "admin123"
  }'
```

**Response:**
```json
{
  "jwtToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "role": "ADMIN",
  "userId": 1,
  "fullName": "System Admin",
  "email": "admin@legalcms.com"
}
```

### 2. Get User Profile

Copy the JWT token from the login response and use it:

```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### 3. Create a New Case

```bash
curl -X POST http://localhost:8080/api/cases/create \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE" \
  -H "Content-Type: application/json" \
  -d '{
    "caseTitle": "Ram Kumar vs State of Bihar",
    "caseType": "Title Suit",
    "caseNumber": "101/2024",
    "courtName": "Patna High Court",
    "status": "ACTIVE",
    "assignedAdvocateId": 2,
    "petitionerName": "Ram Kumar",
    "respondentName": "State of Bihar"
  }'
```

### 4. Search Cases

```bash
curl -X GET "http://localhost:8080/api/cases/search?caseNumber=101/2024" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

## 🎯 Using Swagger UI (Recommended)

The easiest way to test the API is through Swagger UI:

1. Open: http://localhost:8080/swagger-ui.html
2. Click "Authorize" button (🔒 icon)
3. Enter: `Bearer YOUR_JWT_TOKEN_HERE`
4. Click "Authorize"
5. Now you can test all endpoints directly from the browser!

## 📊 Default Users

The system comes with 3 default users:

| Email                  | Password      | Role      |
|------------------------|---------------|-----------|
| admin@legalcms.com     | admin123      | ADMIN     |
| rajesh@legalcms.com    | advocate123   | ADVOCATE  |
| priya@example.com      | client123     | CLIENT    |

## 🔍 Common Issues

### Issue 1: Port 8080 already in use
**Solution:** Change port in `application.yml`:
```yaml
server:
  port: 8081
```

### Issue 2: Database connection failed
**Solution:** 
```bash
# Check PostgreSQL is running
sudo systemctl status postgresql

# Check if database exists
psql -U postgres -l | grep legal_cms_db

# If not, create it
psql -U postgres -c "CREATE DATABASE legal_cms_db;"
```

### Issue 3: JWT token invalid
**Solution:** Get a fresh token by logging in again. Tokens expire after 24 hours.

## 🐳 Using Docker (Alternative)

If you prefer Docker:

```bash
# Build and start services
docker-compose up -d

# Check logs
docker-compose logs -f backend

# Stop services
docker-compose down
```

## 📝 Next Steps

1. ✅ API is running
2. ✅ Default users created
3. ✅ Swagger documentation accessible

**Now you can:**
- Create cases
- Upload documents
- Track hearings
- Integrate with your Next.js frontend

## 🎓 Learning Resources

- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **Full README:** README.md
- **Database Schema:** database/schema.sql
- **Example Requests:** docs/api-examples.md

## 💡 Pro Tips

1. **Use Swagger** for quick API testing
2. **Check logs** in console for debugging
3. **JWT tokens** are logged in DEBUG mode
4. **Database** is auto-created by Hibernate
5. **File uploads** go to `uploads/documents/` folder

## 🆘 Need Help?

If you encounter issues:
1. Check application logs in console
2. Verify PostgreSQL is running
3. Check database connection settings
4. Review Swagger UI for correct request format
5. Email: support@legalcms.com

---

**Happy Coding! 🚀**
