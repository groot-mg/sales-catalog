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

  Scenario: When application is running, display metric content
    Given a private endpoint PRIVATE_METRICS is prepared
    When the request is sent
    Then the response status code should be 200
    And the body of the message contains "jvm_buffer_count_buffers"

  Scenario: Return correct app information when calling private/info
    Given a private endpoint PRIVATE_INFO is prepared
    When the request is sent
    Then the response status code should be 200
    And it should return build information containing the following keys and values:
      | artifact | gateway      |
      | name     | gateway      |
      | group    | com.generoso |
    And the response body contains:
      | git   |
      | build |
      | java  |