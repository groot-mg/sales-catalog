Feature: Checking Service Discovery actuator endpoints return expected outputs

  Scenario: When application is healthy, return 200 response status code and "UP" response body on health endpoint
    Given a private endpoint PRIVATE_HEALTH_CHECK is prepared
    When the request is sent
    Then the response status code should be 200
    And the health response body of the message should have the status "UP"
    And health components should contain the status UP:
      | discoveryComposite |
      | diskSpace          |
      | ping               |
      | refreshScope       |