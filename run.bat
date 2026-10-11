@echo off
setlocal
where java >nul 2>nul
if %errorlevel% neq 0 (
    if defined JAVA_HOME (
        set "PATH=%JAVA_HOME%\bin;%PATH%"
    ) else if exist "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin\java.exe" (
        set "PATH=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.101-hotspot\bin;%PATH%"
    )
)

where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] Java runtime java was not found!
    echo Please make sure Java JRE or JDK is installed and added to PATH or JAVA_HOME.
    pause
    exit /b 1
)

if not exist build\SleepingBarber.class (
    echo Project not built yet. Running build first...
    call build.bat
    if %errorlevel% neq 0 (
        echo Build failed! Exiting.
        pause
        exit /b 1
    )
)

echo Running Sleeping Barber Simulation...
java -cp build SleepingBarber
pause