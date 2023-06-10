default:
	@just --list --unsorted

run-prod:
	./gradlew bootBuildImage
	docker compose -f compose-prod.yaml up

run-dev: start-dev-dependencies
	./gradlew bootRun

start-dev-dependencies:
	docker compose up -d

stop-dev-dependencies:
	docker compose down

test-all:
	./gradlew test

test TEST:
	./gradlew test --tests "{{TEST}}"

test-report:
	xdg-open build/reports/tests/test/index.html

format:
	./gradlew spotlessApply

docs:
	xdg-open 'http://localhost:8080/swagger-ui.html'

postgres-cli:
	docker-compose exec database psql -U postgres

redis-cli:
	docker-compose exec session redis-cli

git-hooks:
	git config core.hooksPath .githooks
