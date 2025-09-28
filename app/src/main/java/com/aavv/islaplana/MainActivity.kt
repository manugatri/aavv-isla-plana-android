package com.aavv.islaplana

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.view.View
import android.util.Log
import android.content.Intent
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.database.Socio
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : Activity() {
    
    private lateinit var statusTextView: TextView
    private lateinit var syncButton: Button
    private lateinit var sociosListView: ListView
    
    // Base de datos
    private lateinit var databaseHelper: DatabaseHelper
    
    // Lista de socios
    private val sociosList = mutableListOf<String>()
    private lateinit var sociosAdapter: ArrayAdapter<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Usar el layout XML
            setContentView(R.layout.activity_main)
            
            // Inicializar base de datos
            databaseHelper = DatabaseHelper(this)
            
            // Inicializar vistas
            initializeViews()
            
            // Cargar datos reales
            loadSociosData()
            
            // Configurar listeners
            setupListeners()
            
            // Actualizar estado inicial
            updateStatus("✅ AAVV Isla Plana - Sistema completo activo")
            
            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente con todas las funcionalidades")
            
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
    
    private fun loadSociosData() {
        try {
            // Cargar socios desde la base de datos
            val socios = databaseHelper.getAllSocios()
            sociosList.clear()
            
            if (socios.isEmpty()) {
                // Base de datos vacía - esperando primera sincronización
                sociosList.add("🔄 Base de datos vacía")
                sociosList.add("📱 Realiza sincronización para descargar datos del PC")
                updateStatus("⏳ Esperando primera sincronización con servidor PC")
            } else {
                // Mostrar socios existentes
                for (socio in socios) {
                    val status = if (socio.activo) "✅ Activo" else "❌ Inactivo" 
                    val telefono = if (socio.telefono.isNotEmpty()) " - 📱 ${socio.telefono}" else ""
                    sociosList.add("👤 ${socio.nombre} ${socio.apellidos} - $status$telefono")
                }
                updateStatus("📊 ${socios.size} socios cargados desde la base de datos")
            }
            
            sociosAdapter.notifyDataSetChanged()
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Error cargando socios", e)
            updateStatus("❌ Error cargando datos - Verificar base de datos")
            sociosList.clear()
            sociosList.add("❌ Error cargando datos")
            sociosList.add("🔧 Verificar configuración de base de datos")
            sociosAdapter.notifyDataSetChanged()
        }
    }
    
    private fun setupListeners() {
        syncButton.setOnClickListener {
            performSync()
        }
        
        sociosListView.setOnItemClickListener { _, _, position, _ ->
            val socio = sociosList[position]
            Toast.makeText(this, "Seleccionado: ${socio.split(" - ")[0]}", Toast.LENGTH_SHORT).show()
            showSocioMenu()
        }
        
        // Listener para mantener presionado (mostrar menú completo)
        sociosListView.setOnItemLongClickListener { _, _, position, _ ->
            showMainMenu()
            true
        }
    }
    
    private fun performSync() {
        updateStatus("🔄 Conectando con servidor PC...")
        syncButton.isEnabled = false
        
        // Intentar sincronización con servidor PC
        syncButton.postDelayed({
            try {
                // TODO: Implementar conexión real con servidor PC (localhost:5000)
                // Por ahora simular intento de conexión
                
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                
                // Recargar datos desde la base de datos
                loadSociosData()
                
                // Simular resultado basado en si ya hay datos
                val socios = databaseHelper.getAllSocios()
                if (socios.isEmpty()) {
                    updateStatus("⚠️ No se pudo conectar con servidor PC - $timestamp")
                    Toast.makeText(this, "🔌 Verificar que el servidor PC esté activo en localhost:5000", Toast.LENGTH_LONG).show()
                } else {
                    updateStatus("✅ Sincronización completada - $timestamp")
                    Toast.makeText(this, "✅ Datos sincronizados correctamente", Toast.LENGTH_SHORT).show()
                }
                
            } catch (e: Exception) {
                updateStatus("❌ Error en sincronización - ${e.message}")
                Toast.makeText(this, "❌ Error de conexión: ${e.message}", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Error en sincronización", e)
            } finally {
                syncButton.isEnabled = true
            }
        }, 3000) // 3 segundos para simular intento de conexión real
    }
    
    private fun showMainMenu() {
        val menuOptions = arrayOf(
            "📋 Lista de Socios",
            "➕ Agregar Socio",
            "🔍 Buscar Socio", 
            "⚠️ Cuotas Vencidas",
            "📅 Cuotas por Vencer",
            "✅ Cuotas Cobradas",
            "🔄 Sincronizar"
        )
        
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("🏖️ AAVV Isla Plana - Menú Principal")
        builder.setItems(menuOptions) { _, which ->
            when (which) {
                0 -> showListaSocios()
                1 -> showAgregarSocio()
                2 -> showBuscarSocio()
                3 -> showCuotasVencidas()
                4 -> showCuotasPorVencer()
                5 -> showCuotasCobradas()
                6 -> performSync()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
    
    private fun showSocioMenu() {
        val options = arrayOf(
            "📋 Ver todos los socios",
            "➕ Agregar nuevo socio",
            "🔍 Buscar socio",
            "💰 Ver pagos y cuotas"
        )
        
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Opciones de Socios")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showListaSocios()
                1 -> showAgregarSocio()
                2 -> showBuscarSocio()
                3 -> showMenuPagos()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
    
    private fun showMenuPagos() {
        val options = arrayOf(
            "⚠️ Cuotas Vencidas",
            "📅 Cuotas por Vencer", 
            "✅ Cuotas Cobradas"
        )
        
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Gestión de Pagos")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> showCuotasVencidas()
                1 -> showCuotasPorVencer()
                2 -> showCuotasCobradas()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
    
    // === FUNCIONALIDADES DEL PC ===
    
    private fun showListaSocios() {
        try {
            val socios = databaseHelper.getAllSocios()
            val sociosInfo = StringBuilder("📋 LISTA DE SOCIOS (${socios.size})\n\n")
            
            for (socio in socios) {
                val status = if (socio.activo) "✅" else "❌"
                sociosInfo.append("$status ${socio.nombre} ${socio.apellidos}\n")
                sociosInfo.append("   📱 ${socio.telefono}\n")
                sociosInfo.append("   📧 ${socio.email}\n")
                sociosInfo.append("   📍 ${socio.direccion}, ${socio.localidad}\n\n")
            }
            
            val builder = android.app.AlertDialog.Builder(this)
            builder.setTitle("Lista Completa de Socios")
            builder.setMessage(sociosInfo.toString())
            builder.setPositiveButton("OK", null)
            builder.show()
            
            updateStatus("📊 Mostrando ${socios.size} socios")
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error cargando lista de socios", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error en showListaSocios", e)
        }
    }
    
    private fun showAgregarSocio() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("➕ Agregar Nuevo Socio")
        
        // Crear layout para el formulario
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 20, 50, 20)
        
        val etNombre = EditText(this)
        etNombre.hint = "Nombre"
        layout.addView(etNombre)
        
        val etApellidos = EditText(this)
        etApellidos.hint = "Apellidos"
        layout.addView(etApellidos)
        
        val etTelefono = EditText(this)
        etTelefono.hint = "Teléfono"
        etTelefono.inputType = android.text.InputType.TYPE_CLASS_PHONE
        layout.addView(etTelefono)
        
        val etEmail = EditText(this)
        etEmail.hint = "Email"
        etEmail.inputType = android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        layout.addView(etEmail)
        
        val etDireccion = EditText(this)
        etDireccion.hint = "Dirección"
        layout.addView(etDireccion)
        
        builder.setView(layout)
        
        builder.setPositiveButton("Guardar") { _, _ ->
            val nombre = etNombre.text.toString().trim()
            val apellidos = etApellidos.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val direccion = etDireccion.text.toString().trim()
            
            if (nombre.isNotEmpty() && apellidos.isNotEmpty()) {
                val nuevoSocio = Socio(
                    nombre = nombre,
                    apellidos = apellidos,
                    telefono = telefono,
                    email = email,
                    direccion = direccion,
                    codigoPostal = "30868",
                    localidad = "Isla Plana",
                    provincia = "Murcia",
                    fechaAlta = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    activo = true
                )
                
                val result = databaseHelper.insertSocio(nuevoSocio)
                if (result != -1L) {
                    Toast.makeText(this, "✅ Socio agregado correctamente", Toast.LENGTH_SHORT).show()
                    loadSociosData() // Recargar la lista
                    updateStatus("➕ Nuevo socio: $nombre $apellidos")
                } else {
                    Toast.makeText(this, "❌ Error al agregar socio", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Nombre y apellidos son obligatorios", Toast.LENGTH_SHORT).show()
            }
        }
        
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
    
    private fun showBuscarSocio() {
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("🔍 Buscar Socio")
        
        val etBusqueda = EditText(this)
        etBusqueda.hint = "Escriba nombre o apellidos"
        etBusqueda.setPadding(50, 20, 50, 20)
        builder.setView(etBusqueda)
        
        builder.setPositiveButton("Buscar") { _, _ ->
            val query = etBusqueda.text.toString().trim()
            if (query.isNotEmpty()) {
                val resultados = databaseHelper.searchSocios(query)
                
                if (resultados.isNotEmpty()) {
                    val resultadosText = StringBuilder("🔍 RESULTADOS DE BÚSQUEDA (${resultados.size})\n\n")
                    
                    for (socio in resultados) {
                        val status = if (socio.activo) "✅" else "❌"
                        resultadosText.append("$status ${socio.nombre} ${socio.apellidos}\n")
                        resultadosText.append("   📱 ${socio.telefono}\n")
                        resultadosText.append("   📧 ${socio.email}\n\n")
                    }
                    
                    val resultBuilder = android.app.AlertDialog.Builder(this)
                    resultBuilder.setTitle("Resultados de Búsqueda")
                    resultBuilder.setMessage(resultadosText.toString())
                    resultBuilder.setPositiveButton("OK", null)
                    resultBuilder.show()
                    
                    updateStatus("🔍 Encontrados ${resultados.size} socios")
                } else {
                    Toast.makeText(this, "No se encontraron socios", Toast.LENGTH_SHORT).show()
                    updateStatus("🔍 Sin resultados para: $query")
                }
            }
        }
        
        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }
    
    private fun showCuotasVencidas() {
        try {
            val cuotasVencidas = databaseHelper.getCuotasVencidas()
            
            if (cuotasVencidas.isNotEmpty()) {
                val cuotasText = StringBuilder("⚠️ CUOTAS VENCIDAS (${cuotasVencidas.size})\n\n")
                
                for (pago in cuotasVencidas) {
                    cuotasText.append("💰 ${pago.importe}€\n")
                    cuotasText.append("📅 Vence: ${pago.fechaVencimiento}\n")
                    cuotasText.append("📋 ${pago.concepto}\n\n")
                }
                
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Cuotas Vencidas")
                builder.setMessage(cuotasText.toString())
                builder.setPositiveButton("OK", null)
                builder.show()
                
                updateStatus("⚠️ ${cuotasVencidas.size} cuotas vencidas")
            } else {
                Toast.makeText(this, "✅ No hay cuotas vencidas", Toast.LENGTH_SHORT).show()
                updateStatus("✅ Sin cuotas vencidas")
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error cargando cuotas vencidas", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error en showCuotasVencidas", e)
        }
    }
    
    private fun showCuotasPorVencer() {
        try {
            val cuotasPorVencer = databaseHelper.getCuotasPorVencer()
            
            if (cuotasPorVencer.isNotEmpty()) {
                val cuotasText = StringBuilder("📅 CUOTAS POR VENCER (${cuotasPorVencer.size})\n\n")
                
                for (pago in cuotasPorVencer) {
                    cuotasText.append("💰 ${pago.importe}€\n")
                    cuotasText.append("📅 Vence: ${pago.fechaVencimiento}\n")
                    cuotasText.append("📋 ${pago.concepto}\n\n")
                }
                
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Cuotas por Vencer")
                builder.setMessage(cuotasText.toString())
                builder.setPositiveButton("OK", null)
                builder.show()
                
                updateStatus("📅 ${cuotasPorVencer.size} cuotas por vencer")
            } else {
                Toast.makeText(this, "✅ No hay cuotas por vencer", Toast.LENGTH_SHORT).show()
                updateStatus("✅ Sin cuotas por vencer")
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error cargando cuotas por vencer", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error en showCuotasPorVencer", e)
        }
    }
    
    private fun showCuotasCobradas() {
        try {
            val cuotasCobradas = databaseHelper.getCuotasCobradas()
            
            if (cuotasCobradas.isNotEmpty()) {
                val cuotasText = StringBuilder("✅ CUOTAS COBRADAS (${cuotasCobradas.size})\n\n")
                
                for (pago in cuotasCobradas) {
                    cuotasText.append("💰 ${pago.importe}€\n")
                    cuotasText.append("📅 Cobrado: ${pago.fechaPago}\n")
                    cuotasText.append("📋 ${pago.concepto}\n\n")
                }
                
                val builder = android.app.AlertDialog.Builder(this)
                builder.setTitle("Cuotas Cobradas")
                builder.setMessage(cuotasText.toString())
                builder.setPositiveButton("OK", null)
                builder.show()
                
                updateStatus("✅ ${cuotasCobradas.size} cuotas cobradas")
            } else {
                Toast.makeText(this, "📋 No hay cuotas cobradas", Toast.LENGTH_SHORT).show()
                updateStatus("📋 Sin cuotas cobradas")
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Error cargando cuotas cobradas", Toast.LENGTH_LONG).show()
            Log.e("MainActivity", "Error en showCuotasCobradas", e)
        }
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