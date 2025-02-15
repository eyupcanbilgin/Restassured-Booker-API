Feature: Restful-booker API Endpoints

  # 1) Create Token
  Scenario: Create a valid authentication token
    Given I have valid user credentials
    When I send POST request to create token
    Then response status code should be 200
    And I save the token from response

  # 2) Get Booking IDs
  Scenario: Retrieve list of booking IDs
    When I send GET request to /booking
    Then response status code should be 200
    And the response should contain booking IDs

  # 3) Create a New Booking
  Scenario: Create a new booking
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response

  # 4) Retrieve a Booking by ID
  Scenario: Retrieve a booking by ID
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response
    When I send GET request to the stored booking
    Then response status code should be 200
    And response should contain the correct firstname "Eyup"
    And response should contain the correct lastname "Can"

  # 5) Update a Booking (PUT)
  Scenario: Update an existing booking using PUT
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response
    And I have an updated booking payload
    When I send PUT request to the stored booking
    Then response status code should be 200
    And response should contain the updated firstname "Ali"

  # 6) Partially Update a Booking (PATCH)
  Scenario: Partially update an existing booking using PATCH
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response
    And I have a partial update booking payload
    When I send PATCH request to the stored booking
    Then response status code should be 200
    And response should contain the updated lastname "Brown"

  # 7) Delete a Booking
  Scenario: Delete an existing booking
    Given I have a new booking payload
    When I send POST request to /booking
    Then response status code should be 200
    And I save the bookingid from response
    When I send DELETE request to the stored booking
    Then response status code should be 201

  # 8) Health Check (Ping)
  Scenario: Verify API health using ping
    When I send GET request to /ping
    Then response status code should be 201
