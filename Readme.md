## Introduction

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
    username: ""
    password: ""
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



#### Summary

- **`RestaurantMapper.java`:** Converts between `RestaurantDTO` and `Restaurant` using MapStruct.
- **`RestaurantDTO.java`:** Data Transfer Object for transferring restaurant data between client and server.
- **`RestaurantService.java`:** Contains business logic for handling restaurants, including database interactions.
- **`Restaurant.java`:** Entity class representing the `restaurant` table in the database.
- **`RestaurantController.java`:** REST controller that exposes endpoints to manage restaurant data.
- **MapStruct:** A tool for generating mapping code between objects at compile time.
- **`application.yml`:** Configuration file for setting application properties in a structured manner.
- **`pom.xml`:** Maven configuration file that defines project dependencies, build configuration, and plugins.
- **`@EnableEurekaServer`:** Annotation that turns a Spring Boot application into a Eureka server for service discovery.



#### Testing Endpoints

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


### User Details Microservice


This Microservice handles user information like its id, name, password,  address where to deliver the food etc. 


Tech stack used 

* Microservice architecture
* Rest APIs
* Java 22
* MySql Relational DB as Datasource
* Spring Boot 
* Lombok
* Eureka Client
* mapstruct

Generate from start.spring.io:
* Project: Mavin
* Language: Java
* Spring Boot: 3.3.3
* Project Metadata
  * Group: com.codedecode
  * Artifact: userinfo
  * Name: UserInformation
  * Description: Demo project for Spring Boot User Details MS
  * Package name: com.codedecode.userinfo
  * Packaging: Jar
  * Java: 22
* Dependencies:
  * Lombok
  * Spring Web
  * Spring Data JPA
  * MySQL Driver
  * Eureka Discovery Client
  * Later add MapStruct
* Generate

in pom.xml, add MapStruct
```xml
...
        <dependency>
			<groupId>org.mapstruct</groupId>
			<artifactId>mapstruct</artifactId>
			<version>1.5.5.Final</version>
		</dependency>
```

in build, add plugin for mapstruct

```xml
...
        <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
							<version>1.18.34</version>
						</path>
						<path>
							<groupId>org.mapstruct</groupId>
							<artifactId>mapstruct-processor</artifactId>
							<version>1.5.5.Final</version>
						</path>

					</annotationProcessorPaths>
				</configuration>
			</plugin>
```


Packages:

* controller
* dto
* entity
* repo
* service
* mapper

For Application.yml: 

* server port: 9093 . Please note different ports for different microservices.
* Configure Eureka client
* Application name configuration.
* Link to user database

Make sure you `CREATE SCHEMA userdb;`

```yml
server:
  port: 9093

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  profiles:
    active: dev
  application:
    name: USER-SERVICE
  datasource:
    url: jdbc:mysql://localhost:3306/userdb
    password: ""
    username: ""
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

Run Eureka server in a seperate IDE, then run Then run the application.

Test in Postman was successful.
Run `http://localhost:8761/`and see if Microservices are registered successfully. This was registered successfully.

Data saved
![Userdb](images/6_userdb_MySQL.png)

Please note, there was no Hashing and Salting done to this password stored in the database. And passwords used was random thus a placehoder before Hashing and salting is applied.


### Food Catalogue Listing

On Front end page where all restaurants are listed , when we select 1 restaurant, Ll its food menu is shown. Also restaurant details , address etc too is shown. 

* Food Catalogue Microservice is responsible to-
    * List **all food items list of that particular Restaurant** and 
    * **Complete Restaurant Details** too on the front end Page / UI


Tech stack used 

* Microservice architecture
* Rest APIs
* Java 22
* MySql Relational DB as Datasource
* Spring Boot 
* Lombok
* Eureka Client
* mapstruct

Generate from start.spring.io:
* Project: Mavin
* Language: Java
* Spring Boot: 3.3.3
* Project Metadata
  * Group: com.codedecode
  * Artifact: userinfo
  * Name: UserInformation
  * Description: Demo project for Spring Boot User Details MS
  * Package name: com.codedecode.userinfo
  * Packaging: Jar
  * Java: 22
* Dependencies:
  * Lombok
  * Spring Web
  * Spring Data JPA
  * MySQL Driver
  * Eureka Discovery Client
  * Later add MapStruct
* Generate


Packages:

* controller
* dto
* entity
* repo
* service
* mapper


in `pom.xml`, add Mapstruct and plugind
s
```xml
    <dependency>
			<groupId>org.mapstruct</groupId>
			<artifactId>mapstruct</artifactId>
			<version>1.5.5.Final</version>
		</dependency>

    ...

    <plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-compiler-plugin</artifactId>
				<version>3.8.1</version>
				<configuration>
					<annotationProcessorPaths>
						<path>
							<groupId>org.projectlombok</groupId>
							<artifactId>lombok</artifactId>
							<version>1.18.34</version>
						</path>
						<path>
							<groupId>org.mapstruct</groupId>
							<artifactId>mapstruct-processor</artifactId>
							<version>1.5.5.Final</version>
						</path>

					</annotationProcessorPaths>
				</configuration>
			</plugin>
```

in `FoodItem.java`. Quantity of the food item is initialized zero, as when you enter a supermarket, you have not selected a quantity.

```java
package com.codedecode.foodcatalogue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String itemName;

    private String itemDescription;

    private  boolean isVeg;

    private Number price;

    private  Integer restaurantId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer quantity;
}

```

In DTO package, create `FoodItemDTO.java` which is our Data Transfer object for the entity FoodItem.java.

`FoodItemDTO.java`
```java
package com.codedecode.foodcatalogue.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    private int id;

    private String itemName;

    private String itemDescription;

    private  boolean isVeg;

    private Number price;

    private  Integer restaurantId;
    private Integer quantity;
}

```


Also create a restaurant DTO in `Foodcatalogue Microservice` which will be used to fetch response from `Restaurant` entity's `restaurantlisting microservice`

So this is a data transfer from restaurant microservice to food catalogue microservice.
`Restaurant.java`
```java
package com.codedecode.foodcatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    
    private int id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;
}

```




next `FoodCataloguePage.java`
Responsible for showing all the food Item list and restaurant details on the frontend.

```java
package com.codedecode.foodcatalogue.dto;

import com.codedecode.foodcatalogue.entity.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class FoodCataloguePage {

    private List<FoodItem> foodItemsList;

    private Restaurant restaurant;
}

```

Mapper

Mapping between FoodItem.java and FoodItemDTO.java
`FoodItemMapper.java`
```java
package com.codedecode.foodcatalogue.mapper;

import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.entity.FoodItem;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FoodItemMapper {

    FoodItemMapper INSTANCE = Mappers.getMapper(FoodItemMapper.class);

    FoodItem mapFoodItemDTOToFoodItem(FoodItemDTO foodItemDTO);

    FoodItemDTO mapFoodItemToFoodItemDto(FoodItem foodItem);
}

```

To fetch a retaurant details from another microservice, we need a rest template.
so got to `FoodCatalogueMicroserviceApplication`
create `@Bean`. Assuming we have 3 instances making  request, which of these three should this rest template go and hit and get our data, so it should be `@LoadBalanced`, because there can be multiple instances of the `restaurantlisting` microservice.

Now `Eureka` will find the instance that is not so busy and is available to quickly respond your request. 

make sure you `Autowired` `RestTemplate` in `FoodCatalogueService.java`




`FoodCatalogueMicroserviceApplication`
```java
package com.codedecode.foodcatalogue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class FoodCatalogueMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FoodCatalogueMicroserviceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return new RestTemplate();
	}
}

```
![Eureka server](images/1.%20Eureka-displaying-services.png)


The `restaurantlisting` runs at port `9091` at `fetchById/{id}` path apply in `fetchRestaurantDetailsFromRestaurantMS`, 
But we are going to use the `restaurantlisting` Microservice name in `application.yml` which is `RESTAURANTLISTING`. So create url in a load balanced way.

also in `fetchRestaurantDetailsFromRestaurantMS(Integer restaurantId)` we use getForObject,to get info for url: `"http://RESTAURANTLISTING/restaurant/fetchById/"+restaurantId` and map it to the retaurant DTO in our application. 
`FoodCatalogueService.java`
```java
package com.codedecode.foodcatalogue.service;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.dto.Restaurant;
import com.codedecode.foodcatalogue.entity.FoodItem;
import com.codedecode.foodcatalogue.mapper.FoodItemMapper;
import com.codedecode.foodcatalogue.repo.FoodItemRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class FoodCatalogueService {

    @Autowired
    FoodItemRepo foodItemRepo;

    @Autowired
    RestTemplate restTemplate;//LoadBalanced restTemplate

    public FoodItemDTO addFoodItem(FoodItemDTO foodItemDTO) {
        FoodItem foodItemSavedInDB= foodItemRepo.save(FoodItemMapper.INSTANCE.mapFoodItemDTOToFoodItem(foodItemDTO));
        return  FoodItemMapper.INSTANCE.mapFoodItemToFoodItemDto(foodItemSavedInDB);
    }

    public FoodCataloguePage fetchFoodCataloguePageDetails(Integer restaurantId) {

        // We need 2 things
        // food item list
        List<FoodItem> foodItemList = fetchFoodItemList(restaurantId);

        //restaurantdetails
        Restaurant restaurant = fetchRestaurantDetailsFromRestaurantMS(restaurantId);

        return createFoodCataloguePage(foodItemList,restaurant);


    }

    /**
     * Merge 2 responses from fetchRestaurant...() and fetchFoodItemList(..) as
     * one food catalog page
     * @param foodItemList
     * @param restaurant
     */
    private FoodCataloguePage createFoodCataloguePage(List<FoodItem> foodItemList, Restaurant restaurant) {
        FoodCataloguePage foodCataloguePage =new FoodCataloguePage();
        foodCataloguePage.setFoodItemsList(foodItemList);
        foodCataloguePage.setRestaurant(restaurant);
        return foodCataloguePage;
    }

    /**
     * Goes to the restaurantlisting microservice and get details
     * @param restaurantId
     * @return
     */
    private Restaurant fetchRestaurantDetailsFromRestaurantMS(Integer restaurantId) {
        return restTemplate.getForObject("http://RESTAURANTLISTING/restaurant/fetchById/"+restaurantId,Restaurant.class);//As seen in Eureka, RESTAURANTLISTING.  Eureka will figure this path out.

    }

    /**
     * Go hit the database and get the list of food items of a restaurant
     * @param restaurantId
     * @return
     */
    private List<FoodItem> fetchFoodItemList(Integer restaurantId) {
        return foodItemRepo.findByRestaurantId(restaurantId);
    }
}

```

in controller package
`FoodCatalogueController.java`
```java
package com.codedecode.foodcatalogue.controller;

import com.codedecode.foodcatalogue.dto.FoodCataloguePage;
import com.codedecode.foodcatalogue.dto.FoodItemDTO;
import com.codedecode.foodcatalogue.repo.FoodItemRepo;
import com.codedecode.foodcatalogue.service.FoodCatalogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/foodCatalogue")
public class FoodCatalogueController {

    @Autowired
    FoodCatalogueService foodCatalogueService;


    @PostMapping("/addFoodItem")
    public ResponseEntity<FoodItemDTO> addFoodItem(@RequestBody FoodItemDTO foodItemDTO){
        FoodItemDTO foodItemSaved = foodCatalogueService.addFoodItem(foodItemDTO);
        return new ResponseEntity<>(foodItemSaved, HttpStatus.CREATED);
    }

    @GetMapping("/fetchRestaurantAndFoodItemsById/{restaurantId}")
    public ResponseEntity<FoodCataloguePage> fetchRestaurantDetailsWithFoodMenu(@PathVariable Integer restaurantId){
        FoodCataloguePage foodCataloguePage = foodCatalogueService.fetchFoodCataloguePageDetails(restaurantId);
        return new ResponseEntity<>(foodCataloguePage,HttpStatus.OK);
    }
}

```


