package com.aavv.islaplana

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.view.View
import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {
    
    private lateinit var statusTextView: TextView
    private lateinit var syncButton: Button
    private lateinit var sociosListView: ListView
    
    // Lista de socios de ejemplo
    private val sociosList = mutableListOf<String>()
    private lateinit var sociosAdapter: ArrayAdapter<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Usar el layout XML
            setContentView(R.layout.activity_main)
            
            // Inicializar vistas
            initializeViews()
            
            // Configurar datos de ejemplo
            setupSampleData()
            
            // Configurar listeners
            setupListeners()
            
            // Actualizar estado inicial
            updateStatus("✅ Aplicación iniciada correctamente")
            
            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate", e)
            // Fallback a versión simple en caso de error
            showSimpleFallback(e)
        }
    }
    
    private fun initializeViews() {
        statusTextView = findViewById(R.id.statusTextView)
        syncButton = findViewById(R.id.syncButton)
        sociosListView = findViewById(R.id.sociosListView)
        
        // Configurar adapter para la lista
        sociosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sociosList)
        sociosListView.adapter = sociosAdapter
    }
    
    private fun setupSampleData() {
        // Datos de ejemplo para mostrar funcionalidad
        sociosList.addAll(listOf(
            "👤 María García López - Activo",
            "👤 José Martínez Ruiz - Activo", 
            "� Carmen Sánchez Torres - Activo",
            "👤 Francisco López García - Activo",
            "👤 Ana Rodríguez Pérez - Activo"
        ))
        sociosAdapter.notifyDataSetChanged()
    }
    
    private fun setupListeners() {
        syncButton.setOnClickListener {
            performSync()
        }
        
        sociosListView.setOnItemClickListener { _, _, position, _ ->
            val socio = sociosList[position]
            Toast.makeText(this, "Seleccionado: ${socio.split(" - ")[0]}", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun performSync() {
        updateStatus("🔄 Sincronizando datos...")
        syncButton.isEnabled = false
        
        // Simular proceso de sincronización
        syncButton.postDelayed({
            try {
                // Aquí iría la lógica real de sincronización
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                updateStatus("✅ Sincronización completada - $timestamp")
                
                // Agregar un socio nuevo como ejemplo
                val newSocio = "👤 Nuevo Socio ${sociosList.size + 1} - Activo"
                sociosList.add(0, newSocio)
                sociosAdapter.notifyDataSetChanged()
                
                Toast.makeText(this, "Sincronización exitosa", Toast.LENGTH_SHORT).show()
                
            } catch (e: Exception) {
                updateStatus("❌ Error en sincronización")
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Error en sincronización", e)
            } finally {
                syncButton.isEnabled = true
            }
        }, 2000) // 2 segundos de simulación
    }
    
    private fun updateStatus(message: String) {
        statusTextView.text = message
        Log.d("MainActivity", "Status: $message")
    }
    
    private fun showSimpleFallback(error: Exception) {
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(32, 32, 32, 32)
        
        val titleText = TextView(this)
        titleText.text = "🏖️ AAVV Isla Plana"
        titleText.textSize = 24f
        
        val errorText = TextView(this)
        errorText.text = "⚠️ Modo básico activado\nError: ${error.message}"
        errorText.textSize = 16f
        
        layout.addView(titleText)
        layout.addView(errorText)
        setContentView(layout)
    }
}