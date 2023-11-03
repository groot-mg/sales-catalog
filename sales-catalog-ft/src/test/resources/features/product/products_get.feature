Feature: Product controller scenarios for retrieving products

  Scenario: When call to retrieve products with an empty table should return success and empty content
    Given an endpoint PRODUCT_GET is prepared
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 0 elements and 0 pages
    And the response content is empty

  Scenario: When call to retrieve products and all are deleted should not include any product
    Given an endpoint PRODUCT_GET is prepared
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 0 elements and 0 pages
    And the response content is empty

  Scenario: When call to retrieve products should not include deleted products
    Given an endpoint PRODUCT_GET is prepared
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false     |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 1 elements and 1 pages
    And the response content contains the product id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: Returns products for public call when products exist on database
    Given an endpoint PRODUCT_GET is prepared
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 2 elements and 1 page
    And the response content contains the product id d6b76b42-7a45-11ee-b962-0242ac120002
    And the response content contains the product id 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82

  Scenario: When call to retrieve products for a sales user should return only the products owned by that user
    Given an endpoint PRODUCT_GET is prepared
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | 2a89937e-ce3d-4dab-8017-85b17e27e517 |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 1 element and 1 page
    And the response content contains the product id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: When call to retrieve products for a sales user should return include delete products
    Given an endpoint PRODUCT_GET is prepared
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false     |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 1 element and 1 page
    And the response content contains the product id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: When call to retrieve products for a sales catalog user should return empty when the user has no products
    Given an endpoint PRODUCT_GET is prepared
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name           | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name 1 | Product description 1 | 10.00 | 50       | 2a89937e-ce3d-4dab-8017-85b17e27e517 |
      | 62ffaaa0-fb7c-4ece-ad4f-349d85de1e82 | Product name 2 | Product description 2 | 10.00 | 50       | 2a89937e-ce3d-4dab-8017-85b17e27e517 |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 0 elements and 0 page
    And the response content is empty