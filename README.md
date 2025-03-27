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

3. Repository, Service, and Controller Layer Enhancements 
- Implemented a structured repository-service-controller architecture for managing Post, User, and GroupClass entities efficiently.
- Repository Layer:
  - Created repositories for Post, User, and GroupClass by extending JpaRepository, enabling CRUD operations.
  - These repositories facilitate database interaction without requiring manual SQL queries.
- Service Layer:
  - Developed dedicated service classes for each entity to encapsulate business logic.
  - Injected the respective repositories using @Autowired to ensure structured access to database operations.
  - Improved entity handling by properly managing object retrieval, persistence, and validation.
- Controller Layer:
  - Created RESTful API controllers to expose database functionalities.
  - Implemented endpoints using @RestController for managing CRUD operations across posts, users, and groups.
  - Refactored the controllers to interact with the updated service methods, ensuring clean and maintainable code. 
 

Changes implemented respect to the API implementation:
