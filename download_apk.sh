#!/bin/bash

# Script para descargar el último APK compilado desde GitHub Actions
# Uso: ./download_apk.sh

echo "🚀 Descargando último APK compilado desde GitHub Actions..."

# Colores para output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m' # No Color

REPO="manugatri/aavv-isla-plana-android"
API_URL="https://api.github.com/repos/$REPO"

echo -e "${BLUE}📡 Consultando últimas builds en GitHub...${NC}"

# Obtener el último workflow run exitoso
LATEST_RUN=$(curl -s "$API_URL/actions/runs?status=success&per_page=1" | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)

if [ -z "$LATEST_RUN" ]; then
    echo -e "${RED}❌ No se encontraron builds exitosos recientes${NC}"
    echo "💡 Verifica que GitHub Actions se haya ejecutado correctamente"
    exit 1
fi

echo -e "${GREEN}✅ Build exitoso encontrado: #$LATEST_RUN${NC}"

# Obtener artifacts de ese run
ARTIFACTS_URL="$API_URL/actions/runs/$LATEST_RUN/artifacts"

echo -e "${BLUE}📦 Descargando información de artefactos...${NC}"

# Buscar el artifact de debug APK
ARTIFACT_INFO=$(curl -s "$ARTIFACTS_URL")
DEBUG_APK_URL=$(echo "$ARTIFACT_INFO" | grep -A5 '"name":"app-debug-apk"' | grep '"archive_download_url"' | cut -d'"' -f4)

if [ -z "$DEBUG_APK_URL" ]; then
    echo -e "${RED}❌ No se encontró el artefacto app-debug-apk${NC}"
    exit 1
fi

echo -e "${GREEN}📱 Descargando APK debug...${NC}"

# Descargar el artifact (requiere autenticación)
echo -e "${BLUE}⚠️  Para descargar automáticamente necesitas un token de GitHub${NC}"
echo -e "${BLUE}💡 Puedes descargar manualmente desde:${NC}"
echo "   https://github.com/$REPO/actions"
echo ""
echo -e "${GREEN}🎯 O usar GitHub CLI:${NC}"
echo "   gh run download $LATEST_RUN --repo $REPO"
echo ""
echo -e "${GREEN}📋 Información del build:${NC}"
echo "   - Repo: $REPO"
echo "   - Run ID: $LATEST_RUN"
echo "   - Artifact: app-debug-apk"

# Si tienes gh CLI instalado
if command -v gh &> /dev/null; then
    echo ""
    echo -e "${BLUE}🔧 GitHub CLI detectado. ¿Descargar automáticamente? (y/n)${NC}"
    read -r response
    if [[ "$response" =~ ^[Yy]$ ]]; then
        echo -e "${GREEN}⬇️  Descargando con GitHub CLI...${NC}"
        gh run download "$LATEST_RUN" --repo "$REPO" --name "app-debug-apk"
        
        if [ $? -eq 0 ]; then
            echo -e "${GREEN}✅ APK descargado exitosamente!${NC}"
            ls -la *.apk 2>/dev/null || echo "Busca el archivo app-debug.apk en el directorio actual"
        else
            echo -e "${RED}❌ Error al descargar. Verifica tu autenticación con 'gh auth login'${NC}"
        fi
    fi
else
    echo -e "${BLUE}💡 Instala GitHub CLI para descarga automática: brew install gh${NC}"
fi

echo ""
echo -e "${GREEN}🎉 Build completado! Revisa GitHub Actions para más detalles.${NC}"