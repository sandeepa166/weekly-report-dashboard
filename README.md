# Weekly Report Generator & Team Dashboard

## Project Overview

This is a full-stack web application developed for managing weekly work reports in a team environment.

Team members can create, view, and submit their own weekly reports. Managers can view submitted reports, check submission status, analyze workload by project, and monitor blockers through a dashboard.

## Technologies Used

### Frontend
- React
- Vite
- React Router DOM
- Axios
- Recharts

### Backend
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Validation
- Lombok

### Database
- MySQL

### Tools
- VS Code
- Thunder Client
- GitHub

## Main Features

### User Authentication
- User registration
- User login
- Password encryption using BCrypt
- Role-based navigation

### User Roles
- TEAM_MEMBER
- MANAGER

### Team Member Features
- Create weekly report
- View own report history
- Submit weekly report
- Track report status

### Manager Features
- View dashboard summary
- View all submitted reports
- View submission status
- View workload by project
- View recent reports
- Manage projects/categories

### Projects / Categories
- Add project
- View projects
- Edit project
- Delete project

## Project Structure

```text
weekly-report-dashboard
│
├── backend
│   ├── src
│   ├── pom.xml
│   └── mvnw.cmd
│
├── frontend
│   ├── src
│   ├── package.json
│   └── vite.config.js
│
└── README.md