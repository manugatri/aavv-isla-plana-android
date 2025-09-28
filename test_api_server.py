#!/usr/bin/env python3
"""
Servidor de prueba temporal para API de sincronización
Simula los endpoints necesarios para que la app Android pueda sincronizar
"""

from flask import Flask, jsonify, request
from flask_cors import CORS
import json
import datetime

app = Flask(__name__)
CORS(app)

# Datos de prueba
test_socios = [
    {
        "id": 1,
        "numero_socio": "001",
        "nombre": "Juan",
        "apellidos": "García López",
        "dni": "12345678A",
        "direccion": "Calle Mayor 1",
        "poblacion": "Isla Plana",
        "codigo_postal": "30868",
        "telefono": "968123456",
        "email": "juan@email.com",
        "activo": True,
        "fecha_alta": "2024-01-15",
        "observaciones": "Socio fundador"
    },
    {
        "id": 2,
        "numero_socio": "002",
        "nombre": "María", 
        "apellidos": "Sánchez Ruiz",
        "dni": "87654321B",
        "direccion": "Avenida del Mar 25",
        "poblacion": "Isla Plana",
        "codigo_postal": "30868", 
        "telefono": "968654321",
        "email": "maria@email.com",
        "activo": True,
        "fecha_alta": "2024-02-20",
        "observaciones": ""
    }
]

test_pagos = [
    {
        "id": 1,
        "socio_id": 1,
        "fecha_pago": "2024-03-15",
        "importe": 25.00,
        "concepto": "Cuota Marzo 2024",
        "forma_pago": "Efectivo"
    },
    {
        "id": 2,
        "socio_id": 2,
        "fecha_pago": "2024-03-18", 
        "importe": 25.00,
        "concepto": "Cuota Marzo 2024",
        "forma_pago": "Transferencia"
    }
]

@app.route('/api/health', methods=['GET'])
def health_check():
    return jsonify({
        "status": "ok",
        "timestamp": datetime.datetime.now().isoformat(),
        "message": "Servidor de prueba API funcionando"
    })

@app.route('/api/socios', methods=['GET'])
def get_socios():
    return jsonify({
        "success": True,
        "data": test_socios,
        "count": len(test_socios)
    })

@app.route('/api/pagos', methods=['GET'])
def get_pagos():
    return jsonify({
        "success": True,
        "data": test_pagos,
        "count": len(test_pagos)
    })

@app.route('/api/sync', methods=['POST'])
def sync_data():
    data = request.get_json()
    print(f"Datos recibidos para sincronización: {json.dumps(data, indent=2)}")
    
    return jsonify({
        "success": True,
        "message": "Sincronización completada correctamente",
        "socios_sincronizados": len(test_socios),
        "pagos_sincronizados": len(test_pagos),
        "timestamp": datetime.datetime.now().isoformat()
    })

@app.route('/api/formas_pago', methods=['GET'])
def get_formas_pago():
    formas = [
        {"id": 1, "nombre": "Efectivo"},
        {"id": 2, "nombre": "Transferencia"},
        {"id": 3, "nombre": "Tarjeta"},
        {"id": 4, "nombre": "Domiciliación"}
    ]
    return jsonify({
        "success": True,
        "data": formas
    })

@app.route('/api/codigos_postales', methods=['GET'])
def get_codigos_postales():
    codigos = [
        {"id": 1, "codigo": "30868", "poblacion": "Isla Plana"},
        {"id": 2, "codigo": "30390", "poblacion": "Cartagena"},
        {"id": 3, "codigo": "30120", "poblacion": "El Palmar"}
    ]
    return jsonify({
        "success": True,
        "data": codigos
    })

if __name__ == '__main__':
    print("🚀 Iniciando servidor de prueba API...")
    print("📡 Endpoints disponibles:")
    print("   GET  /api/health - Estado del servidor")
    print("   GET  /api/socios - Lista de socios")
    print("   GET  /api/pagos - Lista de pagos")
    print("   POST /api/sync - Sincronización de datos")
    print("   GET  /api/formas_pago - Formas de pago")
    print("   GET  /api/codigos_postales - Códigos postales")
    print("🌐 Servidor funcionando en: http://localhost:5001")
    
    app.run(host='0.0.0.0', port=5001, debug=True)