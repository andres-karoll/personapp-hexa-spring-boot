#!/usr/bin/env bash
# Levanta el stack de Docker Compose y ejecuta las pruebas de integración contra la API.
set -euo pipefail
cd "$(dirname "$0")/.."

docker compose up -d --build

echo "Esperando a que la API responda en http://localhost:3000 ..."
for _ in $(seq 1 90); do
  if curl -fs -o /dev/null http://localhost:3000/api-docs; then break; fi
  sleep 2
done
if ! curl -fs -o /dev/null http://localhost:3000/api-docs; then
  echo "La API no respondió. Últimos logs:"
  docker compose logs api | tail -50
  exit 1
fi

mvn -B -Pintegration -pl rest-input-adapter -am test
