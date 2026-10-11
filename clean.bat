@echo off
echo Cleaning build and compiled class files...
if exist build rmdir /s /q build
del /s /q *.class 2>nul
echo Clean complete!