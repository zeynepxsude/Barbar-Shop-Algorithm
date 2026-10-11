#!/usr/bin/env bash
set -e
if [ ! -f "build/SleepingBarber.class" ]; then
    echo "Project not built yet. Building first..."
    bash ./build.sh
fi
echo "Running Sleeping Barber Simulation..."
java -cp build SleepingBarber
