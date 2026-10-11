@echo off
setlocal
where javac >nul 2>nul
if %errorlevel% neq 0 (
    if defined JAVA_HOME (
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\javac.exe" (
        set "PATH=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin;%PATH%"
    )
)

where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java compiler javac was not found!
    echo Please make sure Java JDK version 8 or higher is installed
    echo and added to your PATH or JAVA_HOME environment variable.
    pause
    exit /b 1
)

echo Building Sleeping Barber Project...
if not exist build mkdir build
javac -d build src\*.java
if %errorlevel% equ 0 (
    echo Build successful!
    echo Run with: run.bat
) else (
    echo Build failed!
    pause
)