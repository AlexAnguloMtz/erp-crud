#!/bin/sh

# Get the environment variables
DB_USER="${POSTGRES_USER:-postgres}"
DB_NAME="${POSTGRES_DB:-postgres}"

# Check if the database is ready
echo "Checking if database $DB_NAME is ready..."
pg_isready -U "$DB_USER" -d "$DB_NAME"