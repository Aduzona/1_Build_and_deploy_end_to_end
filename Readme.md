# Introduction

This project demonstrates the creation, deployment, and testing of a microservices-based application using several modern development and deployment tools. The application consists of a **Restaurant Listing** microservice, **User** Microservice, **Food Catalogue** Microservice, **Order** Microservice and an **Eureka server** for service discovery. The project uses Spring Boot for building the services, MySQL as the database, and various cloud-native and DevOps tools for deployment.

Also Included is Frontend Angular and Typescript

[Code links](https://github.com/orgs/udemy-dev-withK8s-AWS-codedecode/repositories)

View our Google Slides presentation: [click here to visit presentation](https://docs.google.com/presentation/d/10yM2esLSyXUrY0qOuR9-JfoXwm9UEMRbVYDnAC9-fVM/edit?usp=sharing)

## Building Backend Microservice Application

* Set Up Eureka Server First.
  * Use Spring Initializr
  * Dependencies
    * Eureka Server
* Then Create Restaurant Listing Microservices:
  * mapstruct used to map your entities to DTOs and DTOs to entities because in real world you will never play with entities.
  * Use spring initializr
  * Dependencies:
    * Spring Web
    * Lombak
    * Spring Data JPA
    * MySQL Driver
    * Eureka Discovery Client
    * We will add mapstruct manually.



Note: `Eureker` server has to start before starting this one `restaurantlisting` microservice

in  database-platform: `org.hibernate.dialect.MySQL5InnoDBDialect` `dialect` converts ORM based language to mySQL based Querry. 

* create restaurantdb in mysql workbench:   
```sql
CREATE SCHEMA `restaurantdb`;
```

* Run Eureka server in a seperate Intellij IDE.
* run Restaurantlisting in another Intellij IDE.

The `MySQL5InnoDBDialect` has been deprecated and removed in recent versions of Hibernate. You should use a more current dialect like `MySQLDialect` or `MySQL8Dialect`.

2 Tables where created in mySQL:
* `restaurant`
* `restaurant_seq`
Also, discovery client created: `DiscoveryClient_RESTAURANTLISTING/host.docker.internal:restaurantListing:9091: registering service...`

Go to the url, `localhost:8761` and see Eureka amd RESTAURANT-SERVICE which is registered.

in `localhost:8761`:


| Application|	AMIs|	Availability Zones|	Status|
|---|---|---|---|
|RESTAURANTLISTING|	n/a (1)|	(1)|	UP (1) - host.docker.internal:restaurantListing:9091|


Then we add more endpoints to the controller:

Now try to save a restaurant using `POST` in path: `http://localhost:9091/restaurant/addRestaurant`

```json
{
    "name": "Restaurant 1",
    "address": "Address line 1",
    "city": "Nürnberg",
    "restaurantDescription":"Restaurant Description"
}
```

I got `201 Created` and it returned:
```json
{
    "id": 1,
    "name": "Restaurant 1",
    "address": "Address line 1",
    "city": "Nürnberg",
    "restaurantDescription": "Restaurant Description"
}
```

* next test the GET request:  `http://localhost:9091/restaurant/fetchById/1`
* Also test GET request: `http://localhost:9091/restaurant/fetchAllRestaurants`

### Eureka Server

**Directory Structure:**
```
.
|-- HELP.md
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
|-- src
|   |-- main
|   |   |-- java
|   |   |   -- com
|   |   |       -- codedecode
|   |   |           -- eureka
|   |   |               -- EurekaApplication.java
|   |   -- resources
|   |       |-- application.properties
|   |       -- application.yml
|   -- test
|       -- java
|           -- com
|               -- codedecode
|                   -- eureka
|                       -- EurekaApplicationTests.java
-- target
    |-- classes
    |   |-- application.properties
    |   |-- application.yml
    |   -- com
    |       -- codedecode
    |           -- eureka
    |               -- EurekaApplication.class
    -- generated-sources
        -- annotations
```

**`@EnableEurekaServer` Annotation**

- **Purpose:** The `@EnableEurekaServer` annotation is used to make your Spring Boot application act as a Eureka server. Eureka is a service discovery server, which allows microservices to register themselves at runtime and discover other services for communication.
- **Functionality:** When you annotate your main class with `@EnableEurekaServer`, Spring Boot sets up the necessary configurations and starts a Eureka server when your application runs.

```java
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {
    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }
}
```

**`application.yml` in Eureka Server**

- **`server.port`:** Sets the port on which the Eureka server will run (e.g., 8761).
- **`eureka.client.fetch-registry` & `eureka.client.register-with-eureka`:** Both set to `false` because this application itself is the Eureka server, so it doesn’t need to fetch a registry or register with another Eureka server.

```yaml
server:
  port: 8761

eureka:
  client:
    fetch-registry: false
    register-with-eureka: false
```

**`pom.xml` in Eureka Server**

- **Dependencies:**
  - **`spring-cloud-starter-netflix-eureka-server`:** Includes all the necessary components to create a Eureka server.
  - **`spring-boot-starter-test`:** Used for writing and running tests.

- **Properties:** Specifies the Java version and Spring Cloud version for compatibility.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.3</version>
        <relativePath/> 
    </parent>
    <groupId>com.codedecode</groupId>
    <artifactId>eureka</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>Eureka</name>
    <description>Demo project for Spring Boot Eureka server</description>
    <properties>
        <java.version>22</java.version>
        <spring-cloud.version>2023.0.3</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### Restaurant Listing Microservice

**Directory Structure:**
```
.
|-- HELP.md
|-- mvnw
|-- mvnw.cmd
|-- pom.xml
|-- src
|   |-- main
|   |   |-- java
|   |   |   -- com
|   |   |       -- codedecode
|   |   |           -- restaurantlisting
|   |   |               |-- RestaurantListingApplication.java
|   |   |               |-- controller
|   |   |               |   -- RestaurantController.java
|   |   |               |-- dto
|   |   |               |   -- RestaurantDTO.java
|   |   |               |-- entity
|   |   |               |   -- Restaurant.java
|   |   |               |-- mapper
|   |   |               |   -- RestaurantMapper.java
|   |   |               |-- repo
|   |   |               |   -- RestaurantRepo.java
|   |   |               -- service
|   |   |                   -- RestaurantService.java
|   |   -- resources
|   |       |-- application.properties
|   |       |-- application.yml
|   |       |-- static
|   |       -- templates
|   -- test
|       -- java
|           -- com
|               -- codedecode
|                   -- restaurantlisting
|                       -- RestaurantListingApplicationTests.java
-- target
    |-- classes
    |   |-- application.properties
    |   |-- application.yml
    |   -- com
    |       -- codedecode
    |           -- restaurantlisting
    |               |-- RestaurantListingApplication.class
    |               |-- controller
    |               |   -- RestaurantController.class
    |               |-- dto
    |               |   -- RestaurantDTO.class
    |               |-- entity
    |               |   -- Restaurant.class
    |               |-- mapper
    |               |   |-- RestaurantMapper.class
    |               |   -- RestaurantMapperImpl.class
    |               |-- repo
    |               |   -- RestaurantRepo.class
    |               -- service
    |                   -- RestaurantService.class
    -- generated-sources
        -- annotations
            -- com
                -- codedecode
                    -- restaurantlisting
                        -- mapper
                            -- RestaurantMapperImpl.java
```


**`RestaurantMapper.java`**

`RestaurantMapper.java` is an interface that defines methods for mapping between different object types, specifically between `RestaurantDTO` and `Restaurant` objects.

- **Purpose:** The primary purpose of this mapper is to convert data between the `Restaurant` entity, which represents the data model tied to the database, and `RestaurantDTO`, which is used for transferring data in a controlled manner between layers, typically from your service layer to the controller and vice versa.

- **Key Components:**
  - **MapStruct Annotation (`@Mapper`):** This tells the MapStruct library that this interface will be used to generate the implementation of the mapping methods at compile time.
  - **Instance (`RestaurantMapper INSTANCE`):** This is a singleton instance of the mapper, which can be used throughout your application.
  - **Mapping Methods:** These methods, `mapRestaurantDTOToRestaurant` and `mapRestaurantToRestaurantDTO`, define the conversion logic between `RestaurantDTO` and `Restaurant`.

```java
@Mapper
public interface RestaurantMapper {
    RestaurantMapper INSTANCE = Mappers.getMapper(RestaurantMapper.class);

    Restaurant mapRestaurantDTOToRestaurant(RestaurantDTO restaurantDTO);
    RestaurantDTO mapRestaurantToRestaurantDTO(Restaurant restaurant);
}
```

**`RestaurantDTO.java`**

`RestaurantDTO.java` is a Data Transfer Object (DTO) that represents the data structure sent between client and server.

- **Purpose:** DTOs are used to encapsulate data and send it over the network. In this case, `RestaurantDTO` contains fields such as `id`, `name`, `address`, `city`, and `restaurantDescription`, representing the essential data of a restaurant that can be exposed to the client.

- **Annotations:**
  - **`@Data`:** This is a Lombok annotation that automatically generates boilerplate code like getters, setters, `toString()`, `equals()`, and `hashCode()` methods.
  - **`@AllArgsConstructor`:** Generates a constructor with all the fields in the class.
  - **`@NoArgsConstructor`:** Generates a no-argument constructor.

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantDTO {
    private int id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;
}
```

**`RestaurantService.java`**

`RestaurantService.java` is a service class that contains the business logic for handling restaurant data.

- **Purpose:** This service handles the core business operations related to restaurants, such as finding all restaurants, adding a new restaurant to the database, and fetching a restaurant by its ID. It interacts with the `RestaurantRepo` to perform these operations on the database.

- **Key Components:**
  - **`@Service`:** Marks the class as a service, a component in the Spring framework, which is used to hold business logic.
  - **Dependency Injection (`@Autowired`):** Injects the `RestaurantRepo` dependency, which is an interface extending `JpaRepository`, to interact with the database.
  - **Methods:**
    - `findAllRestaurants()`: Retrieves all restaurants from the database and maps them to `RestaurantDTO`.
    - `addRestaurantInDB()`: Saves a new restaurant to the database by mapping a `RestaurantDTO` to a `Restaurant` entity.
    - `fetchRestaurantById()`: Fetches a restaurant by its ID and returns it as a `RestaurantDTO`.

```java
@Service
public class RestaurantService {

    @Autowired
    RestaurantRepo restaurantRepo;

    public List<RestaurantDTO> findAllRestaurants(){
        List<Restaurant> restaurants = restaurantRepo.findAll();
        List<RestaurantDTO> restaurantDTOList = restaurants.stream()
                .map(RestaurantMapper.INSTANCE::mapRestaurantToRestaurantDTO)
                .collect(Collectors.toList());
        return restaurantDTOList;
    }

    public RestaurantDTO addRestaurantInDB(RestaurantDTO restaurantDTO) {
        Restaurant savedRestaurant = restaurantRepo.save(RestaurantMapper.INSTANCE.mapRestaurantDTOToRestaurant(restaurantDTO));
        return RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(savedRestaurant);
    }

    public ResponseEntity<RestaurantDTO> fetchRestaurantById(Integer id){
        Optional<Restaurant> restaurant = restaurantRepo.findById(id);
        return restaurant.map(value -> 
                new ResponseEntity<>(RestaurantMapper.INSTANCE.mapRestaurantToRestaurantDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.NOT_FOUND));
    }
}
```

**`Restaurant.java`**

`Restaurant.java` is an entity class that maps to a database table.

- **Purpose:** This class represents the `Restaurant` entity, which is directly tied to the database table `restaurant`. Each instance of this class corresponds to a row in the table.

- **Annotations:**
  - **`@Entity`:** Marks the class as a JPA entity.
  - **`@Id`:** Denotes the primary key field.
  - **`@GeneratedValue(strategy = GenerationType.AUTO)`:** Specifies that the ID should be generated automatically.

- **Fields:** These correspond to the columns in the `restaurant` table: `id`, `name`, `address`, `city`, and `restaurantDescription`.

```java
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;
}
```

**`RestaurantController.java`**

`RestaurantController.java` is a REST controller that handles HTTP requests.

- **Purpose:** This controller exposes endpoints to manage restaurant data, such as creating a new restaurant, fetching all restaurants, and fetching a restaurant by its ID. It serves as the entry point for client interactions with the application.

- **Annotations:**
  - **`@RestController`:** Marks the class as a controller where every method returns a domain object instead of a view. It's a combination of `@Controller` and `@ResponseBody`.
  - **`@RequestMapping("/restaurant")`:** Maps HTTP requests to the controller’s methods.
  - **`@CrossOrigin`:** Enables cross-origin resource sharing (CORS) support.

- **Key Methods:**
  - `fetchAllRestaurants()`: Handles GET requests to retrieve all restaurants.
  - `saveRestaurant()`: Handles POST requests to add a new restaurant.
  - `findRestaurantById()`: Handles GET requests to retrieve a restaurant by ID.

```java
@RestController
@RequestMapping("/restaurant")
@CrossOrigin
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping("/fetchAllRestaurants")
    public ResponseEntity<List<RestaurantDTO>> fetchAllRestaurants(){
        List<RestaurantDTO> allRestaurants = restaurantService.findAllRestaurants();
        return new ResponseEntity<>(allRestaurants, HttpStatus.OK);
    }

    @PostMapping("/addRestaurant")
    public ResponseEntity<RestaurantDTO> saveRestaurant(@RequestBody RestaurantDTO restaurantDTO) {
        RestaurantDTO restaurantAdded = restaurantService.addRestaurantInDB(restaurantDTO);
        return new ResponseEntity<>(restaurantAdded, HttpStatus.CREATED);
    }

    @GetMapping("fetchById/{id}")
    public ResponseEntity<RestaurantDTO> findRestaurantById(@PathVariable Integer id) {
        return restaurantService.fetchRestaurantById(id);
    }
}
```

**Repository Interface (`RestaurantRepo.java`):**
```java
package com.codedecode.restaurantlisting.repo;

