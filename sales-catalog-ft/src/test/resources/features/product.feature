Feature: Product controller scenarios

  # Register product scenarios
  Scenario: When a client user calls to register a new product it should not be allowed
    Given an endpoint PRODUCT_POST is prepared
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 403

  Scenario: When a sales user calls to register a new product should do it
    Given an endpoint PRODUCT_POST is prepared
    And the product request body is set to:
      | name         | description         | price | quantity |
      | Product name | Product description | 10    | 50       |
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 201
    And product table has record:
      | name         | description         | price | quantity | isReserved | isSold | salesUserId | created  | lastUpdate | isDeleted |
      | Product name | Product description | 10.00 | 50       | false      | false  | NON_NULL    | NON_NULL | NULL       | false     |
    And product response object has:
      | name         | description         | price | quantity | isReserved | isSold |
      | Product name | Product description | 10    | 50       | false      | false  |