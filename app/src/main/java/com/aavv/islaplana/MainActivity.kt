package com.aavv.islaplana

import android.app.Activity
import android.os.Bundle
import android.widget.*
import android.view.View
import android.util.Log
import android.content.Intent
import android.content.Context
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.database.Socio
import com.aavv.islaplana.database.Pago
import java.text.SimpleDateFormat
import java.util.*
import java.net.URL
import java.net.HttpURLConnection
import java.io.BufferedReader
import java.io.InputStreamReader
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.Executors

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
            
            // Mostrar información de red para debugging
            logNetworkInfo()
            
        } catch (e: Exception) {
            Log.e("MainActivity", "Error en onCreate", e)
            // Fallback a versión simple en caso de error
            showSimpleFallback(e)
        }
    }
    
    private fun logNetworkInfo() {
        try {
            val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as android.net.ConnectivityManager
            val activeNetwork = connectivityManager.activeNetworkInfo
            
            if (activeNetwork != null && activeNetwork.isConnected) {
                Log.d("MainActivity", "🌐 Red activa: ${activeNetwork.typeName} - ${activeNetwork.subtypeName}")
                Log.d("MainActivity", "🔗 Conectado: ${activeNetwork.isConnected}")
                Log.d("MainActivity", "🌍 Internet disponible: ${activeNetwork.isAvailable}")
            } else {
                Log.w("MainActivity", "⚠️ Sin conexión de red activa")
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "Error obteniendo info de red", e)
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
                    val status = if (socio.enAlta) "✅ Activo" else "❌ Inactivo" 
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
        
        // Ejecutar sincronización en hilo separado
        val executor = Executors.newSingleThreadExecutor()
        executor.execute {
            try {
                syncWithPCServer()
            } catch (e: Exception) {
                runOnUiThread {
                    val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                    updateStatus("❌ Error en sincronización - $timestamp")
                    Toast.makeText(this, "❌ Error: ${e.message}", Toast.LENGTH_LONG).show()
                    Log.e("MainActivity", "Error en sincronización", e)
                    syncButton.isEnabled = true
                }
            }
        }
    }
    
    private fun syncWithPCServer() {
        try {
            // Lista de URLs del servidor PC para probar (diferentes configuraciones de red)
            val possibleServerUrls = listOf(
                "http://192.168.1.107:5000",  // IP actual de red local
                "http://10.0.2.2:5000",       // Emulador Android (localhost mapping)
                "http://127.0.0.1:5000",      // Localhost directo
                "http://192.168.0.107:5000",  // Posible IP alternativa
                "http://192.168.1.1:5000"     // Gateway como fallback
            )
            
            var serverUrl: String? = null
            var statusJson: String? = null
            
            Log.d("MainActivity", "🚀 INICIANDO SINCRONIZACIÓN - Probando conexiones...")
            
            runOnUiThread {
                updateStatus("🔍 Buscando servidor PC en la red...")
            }
            
            // Probar cada URL hasta encontrar una que funcione
            for (url in possibleServerUrls) {
                Log.d("MainActivity", "📡 Probando conexión con: $url")
                runOnUiThread {
                    updateStatus("🔍 Probando: $url")
                }
                
                statusJson = makeHttpRequest("$url/api/status")
                if (statusJson != null) {
                    serverUrl = url
                    Log.d("MainActivity", "✅ Conexión exitosa con: $url")
                    Log.d("MainActivity", "📡 Respuesta status: $statusJson")
                    break
                } else {
                    Log.d("MainActivity", "❌ Falló conexión con: $url")
                }
            }
            
            if (serverUrl == null || statusJson == null) {
                throw Exception("No se puede conectar al servidor PC. Verifique:\n• Servidor ejecutándose\n• Misma red WiFi\n• Puerto 5000 abierto")
            }
            
            runOnUiThread {
                updateStatus("🌐 Conectado al servidor: $serverUrl")
            }
            
            var sociosSincronizados = 0
            var pagosSincronizados = 0
            
            // Descargar socios
            Log.d("MainActivity", "👥 Descargando socios...")
            val sociosJson = makeHttpRequest("${serverUrl}/api/socios")
            Log.d("MainActivity", "👥 Respuesta socios: ${sociosJson?.take(100) ?: "NULL"}")
            if (sociosJson != null) {
                sociosSincronizados = processSociosData(sociosJson)
            } else {
                throw Exception("No se pudieron descargar los socios del servidor")
            }
            
            // Descargar pagos
            Log.d("MainActivity", "💰 Descargando pagos...")
            val pagosJson = makeHttpRequest("${serverUrl}/api/pagos")
            Log.d("MainActivity", "💰 Respuesta pagos: ${pagosJson?.take(100) ?: "NULL"}")
            if (pagosJson != null) {
                pagosSincronizados = processPagosData(pagosJson)
            } else {
                throw Exception("No se pudieron descargar los pagos del servidor")
            }
            
            runOnUiThread {
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val mensaje = "✅ Sincronizados: $sociosSincronizados socios, $pagosSincronizados pagos"
                updateStatus("$mensaje - $timestamp")
                Toast.makeText(this@MainActivity, mensaje, Toast.LENGTH_LONG).show()
                loadSociosData() // Recargar la vista
                syncButton.isEnabled = true
            }
            
        } catch (e: Exception) {
            runOnUiThread {
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                updateStatus("⚠️ No se pudo conectar - $timestamp")
                Toast.makeText(this, "🔌 Verificar:\n• Servidor PC activo\n• Misma red WiFi\n• Puerto 5000 abierto", Toast.LENGTH_LONG).show()
                Log.e("MainActivity", "Error conectando con servidor PC", e)
                syncButton.isEnabled = true
            }
        }
    }
    
    private fun makeHttpRequest(urlString: String): String? {
        return try {
            Log.d("MainActivity", "🌐 HTTP REQUEST: $urlString")
            val url = URL(urlString)
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 20000 // 20 segundos timeout (más tiempo)
            connection.readTimeout = 30000 // 30 segundos timeout (más tiempo)
            connection.setRequestProperty("User-Agent", "AAVV-Android-App/1.0")
            connection.setRequestProperty("Accept", "application/json")
            connection.setRequestProperty("Cache-Control", "no-cache")
            
            Log.d("MainActivity", "🔗 Conectando a: ${connection.url}")
            Log.d("MainActivity", "⏱️ Timeouts: Connect=20s, Read=30s")
            
            val responseCode = connection.responseCode
            Log.d("MainActivity", "📡 Response Code: $responseCode para $urlString")
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val response = reader.readText()
                reader.close()
                Log.d("MainActivity", "✅ HTTP OK - Datos recibidos: ${response.length} chars")
                response
            } else {
                Log.e("MainActivity", "❌ HTTP Error: $responseCode para URL: $urlString")
                // Leer error stream si existe
                try {
                    val errorReader = BufferedReader(InputStreamReader(connection.errorStream))
                    val errorResponse = errorReader.readText()
                    errorReader.close()
                    Log.e("MainActivity", "Error response: $errorResponse")
                } catch (e: Exception) {
                    Log.e("MainActivity", "No se pudo leer error stream")
                }
                null
            }
        } catch (e: Exception) {
            Log.e("MainActivity", "💥 EXCEPCIÓN en HTTP request para $urlString: ${e.message}", e)
            null
        }
    }
    
    private fun processSociosData(jsonString: String): Int {
        return try {
            Log.d("MainActivity", "📊 Procesando JSON socios: ${jsonString.take(100)}...")
            val jsonArray = JSONArray(jsonString)
            var sociosSincronizados = 0
            
            // Limpiar datos previos para sincronización completa
            databaseHelper.clearSocios()
            
            for (i in 0 until jsonArray.length()) {
                val socioJson = jsonArray.getJSONObject(i)
                val socio = Socio(
                    id = socioJson.optInt("id", 0),
                    numeroSocio = socioJson.optInt("numero_socio", 0),
                    nombre = socioJson.optString("nombre", ""),
                    apellidos = socioJson.optString("apellidos", ""),
                    dni = socioJson.optString("dni", ""),
                    direccion = socioJson.optString("direccion", ""),
                    poblacion = socioJson.optString("poblacion", ""),
                    codigoPostal = socioJson.optString("codigo_postal", ""),
                    telefono = socioJson.optString("telefono", ""),
                    formaPago = socioJson.optString("FORMA_PAGP", ""),
                    fechaAlta = socioJson.optString("fecha_alta", ""),
                    fechaBaja = socioJson.optString("fecha_baja", ""),
                    email = socioJson.optString("email", ""),
                    enAlta = socioJson.optBoolean("en_alta", true)
                )
                
                databaseHelper.insertSocio(socio)
                sociosSincronizados++
            }
            
            Log.d("MainActivity", "✅ Sincronizados $sociosSincronizados socios")
            sociosSincronizados
            
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Error procesando datos de socios: ${e.message}", e)
            0
        }
    }
    
    private fun processPagosData(jsonString: String): Int {
        return try {
            Log.d("MainActivity", "💰 Procesando JSON pagos: ${jsonString.take(100)}...")
            val jsonArray = JSONArray(jsonString)
            var pagosSincronizados = 0
            
            // Limpiar datos previos para sincronización completa
            databaseHelper.clearPagos()
            
            for (i in 0 until jsonArray.length()) {
                val pagoJson = jsonArray.getJSONObject(i)
                val pago = Pago(
                    id = pagoJson.optInt("id", 0),
                    fecha = pagoJson.optString("fecha", ""),
                    importe = pagoJson.optDouble("importe", 0.0),
                    idNumeroSocio = pagoJson.optInt("id_numero_socio", 0),
                    nombreSocio = pagoJson.optString("nombre_socio", "")
                )
                
                databaseHelper.insertPago(pago)
                pagosSincronizados++
            }
            
            Log.d("MainActivity", "✅ Sincronizados $pagosSincronizados pagos")
            pagosSincronizados
            
        } catch (e: Exception) {
            Log.e("MainActivity", "❌ Error procesando datos de pagos: ${e.message}", e)
            0
        }
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
                val status = if (socio.enAlta) "✅" else "❌"
                sociosInfo.append("$status ${socio.nombre} ${socio.apellidos}\n")
                sociosInfo.append("   📱 ${socio.telefono}\n")
                sociosInfo.append("   📧 ${socio.email}\n")
                sociosInfo.append("   📍 ${socio.direccion}, ${socio.poblacion}\n\n")
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
                    poblacion = "Isla Plana",
                    codigoPostal = "30868",
                    fechaAlta = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
                    enAlta = true
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
                        val status = if (socio.enAlta) "✅" else "❌"
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
                    cuotasText.append("📅 Fecha: ${pago.fecha}\n")
                    cuotasText.append("� ${pago.nombreSocio}\n\n")
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
                    cuotasText.append("📅 Fecha: ${pago.fecha}\n")
                    cuotasText.append("� ${pago.nombreSocio}\n\n")
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
                    cuotasText.append("📅 Fecha: ${pago.fecha}\n")
                    cuotasText.append("� ${pago.nombreSocio}\n\n")
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