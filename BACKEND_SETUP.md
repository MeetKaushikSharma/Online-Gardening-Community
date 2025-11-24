# Backend Setup Guide - Spring Boot + MySQL with JDBC

## 📋 What's Been Created

✅ **Spring Boot Project Structure** - All Java files created in `backend/`
✅ **Database Entities** - User, Tip, Discussion, Project, PendingContent, Setting
✅ **JPA Repositories** - JDBC/Database access layer
✅ **REST Controllers** - API endpoints for all features
✅ **CORS Configuration** - Frontend can communicate with backend

---

## 🚀 STEP 1: Prerequisites (Install These)

### Required Software:

1. **Java 17 or later**

   - Download from: https://www.oracle.com/java/technologies/downloads/
   - Verify: `java -version`

2. **Maven 3.8+**

   - Download from: https://maven.apache.org/download.cgi
   - Verify: `mvn -v`

3. **MySQL 8.0+** (Local installation)
   - Download from: https://dev.mysql.com/downloads/mysql/
   - OR use Docker: `docker run --name mysql -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mysql:8.0`

### IDE (Recommended):

- **IntelliJ IDEA Community** (free)
- **VS Code** with Java Extension Pack

---

## 🗄️ STEP 2: Create Database

### Option A: Local MySQL (If installed locally)

Open MySQL command line or MySQL Workbench and run:

```sql
CREATE DATABASE gardening_db;
USE gardening_db;

-- Tables will be auto-created by Spring Boot on startup
-- (We set: spring.jpa.hibernate.ddl-auto=update)
```

### Option B: Online Database (Railway.app) - RECOMMENDED

1. Go to https://railway.app
2. Sign up (free)
3. New Project → Add MySQL
4. Copy the connection details:
   - Host
   - Port (usually 3306)
   - Username
   - Password
   - Database name

---

## ⚙️ STEP 3: Configure Database Connection

Edit: `backend/src/main/resources/application.properties`

### For Local MySQL:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/gardening_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### For Railway (Online):

```properties
spring.datasource.url=jdbc:mysql://HOST:PORT/DATABASE?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

---

## 🔨 STEP 4: Build & Run Backend

Navigate to backend folder and run:

```bash
cd backend

# Download dependencies (first time only)
mvn clean install

# Run the application
mvn spring-boot:run
```

✅ **Success!** You should see:

```
Started GardeningCommunityApplication in X seconds
```

Backend runs on: **http://localhost:8080**

Test it: Open browser → `http://localhost:8080/api/users`

---

## 🎨 STEP 5: Update Frontend (Already Done)

The frontend files are already updated to call the backend API.

### Frontend API Base URL:

- **Development**: `http://localhost:8080/api`
- **Production**: Update in `.env.production`

---

## 📱 STEP 6: Run Frontend

```bash
cd frontend

# Install dependencies (first time only)
npm install

# Start development server
npm run dev
```

Frontend runs on: **http://localhost:5173**

---

## 🔌 API Endpoints Created

### Authentication

- `POST /api/auth/login` - Login user
- `POST /api/auth/register` - Register new gardener

### Users (Admin only)

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Tips

- `GET /api/tips` - Get all tips
- `POST /api/tips` - Create tip
- `PUT /api/tips/{id}` - Update tip
- `DELETE /api/tips/{id}` - Delete tip

### Discussions

- `GET /api/discussions` - Get all discussions
- `POST /api/discussions` - Create discussion
- `PUT /api/discussions/{id}` - Update discussion
- `DELETE /api/discussions/{id}` - Delete discussion

### Projects

- `GET /api/projects` - Get all projects
- `POST /api/projects` - Create project
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project

---

## 🌐 DEPLOYMENT TO PRODUCTION

### Backend Deployment (Railway.app - Free)

1. Push backend code to GitHub
2. Go to railway.app
3. New Project → Deploy from GitHub repo
4. Railway auto-detects Spring Boot
5. Get your backend URL: `https://your-app.railway.app`

### Frontend Deployment (Vercel - Free)

1. Push frontend code to GitHub
2. Go to vercel.com
3. Import project from GitHub
4. Set environment variable:
   - `VITE_API_URL=https://your-backend-railway-url/api`
5. Deploy!

---

## ✅ Testing Login Credentials

### Admin:

- Email: `admin`
- Password: `admin123@`
- Role: `admin`

### Test Gardener (Create via Registration):

1. Click "Register as Gardener"
2. Fill in details
3. Login with those credentials

---

## 🐛 Troubleshooting

### Backend won't start:

- Check Java version: `java -version` (must be 17+)
- Check Maven: `mvn -v`
- Clear Maven cache: `mvn clean`

### Database connection failed:

- Verify MySQL is running
- Check username/password in `application.properties`
- For Railway: check credentials are copied correctly

### Frontend can't reach backend:

- Verify backend is running on `localhost:8080`
- Check `frontend/.env.local` has correct API URL
- Refresh frontend in browser

### CORS errors:

- Backend CORS is already configured for `localhost:5173`
- After deployment, update CORS origins in `GardeningCommunityApplication.java`

---

## 📝 Next Steps (After Setup)

1. Test login with admin credentials
2. Test user registration
3. Create tips, discussions, projects
4. Test admin panel
5. Deploy to production

**You're all set!** Start with local development first. 🚀
