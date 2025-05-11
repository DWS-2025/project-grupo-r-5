[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Jd7ILUgB)

Changes implemented respect to the DataBase implementation:
1. Database Integration
- Added the necessary dependencies to implement a relational database using Spring Data JPA and Hibernate.
- Configured the database connection in the application properties file.

2. Entities
- Marked the Post, User, and GroupClass model classes as @Entity, enabling them to be mapped as database tables.
- Defined @Id for each entity to specify their primary key.
- Configured relationships between entities using @OneToMany, @ManyToOne, and @ManyToMany, ensuring proper data associations.
- Specified which entity maps to another within the relationships, defining ownership and mapped entities explicitly to maintain data integrity.
- - Fully integrated the entities with database operations.
- Ensured all CRUD operations and related logic are functional and persist data correctly using the configured JPA setup.

3. Repository, Service, and Controller Layer Enhancements 
- Implemented a structured repository-service-controller architecture for managing Post, User, and GroupClass entities efficiently.
- Repository Layer:
  - Created repositories for Post, User, and GroupClass by extending JpaRepository, enabling CRUD operations.
  - These repositories facilitate database interaction without requiring manual SQL queries.
  - Integrated custom query methods to retrieve data based on specific criteria, enhancing data retrieval capabilities. 
- Service Layer:
  - Developed dedicated service classes for each entity to encapsulate business logic.
  - Injected the respective repositories using @Autowired to ensure structured access to database operations.
  - Improved entity handling by properly managing object retrieval, persistence, and validation.
- Controller Layer:
  - Created RESTful API controllers to expose database functionalities.
  - Implemented endpoints using @RestController for managing CRUD operations across posts, users, and groups. 
  - Refactored the controllers to interact with the updated service methods, ensuring clean and maintainable code.
  - Enhanced error handling logic across all controllers to ensure that meaningful error responses are returned according to the exception or message passed, improving client-side debugging and UX.


Changes implemented respect to the API implementation:
- Integrated DTO (Data Transfer Object) pattern to decouple internal models from exposed API structures.
  - Created dedicated DTO classes for Post, User, and GroupClass to shape the data exchanged through the API.
  - Implemented mappers or transformation logic to convert between entities and DTOs in the service layer.
  - Updated service and controller methods to use DTOs instead of directly exposing or receiving entity objects, improving data encapsulation and API contract clarity.
- Created a dedicated API response structure to standardize the format of responses returned by the API.
- Developed dedicated REST controllers for User, Post, and GroupClass entities to handle CRUD operations through the API.
  - Each controller includes endpoints for creating, retrieving, updating, and deleting resources using standard HTTP methods.
  - These controllers ensure full API coverage for the application's core data models and are tightly integrated with the service and DTO layers for clean separation of concerns.

Others:
- Improved global exception handling to return consistent and descriptive error responses.
- Refined existing methods and removed unused code to improve maintainability and readability.


Changes implemented to add security:
- Created a Validation Service to handle user input validation, by cleaning and validating data before processing it with a Jsoup Safelist.
- 

