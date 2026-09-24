# Analysis of Requirements & Acceptance Criteria
**QA:** Michael
**Project:** QuotesService — Create A New Quote

---

## Architectural Assumptions & Testing Approach

> **Note on Non-Functional & Protocol Scenarios:**  
> The domain UML provided focuses on the internal class structures and data models (`CreateQuoteRequest` / `CreateQuoteResponse`).[cite: 1]  
> To ensure a comprehensive evaluation for a production-ready REST API, the acceptance criteria below are split into two layers:
> 1. **Core Business Criteria:** Based directly on the provided user story and domain UML (AC1, AC2, AC3 + additional business rules).[cite: 1]
> 2. **REST Protocol, Security & Contract Criteria:** Proposed non-functional standards (HTTP Status Codes, Header enforcement, JWT/OAuth2 security, and JSON Schema contract validation) expected in modern microservice architectures.

---

## 1. PROVIDED ACCEPTANCE CRITERIA (Base Requirements)

@ProvidedCriteria @AC1
Feature: Create Quote - Provided AC 1

Scenario: Successfully create a new quote with one item for a customer
Given a customer "Acme Corp"
And one item "Product A" with quantity 2 and price 50.00
When I create a quote for that customer with that item
Then it returns the quote with the correct details, including total line price calculated as 100.00
And a confirmation message "Quote created successfully."

@ProvidedCriteria @AC2
Feature: Create Quote - Provided AC 2

Scenario: Successfully create a new quote with one item with discount for a customer
Given a customer "Acme Corp"
And one item "Product A" with quantity 2, unitary price 50.00, and percentage of discount 10%
When I create a quote for that customer with that item
Then it returns the quote with the correct details, including discount amount calculated as 10.00 and total line price calculated as 90.00
And a confirmation message "Quote created successfully."

@ProvidedCriteria @AC3
Feature: Create Quote - Provided AC 3

Scenario: Successfully create a new quote with two items for a customer
Given a customer "Acme Corp"
And item "Product A" with quantity 2 and price 50.00
And item "Product B" with quantity 1 and price 100.00
When I create a quote for that customer with item A and item B
Then it returns the quote with the correct details, including two lines and quote's total price calculated as 200.00
And a confirmation message "Quote created successfully."

---

## 2. ADDITIONAL ACCEPTANCE CRITERIA - HAPPY PATH

@HappyPath
Feature: Create Quote - Happy Path Scenarios

Scenario: Create a quote with multiple items and mixed discounts
Given a customer "Acme Corp"
And an item "Product A" with quantity 2, unitary price 100.00 and discount 10%
And an item "Product B" with quantity 1, unitary price 500.00 and discount 0%
When I request to create a quote
Then the response confirmation level should be "Success"
And the message should be "Quote created successfully."
And the quote line 1 line price should be 180.00
And the quote line 2 line price should be 500.00
And the total price of the quote should be 680.00
And the quote status should be "Active"
And the revision should be 1

Scenario: Create a quote with fractional item quantities
Given a customer "Logistics Inc"
And an item "Fuel Bulk" with quantity 12.5, unitary price 4.50 and discount 0%
When I request to create a quote
Then the response confirmation level should be "Success"
And the quote line price should be 56.25
And the total price of the quote should be 56.25

Scenario: Create a quote with 100% discount on an item
Given a customer "Promotional Client"
And an item "Sample Item" with quantity 5, unitary price 20.00 and discount 100%
When I request to create a quote
Then the response confirmation level should be "Success"
And the discount amount should be 100.00
And the line price should be 0.00
And the total price of the quote should be 0.00

---

## 3. NEGATIVE CASES

@NegativeCases
Feature: Create Quote - Validation & Negative Scenarios

Scenario: Attempt to create a quote without a customer name
Given an empty or missing customer name
And an item "Product A" with quantity 1, unitary price 100.00 and discount 0%
When I request to create a quote
Then the response confirmation level should be "Error"
And an appropriate error message regarding missing customer should be returned

Scenario: Attempt to create a quote without any items
Given a customer "Valid Customer"
And an empty list of items
When I request to create a quote
Then the response confirmation level should be "Error"
And an appropriate error message regarding missing items should be returned

Scenario: Attempt to create a quote with negative item quantity
Given a customer "Valid Customer"
And an item "Product A" with quantity -5, unitary price 50.00 and discount 0%
When I request to create a quote
Then the response confirmation level should be "Error"
And the error message should indicate that item quantity must be greater than zero

Scenario: Attempt to create a quote with negative unitary price
Given a customer "Valid Customer"
And an item "Product A" with quantity 2, unitary price -10.00 and discount 0%
When I request to create a quote
Then the response confirmation level should be "Error"
And the error message should indicate that unitary price cannot be negative

