#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

if command -v mvn >/dev/null 2>&1; then
  echo "Executando com Maven..."
  mvn -q -DskipTests compile exec:java
  exit 0
fi

bash compile.sh
echo
echo "Iniciando sistema..."
java -Dfile.encoding=UTF-8 -Deventos.dir="$(pwd)" -cp out com.eventos.Main
