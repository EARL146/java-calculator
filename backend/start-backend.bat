@echo off
cd /d "%~dp0"
echo Starting Hito Backend...
echo.
mvn spring-boot:run
pause