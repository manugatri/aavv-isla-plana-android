#!/bin/bash

echo "💾 Compilación ligera - Sin instalaciones adicionales"
echo "=================================================="

cd /Volumes/ProgramasSSD/aavv-isla-plana-android

echo "🔍 Usando herramientas Android ya instaladas..."
BUILD_TOOLS="/usr/local/share/android-commandlinetools/build-tools/34.0.0"
PLATFORM="/usr/local/share/android-commandlinetools/platforms/android-34"

if [ ! -f "$PLATFORM/android.jar" ]; then
    echo "❌ Android SDK no encontrado completo"
    echo "🔧 Intentando compilación manual básica..."
    
    # Crear estructura mínima en disco externo
    WORK_DIR="/Volumes/ProgramasSSD/aavv_temp_build"
    mkdir -p "$WORK_DIR"/{src,res,bin}
    
    # Copiar fuentes
    cp -r app/src/main/java/* "$WORK_DIR/src/" 2>/dev/null || echo "Copiando fuentes..."
    cp -r app/src/main/res/* "$WORK_DIR/res/" 2>/dev/null || echo "Copiando recursos..."
    cp app/src/main/AndroidManifest.xml "$WORK_DIR/"
    
    echo "📁 Estructura creada en: $WORK_DIR"
    echo "📱 Para compilar necesitarías:"
    echo "   1. Compilar .java/.kt → .class"
    echo "   2. Convertir .class → .dex"
    echo "   3. Empaquetar en APK"
    echo "   4. Firmar APK"
    
else
    echo "✅ Android SDK encontrado"
    echo "🔨 Intentando compilación directa..."
    
    # Usar el build tools existente
    "$BUILD_TOOLS/aapt" package -f -m \
        -J /tmp/gen \
        -M app/src/main/AndroidManifest.xml \
        -S app/src/main/res \
        -I "$PLATFORM/android.jar" \
        && echo "✅ Recursos procesados" \
        || echo "❌ Error procesando recursos"
fi

echo ""
echo "💡 ALTERNATIVA PRÁCTICA:"
echo "   🌐 Usar servicio online de compilación"
echo "   📤 Subir proyecto a GitHub"
echo "   🤖 Usar GitHub Actions (gratis)"
echo "   📥 Descargar APK compilado"
echo ""
echo "🎯 O usar el APK existente modificando el error específico"