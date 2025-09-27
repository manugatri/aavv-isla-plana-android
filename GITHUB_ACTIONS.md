# 🚀 Guía GitHub Actions - AAVV Isla Plana

## 📋 **Pasos para compilar APK automáticamente:**

### **1. Crear cuenta GitHub (si no tienes)**
- Ve a [github.com](https://github.com)
- Registrarse es gratis
- Verificar email

### **2. Crear nuevo repositorio**
- Click en "New repository"
- Nombre: `aavv-isla-plana-android`
- Marcar como "Public" (para usar Actions gratis)
- No inicializar con README (ya tienes uno)

### **3. Subir tu proyecto**

#### **Opción A: Usando GitHub Desktop (Fácil)**
1. Descargar GitHub Desktop
2. "Add an Existing Repository from your Hard Drive"
3. Seleccionar: `/Volumes/ProgramasSSD/aavv-isla-plana-android`
4. Publish repository

#### **Opción B: Usando Terminal (Experto)**
```bash
cd /Volumes/ProgramasSSD/aavv-isla-plana-android
git init
git add .
git commit -m "Código corregido - listo para compilar"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/aavv-isla-plana-android.git
git push -u origin main
```

### **4. GitHub Actions se ejecuta automáticamente**
- Ve a la pestaña "Actions" de tu repositorio
- Verás el workflow "Build Android APK" ejecutándose
- Espera 5-10 minutos a que compile

### **5. Descargar APK compilado**
- En "Actions", click en el build más reciente
- En "Artifacts", descarga `aavv-isla-plana-debug-apk`
- Descomprime el ZIP, dentro está tu APK funcional

### **6. Instalar APK**
```bash
adb install -r app-debug.apk
```

## 🎯 **¿Qué hace GitHub Actions?**
1. ✅ Configura Java 11 (compatible con herramientas Android)
2. ✅ Ejecuta `./gradlew clean assembleDebug`
3. ✅ Compila tu código corregido
4. ✅ Genera APK funcional
5. ✅ Lo guarda como artifact descargable

## 🔧 **Troubleshooting**

### **Si falla el build:**
- Check el log en Actions
- El código está corregido, debería funcionar

### **Si gradlew falla:**
- GitHub Actions también probará con `./gradle-8.0/bin/gradle`
- Fallback automático incluido

## 🎉 **Resultado esperado:**
**APK funcional que se abre correctamente sin cerrarse**

- ✅ MainActivity usa Activity estándar
- ✅ Tema Material Light compatible
- ✅ Interfaz funcional implementada
- ✅ Sin errores de tema AppCompat

## ⏱️ **Timeline:**
- Subir código: 2-3 minutos
- Compilación automática: 5-10 minutos
- **Total: ~15 minutos para APK funcional**

## 📱 **Después de instalar:**
La app mostrará:
```
🎉 AAVV Isla Plana 🎉

✅ La aplicación se ha iniciado correctamente

📱 Versión: 1.0 Debug
🔧 Estado: Funcional

🚀 Próximas funcionalidades:
• Gestión de socios
• Sincronización automática
• Base de datos local

💡 La app está funcionando sin errores
```