# Business Requirements

Create a backend for simple News Management application. Pay attention that your application should be without view layer (UI).

The system should expose REST APIs to perform the following operations:

1. CRUD operations for News.
2. If new tags and(or) Author are passed during creation or modification of a News, then they should be created in the database. (Save news message with author and tags should go as one step in service layer.)
3. CRUD operations for Tag.
4. CRUD operations for Author.
5. Get news. All request parameters are optional and can be used in conjunction.
6. Sort news by date or tag or author;
7. Search according SearchCriteria (see details in Tools and Implementation Requirements section);
8. Add tag/tags for news message;
9. Add news author;
10. Each news should have only one author;
11. Each news may have more than 1 tag;
12. Count all news;

# Technical requirements

General Requirements:

1. Code should be clean and should not contain any “developer-purpose” constructions.
2. Application should be designed and written with respect to OOD and SOLID principles.
3. Code should contain valuable comments where appropriate.
4. Public APIs should be documented using Javadoc.
5. A clear layered structure should be used: responsibilities of each application layer should be defined.
6. JSON should be used as a format of client-server communication messages.
7. Convenient error and exception handling mechanism should be implemented: all errors should be meaningful and localized on backend side.
8. Abstraction should be used to avoid code duplication.
9. Database schema should be adjusted as described on image. Create 2 separate db scripts: one for db schema generation and one for loading default data (near 20 items for each table);

![alt text](https://kb.epam.com/download/attachments/934784537/image2020-1-26_15-58-37.png?version=1&modificationDate=1580043518219&api=v2)

# Tools and Implementation Requirements:

**Please note that only GA versions of tools, frameworks, and libraries are allowed.**

1. JDK version: 8. Use Streams, java.time.*, etc. where it is appropriate.
2. Application package root: com.epam.lab
3. Database connection pooling: HikariCP.
4. Spring JDBC Template should be used for data access.
5. Java Code Convention is mandatory. Exception: margin size – 120 chars.
6. Build tool: Apache Maven 3.5+. Multi-module project.
7. Web server: Apache Tomcat.
8. Application container: Spring IoC. Spring Framework +.
9. Spring should be configured via Java config (no spring.xml).
10. Database: PostgreSQL 9.+ or 10.+
11. Testing: JUnit 4.+ or 5.+, Mockito.
12. Service layer should be covered with unit tests. Unit tests coverage should be not less than 80%.
13. Repository layer should be tested using integration tests with an in-memory embedded database. All operations with news should be covered with integration tests.
14. APIs should be demonstrated using Postman tool. Postman collections with APIs should be prepared for the demo.
15. For search functionality use SeachCriteria object which may contain author entity and list of tags (will be using for constructing SQL queries in DAO layer);

Example multi module project package structure:

![alt text](https://kb.epam.com/download/thumbnails/934784537/2020-01-23_11h48_36.png?version=1&modificationDate=1580043646545&api=v2)
![alt text](https://kb.epam.com/download/thumbnails/934784537/2020-01-23_11h49_14.png?version=1&modificationDate=1580043646794&api=v2)
![alt text](https://kb.epam.com/download/thumbnails/934784537/2020-01-23_11h50_27.png?version=1&modificationDate=1580043647322&api=v2)
![alt text](https://kb.epam.com/download/thumbnails/934784537/2020-01-23_11h51_38.png?version=1&modificationDate=1580043647587&api=v2)

# Restrictions

It is forbidden to use:

* Spring Boot,
* Spring Data Repositories,
* JPA,
* Lombok,
* Powermock.


