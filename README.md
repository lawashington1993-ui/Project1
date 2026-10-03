# SafetyNet Alerts

SafetyNet Alerts is a Spring Boot REST API for looking up emergency-response information. It keeps people, fire-station assignments, and medical records in a JSON file.

## Requirements

- Java 25
- Maven

## Run the application

From the project root, start the server with:

```bash
mvn spring-boot:run
```

The application listens on port `8080` by default. You can change the port in `src/main/resources/application.properties`.

## Run the tests

```bash
mvn test
```

JaCoCo creates a coverage report at `target/site/jacoco/index.html` after the test phase.

## API endpoints

Read endpoints return JSON:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `GET` | `/` | Shows API status and links to available endpoints. |
| `GET` | `/firestation?stationNumber={number}` | Lists people covered by a station and adult/child counts. |
| `GET` | `/childAlert?address={address}` | Lists children at an address and other household members. |
| `GET` | `/phoneAlert?firestation={number}` | Lists distinct phone numbers covered by a station. |
| `GET` | `/fire?address={address}` | Lists residents and the station assigned to an address. |
| `GET` | `/flood/stations?stations={numbers}` | Groups residents by address for one or more stations. Separate station numbers with commas. |
| `GET` | `/personInfo?lastName={name}` | Lists matching people with contact and medical information. |
| `GET` | `/communityEmail?city={city}` | Lists distinct email addresses for people in a city. |

The API also supports creating, updating, and deleting records:

| Method | Endpoint | Purpose |
| --- | --- | --- |
| `POST` | `/person` | Add a person. |
| `PUT` | `/person` | Update a person. |
| `DELETE` | `/person?firstName={name}&lastName={name}` | Delete a person. |
| `POST` | `/firestation` | Add an address-to-station mapping. |
| `PUT` | `/firestation` | Update a mapping. |
| `DELETE` | `/firestation?address={address}&station={number}` | Delete mapping(s). Either parameter may be omitted. |
| `POST` | `/medicalRecord` | Add a medical record. |
| `PUT` | `/medicalRecord` | Update a medical record. |
| `DELETE` | `/medicalRecord?firstName={name}&lastName={name}` | Delete a medical record. |

For `POST` and `PUT`, send a JSON request body matching the relevant resource. Required fields are validated by the API. Errors are returned as JSON with an `error` field.

## Project layout

- `src/main/java/com/safetynet/alerts/controller/` handles HTTP requests and responses.
- `src/main/java/com/safetynet/alerts/service/` contains data access and age-calculation logic.
- `src/main/java/com/safetynet/alerts/model/` contains the application's data objects.
- `src/main/java/com/safetynet/alerts/dto/` defines the JSON shapes accepted or returned by the API.
- `src/main/java/com/safetynet/alerts/mapper/` converts between DTOs and models.
- `src/main/resources/data.json` is the application's data file.
- `src/test/java/com/safetynet/alerts/` contains automated tests.

## Data storage

The application loads `src/main/resources/data.json` into memory when it starts. Updates are written back to that JSON file. This is a simple storage approach for a learning project; it is not a replacement for a database in a production deployment.

## Technology

- Java 25
- Spring Boot 4.0.0
- Maven
- Log4j2
- JUnit and Spring Boot Test
- JaCoCo for code coverage
