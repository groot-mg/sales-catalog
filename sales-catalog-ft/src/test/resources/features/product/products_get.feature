Feature: Product controller scenarios for retrieving products

  ## tests for GET /v1/products
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

  # Sorting tests
  Scenario: When sort parameters is used on the get all products, should sort the response data
    Given an endpoint PRODUCT_GET is prepared
    And query parameter sort has value name
    And product table has records:
      | id                                   | name | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | ZZZ  | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | f3ee17e1-9ce3-437d-a79a-38a83bc20e63 | AAA  | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | ebb57ce6-93b0-4b46-a24a-5f1138cd7bcd | CCC  | Product description 3 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 3 elements and 1 page
    And products response should be on the order AAA,CCC,ZZZ

  Scenario: When sort parameters with DESC is used on the get all products, should sort the response data
    Given an endpoint PRODUCT_GET is prepared
    And query parameter sort has value name,desc
    And product table has records:
      | id                                   | name | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | ZZZ  | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | f3ee17e1-9ce3-437d-a79a-38a83bc20e63 | AAA  | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | ebb57ce6-93b0-4b46-a24a-5f1138cd7bcd | CCC  | Product description 3 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 3 elements and 1 page
    And products response should be on the order ZZZ,CCC,AAA

  # Paging tests
  Scenario: When using page parameters should return the correct products according pages
    Given an endpoint PRODUCT_GET is prepared
    And query parameter sort has value name,ASC
    And query parameter size has value 1
    And query parameter page has value 0
    And product table has records:
      | id                                   | name | description           | price | quantity | salesUserId                          |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | AAA  | Product description 1 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | f3ee17e1-9ce3-437d-a79a-38a83bc20e63 | BBB  | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
      | ebb57ce6-93b0-4b46-a24a-5f1138cd7bcd | CCC  | Product description 3 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d |
    When the request is sent
    Then the response status code should be 200
    And the pageable response contains 1 element and page is 0 and 3 total elements and 3 total pages
    And products response should be on the order AAA
    And query parameter page has value 1
    When the request is sent
    And the pageable response contains 1 element and page is 1 and 3 total elements and 3 total pages
    And products response should be on the order BBB
    And query parameter page has value 2
    When the request is sent
    And the pageable response contains 1 element and page is 2 and 3 total elements and 3 total pages
    And products response should be on the order CCC

  ## Tests for GET /v1/products/{productId}
  Scenario: Given a non-sales user call get product by id and it does not exist on Database should return 404
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: Given a non-sales user call get product by id and it is delete on Database should return 404
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And product table has records:
      | id                                   | name         | description           | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: Given a non-sales user call get product by id and it is on database then should return it
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false     |
    When the request is sent
    Then the response status code should be 200
    And product response object has:
      | version | name         | description         | price | quantity |
      | v1      | Product name | Product description | 10.00 | 50       |

  Scenario: Given a sales user call get product by id and it does not exist on Database should return 404
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: Given a sales user call get product by id and it is delete on Database should return 404
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description           | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description 2 | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | true      |
    When the request is sent
    Then the response status code should be 404
    And the response error detail should be Product not found with id d6b76b42-7a45-11ee-b962-0242ac120002

  Scenario: Given a sales user call get product by id and it is on database then should return it
    Given an endpoint PRODUCT_GET is prepared with path parameter d6b76b42-7a45-11ee-b962-0242ac120002
    And use a JWT token for user id d32b6f49-d42b-4c08-b7c7-c73617bdc88d with role api-sales
    And product table has records:
      | id                                   | name         | description         | price | quantity | salesUserId                          | isDeleted |
      | d6b76b42-7a45-11ee-b962-0242ac120002 | Product name | Product description | 10.00 | 50       | d32b6f49-d42b-4c08-b7c7-c73617bdc88d | false     |
    When the request is sent
    Then the response status code should be 200
    And product response object has:
      | version | name         | description         | price | quantity | isReserved | isSold |
      | v1      | Product name | Product description | 10.00 | 50       | false      | false  |