#!/usr/bin/env bash
set -e
echo "Starting Elastic stack..."
cd docker
docker compose up -d
cd ..
echo "Building Java project..."
mvn clean package -DskipTests
echo "Done. Run: java -jar target/siem-0.0.1-SNAPSHOT.jar"
