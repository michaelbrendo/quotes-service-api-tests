.PHONY: test docker-test report clean help

help:
	@echo "Available commands:"
	@echo "  make test        - Run automated tests locally via Gradle Wrapper"
	@echo "  make docker-test - Run automated tests inside Docker container"
	@echo "  make report      - Generate and serve Allure Report in browser"
	@echo "  make clean       - Clean build artifacts and previous test reports"

test:
	./gradlew test

docker-test:
	docker-compose up --build

report:
	./gradlew allureReport allureServe

clean:
	./gradlew clean