import com.codedecode.restaurantlisting.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepo extends JpaRepository<Restaurant, Integer> {
}
```

**Application Class (`RestaurantListingApplication.java`):**
```java
package com.codedecode.restaurantlisting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantListingApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestaurantListingApplication.class, args);
    }
}
```

**MapStruct**

MapStruct is a Java annotation processor used for generating type-safe, performant, and easy-to-use mappers for converting between Java objects (like DTOs and entities). It automatically generates the implementation of mapper interfaces at compile-time, avoiding the need for repetitive and error-prone manual mapping code.

**Advantages:**
- **Type Safety:** Ensures the mappings are type-safe and checked at compile time.
- **Performance:** Since the mapping code is generated at compile time, it’s faster than using reflection-based mappers.
- **Less Boilerplate Code:** Reduces the amount of code needed to convert between different types.


**`application.yml` in Restaurant Listing Microservice**

The `application.yml` file is the main configuration file for a Spring Boot application. It allows you to define various properties in a hierarchical, structured format.

**Key Sections:**
- **`server.port`:** Specifies the port on which the microservice will run (e.g., 9091).
- **`eureka.client.service-url.defaultZone`:** Configures the URL for the Eureka server, which the microservice will use for registration and discovery.
- **`spring.datasource`:** Defines the database connection settings like URL, username, password, and driver class.
- **`spring.jpa.hibernate.ddl-auto`:** Specifies the behavior of the database schema creation (e.g., `update` to update the schema automatically).
- **`spring.jpa.show-sql`:** Enables the logging of SQL statements generated by Hibernate.
- **`spring.jpa.database-platform`:** Defines the Hibernate dialect to use for the MySQL database (`MySQL8Dialect`).

```yaml
server:
  port: 9091

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  application:
    name: RESTAURANT-SERVICE
  datasource:
    url: jdbc:mysql://localhost:3306/restaurantdb
    username: root
    password: Diego#91spring
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform:

 org.hibernate.dialect.MySQL8Dialect
