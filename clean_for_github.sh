#!/bin/bash

echo "🧹 LIMPIANDO DATOS SENSIBLES ANTES DE GITHUB"
echo "==========================================="

cd /Volumes/ProgramasSSD/aavv-isla-plana-android

echo "🔍 Buscando archivos sensibles..."

# Eliminar bases de datos si existen
find . -name "*.db" -type f -delete && echo "✅ Archivos .db eliminados" || echo "ℹ️  No se encontraron archivos .db"
find . -name "*.sqlite" -type f -delete && echo "✅ Archivos .sqlite eliminados" || echo "ℹ️  No se encontraron archivos .sqlite"
find . -name "*.sqlite3" -type f -delete && echo "✅ Archivos .sqlite3 eliminados" || echo "ℹ️  No se encontraron archivos .sqlite3"

# Eliminar logs sensibles
find . -name "*_logs.txt" -type f -delete && echo "✅ Logs eliminados" || echo "ℹ️  No se encontraron logs"
find . -name "app_logs.txt" -type f -delete 2>/dev/null
find . -name "sync_logs.txt" -type f -delete 2>/dev/null

# Eliminar archivos de build temporales
rm -rf /tmp/gen /tmp/app_test_logs.txt /tmp/aavv_* 2>/dev/null
rm -rf build_simple full_build aavv_apk_work aavv_temp_build 2>/dev/null

# Eliminar keystores de debug (se regenerarán)
find . -name "debug.keystore" -type f -delete 2>/dev/null && echo "✅ Debug keystores eliminados"

# Limpiar directorios de build
echo "🧹 Limpiando directorios de build..."
rm -rf app/build/ 2>/dev/null
rm -rf build/ 2>/dev/null
rm -rf .gradle/ 2>/dev/null

echo ""
echo "✅ LIMPIEZA COMPLETADA"
echo ""
echo "🔒 ARCHIVOS SENSIBLES ELIMINADOS:"
echo "   • Bases de datos (.db, .sqlite)"
echo "   • Logs con datos personales"
echo "   • Keystores de debug"
echo "   • Archivos de build temporales"
echo ""
echo "📁 PROYECTO LIMPIO Y SEGURO PARA GITHUB"
echo ""
echo "🚀 SIGUIENTE PASO:"
echo "   Ejecutar: git add . && git commit -m 'Proyecto limpio - listo para GitHub'"