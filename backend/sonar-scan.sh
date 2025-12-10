#!/bin/bash
# FOR LINUX DEVS
echo "starting SonarQube testing and scanning..."

mvn clean verify sonar:sonar \
  -Dsonar.projectKey=fast-tasker-sonar \
  -Dsonar.projectName="Fast Tasker Backend" \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=$SONAR_TOKEN #you token here

echo ""
echo "scan complete. Check http://localhost:9000"
read -p "Press Enter to exit..."