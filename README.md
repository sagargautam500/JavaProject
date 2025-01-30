# JavaProjects Repository

This repository contains two Java-based projects developed using Eclipse IDE, both integrated with MySQL for data persistence.

## 1. Form Validation using Servlet

### Overview
A web application demonstrating form validation using **Java Servlets, JSP, and MySQL**. Features client-side and server-side validation with database storage.

### Features
- Client-side validation with HTML5
- Server-side validation using Servlets
- MySQL database integration for storing form data
- Error handling and success notifications

### Technologies
- Java Servlets
- JSP
- HTML/CSS
- MySQL
- Apache Tomcat

## 2. Hospital Management System using Swing

### Overview
A desktop application for managing hospital operations with **Java Swing and MySQL**, featuring CRUD operations for patients, doctors, and appointments.

### Features
- Patient/Doctor registration system
- Appointment scheduling
- MySQL database integration
- Interactive GUI with Swing components

### Technologies
- Java Swing
- MySQL
- Eclipse WindowBuilder

## Prerequisites
- Eclipse IDE
- JDK 8+
- Apache Tomcat 9+
- MySQL 8+
- MySQL Connector/J

## Setup Instructions

### For Both Projects:
1. Clone repository:  
   `git clone https://github.com/sagargautam500/javaProject.git`
2. Create MySQL database named:
   - `form_validation_db` for Servlet project
   - `hospital_db` for Swing project
3. Update database credentials in:
   - `src/db.properties` (Servlet project)
   - `src/DatabaseUtil.java` (Swing project)

### Form Validation Project:
1. Import as Dynamic Web Project in Eclipse
2. Add MySQL Connector/J to WEB-INF/lib
3. Configure Tomcat server
4. Run on server

### Hospital Management Project:
1. Import as Java Project in Eclipse
2. Add MySQL Connector/J to build path
3. Run `Main.java`

## Contribution
Contributions welcome! Please ensure:
1. Database credentials are not hardcoded
2. MySQL connection properly closed in all DAOs
3. Follow Java coding conventions

⚠️ **Note:** Database schema details available in respective DAO classes
