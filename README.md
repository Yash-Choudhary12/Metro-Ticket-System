# Metro Ticket Management Backend

This project provides a backend solution for a Metro Ticket Management System, implemented using Servlets, Hibernate (v5.6.15), and SQL (v8.0.36). It supports ticket booking, user authentication, station management, and ticket validation.

---

## Features
- **User Management:**
  - User registration and login.

- **Station Management:**
  - View all available stations.

- **Ticket Management:**
  - Book and validate tickets.
  - Calculate fare based on source and destination.

- **Reports:**
  - Generate daily, weekly, and monthly reports.

---

## Tech Stack
- **Java Version:** Java 21
- **Frameworks:** Servlets, Hibernate
- **Database:** MySQL 8.0.36
- **Server:** Apache Tomcat (v8 or later)
- **Dependencies:**
  - Razorpay SDK (v1.4.6)
  - Hibernate Core

---

## Installation

### Prerequisites
- Java 21 installed on your system.
- MySQL database server (v8.0.36) configured.
- Apache Tomcat (v9 or later).
- Maven build tool installed.

### Setup Steps

1. Clone the repository:
   git clone https://github.com/yourusername/metro-ticket-backend.git
   cd metro-ticket-backend

2. Configure the database:
   - Create a new database in MySQL:
     CREATE DATABASE metro_ticket_db;
   
   - Update `hibernate.cfg.xml` with your database credentials.

3. Add required dependencies:
   - Ensure the `pom.xml` includes:
  
     <dependency>
       <groupId>org.hibernate</groupId>
       <artifactId>hibernate-core</artifactId>
       <version>5.6.15.Final</version>
     </dependency>
     <dependency>
       <groupId>com.razorpay</groupId>
       <artifactId>razorpay-java</artifactId>
       <version>1.4.6</version>
     </dependency>
    

4. Build the project:
   mvn clean install

5. Deploy to Tomcat:
   - Copy the generated `.war` file from `target/` to the Tomcat `webapps` folder.

6. Start the Tomcat server and access the application at:
   http://localhost:8080/metro-ticket-backend



## Contribution Guidelines

1. Fork the repository.
2. Create a feature branch:
   git checkout -b feature-name
  
3. Commit your changes:
   git commit -m "Add feature description"
   
4. Push to the branch:
   git push origin feature-name
   
6. Create a Pull Request
   

## Known Issues
- Razorpay dependency issues on some systems. Ensure the SDK is correctly added to the classpath.

---

## License
This project is licensed under the MIT License. See the LICENSE file for details.

---

## Contact
For any inquiries or support, please contact:
- **Name:** Yash Choudhary