Scenario: Attempt to create a quote with discount percentage greater than 100%
Given a customer "Valid Customer"
And an item "Product A" with quantity 1, unitary price 100.00 and discount 150%
When I request to create a quote
Then the response confirmation level should be "Error"
And the error message should indicate that discount percentage must be between 0 and 100

---

## 4. CORNER, EDGE & BOUNDARY CASES

@CornerCases @EdgeCases @BoundaryCases
Feature: Create Quote - Boundary & Edge Scenarios

Scenario: Discount percentage boundary value at 0%
Given a customer "Edge Test Corp"
And an item "Product A" with quantity 10, unitary price 15.00 and discount 0%
When I request to create a quote
Then the discount amount should be 0.00
And the line price should be 150.00

Scenario: Handle extreme currency values without rounding errors
Given a customer "Enterprise Partner"
And an item "High Value Item" with quantity 3, unitary price 999999.99 and discount 12.5%
When I request to create a quote
Then the calculated values should preserve decimal accuracy to 2 decimal places

Scenario: Attempt to create a quote with invalid item name payload
Given a customer "Valid Customer"
And an item with empty string as item name, quantity 1, unitary price 10.00
When I request to create a quote
Then the response confirmation level should be "Error"

---

## 5. CONTRACT TESTING (SCHEMA VALIDATION)

@ContractTesting
Feature: Create Quote - Contract & Schema Validation (Strict & Negative)

Scenario: Validate CreateQuote Response Payload Schema Contract
Given a valid create quote request payload
When the request is processed by the service
Then the HTTP status code should be 200 OK
And the response JSON body must strictly comply with the "CreateQuoteResponse.schema.json" schema

Scenario Outline: Reject responses with invalid data types or missing required contract fields
Given a mock response payload derived from CreateQuoteResponse
When the response payload has <contract_violation>
Then the JSON Schema validator should report a contract failure
And the validation error should specify <expected_error_detail>

    Examples:
      | contract_violation                                    | expected_error_detail                             |
      | missing "totalPrice" property in quote                | missing required property 'totalPrice'            |
      | missing "confirmation" root object                    | missing required property 'confirmation'          |
      | "quantity" formatted as string "10" instead of float  | expected type float/number but got string         |
      | "status" value set to "Pending" instead of enum value | value 'Pending' is not allowed in enum             |
      | "level" value set to "Unknown" instead of enum value  | value 'Unknown' is not allowed in enum            |

Scenario: Strict Schema Validation - Reject unexpected extra properties in response
Given a mock response payload containing an unexpected extra field "internalDebugLogs"
When validated against the strict JSON Schema (additionalProperties: false)
Then the schema validator should fail indicating unauthorized additional properties

Scenario: Tolerant Reader Validation - Allow backward-compatible extra properties
Given a mock response payload containing a new optional field "createdTimestamp"
When validated against the non-strict integration JSON Schema
Then the schema validation should pass successfully

---

## 6. PROTOCOL & SECURITY VALIDATION

@ProtocolValidation
Feature: Create Quote - HTTP Protocol, Security & Status Codes

# -------------------------------------------------------------------
# HTTP Status Codes & Error Handling
# -------------------------------------------------------------------

Scenario: Return 400 Bad Request when payload fails validation
Given an invalid request payload with missing mandatory field "Customer"
When I send an HTTP POST request to "/api/quotes"
Then the HTTP status code should be 400 Bad Request
And the response body should contain the validation error details

Scenario: Return 405 Method Not Allowed when using incorrect HTTP Verb
Given a valid create quote request payload
When I send an HTTP GET request to "/api/quotes"
Then the HTTP status code should be 405 Method Not Allowed
And the "Allow" header should specify "POST"

Scenario: Return 415 Unsupported Media Type for non-JSON payloads
Given a valid request formatted as XML or Plain Text
When I send the request with header "Content-Type: application/xml"
Then the HTTP status code should be 415 Unsupported Media Type

Scenario: Return 500 Internal Server Error without leaks on unexpected server fault
Given a request condition that triggers an unhandled backend exception
When I send the HTTP POST request
Then the HTTP status code should be 500 Internal Server Error
And the response should return a generic error message
And no internal stack trace or sensitive system details should be exposed

Scenario: Return 415 Unsupported Media Type when Content-Type header is missing or incorrect
Given a valid create quote request payload
And the "Content-Type" header is set to "text/plain"
When I send the HTTP POST request to CreateQuote
Then the HTTP status code should be 415 Unsupported Media Type

