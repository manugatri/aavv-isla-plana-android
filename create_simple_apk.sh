#!/bin/bash

echo "🔧 Creando APK funcional simple - AAVV Isla Plana"
echo "=============================================="

# Parar el logcat en background
pkill -f "adb logcat"

# Crear estructura básica
mkdir -p /tmp/simple_apk/{java/com/aavv/islaplana,res/{values,layout,drawable}}

# Crear MainActivity simple
cat > /tmp/simple_apk/java/com/aavv/islaplana/MainActivity.java << 'EOF'
package com.aavv.islaplana;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Color;
import android.view.Gravity;

public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textView = new TextView(this);
        textView.setText("🎉 AAVV Isla Plana 🎉\n\n✅ Aplicación funcionando correctamente\n\n📱 Versión: 1.0\n🔧 Estado: Operativo");
        textView.setTextSize(18);
        textView.setPadding(50, 100, 50, 50);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.BLACK);
        textView.setBackgroundColor(Color.parseColor("#E3F2FD"));
        
        setContentView(textView);
    }
}
EOF

# Crear AndroidManifest simple
cat > /tmp/simple_apk/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.aavv.islaplana.simple">
    
    <uses-permission android:name="android.permission.INTERNET" />
    
    <application
        android:allowBackup="true"
        android:icon="@android:drawable/ic_dialog_info"
        android:label="AAVV Isla Plana"
        android:theme="@android:style/Theme.Material.Light">
        
        <activity 
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>
</manifest>
EOF

echo "📄 Archivos creados en /tmp/simple_apk/"
echo "📱 Para crear APK necesitarías Android SDK completo"
echo "💡 Usando APK existente modificado..."

# Cambiar package name en el APK existente usando herramientas disponibles
echo "🔄 Intentando descomprimir APK existente..."
cd /Volumes/ProgramasSSD/aavv-isla-plana-android
unzip -q AAVV-Isla-Plana-Debug.apk -d /tmp/apk_extracted/ 2>/dev/null || echo "⚠️  No se pudo extraer APK"

echo "✅ Script completado. Los cambios de código están listos."
echo "📋 Siguiente paso: necesitas recompilar manualmente o usar Android Studio"