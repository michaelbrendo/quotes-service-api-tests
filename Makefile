.PHONY: help test report clean lint lint-fix test-report docker-test docker-down

help:
	@echo "Available commands:"
	@echo "  make test        - Run tests locally via Gradle Wrapper"
	@echo "  make lint        - Run Ktlint check without modifying code"
	@echo "  make lint-fix    - Run Ktlint format to auto-fix code style issues"
	@echo "  make report      - Generate and serve Allure Report in browser"
	@echo "  make clean       - Clean build artifacts and previous test reports"
	@echo "  make docker-test - Build and run tests inside Docker container"
	@echo "  make docker-down - Stop containers and remove temporary volumes"

# local
test:
	./gradlew test

lint:
	./gradlew ktlintCheck

lint-fix:
	./gradlew ktlintFormat

report:
	./gradlew allureReport
	python3 -m http.server 45879 --directory build/reports/allure-report/allureReport

clean:
	./gradlew clean

test-report: clean lint test report

# Container Docker
docker-test:
	docker compose up --build --exit-code-from quotes-api-tests

docker-down:
	docker compose down -v