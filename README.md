# 🏋️ CoachApp

CoachApp is a web application built with **Spring Boot**, **Thymeleaf**, and **Bootstrap** that helps personal trainers manage clients, training plans, workouts, and progress tracking.

---

## 🚀 Features

- ✅ User registration & login
- 🧑 Client management (add, edit, view, filter)
- 📅 Training plan & workout scheduling
- 📊 Statistics for:
    - Total and completed workouts
    - Weight tracking (max, min, average, difference)
    - Training success rate
- 📈 Chart visualizations using Chart.js
- 🔍 Client filtering (gender, goal, training plan)

## 🛠️ Technologies Used

- Java 21
- Spring Boot 3
- Spring Security
- Thymeleaf
- Bootstrap 5
- MariaDB (or MySQL)
- JPA / Hibernate
- Chart.js

---

### 2. Configure your database

Update `src/main/resources/application.properties` with your local database configuration:

```
spring.datasource.url=jdbc:mariadb://localhost:3306/CoachProgramDatabase
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## 3. Run the application


Open the project in your IDE and run `CoachApplication.java`

---

## 👤 Default Test User

When the app starts for the first time, it automatically loads a default user:

- **Email:** `coach@coachapp.com`
- **Password:** `testcoach`

This is handled by `TestDataLoader.java`. You can modify or remove this file.

---

## 📌 TODO

- Add email reminders for workouts
- Add admin role and role-based access
- Add calendar view 
- Add create program with actual workouts and exercises
- Add file upload for client profile pictures
- Improve responsivity for small mobile devices

---
