## 1. Before running `hotel_service`, make sure to run Kafka Server.
- Start Kafka Server - 
`
PS C:<PATH_TO_KAFKA> .\bin\windows\kafka-server-start.bat .\config\server.properties
`

## 2. The payload package is locally installed (`com.mainak:payload:1.0-SNAPSHOT`)

To install the package locally - 
1. Open the `hotel-booking-microservice-payload` project.
2. Run `./mvnw clean install`