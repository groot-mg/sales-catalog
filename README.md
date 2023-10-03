# sales-catalog

[![Build and test](https://github.com/groot-mg/sales-catalog/actions/workflows/sales-catalog-ci.yml/badge.svg)](https://github.com/groot-mg/sales-catalog/actions/workflows/sales-catalog-ci.yml) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=groot-mg_sales-catalog&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=groot-mg_sales-catalog) [![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://github.com/groot-mg/sales-catalog/blob/main/LICENSE)

Sales-catalog is used to products and stock management.

## Build, tests and run

### Build
Build with gradle (build + unit tests):
```
./gradlew build
```

### Functional tests
Run functional-tests:
```
./gradlew cucumber
```

`sales-catalog` depends on `Service Discovery`, so wiremock is used to mock it and test the functionalities of the application.

### Run
> [!WARNING]  
> The application contains dependencies and will throw exceptions on the logs if the dependencies are not started

Dependencies:
* `Service discovery` on port 8081
    * To start the Service discovery, go to [service-discovery](https://github.com/groot-mg/service-discovery) and start it manually, or go to [docker-local-setup](https://github.com/groot-mg/docker-local-setup) and start it via docker compose
    * Without the Service discovery the application logs a log of exceptions on console

Local app is available on the port `8082`, health check endpoint is [http://localhost:8082/sales-catalog/private/health](http://localhost:8082/sales-catalog/private/health)

```
./gradlew bootRun
 ```

Alternatively, it is possible to run using `java -jar sales-catalog-app/build/libs/sales-catalog-app.jar`

### Run together with the project

API Gateway should run together with the Service discovery and other services, to run all together, please see [docker-local-setup](https://github.com/groot-mg/docker-local-setup).

## OpenAPI / Swagger

Open API and Swagger UI are available on the endpoints:
- [http://localhost:8082/sales-catalog/private/api-docs](http://localhost:8082/sales-catalog/private/api-docs)
- [http://localhost:8082/sales-catalog/private/swagger](http://localhost:8082/sales-catalog/private/swagger)