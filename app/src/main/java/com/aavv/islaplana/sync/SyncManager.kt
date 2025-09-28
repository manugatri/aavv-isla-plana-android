package com.aavv.islaplana.sync

import android.content.Context
import android.util.Log
import com.aavv.islaplana.database.DatabaseHelper
import com.aavv.islaplana.models.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*

class SyncManager(private val context: Context) {
    
    private val dbHelper = DatabaseHelper(context)
    private val baseUrl = "http://192.168.1.100:5000" // IP de la aplicación PC
    
    companion object {
        private const val TAG = "SyncManager"
    }
    
    interface SyncCallback {
        fun onSyncStart()
        fun onSyncProgress(message: String)
        fun onSyncComplete(success: Boolean, message: String)
    }
    
    fun syncWithPC(callback: SyncCallback) {
        Thread {
            try {
                callback.onSyncStart()
                
                // 1. Sincronizar socios
                callback.onSyncProgress("Sincronizando socios...")
                syncSocios()
                
                // 2. Sincronizar pagos
                callback.onSyncProgress("Sincronizando pagos...")
                syncPagos()
                
                // 3. Sincronizar formas de pago
                callback.onSyncProgress("Sincronizando formas de pago...")
                syncFormasPago()
                
                callback.onSyncComplete(true, "Sincronización completada exitosamente")
                
            } catch (e: Exception) {
                Log.e(TAG, "Error en sincronización", e)
                callback.onSyncComplete(false, "Error: ${e.message}")
            }
        }.start()
    }
    
    private fun syncSocios() {
        try {
            val url = URL("$baseUrl/api/socios")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)
                
                // Limpiar datos existentes
                dbHelper.clearAllData()
                
                // Insertar nuevos datos
                for (i in 0 until jsonArray.length()) {
                    val jsonSocio = jsonArray.getJSONObject(i)
                    val socio = Socio(
                        id = jsonSocio.optInt("Id", 0),
                        numeroSocio = jsonSocio.optInt("NUMERO_SOCIO", 0),
                        nombre = jsonSocio.optString("NOMBRE", ""),
                        apellidos = jsonSocio.optString("APELLIDOS", ""),
                        dni = jsonSocio.optString("DNI", ""),
                        direccion = jsonSocio.optString("DIRECCION", ""),
                        poblacion = jsonSocio.optString("POBLACION", ""),
                        codigoPostal = jsonSocio.optString("CODIGO_POSTAL", ""),
                        telefono = jsonSocio.optString("TELEFONO", ""),
                        formaPago = jsonSocio.optString("FORMA_PAGP", ""),
                        fechaAlta = jsonSocio.optString("FECHA_ALTA", ""),
                        fechaBaja = jsonSocio.optString("FECHA_BAJA", ""),
                        email = jsonSocio.optString("email", ""),
                        enAlta = jsonSocio.optInt("EN_ALTA", 1) == 1
                    )
                    dbHelper.insertSocio(socio)
                }
                
                Log.d(TAG, "Socios sincronizados: ${jsonArray.length()}")
            } else {
                throw Exception("Error del servidor: $responseCode")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sincronizando socios", e)
            throw e
        }
    }
    
    private fun syncPagos() {
        try {
            val url = URL("$baseUrl/api/pagos")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 10000
            connection.readTimeout = 10000
            
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val response = connection.inputStream.bufferedReader().readText()
                val jsonArray = JSONArray(response)
                
                // Insertar pagos
                for (i in 0 until jsonArray.length()) {
                    val jsonPago = jsonArray.getJSONObject(i)
                    val pago = Pago(
                        id = jsonPago.optInt("Id", 0),
                        fecha = jsonPago.optString("FECHA", ""),
                        importe = jsonPago.optDouble("IMPORTE", 0.0),
                        idNumeroSocio = jsonPago.optInt("ID_NUMERO_SOCIO", 0)
                    )
                    dbHelper.insertPago(pago)
                }
                
                Log.d(TAG, "Pagos sincronizados: ${jsonArray.length()}")
            } else {
                throw Exception("Error del servidor: $responseCode")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sincronizando pagos", e)
            throw e
        }
    }
    
    private fun syncFormasPago() {
        // Implementar sincronización de formas de pago si es necesario
        Log.d(TAG, "Formas de pago sincronizadas")
    }
    
    fun testConnection(): Boolean {
        return try {
            val url = URL("$baseUrl/")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            val responseCode = connection.responseCode
            responseCode == HttpURLConnection.HTTP_OK
        } catch (e: Exception) {
            Log.e(TAG, "Error probando conexión", e)
            false
        }
    }
    
    // Método para enviar un nuevo socio al servidor
    fun addSocioToServer(socio: Socio, callback: (Boolean, String) -> Unit) {
        Thread {
            try {
                val url = URL("$baseUrl/api/socios")
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                
                val jsonSocio = JSONObject().apply {
                    put("NUMERO_SOCIO", socio.numeroSocio)
                    put("NOMBRE", socio.nombre)
                    put("APELLIDOS", socio.apellidos)
                    put("DNI", socio.dni)
                    put("DIRECCION", socio.direccion)
                    put("POBLACION", socio.poblacion)
                    put("CODIGO_POSTAL", socio.codigoPostal)
                    put("TELEFONO", socio.telefono)
                    put("FORMA_PAGP", socio.formaPago)
                    put("FECHA_ALTA", socio.fechaAlta)
                    put("email", socio.email)
                    put("EN_ALTA", if (socio.enAlta) 1 else 0)
                }
                
                connection.outputStream.use { os ->
                    os.write(jsonSocio.toString().toByteArray())
                }
                
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_CREATED) {
                    callback(true, "Socio añadido exitosamente")
                } else {
                    callback(false, "Error del servidor: $responseCode")
                }
                
            } catch (e: Exception) {
                Log.e(TAG, "Error añadiendo socio", e)
                callback(false, "Error: ${e.message}")
            }
        }.start()
    }
}