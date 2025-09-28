package com.aavv.islaplana.database

import java.io.Serializable

// Modelo para la tabla socios
data class Socio(
    val id: Long = 0,
    val nombre: String,
    val apellidos: String,
    val telefono: String = "",
    val email: String = "",
    val direccion: String = "",
    val codigoPostal: String = "",
    val localidad: String = "",
    val provincia: String = "",
    val fechaAlta: String = "",
    val activo: Boolean = true,
    val observaciones: String = ""
) : Serializable

// Modelo para la tabla pagos
data class Pago(
    val id: Long = 0,
    val socioId: Long,
    val importe: Double,
    val fechaPago: String,
    val fechaVencimiento: String,
    val formaPagoId: Long,
    val concepto: String = "",
    val observaciones: String = "",
    val pagado: Boolean = false
) : Serializable

// Modelo para la tabla forma_pago
data class FormaPago(
    val id: Long = 0,
    val nombre: String
) : Serializable

// Modelo para la tabla codigos_postales
data class CodigoPostal(
    val codigo: String,
    val localidad: String,
    val provincia: String
) : Serializable