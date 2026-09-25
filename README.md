# Quotes API Automation & Quality Architecture

[![API Tests](https://github.com/michaelbrendo/quotes-service-api-tests/actions/workflows/api-tests.yml/badge.svg)](https://github.com/michaelbrendo/quotes-service-api-tests/actions/workflows/api-tests.yml)
[![Allure Report](https://img.shields.io/badge/Allure%20Report-GitHub%20Pages-brightgreen)](https://michaelbrendo.github.io/quotes-service-api-tests/)

A robust, portable, and production-grade automated testing framework for the **Quotes Management Service API**, built using **Kotlin**, **REST Assured**, **JUnit 5**, **WireMock**, **JSON Schema Validator**, **Ktlint**, and **Allure Reports**.

---

## Tech Stack & Design Patterns

* **Language:** Kotlin 1.9 (JVM 17/21)
* **Test Runner:** JUnit 5 (Jupiter Engine)
* **API Automation Client:** REST Assured
* **API Mock Server:** WireMock (In-memory mock for deterministic & isolated execution)
* **Contract Testing:** RestAssured JSON Schema Validator
* **Code Style & Linting:** Ktlint (Gradle Plugin)
* **Reporting:** Allure Reports (Interactive HTML reports with BDD steps & charts)
* **Build Tool:** Gradle (Kotlin DSL)
* **Containerization:** Docker & Docker Compose
* **Task Runner:** Makefile
* **CI/CD:** GitHub Actions (Automated linting, test execution & Allure report deployment)

---

## Technical Decisions & Architecture Trade-offs

### 1. Technology Stack Choice
* **Expertise & Productivity:** Kotlin with REST Assured and JUnit 5 was chosen because it represents my primary tech stack in day-to-day engineering. Leveraging familiar tools allowed me to deliver a clean, production-grade test framework efficiently while focusing heavily on scenario coverage, quality gates, and pipeline architecture.
* **OpenAPI Flexibility:** As allowed in the assignment instructions, the project was built from the ground up using an OpenAPI/Swagger.

### 2. Test Architecture & Design Decisions
* **Test Data Builder Pattern (`QuoteTestDataBuilder`):** Implemented to centralize request payload creation with sensible defaults. This keeps individual test methods concise and readable by overriding only the fields relevant to each test case.
* **Decoupled Assertions for Negative Cases:** For validation errors (`400 Bad Request`), assertions extract response fields directly via REST Assured `jsonPath` instead of binding to rigid error DTOs. This avoids over-coupling test code with brittle error response structures.
* **Deterministic Environment via WireMock:** The entire suite runs against a local WireMock server, guaranteeing fast, reliable, and reproducible test executions both locally and in CI/CD without external environment flakiness.
* **Protocol & Domain Separation:** Business calculation rules (prices, discounts) are tested at the domain level, while protocol requirements (HTTP status codes, content-type handling) are validated separately to ensure clean test boundaries.
---

## Business Rules & Requirements

The testing strategy and acceptance criteria for the Quotes Management Service are documented under:
* [`docs/Acceptance_Criteria.md`](./docs/Acceptance_Criteria.md): Detailed mapping of API endpoints, business rules, and expected status codes.

---

## Requirements & Environment Setup

You can execute this test suite either via a **Local Java Setup** or an **Isolated Docker Container**.

### Prerequisites (Local Run)
* **JDK 17** or higher
* **Make** (optional, for shortcut execution)

### Prerequisites (Docker Run)
* **Docker** & **Docker Compose**
* **WSL 2** (if running on Windows)

---

## Quick Start (Execution Guide)

The repository uses a `Makefile` to standardize commands across local and CI/CD environments.

| Target | Description |
| :--- | :--- |
| `make help` | Lists all available Makefile commands |
| `make test` | Runs the test suite locally via Gradle Wrapper |
| `make lint` | Checks Kotlin code formatting and style guidelines via Ktlint |
| `make lint-fix` | Automatically formats and fixes Kotlin code style issues |
| `make report` | Generates and serves the Allure Report locally at `http://localhost:45879` |
| `make test-report` | Cleans build, runs lint, executes tests, and opens the Allure Report |
| `make docker-test` | Builds the image and runs tests inside an isolated Docker container |
| `make docker-down` | Stops containers and removes temporary Docker volumes |
| `make clean` | Cleans build artifacts, temporary reports, and Gradle cache |

### Running Locally
```bash
# Check code style and formatting
make lint
```
```bash
# Execute tests using Gradle
make test
```
```bash

# Generate and view the interactive Allure Report
make report
```

## CI/CD Pipeline & GitHub Actions

The project uses GitHub Actions (`.github/workflows/api-tests.yml`) to automatically build, test, and publish reports.

### Triggering the Pipeline

The pipeline runs automatically on:
* **Push / Pull Request:** Any change pushed to the `master` or `develop` branch.
* **Manual Trigger (`workflow_dispatch`):** You can manually run the test suite directly from the GitHub UI under the **Actions** tab at any time without pushing new commits.

---

### How to Trigger Manually via GitHub UI

1. Go to the repository's **Actions** tab.
2. Select the **API Tests & Allure Report** workflow on the left sidebar.
3. Click the **Run workflow** dropdown menu.
4. Select the branch (e.g., `master`) and click the **Run workflow** button.

---

### Pipeline Workflow Steps

1. **Checkout Code:** Retrieves the latest code from the repository.
2. **Setup Environment & Caching:** Configures JDK 17 and sets up Gradle dependency caching.
3. **Quality Gate (Lint):** Runs `make lint` to enforce Kotlin code style before execution.
4. **Build & Execute Suite:** Compiles the project and executes automated tests via Gradle / Docker.
5. **Publish Report:** Generates and deploys the Allure site to GitHub Pages.

**Live Allure Report:** [View Latest Executed Report](https://michaelbrendo.github.io/quotes-service-api-tests/)