Feature:
  As a hotel customer
  In order to stay in the hotel
  I would like to book a room

  Scenario: The customer can book a room on a specified date
    Given I am on the booking page
    When I enter <data> into the booking form
    And I save it
    Then the booking is complete