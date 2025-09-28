package com.aavv.islaplana.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * DatabaseHelper para AAVV Isla Plana
 * 
 * IMPORTANTE: Esta clase contiene la estructura IDÉNTICA a la base de datos del PC.
 * Los datos reales de socios y pagos se descargan desde el servidor PC
 * durante la primera sincronización para preservar la seguridad.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        const val DATABASE_NAME = "aavv_isla_plana.db"
        const val DATABASE_VERSION = 1
        private const val TAG = "DatabaseHelper"
        
        // Tabla socios - ESTRUCTURA REAL DEL PC
        const val TABLE_SOCIOS = "socios"
        const val COLUMN_SOCIOS_ID = "id"
        const val COLUMN_NUMERO_SOCIO = "numero_socio"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_APELLIDOS = "apellidos"
        const val COLUMN_DNI = "dni"
        const val COLUMN_DIRECCION = "direccion"
        const val COLUMN_POBLACION = "poblacion"
        const val COLUMN_CODIGO_POSTAL = "codigo_postal"
        const val COLUMN_TELEFONO = "telefono"
        const val COLUMN_FORMA_PAGO = "forma_pago"
        const val COLUMN_FECHA_ALTA = "fecha_alta"
        const val COLUMN_FECHA_BAJA = "fecha_baja"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_EN_ALTA = "en_alta"
        
        // Tabla pagos - ESTRUCTURA REAL DEL PC
        const val TABLE_PAGOS = "pagos"
        const val COLUMN_PAGOS_ID = "id"
        const val COLUMN_FECHA = "fecha"
        const val COLUMN_IMPORTE = "importe"
        const val COLUMN_ID_NUMERO_SOCIO = "id_numero_socio"
        
        // Tabla forma_pago
        const val TABLE_FORMA_PAGO = "forma_pago"
        const val COLUMN_FORMA_PAGO_ID = "id"
        const val COLUMN_FORMA_PAGO_NOMBRE = "nombre"
        
        // Tabla codigos_postales
        const val TABLE_CODIGOS_POSTALES = "codigos_postales"
        const val COLUMN_CP_ID = "id"
        const val COLUMN_CODIGO = "codigo"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla socios - IDÉNTICA AL PC
        val createSociosTable = """
            CREATE TABLE $TABLE_SOCIOS (
                $COLUMN_SOCIOS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NUMERO_SOCIO INTEGER,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDOS TEXT,
                $COLUMN_DNI TEXT,
                $COLUMN_DIRECCION TEXT,
                $COLUMN_POBLACION TEXT,
                $COLUMN_CODIGO_POSTAL TEXT,
                $COLUMN_TELEFONO TEXT,
                $COLUMN_FORMA_PAGO TEXT,
                $COLUMN_FECHA_ALTA TEXT,
                $COLUMN_FECHA_BAJA TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_EN_ALTA INTEGER DEFAULT 1
            )
        """.trimIndent()
        
        // Crear tabla pagos - IDÉNTICA AL PC
        val createPagosTable = """
            CREATE TABLE $TABLE_PAGOS (
                $COLUMN_PAGOS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FECHA TEXT,
                $COLUMN_IMPORTE REAL,
                $COLUMN_ID_NUMERO_SOCIO INTEGER
            )
        """.trimIndent()
        
        // Crear tabla forma_pago
        val createFormaPagoTable = """
            CREATE TABLE $TABLE_FORMA_PAGO (
                $COLUMN_FORMA_PAGO_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FORMA_PAGO_NOMBRE TEXT
            )
        """.trimIndent()
        
        // Crear tabla codigos_postales
        val createCodigosPostalesTable = """
            CREATE TABLE $TABLE_CODIGOS_POSTALES (
                $COLUMN_CP_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CODIGO TEXT
            )
        """.trimIndent()
        
        try {
            db.execSQL(createSociosTable)
            db.execSQL(createPagosTable)
            db.execSQL(createFormaPagoTable)
            db.execSQL(createCodigosPostalesTable)
            
            // Insertar datos iniciales básicos (sin datos sensibles)
            insertInitialData(db)
            
            Log.d(TAG, "Base de datos creada con estructura idéntica al PC")
        } catch (e: Exception) {
            Log.e(TAG, "Error creando base de datos", e)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAGOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FORMA_PAGO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CODIGOS_POSTALES")
        onCreate(db)
    }

    private fun insertInitialData(db: SQLiteDatabase) {
        // Insertar solo formas de pago básicas (sin datos sensibles)
        val formasPago = listOf(
            "Efectivo",
            "Transferencia", 
            "Tarjeta",
            "Domiciliación bancaria"
        )
        
        formasPago.forEach { forma ->
            val values = ContentValues().apply {
                put(COLUMN_FORMA_PAGO_NOMBRE, forma)
            }
            db.insert(TABLE_FORMA_PAGO, null, values)
        }
        
        // Insertar códigos postales básicos de la zona (información pública)
        val codigosPostales = listOf(
            "30868", // Isla Plana
            "30860", // Puerto de Mazarrón
            "30870"  // Mazarrón
        )
        
        codigosPostales.forEach { codigo ->
            val values = ContentValues().apply {
                put(COLUMN_CODIGO, codigo)
            }
            db.insert(TABLE_CODIGOS_POSTALES, null, values)
        }
        
        Log.d(TAG, "Base de datos inicializada - Lista para sincronizar con servidor PC")
    }

    // === OPERACIONES SOCIOS ===
    
    fun insertSocio(socio: Socio): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NUMERO_SOCIO, socio.numeroSocio)
            put(COLUMN_NOMBRE, socio.nombre)
            put(COLUMN_APELLIDOS, socio.apellidos)
            put(COLUMN_DNI, socio.dni)
            put(COLUMN_DIRECCION, socio.direccion)
            put(COLUMN_POBLACION, socio.poblacion)
            put(COLUMN_CODIGO_POSTAL, socio.codigoPostal)
            put(COLUMN_TELEFONO, socio.telefono)
            put(COLUMN_FORMA_PAGO, socio.formaPago)
            put(COLUMN_FECHA_ALTA, socio.fechaAlta)
            put(COLUMN_FECHA_BAJA, socio.fechaBaja)
            put(COLUMN_EMAIL, socio.email)
            put(COLUMN_EN_ALTA, if (socio.enAlta) 1 else 0)
        }
        return db.insert(TABLE_SOCIOS, null, values)
    }
    
    fun getAllSocios(): List<Socio> {
        val socios = mutableListOf<Socio>()
        val db = readableDatabase
        val cursor = db.query(TABLE_SOCIOS, null, null, null, null, null, "$COLUMN_APELLIDOS, $COLUMN_NOMBRE")
        
        cursor.use {
            while (it.moveToNext()) {
                val socio = Socio(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_SOCIOS_ID)),
                    numeroSocio = it.getInt(it.getColumnIndexOrThrow(COLUMN_NUMERO_SOCIO)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE)) ?: "",
                    apellidos = it.getString(it.getColumnIndexOrThrow(COLUMN_APELLIDOS)) ?: "",
                    dni = it.getString(it.getColumnIndexOrThrow(COLUMN_DNI)) ?: "",
                    direccion = it.getString(it.getColumnIndexOrThrow(COLUMN_DIRECCION)) ?: "",
                    poblacion = it.getString(it.getColumnIndexOrThrow(COLUMN_POBLACION)) ?: "",
                    codigoPostal = it.getString(it.getColumnIndexOrThrow(COLUMN_CODIGO_POSTAL)) ?: "",
                    telefono = it.getString(it.getColumnIndexOrThrow(COLUMN_TELEFONO)) ?: "",
                    formaPago = it.getString(it.getColumnIndexOrThrow(COLUMN_FORMA_PAGO)) ?: "",
                    fechaAlta = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_ALTA)) ?: "",
                    fechaBaja = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_BAJA)) ?: "",
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)) ?: "",
                    enAlta = it.getInt(it.getColumnIndexOrThrow(COLUMN_EN_ALTA)) == 1
                )
                socios.add(socio)
            }
        }
        return socios
    }
    
    fun getSociosActivos(): List<Socio> {
        return getAllSocios().filter { it.enAlta }
    }
    
    fun searchSocios(query: String): List<Socio> {
        val socios = mutableListOf<Socio>()
        val db = readableDatabase
        val selection = "$COLUMN_NOMBRE LIKE ? OR $COLUMN_APELLIDOS LIKE ? OR $COLUMN_DNI LIKE ?"
        val selectionArgs = arrayOf("%$query%", "%$query%", "%$query%")
        val cursor = db.query(TABLE_SOCIOS, null, selection, selectionArgs, null, null, "$COLUMN_APELLIDOS, $COLUMN_NOMBRE")
        
        cursor.use {
            while (it.moveToNext()) {
                val socio = Socio(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_SOCIOS_ID)),
                    numeroSocio = it.getInt(it.getColumnIndexOrThrow(COLUMN_NUMERO_SOCIO)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_NOMBRE)) ?: "",
                    apellidos = it.getString(it.getColumnIndexOrThrow(COLUMN_APELLIDOS)) ?: "",
                    dni = it.getString(it.getColumnIndexOrThrow(COLUMN_DNI)) ?: "",
                    direccion = it.getString(it.getColumnIndexOrThrow(COLUMN_DIRECCION)) ?: "",
                    poblacion = it.getString(it.getColumnIndexOrThrow(COLUMN_POBLACION)) ?: "",
                    codigoPostal = it.getString(it.getColumnIndexOrThrow(COLUMN_CODIGO_POSTAL)) ?: "",
                    telefono = it.getString(it.getColumnIndexOrThrow(COLUMN_TELEFONO)) ?: "",
                    formaPago = it.getString(it.getColumnIndexOrThrow(COLUMN_FORMA_PAGO)) ?: "",
                    fechaAlta = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_ALTA)) ?: "",
                    fechaBaja = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA_BAJA)) ?: "",
                    email = it.getString(it.getColumnIndexOrThrow(COLUMN_EMAIL)) ?: "",
                    enAlta = it.getInt(it.getColumnIndexOrThrow(COLUMN_EN_ALTA)) == 1
                )
                socios.add(socio)
            }
        }
        return socios
    }

    // === OPERACIONES PAGOS ===
    
    fun insertPago(pago: Pago): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_FECHA, pago.fecha)
            put(COLUMN_IMPORTE, pago.importe)
            put(COLUMN_ID_NUMERO_SOCIO, pago.idNumeroSocio)
        }
        return db.insert(TABLE_PAGOS, null, values)
    }
    
    fun getAllPagos(): List<Pago> {
        val pagos = mutableListOf<Pago>()
        val db = readableDatabase
        val query = """
            SELECT p.*, (s.$COLUMN_NOMBRE || ' ' || s.$COLUMN_APELLIDOS) as nombre_completo
            FROM $TABLE_PAGOS p
            LEFT JOIN $TABLE_SOCIOS s ON p.$COLUMN_ID_NUMERO_SOCIO = s.$COLUMN_SOCIOS_ID
            ORDER BY p.$COLUMN_FECHA DESC
        """.trimIndent()
        
        val cursor = db.rawQuery(query, null)
        cursor.use {
            while (it.moveToNext()) {
                val pago = Pago(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_PAGOS_ID)),
                    fecha = it.getString(it.getColumnIndexOrThrow(COLUMN_FECHA)) ?: "",
                    importe = it.getDouble(it.getColumnIndexOrThrow(COLUMN_IMPORTE)),
                    idNumeroSocio = it.getInt(it.getColumnIndexOrThrow(COLUMN_ID_NUMERO_SOCIO)),
                    nombreSocio = it.getString(it.getColumnIndexOrThrow("nombre_completo")) ?: "Socio no encontrado"
                )
                pagos.add(pago)
            }
        }
        return pagos
    }
    
    // Métodos adicionales que usa MainActivity
    fun getCuotasVencidas(): List<Pago> {
        // Implementación simplificada - retorna lista vacía por ahora
        // En una implementación real, filtrarías por fecha de vencimiento
        return emptyList()
    }
    
    fun getCuotasPorVencer(): List<Pago> {
        // Implementación simplificada - retorna lista vacía por ahora  
        // En una implementación real, filtrarías por fechas futuras
        return emptyList()
    }
    
    fun getCuotasCobradas(): List<Pago> {
        // Retorna todos los pagos (considerados como cobrados)
        return getAllPagos()
    }
    
    fun getAllFormasPago(): List<FormaPago> {
        val formasPago = mutableListOf<FormaPago>()
        val db = readableDatabase
        val cursor = db.query(TABLE_FORMA_PAGO, null, null, null, null, null, COLUMN_FORMA_PAGO_NOMBRE)
        
        cursor.use {
            while (it.moveToNext()) {
                val formaPago = FormaPago(
                    id = it.getInt(it.getColumnIndexOrThrow(COLUMN_FORMA_PAGO_ID)),
                    nombre = it.getString(it.getColumnIndexOrThrow(COLUMN_FORMA_PAGO_NOMBRE)) ?: ""
                )
                formasPago.add(formaPago)
            }
        }
        return formasPago
    }
    
    fun clearSocios() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_SOCIOS")
        Log.d(TAG, "Datos de socios eliminados para sincronización")
    }
    
    fun clearPagos() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_PAGOS")
        Log.d(TAG, "Datos de pagos eliminados para sincronización")
    }
    
    fun clearAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_SOCIOS")
        db.execSQL("DELETE FROM $TABLE_PAGOS")
        db.execSQL("DELETE FROM $TABLE_FORMA_PAGO")
        db.execSQL("DELETE FROM $TABLE_CODIGOS_POSTALES")
        Log.d(TAG, "Todos los datos eliminados")
    }
}