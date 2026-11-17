#!/bin/sh
# Carga el esquema y los datos de MongoDB una vez que el servidor responde.
# Los scripts se pasan por entrada estándar: con "use admin" mongosh solo cambia de base si el
# script llega por stdin. Es idempotente: si ya existen, el script informa y termina con éxito.
set -e

until mongosh --host mongodb --quiet --eval 'db.adminCommand({ ping: 1 }).ok' >/dev/null 2>&1; do
  echo "esperando a MongoDB..."
  sleep 1
done

echo "DDL (usuario persona_db)"
mongosh --host mongodb --quiet -u root -p root_pwd --authenticationDatabase admin \
  < /scripts/persona_ddl_mongo.js || echo "  -> no se creó el usuario (si ya existía, es normal)"

echo "DML (datos de ejemplo)"
mongosh --host mongodb --quiet -u persona_db -p persona_db --authenticationDatabase admin \
  < /scripts/persona_dml_mongo.js || echo "  -> no se cargaron datos (si ya estaban, es normal)"

echo "MongoDB listo"
