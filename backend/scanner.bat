:: FOR WINDOWS DEVS
@echo off
echo starting SonarQube testing and scanning...
mvn clean verify sonar:sonar ^
  -Dsonar.projectKey=fast-tasker-sonar ^
  -Dsonar.projectName="Fast Tasker Backend" ^
  -Dsonar.host.url=http://localhost:9000 ^
  -Dsonar.token=%SONAR_TOKEN%
echo.
echo scan complete. Check http://localhost:9000
pause