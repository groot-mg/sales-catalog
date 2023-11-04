Feature: Product controller scenarios for creating a new product

  Scenario: When a call is received to register a new product without authentication it should return unauthorised
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    When the request is sent
    Then the response status code should be 401

  Scenario: When a client user calls to register a new product it should not be allowed
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 403

  Scenario: When tries to update a non-existing product should return 404
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And a product is prepared with data:
      | name      | description      | price | quantity |
      | name-test | description-test | 10    | 40       |
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: When tries to update a deleted product should return 404
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    And a product is prepared with data:
      | name      | description      | price | quantity |
      | name-test | description-test | 10    | 40       |
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario Outline: When a sales user calls to update a product with invalid fields, should return 400 with error messages on body
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user sales with role api-sales
    And the product request body has <SCENARIO>
    When the request is sent
    Then the response status code should be 400
    And response error body should contain the field <FIELD> and message <FIELD_MESSAGE>
    And product table has no records
    Examples:
      | SCENARIO                               | FIELD       | FIELD_MESSAGE                                                |
      | NAME_MISSING                           | name        | must not be blank                                            |
      | NAME_EMPTY                             | name        | must not be blank, size must be between 5 and 100            |
      | NAME_SHORTER_THAN_MIN_LENGTH_ALLOWED   | name        | size must be between 5 and 100                               |
      | NAME_LONGER_THAN_ALLOWED               | name        | size must be between 5 and 100                               |
      | DESCRIPTION_LONGER_THAN_ALLOWED        | description | size must be between 0 and 256                               |
      | PRICE_MISSING                          | price       | must not be null                                             |
      | PRICE_NEGATIVE                         | price       | must be greater than 0                                       |
      | PRICE_ZERO                             | price       | must be greater than 0                                       |
      | PRICE_DIGITS_MORE_THAN_ALLOWED         | price       | numeric value out of bounds (<8 digits>.<2 digits> expected) |
      | PRICE_DECIMAL_DIGITS_MORE_THAN_ALLOWED | price       | numeric value out of bounds (<8 digits>.<2 digits> expected) |
      | QUANTITY_MISSING                       | quantity    | must not be null                                             |
      | QUANTITY_NEGATIVE                      | quantity    | must be greater than 0                                       |
      | QUANTITY_ZERO                          | quantity    | must be greater than 0                                       |

  Scenario: When call to update a product, should update it
    Given an endpoint PRODUCT_UPDATE is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false     |
    And a product is prepared with data:
      | name     | description     | price | quantity |
      | new-name | new-description | 15    | 60       |
    When the request is sent
    Then the response status code should be 200
    And assert product table has record:
      | name     | description     | price | quantity | isReserved | isSold | salesUserId                          | created  | lastUpdate | isDeleted |
      | new-name | new-description | 15.00 | 60       | false      | false  | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | NON_NULL | NULL       | false     |
