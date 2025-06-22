#!/bin/bash

./gradlew clean build

docker build -t emanuelrodrigues/hello-social-oauth .

docker image prune -f