package com.aavv.islaplanapackage com.aavv.islaplana



import android.app.Activityimport android.app.Activity

import android.app.AlertDialogimport android.os.Bundle

import android.content.Intentimport android.widget.*

import android.os.Bundleimport android.view.View

import android.widget.*import android.util.Log

import android.view.Viewimport java.text.SimpleDateFormat

import android.util.Logimport java.util.*

import com.aavv.islaplana.database.DatabaseHelper

import com.aavv.islaplana.sync.SyncManagerclass MainActivity : Activity() {

import java.text.SimpleDateFormat    

import java.util.*    private lateinit var statusTextView: TextView

    private lateinit var syncButton: Button

class MainActivity : Activity() {    private lateinit var sociosListView: ListView

        

    private lateinit var dbHelper: DatabaseHelper    // Lista de socios de ejemplo

    private lateinit var syncManager: SyncManager    private val sociosList = mutableListOf<String>()

    private lateinit var statusTextView: TextView    private lateinit var sociosAdapter: ArrayAdapter<String>

    private lateinit var syncButton: Button    

    private lateinit var menuLayout: LinearLayout    override fun onCreate(savedInstanceState: Bundle?) {

            super.onCreate(savedInstanceState)

    override fun onCreate(savedInstanceState: Bundle?) {        

        super.onCreate(savedInstanceState)        try {

                    // Usar el layout XML

        try {            setContentView(R.layout.activity_main)

            // Inicializar componentes de base de datos            

            dbHelper = DatabaseHelper(this)            // Inicializar vistas

            syncManager = SyncManager(this)            initializeViews()

                        

            // Crear interfaz            // Configurar datos de ejemplo

            createMainInterface()            setupSampleData()

                        

            // Configurar listeners            // Configurar listeners

            setupListeners()            setupListeners()

                        

            // Actualizar estado inicial            // Actualizar estado inicial

            updateStatus("✅ AAVV Isla Plana iniciada correctamente")            updateStatus("✅ Aplicación iniciada correctamente")

                        

            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")

                        

        } catch (e: Exception) {        } catch (e: Exception) {

            Log.e("MainActivity", "Error en onCreate", e)            Log.e("MainActivity", "Error en onCreate", e)

            // Fallback a versión simple en caso de error            // Fallback a versión simple en caso de error

            showSimpleFallback(e)            showSimpleFallback(e)

        }        }

    }    }

        

    private fun createMainInterface() {    private fun initializeViews() {

        val mainLayout = LinearLayout(this).apply {        statusTextView = findViewById(R.id.statusTextView)

            orientation = LinearLayout.VERTICAL        syncButton = findViewById(R.id.syncButton)

            setPadding(16, 16, 16, 16)        sociosListView = findViewById(R.id.sociosListView)

        }        

                // Configurar adapter para la lista

        // Título principal        sociosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sociosList)

        val titleText = TextView(this).apply {        sociosListView.adapter = sociosAdapter

            text = "🏖️ AAVV Isla Plana"    }

            textSize = 24f    

            setPadding(0, 0, 0, 16)    private fun setupSampleData() {

        }        // Datos de ejemplo para mostrar funcionalidad

        mainLayout.addView(titleText)        sociosList.addAll(listOf(

                    "👤 María García López - Activo",

        // Estado de la aplicación            "👤 José Martínez Ruiz - Activo", 

        statusTextView = TextView(this).apply {            "� Carmen Sánchez Torres - Activo",

            text = "Iniciando..."            "👤 Francisco López García - Activo",

            textSize = 14f            "👤 Ana Rodríguez Pérez - Activo"

            setPadding(12, 12, 12, 12)        ))

            setBackgroundColor(android.graphics.Color.parseColor("#E3F2FD"))        sociosAdapter.notifyDataSetChanged()

        }    }

        mainLayout.addView(statusTextView)    

            private fun setupListeners() {

        // Botón de sincronización        syncButton.setOnClickListener {

        syncButton = Button(this).apply {            performSync()

            text = "🔄 Sincronizar con PC"        }

            setPadding(0, 16, 0, 16)        

        }        sociosListView.setOnItemClickListener { _, _, position, _ ->

        mainLayout.addView(syncButton)            val socio = sociosList[position]

                    Toast.makeText(this, "Seleccionado: ${socio.split(" - ")[0]}", Toast.LENGTH_SHORT).show()

        // Separador        }

        val separator = View(this).apply {    }

            layoutParams = LinearLayout.LayoutParams(    

                LinearLayout.LayoutParams.MATCH_PARENT,     private fun performSync() {

                2        updateStatus("🔄 Sincronizando datos...")

            )        syncButton.isEnabled = false

            setBackgroundColor(android.graphics.Color.GRAY)        

        }        // Simular proceso de sincronización

        mainLayout.addView(separator)        syncButton.postDelayed({

                    try {

        // Menú de opciones                // Aquí iría la lógica real de sincronización

        val menuTitle = TextView(this).apply {                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            text = "📋 Menú Principal"                updateStatus("✅ Sincronización completada - $timestamp")

            textSize = 18f                

            setPadding(0, 24, 0, 16)                // Agregar un socio nuevo como ejemplo

        }                val newSocio = "👤 Nuevo Socio ${sociosList.size + 1} - Activo"

        mainLayout.addView(menuTitle)                sociosList.add(0, newSocio)

                        sociosAdapter.notifyDataSetChanged()

        menuLayout = LinearLayout(this).apply {                

            orientation = LinearLayout.VERTICAL                Toast.makeText(this, "Sincronización exitosa", Toast.LENGTH_SHORT).show()

        }                

                    } catch (e: Exception) {

        // Crear botones del menú                updateStatus("❌ Error en sincronización")

        createMenuButtons()                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()

                        Log.e("MainActivity", "Error en sincronización", e)

        mainLayout.addView(menuLayout)            } finally {

                        syncButton.isEnabled = true

        setContentView(mainLayout)            }

    }        }, 2000) // 2 segundos de simulación

        }

    private fun createMenuButtons() {    

        val buttonConfigs = listOf(    private fun updateStatus(message: String) {

            "👥 Gestión de Socios" to { startActivity(Intent(this, SociosActivity::class.java)) },        statusTextView.text = message

            "➕ Añadir Nuevo Socio" to { startActivity(Intent(this, AddSocioActivity::class.java)) },        Log.d("MainActivity", "Status: $message")

            "💰 Ver Todos los Pagos" to {     }

                val intent = Intent(this, PagosActivity::class.java)    

                startActivity(intent)    private fun showSimpleFallback(error: Exception) {

            },        val layout = LinearLayout(this)

            "📊 Resumen de Cuotas" to { showCuotasResumen() },        layout.orientation = LinearLayout.VERTICAL

            "🔍 Buscar Socio" to { showBuscarSocio() },        layout.setPadding(32, 32, 32, 32)

            "⚙️ Configuración" to { showConfiguracion() }        

        )        val titleText = TextView(this)

                titleText.text = "🏖️ AAVV Isla Plana"

        buttonConfigs.forEach { (text, action) ->        titleText.textSize = 24f

            val button = Button(this).apply {        

                this.text = text        val errorText = TextView(this)

                setPadding(16, 16, 16, 16)        errorText.text = "⚠️ Modo básico activado\nError: ${error.message}"

                layoutParams = LinearLayout.LayoutParams(        errorText.textSize = 16f

                    LinearLayout.LayoutParams.MATCH_PARENT,        

                    LinearLayout.LayoutParams.WRAP_CONTENT        layout.addView(titleText)

                ).apply {        layout.addView(errorText)

                    setMargins(0, 8, 0, 8)        setContentView(layout)

                }    }

                setOnClickListener { action() }}
            }
            menuLayout.addView(button)
        }
    }
    
    private fun setupListeners() {
        syncButton.setOnClickListener {
            performSync()
        }
    }
    
    private fun performSync() {
        updateStatus("🔄 Iniciando sincronización...")
        syncButton.isEnabled = false
        syncButton.text = "⏳ Sincronizando..."
        
        syncManager.syncWithPC(object : SyncManager.SyncCallback {
            override fun onSyncStart() {
                runOnUiThread {
                    updateStatus("🔄 Conectando con el servidor...")
                }
            }
            
            override fun onSyncProgress(message: String) {
                runOnUiThread {
                    updateStatus("🔄 $message")
                }
            }
            
            override fun onSyncComplete(success: Boolean, message: String) {
                runOnUiThread {
                    syncButton.isEnabled = true
                    syncButton.text = "🔄 Sincronizar con PC"
                    
                    if (success) {
                        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                        updateStatus("✅ Sincronización completada - $timestamp")
                        
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("✅ Sincronización Exitosa")
                            .setMessage("$message\n\nTodos los datos se han actualizado correctamente.")
                            .setPositiveButton("Aceptar", null)
                            .show()
                    } else {
                        updateStatus("❌ Error en sincronización")
                        
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("❌ Error de Sincronización")
                            .setMessage("$message\n\nVerifica que la aplicación de PC esté ejecutándose y conectada a la red.")
                            .setPositiveButton("Reintentar") { _, _ -> performSync() }
                            .setNegativeButton("Cancelar", null)
                            .show()
                    }
                }
            }
        })
    }
    
    private fun showCuotasResumen() {
        val socios = dbHelper.getSociosActivos()
        val pagos = dbHelper.getAllPagos()
        
        val totalSocios = socios.size
        val totalPagos = pagos.size
        val totalImporte = pagos.sumOf { it.importe }
        
        val resumen = """
            📊 RESUMEN DE CUOTAS
            
            👥 Socios activos: $totalSocios
            💰 Total pagos registrados: $totalPagos
            💵 Importe total: €${String.format("%.2f", totalImporte)}
            
            📅 Información actualizada al: 
            ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("📊 Resumen de Cuotas")
            .setMessage(resumen)
            .setPositiveButton("Ver Pagos") { _, _ ->
                startActivity(Intent(this, PagosActivity::class.java))
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }
    
    private fun showBuscarSocio() {
        val searchEditText = EditText(this).apply {
            hint = "Nombre, apellidos o DNI del socio..."
            setPadding(16, 16, 16, 16)
        }
        
        AlertDialog.Builder(this)
            .setTitle("🔍 Buscar Socio")
            .setView(searchEditText)
            .setPositiveButton("Buscar") { _, _ ->
                val query = searchEditText.text.toString().trim()
                if (query.isNotEmpty()) {
                    buscarYMostrarSocios(query)
                } else {
                    Toast.makeText(this, "Introduce un término de búsqueda", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun buscarYMostrarSocios(query: String) {
        val resultados = dbHelper.buscarSocios(query)
        
        if (resultados.isEmpty()) {
            AlertDialog.Builder(this)
                .setTitle("🔍 Búsqueda")
                .setMessage("No se encontraron socios que coincidan con '$query'")
                .setPositiveButton("Aceptar", null)
                .show()
        } else {
            val opciones = resultados.map { it.getDisplayInfo() }.toTypedArray()
            
            AlertDialog.Builder(this)
                .setTitle("🔍 Resultados (${resultados.size})")
                .setItems(opciones) { _, position ->
                    val socio = resultados[position]
                    mostrarDetallesSocio(socio)
                }
                .setNegativeButton("Cerrar", null)
                .show()
        }
    }
    
    private fun mostrarDetallesSocio(socio: com.aavv.islaplana.models.Socio) {
        val details = """
            📋 ${socio.getNombreCompleto()}
            
            🆔 Número: ${socio.numeroSocio}
            📄 DNI: ${socio.dni}
            📍 ${socio.direccion}, ${socio.poblacion}
            📞 ${socio.telefono}
            📧 ${socio.email}
            ✅ Estado: ${socio.getEstado()}
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("👤 Detalles del Socio")
            .setMessage(details)
            .setPositiveButton("Ver Pagos") { _, _ ->
                val intent = Intent(this, PagosActivity::class.java)
                intent.putExtra("socio_id", socio.id)
                intent.putExtra("socio_nombre", socio.getNombreCompleto())
                startActivity(intent)
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }
    
    private fun showConfiguracion() {
        val opciones = arrayOf(
            "🔄 Sincronizar ahora",
            "📊 Ver estadísticas",
            "🗑️ Limpiar datos locales",
            "ℹ️ Información de la app"
        )
        
        AlertDialog.Builder(this)
            .setTitle("⚙️ Configuración")
            .setItems(opciones) { _, position ->
                when (position) {
                    0 -> performSync()
                    1 -> showCuotasResumen()
                    2 -> confirmarLimpiarDatos()
                    3 -> showAppInfo()
                }
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }
    
    private fun confirmarLimpiarDatos() {
        AlertDialog.Builder(this)
            .setTitle("⚠️ Confirmar")
            .setMessage("¿Estás seguro de que quieres eliminar todos los datos locales?\n\nEsta acción no se puede deshacer. Los datos se pueden recuperar con una nueva sincronización.")
            .setPositiveButton("Sí, eliminar") { _, _ ->
                dbHelper.clearAllData()
                updateStatus("🗑️ Datos locales eliminados")
                Toast.makeText(this, "Datos eliminados. Sincroniza para recuperar información.", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showAppInfo() {
        val info = """
            🏖️ AAVV Isla Plana
            
            📱 Versión: 1.0 
            🔧 Estado: Funcional
            📅 Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}
            
            ✨ Funcionalidades:
            • Gestión completa de socios
            • Control de pagos y cuotas
            • Sincronización con PC
            • Búsqueda avanzada
            • Informes y estadísticas
            
            💡 Para soporte técnico, contactar con el administrador del sistema.
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("ℹ️ Información")
            .setMessage(info)
            .setPositiveButton("Aceptar", null)
            .show()
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