@echo off
setlocal

cd /d "%~dp0"

where mvn >nul 2>nul
if not %errorlevel%==0 (
  echo Maven nao encontrado. Instale o Maven ou use run.bat com apenas o JDK.
  exit /b 1
)

echo Gerando JAR portavel...
mvn -q -DskipTests package assembly:single

if errorlevel 1 exit /b 1

echo.
echo JAR gerado em: target\eventos-cidade.jar
echo Copie o JAR junto com events.data e users.data para qualquer pasta.
echo Execute: java -jar eventos-cidade.jar
exit /b 0
