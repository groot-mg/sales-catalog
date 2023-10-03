Feature: Checking OpenApi/Swagger endpoints return expected outputs

  Scenario: When application is running, Open API should be exposed
    Given a private endpoint PRIVATE_OPEN_API is prepared
    When the request is sent
    Then the response status code should be 200

  Scenario: When application is running, private Swagger should return a redirection
    Given a private endpoint PRIVATE_SWAGGER_REDIRECTION is prepared
    When the request is sent
    Then the response status code should be 302

  Scenario: When application is running, Swagger UI should be exposed
    Given a private endpoint PRIVATE_SWAGGER_UI is prepared
    When the request is sent
    Then the response status code should be 200