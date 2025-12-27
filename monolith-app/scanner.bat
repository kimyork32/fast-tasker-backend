:: FOR WINDOWS DEVS
@echo off
echo starting SonarQube testing and scanning...
mvn clean verify sonar:sonar ^
  -Dsonar.projectKey=kimyork32_fast-tasker_ca1abb76-2c67-4324-9a8e-b18d4f2cccf8 ^
  -Dsonar.projectName='fast-tasker' ^
  -Dsonar.host.url=http://localhost:9000 ^
  -Dsonar.token=sqp_9fdffd31facd14afe1b68f4e1bb9953db97ceb24
echo.
echo scan complete. Check http://localhost:9000
pause