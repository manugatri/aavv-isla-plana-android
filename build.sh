#!/bin/bash

# Script de construcción para AAVV Isla Plana Android
# Autor: Sistema AAVV Isla Plana
# Fecha: Septiembre 2025

echo "🚀 Iniciando construcción de AAVV Isla Plana Android"
echo "================================================"

# Verificar que estamos en el directorio correcto
if [ ! -f "app/src/main/AndroidManifest.xml" ]; then
    echo "❌ Error: No se encuentra AndroidManifest.xml. Ejecute desde la raíz del proyecto."
    exit 1
fi

# Limpiar proyecto
echo "🧹 Limpiando proyecto..."
./gradlew clean

# Verificar que gradlew existe y es ejecutable
if [ ! -x "./gradlew" ]; then
    echo "❌ Error: gradlew no es ejecutable o no existe."
    exit 1
fi

# Construir APK de debug
echo "🔨 Construyendo APK de debug..."
./gradlew assembleDebug

if [ $? -eq 0 ]; then
    echo "✅ APK de debug construido exitosamente"
    echo "📍 Ubicación: app/build/outputs/apk/debug/app-debug.apk"
else
    echo "❌ Error construyendo APK de debug"
    exit 1
fi

# Construir APK de release (producción)
echo "🔨 Construyendo APK de release..."
./gradlew assembleRelease

if [ $? -eq 0 ]; then
    echo "✅ APK de release construido exitosamente"
    echo "📍 Ubicación: app/build/outputs/apk/release/app-release.apk"
    
    # Mostrar información del APK
    APK_SIZE=$(du -h app/build/outputs/apk/release/app-release.apk | cut -f1)
    echo "📊 Tamaño del APK: $APK_SIZE"
    
    # Copiar APK al directorio raíz para fácil acceso
    cp app/build/outputs/apk/release/app-release.apk ./aavv-isla-plana.apk
    echo "📋 APK copiado como: ./aavv-isla-plana.apk"
    
else
    echo "❌ Error construyendo APK de release"
    exit 1
fi

echo ""
echo "🎉 ¡Construcción completada exitosamente!"
echo ""
echo "📱 Archivos generados:"
echo "   • APK Debug:   app/build/outputs/apk/debug/app-debug.apk"
echo "   • APK Release: app/build/outputs/apk/release/app-release.apk"
echo "   • APK Final:   ./aavv-isla-plana.apk"
echo ""
echo "📋 Próximos pasos:"
echo "   1. Instalar APK en dispositivo Android"
echo "   2. Ejecutar aplicación Flask en PC (puerto 5000)"
echo "   3. Conectar dispositivo Android a la misma red WiFi"
echo "   4. La app se sincronizará automáticamente"
echo ""
echo "✨ ¡Aplicación lista para despliegue!"