```


**`pom.xml` in Restaurant Listing Microservice**

The `pom.xml` (Project Object Model) is the core configuration file for a Maven project. It defines dependencies, plugins, and project-specific details like the Java version.

**Key Sections:**
- **`parent`:** Inherits common configurations from `spring-boot-starter-parent`.
- **`dependencies`:** Includes all necessary dependencies like `spring-boot-starter-data-jpa`, `spring-boot-starter-web`, `spring-cloud-starter-netflix-eureka-client`, and database connector `mysql-connector-j`.
- **`properties`:** Defines project properties like `java.version` and `spring-cloud.version`.
- **`build.plugins`:** Configures the Maven build process, including the Spring Boot Maven plugin for packaging the application.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.3.3</version>
        <relativePath/> 
    </parent>
    <groupId>com.codedecode</groupId>
    <artifactId>restaurantlisting</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>restaurantListing</name>
    <description>Demo project for Spring Boot Restaurant Listing App</description>
    <properties>
        <java.version>22</java.version>
        <spring-cloud.version>2023.0.3</spring-cloud.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>1.5.5.Final</version>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```


**Database Setup**

Create the MySQL database schema using the following SQL command:

```sql
CREATE SCHEMA `restaurantdb`;
```

This will create the required schema for your application.

**Service Registration and Eureka Dashboard**

