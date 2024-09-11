_default:
	@just --list --unsorted

run: start-deps
	./gradlew bootRun

start-deps:
	docker compose up -d

stop-deps:
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

observe:
	xdg-open 'http://localhost:3000'

postgres-cli:
	docker-compose exec database psql -U postgres

redis-cli:
	docker-compose exec session redis-cli

git-hooks:
	git config core.hooksPath .githooks
