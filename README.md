## LADA Backend API

_Logistics Assisted Data Aggregation service_

An example backend for handling data related to transports and transportation units, and access control for users. 

Spring Boot + gradle, JWT authentication.

Utilizes mongobee for database change management and flapdoodle Embedded MongoDB.

### Build and run backend

#### *nix
./gradlew build && java -jar build/libs/lada-api.0.0.1-SNAPSHOT.jar

#### Windows
gradlew build && java -jar build/libs/lada-api.0.0.1-SNAPSHOT.jarjava -jar build\libs\lada-api-0.0.1-SNAPSHOT.jar

### Running Backend

gradlew bootRun

