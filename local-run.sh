#!/bin/bash
set -e

APP_NAME="employeedetails"
IMAGE_TAG="local"

echo "----------------------------------------"
echo " Using Zulu JDK 17"
echo "----------------------------------------"
java -version || echo "⚠️  Make sure Zulu JDK 17 is installed and active"

echo "----------------------------------------"
echo " Step 1: Maven Clean & Build"
echo "----------------------------------------"
mvn clean package -DskipTests=false

echo "----------------------------------------"
echo " Step 2: Run Unit Tests"
echo "----------------------------------------"
mvn test

echo "----------------------------------------"
echo " Step 3: OWASP Dependency Check"
echo "----------------------------------------"

mkdir -p dc-report

dependency-check \
  --project "$APP_NAME" \
  --scan . \
  --format HTML \
  --out ./dc-report \
  --failOnCVSS 7 || true

echo " OWASP report generated at: dc-report/dependency-check-report.html"

echo "----------------------------------------"
echo " Step 4: Build Docker Image"
echo "----------------------------------------"
docker build -t ${APP_NAME}:${IMAGE_TAG} .

echo "----------------------------------------"
echo " Step 5: Trivy Image Scan"
echo "----------------------------------------"
trivy image --severity HIGH,CRITICAL ${APP_NAME}:${IMAGE_TAG} || true

echo "----------------------------------------"
echo " Step 6: Run Docker Container"
echo "----------------------------------------"
docker run -d -p 8443:8443 --name ${APP_NAME}_container ${APP_NAME}:${IMAGE_TAG}

echo "----------------------------------------"
echo " Application is running at https://localhost:8443"
echo "----------------------------------------"