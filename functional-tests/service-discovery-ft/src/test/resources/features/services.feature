Feature: Checking Service Discovery registration works as expected

  Background:
    Given initial metrics are gathered

  Scenario: When application starts, get apps should returns an empty app list
    Given an endpoint GET_APPS is prepared
    When the request is sent
    Then the response status code should be 200
    And the app list should be empty
    And metrics are gathered again
    And the application_responses_total metric for endpoint GET_APPS with status response code 200 has incremented by 1

  # it is flaky, spring-eureka takes time to return the new app after register it and the list might be empty
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
    And metrics are gathered again
    And the application_responses_total metric for endpoint ADD_APP with parameter APP_NAME and status response code 204 has incremented by 1