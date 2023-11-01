Feature: Product controller scenarios

  Scenario: When a call is received to register a new product without authentication it should return unauthorised
    Given an endpoint PRODUCT_POST is prepared
    When the request is sent
    Then the response status code should be 401

  Scenario: When a client user calls to register a new product it should not be allowed
    Given an endpoint PRODUCT_POST is prepared
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 403

  Scenario Outline: When a sales user calls to register a new product with invalid fields, should return 400 with error messages on body
    Given an endpoint PRODUCT_POST is prepared
    And the product request body has <SCENARIO>
    And use a JWT token for user sales with role api-sales
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

  Scenario: When a sales user calls to register a new product should do it
    Given an endpoint PRODUCT_POST is prepared
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 201
    And product table has record:
      | name         | description         | price | quantity | isReserved | isSold | salesUserId | created  | lastUpdate | isDeleted |
      | Product name | Product description | 10.00 | 50       | false      | false  | NON_NULL    | NON_NULL | NULL       | false     |
    And product response object has:
      | version | name         | description         | price | quantity | isReserved | isSold |
      | v1      | Product name | Product description | 10    | 50       | false      | false  |

  Scenario Outline: When sales user calls to register a new product within validation rules, should register it successfully
    Given an endpoint PRODUCT_POST is prepared
    And the product request body has <SCENARIO>
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 201
    Examples:
      | SCENARIO                         |
      | NAME_MIN_LENGTH_ALLOWED          |
      | NAME_MAX_LENGTH_ALLOWED          |
      | DESCRIPTION_MAX_LENGTH_ALLOWED   |
      | DESCRIPTION_EMPTY                |
      | DESCRIPTION_MISSING              |
      | PRICE_MAX_DIGITS_ALLOWED         |
      | PRICE_DECIMAL_MAX_DIGITS_ALLOWED |
      | QUANTITY_POSITIVE                |