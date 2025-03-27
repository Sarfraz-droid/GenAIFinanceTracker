#!/bin/sh

# Load environment variables from .env file
[ ! -f .env ] || export $(grep -v '^#' .env | xargs)

export NEON_DB_USER=${NEON_DB_USER}
export NEON_DB_PASSWORD=${NEON_DB_PASSWORD}
export SPRING_AI_OPENAI_API_KEY=${SPRING_AI_OPENAI_API_KEY}
