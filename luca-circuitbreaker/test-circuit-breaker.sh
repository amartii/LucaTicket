#!/bin/bash

# ============================================
# LUCA Circuit Breaker - Script de Pruebas
# ============================================
# Ejecuta este script para probar todos los endpoints
# Usage: bash test-circuit-breaker.sh

set -e

# Colores para output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# URL base
BASE_URL="http://localhost:8083/api"
HEALTH_URL="http://localhost:8083/api/health"

# Función para imprimir títulos
print_title() {
    echo -e "\n${BLUE}========================================${NC}"
    echo -e "${BLUE}$1${NC}"
    echo -e "${BLUE}========================================${NC}\n"
}

# Función para imprimir resultados
print_success() {
    echo -e "${GREEN}✓ $1${NC}"
}

print_error() {
    echo -e "${RED}✗ $1${NC}"
}

print_info() {
    echo -e "${YELLOW}ℹ $1${NC}"
}

# ============================================
# TEST 1: Health Check
# ============================================
print_title "TEST 1: Health Check"

print_info "Verificando que el servicio está activo..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$HEALTH_URL")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

if [ "$HTTP_CODE" = "200" ]; then
    print_success "Health check OK (HTTP 200)"
    echo "Respuesta: $BODY"
else
    print_error "Health check falló (HTTP $HTTP_CODE)"
    exit 1
fi

# ============================================
# TEST 2: Obtener todos los eventos
# ============================================
print_title "TEST 2: GET /api/eventos"

print_info "Intentando obtener todos los eventos..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/eventos")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "200" ]; then
    print_success "Obtención de eventos exitosa"
elif [ "$HTTP_CODE" = "503" ]; then
    print_info "Servicio de eventos no disponible (esperado si el servicio está caído)"
else
    print_error "Error inesperado (HTTP $HTTP_CODE)"
fi

# ============================================
# TEST 3: Obtener evento por ID
# ============================================
print_title "TEST 3: GET /api/eventos/{id}"

print_info "Intentando obtener evento con ID 1..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/eventos/1")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

# ============================================
# TEST 4: Crear evento
# ============================================
print_title "TEST 4: POST /api/eventos"

EVENT_DATA='{
  "nombre": "Test Concert",
  "fecha": "2026-06-15",
  "ubicacion": "Madrid"
}'

print_info "Creando nuevo evento..."
print_info "Payload: $EVENT_DATA"

RESPONSE=$(curl -s -w "\n%{http_code}" \
  -X POST "$BASE_URL/eventos" \
  -H "Content-Type: application/json" \
  -d "$EVENT_DATA")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "200" ]; then
    print_success "Evento creado exitosamente"
elif [ "$HTTP_CODE" = "503" ]; then
    print_info "Servicio no disponible (esperado)"
fi

# ============================================
# TEST 5: Obtener todas las compras
# ============================================
print_title "TEST 5: GET /api/compras"

print_info "Intentando obtener todas las compras..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/compras")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

# ============================================
# TEST 6: Obtener compra por ID
# ============================================
print_title "TEST 6: GET /api/compras/{id}"

print_info "Intentando obtener compra con ID 1..."
RESPONSE=$(curl -s -w "\n%{http_code}" "$BASE_URL/compras/1")
HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

# ============================================
# TEST 7: Procesar compra
# ============================================
print_title "TEST 7: POST /api/compras"

PURCHASE_DATA='{
  "monto": 199.99,
  "metodo_pago": "tarjeta",
  "evento_id": 1
}'

print_info "Procesando nueva compra..."
print_info "Payload: $PURCHASE_DATA"

RESPONSE=$(curl -s -w "\n%{http_code}" \
  -X POST "$BASE_URL/compras" \
  -H "Content-Type: application/json" \
  -d "$PURCHASE_DATA")

HTTP_CODE=$(echo "$RESPONSE" | tail -n1)
BODY=$(echo "$RESPONSE" | head -n-1)

echo "HTTP Code: $HTTP_CODE"
echo "Response:"
echo "$BODY" | jq '.' 2>/dev/null || echo "$BODY"

if [ "$HTTP_CODE" = "201" ] || [ "$HTTP_CODE" = "200" ]; then
    print_success "Compra procesada exitosamente"
elif [ "$HTTP_CODE" = "503" ]; then
    print_info "Servicio no disponible (esperado)"
fi

# ============================================
# TEST 8: Actuator - Health Endpoints
# ============================================
print_title "TEST 8: Actuator - Health Details"

print_info "Obteniendo detalles de salud..."
RESPONSE=$(curl -s "$HEALTH_URL/probes" 2>/dev/null || curl -s "http://localhost:8083/actuator/health")

echo "Response:"
echo "$RESPONSE" | jq '.' 2>/dev/null || echo "$RESPONSE"

# ============================================
# TEST 9: Circuit Breaker Status
# ============================================
print_title "TEST 9: Circuit Breaker Status"

print_info "Obteniendo estado de los circuit breakers..."
RESPONSE=$(curl -s "http://localhost:8083/actuator/circuitbreakers")

echo "Response:"
echo "$RESPONSE" | jq '.' 2>/dev/null || echo "$RESPONSE"

# ============================================
# TEST 10: Métricas Resilience4j
# ============================================
print_title "TEST 10: Resilience4j Metrics"

print_info "Obteniendo métricas..."
RESPONSE=$(curl -s "http://localhost:8083/actuator/metrics/resilience4j.circuitbreaker.calls.total")

echo "Response:"
echo "$RESPONSE" | jq '.' 2>/dev/null || echo "$RESPONSE"

# ============================================
# RESUMEN
# ============================================
print_title "RESUMEN DE PRUEBAS COMPLETADO"

print_info "Todos los tests han finalizado."
print_info "Si algún test muestra HTTP 503, significa que el servicio correspondiente no está disponible."
print_info "Asegúrate de tener ejecutando:"
print_info "  - eureka-server (puerto 8761)"
print_info "  - eventos-service (puerto 8081)"
print_info "  - compra-service (puerto 8082)"
print_info "  - luca-circuitbreaker (puerto 8083)"

echo ""
print_success "Script de pruebas completado"
