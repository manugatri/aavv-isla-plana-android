package com.aavv.islaplana.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

/**
 * DatabaseHelper para AAVV Isla Plana
 * 
 * IMPORTANTE: Esta clase contiene SOLO la estructura de la base de datos.
 * Los datos reales de socios y pagos se descargan desde el servidor PC
 * durante la primera sincronización para preservar la seguridad.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    
    companion object {
        const val DATABASE_NAME = "aavv_isla_plana.db"
        const val DATABASE_VERSION = 1
        
        // Tabla socios
        const val TABLE_SOCIOS = "socios"
        const val COLUMN_SOCIOS_ID = "Id"
        const val COLUMN_NUMERO_SOCIO = "NUMERO_SOCIO"
        const val COLUMN_NOMBRE = "NOMBRE"
        const val COLUMN_APELLIDOS = "APELLIDOS"
        const val COLUMN_DNI = "DNI"
        const val COLUMN_DIRECCION = "DIRECCION"
        const val COLUMN_POBLACION = "POBLACION"
        const val COLUMN_CODIGO_POSTAL = "CODIGO_POSTAL"
        const val COLUMN_TELEFONO = "TELEFONO"
        const val COLUMN_FORMA_PAGO = "FORMA_PAGP"
        const val COLUMN_FECHA_ALTA = "FECHA_ALTA"
        const val COLUMN_FECHA_BAJA = "FECHA_BAJA"
        const val COLUMN_EMAIL = "email"
        const val COLUMN_EN_ALTA = "EN_ALTA"
        
        // Tabla pagos
        const val TABLE_PAGOS = "pagos"
        const val COLUMN_PAGOS_ID = "Id"
        const val COLUMN_FECHA = "FECHA"
        const val COLUMN_IMPORTE = "IMPORTE"
        const val COLUMN_ID_NUMERO_SOCIO = "ID_NUMERO_SOCIO"
        
        // Tabla forma_pago
        const val TABLE_FORMA_PAGO = "forma_pago"
        const val COLUMN_FORMA_PAGO_ID = "Id"
        const val COLUMN_FORMA_PAGO_NOMBRE = "FORMA_PAGO"
        
        // Tabla codigos_postales
        const val TABLE_CODIGOS_POSTALES = "codigos_postales"
        const val COLUMN_CP_ID = "Id"
        const val COLUMN_CODIGO = "CODIGO"
    }
    
    override fun onCreate(db: SQLiteDatabase) {
        // Crear tabla socios
        val createSociosTable = """
            CREATE TABLE $TABLE_SOCIOS (
                $COLUMN_SOCIOS_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NUMERO_SOCIO INTEGER,
                $COLUMN_NOMBRE TEXT,
                $COLUMN_APELLIDOS TEXT,
                $COLUMN_DNI TEXT,
                $COLUMN_DIRECCION TEXT,
                $COLUMN_POBLACION TEXT,
                $COLUMN_CODIGO_POSTAL INTEGER,
                $COLUMN_TELEFONO TEXT,
                $COLUMN_FORMA_PAGO INTEGER,
                $COLUMN_FECHA_ALTA TEXT,
                $COLUMN_FECHA_BAJA TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_EN_ALTA INTEGER DEFAULT 1
            )
        """.trimIndent()
        
        // Crear tabla pagos
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
        
        db.execSQL(createSociosTable)
        db.execSQL(createPagosTable)
        db.execSQL(createFormaPagoTable)
        db.execSQL(createCodigosPostalesTable)
        
        Log.d("DatabaseHelper", "Base de datos creada exitosamente")
    }
    
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_SOCIOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PAGOS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FORMA_PAGO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CODIGOS_POSTALES")
        onCreate(db)
    }
    
    // MÉTODOS PARA SOCIOS
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
        val socios = mutableListOf<Socio>()
        val db = readableDatabase
        val selection = "$COLUMN_EN_ALTA = ?"
        val selectionArgs = arrayOf("1")
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
    
    fun buscarSocios(query: String): List<Socio> {
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
    
    // MÉTODOS PARA PAGOS
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
    
    fun clearAllData() {
        val db = writableDatabase
        db.execSQL("DELETE FROM $TABLE_SOCIOS")
        db.execSQL("DELETE FROM $TABLE_PAGOS")
        db.execSQL("DELETE FROM $TABLE_FORMA_PAGO")
        db.execSQL("DELETE FROM $TABLE_CODIGOS_POSTALES")
        Log.d("DatabaseHelper", "Todos los datos eliminados")
    }
}