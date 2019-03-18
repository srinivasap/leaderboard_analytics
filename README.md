======================================================
## Leader Board Analytics API
======================================================

### Supported Operations
- Upload file(s) to process
- Search for leader board by date (by default, today's date)
- Search for top from leader board for each date in the date range


### Runtime / Libraries / Dependencies
- Spring Boot (https://spring.io/projects/spring-boot)
- H2 in-memory Database (http://www.h2database.com/html/main.html)

### API Specifications
- Postman API collection - https://www.getpostman.com/collections/c89331f155a57f08c30b


### Run Instructions
$ ./gradlew bootrun

or

$ java -jar analytics-0.0.1.jar

### Build Instructions
$ ./gradlew clean build