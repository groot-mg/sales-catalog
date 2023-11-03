Feature: Checking Service Discovery actuator endpoints return expected outputs

  Scenario: When application is healthy, return 500 response status code and "UP" response body on health endpoint
    Given a private endpoint PRIVATE_HEALTH_CHECK is prepared
    When the request is sent
    Then the response status code should be 200
    And the health response body of the message should have the status "UP"
    And health components should contain the status UP:
      | db                 |
      | discoveryComposite |
      | ping               |
      | refreshScope       |

  Scenario: When database is down, returns 503 response status
    Given a private endpoint PRIVATE_HEALTH_CHECK is prepared
    And database is down
    When the request is sent
    Then the response status code should be 503
    And the health response body of the message should have the status "DOWN"
    And health components should contain the status DOWN:
      | db                 |

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
      | artifact | sales-catalog-app |
      | name     | sales-catalog-app |
      | group    | com.generoso      |
    And the response body contains:
      | git   |
      | build |
      | java  |