#!/bin/bash

# Load .env into the current shell session
if [ -f .env ]; then
  while IFS='=' read -r key value
  do
    # Skip comments and empty lines
    if [[ -n "$key" && "$key" != \#* ]]; then
      export "$key"="$value"
    fi
  done < .env
fi

# Kick off backend
./gradlew bootRun
