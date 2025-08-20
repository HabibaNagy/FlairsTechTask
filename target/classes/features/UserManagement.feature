Feature: User Management in OrangeHRM

  Scenario: Login with valid credentials
    Given user is on login page
    When user enters valid username and password
    Then user should be redirected to the homepage
    And user clicks on Admin tab
    And user gets the number of records
    And user clicks Add button
    And user fills the required data
    And user clicks Save button
    Then the number of records should increase by 1
    When user searches for the new user
    And user deletes the new user
    Then the number of records should decrease by 1