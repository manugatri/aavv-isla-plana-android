#!/bin/bash

echo "🚀 INTENTO COMPLETO DE COMPILACIÓN"
echo "================================"

cd /Volumes/ProgramasSSD/aavv-isla-plana-android

# Variables
BUILD_TOOLS="/usr/local/share/android-commandlinetools/build-tools/34.0.0"
PLATFORM="/usr/local/share/android-commandlinetools/platforms/android-34"
WORK_DIR="/Volumes/ProgramasSSD/full_build"

echo "🧹 Preparando entorno..."
rm -rf "$WORK_DIR"
mkdir -p "$WORK_DIR"/{gen,classes,dex,apk}

echo "📱 Paso 1: Generar R.java..."
"$BUILD_TOOLS/aapt" package -f -m \
    -J "$WORK_DIR/gen" \
    -M app/src/main/AndroidManifest.xml \
    -S app/src/main/res \
    -I "$PLATFORM/android.jar" \
    && echo "✅ R.java generado" || { echo "❌ Error en R.java"; exit 1; }

echo "☕ Paso 2: Compilar Java..."
javac -d "$WORK_DIR/classes" \
    -classpath "$PLATFORM/android.jar:$WORK_DIR/gen" \
    "$WORK_DIR/gen/com/aavv/islaplana/R.java" \
    "MainActivity.java" \
    && echo "✅ Compilación Java OK" || { echo "❌ Error compilando Java"; exit 1; }

echo "🔄 Paso 3: Convertir .class a .dex..."
if [ -f "$BUILD_TOOLS/d8" ]; then
    "$BUILD_TOOLS/d8" --lib "$PLATFORM/android.jar" \
        --output "$WORK_DIR/dex/" \
        "$WORK_DIR/classes/com/aavv/islaplana/"*.class \
        && echo "✅ DEX creado" || { echo "❌ Error creando DEX"; exit 1; }
else
    echo "❌ No se encontró d8"
    exit 1
fi

echo "📦 Paso 4: Crear APK..."
"$BUILD_TOOLS/aapt" package -f \
    -M app/src/main/AndroidManifest.xml \
    -S app/src/main/res \
    -I "$PLATFORM/android.jar" \
    -F "$WORK_DIR/apk/app-unsigned.apk" \
    && echo "✅ APK base creado" || { echo "❌ Error creando APK"; exit 1; }

echo "📁 Paso 5: Añadir DEX al APK..."
cd "$WORK_DIR/dex"
zip -r "../apk/app-unsigned.apk" classes.dex \
    && echo "✅ DEX añadido al APK" || { echo "❌ Error añadiendo DEX"; exit 1; }

echo "🔐 Paso 6: Firmar APK..."
cd /Volumes/ProgramasSSD/aavv-isla-plana-android
cp "$WORK_DIR/apk/app-unsigned.apk" "AAVV-Isla-Plana-FIXED.apk"

echo ""
echo "🎉 RESULTADO:"
if [ -f "AAVV-Isla-Plana-FIXED.apk" ]; then
    echo "✅ APK CREADO: AAVV-Isla-Plana-FIXED.apk"
    ls -lh AAVV-Isla-Plana-FIXED.apk
else
    echo "❌ No se pudo crear el APK"
fi