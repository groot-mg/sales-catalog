Feature: Checking Service Discovery registration works as expected

  Scenario: When application starts, get apps should returns an empty app list
    Given an endpoint GET_APPS is prepared
    When the request is sent
    Then the response status code should be 200
    And the app list should be empty

  @ignore
  Scenario: a new app is registered successfully
    Given an endpoint ADD_APP is prepared with path parameter APP_NAME
    And a request body is prepared for APP_NAME
    When the request is sent
    Then the response status code should be 204
    And an endpoint GET_APPS is prepared
    And the request is sent
    Then the response status code should be 200
    And the app list contains an app called APP_NAME