in application.yml
```yml
server:
  port: 9092

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  profiles:
    active: dev
  application:
    name: FOOD-CATALOGUE-SERVICE
  datasource:
    url: jdbc:mysql://localhost:3306/foodcataloguedb
    password: ""
    username: ""
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

Create a database:
```sql
CREATE database foodcataloguedb;
```

Then run, start the Eureka server first, then  run foodcatalogue microservice.

Check mysql workbench, you will see 2 tables there. `food_item`and `food_item_seq`

Go to eureka server's `localhost:8761`then check for registered service.

![Eureka](images/1.%20Eureka-displaying-services.png)

You will see `FOODCATALOGUEMICROSERVICE`.

#### Testing

Go to postman and add items.
1. POST: `http://localhost:9092/foodCatalogue/addFoodItem`

```json
{
    "itemName": "Dal Fry",
    "itemDescription": "Lentils cooked with mixed spices",
    "veg": true,
    "price": 350,
    "restaurantId": 1,
    "quantity": 0

}
```

It was a success.

2. GET: `http://localhost:9092/foodCatalogue/fetchRestaurantAndFoodItemsById/1`

Output:

```json
{
    "foodItemsList": [
        {
            "id": 1,
            "itemName": "Dal Fry",
            "itemDescription": "Lentils cooked with mixed spices",
            "price": 350,
            "restaurantId": 1,
            "quantity": 0,
            "veg": true
        }
    ],
    "restaurant": {
        "id": 1,
        "name": "Restaurant 1",
        "address": "Address line 1",
        "city": "Nürnberg",
        "restaurantDescription": "Restaurant Description"
    }
}
```

### Order Microservice


**Tech Stack**

* When we Hit OrderNow button,  the order details and user id reached backed, This MS  saves all order details like 
  * Food items list
  * Restaurant details
  * User Information

* Tech stack used 

  * Microservice architecture
  * Rest APIs
  * Java 23
  * Mongo DB as Datasource
  * Spring Boot
  * Lombok
  * Eureka Client

Note we changed datasource to mongoDB
* Install MongoDB and Compass.
* Compass is one way of seeing all the documents in your MongoDB connection.

Order MS

All will have order id as key
* Restaurant Detals
* User Details
* Food Item list

If we are using relational database,
* We will join , user_id, restaurant id, with order id, this will seem complecated.
* But in mongodb, its simpler.
You can have one big document nosql doc, a JSON document, where:

```json
{
  order id: 1
  restaurant {
    rid
    desc
  }
  food item {}
  user detail {}
}
```
  * So no foreign key, 
  * no joins.
  * no need to maintain multiple tables.

Project : Maven

Spring Boot: 3.3.4

Packaging: Jar

Java: 23

Dependencies:
  * Lombok
  * Spring Web
  * Spring Data MongoDB
  * Eureka Discovery Client


Packages:
* controller
* dto
* entity
* repo
* service

Create a dto that we will be getting from the `frontend` and from the `user details microservice`

This will be the exact DTO `RestaurantDTO ` as the `restaurantlisting` microservice.

Restaurant.java:
```java
package com.codedecode.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {
    private int id;
    private String name;
    private String city;
    private String restaurantDescription;
}

```

Next, we need the list of food Items.
Should be exactly similar to `foodcatalogue Microservice` DTO.


These 3 DTOs are saved in the database which are:  FoodItemDTO is the list coming from the frontend, the Restaurant  DTO and userDTO.


OrderDTO comming from frontend.

```java
package com.codedecode.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private List<FoodItemsDTO> foodItemsList;
    private Integer userId;
    private Restaurant restaurant;

}

```

We need tp generate the order ID automatically and save it in the MongoDB document. But now MongoDB does not generate sequence table like MySQL., so create a sequence generator for each order saved.

In the `entity` package, the only thing we need to save it Order. and it is going to be saved with orderId.

We need `@Document("order")` for mongodb.
We will also have restaurant information and UserDTO as well.

3 inner JSONS:
```java
private List<FoodItemsDTO> foodItemsList;
    private Restaurant restaurant;
    private UserDTO userDTO;

```

```java
package com.codedecode.order.entity;

import com.codedecode.order.dto.FoodItemsDTO;
import com.codedecode.order.dto.Restaurant;
import com.codedecode.order.dto.UserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("order")
public class Order {
    private  Integer orderId;
    private List<FoodItemsDTO> foodItemsList;
    private Restaurant restaurant;
    private UserDTO userDTO;

}

```

**repo**

Lets start creating repositories, mongo repository:

```java
package com.codedecode.order.repo;

import com.codedecode.order.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends MongoRepository<Order,Integer> {
}

```

**service**

**controller**

OrderDTO does not contain user information, so we rename this `OrderDTO`to `OrderDTOFromFE` thus From FrontEnd.

Now we need a orderDTO that is going to return when exact order is saved in the DB.
So we create `OrderDTO` which is true replication of Order.java in entity.
continue `controller`:

What we are getting from frontend are here: `OrderDTOFromFE orderDetails`, next, save in DB.

```java
package com.codedecode.order.controller;

import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/saveOrder")
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTOFromFE orderDetails){
        OrderDTO orderSavedInDB =orderService.saveOrderInDb(orderDetails);
        return  new ResponseEntity<>(orderSavedInDB, HttpStatus.CREATED);
    }
}

```
Next add list of items and restaurant  from OrderDTOFromFE.

Create entity Sequence:

``Sequence.java`:
```java
package com.codedecode.order.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sequence")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Sequence {
    @Id
    private String id;
    private int sequence;
}

```

Now we need `SequenceGenerator.java`.

```java
package com.codedecode.order.service;

import com.codedecode.order.entity.Sequence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import static org.springframework.data.mongodb.core.query.Criteria.where;



@Service
public class SequenceGenerator {

    @Autowired
    MongoOperations mongoOperations;

    public int generateNextOrderId(){
        Sequence counter= mongoOperations.findAndModify(
                Query.query(where("_id").is("sequence")),
                new Update().inc("sequence", 1),
                FindAndModifyOptions.options().returnNew(true).upsert(true),
                Sequence.class
        );
        return  counter.getSequence();
    }
}

```

findAndModify takes 4 things: 
* Query, 
* Update which is the sequence key in sequence entity, 
* Options, the modified documents has to be returned
* Entity class which is the sequence.

Not, `generateNextOrderId` gets sequence id.
see the powerpoint. 

Now in `OrderService`:

```java

package com.codedecode.order.service;

import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.dto.UserDTO;
import com.codedecode.order.entity.Order;
import com.codedecode.order.mapper.OrderMapper;
import com.codedecode.order.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class OrderService {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    SequenceGenerator sequenceGenerator;

    @Autowired
    RestTemplate restTemplate;

    public OrderDTO saveOrderInDb(OrderDTOFromFE orderDetails) {
        Integer newOrderID= sequenceGenerator.generateNextOrderId();
        UserDTO userDTO = fetchUserDetailsFromUserId(orderDetails.getUserId());//null;
        Order orderToBeSaved = new Order(newOrderID, orderDetails.getFoodItemsList(),orderDetails.getRestaurant(),userDTO);
        orderRepo.save(orderToBeSaved);
        return OrderMapper.INSTANCE.mapOrderToOrderDTO(orderToBeSaved);

    }


    private UserDTO fetchUserDetailsFromUserId(Integer userId){
        return restTemplate.getForObject("http://USERINFORMATION/user/fetchUserById/"+userId,UserDTO.class);//http://USER-SERVICE/user/fetchUserById/
    }

}

```

in `restTemplate.getForObject("http://USERINFORMATION/user/fetchUserById/"+userId,UserDTO.class);` url name `USERINFORMATION` was copied from pom.xml name all capitalized, you can also copy the name as it is registered in Eureka server.

In `Order orderToBeSaved = new Order(newOrderID, orderDetails.getFoodItemsList(),orderDetails.getRestaurant(),userDTO);`
We need to detch order details from userID. 

to get user details, we need a loadbalanced bean for restTemplate:

```java
package com.codedecode.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class OrderMsApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMsApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate getRestTemplate(){
		return  new RestTemplate();
	}
}

```

`return OrderMapper.INSTANCE.mapOrderToOrderDTO(orderToBeSaved);` Shows that you need a mapper. 

OrderMapper.java:
```java
package com.codedecode.order.mapper;

import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface OrderMapper {

    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    Order mapOrderDTOToOrder(OrderDTO orderDTO);
    OrderDTO mapOrderToOrderDTO(Order order);
}

```

Next `application.yml`:
```yml
server:
  port: 9094
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/


  application:
    name: ORDER-SERVICE
  data:
    mongodb:
      url: mongodb://localhost:27017/orderdb
      host: localhost
      port: 27017
      database: orderdb
      repositories:
        enabled: true
```
run eureka server first,  add other microservices and other microservices:

![All Microservices](images/2_Eureka_all_Microservices.png)

Open Mongodb compass, as you can see, the database is not created. 
Open MongoDb compass, click plus sign and add connection. 

Open Postman, to Test REST APIs.
POST:` http://localhost:9094/order/saveOrder` click send
```json
{
    "foodItemsList":[
        {
            "id":1,
            "itemName": "Dal FRY",
            "itemDescription": "Lentils cooked with mixed spices",
            "veg": true,
            "price": 350,
            "restaurantId": 1,
            "quantity": 0
        }
    ],
    "restaurant": {
        "id": 1,
        "name": "Restaurant 1",
        "name": "Address line 1",
        "city": "Nürnberg",
        "restaurantDescription": "Restaurant Description"
    },
    "userId":1
}
```

![Order Service Postman](images/3_POST_orderService.png)

Go to MongoDB and Refresh: `localhost:27017`.

![Sequence MongoDB](images/4_Sequence_MongoDB.png)

![Order MongoDB](images/5_Order_MongoDB.png)

As seen above, data for userDTO was also fetched from MySQL.

for refresh, see userdb:
![Userdb](images/6_userdb_MySQL.png)

Please note, there was no Hashing and Salting done to this password stored in the database. And passwords used was random thus a placehoder before Hashing and salting is applied.


## FrontEnd with Angular

### Introduction to Node js and VSCode

**Angular Projects**

First check the angular version being used:
 
```sh
ng version

# output below
Angular CLI: 18.2.6
Node: 20.9.0
Package Manager: npm 10.1.0
OS: win32 x64
```

This project uses `Angular 16`:

To follow a Project that uses Angular 16 while you are using Angular 18, you can either downgrade your Angular version to match the project.

1. Uninstall Angular CLI:

```sh
npm uninstall -g @angular/cli
```

2. Install Angular CLI version 16:
```sh
npm install @angular/cli@16
```

3. Create a new Angular 16 project:

```sh
ng new <Project-name>

```
4. Navigate to your project directory:

```sh
cd <Project-name>

```
5. Serve the project:

```sh
ng serve
```





**Some Important Node js commands**

* **npm install -g @angular/cli**
  * Used to install Angular CLI. In code it wil create node modules.
