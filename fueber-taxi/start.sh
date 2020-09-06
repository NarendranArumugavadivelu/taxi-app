#!/usr/bin/env bash

# Implement this function so that when executed it would build and start your service.
run_service() {
    mvn package && java -jar target/fueber-taxi.jar
}
run_service



