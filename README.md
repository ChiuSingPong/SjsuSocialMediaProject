SJSU Social Media Project

This project is a simple social media web application built with Spring Boot and Java for CS 157A: Introduction to Database Management.

## To run the application.

1.  Go to project folder, where mvnw.cmd is located
   
2.  Enter into terminal and run:
    .\mvnw.cmd spring-boot:run

3.  Go to: http://localhost:8080

## Access the database console
1.  Make sure the application is running

2.  Go to: http://localhost:8080/h2-console

3.  For JDBC URL, enter: jdbc:h2:file:./data/sjsu-social
    For User Name, enter: sa
    For Password, leave empty

4.  Click "Connect"

5.  To view all tables, run:
    SELECT * FROM USERS;
    SELECT * FROM POSTS;
    SELECT * FROM COMMENTS;
    SELECT * FROM FOLLOWING;
    SELECT * FROM LIKED;
    SELECT * FROM PROFILES;


## To enter development. 
Required Software:
Before getting started, make sure the following software is installed:

1.  Java JDK 17 or higher
    Download: https://www.oracle.com/java/technologies/downloads/
    To verify installation, open Command Prompt or Terminal and run:
    java -version

2.  Eclipse IDE for Enterprise Java Developers
    Download: https://www.eclipse.org/downloads/packages/

    Select: "Eclipse IDE for Enterprise Java and Web Developers"

3.  Lombok Plugin for Eclipse
    Eclipse requires this plugin to correctly recognize Lombok annotations. The installation steps are listed below.


### Step 1: Install Lombok Plugin

1.  Download Lombok: https://projectlombok.org/download

2.  Open the downloaded lombok.jar
    If double-clicking does not work, run this instead:
    java -jar lombok.jar

3.  When the installer opens, click "Specify location"

4.  Find your Eclipse installation directory

5.  Select eclipse.exe

6.  Click "Install / Update"

7.  Close the installer

8.  Restart Eclipse

### Step 2: Import the Project

1.  Open Eclipse

2.  Go to File > Import

3.  Select Maven > Existing Maven Projects

4.  Click Next

5.  Browse to the project folder (SjsuSocialMediaProject)

6.  Make sure pom.xml is checked

7.  Click Finish

8.  Wait for Eclipse to download all Maven dependencies

9.  Right-click the project > Maven > Update Project > check "Force Update" > OK

### Step 3: Run the Application

1.  In the Package Explorer, expand the project

2.  Navigate to:
    src/main/java > s25.cs157a.sjsusocialmediaproject

3.  Right-click SjsuSocialMediaProjectApplication.java

4.  Select Run As > Java Application

5.  The server is running if the console prints something like the following:
    Started SjsuSocialMediaProjectApplication in X.XXX seconds

### Step 4: Open the Application

1.  Open a web browser

2.  Go to: http://localhost:8080

3.  The login page should appear

Note: Make sure to terminate session on the console when you rerun the Java application.
