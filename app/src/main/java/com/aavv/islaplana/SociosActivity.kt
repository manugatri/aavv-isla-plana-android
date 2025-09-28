package com.aavv.islaplana

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.*
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.models.Socio

class SociosActivity : Activity() {
    
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var searchEditText: EditText
    private lateinit var sociosListView: ListView
    private lateinit var addSocioButton: Button
    private lateinit var activosCheckBox: CheckBox
    
    private var allSocios = mutableListOf<Socio>()
    private var filteredSocios = mutableListOf<Socio>()
    private lateinit var sociosAdapter: ArrayAdapter<String>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            createLayout()
            initializeComponents()
            setupListeners()
            loadSocios()
            
        } catch (e: Exception) {
            Log.e("SociosActivity", "Error en onCreate", e)
            showErrorAndFinish("Error al inicializar la pantalla de socios: ${e.message}")
        }
    }
    
    private fun createLayout() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        // Título
        val titleText = TextView(this).apply {
            text = "👥 Gestión de Socios"
            textSize = 20f
            setPadding(0, 0, 0, 16)
        }
        layout.addView(titleText)
        
        // Campo de búsqueda
        searchEditText = EditText(this).apply {
            hint = "Buscar socio (nombre, apellidos, DNI)..."
            setPadding(12, 12, 12, 12)
        }
        layout.addView(searchEditText)
        
        // Checkbox para mostrar solo activos
        activosCheckBox = CheckBox(this).apply {
            text = "Mostrar solo socios activos"
            isChecked = true
            setPadding(0, 8, 0, 8)
        }
        layout.addView(activosCheckBox)
        
        // Botón añadir socio
        addSocioButton = Button(this).apply {
            text = "➕ Añadir Nuevo Socio"
            setPadding(0, 16, 0, 16)
        }
        layout.addView(addSocioButton)
        
        // Lista de socios
        sociosListView = ListView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }
        layout.addView(sociosListView)
        
        // Botón volver
        val backButton = Button(this).apply {
            text = "⬅ Volver"
            setPadding(0, 16, 0, 0)
        }
        layout.addView(backButton)
        
        backButton.setOnClickListener { finish() }
        
        setContentView(layout)
    }
    
    private fun initializeComponents() {
        dbHelper = DatabaseHelper(this)
        sociosAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf<String>())
        sociosListView.adapter = sociosAdapter
    }
    
    private fun setupListeners() {
        // Búsqueda en tiempo real
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterSocios()
            }
        })
        
        // Filtro de activos
        activosCheckBox.setOnCheckedChangeListener { _, _ ->
            filterSocios()
        }
        
        // Click en socio
        sociosListView.setOnItemClickListener { _, _, position, _ ->
            val socio = filteredSocios[position]
            showSocioDetails(socio)
        }
        
        // Añadir socio
        addSocioButton.setOnClickListener {
            startActivity(Intent(this, AddSocioActivity::class.java))
        }
    }
    
    private fun loadSocios() {
        try {
            allSocios.clear()
            allSocios.addAll(dbHelper.getAllSocios())
            filterSocios()
            
            Log.d("SociosActivity", "Cargados ${allSocios.size} socios")
            
        } catch (e: Exception) {
            Log.e("SociosActivity", "Error cargando socios", e)
            Toast.makeText(this, "Error cargando socios: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun filterSocios() {
        filteredSocios.clear()
        
        val query = searchEditText.text.toString().toLowerCase().trim()
        val soloActivos = activosCheckBox.isChecked
        
        for (socio in allSocios) {
            // Filtrar por estado
            if (soloActivos && !socio.enAlta) continue
            
            // Filtrar por búsqueda
            if (query.isNotEmpty()) {
                val nombreCompleto = "${socio.nombre} ${socio.apellidos}".toLowerCase()
                val dni = socio.dni.toLowerCase()
                
                if (!nombreCompleto.contains(query) && !dni.contains(query)) {
                    continue
                }
            }
            
            filteredSocios.add(socio)
        }
        
        // Actualizar adapter
        val displayList = filteredSocios.map { it.getDisplayInfo() }
        sociosAdapter.clear()
        sociosAdapter.addAll(displayList)
        sociosAdapter.notifyDataSetChanged()
        
        // Mostrar contador
        val estado = if (activosCheckBox.isChecked) "activos" else "total"
        Toast.makeText(this, "${filteredSocios.size} socios $estado encontrados", Toast.LENGTH_SHORT).show()
    }
    
    private fun showSocioDetails(socio: Socio) {
        val details = """
            📋 DETALLES DEL SOCIO
            
            👤 Nombre: ${socio.getNombreCompleto()}
            🆔 Número: ${socio.numeroSocio}
            📄 DNI: ${socio.dni}
            📍 Dirección: ${socio.direccion}
            🏘️ Población: ${socio.poblacion}
            📮 C.P.: ${socio.codigoPostal}
            📞 Teléfono: ${socio.telefono}
            📧 Email: ${socio.email}
            💳 Forma pago: ${socio.formaPago}
            📅 Fecha alta: ${socio.fechaAlta}
            📅 Fecha baja: ${socio.fechaBaja}
            ✅ Estado: ${socio.getEstado()}
        """.trimIndent()
        
        AlertDialog.Builder(this)
            .setTitle("Detalles del Socio")
            .setMessage(details)
            .setPositiveButton("Cerrar") { dialog, _ -> dialog.dismiss() }
            .setNeutralButton("Ver Pagos") { _, _ -> 
                val intent = Intent(this, PagosActivity::class.java)
                intent.putExtra("socio_id", socio.id)
                intent.putExtra("socio_nombre", socio.getNombreCompleto())
                startActivity(intent)
            }
            .show()
    }
    
    override fun onResume() {
        super.onResume()
        loadSocios() // Recargar al volver de añadir socio
    }
    
    private fun showErrorAndFinish(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Cerrar") { _, _ -> finish() }
            .show()
    }
}