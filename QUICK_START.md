# 🚀 QUICK START GUIDE - Ready to Launch!

## ✅ What's Already Done

Your project is now fully organized with a complete Spring Boot backend!

```
gardening-community/
├── frontend/              (React + Vite) ✅ READY
├── backend/               (Spring Boot) ✅ CREATED
├── BACKEND_SETUP.md      (Detailed instructions)
├── README.md
└── .git/
```

### Backend Files Created:

✅ **pom.xml** - All dependencies configured
✅ **application.properties** - Database connection config
✅ **Entity Classes** - User, Tip, Discussion, Project, PendingContent, Setting
✅ **Repositories** - Database access (JDBC/JPA)
✅ **Controllers** - REST API endpoints
✅ **DTOs** - Request/Response objects
✅ **Main Application Class** - CORS configured

### Frontend Files Updated:

✅ **api.js** - Points to backend: `http://localhost:8080/api`
✅ **.env.local** - Development environment
✅ **.env.production** - Production environment

---

## 📋 Next Steps (In Order)

### STEP 1: Install Java & Maven (5 minutes)

```bash
# Check if Java is installed
java -version

# Check if Maven is installed
mvn -v

# If not installed:
# - Download Java 17+ from oracle.com
# - Download Maven from maven.apache.org
```

### STEP 2: Set Up Database (5 minutes)

**Choose ONE option:**

**Option A: Local MySQL (easiest for testing)**

```sql
CREATE DATABASE gardening_db;
```

**Option B: Railway.app (free, online)**

1. Go to https://railway.app
2. Sign up (free tier)
3. New Project → MySQL
4. Copy connection details

### STEP 3: Configure Backend (2 minutes)

Edit: `backend/src/main/resources/application.properties`

```properties
# For local MySQL:
spring.datasource.url=jdbc:mysql://localhost:3306/gardening_db
spring.datasource.username=root
spring.datasource.password=

# For Railway (online):
spring.datasource.url=jdbc:mysql://HOST:PORT/DB?useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=USERNAME
spring.datasource.password=PASSWORD
```

### STEP 4: Start Backend (first time takes ~1 min)

```bash
cd backend
mvn clean install
mvn spring-boot:run
```

✅ You should see: `Started GardeningCommunityApplication`

Backend runs on: **http://localhost:8080**

### STEP 5: Start Frontend

```bash
cd frontend
npm install  # (first time only)
npm run dev
```

Frontend runs on: **http://localhost:5173**

### STEP 6: Test Login

```
Email: admin
Password: admin123@
Role: admin
```

Or register a new gardener account!

---

## 🔌 API Endpoints Ready

All endpoints are automatically created:

```
POST   /api/auth/login           → Login
POST   /api/auth/register        → Register

GET    /api/users                → List all users
GET    /api/tips                 → List all tips
GET    /api/discussions          → List all discussions
GET    /api/projects             → List all projects

POST   /api/users                → Create user
POST   /api/tips                 → Create tip
POST   /api/discussions          → Create discussion
POST   /api/projects             → Create project

PUT    /api/users/{id}           → Update user
PUT    /api/tips/{id}            → Update tip
etc...
```

---

## 🗄️ Database Tables (Auto-Created)

When backend starts, these tables are automatically created:

- `users` - User accounts (admin + gardeners)
- `tips` - Gardening tips
- `discussions` - Community discussions
- `projects` - User projects
- `pending_content` - Moderation queue
- `settings` - System settings

---

## ⚠️ Common Issues & Fixes

### "Java not found"

```bash
# Download from: https://www.oracle.com/java/technologies/downloads/
# Then verify:
java -version
```

### "Maven not found"

```bash
# Download from: https://maven.apache.org/download.cgi
# Add to PATH and verify:
mvn -v
```

### "Database connection failed"

- Check MySQL is running
- Verify credentials in `application.properties`
- For Railway: copy credentials exactly as shown

### "Frontend can't connect to backend"

- Verify backend is running: `http://localhost:8080/api/tips`
- Check `.env.local` has: `VITE_API_URL=http://localhost:8080/api`
- Hard refresh frontend (Ctrl+Shift+R)

### "Port 8080 already in use"

```bash
# Change port in application.properties:
server.port=9090
```

---

## 🌐 Deploy to Production (When Ready)

### Backend → Railway.app (Free)

1. Push `backend/` folder to GitHub
2. Connect GitHub to Railway
3. Railway auto-detects Spring Boot
4. Get URL: `https://your-app-xxx.railway.app`

### Frontend → Vercel (Free)

1. Push `frontend/` folder to GitHub
2. Connect GitHub to Vercel
3. Set env var: `VITE_API_URL=https://your-backend-url/api`
4. Deploy!

---

## 📚 File Structure Reference

```
backend/
├── src/main/java/com/gardening/
│   ├── entity/          (Database models)
│   ├── repository/      (Database access - JDBC)
│   ├── controller/      (REST API endpoints)
│   ├── dto/            (Data Transfer Objects)
│   └── GardeningCommunityApplication.java
├── src/main/resources/
│   └── application.properties (Database config)
└── pom.xml             (Maven dependencies)

frontend/
├── src/
│   ├── components/
│   ├── pages/
│   ├── services/       (api.js - connects to backend)
│   ├── context/
│   └── store/
├── package.json
└── vite.config.js
```

---

## 🎯 Your Current Status

| Component           | Status            | Next Step                 |
| ------------------- | ----------------- | ------------------------- |
| Frontend React      | ✅ Ready          | Start with: `npm run dev` |
| Backend Spring Boot | ✅ Created        | Configure database        |
| Database            | ⏳ Need to set up | Follow STEP 2 above       |
| API Endpoints       | ✅ All created    | Will work after DB setup  |
| Authentication      | ✅ Ready          | Test with admin/admin123@ |

---

## 💡 Pro Tips

1. **Keep both servers running:** Terminal 1 for backend, Terminal 2 for frontend
2. **Check logs:** They tell you exactly what's wrong
3. **Test API directly:** Open `http://localhost:8080/api/tips` in browser
4. **Use Postman:** Great for testing APIs without frontend

---

## ❓ Need Help?

1. Check `BACKEND_SETUP.md` for detailed troubleshooting
2. Check terminal logs for error messages
3. Verify all prerequisites are installed
4. Make sure both frontend and backend are running

---

**You're all set! Start from STEP 1 above. 🚀**

Good luck! 🌿
