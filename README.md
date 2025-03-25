[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Jd7ILUgB)

Changes implemented respect to the DataBase implementation:
1. Database Integration
- Added the necessary dependencies to implement a relational database using Spring Data JPA and Hibernate.
- Configured the database connection in the application properties file.

2. Entity Annotations
- Marked the Post, User, and GroupClass model classes as @Entity, enabling them to be mapped as database tables.
- Defined @Id for each entity to specify their primary key.
- Configured relationships between entities using @OneToMany, @ManyToOne, and @ManyToMany, ensuring proper data associations.
- Specified which entity maps to another within the relationships, defining ownership and mapped entities explicitly to maintain data integrity.

3. Repository Layer
- Created repositories for Post, User, and GroupClass by extending JpaRepository, enabling CRUD operations.
- These repositories allow easy interaction with the database without writing SQL queries manually.

4. Service Layer Enhancements
- Implemented service classes for Post, User, and GroupClass to handle business logic.
- Injected the respective repositories into service classes using @Autowired to facilitate data retrieval and manipulation.
- Ensured services provide structured access to database operations while maintaining separation of concerns.

5. Controller Layer Implementation
- Added a RESTful API controller to expose database functionalities to the frontend or external clients.
- Created endpoints using @RestController to manage CRUD operations for posts, users, and group classes.
- Used @Autowired to inject the corresponding services into the controller.


Changes implemented respect to the API implementation:
