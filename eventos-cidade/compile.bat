@echo off
setlocal

rem Sempre executa a partir da pasta do projeto (portavel em qualquer maquina Windows)
cd /d "%~dp0"

set SRC=src\main\java
set OUT=out

if not exist "%OUT%" mkdir "%OUT%"

echo Compilando projeto...
javac -encoding UTF-8 -d "%OUT%" -sourcepath "%SRC%" "%SRC%\com\eventos\Main.java"

if errorlevel 1 (
  echo Falha na compilacao. Verifique se o JDK esta instalado.
  exit /b 1
)

echo Compilacao concluida com sucesso.
exit /b 0
