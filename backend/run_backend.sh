#!/bin/bash

# Set environment variables
export DB_HOST=localhost
export DB_PORT=5432
export DB_USER=mario
export DB_PASS=supersecret
export SOME_API_KEY=abc123

# Add anything else your project needs

# Run the backend
./gradlew bootRun
