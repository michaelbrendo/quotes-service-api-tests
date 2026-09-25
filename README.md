# Quotes API Automation & Quality Architecture

[![API Tests](https://github.com/michaelbrendo/quotes-service-api-tests/actions/workflows/api-tests.yml/badge.svg)](https://github.com/michaelbrendo/quotes-service-api-tests/actions/workflows/api-tests.yml)
[![Allure Report](https://img.shields.io/badge/Allure%20Report-GitHub%20Pages-brightgreen)](https://michaelbrendo.github.io/quotes-service-api-tests/)

A robust, portable, and production-grade automated testing framework for the **Quotes Management Service API**, built using **Kotlin**, **REST Assured**, **JUnit 5**, **WireMock**, **JSON Schema Validator**, and **Allure Reports**.

---

## Tech Stack & Design Patterns

* **Language:** Kotlin 1.9 (JVM 17/21)
* **Test Runner:** JUnit 5 (Jupiter Engine)
* **API Automation Client:** REST Assured
* **API Mock Server:** WireMock (In-memory mock for deterministic & isolated execution)
* **Contract Testing:** RestAssured JSON Schema Validator
* **Reporting:** Allure Reports (Interactive HTML reports with BDD steps & charts)
* **Build Tool:** Gradle (Kotlin DSL)
* **Containerization:** Docker & Docker Compose
* **Task Runner:** Makefile
* **CI/CD:** GitHub Actions (Automated test execution & Allure report deployment)

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
| `make help` | Runs Available commands |
| `make test` | Runs the test suite locally via Gradle Wrapper |
| `make docker-test` | Builds the image and runs tests inside an isolated Docker container |
| `make docker-down` | Stops containers and removes temporary Docker volumes |
| `make report` | Generates and serves the Allure Report locally at `http://localhost:45879` |
| `make clean` | Cleans build artifacts, temporary reports, and Gradle cache |

### Running Locally
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
* **Push / Pull Request:** Any change pushed to the `master` branch.
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
2. **Setup Environment:** Configures JDK 17.
3. **Execute Suite:** Runs the automated test suite using `./gradlew test` / `make docker-test`.
4. **Publish Report:** Generates and deploys the Allure site to the `gh-pages` branch.

**Live Allure Report:** [View Latest Executed Report](https://michaelbrendo.github.io/quotes-service-api-tests/)