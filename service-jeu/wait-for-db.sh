#!/bin/sh
set -e

echo "Waiting for MySQL to be ready..."
until mysqladmin ping -h"$DB_HOST" --silent; do
    sleep 2
done

echo "MySQL is up - starting application..."
exec java -jar /app.jar
