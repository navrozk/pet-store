# Pet Store Demo using AngularJS and Spring Boot

## Technology Stack:

### Service Layer:
- Spring Boot
- Jersey for RESTful services
- Spring Security
- H2 in memory database with some hardcoded initial data supplied on startup via application.properties
- Maven for build
- Unit tests not implemented yet

#### NOTE: H2 console (http://localhost:8080/h2console) is not secured currently for demo purpose.

### UI Layer
- AngularJS
- Bootstrap
- Bower
- Jasmine/Karma

### How to run the application
- cd pet-store/src/main/resources/public/
- bower install
- cd ../../../..
- maven package
- cd target/
- java -jar pet-store-0.0.1-SNAPSHOT.jar

Then access via http://localhost:8080/app/index.html

### Credentials (hardcoded in spring security config for demo)
- Readonly user (Search by petId, view): userid: userA, password: userA
- Admin user (Search by petId, view, add, delete): userid: userB, password: userB