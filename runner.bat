@echo off
setlocal

title prntrgz by wibowo@kanca.dev

:: Set the current directory to the directory of the batch file
cd /d "%~dp0"

:: Get the first argument (the URL)
set "URL=%1"

if "%URL%" == "" (
    echo No URL provided.
    exit /b 1
)

java -jar C:\prntrgz\prntrgz.jar "%URL%"

endlocal

:: uncomment kalo mau debug
:: pause
