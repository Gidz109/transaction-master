#!/bin/bash

set -e
set -u

function create_user_and_database() {
  local database=$1
  echo "  Creating database '$database'."
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE DATABASE $database;
	EOSQL
}

function create_schema() {
  local database=$1
  local schema=$2
  echo "  Creating schema '$schema' for database '$database'."
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" "$database" <<-EOSQL
	    CREATE SCHEMA $schema;
	EOSQL
}

function create_user() {
  local dbuser=$1
  echo "  Creating user '$dbuser'."
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
	    CREATE USER $dbuser
	    ;
	EOSQL
}

function grant_user() {
  local database=$1
  local dbuser=$2
  echo "  Granting privilges for database '$database' to user '$dbuser'."
  psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" "$database" <<-EOSQL
	    GRANT ALL PRIVILEGES ON DATABASE $database TO $dbuser;
	EOSQL
}

if [ -n "$POSTGRES_DATABASE_USER" ]; then
  echo "Multiple create users requested: $POSTGRES_DATABASE_USER"
  for user in $(echo $POSTGRES_DATABASE_USER | tr ',' ' '); do
    create_user $user
  done
fi

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo $POSTGRES_MULTIPLE_DATABASES | tr ',' ' '); do
    create_user_and_database $db
    for schema in $(echo $POSTGRES_ADD_SCHEMAS | tr ',' ' '); do
      if [ -n "$POSTGRES_ADD_SCHEMAS" ]; then
        echo "Create schema requested: $schema"
        create_schema $db $schema
      fi
    done
    if [ -n "$POSTGRES_DATABASE_USER" ]; then
      echo "Multiple grant users requested: $POSTGRES_DATABASE_USER"
      for user in $(echo $POSTGRES_DATABASE_USER | tr ',' ' '); do
        grant_user $db $user
      done
    fi
  done
  echo "Multiple databases created."
fi