* The Angular CLI is a command-line interface tool that you use to initialize, develop, run and maintain Angular applications.
* But Before you can use Angular CLI, you must have
  * Node.js 6.9.0 and
  * npm 3.0,0 or higher installed on your system.
* **ng new my-first-project**
  * The Angular CLI makes it easy to create an application that already works, right out of the box. It already follows all best practices!

* **Package.json and Package-lock.json**
  * **Package.json** has 2 main sub-parts:  dependencies and devDependencies
    * dependepencies has 3 main types of packages:
      * angular packages
      * support packages: e.g. rxjs: This is the frontend we are designing. How will frontend interact with backend via HTTP connection.
    * devDependencies: For developing application
    * versioning: `x` is major version, `y` is minor version and `z` is pach version.
      * `~`(tilda sign):Install anything  equal to or  the package with the higher patch version. patch is the `z` thing.
      * `^`(carrot sign): Install anything with the same major, but higher or the latest minor or the patch, meaning keep x as the same but you can upgrade the latest verion of `y` and `z`.
  * **package-log.json**:

    Looking at this, we will see a Tree structure. Now how will you make sure that your project is built with the same package  versions in different environment and in different time. 

    E.g. We have seen versioning system in Package.json file that is caret system and root System. Now in my local. if I've used the caret sign with `1.2.3` as my version and when this particular project goes into production, it says either this or the latest version, your code will work different in production. This is the issue created by verisoning in `Package.json` file. Thats is why we have `package-log.json`.

    package.json file does nothing but actually creates the exact version of what you have taken in the package.json file so that the same version works at production and same version works at development.


**Angular file Description**

