#!/bin/bash

# Set environment variables
export DB_HOST=localhost
export DB_PORT=5432
export DB_USER=postgres # add your username here 
export DB_PASS=postgres # add your password here

# Add anything else your project needs

# Run the backend
./gradlew bootRun