Scenario: Return 406 Not Acceptable when Accept header is incompatible
Given a valid create quote request payload
And the "Accept" header is set to "application/xml"
When I send the HTTP POST request to CreateQuote
Then the HTTP status code should be 406 Not Acceptable

Scenario: Traceability via X-Correlation-ID Header
Given a valid create quote request payload
And a custom header "X-Correlation-ID: 12345-abcde-67890"
When I send the HTTP POST request
Then the response header should include "X-Correlation-ID" with value "12345-abcde-67890"

# -------------------------------------------------------------------
# Authentication & Authorization (JWT / Token Security)
# -------------------------------------------------------------------

Scenario: Return 401 Unauthorized when requesting without Bearer Token
Given a valid create quote request payload
And no "Authorization" header is provided
When I send the HTTP POST request to the protected endpoint "/api/quotes"
Then the HTTP status code should be 401 Unauthorized

Scenario: Return 401 Unauthorized when requesting with expired or invalid JWT
Given a valid create quote request payload
And an expired or malformed JWT "Authorization: Bearer invalid_token_xyz"
When I send the HTTP POST request
Then the HTTP status code should be 401 Unauthorized

Scenario: Return 403 Forbidden when user lacks required role
Given a valid JWT belonging to a user with "ReadOnly" role
When I send an HTTP POST request to create a quote
Then the HTTP status code should be 403 Forbidden
And the message should indicate insufficient permissions to perform this operation

---

## 7. NON-FUNCTIONAL CASES & IDEMPOTENCY

@NonFunctional
Feature: Create Quote - Non-Functional Scenarios

Scenario: API Response Time SLA
Given a valid create quote request payload
When the request is processed by the service
Then the HTTP status code should be 200 OK
And the total response time should be less than 500 milliseconds

Scenario: Sanitize input fields against Script Injection
Given a customer name containing script tags "<script>alert('xss')</script>"
And a valid item
When I request to create a quote
Then the service should sanitize the customer string or reject the payload with error

Scenario: Handle malformed JSON body gracefully
Given a malformed JSON payload structure
When I send the HTTP POST request to CreateQuote
Then the response HTTP status code should be 400 Bad Request
And the application should not expose raw stack traces

@Architecture @Idempotency
Feature: Create Quote - Idempotent Request Handling

Scenario: Safely retry quote creation using an Idempotency Key
Given a valid create quote request payload
And a unique header "X-Idempotency-Key: 7b9a2c3d-4e5f-6a7b-8c9d-0e1f2a3b4c5d"
When I send the HTTP POST request to create a quote
Then the response status should be 200 OK
And a quote should be created with ID "GUID_1"

    When I resend the exact same HTTP POST request with the same "X-Idempotency-Key" header
    Then the response status should still be 200 OK
    And the returned quote ID should still be "GUID_1"
    And no duplicated quote should be created in the system

---

## Technical Improvements & Architecture Questions

* **HTTP Status Codes Alignment:** The domain model returns `ConfirmationLevel` (`Success`/`Error`) inside the payload.[cite: 1] The API should align payload errors with standard HTTP Status Codes (e.g., return `400 Bad Request` instead of `200 OK` with `Error` status).
* **Header Validation & Content Negotiation:** Validate presence and enforcement of `Content-Type: application/json` and `Accept: application/json` headers, returning `415 Unsupported Media Type` or `406 Not Acceptable` when invalid.
* **Distributed Tracing & Observability:** Enforce or generate a correlation header (e.g., `X-Correlation-ID`) across requests to enable trace logging in microservices architecture.
* **Authentication & Role-Based Access Control (RBAC):** Define authentication mechanisms (OAuth2/JWT) and evaluate authorization policies for different roles (e.g., Sales Representative vs. Read-Only user).
* **Input Sanitization & Security (XSS/Injection):** Ensure input string fields (`Customer`, `Item`) are sanitized against XSS and injection attacks.[cite: 1]
* **Currency Precision & Rounding Rules:** Define rounding behavior (e.g., Bankers Rounding vs. Half-Up) for float calculations (`Quantity`, `DiscoutPercentage`) and decimal currency totals.[cite: 1]
* **Boundary Limits on Numeric Inputs:** Enforce upper limits on `Quantity` and `UnitaryPrice` to prevent arithmetic overflow or performance issues during price calculation.[cite: 1]
* **Strict vs. Tolerant Schema Validation (CI/CD):** Implement strict JSON Schema validation (`additionalProperties: false`) during build/test pipelines to catch breaking changes, while supporting tolerant reader patterns in live integrations.
* **Idempotency Strategy:** Establish idempotency mechanisms (`X-Idempotency-Key`) for quote creation requests to avoid duplicate entries caused by network retries.