[Food delivery-app-FE](https://github.com/udemy-dev-withK8s-AWS-codedecode/food-delivery-app-FE)

The file [app.module.ts](https://github.com/udemy-dev-withK8s-AWS-codedecode/food-delivery-app-FE/blob/master/src/app/app.module.ts) defines the root module for you named App module that tells Angular how to assemble your application together.

Lets first understand what is bootrapping from [main.ts](https://github.com/udemy-dev-withK8s-AWS-codedecode/food-delivery-app-FE/blob/master/src/main.ts), bootrapping is where the application is loaded when Angular comes into life.
There was something called `index.html` there we were getting int ascript that is Main.js which is actually calling the main.js file.

So from `index.html->main.ts` file which is bootstrapping or initiating app module file which is `app.module.ts`.

So the flow goes from `index.html->main.ts->app.module.ts` and app.module.ts bootstraps the app component

**app.component.ts**
* defines the logic of the `app-root` component at this `AppComponent`:

`app.component.ts`
```ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'food-delivery-app';
}
```

The view associated with this root component becaomes the root of the view hierarchy or the dome

Defines the html associated with the `app.component.ts`:
`app.component.html`:
```html
<app-header></app-header>
<router-outlet></router-outlet>
```

[app.component.spec.ts](https://github.com/udemy-dev-withK8s-AWS-codedecode/food-delivery-app-FE/blob/master/src/app/app.component.spec.ts): is for unit testing the app component files.


In **app.module.ts** file, `NGmodule` are simply TypeScript classes which are decorated with elaborate NG module decorator, which is actually imported from angular code library. 

The purpose of this `NGmodule` is to declare each thing you create in angular and group them together just like java packages.

modules provide a way for developers to organize their code and they are particarly very helpful when your app grows in size.

So your application always have at least one module, which is a root module, which is app module which if you can remeber we bootstrap in `main.ts` file.

Now lets understand what are these `declarations` and `imports` and other things.
app.module.ts:
```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderModule } from './header/header.module';
import { RestaurantListingModule } from './restaurant-listing/restaurant-listing.module';
import { HttpClientModule } from '@angular/common/http';
import { FoodCatalogueModule } from './food-catalogue/food-catalogue.module';
import { OrderSummaryModule } from './order-summary/order-summary.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule,
    RestaurantListingModule,
    HttpClientModule,
    FoodCatalogueModule,
    OrderSummaryModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
```

So NG module is defined by class decorator `@NgModule`.

`@NgModule` is a function that takes a single object.

As you can see, it takes a `JSON` object. this `JSON`object, contains the properties that describes the module. 

* `declarations`: this is the most important. It declares views to make them privately available in a module.

* `export`:
If you want to make any of your module available to another module, there is another thing called `export`
* import: for any component to work, it might require a help of other modules because many of the times you might need  these things to embed into your current module or component.
So import is wherre you import other modules.

* providers: arrays of dependencies you are going to inject in your module. so the dependency injections, the services will be explained later. 

Component is only the view, but the actual logic or connection to the backend and databases, everything is done in something called services.
These services are injected into the components. 

### Package.json and Package-lock.json


Package.json contains 2 main parts: dependencies and devDependencies.

* dependencies:
Are actually essential for running your application.

* devDependencies:
Are only necessary to develop your angular application for example is something which is required to build your angular.



### Angular files Description


### Service and Dependency injection in Angular

Only write HTML or your views part  on component, but always try to get and put your logics and everything to derive that data you show on the component in services.

## Code for Front End Food Delivery Application

Looking at the wireframe:  from [page 47 to 50](https://docs.google.com/presentation/d/10yM2esLSyXUrY0qOuR9-JfoXwm9UEMRbVYDnAC9-fVM/edit#slide=id.g249f60b2267_0_5)


### Quick start with Angular app and wiremock

### Setting up the front end of Food delivery application.

* Install Node.js: Angular requires `Node.js and npm`.
* Install Angular CLI
Since I am using angular 18, I will run:
```sh
npm uninstall -g @angular/cli
```
Then Install angular 16
```sh
npm install -g @angular/cli@16
```
* Create a new Angular Project
```sh
ng new food-delivery-app
```
  * add Angular routing? y
  * Style sheet, default: css

* Navigate to the project directory
```sh
cd food-delivery-app
```
* serve the application:
```sh
ng serve
```


The Source Projects uses:

|package| Source| Mine|
|---|---|---|
|npm version|9.3.1|10.1.0|
|node version|v18.14.0|v20.9.0|
|Angular CLI| 16|16|


* Project name: food-delivery-app
In `src/index.html`, you will see the selector `<app-root></app-root>` which can be seen in `app.component.ts`:

```ts
import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'food-delivery-app';
}

``` 
* `@Component`is responsible to give a view to your screen.
* so `selector`, the `templateUrl`,`styleUrls` and `AppComponent`(reperesents the data) together gives us the screen view.

run `ng serve` it compiles successfully.
So this comes from 3 files, `app.component.html`, `app.component.css` `app.component.ts`.

To make it modularized, Modules has been created.
* `app.module.ts` is the root of our application. It has 4 parts.
   `declarations`, `imports`, `providers` and `bootstrap`.
```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

```

* The declaration says what modules are created as a part of `appComponent`. Our task is to create different modules.

* imports: this is the array of modules we will be needing. e.g BrowserModule, Http Modules like like `HttpClientModule`for http calls.
* Providers: nothing but service layers, so there in Java you might have service layer having business logic, but here in Angular, in the service layer, we make connection to backend and databases.
* Bootstrap, creates instances of app component.

We will need 4 segregated module:
* header module,
* Restaurantlisting module
* Foodcatalogue Module
* Order Summary module

**So first is header Module**:

Example:
* `cd .\src\app\`
* `ng generate module restaurant-listing --routing`
* `ng generate component  restaurant-listing --module=restaurant-listing`

After generating Module, you generate component.

Part of components: HTML,CSS and component. While component.spec.ts is for `junit` part.

Also, Routing is very important seen in `app-routing.module.ts`. Aimed not to change your index.html but to embed your data in the body like `<app-root></app-root>`.

Lets make HTML empty. in `app.component.html`.

We willl use [Router Outlet](https://docs.google.com/presentation/d/10yM2esLSyXUrY0qOuR9-JfoXwm9UEMRbVYDnAC9-fVM/edit#slide=id.g24bd14b3a50_0_18)

Remmemeber, first, we will generate a module, then create component inside the module.

* `cd ./src/app`
*  `ng g module header --routing`
  I wrote --routing, because i want to create a route file for this.
  * So it has header module and header routing module.
  * next is 4 files, html, css, junit and component.ts
* `ng g c header --module=header` to generate the 4 files.
  * This name `module=header`is there because module name is header.
* Lets create subfolders called `module`,`service`,`components` to organize our files.
  * In header module, Copy the 4 component files to component folder. Note, `header.component.html`and `header.component.spec.ts`was updated.
  * Notice in header.module.ts, `import { HeaderComponent } from './components/header.component';`was updated.
* In `app.component.html`, before routing, show header.
  
  `header/components/header.component.ts`

```ts
  import { Component } from '@angular/core';

  @Component({
    selector: 'app-header',
    templateUrl: './header.component.html',
    styleUrls: ['./header.component.css']
  })
  export class HeaderComponent {

  }
```

in app.component.html: Notice `<app-header></app-header>`

```html
<app-header></app-header>
<router-outlet></router-outlet>
```
This means before routing, show header, what so ever page you navigate to.

Now in `header.module.ts`.
Whenever, you create a componetn, its always a private component, to make it public,  you need to export more here. 
`exports:[HeaderModule]`;

```ts
import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { HeaderRoutingModule } from './header-routing.module';
import { HeaderComponent } from './components/header.component';


@NgModule({
  declarations: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    HeaderRoutingModule
  ],
  exports:[HeaderComponent]
})
export class HeaderModule { }

```
* Now in `app.module.ts`,  we need to import header module. `HeaderModule`

```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderModule } from './header/header.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

```

* now we put what is required. in the header module's components. `header.component.html`. refering to `{{ username }}`.

```html
<header class="header">
    <div class="header-container">
      <h1 class="header-logo">Code Decode Food Delivery</h1>
      <nav class="header-nav">
        <div class="user-avatar">
            <img src="assets/avatar-pics/avatar.jpg" alt="User Avatar">
            <span class="username">Hey {{ username }}</span>
          </div>
      </nav>
    </div>
  </header>
```
* go to `header.component.ts`: `username = 'JohnDoe';`
Go to assets and add avatar.jpg.
Add styles in both style.css which is global  and `header/components/header.component.css`

### Restaurant List page designing in Angular

Lets create restaurant listing module
* `cd src/app/`
* `ng g module restaurant-listing --routing`
* We create restaurant listing component: `ng g c restaurant-listing --module=restaurant-listing`
* We need to store that data in some pojo or some object, that object will be storing in `models`folder. Creat `models` folder
* To write a logic to hit the backend  service that is `restaurantlisting microservice`, we need a service layer,so create `service` folder. 
* Create a `components` folder and copy the 4 components into it.
  * notice the component.ts is updates
  * but we need to update `restaurant-listing.module.ts`. We changed the part: to `import { RestaurantListingComponent } from './components/restaurant-listing.component';` because we moved it into `components`folder

We will start working on this  wireframe:
![Restaurant Listing Wireframe](images/7_RestaurantListing_Wireframe.png)

* in `app.component.html` we need to route from it to `restaurant-listing.component.ts`
  * in `app-routing.module.ts`, we need to specify please  navigate me to restaurant-lisitng page using `{ path: '', redirectTo: 'restaurant-listing', pathMatch: 'full' }` as seen below:
```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'restaurant-listing', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }

```

`{ path: '', redirectTo: 'restaurant-listing', pathMatch: 'full' }` means path: '': This means, when part is empty, thus `localhost:4200` no other part, Please redirect me to: `restaurant-listing` page and Match the full one. 

* Also to use this `restaurant-listing` in the `app-routing.module.ts`, we need to list this module as import in our `app.module.ts`.
In  `app.module.ts` add `RestaurantListingModule`as an import.
```ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderModule } from './header/header.module';
import { RestaurantListingModule } from './restaurant-listing/restaurant-listing.module';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule,
    RestaurantListingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

```
* Now in Restaurant listing also, we need to do some routing.in `restaurant-listing-routing.module.ts`: 
So add this path: `{ path: '', component: RestaurantListingComponent }`

```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { RestaurantListingComponent } from './components/restaurant-listing.component';

const routes: Routes = [
  { path: '', component: RestaurantListingComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class RestaurantListingRoutingModule { }
```
If anything comes to me, route me to restaurant listing component, thus from `restaurant-listing-routing.module.ts` to `Components/restaurant-listing.component.ts/` from there to,

```ts

@Component({
  selector: 'app-restaurant-listing',
  templateUrl: './restaurant-listing.component.html',
  styleUrls: ['./restaurant-listing.component.css']
})
```


in `index.html` there is `<app-root></app-root>` selector, which embeds 2 lines in `app.component.html`:

```html
<app-header></app-header>
<router-outlet></router-outlet>
```

`App-routing.module.ts` redirects to  `restaurant-listing` module using `{ path: '', redirectTo: 'restaurant-listing', pathMatch: 'full' }` as seen below:


```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', redirectTo: 'restaurant-listing', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
```

from restaurant-listing module to restaurant-listing components.

Looking at the wireframe page:

![Restaurant Listing Wireframe](images/7_RestaurantListing_Wireframe.png)

Now we create Html, css and then create the typescript file to get the data and also then create the service file.

That service file will be responsible to make a connection from my angular application to my backend application  that is Restaurant Listing Microservice.


It will hit an endpoint, get all restaurant,It will fetch in a DTO, save it in a DTO and will give you as a data from Typescript file to HTML file.

* First:Create a service file: `restaurant.service.ts`: 

```ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { API_URL_RL } from 'src/app/constants/url';

@Injectable({
  providedIn: 'root'
})
export class RestaurantService {

  private apiUrl = API_URL_RL+'/restaurant/fetchAllRestaurants'; 

  constructor(private http: HttpClient) { }

  getAllRestaurants(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  private handleError(error: any) {
    console.error('An error occurred:', error);
    return throwError(error.message || error);
  }
}
```

`@Injectable` means we have created this as a service, Then I have created a URL for the restaurant listing microservice: `API_URL_RL` and call `'/restaurant/fetchAllRestaurants'`.

I am going to have `HttpClient`for this, `getAllRestaurants()`method using http.get, fetch all restaurant and call this URL that I have created.

Return type is Observable of type any. I am going to receive data using the typescript file of restaurant `components/restaurant-listing.component.ts`. The I am going to call this method `getAllRestaurant()` and add it to my DTO. 

First create the url.

 Create `constants` folder with file named `url.ts`, It is going to have all localhost URLs to hit different microservices.

 `url.ts`
 ```ts
  export const API_URL_RL ='http://localhost:9091';
  export const API_URL_Order ='http://localhost:9094';
  export const API_URL_FC ='http://localhost:9092';
  export const API_URL_UD ='http://localhost:9093';
 ```

 Now you can see that my localhost for restaurant listing microservice is `http://localhost:9091'`
 In getAllRestaurants() method we are using HTTP here `return this.http.get<any>(`${this.apiUrl}`)`  we will add HTTPClient to `app.module.ts` as seen below:
 ```ts
   imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule,
    RestaurantListingModule,
    HttpClientModule
  ]
 ```

Since the port number is port:  4200 for angular app and restaurant listing Microservice is `9091`, these 2 ports are different, so request from angular will be blocked, thus cross-origin issues.

Enable cross-origin in spring-boot, in `RestaurantController` using `@CrossOrigin`.:

```java
package com.codedecode.restaurantlisting.controller;

import com.codedecode.restaurantlisting.dto.RestaurantDTO;
import com.codedecode.restaurantlisting.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
  ...
  }
```
Notice `/restaurant/fetchAllRestaurants`.

Check Eureka using `localhost:8761`, you should see the all backend microservices registered to Eureka.

Now create a POJO(Restaurant Detail) similar to Restaurant-Listing microservice. Now this restaurant details that we are going to get is not only going to be used in restaurant listing microservice, but that restaurant detail is also going to be used in the food-catalogue page(Remember `foodcatalogue`) microservice.

![Food Catalogue wireframe](images/8_Food_Catalogue_Microservice.png)

So the Restaurant DTO is going to be common between, restaurant listing page and food-catalogue page, thus it should go into the shared folder. `Shared\models\Restaurant.ts`:

```ts

export interface Restaurant {
    id?: number;
    name?: string;
    address?: string ;
    city?: string;
    restaurantDescription?: string;
}
```

This is similar to RestaurantDTO so that the JSON can be directly mapped to the DTO in Restaurant.ts.
```java
package com.codedecode.restaurantlisting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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



* Create a Model which will be capable enough to handle data that is recieved from backend.
* Thirdly create a Typescript file, get the data and serve it to HTML file.

Notice event binding below in `{{restaurant.name}}`
```html

<main class="main-content my-gap" >
    <div class="restaurant-grid">
        <div class="restaurant-card" *ngFor="let restaurant of restaurantList">
          <div class="restaurant-image">
            <img [src]="'assets/restaurant-pics/' + getRandomImage()" alt="Restaurant 1">
          </div>
          <div class="restaurant-details">
            <h3 class="restaurant-name">{{restaurant.name}}</h3>
            <div class="restaurant-rating">
              <span class="rating"><i class="fa fa-star"></i>4.5</span>
              <span class="rating-count">(500+)</span>
            </div>
            <button class="order-btn" (click)="onButtonClick(restaurant.id)">Order Now</button>
          </div>
        </div>
      </div>
    </main>
    
    
```

Now lets create the 3 methods seen in the HTML, in `restaurant-listing.component.ts` They include `restaurantList`, `getRandomImage()` and `onButtonClick(restaurant.id)`:

```ts
export class RestaurantListingComponent {
  public restaurantList: Restaurant[];

  ngOnInit() {
   this.getAllRestaurants();
  }
  constructor(private router: Router, private restaurantService: RestaurantService) { }

  getAllRestaurants() {
    this.restaurantService.getAllRestaurants().subscribe(
      data => {
        this.restaurantList = data;
      }
    )
  }


}

```

To prevent initialization of `restaurantList` in  `public restaurantList: Restaurant[];` use `tsconfig.json` and add `"strictPropertyInitialization": false,`to it to prevent stricttype checking.This is because we do not want to initialize our list.

Now I want to inject my service in my component,  I can do that with my constructor. `constructor(private router: Router, private restaurantService: RestaurantService) { }`. Because of the button click we need to route to another page.

As soon as restaurant list component is loaded, data should be loaded so wee need `ngOninit()` to call the service and get populated.

We also need random images. and onButtonClick and navigate to `food-catalogue page.


Now in `Restaurant-listing.component.ts`:
```ts
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Restaurant } from 'src/app/Shared/models/Restaurant';
import { RestaurantService } from '../service/restaurant.service';

@Component({
  selector: 'app-restaurant-listing',
  templateUrl: './restaurant-listing.component.html',
  styleUrls: ['./restaurant-listing.component.css']
})
export class RestaurantListingComponent {
  public restaurantList: Restaurant[];
  ngOnInit() {
    this.getAllRestaurants();
  }

  constructor(private router: Router, private restaurantService: RestaurantService) { }

  getAllRestaurants() {
    this.restaurantService.getAllRestaurants().subscribe(
      data => {
        this.restaurantList = data;
      }
    )
  }
  getRandomNumber(min: number, max: number): number {
    return Math.floor(Math.random() * (max - min + 1)) + min;
  }


  getRandomImage(): string {
    const imageCount = 8; // Adjust this number based on the number of images in your asset folder
    const randomIndex = this.getRandomNumber(1, imageCount);
    return `${randomIndex}.jpg`; // Replace with your image filename pattern
  }

  onButtonClick(id: number) {
    this.router.navigate(['/food-catalogue', id]);
  }
}

```
The next is food-catalogue when Button is clicked.

### Food catalogue page designing in Angular

We will be creating again a component, the routing, the service, modules and components.

Lets create food catalogue module
* `cd src/app/`
* Create a module: `ng g module food-catalogue --routing`
* We create a components in food catalogue module: `ng g c food-catalogue --module=food-catalogue`
* We need to store that data in some pojo or some object, that object will be storing in `models`folder. Create `models` folder
* To write a logic to hit the backend  service that is `food Catalogue microservice`, we need a service layer,so create `service` folder.
*  Create a `components` folder and copy the 4 components into it.
  * notice the component.ts is updates. Also notice a `update imports for ...` message
  * If not updated automatically, but we need to update `food-catalogue.module.ts`. We changed the part: to `import { FoodCatalogueComponent } from './components/food-catalogue.component';` because we moved it into `components`folder

We will start working with the Food Catalogue Wireframe page.

![Food Catalogue Wireframe page](images/8_Food_Catalogue_Microservice.png)


This is responsible to get all the data e.g. 
* Restaurant name
* Restaurant description
* And list of food menu related to that restaurant only.
* And to have Checkout button.


To create that, lets create service layer to hit the backend application `food listing microservice`, which will internally hit the `restaurant listing microservice`, get the `restaurant data , also will hit its own database, the food item database to get the food items only for that restaurant service.
* Create `fooditem.service.ts` in `service`
  * This food Item service is going to have a link to 9092.

```ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { API_URL_FC } from 'src/app/constants/url'; 

@Injectable({
    providedIn: 'root'
})
export class FoodItemService {

    private apiUrl = API_URL_FC+'/foodCatalogue/fetchRestaurantAndFoodItemsById/';

    constructor(private http: HttpClient) { }

    getFoodItemsByRestaurant(id:number): Observable<any> {
        return this.http.get<any>(`${this.apiUrl+id}`)
          .pipe(
            catchError(this.handleError)
          );
      }
    
      private handleError(error: any) {
        console.error('An error occurred:', error);
        return throwError(error.message || error);
      }

}
```

We call the restaurant to fetch the food catalogue and restaurant by ID.
Go to the food Catalogue controller, we have `@RequestMapping("/foodCatalogue")` and `@GetMapping("/fetchRestaurantAndFoodItemsById/{restaurantId}")` which are equivalent to `private apiUrl = API_URL_FC+'/foodCatalogue/fetchRestaurantAndFoodItemsById/';`in `fooditem.service.ts`.

I created a method to get fooditem by id `getFoodItemsByRestaurant(id:number)` and passing number.  remember `restaurant-listing.component.ts`,

restaurant-listing.component.ts:
```ts
...
onButtonClick(id: number){
  this.router.navigate(['/food-catalogue',id]);
}
```

Thats the same id we pass here: `getFoodItemsByRestaurant(id:number)`.

In Spring boots the FoodCatalogue Microservice, we have 2 main DTO.

* `FoodItemDTO.java`, which is going to have food item description.

```java
package com.codedecode.foodcatalogue.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodItemDTO {

    private int id;

    private String itemName;

    private String itemDescription;

    private  boolean isVeg;

    private Number price;

    private  Integer restaurantId;
    private Integer quantity;
}

```
* One is the `FoodCataloguePage.java` which is the actual combination of those two, the FoodItem list for all the food items in that restaurant and the restaurant details.

```java
package com.codedecode.foodcatalogue.dto;

import com.codedecode.foodcatalogue.entity.FoodItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor


public class FoodCataloguePage {

    private List<FoodItem> foodItemsList;

    private Restaurant restaurant;
}

```
* Restaurant:
```java
package com.codedecode.foodcatalogue.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Restaurant {

    private int id;
    private String name;
    private String address;
    private String city;
    private String restaurantDescription;
}

```

So we are creating the 3 structures similar to the `dto` folder in Spring boot.

We have already created `Restaurant.ts`in the `Shared` folder.
```ts
export interface Restaurant {
    id?: number;
    name?: string;
    address?: string ;
    city?: string;
    restaurantDescription?: string;
}
```

* Since we have `Restaurant.ts` we only need to create 2 more DTOs that is `.ts` and `FoodCataloguePage.ts` in the Shared module.

`FoodItem.ts`:
```ts
export interface FoodItem {
    id?: number;
    itemName?: string;
    itemDescription?: string;
    isVeg?: boolean;
    price?: number;
    restaurantId?: number;
    quantity: number;
}
```

Both FoodItem and FoodCataloguePage similar to Java DTO.

`FoodCataloguePage.ts`
```ts
import { FoodItem } from "./FoodItem";
import { Restaurant } from "./Restaurant";

export interface FoodCataloguePage{
    foodItemsList:FoodItem[];
    restaurant:Restaurant;
}
```

* The names of the fields should be same for both backend and Frontend, otherwise the Json will not be able to be parsed properly into the POJO.

Now both Pojo and service are ready, this will hit the restaurant listing also and get the complete data to us now.

now create HTML and CSS pages
Then create the TypeScript for this.

In `restaurant-lisitng.component.ts`, we added page navigation, we need a router navigating to catalog with ID as the parameter.
```ts
...
onButtonClick(id: number) {
    this.router.navigate(['/food-catalogue', id]);
  }
```

We need to put the same route `food-catalogue/:id` above in routing model `food-catalogue-routing.module.ts`.

`food-catalogue-routing.module.ts`:
```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { FoodCatalogueComponent } from './components/food-catalogue.component';

const routes: Routes = [
  { path: 'food-catalogue/:id', component: FoodCatalogueComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class FoodCatalogueRoutingModule { }
```

in `app.module.ts`import  FoodCatalogueModule as seen below.

```ts
imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule,
    RestaurantListingModule,
    HttpClientModule,
    FoodCatalogueModule
  ],
```

18:56

see database foodcataloguedb

Make sure you create a database:
```sql
CREATE DATABASE foodcataloguedb;
```

Data run in mysql:
```sql
INSERT INTO food_item (id, item_name, item_description, is_veg, price, restaurant_id, quantity) VALUES
(1, 'Vegetable Biryani', 'Delicious vegetarian dish', true, 300, 1, 0),
(2, 'Butter Chicken', 'Succulent chicken in a creamy sauce', false, 310, 1, 0),
(3, 'Dal Tadka', 'Mouthwatering lentil curry', true, 350, 2, 0),
(4, 'Rogan Josh', 'Spicy and flavorful lamb curry', false, 315, 2, 0),
(5, 'Aloo Tikki', 'Crispy and spicy potato patties', true, 500, 3, 0),
(6, 'Tandoori Paneer Tikka', 'Paneer cubes marinated in tandoori spices', true, 900, 3, 0),
(7, 'Chicken Biryani', 'Fragrant and aromatic rice dish', false, 120, 4, 0),
(8, 'Vegetable Korma', 'Mixed vegetable curry', true, 110, 4, 0),
(9, 'Goan Prawn Curry', 'Spicy and tangy shrimp curry', false, 140, 5, 0),
(10, 'Naan', 'Fluffy Indian bread', true, 300, 5, 0),
(11, 'Chicken Tikka', 'Chicken marinated in yogurt and spices', false, 100, 6, 0),
(12, 'Kheer', 'Aromatic rice pudding', true, 600, 6, 0),
(13, 'Medu Vada', 'Savory lentil donuts', true, 400, 7, 0),
(14, 'Masala Dosa', 'Crispy crepe filled with spiced potatoes', false, 800, 7, 0),
(15, 'Mango Lassi', 'Refreshing yogurt-based drink', true, 500, 8, 0),
(16, 'Basket of Rotis', 'Assorted Indian bread basket', false, 700, 8, 0),
(17, 'Dal Fry', 'Lentils cooked with mixed spices', true, 350, 1, 0);

```

schema.sql:

```sql
CREATE TABLE IF NOT EXISTS food_item (
    id INT AUTO_INCREMENT PRIMARY KEY,
    item_name VARCHAR(255) NOT NULL,
    item_description TEXT,
    is_veg BOOLEAN,
    price DECIMAL(10, 2),
    restaurant_id INT,
    quantity INT DEFAULT 0
);

```

I altered my Entity, FoodItem:
```java
package com.codedecode.foodcatalogue.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String itemName;

    private String itemDescription;

    private  boolean isVeg;

    @Column(precision = 10, scale = 2)// matches DECIMAL(10, 2) in SQL schema
    private BigDecimal price;

    private  Integer restaurantId;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer quantity;
}

```
I changed `private number price` to `private BigDecimal price`.

In Postman, output looks like this:

```json
{
    "foodItemsList": [
        {
            "id": 1,
            "itemName": "Vegetable Biryani",
            "itemDescription": "Delicious vegetarian dish",
            "price": 300.00,
            "restaurantId": 1,
            "quantity": 0,
            "veg": true
        },
        {
            "id": 2,
            "itemName": "Butter Chicken",
            "itemDescription": "Succulent chicken in a creamy sauce",
            "price": 310.00,
            "restaurantId": 1,
            "quantity": 0,
            "veg": false
        },
        {
            "id": 17,
            "itemName": "Dal Fry",
            "itemDescription": "Lentils cooked with mixed spices",
            "price": 350.00,
            "restaurantId": 1,
            "quantity": 0,
            "veg": true
        }
    ],
    "restaurant": {
        "id": 1,
        "name": "The Hungery Hut",
        "address": "Address line 1",
        "city": "Nürnberg",
        "restaurantDescription": "Restaurant Description"
    }
}
```
Next we need order summary to be created.

### Order page front end in Angular

We should be able to save the order in MongoDB in form of orderdb, But we need a userdb in mySQL which has relation with the orderdb in mongodb.


We will be creating again a component, the routing, the service, modules and components.

Lets create food catalogue module
*  `cd food-delivery-app`
* `cd src/app/`
* Create a module: `ng g module order-summary --routing`
* We create a components in order summary module: `ng g c order-summary --module=order-summary`
* We need to store that data in some pojo or some object, that object will be storing in `models`folder. Create `models` folder
* To write a logic to hit the backend  service that is `order summary microservice`, we need a service layer,so create `service` folder.
*  Create a `components` folder and copy the 4 components into it.
  * notice the component.ts is updates. Also notice a `update imports for ...` message
  * If not updated automatically, but we need to update `order-summary.module.ts`. We changed the part: to `import { OrderSummaryComponent } from './components/order-summary.component';` because we moved it into `components`folder

I only created service folder, nothing is inside

The service layer will be the one responsible to add the complete cart information and also the `user ID` and hit the backend `microservice order service` and save the whole order in the database.

In Service I create a service where all the data including the `foodItem` list the proper `quantity`, the `restaurant details` and the `userID` will go to the backend. create `order-service.ts`.

`order-service.ts`:
```ts
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import {  API_URL_Order } from 'src/app/constants/url'; 

@Injectable({
  providedIn: 'root'
})

export class OrderService {

  private apiUrl = API_URL_Order+'/order/saveOrder';

  constructor(private http: HttpClient) { }

 httpOptions = {
    headers: new HttpHeaders({
      'Content-Type':'text/plain',
      'Access-Control-Allow-Origin': 'http://localhost:4200' // Replace with your Angular app URL
    })
  };

  saveOrder(data: any):Observable<any>  {
    return this.http.post<any>(this.apiUrl, data);
  }

  private handleError(error: any) {
    console.error('An error occurred:', error);
    return throwError(error.message || error);
  }


}
```

Here `saveOrder(data: any)` we are hitting the HTTP post request, and save all data in database.


make sure to add `@CrossOrigin` in `OrderController.java`
```java
package com.codedecode.order.controller;

import com.codedecode.order.dto.OrderDTO;
import com.codedecode.order.dto.OrderDTOFromFE;
import com.codedecode.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@CrossOrigin
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/saveOrder")
    public ResponseEntity<OrderDTO> saveOrder(@RequestBody OrderDTOFromFE orderDetails){
        OrderDTO orderSavedInDB =orderService.saveOrderInDb(orderDetails);
        return  new ResponseEntity<>(orderSavedInDB, HttpStatus.CREATED);
    }
}

```

order service is ready, next we will be creating **model**. It will have 3 things:
* list of food items in OrderDTO
* restaurant details
* user ID.
  
Similar to OrderDTOFromFE.
```java
package com.codedecode.order.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTOFromFE {

    private List<FoodItemsDTO> foodItemsList;
    private Integer userId;
    private Restaurant restaurant;

}

```

Create in models Similar to java, Typescript OrderDTO.ts:
```ts
import { FoodItem } from "src/app/Shared/models/FoodItem";
import { Restaurant } from "src/app/Shared/models/Restaurant";

export interface OrderDTO{

    foodItemsList?: FoodItem[];
    userId?: number;
    restaurant?: Restaurant;
}
```

Now HTML and CSS file.

![Order Summary Wireframe](images/9_Order_Summary_Wireframe.png)

Now in components, HTML:
```html
<div class="order-summary">
    <h1>Order Summary</h1>
    <table>
      <thead>
        <tr>
          <th>Item</th>
          <th>Quantity</th>
          <th>Price</th>
        </tr>
      </thead>
      <tbody>
        <tr *ngFor="let item of obj.foodItemsList">
          <td>{{ item.itemName }}</td>
          <td>{{ item.quantity }}</td>
          <td>&#x20b9; {{ item.price }}</td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <td colspan="2">Total</td>
          <td>&#x20b9; {{ total }}</td>
        </tr>
      </tfoot>
    </table>
    <button class="checkout-button" (click)="saveOrder()">Place Order</button>
  </div>
  
  
  <div class="dialog-container" *ngIf="showDialog">
    <div class="dialog-box">
      <h2>Order Placed!</h2>
      <p>Your order has been successfully placed.</p>
      <button class="close-button" (click)="closeDialog()">Close</button>
    </div>
  </div>
  
```

We will calculate total ` <td>&#x20b9; {{ total }}</td>` in typescript file, We also need to write this method `saveOrder()` to handle events.
After placing the order successfully, there should be a dialog button shown `closeDialog()`.

Lets get the list of items from the route, Remember `food-catalogue.component.ts` we will extract the foodItemsList:
```ts
...
onCheckOut() {
    this.foodItemCart;
    this.orderSummary = {
      foodItemsList: [],
      restaurant: null
    }
    this.orderSummary.foodItemsList = this.foodItemCart;
    this.orderSummary.restaurant = this.foodItemResponse.restaurant;
    this.router.navigate(['/orderSummary'], { queryParams: { data: JSON.stringify(this.orderSummary) } });
  }
```

So we map our routes:  `foodItemsList` `<tr *ngFor="let item of obj.foodItemsList">`
Go to Order Component 's ts' file:

`order-summary.component.ts`:

```ts
import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../service/order.service';
import { OrderDTO } from '../models/OrderDTO';

@Component({
  selector: 'app-order-summary',
  templateUrl: './order-summary.component.html',
  styleUrls: ['./order-summary.component.css']
})
export class OrderSummaryComponent {

  orderSummary?: OrderDTO;
  obj: any;
  total?: any;
  showDialog: boolean = false;

  constructor(private route: ActivatedRoute, private orderService: OrderService, private router: Router) { }
  
  ngOnInit() {
    const data = this.route.snapshot.queryParams['data'];
    this.obj = JSON.parse(data);
    this.obj.userId=1;
    this.orderSummary = this.obj;

    this.total = this.orderSummary.foodItemsList.reduce((accumulator, currentValue) => {
      const quantity = currentValue.quantity || 0;
      const price = currentValue.price || 0;
      return accumulator + (quantity * price);
    }, 0);

  }

  saveOrder() {
    this.orderService.saveOrder(this.orderSummary)
      .subscribe(
        response => {
            this.showDialog = true;
        },
        error => {
          console.error('Failed to save data:', error);
        }
      );
  }

  closeDialog() {
    this.showDialog = false;
    this.router.navigate(['/']); // Replace '/home' with the actual route for your home page
  }





}
```

`router.navigate(['/']` takes you to the restaurant listing page.

Update the module in our `app.module.ts`:
```ts
...
imports: [
    BrowserModule,
    AppRoutingModule,
    HeaderModule,
    RestaurantListingModule,
    HttpClientModule,
    FoodCatalogueModule,
    OrderSummaryModule
  ],
```

Add routing to the to connect the Checkout and OrderSummary thus map OrderSummary in `food-catalogue.component.ts` to  `order-summary-routing.module.ts`  like `{ path: 'orderSummary', component: OrderSummaryComponent }`.

`order-summary-routing.module.ts`:
```ts
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { OrderSummaryComponent } from './component/order-summary.component';

const routes: Routes = [
  { path: 'orderSummary', component: OrderSummaryComponent }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class OrderSummaryRoutingModule { }
```

In `const routes: Routes = [{ path: 'orderSummary', component: OrderSummaryComponent }];`

* `routes`: This constant defines an array of route configurations.
* `path`: 'orderSummary': This specifies the URL path that will trigger this route.
* `component`: OrderSummaryComponent: This specifies the component that will be displayed when the URL path is matched.

When I click `Place Order` button a dialog box pops up, the data should go back to the Order Microservice then save to MongoDB. sequence increases to 4.

![MongoDB Angular](images/10_MongoDB_Angular.png)

Please note, this application is missing: 
* Security JWT, 
* salting and hashing password, 
* API documentation with swagger, 
* Defining the schema.sql in Resources for all Microservices. Probabily adding data.sql to resources.
* Testing with Junit e.t.c

It runs very well, I will now put this application in a folder. The use its copy for further process.


## All about Docker and Dockerization of food delivery application.

Our Backend services includes:
1. Eureka for discovery
2. Restaurant listing service: List all the restaurant in my area
3. Food catalog service to  list all the food items available in that restaurant.
4. Order service: When you plce your order
5. User service to give user information to you.

So in all these 5 applications, we are going to add a docker image.
* For Frontend, Food delivery application, we are also going to add a docker image.

Total of 6 docker images.

### Eureka Dockerfile

I need to use JDK 22 instead of JDK 11. Here’s a breakdown of my Dockerfile and the steps to change the JDK version.

#### Generating the `.jar` with Maven

To package your application into a `.jar` file with Maven, run the following command in your project directory:

```bash
mvn clean package
```

This will create a `.jar` file in the `target` directory, which will then be used in the Docker build process.The file generated is `eureka-0.0.1-SNAPSHOT.jar`

#### Explanation of the Current Dockerfile

```dockerfile
FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
```

1. **FROM adoptopenjdk/openjdk11:jdk-11.0.2.9-slim**: This line specifies the base image, which currently uses OpenJDK 11. To update it for Java 22, you'll need to use an appropriate OpenJDK 22 image.
  
2. **WORKDIR /opt**: Sets the working directory to `/opt` within the container, where your application will run.

3. **COPY target/*.jar /opt/app.jar**: Copies the compiled `.jar` file from your `target` directory on the host to the container’s `/opt` directory, naming it `app.jar`.

4. **ENTRYPOINT exec java $JAVA_OPTS -jar app.jar**: Starts the application by running `java` with any environment options defined in `$JAVA_OPTS`.

#### Changing the Dockerfile to Use JDK 22

1. Replace the base image with one that uses JDK 22. For instance:

   ```dockerfile
   FROM eclipse-temurin:22-jre  # Using an OpenJDK 22 image from Eclipse Temurin
   ```

2. Update the rest of the file as needed (most of it will remain the same):

```dockerfile
FROM eclipse-temurin:22-jre  #Using an OpenJDK 22 image from Eclipse Temurin
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar

```


### Restaurant listing Microservice Dockerfile

Similar thing here:

#### Generating the `.jar` with Maven

To package your application into a `.jar` file with Maven, run the following command in your project directory:

```bash
mvn clean package
```

This will create a `.jar` file in the `target` directory, which will then be used in the Docker build process.The file generated is `restaurantlisting-0.0.1-SNAPSHOT.jar`

```dockerfile
FROM eclipse-temurin:22-jre  #Using an OpenJDK 22 image from Eclipse Temurin
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar
```


### Foodcatalogue Microservice Dockerfile

Similar thing here:

#### Generating the `.jar` with Maven

To package your application into a `.jar` file with Maven, run the following command in your project directory:

```bash
mvn clean package
```

This will create a `.jar` file in the `target` directory, which will then be used in the Docker build process.The file generated is `foodcatalogue-0.0.1-SNAPSHOT.jar`

```dockerfile
FROM eclipse-temurin:22-jre  #Using an OpenJDK 22 image from Eclipse Temurin
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar
```


### Order Microservice Dockerfile

Similar thing here:

#### Generating the `.jar` with Maven

To package your application into a `.jar` file with Maven, run the following command in your project directory:

```bash
mvn clean package
```


This will create a `.jar` file in the `target` directory, which will then be used in the Docker build process.The file generated is `order-0.0.1-SNAPSHOT.jar`

```dockerfile
FROM eclipse-temurin:22-jre  #Using an OpenJDK 22 image from Eclipse Temurin
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar
```



### User Information Microservice Dockerfile

Similar thing here:

#### Generating the `.jar` with Maven

To package your application into a `.jar` file with Maven, run the following command in your project directory:

```bash
mvn clean package
```


This will create a `.jar` file in the `target` directory, which will then be used in the Docker build process.The file generated is `userinfo-0.0.1-SNAPSHOT.jar`

```dockerfile
FROM eclipse-temurin:22-jre  #Using an OpenJDK 22 image from Eclipse Temurin
WORKDIR /opt
COPY target/*.jar /opt/app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /opt/app.jar
```
### Alternatively to generate jar:


in Maven, run `mvn clean install`, this will create jar for target folder.

### Create Docker file for Frontend Application

Angular front-end is called `food-delivery-app`.

```Dockerfile
# Stage 1: Build the Angular app
FROM node:16 as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve the Angular app using Nginx
FROM nginx:alpine
COPY --from=build /app/dist/food-delivery-app /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

This Dockerfile is set up to build and serve an Angular frontend application called `food-delivery-app`. It uses a multi-stage build to optimize the final image, keeping it lightweight. Here’s a breakdown of each stage:

#### Stage 1: Build the Angular App
This stage uses a Node.js environment to compile the Angular app.

```dockerfile
FROM node:16 as build
```
- **FROM node:16 as build**: This uses the Node.js version 16 image and labels it as `build` for referencing in later stages.
  
```dockerfile
WORKDIR /app
```
- **WORKDIR /app**: Sets the working directory to `/app` in the container, where all subsequent commands will run.

```dockerfile
COPY package*.json ./
RUN npm install
```
- **COPY package*.json ./**: Copies `package.json` and `package-lock.json` files to the container. This is done first to utilize Docker caching for dependency installation.
- **RUN npm install**: Installs all dependencies listed in `package.json`.

```dockerfile
COPY . .
RUN npm run build
```
- **COPY . .**: Copies the entire project into the `/app` directory within the container.
- **RUN npm run build**: Runs the Angular build command, which compiles the app into static files and outputs them into the `dist/food-delivery-app` directory.

#### Stage 2: Serve the Angular App Using Nginx
This stage uses Nginx to serve the built static files, creating a lightweight final image that doesn't include Node.js.

```dockerfile
FROM nginx:alpine
```
- **FROM nginx:alpine**: Uses the `alpine` version of Nginx, a lightweight version suitable for serving static files.

```dockerfile
COPY --from=build /app/dist/food-delivery-app /usr/share/nginx/html
```
- **COPY --from=build /app/dist/food-delivery-app /usr/share/nginx/html**: Copies the static files from the `build` stage to the Nginx default HTML directory, so Nginx can serve the Angular app.

```dockerfile
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```
- **EXPOSE 80**: Exposes port 80, allowing the app to be accessed via HTTP.
- **CMD ["nginx", "-g", "daemon off;"]**: Starts Nginx in the foreground, keeping the container running.

#### Summary

1. **Stage 1** builds the Angular app.
2. **Stage 2** uses Nginx to serve the built files efficiently, resulting in a smaller, production-ready container.


### Now build all images

* Run Docker desktop
* Docker build -t **codedecode25/app-name:0.0.1**  .
* 0.0.2 means version
* . represents current director
* Apps can be `- restaurant-listing-service`, `order-service` etc. All lower case segregated by a hyphen for better readability

Lets start from Eureka:
* namespace in docker hub: `aduzona`
* app-name eureka-server: `Docker build -t aduzona/food-delivery-eureka-server:0.0.1  .`
* Log in to Docker Hub using:
  * `docker login`
* Push image: `Docker push aduzona/food-delivery-eureka-server:0.0.1`

Restaurant listing:
* Build: `Docker build -t aduzona/food-delivery-restaurant-listing-service:0.0.1  .`
* Push image: `Docker push aduzona/food-delivery-restaurant-listing-service:0.0.1`

Food catalogue service:

* Build: `Docker build -t aduzona/food-delivery-food-catalogue-service:0.0.1  .`
* Push it: `Docker push aduzona/food-delivery-food-catalogue-service:0.0.1`

Order Service:

* Build image: `Docker build -t aduzona/food-delivery-order-service:0.0.1  .`
* Push it: `Docker push aduzona/food-delivery-order-service:0.0.1`

User info service:

* Build image: `Docker build -t aduzona/food-delivery-user-service:0.0.1  .`
* Push it: `Docker push aduzona/food-delivery-user-service:0.0.1`

Angular App:

* Build image: `Docker build -t aduzona/food-delivery-app-frontend:0.0.1  .`
* Push it: `Docker push aduzona/food-delivery-app-frontend:0.0.1`

## Spring boot profiling

Spring boot profiling configure application based on the environemnt.

remember urls changes as you move an application. will we keep on chnging urls everytime we deploy to a different environment. e.g http://localhost:8761/eureka/ will keep on changing.

Suppose I have an environemnt like dev, then once it is passed from dev it goes to test, then to prod. you keep on cahnging this properties, thats nt a good idea.

Spring Boot profiling is a feature that allows you to segregate parts of your application configuration and make them available only in certain environments. This is particularly useful when you want to have different configurations for development, testing, and production environments.

Here are some key points about Spring Boot profiling:

1. **Profiles**: Profiles are a way to group beans and configurations that should only be active in specific environments. For example, you can have a `dev` profile for development, a `test` profile for testing, and a `prod` profile for production
2. **@Profile Annotation**: You can use the `@Profile` annotation to specify which beans should be loaded for a particular profile. For example, a bean annotated with `@Profile("dev")` will only be loaded when the `dev` profile is active
3. **Activating Profiles**: Profiles can be activated in various ways, such as through the `spring.profiles.active` property in the `application.properties` file, as a JVM argument, or programmatically within your application
4. **Default Profile**: If no profile is explicitly activated, Spring Boot will use the default profile. You can specify the default profile using the `spring.profiles.default` property.
5. **Profile-Specific Properties**: You can have profile-specific properties files, such as `application-dev.properties` and `application-prod.properties`, which contain configuration settings specific to each profile

Spring Boot profiling helps you manage different configurations for different environments, making your application more flexible and easier to maintain.

How Profiling works:

* **Configuration Files**: Spring Boot allows you to define multiple configuration files, each associated with a specific profile. These files are typically named `application-{profile}.properties` or `application-{profile}.yml`, where {profile} is the name of the profile.

* **Application Properties**: Inside **each profile-specific configuration file, you can specify different properties based on your requirements**. These properties can include database connection details, logging settings, cache configurations, and any other application-specific configurations.

* **Activating Profiles**: You can activate a specific profile by setting the `spring.profiles.active` property. This can be done in various ways, such as by **setting an environment variable, using the -D flag when running the application**, or programmatically in code.
For example, to activate the development profile, you can set `spring.profiles.active=development`.

in application.yml, are just common configurations.

### RestaurantListing Springboot

application.yml:
```yml

server:
  port: 9091

spring:
  profiles:
    active: dev

  application:
    name: RESTAURANT-SERVICE
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application.local.yml:
* port is common(in `application.yml`) so we removed it.
* Removed spring application name which is common.
```yml

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurantdb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-dev.yml:
* Here we are going to use k8s and it will have its own deployment files.
* Thus remove spring , hibernate, datasource e.t.c
* In dev, the url for eureka is: ` http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka`
```yml
eureka:
  instance:
    preferIpAddress: true
    hostname: eureka-0
  client:
    service-url:
      defaultZone:  http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/restaurantdb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
```

Next in `application.yml` we activate Profiles: with `spring.profile.active=dev`, thus only application-dev.yml will be picked up.

#### Meaning of `http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka`

* **http://** This specifies the protocol used for communication, in this case, HTTP.

* **eureka-0**: This is the hostname of the Eureka server instance within the Kubernetes cluster. The "0" in the hostname suggests that it might be the first instance of the Eureka server. The specific naming convention may vary depending on the deployment configuration.

* **eureka-service**: This is the name of the Kubernetes service associated with the Eureka server. Kubernetes services provide a stable endpoint for accessing pods within the cluster.

* **default**: This is the namespace in which the Eureka service and pods are deployed. Kubernetes namespaces provide a way to isolate resources within a cluster.

* **svc.cluster.local**: This is the domain suffix for Kubernetes services within the cluster.

* **8761**: This is the port number on which the Eureka server is listening for incoming requests. By default, Eureka uses port 8761 for its HTTP-based communication.

* **/eureka**: This is the path or endpoint within the Eureka server. In this case, it refers to the Eureka server's own API endpoint. Typically, this endpoint is used by Eureka clients (services) to register themselves and discover other services.

* In summary, it **represents the Eureka server's endpoint within a Kubernetes cluster**, where services can register and discover other services using the Eureka service registry.

Generate another jar file
```sh
mvn clean install
```

When building a JAR file with Maven, the commands `mvn clean package` and `mvn clean install` serve different purposes:

1. **`mvn clean package`**:
   - This command cleans the project (removes the `target` directory) and then packages the project into a JAR file.
   - It compiles the source code, runs tests, and packages the compiled code into a JAR file located in the `target` directory.
   - It does not install the JAR file into the local Maven repository.

2. **`mvn clean install`**:
   - This command also cleans the project and packages it into a JAR file.
   - In addition to packaging, it installs the JAR file into the local Maven repository (`~/.m2/repository`).
   - This makes the JAR file available for other projects on your local machine to use as a dependency.

In summary, `mvn clean package` creates the JAR file, while `mvn clean install` creates the JAR file and installs it into the local Maven repository for use by other projects. If you only need the JAR file for the current project, `mvn clean package` is sufficient. If you want to use the JAR file as a dependency in other projects, you should use `mvn clean install`.

If you have any more questions or need further clarification, feel free to ask!


**Build the image with a new tag**:
*  Open docker desktop then run below
```sh
docker build -t aduzona/food-delivery-restaurant-listing-service:latest .
docker push aduzona/food-delivery-restaurant-listing-service:latest
```


### Food catalogue spring boot

application.yml
```yml
server:
  port: 9092

logging:
  level:
    org.hibernate: DEBUG
    org.hibernate.type: TRACE
spring:
  cache:
    type: none
  profiles:
    active: dev
  application:
    name: FOOD-CATALOGUE-SERVICE
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-local.yml
```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/foodcataloguedb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-dev.yml
```yml
eureka:
  instance:
    preferIpAddress: true
    hostname: eureka-0
  client:
    service-url:
      defaultZone: http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/foodcataloguedb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver

```

```sh
mvn clean install
```

```sh
docker build -t aduzona/food-delivery-restaurant-listing-service:latest .
docker push aduzona/food-delivery-restaurant-listing-service:latest
```

### Order spring boot

application.yml
```yml
server:
  port: 9094

spring:
  profiles:
    active: dev
  application:
    name: ORDER-SERVICE
    data:
    mongodb:
      url: mongodb://localhost:27017/orderdb
      host: localhost
      port: 27017
      database: orderdb
      repositories:
        enabled: true
```

application-local.yml
```yml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/foodcataloguedb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-dev.yml
```yml
eureka:
  instance:
    preferIpAddress: true
    hostname: eureka-0
  client:
    service-url:
      defaultZone: http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka
```

```sh
mvn clean install
```

```sh
docker build -t aduzona/food-delivery-order-service:latest .
docker push aduzona/food-delivery-order-service:latest
```

### User Info spring boot

application.yml
```yml
server:
  port: 9093

spring:
  profiles:
    active: dev
  application:
    name: USER-SERVICE
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-local.yml
```yml
eureka:
  instance:
    preferIpAddress: true
    hostname: eureka-0
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/userdb
    password: Diego#91spring
    username: root
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    database-platform: org.hibernate.dialect.MySQL8Dialect
```

application-dev.yml
```yml
eureka:
  client:
    service-url:
      defaultZone: http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka

```

```sh
mvn clean install
```

```sh
docker build -t aduzona/food-delivery-user-service:latest .
docker push aduzona/food-delivery-user-service:latest
```


### Additions

Now profiling is now complete
With AWS load balancer also so that external clients should be able to interact with our backend  and frontend applications. for external users to be able to interact with our backend  and frontend applications we will need a gateway or a load balancer which provides an external IP to the whole echo system we have created. 

Thus we will use ingress and load balancer for that.

But to be able to seamlessly work with Eureka with the AWS load balancer, we would be needing two extra properties to be added in the development properties, so that we will not get `unknown host exception`.

application-dev.yml
```yml
eureka:
  instance:
    preferIpAddress: true
    hostname: eureka-0
  client:
    service-url:
      defaultZone: http://eureka-0.eureka-service.default.svc.cluster.local:8761/eureka

```

This property `eureka.instance.preferIpAddress=true`  and `eureka.instance.hostname=eureka-0` do is when you load balance your application, you might find unknown host exception for `eureka-0` because there might be multiple ports and to figure out which IP address will alway be difficult for a load balancer, you will always add a `preferIPAddress: true`so that now your DNS resoluction from host name to IP is very easy beacuese of this customized spring boot property.

Then run:

```sh
mvn clean install
```

then build and push for all backend containers.
## AWS

Deploy in AWS EKS cluster.
cluster name: `aws-eks-cluster` 

chololatey is windows equivalent of brew.

### AWS EKS

run: `aws sts get-caller-identity` you will see:
* UserId
* Account
* Arn


[Create EKS cluster](eksctl.io). A simple way to do this:

```sh
eksctl create cluster --name cluster name --region region name --nodegroup-name node name --node-type t3.medium --nodes 1
```


see pdf for details.

e.g 
run below
```sh
eksctl create cluster --name aws-eks-cluster --region eu-west-1 --nodegroup-name eks-cluster-node --node-type t3.medium --nodes 1
```

to delete:
```sh
eksctl delete cluster --name aws-eks-cluster --region eu-west-1
```

Go to ireland and search for `aws-eks-cluster`.

when successful, it will generate kubeconfig file.

#### Kubeconfig

* The kubeconfig file is necessary for authenticating and accessing the EKS cluster.

* By default, the kubeconfig file is updated with the necessary information to connect to the newly created EKS cluster, allowing you to use tools like kubectl to interact with the cluster from your local machine..

Go to `users/aduzo/.kube/config`, then open config with notepad

check how many nodes from local computer, you can see it:
`kubectl get nodes`: you will see

## Cloud Databases


### What is AWS RDS and why to use it

* We can Deploy Our DB also as a Deployment / POD 

* But what if POD crashes?

* Your data is lost and that's the task of DB - to manage data

* So now u have following options

  * Deployments with Persistent volumes - but not recommended bcz its for stateless apps
  * Stateful set with PV - difficult to manage n create
  * Best way is to segregate it completely outside the cluster 
  * So use AWS RDS

**What is AWS RDS**:

* AWS RDS (Amazon Relational Database Service) is a fully managed database service provided by Amazon Web Services (AWS). It enables you to set up, operate, and scale relational databases in the cloud easily. 

* By leveraging AWS RDS, you can **quickly provision database instances**, scale resources up or down as needed, and easily manage your databases using the AWS Management Console, command-line interface (CLI), or APIs. This allows you to focus on building applications without worrying about the underlying infrastructure and maintenance of your databases.

* With AWS RDS, we have the option to choose from various popular relational database engines including
  * **MySQL**
  * PostgreSQL
  * MariaDB
  * Oracle
  * Microsoft SQL Server



Go to AWS RDS and create a relational database in `eu-west-1` for mysql.

`/RDS/Create database`:
* Standard create: Because we need custome options
* MySQL
* Templates: Free tier
* Settings: DB instance identifier: write `mysql-db-instance`
* Credential Settings:
  * Master username: `admin`
  * create and confirm password.
  * Instance configuration remains same.
* Connectivity:
  * Don't connect to an EC2 compute resource
  * VPC: default selected
    * But you can create a vpc before creating RDS
  * Public access: Yes, though this is not recommended
  * VPC security group(firewall): Choose existing
  * Existing VPC security groups: default
  * Additional configuration: 
    * Database port:3306
* Database authentification
  * Password authentification
* Additional configuration
  * initial dabase name:`restaurantdb`
* create database

Estimated monthly costs:

The Amazon RDS Free Tier is available to you for 12 months. Each calendar month, the free tier will allow you to use the Amazon RDS resources listed below for free:
* 750 hrs of Amazon RDS in a Single-AZ db.t2.micro, db.t3.micro or db.t4g.micro Instance.
* 20 GB of General Purpose Storage (SSD).
* 20 GB for automated backup storage and any user-initiated DB Snapshots.

![Amazon_RDS MySQL](images/11_Amazon_RDS.png)

Go to security group and modify it
`mysql-db-instance/Connectivity & security/Security/VPC security groups`:
default (sg-0...)
Lets modify it to be able to be accessible from outside also.

* Add Inbound rules:

Traffic comes from outside to here.

|Security Group rule ID| Type|Protocol|Port range|Source|Source IP|
|---|---|---|---|---|---|
|sgr-044...|Custom TCP| TCP | 3306| Anywhere IPv4| 0.0.0.0.0.0/0|

From anywhere a query comes


In mysql-db-instance: 

You will see connectivity & Security, you will see `Endpoint & port`.
You can connect to db using the endpoint.

Connect anywhere to my DB instance. Using MySQL Workbench.

![MySQL Workbench_Test Connection](images/12_Test_Amazon_RDS_Connection.png)

Connection Name: AWS_RDS_Connection
Hostname: mysql-db-instance.c7qy6u80qseu.eu-west-1.rds.amazonaws.com
Username: admin
Password: click Store in Vault... and add Password

Test connection.

now create all DBs reqired and populate our Data
* Table Data Wizard
To copy the content of one database table in one connection to another table in a different connection in MySQL Workbench, follow these steps:

**Option 1: Export and Import via File**

1. Export the Table from Source Connection

* Connect to the source database.
Right-click on the table you want to copy, and select "Table Data Export Wizard".
* Choose the export location and save the file (e.g., as a .csv file).
* Complete the wizard to export the data.
2. Import the Table into Destination Connection

* Switch to the destination connection.
* Right-click on the destination database, and select "Table Data Import Wizard".
* Choose the exported file (from step 1).
* Map the columns if necessary and complete the wizard to import the data.


Or just do it manually, 

Please make sure the db names are same with application.yml  names for proper connection. 

Copy the databases foodcataloguedb, restaurantdb,userdb. 

Now go to application-dev.yml for all of them and change datasource.url e.g `userdb` from `jdbc:mysql://localhost:3306/userdb` to AWS Endpoint: `mysql-db-instance.c7um2qieq6sx.eu-west-1.rds.amazonaws.com/userdb`

#### Database

##### Restaurant

```sql

-- Use the database
USE restaurantdb;

-- Create the `restaurant` table
CREATE TABLE IF NOT EXISTS restaurant (
    id INT PRIMARY KEY,
    address VARCHAR(255),
    city VARCHAR(255),
    name VARCHAR(255),
    restaurant_description VARCHAR(255)
);

-- Insert data into the `restaurant` table
INSERT INTO restaurant (id, address, city, name, restaurant_description)
VALUES
(1, '123 Main Street', 'Anytown', 'The Hungry Hut', 'A cozy family-owned restaurant offering a diverse menu of international cuisines. From sizzling steaks to mouthwatering pasta dishes, we have something to satisfy every craving'),
(2, '456 Elm Avenue', 'Townsville', 'Spice Fusion', 'Experience the vibrant flavors of India at Spice Fusion. Our talented chefs blend traditional Indian spices with a modern twist, creating a culinary journey that will delight your taste buds'),
(3, '789 Oak Lane', 'Metropolis', 'Seafood Paradise', 'Indulge in the freshest catch of the day at Seafood Paradise. With a prime waterfront location, our restaurant offers a wide selection of seafood delicacies, prepared with a touch of coastal flair'),
(4, '321 Maple Road', 'Riverside', 'Tandoori Delight', 'Step into a slice of Italy at La Bella Trattoria. Our charming trattoria-style restaurant serves authentic Italian dishes made from scratch using the finest ingredients. Buon appetito!'),
(5, '987 Pine Street', 'Lakeside', 'The Green Garden', 'Embrace healthy eating at The Green Garden. Our vegetarian and vegan-friendly restaurant showcases the best of plant-based cuisine, with colorful salads, hearty bowls, and nutritious smoothies.'),
(6, '555 Chestnut Avenue', 'Belleview', 'Curry House', 'The Savory Bistro offers an exquisite dining experience with a focus on seasonal, locally sourced ingredients. Our talented chefs create artful plates that blend flavors from around the world, ensuring a memorable culinary adventure'),
(7, '222 Walnut Street', 'Harborview', 'Saffron Sizzlers', 'Sushi Haven invites you to savor the art of Japanese cuisine. Our skilled sushi chefs meticulously craft an array of fresh sushi rolls, sashimi, and delectable tempura, all served in a modern and elegant setting'),
(8, '777 Oakwood Drive', 'Summitville', 'Fireside Grill & Bar', 'Set against a rustic backdrop, Fireside Grill & Bar is the perfect spot for a cozy meal. Our menu features hearty grilled favorites, signature cocktails, and a warm ambiance that will make you feel right at home');

```

##### FoodCatalogue

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS foodcataloguedb;

-- Use the database
USE foodcataloguedb;

-- Create the `food_item` table
CREATE TABLE IF NOT EXISTS food_item (
    id INT PRIMARY KEY,
    is_veg BIT(1),
    item_description VARCHAR(255),
    item_name VARCHAR(255),
    price DECIMAL(10,2),
    quantity INT,
    restaurant_id INT
);

-- Insert data into the `food_item` table
INSERT INTO food_item (id, is_veg, item_description, item_name, price, quantity, restaurant_id)
VALUES
(1, 1, 'Delicious vegetarian dish', 'Vegetable Biryani', 300.00, 0, 1),
(2, 0, 'Succulent chicken in a creamy sauce', 'Butter Chicken', 310.00, 0, 1),
(3, 1, 'Mouthwatering lentil curry', 'Dal Tadka', 350.00, 0, 2),
(4, 0, 'Spicy and flavorful lamb curry', 'Rogan Josh', 315.00, 0, 2),
(5, 1, 'Crispy and spicy potato patties', 'Aloo Tikki', 500.00, 0, 3),
(6, 1, 'Paneer cubes marinated in tandoori spices', 'Tandoori Paneer Tikka', 900.00, 0, 3),
(7, 0, 'Fragrant and aromatic rice dish', 'Chicken Biryani', 120.00, 0, 4),
(8, 1, 'Mixed vegetable curry', 'Vegetable Korma', 110.00, 0, 4),
(9, 0, 'Spicy and tangy shrimp curry', 'Goan Prawn Curry', 140.00, 0, 5),
(10, 1, 'Fluffy Indian bread', 'Naan', 300.00, 0, 5),
(11, 0, 'Chicken marinated in yogurt and spices', 'Chicken Tikka', 100.00, 0, 6),
(12, 1, 'Aromatic rice pudding', 'Kheer', 600.00, 0, 6),
(13, 1, 'Savory lentil donuts', 'Medu Vada', 400.00, 0, 7),
(14, 0, 'Crispy crepe filled with spiced potatoes', 'Masala Dosa', 800.00, 0, 7),
(15, 1, 'Refreshing yogurt-based drink', 'Mango Lassi', 500.00, 0, 8),
(16, 0, 'Assorted Indian bread basket', 'Basket of Rotis', 700.00, 0, 8),
(17, 1, 'Lentils cooked with mixed spices', 'Dal Fry', 350.00, 0, 1);

```



##### User Info

```sql
-- Create the database
CREATE DATABASE IF NOT EXISTS userdb;

-- Use the database
USE userdb;

-- Create the `user` table
CREATE TABLE IF NOT EXISTS user (
    user_id INT PRIMARY KEY,
    address VARCHAR(255),
    city VARCHAR(255),
    user_name VARCHAR(255),
    user_password VARCHAR(255)
);

-- Insert data into the `user` table
INSERT INTO user (user_id, address, city, user_name, user_password)
VALUES
(1, 'somewhere Germany', 'Munich', 'Ife', 'pass123');

```
#### Change Datasource for different MySQL database

Use Endpoint: `mysql-db-instance.****.region.rds.amazonaws.com`

``` yml
spring:
  datasource:
    url: mysql-db-instance.c7qy6u80qseu.eu-west-1.rds.amazonaws.com:3306/restaurantdb
```

same for other microservices:


### Mongo Atlas DB

How to configure NoSQL DB in AWS using MongoDB Atlas used for multi-cloud database.
This is managed by Mongodb. See Udemy_Build_Deploy_End_to_End pdf for more information.


Setup:
*  Sign up and log in: Go to the MongoDB Atlas website 
https://www.mongodb.com/cloud/atlas
 and click on the "Try Free" or "Get Started Free" button. Create a new MongoDB Atlas account 
or log in using your existing MongoDB account.
* Create a new project: Once you're logged in, click on the "Create a New Project" button and 
provide a name for your project. The project acts as a container for your MongoDB clusters 
and related resources.
* Build a cluster: Inside your project, click on the "Build a Cluster" button to create a new 
MongoDB cluster. A cluster is a set of interconnected MongoDB instances that store your 
data.
* Whitelist IP addresses: By default, your cluster only allows connections from IP addresses on 
the Atlas IP whitelist. Go to the "Network Access" tab and click on the "Add IP Address" button 
to add your IP address or IP range to the whitelist. Alternatively, you can allow access from 
anywhere (0.0.0.0/0), but this is not recommended for production environments.
* Configure cluster settings , Wait for cluster creation n Connect to your cluster




Create a Project
* name:  Atlas-Project

Create a cluster
Build a Database;
* M0 Free
* Name: atlas-cluster
* Provider AWS
* Region: eu-west-1 ireland
* Create Deployment

Connect to atlass-cluster

* Set up connection security:
  * Username: admin
  * password: 

* Choose a connection method: 

1. Add a connection IP address
Your current IP address (217.84.1.154) has been added to enable local connectivity. Only an IP address you add to your Access List will be able to connect to your project's clusters. Add more later in 
[Network Access](https://cloud.mongodb.com/v2/67448e0b9817d449a4d06856#/security/network/accessList)

1. Create a database user
A database user has been added to this project. Create another user later in 
[Database Access](https://cloud.mongodb.com/v2/67448e0b9817d449a4d06856#/security/database/users)
.
You'll need your database user's credentials in the next step.


* Connect to your application:
  * Choose Compass
  * I have MongoDB Compass installed
  * copy Connection Spring: mongodb+srv://username:password@atlas-cluster.yumcd.mongodb.net/
    * The above a the structure.
  * Add the Connection String to my Mongo Compass.
  * Create New Connection:

## Kubernetes

## Kubernetes Manifest files

These are just blue print.

4 deployment files for each of the backend applications plus Eureka making it 5.
* restaurant
* food-catalogue
* user
* order
* Eureka

Then Additional files:
* configmap
* Secrets

Rememeber configmaps and secrets are used to hold the key-value pairs of my properties that is used for deployment

Deployment is for pod.



restaurant-manifest.yml
Deployments:
```yml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: restaurantapp
  labels:
    app: restaurantapp
spec:
  replicas: 1
  selector:
    matchLabels:
      app: restaurantapp
  template:
    metadata:
      labels:
        app: restaurantapp
    spec:
      containers:
        - name: restaurantapp
          image: aduzona/food-delivery-restaurant-listing-service:latest
          imagePullPolicy: Always
          ports:
            - containerPort: 9091
          env:
            - name: SPRING_DATASOURCE_USERNAME
              valueFrom:
                secretKeyRef:
                  name: secret
                  key: mysql-username
            - name: SPRING_DATASOURCE_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: secret
                  key: mysql-password
            - name: SPRING_DATASOURCE_URL
              valueFrom:
                configMapKeyRef:
                  name: configmap
                  key: restaurantdb_url
---
apiVersion: v1
kind: Service
metadata:
  name: restaurant-service
spec:
  ports:
    - protocol: TCP
      port: 9091
      targetPort: 9091
  selector:
    app: restaurantapp
```

**3:20**

