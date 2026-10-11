#!/usr/bin/env bash
echo "Cleaning build files..."
rm -rf build
find . -name "*.class" -type f -delete 2>/dev/null || true
echo "Clean complete!"
