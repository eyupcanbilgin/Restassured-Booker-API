Feature: Booking End-to-End

  Scenario: Perform full CRUD flow on restful-booker
    # 1) Create Booking
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response

    # 2) GET booking
    When I send GET request to the stored booking
    Then response status code should be 200
    And response should contain the correct firstname "Eyup"
    And response should contain the correct lastname "Can"

    # 3) PUT booking
    Given I have an updated booking payload
    When I send PUT request to the stored booking
    Then response status code should be 200
    And response should contain the updated firstname "Ali"

    # 4) PATCH booking
    Given I have a partial update booking payload
    When I send PATCH request to the stored booking
    Then response status code should be 200
    And response should contain the updated lastname "Brown"

    # 5) DELETE booking
    When I send DELETE request to the stored booking
    Then response status code should be 201
