Feature:
  As a hotel customer
  In order to stay in the hotel
  I would like to book a room

  Scenario: The customer can book a room on a specified date
    Given I am on the booking page
    When I enter data into the booking form and save it
    | firstName | James      |
    | lastName  | Test       |
    | price     | 31337      |
    | deposit   | false      |
    | checkin   | 2018-01-01 |
    | checkout  | 2018-02-01 |
    Then the booking is complete