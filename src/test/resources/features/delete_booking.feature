Feature:
  As a hotel customer
  In order to cancel a booking
  I would like to be able to delete it from the website

  Scenario: Booking can be deleted
    Given I have a booking
    When I click the delete button for that booking
    Then the booking is deleted