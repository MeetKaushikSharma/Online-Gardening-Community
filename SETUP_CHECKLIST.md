# ✅ SETUP CHECKLIST

## BEFORE YOU START

- [ ] Downloaded Java 17 or later
- [ ] Downloaded Maven 3.8+
- [ ] Java & Maven are in your PATH (verify with `java -version` and `mvn -v`)

## DATABASE SETUP

- [ ] Created database (local MySQL or Railway)
- [ ] Have connection details ready (HOST, PORT, USERNAME, PASSWORD, DATABASE)

## BACKEND CONFIGURATION

- [ ] Opened `backend/src/main/resources/application.properties`
- [ ] Updated these 3 lines with your database details:
  ```
  spring.datasource.url=
  spring.datasource.username=
  spring.datasource.password=
  ```
- [ ] Saved the file

## BACKEND BUILD & RUN

- [ ] Opened PowerShell/Command Prompt
- [ ] Navigated to `backend` folder
- [ ] Ran `mvn clean install` (wait for it to finish)
- [ ] Ran `mvn spring-boot:run`
- [ ] Saw message: "Started GardeningCommunityApplication"
- [ ] Backend is running on http://localhost:8080

## FRONTEND VERIFICATION

- [ ] Opened another PowerShell/Command Prompt
- [ ] Navigated to `frontend` folder
- [ ] Ran `npm install` (if not done before)
- [ ] Ran `npm run dev`
- [ ] Frontend running on http://localhost:5173
- [ ] See message: "Local: http://localhost:5173"

## TESTING

- [ ] Open browser → http://localhost:5173
- [ ] See login page
- [ ] Login with:
  - Email: `admin`
  - Password: `admin123@`
- [ ] Click "Login as Administrator"
- [ ] Should see Admin Dashboard
- [ ] Try creating a tip or project
- [ ] Check network tab (DevTools) - should see API calls to localhost:8080

## TROUBLESHOOTING

If something doesn't work:

- [ ] Check backend terminal for error messages
- [ ] Check frontend terminal for error messages
- [ ] Verify database connection in `application.properties`
- [ ] Make sure both servers are still running (don't close terminals!)
- [ ] Hard refresh frontend: Ctrl+Shift+R
- [ ] Check if MySQL is running (for local) or Railway is accessible (for online)

## NEXT STEPS (When Everything Works)

- [ ] Explore the admin panel
- [ ] Register a test gardener account
- [ ] Try different features (tips, projects, discussions)
- [ ] Read BACKEND_SETUP.md for deployment guide
- [ ] Deploy to Railway (backend) and Vercel (frontend)

---

**Current Status:** ********\_\_\_\_********

**Notes:** ********\_\_\_\_********

**Finished on:** ********\_\_\_\_********
