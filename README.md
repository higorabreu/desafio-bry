# Users CRUD

This is a Java SpringBoot backend API integrated with a PostgreSQL database and an Angular frontend interface. The system allows for user registration, updating user information, listing users, and displaying user details.

## Technologies Used

- Java SpringBoot
- PostgreSQL
- Angular

## Features

- User registration with CPF, name and photo
- User delete
- User information update
- Displaying user details including anonymized CPF, name and photo

## Prerequisites

- Java Development Kit (JDK)
- Node.js and npm for Angular development
- PostgreSQL database installed

## Installation

1. Clone the repository to your local machine:
   ```bash
   git clone https://github.com/higorabreu/desafio-bry.git
   ```
2. Navigate to the project directory:
   ```bash
   cd desafio-bry
   ```
3. Ensure PostgreSQL is installed on your machine.
4. Access PostgreSQL as the superuser:
   ```bash
   sudo -u postgres psql
   ```
5. Inside the PostgreSQL shell, create a new user 'desafiobry' with the ability to create databases and an encrypted password:
   ```sql
   CREATE USER desafiobry WITH CREATEDB ENCRYPTED PASSWORD '1234';
   ```
6. Create the database "desafiobry":
   ```bash
   sudo -u postgres createdb desafiobry
   ```
7. Backend setup:
   - Open the backend folder:
     ```bash
     cd back
     ```
   - Install dependencies and start the SpringBoot server:
     ```bash
     ./mvnw spring-boot:run
     ```
8. Frontend setup:
   - Open another terminal window and navigate to the frontend folder:
     ```bash
     cd front
     ```
   - Install Angular CLI globally (if not already installed):
     ```bash
     npm install -g @angular/cli
     ```
   - Install dependencies:
     ```bash
     npm install
     ```
   - Start the Angular development server:
     ```bash
     ng serve
     ```

## Usage

1. Access the application at `http://localhost:4200` in your browser.
2. Use the provided frontend interface to interact with the system, including user registration, updating user information, listing users, and viewing user details.

## Postman Collection

You can use the provided Postman collection to test the backend API endpoints.

[![Run in Postman](https://run.pstmn.io/button.svg)]([https://www.getpostman.com/collections/your-postman-collection-link](https://www.postman.com/dark-robot-470395/workspace/higor-abreu/collection/28919232-9356cc51-1727-4446-81fd-7816e501e07e?action=share&creator=28919232))

Click the button above to import the collection into your Postman application.
