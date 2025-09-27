#!/bin/bash

echo "📤 COMANDOS PARA SUBIR A GITHUB"
echo "=============================="
echo ""
echo "🔗 Una vez creado el repositorio en GitHub, ejecuta:"
echo ""
echo "# Conectar con tu repositorio (reemplaza TU_USUARIO)"
echo 'git remote add origin https://github.com/TU_USUARIO/aavv-isla-plana-android.git'
echo ""
echo "# Subir código"
echo 'git branch -M main'
echo 'git push -u origin main'
echo ""
echo "🎯 IMPORTANTE: Reemplaza TU_USUARIO por tu nombre de usuario de GitHub"
echo ""
echo "⏱️ Después de hacer push:"
echo "   1. Ve a tu repositorio en GitHub"
echo "   2. Click en pestaña 'Actions'"
echo "   3. Verás el workflow compilando automáticamente"
echo "   4. En 5-10 minutos tendrás tu APK listo"
echo ""
echo "📱 Para descargar APK:"
echo "   • Actions > Click en el build más reciente"
echo "   • Artifacts > Descargar 'aavv-isla-plana-debug-apk'"
echo "   • Descomprime ZIP > ¡Instala APK en tu móvil!"
echo ""
echo "🎉 RESULTADO: Aplicación funcionando sin errores de cierre"