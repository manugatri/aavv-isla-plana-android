# 🚀 Instrucciones para Construir APK - AAVV Isla Plana Android

## 📋 Opción 1: Android Studio (Recomendado)

### **Prerrequisitos:**
1. Descargar e instalar [Android Studio](https://developer.android.com/studio)
2. Durante la instalación, asegurar que se instale Android SDK

### **Pasos de construcción:**

1. **Abrir proyecto:**
   ```
   Android Studio → Open → Seleccionar carpeta aavv-isla-plana-android
   ```

2. **Sincronizar proyecto:**
   ```
   Android Studio mostrará "Sync Now" → Hacer clic
   ```

3. **Construir APK:**
   ```
   Build → Build Bundle(s)/APK(s) → Build APK(s)
   ```

4. **Localizar APK:**
   ```
   APK generado en: app/build/outputs/apk/release/app-release.apk
   ```

---

## 📋 Opción 2: Línea de Comandos (Alternativa)

### **Instalar Android SDK Command Line Tools:**

1. **Descargar SDK Command Line Tools:**
   ```bash
   # Para macOS
   wget https://dl.google.com/android/repository/commandlinetools-mac-9477386_latest.zip
   unzip commandlinetools-mac-9477386_latest.zip
   ```

2. **Configurar variables de entorno:**
   ```bash
   export ANDROID_HOME=$HOME/android-sdk
   export PATH=$PATH:$ANDROID_HOME/cmdline-tools/latest/bin
   export PATH=$PATH:$ANDROID_HOME/platform-tools
   ```

3. **Instalar componentes necesarios:**
   ```bash
   sdkmanager "platform-tools" "platforms;android-34" "build-tools;34.0.0"
   ```

4. **Construir APK:**
   ```bash
   cd /Users/manuelangelgallardotrinidad/aavv-isla-plana-android
   ./gradlew assembleRelease
   ```

---

## 📋 Opción 3: Usar Proyecto Preparado

### **El proyecto está 100% listo, solo necesita:**

1. **Android Studio instalado**
2. **Importar el proyecto**
3. **Build APK**

### **Estructura del proyecto preparada:**
```
✅ app/build.gradle - Configuración completa
✅ AndroidManifest.xml - Permisos configurados
✅ Código fuente completo en Kotlin
✅ Resources y layouts listos
✅ Sistema de sincronización implementado
✅ Base de datos SQLite configurada
```

---

## 🎯 Resultado Esperado

**APK final:** `app-release.apk` (~2-5 MB)

### **Funcionalidades incluidas:**
- ✅ Gestión completa de socios
- ✅ Sincronización automática con PC
- ✅ Base de datos SQLite local
- ✅ Interfaz nativa Android
- ✅ Servicio en background

---

## 🔧 Solución de Problemas

### **Error: "SDK not found"**
- Instalar Android Studio completo
- Configurar ANDROID_HOME

### **Error: "Build failed"**
- Verificar Java 8+ instalado
- Sync proyecto en Android Studio

### **Error: "Permission denied"**
```bash
chmod +x gradlew
```

---

## 📱 Instalación del APK

### **En dispositivo Android:**
1. Activar "Orígenes desconocidos" en Configuración
2. Transferir APK al dispositivo
3. Abrir APK y seguir instalación

### **Vía ADB:**
```bash
adb install app-release.apk
```

---

## ✨ ¡Proyecto Listo!

El proyecto Android está **completamente implementado** y listo para construir. Solo necesita Android Studio para generar el APK final.

**📞 Soporte:** Ver documentación completa en [README.md](README.md)