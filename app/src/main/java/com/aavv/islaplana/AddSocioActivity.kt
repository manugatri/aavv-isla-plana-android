package com.aavv.islaplana

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.models.Socio
import com.aavv.islaplana.sync.SyncManager
import java.text.SimpleDateFormat
import java.util.*

class AddSocioActivity : Activity() {
    
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var syncManager: SyncManager
    
    // Campos del formulario
    private lateinit var numeroSocioEditText: EditText
    private lateinit var nombreEditText: EditText
    private lateinit var apellidosEditText: EditText
    private lateinit var dniEditText: EditText
    private lateinit var direccionEditText: EditText
    private lateinit var poblacionEditText: EditText
    private lateinit var codigoPostalEditText: EditText
    private lateinit var telefonoEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var formaPagoSpinner: Spinner
    private lateinit var saveButton: Button
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            createLayout()
            initializeComponents()
            setupSpinners()
            setupListeners()
            
        } catch (e: Exception) {
            Log.e("AddSocioActivity", "Error en onCreate", e)
            showErrorAndFinish("Error al inicializar el formulario: ${e.message}")
        }
    }
    
    private fun createLayout() {
        val scrollView = ScrollView(this)
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 16, 16, 16)
        }
        
        // Título
        val titleText = TextView(this).apply {
            text = "➕ Añadir Nuevo Socio"
            textSize = 20f
            setPadding(0, 0, 0, 24)
        }
        layout.addView(titleText)
        
        // Número de socio
        layout.addView(createLabel("Número de Socio:"))
        numeroSocioEditText = createEditText("Número único del socio")
        layout.addView(numeroSocioEditText)
        
        // Nombre
        layout.addView(createLabel("Nombre: *"))
        nombreEditText = createEditText("Nombre del socio")
        layout.addView(nombreEditText)
        
        // Apellidos
        layout.addView(createLabel("Apellidos: *"))
        apellidosEditText = createEditText("Apellidos del socio")
        layout.addView(apellidosEditText)
        
        // DNI
        layout.addView(createLabel("DNI:"))
        dniEditText = createEditText("12345678X")
        layout.addView(dniEditText)
        
        // Dirección
        layout.addView(createLabel("Dirección:"))
        direccionEditText = createEditText("Calle, número, piso...")
        layout.addView(direccionEditText)
        
        // Población
        layout.addView(createLabel("Población:"))
        poblacionEditText = createEditText("Ciudad o pueblo")
        layout.addView(poblacionEditText)
        
        // Código postal
        layout.addView(createLabel("Código Postal:"))
        codigoPostalEditText = createEditText("30000")
        layout.addView(codigoPostalEditText)
        
        // Teléfono
        layout.addView(createLabel("Teléfono:"))
        telefonoEditText = createEditText("600123456")
        layout.addView(telefonoEditText)
        
        // Email
        layout.addView(createLabel("Email:"))
        emailEditText = createEditText("ejemplo@email.com")
        layout.addView(emailEditText)
        
        // Forma de pago
        layout.addView(createLabel("Forma de Pago:"))
        formaPagoSpinner = Spinner(this).apply {
            setPadding(0, 8, 0, 16)
        }
        layout.addView(formaPagoSpinner)
        
        // Botones
        val buttonLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 24, 0, 0)
        }
        
        saveButton = Button(this).apply {
            text = "💾 Guardar Socio"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        buttonLayout.addView(saveButton)
        
        val cancelButton = Button(this).apply {
            text = "❌ Cancelar"
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        }
        buttonLayout.addView(cancelButton)
        
        layout.addView(buttonLayout)
        
        cancelButton.setOnClickListener { finish() }
        
        scrollView.addView(layout)
        setContentView(scrollView)
    }
    
    private fun createLabel(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = 14f
            setPadding(0, 16, 0, 4)
        }
    }
    
    private fun createEditText(hint: String): EditText {
        return EditText(this).apply {
            this.hint = hint
            setPadding(12, 12, 12, 12)
        }
    }
    
    private fun initializeComponents() {
        dbHelper = DatabaseHelper(this)
        syncManager = SyncManager(this)
    }
    
    private fun setupSpinners() {
        // Formas de pago comunes
        val formasPago = listOf(
            "Efectivo",
            "Transferencia",
            "Domiciliación",
            "Tarjeta",
            "Bizum"
        )
        
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, formasPago)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        formaPagoSpinner.adapter = adapter
    }
    
    private fun setupListeners() {
        saveButton.setOnClickListener {
            if (validateForm()) {
                saveSocio()
            }
        }
    }
    
    private fun validateForm(): Boolean {
        var isValid = true
        
        if (nombreEditText.text.toString().trim().isEmpty()) {
            nombreEditText.error = "El nombre es obligatorio"
            isValid = false
        }
        
        if (apellidosEditText.text.toString().trim().isEmpty()) {
            apellidosEditText.error = "Los apellidos son obligatorios"
            isValid = false
        }
        
        val numeroSocio = numeroSocioEditText.text.toString().trim()
        if (numeroSocio.isEmpty()) {
            numeroSocioEditText.error = "El número de socio es obligatorio"
            isValid = false
        } else {
            try {
                numeroSocio.toInt()
            } catch (e: NumberFormatException) {
                numeroSocioEditText.error = "Debe ser un número válido"
                isValid = false
            }
        }
        
        return isValid
    }
    
    private fun saveSocio() {
        try {
            val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            
            val socio = Socio(
                numeroSocio = numeroSocioEditText.text.toString().trim().toInt(),
                nombre = nombreEditText.text.toString().trim(),
                apellidos = apellidosEditText.text.toString().trim(),
                dni = dniEditText.text.toString().trim(),
                direccion = direccionEditText.text.toString().trim(),
                poblacion = poblacionEditText.text.toString().trim(),
                codigoPostal = codigoPostalEditText.text.toString().trim(),
                telefono = telefonoEditText.text.toString().trim(),
                email = emailEditText.text.toString().trim(),
                formaPago = formaPagoSpinner.selectedItem.toString(),
                fechaAlta = fechaActual,
                enAlta = true
            )
            
            // Guardar localmente
            val result = dbHelper.insertSocio(socio)
            
            if (result != -1L) {
                // Intentar sincronizar con el servidor
                syncWithServer(socio)
            } else {
                Toast.makeText(this, "Error al guardar el socio", Toast.LENGTH_LONG).show()
            }
            
        } catch (e: Exception) {
            Log.e("AddSocioActivity", "Error guardando socio", e)
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun syncWithServer(socio: Socio) {
        val progressDialog = AlertDialog.Builder(this)
            .setTitle("Guardando Socio")
            .setMessage("Sincronizando con el servidor...")
            .setCancelable(false)
            .create()
        
        progressDialog.show()
        
        syncManager.addSocioToServer(socio) { success, message ->
            runOnUiThread {
                progressDialog.dismiss()
                
                if (success) {
                    AlertDialog.Builder(this)
                        .setTitle("✅ Éxito")
                        .setMessage("Socio guardado exitosamente\n\n$message")
                        .setPositiveButton("Aceptar") { _, _ ->
                            setResult(RESULT_OK)
                            finish()
                        }
                        .show()
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("⚠️ Guardado Local")
                        .setMessage("El socio se guardó localmente, pero no se pudo sincronizar con el servidor:\n\n$message\n\nSe sincronizará en la próxima conexión.")
                        .setPositiveButton("Aceptar") { _, _ ->
                            setResult(RESULT_OK)
                            finish()
                        }
                        .show()
                }
            }
        }
    }
    
    private fun showErrorAndFinish(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("Cerrar") { _, _ -> finish() }
            .show()
    }
}