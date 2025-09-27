package com.aavv.islaplana

import android.app.Activity
import android.os.Bundle
import android.widget.TextView
import android.graphics.Color
import android.view.Gravity

class MainActivity : Activity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Crear un layout simple programáticamente
            val textView = TextView(this)
            textView.text = """
                🎉 AAVV Isla Plana 🎉
                
                ✅ La aplicación se ha iniciado correctamente
                
                📱 Versión: 1.0 Debug
                🔧 Estado: Funcional
                
                🚀 Próximas funcionalidades:
                • Gestión de socios
                • Sincronización automática
                • Base de datos local
                
                💡 La app está funcionando sin errores
            """.trimIndent()
            
            textView.textSize = 16f
            textView.setPadding(40, 60, 40, 40)
            textView.gravity = Gravity.CENTER_HORIZONTAL
            textView.setTextColor(Color.BLACK)
            textView.setBackgroundColor(Color.parseColor("#F0F8FF"))
            
            setContentView(textView)
            
        } catch (e: Exception) {
            // Fallback super simple
            val fallbackText = TextView(this)
            fallbackText.text = "AAVV Isla Plana - Funciona!\n\nError: ${e.message}"
            fallbackText.textSize = 18f
            setContentView(fallbackText)
        }
    }
}