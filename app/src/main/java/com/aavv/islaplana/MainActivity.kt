package com.aavv.islaplanapackage com.aavv.islaplana



import android.app.Activityimport android.app.Activity

import android.app.AlertDialogimport android.app.AlertDialog

import android.content.Intentimport android.content.Intent

import android.os.Bundleimport android.os.Bundle

import android.widget.*import android.widget.*

import android.view.Viewimport android.view.View

import android.util.Logimport android.util.Log

import com.aavv.islaplana.database.DatabaseHelperimport com.aavv.islaplana.database.DatabaseHelper

import com.aavv.islaplana.sync.SyncManager

import java.text.SimpleDateFormatimport com.aavv.islaplana.sync.SyncManagerclass MainActivity : Activity() {

import java.util.*

import java.text.SimpleDateFormat    

class MainActivity : Activity() {

    import java.util.*    private lateinit var statusTextView: TextView

    private lateinit var dbHelper: DatabaseHelper

    private lateinit var syncManager: SyncManager    private lateinit var syncButton: Button

    private lateinit var statusTextView: TextView

    private lateinit var syncButton: Buttonclass MainActivity : Activity() {    private lateinit var sociosListView: ListView

    private lateinit var menuLayout: LinearLayout

            

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)    private lateinit var dbHelper: DatabaseHelper    // Lista de socios de ejemplo

        

        try {    private lateinit var syncManager: SyncManager    private val sociosList = mutableListOf<String>()

            // Inicializar componentes de base de datos

            dbHelper = DatabaseHelper(this)    private lateinit var statusTextView: TextView    private lateinit var sociosAdapter: ArrayAdapter<String>

            syncManager = SyncManager(this)

                private lateinit var syncButton: Button    

            // Crear interfaz

            createMainInterface()    private lateinit var menuLayout: LinearLayout    override fun onCreate(savedInstanceState: Bundle?) {

            

            // Configurar listeners            super.onCreate(savedInstanceState)

            setupListeners()

                override fun onCreate(savedInstanceState: Bundle?) {        

            // Actualizar estado inicial

            updateStatus("✅ AAVV Isla Plana iniciada correctamente")        super.onCreate(savedInstanceState)        try {

            

            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")                    // Usar el layout XML

            

        } catch (e: Exception) {        try {            setContentView(R.layout.activity_main)

            Log.e("MainActivity", "Error en onCreate", e)

            // Fallback a versión simple en caso de error            // Inicializar componentes de base de datos            

            showSimpleFallback(e)

        }            dbHelper = DatabaseHelper(this)            // Inicializar vistas

    }

                syncManager = SyncManager(this)            initializeViews()

    private fun createMainInterface() {

        val mainLayout = LinearLayout(this).apply {                        

            orientation = LinearLayout.VERTICAL

            setPadding(16, 16, 16, 16)            // Crear interfaz            // Configurar datos de ejemplo

        }

                    createMainInterface()            setupSampleData()

        // Título principal

        val titleText = TextView(this).apply {                        

            text = "🏖️ AAVV Isla Plana"

            textSize = 24f            // Configurar listeners            // Configurar listeners

            setPadding(0, 0, 0, 16)

        }            setupListeners()            setupListeners()

        mainLayout.addView(titleText)

                                

        // Estado de la aplicación

        statusTextView = TextView(this).apply {            // Actualizar estado inicial            // Actualizar estado inicial

            text = "Iniciando..."

            textSize = 14f            updateStatus("✅ AAVV Isla Plana iniciada correctamente")            updateStatus("✅ Aplicación iniciada correctamente")

            setPadding(12, 12, 12, 12)

            setBackgroundColor(android.graphics.Color.parseColor("#E3F2FD"))                        

        }

        mainLayout.addView(statusTextView)            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")            Log.d("MainActivity", "AAVV Isla Plana iniciada exitosamente")

        

        // Botón de sincronización                        

        syncButton = Button(this).apply {

            text = "🔄 Sincronizar con PC"        } catch (e: Exception) {        } catch (e: Exception) {

            setPadding(0, 16, 0, 16)

        }            Log.e("MainActivity", "Error en onCreate", e)            Log.e("MainActivity", "Error en onCreate", e)

        mainLayout.addView(syncButton)

                    // Fallback a versión simple en caso de error            // Fallback a versión simple en caso de error

        // Separador

        val separator = View(this).apply {            showSimpleFallback(e)            showSimpleFallback(e)

            layoutParams = LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.MATCH_PARENT,         }        }

                2

            )    }    }

            setBackgroundColor(android.graphics.Color.GRAY)

        }        

        mainLayout.addView(separator)

            private fun createMainInterface() {    private fun initializeViews() {

        // Menú de opciones

        val menuTitle = TextView(this).apply {        val mainLayout = LinearLayout(this).apply {        statusTextView = findViewById(R.id.statusTextView)

            text = "📋 Menú Principal"

            textSize = 18f            orientation = LinearLayout.VERTICAL        syncButton = findViewById(R.id.syncButton)

            setPadding(0, 24, 0, 16)

        }            setPadding(16, 16, 16, 16)        sociosListView = findViewById(R.id.sociosListView)

        mainLayout.addView(menuTitle)

                }        

        menuLayout = LinearLayout(this).apply {

            orientation = LinearLayout.VERTICAL                // Configurar adapter para la lista

        }

                // Título principal        sociosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, sociosList)

        // Crear botones del menú

        createMenuButtons()        val titleText = TextView(this).apply {        sociosListView.adapter = sociosAdapter

        

        mainLayout.addView(menuLayout)            text = "🏖️ AAVV Isla Plana"    }

        

        setContentView(mainLayout)            textSize = 24f    

    }

                setPadding(0, 0, 0, 16)    private fun setupSampleData() {

    private fun createMenuButtons() {

        val buttonConfigs = listOf(        }        // Datos de ejemplo para mostrar funcionalidad

            "👥 Gestión de Socios" to { startActivity(Intent(this, SociosActivity::class.java)) },

            "➕ Añadir Nuevo Socio" to { startActivity(Intent(this, AddSocioActivity::class.java)) },        mainLayout.addView(titleText)        sociosList.addAll(listOf(

            "💰 Ver Todos los Pagos" to { 

                val intent = Intent(this, PagosActivity::class.java)                    "👤 María García López - Activo",

                startActivity(intent)

            },        // Estado de la aplicación            "👤 José Martínez Ruiz - Activo", 

            "📊 Resumen de Cuotas" to { showCuotasResumen() },

            "🔍 Buscar Socio" to { showBuscarSocio() },        statusTextView = TextView(this).apply {            "� Carmen Sánchez Torres - Activo",

            "⚙️ Configuración" to { showConfiguracion() }

        )            text = "Iniciando..."            "👤 Francisco López García - Activo",

        

        buttonConfigs.forEach { (text, action) ->            textSize = 14f            "👤 Ana Rodríguez Pérez - Activo"

            val button = Button(this).apply {

                this.text = text            setPadding(12, 12, 12, 12)        ))

                setPadding(16, 16, 16, 16)

                layoutParams = LinearLayout.LayoutParams(            setBackgroundColor(android.graphics.Color.parseColor("#E3F2FD"))        sociosAdapter.notifyDataSetChanged()

                    LinearLayout.LayoutParams.MATCH_PARENT,

                    LinearLayout.LayoutParams.WRAP_CONTENT        }    }

                ).apply {

                    setMargins(0, 8, 0, 8)        mainLayout.addView(statusTextView)    

                }

                setOnClickListener { action() }            private fun setupListeners() {

            }

            menuLayout.addView(button)        // Botón de sincronización        syncButton.setOnClickListener {

        }

    }        syncButton = Button(this).apply {            performSync()

    

    private fun setupListeners() {            text = "🔄 Sincronizar con PC"        }

        syncButton.setOnClickListener {

            performSync()            setPadding(0, 16, 0, 16)        

        }

    }        }        sociosListView.setOnItemClickListener { _, _, position, _ ->

    

    private fun performSync() {        mainLayout.addView(syncButton)            val socio = sociosList[position]

        updateStatus("🔄 Iniciando sincronización...")

        syncButton.isEnabled = false                    Toast.makeText(this, "Seleccionado: ${socio.split(" - ")[0]}", Toast.LENGTH_SHORT).show()

        syncButton.text = "⏳ Sincronizando..."

                // Separador        }

        syncManager.syncWithPC(object : SyncManager.SyncCallback {

            override fun onSyncStart() {        val separator = View(this).apply {    }

                runOnUiThread {

                    updateStatus("🔄 Conectando con el servidor...")            layoutParams = LinearLayout.LayoutParams(    

                }

            }                LinearLayout.LayoutParams.MATCH_PARENT,     private fun performSync() {

            

            override fun onSyncProgress(message: String) {                2        updateStatus("🔄 Sincronizando datos...")

                runOnUiThread {

                    updateStatus("🔄 $message")            )        syncButton.isEnabled = false

                }

            }            setBackgroundColor(android.graphics.Color.GRAY)        

            

            override fun onSyncComplete(success: Boolean, message: String) {        }        // Simular proceso de sincronización

                runOnUiThread {

                    syncButton.isEnabled = true        mainLayout.addView(separator)        syncButton.postDelayed({

                    syncButton.text = "🔄 Sincronizar con PC"

                                        try {

                    if (success) {

                        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())        // Menú de opciones                // Aquí iría la lógica real de sincronización

                        updateStatus("✅ Sincronización completada - $timestamp")

                                val menuTitle = TextView(this).apply {                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

                        AlertDialog.Builder(this@MainActivity)

                            .setTitle("✅ Sincronización Exitosa")            text = "📋 Menú Principal"                updateStatus("✅ Sincronización completada - $timestamp")

                            .setMessage("$message\n\nTodos los datos se han actualizado correctamente.")

                            .setPositiveButton("Aceptar", null)            textSize = 18f                

                            .show()

                    } else {            setPadding(0, 24, 0, 16)                // Agregar un socio nuevo como ejemplo

                        updateStatus("❌ Error en sincronización")

                                }                val newSocio = "👤 Nuevo Socio ${sociosList.size + 1} - Activo"

                        AlertDialog.Builder(this@MainActivity)

                            .setTitle("❌ Error de Sincronización")        mainLayout.addView(menuTitle)                sociosList.add(0, newSocio)

                            .setMessage("$message\n\nVerifica que la aplicación de PC esté ejecutándose y conectada a la red.")

                            .setPositiveButton("Reintentar") { _, _ -> performSync() }                        sociosAdapter.notifyDataSetChanged()

                            .setNegativeButton("Cancelar", null)

                            .show()        menuLayout = LinearLayout(this).apply {                

                    }

                }            orientation = LinearLayout.VERTICAL                Toast.makeText(this, "Sincronización exitosa", Toast.LENGTH_SHORT).show()

            }

        })        }                

    }

                        } catch (e: Exception) {

    private fun showCuotasResumen() {

        val socios = dbHelper.getSociosActivos()        // Crear botones del menú                updateStatus("❌ Error en sincronización")

        val pagos = dbHelper.getAllPagos()

                createMenuButtons()                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()

        val totalSocios = socios.size

        val totalPagos = pagos.size                        Log.e("MainActivity", "Error en sincronización", e)

        val totalImporte = pagos.sumOf { it.importe }

                mainLayout.addView(menuLayout)            } finally {

        val resumen = """

            📊 RESUMEN DE CUOTAS                        syncButton.isEnabled = true

            

            👥 Socios activos: $totalSocios        setContentView(mainLayout)            }

            💰 Total pagos registrados: $totalPagos

            💵 Importe total: €${String.format("%.2f", totalImporte)}    }        }, 2000) // 2 segundos de simulación

            

            📅 Información actualizada al:         }

            ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}

        """.trimIndent()    private fun createMenuButtons() {    

        

        AlertDialog.Builder(this)        val buttonConfigs = listOf(    private fun updateStatus(message: String) {

            .setTitle("📊 Resumen de Cuotas")

            .setMessage(resumen)            "👥 Gestión de Socios" to { startActivity(Intent(this, SociosActivity::class.java)) },        statusTextView.text = message

            .setPositiveButton("Ver Pagos") { _, _ ->

                startActivity(Intent(this, PagosActivity::class.java))            "➕ Añadir Nuevo Socio" to { startActivity(Intent(this, AddSocioActivity::class.java)) },        Log.d("MainActivity", "Status: $message")

            }

            .setNegativeButton("Cerrar", null)            "💰 Ver Todos los Pagos" to {     }

            .show()

    }                val intent = Intent(this, PagosActivity::class.java)    

    

    private fun showBuscarSocio() {                startActivity(intent)    private fun showSimpleFallback(error: Exception) {

        val searchEditText = EditText(this).apply {

            hint = "Nombre, apellidos o DNI del socio..."            },        val layout = LinearLayout(this)

            setPadding(16, 16, 16, 16)

        }            "📊 Resumen de Cuotas" to { showCuotasResumen() },        layout.orientation = LinearLayout.VERTICAL

        

        AlertDialog.Builder(this)            "🔍 Buscar Socio" to { showBuscarSocio() },        layout.setPadding(32, 32, 32, 32)

            .setTitle("🔍 Buscar Socio")

            .setView(searchEditText)            "⚙️ Configuración" to { showConfiguracion() }        

            .setPositiveButton("Buscar") { _, _ ->

                val query = searchEditText.text.toString().trim()        )        val titleText = TextView(this)

                if (query.isNotEmpty()) {

                    buscarYMostrarSocios(query)                titleText.text = "🏖️ AAVV Isla Plana"

                } else {

                    Toast.makeText(this, "Introduce un término de búsqueda", Toast.LENGTH_SHORT).show()        buttonConfigs.forEach { (text, action) ->        titleText.textSize = 24f

                }

            }            val button = Button(this).apply {        

            .setNegativeButton("Cancelar", null)

            .show()                this.text = text        val errorText = TextView(this)

    }

                    setPadding(16, 16, 16, 16)        errorText.text = "⚠️ Modo básico activado\nError: ${error.message}"

    private fun buscarYMostrarSocios(query: String) {

        val resultados = dbHelper.buscarSocios(query)                layoutParams = LinearLayout.LayoutParams(        errorText.textSize = 16f

        

        if (resultados.isEmpty()) {                    LinearLayout.LayoutParams.MATCH_PARENT,        

            AlertDialog.Builder(this)

                .setTitle("🔍 Búsqueda")                    LinearLayout.LayoutParams.WRAP_CONTENT        layout.addView(titleText)

                .setMessage("No se encontraron socios que coincidan con '$query'")

                .setPositiveButton("Aceptar", null)                ).apply {        layout.addView(errorText)

                .show()

        } else {                    setMargins(0, 8, 0, 8)        setContentView(layout)

            val opciones = resultados.map { it.getDisplayInfo() }.toTypedArray()

                            }    }

            AlertDialog.Builder(this)

                .setTitle("🔍 Resultados (${resultados.size})")                setOnClickListener { action() }}

                .setItems(opciones) { _, position ->            }

                    val socio = resultados[position]            menuLayout.addView(button)

                    mostrarDetallesSocio(socio)        }

                }    }

                .setNegativeButton("Cerrar", null)    

                .show()    private fun setupListeners() {

        }        syncButton.setOnClickListener {

    }            performSync()

            }

    private fun mostrarDetallesSocio(socio: com.aavv.islaplana.models.Socio) {    }

        val details = """    

            📋 ${socio.getNombreCompleto()}    private fun performSync() {

                    updateStatus("🔄 Iniciando sincronización...")

            🆔 Número: ${socio.numeroSocio}        syncButton.isEnabled = false

            📄 DNI: ${socio.dni}        syncButton.text = "⏳ Sincronizando..."

            📍 ${socio.direccion}, ${socio.poblacion}        

            📞 ${socio.telefono}        syncManager.syncWithPC(object : SyncManager.SyncCallback {

            📧 ${socio.email}            override fun onSyncStart() {

            ✅ Estado: ${socio.getEstado()}                runOnUiThread {

        """.trimIndent()                    updateStatus("🔄 Conectando con el servidor...")

                        }

        AlertDialog.Builder(this)            }

            .setTitle("👤 Detalles del Socio")            

            .setMessage(details)            override fun onSyncProgress(message: String) {

            .setPositiveButton("Ver Pagos") { _, _ ->                runOnUiThread {

                val intent = Intent(this, PagosActivity::class.java)                    updateStatus("🔄 $message")

                intent.putExtra("socio_id", socio.id)                }

                intent.putExtra("socio_nombre", socio.getNombreCompleto())            }

                startActivity(intent)            

            }            override fun onSyncComplete(success: Boolean, message: String) {

            .setNegativeButton("Cerrar", null)                runOnUiThread {

            .show()                    syncButton.isEnabled = true

    }                    syncButton.text = "🔄 Sincronizar con PC"

                        

    private fun showConfiguracion() {                    if (success) {

        val opciones = arrayOf(                        val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())

            "🔄 Sincronizar ahora",                        updateStatus("✅ Sincronización completada - $timestamp")

            "📊 Ver estadísticas",                        

            "🗑️ Limpiar datos locales",                        AlertDialog.Builder(this@MainActivity)

            "ℹ️ Información de la app"                            .setTitle("✅ Sincronización Exitosa")

        )                            .setMessage("$message\n\nTodos los datos se han actualizado correctamente.")

                                    .setPositiveButton("Aceptar", null)

        AlertDialog.Builder(this)                            .show()

            .setTitle("⚙️ Configuración")                    } else {

            .setItems(opciones) { _, position ->                        updateStatus("❌ Error en sincronización")

                when (position) {                        

                    0 -> performSync()                        AlertDialog.Builder(this@MainActivity)

                    1 -> showCuotasResumen()                            .setTitle("❌ Error de Sincronización")

                    2 -> confirmarLimpiarDatos()                            .setMessage("$message\n\nVerifica que la aplicación de PC esté ejecutándose y conectada a la red.")

                    3 -> showAppInfo()                            .setPositiveButton("Reintentar") { _, _ -> performSync() }

                }                            .setNegativeButton("Cancelar", null)

            }                            .show()

            .setNegativeButton("Cerrar", null)                    }

            .show()                }

    }            }

            })

    private fun confirmarLimpiarDatos() {    }

        AlertDialog.Builder(this)    

            .setTitle("⚠️ Confirmar")    private fun showCuotasResumen() {

            .setMessage("¿Estás seguro de que quieres eliminar todos los datos locales?\n\nEsta acción no se puede deshacer. Los datos se pueden recuperar con una nueva sincronización.")        val socios = dbHelper.getSociosActivos()

            .setPositiveButton("Sí, eliminar") { _, _ ->        val pagos = dbHelper.getAllPagos()

                dbHelper.clearAllData()        

                updateStatus("🗑️ Datos locales eliminados")        val totalSocios = socios.size

                Toast.makeText(this, "Datos eliminados. Sincroniza para recuperar información.", Toast.LENGTH_LONG).show()        val totalPagos = pagos.size

            }        val totalImporte = pagos.sumOf { it.importe }

            .setNegativeButton("Cancelar", null)        

            .show()        val resumen = """

    }            📊 RESUMEN DE CUOTAS

                

    private fun showAppInfo() {            👥 Socios activos: $totalSocios

        val info = """            💰 Total pagos registrados: $totalPagos

            🏖️ AAVV Isla Plana            💵 Importe total: €${String.format("%.2f", totalImporte)}

                        

            📱 Versión: 1.0             📅 Información actualizada al: 

            🔧 Estado: Funcional            ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}

            📅 Fecha: ${SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())}        """.trimIndent()

                    

            ✨ Funcionalidades:        AlertDialog.Builder(this)

            • Gestión completa de socios            .setTitle("📊 Resumen de Cuotas")

            • Control de pagos y cuotas            .setMessage(resumen)

            • Sincronización con PC            .setPositiveButton("Ver Pagos") { _, _ ->

            • Búsqueda avanzada                startActivity(Intent(this, PagosActivity::class.java))

            • Informes y estadísticas            }

                        .setNegativeButton("Cerrar", null)

            💡 Para soporte técnico, contactar con el administrador del sistema.            .show()

        """.trimIndent()    }

            

        AlertDialog.Builder(this)    private fun showBuscarSocio() {

            .setTitle("ℹ️ Información")        val searchEditText = EditText(this).apply {

            .setMessage(info)            hint = "Nombre, apellidos o DNI del socio..."

            .setPositiveButton("Aceptar", null)            setPadding(16, 16, 16, 16)

            .show()        }

    }        

            AlertDialog.Builder(this)

    private fun updateStatus(message: String) {            .setTitle("🔍 Buscar Socio")

        statusTextView.text = message            .setView(searchEditText)

        Log.d("MainActivity", "Status: $message")            .setPositiveButton("Buscar") { _, _ ->

    }                val query = searchEditText.text.toString().trim()

                    if (query.isNotEmpty()) {

    private fun showSimpleFallback(error: Exception) {                    buscarYMostrarSocios(query)

        val layout = LinearLayout(this)                } else {

        layout.orientation = LinearLayout.VERTICAL                    Toast.makeText(this, "Introduce un término de búsqueda", Toast.LENGTH_SHORT).show()

        layout.setPadding(32, 32, 32, 32)                }

                    }

        val titleText = TextView(this)            .setNegativeButton("Cancelar", null)

        titleText.text = "🏖️ AAVV Isla Plana"            .show()

        titleText.textSize = 24f    }

            

        val errorText = TextView(this)    private fun buscarYMostrarSocios(query: String) {

        errorText.text = "⚠️ Modo básico activado\nError: ${error.message}"        val resultados = dbHelper.buscarSocios(query)

        errorText.textSize = 16f        

                if (resultados.isEmpty()) {

        layout.addView(titleText)            AlertDialog.Builder(this)

        layout.addView(errorText)                .setTitle("🔍 Búsqueda")

        setContentView(layout)                .setMessage("No se encontraron socios que coincidan con '$query'")

    }                .setPositiveButton("Aceptar", null)

}                .show()
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