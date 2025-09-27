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
        
        try {
            // Crear interfaz programáticamente
            TextView textView = new TextView(this);
            textView.setText("🎉 AAVV Isla Plana 🎉\n\n✅ Aplicación funcionando correctamente\n\n📱 Versión: 1.0\n🔧 Estado: Operativo\n\n💡 Interfaz básica funcional");
            textView.setTextSize(16);
            textView.setPadding(40, 60, 40, 40);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextColor(Color.BLACK);
            textView.setBackgroundColor(Color.parseColor("#F0F8FF"));
            
            setContentView(textView);
            
        } catch (Exception e) {
            // Fallback ultra-simple
            TextView fallback = new TextView(this);
            fallback.setText("AAVV Isla Plana\n\nAplicación iniciada\n\nError: " + e.getMessage());
            fallback.setTextSize(18);
            fallback.setPadding(50, 100, 50, 50);
            setContentView(fallback);
        }
    }
}