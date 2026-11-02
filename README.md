# Met Library System
MetLibrary – Java Desktop Library Management System

1. Project Overview: MetLibrary is a standalone Java desktop application developed to manage the core
operations of a small university library. The system supports two user roles:
Librarian and Student Member, each with different access privileges.

The application demonstrates object-oriented design principles, GUI development
using JavaFX, data persistence, and basic concurrency concepts, in accordance
with the module requirements.

2. System Requirements: 
To compile and run this application, the following are required:
• Java Development Kit (JDK): Version 17
• JavaFX SDK: Version 17.0.17
• Operating System: Windows 10 or later
• IDE used for development: IntelliJ IDEA (Community Edition)

3. Project Structure
The project is organised using standard Java package conventions:
• metLibrary.model       → Domain model classes (User, Book, Loan, etc.)
• metLibrary.service     → Business logic (UserService, CatalogService, LoanService)
• metLibrary.persistence → Data persistence (DataStorage)
• metLibrary.gui         → JavaFX GUI views
• out/artifacts          → Compiled executable JAR

4. Compiling the Source Code (from IntelliJ IDEA)

1. Open IntelliJ IDEA
2. Select "Open Project"
3. Open the folder containing the MetLibrary project
4. Ensure the Project SDK is set to JDK 17
5. Ensure JavaFX 17.0.17 is added as a library
6. Build the project using:
   Build → Build Artifacts → MetLibrary.jar → Build

5. Running the Application (Executable JAR)
1. Open Command Prompt.
2. Navigate to the directory containing the MetLibrary.jar file:

   cd path\to\MetLibrary_jar

3. Run the application using JavaFX:

   java --module-path "path\to\javafx-sdk-17.0.17\lib" --add-modules javafx.controls,javafx.fxml -jar MetLibrary.jar

Note:
- Replace path\to\MetLibrary_jar with the directory where the MetLibrary.jar file is located.
- Replace path\to\javafx-sdk-17.0.17\lib with the directory where JavaFX 17.0.17 is installed on your system.

6. Login Details

A default librarian account is provided:
• User ID: admin
• Password: admin
A default student account is provided for immediate login:
• User ID: S001
• Password: 123
Student accounts can be created by the librarian using the User Management
interface.	

7. Core Features:
• User Management (Librarian only)
  - Add, update, and delete student accounts
• Catalog Management (Librarian only)
  - Add and remove Books and Journals
  - Demonstrates inheritance and polymorphism
• Loan Management (Librarian only)
  - Issue and return library items
  - Automatic due date calculation
  - Overdue loan detection
• Student Access
  - Search the catalog (read-only)
  - View personal borrowing history
• Data Persistence
  - Users, items, and loans are saved using serialized files
  - Data is restored automatically when the application starts
  
8. Notes
• All data files (users.dat, items.dat, loans.dat) must be located in the same
  directory as the executable JAR for persistence to function correctly.
• The application is entirely GUI-based; no console interaction is required.

End of README
