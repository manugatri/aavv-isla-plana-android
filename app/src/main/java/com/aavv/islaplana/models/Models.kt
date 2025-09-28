package com.aavv.islaplana.models

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
) {
    fun getNombreCompleto(): String {
        return "$nombre $apellidos".trim()
    }
    
    fun getEstado(): String {
        return if (enAlta) "Activo" else "Baja"
    }
    
    fun getDisplayInfo(): String {
        return "👤 ${getNombreCompleto()} - ${getEstado()}"
    }
}

data class Pago(
    val id: Int = 0,
    val fecha: String = "",
    val importe: Double = 0.0,
    val idNumeroSocio: Int = 0,
    val nombreSocio: String = ""
) {
    fun getDisplayInfo(): String {
        return "$fecha - €${String.format("%.2f", importe)} - $nombreSocio"
    }
}

data class FormaPago(
    val id: Int = 0,
    val formaPago: String = ""
)

data class CodigoPostal(
    val id: Int = 0,
    val codigo: String = ""
)