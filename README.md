# Library Management System

This is a JavaFX-based library management application built using JPA for database persistence. It allows users to manage books, borrow and return books, and manage their friend lists.

## Features

*   **User Authentication:** Secure login system to manage user sessions.
*   **Book Management:** Search for books by title or author, view book details.
*   **Borrowing & Returning:** Allows users to borrow and return books, tracking borrowed dates.
*   **Online Reading:** Allows users to view online content for borrowed books.
*   **Friend Management:** Users can send friend requests, accept or reject them, and manage their friend lists.
*  **See Friends' Borrowed Books** Allows users to see what their friends have borrowed.
*   **Database Persistence:** Data is stored using JPA, ensuring persistent data storage.

## Screenshots

Here are some screenshots of the application:

### Login Screen
![image](https://github.com/user-attachments/assets/eed98f61-354a-4be2-8e5c-c28d95ced5d2)


### Main Screen
![image](https://github.com/user-attachments/assets/5175a98a-380b-4970-a8ed-f3fc601ed1cf)


### Search Book
![image](https://github.com/user-attachments/assets/72d90015-9098-4814-a2c6-30ca06ac494d)


### Borrow Book
![image](https://github.com/user-attachments/assets/f0bfe53e-b028-4cb3-94d2-2453d2b55972)


### Return Book
![image](https://github.com/user-attachments/assets/39d9f5e2-331f-4638-a082-31c5f3b1a092)


### Display Borrowed and Previously Borrowed Books
![image](https://github.com/user-attachments/assets/df17735d-77a1-44a3-b021-ee4486915149)

### Read Online
![image](https://github.com/user-attachments/assets/80c8a884-2077-4a5c-a70a-99143ec71a55)


### Display Friend List
![image](https://github.com/user-attachments/assets/30e5731d-1d14-4fb6-b3e1-d9b8c6ada987)

### Display Friend Requests
![image](https://github.com/user-attachments/assets/51ac2445-ba17-456c-adb7-ad56551442ba)


### See Friends' Borrowed Books
![image](https://github.com/user-attachments/assets/325f7cd9-a25f-44fc-8b90-3aca91e830dc)


## How to Run

1.  **Prerequisites:**
    *   Java Development Kit (JDK) 11 or higher
    *   A Java IDE such as IntelliJ IDEA, Eclipse, or NetBeans.
    *   A database server (like MySQL) configured with a database named "librarydb" (or you can adjust the persistence settings to your database).

2.  **Set Up the Database:**
     * Make sure your database server (such as MySQL) is running.
     *   Create a database named `librarydb`.

3. **Import the Project**
    * Open the project in your IDE. If your IDE prompts you to import or build, accept the default prompts.
    * Make sure the necessary libraries are included (JavaFX, JPA). If the IDE does not automatically include them, please include it manually.
4.  **Configure Database:**
    * Go to `persistence.xml` file (usually under `src/main/resources/META-INF`).
    * Make sure your database username and password are correct.

5.  **Run the Application:**
    * Locate the `Lib.java` file (usually in the `src/main/java/lib/` package)
    *   Run the `main` method in `Lib.java` to start the application.

## Technologies Used

*   **Java:** Programming language.
*   **JavaFX:** UI framework.
*   **JPA (Java Persistence API):** For database interaction.
*   **MySQL:** Database management system. (You may also use another SQL database if you configure the persistence.xml correctly)

