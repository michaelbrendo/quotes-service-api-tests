# quotes-service-api-tests

# Quotes API Automation & Quality Architecture

A robust, portable, and production-grade automated testing framework for the **Quotes Management Service API**, built using **Kotlin**, **REST Assured**, **JUnit 5**, **WireMock**, **JSON Schema Validator**, and **Allure Reports**.

---

## Tech Stack & Design Patterns

* **Language:** Kotlin 1.9 (JVM 21)
* **Test Runner:** JUnit 5 (Jupiter Engine)
* **API Automation Client:** REST Assured
* **API Mock Server:** WireMock (In-memory mock for deterministic & isolated execution)
* **Contract Testing:** RestAssured JSON Schema Validator
* **Reporting:** Allure Reports (Interactive HTML reports with BDD steps & charts)
* **Build Tool:** Gradle (Kotlin DSL)
* **Containerization:** Docker & Docker Compose
* **Task Runner:** Makefile
* **CI/CD:** GitHub Actions (Automated test run & Allure report deployment on GitHub Pages)

---

## Requirements & Environment Setup

To run this project, you can choose **either** a local Java setup **or** a containerized Docker execution.

### Prerequisites (Local Run)
* **JDK 17** or higher
* **Make** (optional, for convenience commands)

### Prerequisites (Docker Run)
* **Docker** & **Docker Compose**