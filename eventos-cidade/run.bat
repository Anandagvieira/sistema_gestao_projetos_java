@echo off
setlocal

cd /d "%~dp0"

where mvn >nul 2>nul
if %errorlevel%==0 (
  echo Executando com Maven...
  mvn -q -DskipTests compile exec:java
  exit /b %errorlevel%
)

call compile.bat
if errorlevel 1 exit /b 1

echo.
echo Iniciando sistema...
java -Dfile.encoding=UTF-8 -Deventos.dir="%CD%" -cp out com.eventos.Main
exit /b %errorlevel%
