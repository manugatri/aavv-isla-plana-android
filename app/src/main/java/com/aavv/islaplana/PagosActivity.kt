package com.aavv.islaplana

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.models.Pago
import java.text.SimpleDateFormat
import java.util.*

class PagosActivity : Activity() {
    
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var pagosListView: ListView
    private lateinit var filterSpinner: Spinner
    private lateinit var addPagoButton: Button
    
    private var allPagos = mutableListOf<Pago>()
    private var filteredPagos = mutableListOf<Pago>()
    private lateinit var pagosAdapter: ArrayAdapter<String>
    
    private var socioId: Int = -1
    private var socioNombre: String = ""
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Obtener datos del intent
            socioId = intent.getIntExtra("socio_id", -1)
            socioNombre = intent.getStringExtra("socio_nombre") ?: ""
            
            createLayout()
            initializeComponents()
            setupListeners()
            loadPagos()
            
        } catch (e: Exception) {
            Log.e("PagosActivity", "Error en onCreate", e)
            showErrorAndFinish("Error al inicializar la pantalla de pagos: ${e.message}")
        }
    }
    
    private fun createLayout() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        // Título
        val titleText = TextView(this).apply {
            text = if (socioNombre.isNotEmpty()) {
                "💰 Pagos de $socioNombre"
            } else {
                "💰 Gestión de Pagos"
            }
            textSize = 20f
            setPadding(0, 0, 0, 16)
        }
        layout.addView(titleText)
        
        // Filtro de pagos
        val filterLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 0, 0, 16)
        }
        
        val filterLabel = TextView(this).apply {
            text = "Ver: "
            textSize = 16f
            setPadding(0, 0, 8, 0)
        }
        filterLayout.addView(filterLabel)
        
        filterSpinner = Spinner(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        filterLayout.addView(filterSpinner)
        
        layout.addView(filterLayout)
        
        // Información de resumen
        val summaryText = TextView(this).apply {
            text = "📊 Cargando información..."
            textSize = 14f
            setPadding(0, 0, 0, 16)
        }
        layout.addView(summaryText)
        
        // Lista de pagos
        pagosListView = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        layout.addView(pagosListView)
        
        // Botones
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 16, 0, 0)
        }
        
        addPagoButton = Button(this).apply {
            text = "➕ Añadir Pago"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        buttonLayout.addView(addPagoButton)
        
        val backButton = Button(this).apply {
            text = "⬅ Volver"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        buttonLayout.addView(backButton)
        
        layout.addView(buttonLayout)
        
        backButton.setOnClickListener { finish() }
        
        setContentView(layout)
    }
    
    private fun initializeComponents() {
        dbHelper = DatabaseHelper(this)
        pagosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        pagosListView.adapter = pagosAdapter
        
        setupFilterSpinner()
    }
    
    private fun setupFilterSpinner() {
        val filterOptions = listOf(
            "Todos los pagos",
            "Últimos 30 días",
            "Últimos 3 meses",
            "Este año",
            "Año anterior"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, filterOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        filterSpinner.adapter = adapter
    }
    
    private fun setupListeners() {
        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                filterPagos(position)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
        
        pagosListView.setOnItemClickListener { _, _, position, _ ->
            val pago = filteredPagos[position]
            showPagoDetails(pago)
        }
        
        addPagoButton.setOnClickListener {
            showAddPagoDialog()
        }
    }
    
    private fun loadPagos() {
        try {
            allPagos.clear()
            
            if (socioId != -1) {
                // Cargar pagos de un socio específico (implementar método en DatabaseHelper)
                allPagos.addAll(dbHelper.getAllPagos().filter { it.idNumeroSocio == socioId })
            } else {
                // Cargar todos los pagos
                allPagos.addAll(dbHelper.getAllPagos())
            }
            
            filterPagos(0) // Mostrar todos inicialmente
            updateSummary()
            
            Log.d("PagosActivity", "Cargados ${allPagos.size} pagos")
            
        } catch (e: Exception) {
            Log.e("PagosActivity", "Error cargando pagos", e)
            Toast.makeText(this, "Error cargando pagos: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun filterPagos(filterIndex: Int) {
        filteredPagos.clear()
        
        val now = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        
        when (filterIndex) {
            0 -> filteredPagos.addAll(allPagos) // Todos
            1 -> { // Últimos 30 días
                val thirtyDaysAgo = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, -30) }
                filteredPagos.addAll(allPagos.filter { pago ->
                    try {
                        val pagoDate = dateFormat.parse(pago.fecha)
                        pagoDate != null && pagoDate.after(thirtyDaysAgo.time)
                    } catch (e: Exception) { false }
                })
            }
            2 -> { // Últimos 3 meses
                val threeMonthsAgo = Calendar.getInstance().apply { add(Calendar.MONTH, -3) }
                filteredPagos.addAll(allPagos.filter { pago ->
                    try {
                        val pagoDate = dateFormat.parse(pago.fecha)
                        pagoDate != null && pagoDate.after(threeMonthsAgo.time)
                    } catch (e: Exception) { false }
                })
            }
            3 -> { // Este año
                filteredPagos.addAll(allPagos.filter { pago ->
                    try {
                        val pagoDate = dateFormat.parse(pago.fecha)
                        val pagoCalendar = Calendar.getInstance().apply { time = pagoDate }
                        pagoCalendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                    } catch (e: Exception) { false }
                })
            }
            4 -> { // Año anterior
                filteredPagos.addAll(allPagos.filter { pago ->
                    try {
                        val pagoDate = dateFormat.parse(pago.fecha)
                        val pagoCalendar = Calendar.getInstance().apply { time = pagoDate }
                        pagoCalendar.get(Calendar.YEAR) == now.get(Calendar.YEAR) - 1
                    } catch (e: Exception) { false }
                })
            }
        }
        
        // Ordenar por fecha descendente
        filteredPagos.sortByDescending { pago ->
            try {
                dateFormat.parse(pago.fecha)
            } catch (e: Exception) { Date(0) }
        }
        
        // Actualizar adapter
        val displayList = filteredPagos.map { it.getDisplayInfo() }
        pagosAdapter.clear()
        pagosAdapter.addAll(displayList)
        pagosAdapter.notifyDataSetChanged()
        
        updateSummary()
    }
    
    private fun updateSummary() {
        val totalImporte = filteredPagos.sumOf { it.importe }
        val totalPagos = filteredPagos.size
        
        val summaryText = findViewById<TextView>(android.R.id.text1) // Buscar el TextView de resumen
        // Como no tenemos ID, actualizamos el título
        val titleText = (findViewById<LinearLayout>(android.R.id.content).getChildAt(0) as LinearLayout).getChildAt(2) as TextView
        titleText.text = "📊 Total: €${String.format("%.2f", totalImporte)} en $totalPagos pagos"
    }
    
    private fun showPagoDetails(pago: Pago) {
        val details = """
            💰 DETALLES DEL PAGO
            
            📅 Fecha: ${pago.fecha}
            💵 Importe: €${String.format("%.2f", pago.importe)}
            👤 Socio: ${pago.nombreSocio}
            🆔 ID Pago: ${pago.id}
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("Detalles del Pago")
            .setMessage(details)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .show()
    }
    
    private fun showAddPagoDialog() {
        // Crear un diálogo simple para añadir pago
        val dialogView = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        val importeEditText = EditText(this).apply {
            hint = "Importe (€)"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        dialogView.addView(importeEditText)
        
        val fechaEditText = EditText(this).apply {
            hint = "Fecha (DD/MM/AAAA)"
            setText(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
        }
        dialogView.addView(fechaEditText)
        
        AlertDialog.Builder(this)
            .setTitle("➕ Añadir Nuevo Pago")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val importe = importeEditText.text.toString().toDoubleOrNull()
                val fecha = fechaEditText.text.toString()
                
                if (importe != null && fecha.isNotEmpty()) {
                    val pago = Pago(
                        fecha = fecha,
                        importe = importe,
                        idNumeroSocio = socioId,
                        nombreSocio = socioNombre
                    )
                    
                    val result = dbHelper.insertPago(pago)
                    if (result != -1L) {
                        Toast.makeText(this, "Pago añadido exitosamente", Toast.LENGTH_SHORT).show()
                        loadPagos() // Recargar lista
                    } else {
                        Toast.makeText(this, "Error al añadir el pago", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    
    private fun showErrorAndFinish(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Cerrar") { _, _ -> finish() }
            .show()
    }
}