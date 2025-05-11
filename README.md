## Harbor Tariff Transaction System

A Java-based desktop application for managing harbor ship traffic, company records, and dynamic tariff calculations. Built with Swing for the GUI and MySQL for the backend database.

## Features

- User Authentication: Login system with credential validation
- Ship Management: Add, edit, delete, and search ship records
- Company Records: Manage companies associated with each ship
- Dynamic Tariff Calculation: Calculates total tariff charges based on duration of stay and applies exponential daily increases (tariff doubles each day)
- Date Handling: Calculates days between arrival and departure
- Live Table Updates: Reflects database changes immediately in the UI
- Search Functionality: Filter ship records by ship name
- Data Validation: Ensures correct entries and provides informative error messages

## Technologies Used

- Java: For GUI
- JDBC: For database operations
- MySQL: For the relational database

## Database Schema

- `users`: Handles login credentials
- `companies`: Stores company details
- `ships`: Records ship information
- `category`: Defines ship categories
- `tariff`: Stores tariff rates per category
- `transactions`: Records ship transactions and tariff calculations

## Setup Instructions

1. Clone the repository:
> git clone https://github.com/""/Harbor-Tariff-Transaction-System.git
> cd Harbor-Tariff-Transaction-System

2. Import into any IDE, ensure your JDBC driver is configured (MySQL Connector/J)

3. Create a MySQL database named `htts` and import the provided SQL dump

4. Update DB Credentials
Modify the DB connection URL, username, and password in the Java files:
> Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/htts", "root", "");

5. Run the application