- Start the Eureka server and the RestaurantListing microservice.
- Navigate to `http://localhost:8761` to view the Eureka dashboard.
- You should see the `RESTAURANTLISTING` service registered and UP.




### Testing Endpoints

1. **Add a Restaurant (POST Request):**

   URL: `http://localhost:9091/restaurant/addRestaurant`

   **Request Body:**
   ```json
   {
       "name": "Restaurant 1",
       "address": "Address line 1",
       "city": "Nürnberg",
       "restaurantDescription": "Restaurant Description"
   }
   ```

   **Expected Response:**
   ```json
   {
       "id": 1,
       "name": "Restaurant 1",
       "address": "Address line 1",
       "city": "Nürnberg",
       "restaurantDescription": "Restaurant Description"
   }
   ```

2. **Fetch Restaurant by ID (GET Request):**

   URL: `http://localhost:9091/restaurant/fetchById/1`

   **Expected Response:**
   ```json
   {
       "id": 1,
       "name": "Restaurant 1",
       "address": "Address line 1",
       "city": "Nürnberg",
       "restaurantDescription": "Restaurant Description"
   }
   ```

3. **Fetch All Restaurants (GET Request):**

   URL: `http://localhost:9091/restaurant/fetchAllRestaurants`

   **Expected Response:**
   ```json
   [
       {
           "id": 1,
           "name": "Restaurant 1",
           "address": "Address line 1",
           "city": "Nürnberg",
           "restaurantDescription": "Restaurant Description"
       }
   ]
   ```


### Summary

- **`RestaurantMapper.java`:** Converts between `RestaurantDTO` and `Restaurant` using MapStruct.
- **`RestaurantDTO.java`:** Data Transfer Object for transferring restaurant data between client and server.
- **`RestaurantService.java`:** Contains business logic for handling restaurants, including database interactions.
- **`Restaurant.java`:** Entity class representing the `restaurant` table in the database.
- **`RestaurantController.java`:** REST controller that exposes endpoints to manage restaurant data.
- **MapStruct:** A tool for generating mapping code between objects at compile time.
- **`application.yml`:** Configuration file for setting application properties in a structured manner.
- **`pom.xml`:** Maven configuration file that defines project dependencies, build configuration, and plugins.
- **`@EnableEurekaServer`:** Annotation that turns a Spring Boot application into a Eureka server for service discovery.