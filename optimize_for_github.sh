#!/bin/bash

echo "🧹 LIMPIANDO ARCHIVOS PESADOS INNECESARIOS..."
echo "=============================================="

# Eliminar distribuciones de Gradle (GitHub Actions descargará la necesaria)
echo "📦 Eliminando distribuciones de Gradle pesadas..."
rm -rf gradle-6.7.1/
rm -rf gradle-8.0/
rm -f gradle-6.7.1-bin.zip
rm -f gradle.zip

# Eliminar APKs ya compilados (GitHub Actions generará nuevos)
echo "📱 Eliminando APKs precompilados..."
rm -f AAVV-Isla-Plana-Debug.apk
rm -f AAVV-Isla-Plana-Release.apk

# Limpiar archivos de build temporales si existen
echo "🗑️ Limpiando archivos temporales..."
rm -rf app/build/
rm -rf build/
rm -rf .gradle/

echo ""
echo "✅ LIMPIEZA COMPLETADA"
echo "📊 Nuevo tamaño del proyecto:"
du -sh . | cut -f1
echo ""
echo "🎯 RESULTADO: Proyecto optimizado para GitHub"
echo "   • Sin distribuciones pesadas de Gradle"
echo "   • Sin APKs precompilados"  
echo "   • GitHub Actions descargará solo lo necesario"