#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if ! command -v mvn >/dev/null 2>&1; then
  echo "Maven nao encontrado. Instale o Maven ou use ./run.sh com apenas o JDK."
  exit 1
fi

echo "Gerando JAR portavel..."
mvn -q -DskipTests package assembly:single

echo
echo "JAR gerado em: target/eventos-cidade.jar"
echo "Copie o JAR junto com events.data e users.data para qualquer pasta."
echo "Execute: java -jar eventos-cidade.jar"
