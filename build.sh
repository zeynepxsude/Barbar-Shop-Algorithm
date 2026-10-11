#!/usr/bin/env bash
set -e
echo "Building Sleeping Barber Project..."
mkdir -p build
javac -d build src/*.java
echo "Build successful! Run with: ./run.sh"
