#!/usr/bin/env bash
set -euo pipefail

cd "$(dirname "$0")"

OUT="out"
SRC="src/main/java"

mkdir -p "$OUT"

echo "Compilando projeto..."
javac -encoding UTF-8 -d "$OUT" -sourcepath "$SRC" "$SRC/com/eventos/Main.java"
echo "Compilacao concluida com sucesso."
