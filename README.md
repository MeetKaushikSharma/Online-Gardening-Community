# 🌿 Gardening Community Platform

A full-stack gardening community application built with **React** (frontend) and **Spring Boot with JDBC** (backend), connected to a **MySQL database**.

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Prerequisites](#-prerequisites)
- [Local Development](#-local-development)
- [Database Setup](#-database-setup)
- [API Endpoints](#-api-endpoints)
- [Deployment](#-deployment)
- [GitHub Setup](#-github-setup)
- [Testing](#-testing)
- [Contributing](#-contributing)

## ✨ Features

- **User Management**: Role-based authentication (Admin/Gardener)
- **Discussion Forums**: Create and participate in gardening discussions
- **Project Management**: Track gardening projects with progress
- **Post & Comments**: Share posts and engage with comments
- **Admin Dashboard**: User management, content moderation
- **Modern UI**: Responsive design with dark/light theme support

## 🛠️ Tech Stack

### Frontend

- **React 18** - UI library
- **Vite** - Build tool
- **Tailwind CSS** - Styling
- **React Router** - Routing
- **Zustand** - State management
- **React Hook Form** - Form handling

### Backend

- **Spring Boot 3.2.0** - Framework
- **JDBC** - Database connectivity (no JPA/Hibernate)
- **MySQL** - Database
- **Maven** - Build tool
- **Java 17** - Programming language

### Database

- **MySQL** (FreeSQL Database)
- **JDBC Driver** - Direct database connection

## 📁 Project Structure

```
gardening-community/
├── backend/                    # Spring Boot backend
│   ├── src/main/java/com/gardening/
│   │   ├── controller/        # REST Controllers
│   │   ├── dao/               # Data Access Objects (JDBC)
│   │   ├── db/                # Database utilities
│   │   ├── entity/            # Plain Java entities
│   │   ├── dto/               # Data Transfer Objects
│   │   └── util/              # Utilities
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── schema.sql
│   ├── Dockerfile             # Docker configuration
│   ├── Procfile               # Heroku configuration
│   ├── railway.json           # Railway configuration
│   ├── render.yaml            # Render configuration
│   └── pom.xml
│
├── frontend/                   # React frontend
│   ├── src/
│   │   ├── components/        # React components
│   │   ├── context/           # React contexts
│   │   ├── pages/             # Page components
│   │   ├── services/          # API services
│   │   └── store/             # Zustand store
│   ├── Dockerfile             # Docker configuration
│   ├── vercel.json            # Vercel configuration
│   ├── netlify.toml           # Netlify configuration
│   ├── nginx.conf             # Nginx configuration
│   └── package.json
│
├── .gitignore                  # Git ignore rules
└── README.md                   # This file
```

## 📦 Prerequisites

- **Java 17+** - [Download](https://adoptium.net/)
- **Node.js 16+** - [Download](https://nodejs.org/)
- **Maven 3.6+** - [Download](https://maven.apache.org/)
- **MySQL Database** - FreeSQL Database (configured) or local MySQL

## 🚀 Local Development

### 1. Clone the Repository

```bash
git clone <your-repo-url>
cd gardening-community
```

### 2. Backend Setup

```bash
cd backend

# Build the project
mvn clean package -DskipTests

# Run the application
mvn spring-boot:run
```

Backend will start on: **http://localhost:8080**

### 3. Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev
```

Frontend will start on: **http://localhost:5173**

## 🗄️ Database Setup

The project uses **FreeSQL Database** (MySQL). Connection details are configured in:

**`backend/src/main/java/com/gardening/db/DatabaseConnection.java`**

```java
URL: jdbc:mysql://sql12.freesqldatabase.com:3306/sql12809214
User: sql12809214
Password: TnWjtUhaKQ
```

### Database Schema

Tables are created automatically via `schema.sql`:

- `users` - User accounts
- `posts` - Discussion posts
- `comments` - Post comments
- `projects` - Gardening projects

## 🔌 API Endpoints

### Authentication

- `POST /api/auth/login` - User login
- `POST /api/auth/register` - User registration

### Users

- `GET /api/users` - Get all users
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Posts

- `GET /api/posts` - Get all posts
- `GET /api/posts/{id}` - Get post by ID
- `POST /api/posts` - Create post
- `PUT /api/posts/{id}` - Update post
- `DELETE /api/posts/{id}` - Delete post

### Comments

- `GET /api/comments?postId={postId}` - Get comments for a post
- `POST /api/comments` - Create comment
- `DELETE /api/comments/{commentId}` - Delete comment

### Projects

- `GET /api/projects` - Get all projects
- `GET /api/projects/user/{userId}` - Get projects by user
- `POST /api/projects` - Create project
- `PUT /api/projects/{id}` - Update project
- `DELETE /api/projects/{id}` - Delete project

## 🔐 Default Credentials

### Admin Login

- **Email**: `admin`
- **Password**: `admin123@`
- **Role**: `ADMIN`

## 🌐 Deployment

### Backend Deployment

#### Option 1: Railway (Recommended)

1. Go to [railway.app](https://railway.app) and sign up with GitHub
2. Click "New Project" → "Deploy from GitHub repo"
3. Select `gardening-community` repository
4. Add service → Select `backend` directory
5. Set environment variables:
   ```
   DB_URL=jdbc:mysql://sql12.freesqldatabase.com:3306/sql12809214
   DB_USER=sql12809214
   DB_PASSWORD=TnWjtUhaKQ
   PORT=8080
   CORS_ALLOWED_ORIGINS=https://your-frontend-url.vercel.app
   ```
6. Deploy → Get your backend URL: `https://your-app.railway.app`

#### Option 2: Render

1. Go to [render.com](https://render.com) and sign up with GitHub
2. New → Web Service
3. Connect `gardening-community` repository
4. Settings:
   - **Root Directory**: `backend`
   - **Environment**: `Java`
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -jar target/gardening-community-1.0.0.jar`
5. Set environment variables (same as Railway)
6. Deploy → Get URL: `https://your-app.onrender.com`

#### Option 3: Docker

```bash
cd backend
docker build -t gardening-backend .
docker run -p 8080:8080 \
  -e DB_URL=jdbc:mysql://sql12.freesqldatabase.com:3306/sql12809214 \
  -e DB_USER=sql12809214 \
  -e DB_PASSWORD=TnWjtUhaKQ \
  gardening-backend
```

### Frontend Deployment

#### Option 1: Vercel (Recommended)

1. Go to [vercel.com](https://vercel.com) and sign up with GitHub
2. Add New Project → Import `gardening-community` repository
3. Settings:
   - **Framework Preset**: Vite
   - **Root Directory**: `frontend`
   - **Build Command**: `npm run build`
   - **Output Directory**: `dist`
4. Environment Variables:
   ```
   VITE_API_URL=https://your-backend.railway.app/api
   ```
5. Deploy → Get URL: `https://your-app.vercel.app`

#### Option 2: Netlify

1. Go to [netlify.com](https://netlify.com) and sign up with GitHub
2. Add new site → Import from Git
3. Select `gardening-community` repository
4. Settings:
   - **Base directory**: `frontend`
   - **Build command**: `npm run build`
   - **Publish directory**: `frontend/dist`
5. Environment Variables:
   ```
   VITE_API_URL=https://your-backend.railway.app/api
   ```
6. Deploy → Get URL: `https://your-app.netlify.app`

#### Option 3: Docker

```bash
cd frontend
docker build -t gardening-frontend .
docker run -p 80:80 \
  -e VITE_API_URL=https://your-backend.railway.app/api \
  gardening-frontend
```

## 📦 GitHub Setup

### 1. Initialize Git Repository

```bash
# Navigate to project root
cd gardening-community

# Initialize git (if not already)
git init

# Add all files
git add .

# Commit
git commit -m "Gardening Community Platform - Production Ready

Features:
- Spring Boot backend with JDBC
- React frontend with Vite
- MySQL database integration
- Docker support
- Deployment configs for Railway/Render/Vercel/Netlify"

# Add remote (replace YOUR_USERNAME)
git remote add origin https://github.com/YOUR_USERNAME/gardening-community.git

# Push to GitHub
git branch -M main
git push -u origin main
```

### 2. Environment Variables

The project uses environment variables for deployment. Set these in your hosting platform:

**Backend:**

- `DB_URL` - Database connection URL
- `DB_USER` - Database username
- `DB_PASSWORD` - Database password
- `PORT` - Server port (default: 8080)
- `CORS_ALLOWED_ORIGINS` - Allowed frontend origins (comma-separated)

**Frontend:**

- `VITE_API_URL` - Backend API URL

### 3. Post-Deployment Steps

1. **Update CORS**: After deploying frontend, update backend CORS environment variable with your frontend URL
2. **Test Connections**: Verify backend and frontend are communicating
3. **Test Features**: Login, registration, and all CRUD operations

## 🧪 Testing

### Test Database Connection

```bash
cd backend
mvn compile
java -cp "target/classes;$(mvn dependency:build-classpath -q -Dmdep.outputFile=/dev/stdout)" com.gardening.db.DatabaseConnection
```

### Test Backend API

```bash
# Test login endpoint
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin","password":"admin123@"}'
```

### Test Frontend

1. Start backend: `mvn spring-boot:run` (in backend directory)
2. Start frontend: `npm run dev` (in frontend directory)
3. Open `http://localhost:5173`
4. Test admin login: `admin` / `admin123@`

## 📝 Development Notes

- **No JPA/Hibernate**: Project uses pure JDBC with DAO pattern
- **Password Hashing**: BCrypt via `PasswordUtil`
- **CORS**: Configured for localhost and production domains
- **Role Standardization**: All roles stored as uppercase (ADMIN, GARDENER)
- **Environment Variables**: Used for deployment flexibility

## ✅ Project Status

- [x] All JPA/Repository code removed
- [x] Pure JDBC with DAO pattern implemented
- [x] Environment variables configured
- [x] Database connection tested
- [x] CORS configured for production
- [x] Docker files created
- [x] Deployment configs created (Railway, Render, Vercel, Netlify)
- [x] Unnecessary files removed
- [x] .gitignore configured
- [x] Build verified
- [x] Ready for production deployment

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Author

**Kaushik Sharma** - [GitHub](https://github.com/meetkaushiksharma)

---

Made with 💚 for the gardening community
