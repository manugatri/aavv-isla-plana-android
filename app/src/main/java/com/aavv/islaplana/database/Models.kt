package com.aavv.islaplana.database

import java.io.Serializable

// Modelo para la tabla socios - ESTRUCTURA REAL DEL PC
data class Socio(
    val id: Int = 0,
    val numeroSocio: Int = 0,
    val nombre: String = "",
    val apellidos: String = "",
    val dni: String = "",
    val direccion: String = "",
    val poblacion: String = "",
    val codigoPostal: String = "",
    val telefono: String = "",
    val formaPago: String = "",
    val fechaAlta: String = "",
    val fechaBaja: String = "",
    val email: String = "",
    val enAlta: Boolean = true
) : Serializable

// Modelo para la tabla pagos - ESTRUCTURA REAL DEL PC
data class Pago(
    val id: Int = 0,
    val fecha: String = "",
    val importe: Double = 0.0,
    val idNumeroSocio: Int = 0,
    val nombreSocio: String = ""
) : Serializable

// Modelo para la tabla forma_pago
data class FormaPago(
    val id: Int = 0,
    val nombre: String = ""
) : Serializable

// Modelo para la tabla codigos_postales  
data class CodigoPostal(
    val id: Int = 0,
    val codigo: String = ""
) : Serializable