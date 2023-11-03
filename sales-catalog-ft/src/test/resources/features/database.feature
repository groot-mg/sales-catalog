Feature: Scenarios for database down

  Background:
    And database is down

  # Actuator
  Scenario: When database is down, returns 503 response status
    Given a private endpoint PRIVATE_HEALTH_CHECK is prepared
    When the request is sent
    Then the response status code should be 503
    And the health response body of the message should have the status "DOWN"
    And health components should contain the status DOWN:
      | db |

  # Get products
  Scenario: When call to retrieve products and database is down should return 500
    Given an endpoint PRODUCT_GET is prepared
    When the request is sent
    Then the response status code should be 500
    And the response error detail should be Database exception

  Scenario: When call to retrieve product by id gets a database down should return 500
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    When the request is sent
    Then the response status code should be 500
    And the response error detail should be Database exception

  # Create products
  Scenario: When a sales user calls to register a new product and database is down should return internal server error
    Given an endpoint PRODUCT_POST is prepared
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 500
    And the response error detail should be Database exception