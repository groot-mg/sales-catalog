Feature: Product controller scenarios for delete product

  Scenario: When a call to delete a product has no authentication it should return unauthorised
    Given an endpoint PRODUCT_DELETE is prepared
    When the request is sent
    Then the response status code should be 401

  Scenario: When a client user calls to delete a product it should not be allowed
    Given an endpoint PRODUCT_DELETE is prepared
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 403

  Scenario: When a product does not exist on database should return success
    Given an endpoint PRODUCT_DELETE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user client with role api-sales
    When the request is sent
    Then the response status code should be 204

  Scenario: When a product is already deleted on database should return success
    Given an endpoint PRODUCT_DELETE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 204
    And assert product table has record:
      | name         | description         | price | quantity | isReserved | isSold | salesUserId                          | created  | lastUpdate | isDeleted |
      | Product name | Product description | 10.00 | 50       | false      | false  | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | NON_NULL | NULL       | true      |

  Scenario: When delete a product, should update it to deleted on database
    Given an endpoint PRODUCT_DELETE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false      |
    When the request is sent
    Then the response status code should be 204
    And assert product table has record:
      | name         | description         | price | quantity | isReserved | isSold | salesUserId                          | created  | lastUpdate | isDeleted |
      | Product name | Product description | 10.00 | 50       | false      | false  | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | NON_NULL | NULL       | true      |
