Feature: Hello World controller test security configurations

  # hello-world-public
  Scenario: When calling the hello-world-public without token should get success response
    Given an endpoint HELLO_WORLD_PUBLIC is prepared
    When the request is sent
    Then the response status code should be 200
    And string response body should be "Hello"

  # hello-world
  Scenario: When calling the hello-world without token should get unauthorized
    Given an endpoint HELLO_WORLD is prepared
    When the request is sent
    Then the response status code should be 401

  Scenario: When calling the hello-world with an unauthorized token should get 403
    Given an endpoint HELLO_WORLD is prepared
    And use a JWT token for user random with role api-random
    When the request is sent
    Then the response status code should be 403

  Scenario: When calling the hello-world with an client token should get success
    Given an endpoint HELLO_WORLD is prepared
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 200
    And string response body should be "Hello client!"

  Scenario: When calling the hello-world with an client token should get success
    Given an endpoint HELLO_WORLD is prepared
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 200
    And string response body should be "Hello sales!"

  # hello-world-client
  Scenario: When calling the hello-world-client without token should get unauthorized
    Given an endpoint HELLO_WORLD_CLIENT is prepared
    When the request is sent
    Then the response status code should be 401

  Scenario: When calling the hello-world-client with an unauthorized token should get 403
    Given an endpoint HELLO_WORLD_CLIENT is prepared
    And use a JWT token for user random with role api-random
    When the request is sent
    Then the response status code should be 403

  Scenario: When calling the hello-world-client with an client token should get success
    Given an endpoint HELLO_WORLD_CLIENT is prepared
    And use a JWT token for user client with role api-client
    When the request is sent
    Then the response status code should be 200
    And string response body should be "Hello client!"

  # hello-world-sales
  Scenario: When calling the hello-world-sales without token should get unauthorized
    Given an endpoint HELLO_WORLD_SALES is prepared
    When the request is sent
    Then the response status code should be 401

  Scenario: When calling the hello-world-sales with an unauthorized token should get 403
    Given an endpoint HELLO_WORLD_SALES is prepared
    And use a JWT token for user random with role api-random
    When the request is sent
    Then the response status code should be 403

  Scenario: When calling the hello-world-sales with an client token should get success
    Given an endpoint HELLO_WORLD_SALES is prepared
    And use a JWT token for user sales with role api-sales
    When the request is sent
    Then the response status code should be 200
    And string response body should be "Hello sales!"
