# 📖 Quick Reference - LUCA Circuit Breaker

## Comandos Rápidos

### Compilar y Ejecutar

```bash
# Compilar
mvn clean install -pl luca-circuitbreaker

# Ejecutar
mvn spring-boot:run -pl luca-circuitbreaker

# Con perfil específico
mvn spring-boot:run -pl luca-circuitbreaker -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Probar Endpoints (CURL)

```bash
# Health check
curl http://localhost:8083/api/health

# Obtener eventos
curl http://localhost:8083/api/eventos

# Obtener evento por ID
curl http://localhost:8083/api/eventos/1

# Crear evento
curl -X POST http://localhost:8083/api/eventos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test","fecha":"2026-06-15"}'

# Obtener compras
curl http://localhost:8083/api/compras

# Procesar compra
curl -X POST http://localhost:8083/api/compras \
  -H "Content-Type: application/json" \
  -d '{"monto":199.99,"metodo_pago":"tarjeta"}'
```

### Monitoreo

```bash
# Health detallado
curl http://localhost:8083/actuator/health | jq

# Circuit breakers
curl http://localhost:8083/actuator/circuitbreakers | jq

# Métricas
curl http://localhost:8083/actuator/metrics | jq

# Estado de resilience4j
curl http://localhost:8083/actuator/metrics/resilience4j.circuitbreaker.state | jq

# Logs en tiempo real
tail -f target/logs/luca-circuitbreaker.log
```

---

## Checklist de Inicio

- [ ] Eureka Server ejecutándose (puerto 8761)
- [ ] eventos-service registrado en Eureka
- [ ] compra-service registrado en Eureka
- [ ] luca-circuitbreaker compilado: `mvn clean install`
- [ ] luca-circuitbreaker ejecutándose (puerto 8083)
- [ ] Health check OK: `curl http://localhost:8083/api/health`

---

## Configuración Esencial

### application.properties

```properties
spring.application.name=luca-circuitbreaker
server.port=8083
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/
management.endpoints.web.exposure.include=*
```

### application.yaml (Snippet)

```yaml
resilience4j:
  circuitbreaker:
    instances:
      eventosServiceCB:
        failureRateThreshold: 50
        waitDurationInOpenState: 5s
      compraServiceCB:
        failureRateThreshold: 50
        waitDurationInOpenState: 5s
```

---

## Estructura de Carpetas

```
src/main/
├── java/com/lucatic/circuitbreaker/
│   ├── LucaCircuitBreakerApplication.java
│   ├── config/          → CircuitBreakerConfig.java
│   ├── controller/      → CircuitBreakerController.java
│   ├── feign/           → FeignClients
│   └── service/         → ServiceWrappers
└── resources/
    ├── application.properties
    └── application.yaml
```

---

## Estados del Circuit Breaker

| Estado | Significado | Acción |
|--------|-------------|--------|
| 🟢 CLOSED | Funcionando normal | Permite todas las llamadas |
| 🔴 OPEN | Servicio caído | Rechaza llamadas, usa fallback |
| 🟡 HALF_OPEN | Recuperándose | Permite llamadas de prueba |

---

## HTTP Status Codes

| Code | Significado |
|------|-------------|
| 200 ✓ | OK - Servicio disponible |
| 201 ✓ | Created - Recurso creado |
| 503 ✗ | Service Unavailable - Circuit breaker activado |
| 404 ✗ | Not Found - Endpoint no existe |

---

## Troubleshooting Rápido

### El servicio no inicia

```bash
# Ver logs
mvn clean spring-boot:run -pl luca-circuitbreaker | grep ERROR

# Verificar puerto
lsof -i :8083
```

### Circuit breaker siempre OPEN

```bash
# 1. Verificar Eureka
curl http://localhost:8761/eureka/apps

# 2. Ver estado del CB
curl http://localhost:8083/actuator/circuitbreakers | jq

# 3. Aumentar waitDurationInOpenState en application.yaml
waitDurationInOpenState: 10s  # aumentar a 10 segundos
```

### Feign client no encuentra el servicio

```bash
# Verificar que el servicio está en Eureka
curl http://localhost:8761/eureka/apps/eventos-service

# Verificar nombre de la aplicación
# En eventos-service debe estar: spring.application.name=eventos-service
```

---

## Logs Importantes

```bash
# Buscar errores
grep ERROR target/logs/luca-circuitbreaker.log

# Ver transiciones de estado
grep "CLOSED\|OPEN\|HALF_OPEN" target/logs/luca-circuitbreaker.log

# Ver fallbacks activados
grep "fallback\|Circuit Breaker" target/logs/luca-circuitbreaker.log
```

---

## Dependencias Clave

```xml
spring-cloud-starter-netflix-eureka-client
spring-cloud-starter-openfeign
spring-cloud-starter-circuitbreaker-resilience4j
spring-boot-starter-aop
spring-boot-starter-actuator
```

---

## Ports Rápida Referencia

| Servicio | Puerto | URL |
|----------|--------|-----|
| Eureka | 8761 | http://localhost:8761 |
| eventos-service | 8081 | http://localhost:8081 |
| compra-service | 8082 | http://localhost:8082 |
| **luca-circuitbreaker** | **8083** | **http://localhost:8083** |
| luca-gateway | 8282 | http://localhost:8282 |

---

## Accesos Útiles

```
Health: http://localhost:8083/api/health
Eureka: http://localhost:8761
Actuator: http://localhost:8083/actuator
Métricas: http://localhost:8083/actuator/metrics
CB Status: http://localhost:8083/actuator/circuitbreakers
```

---

## Notas de Desarrollo

- Circuit Breaker está configurado con **slidingWindowSize: 10**
- Los fallbacks retornan **null** o mensajes de error
- Todos los endpoints usan **Eureka Service Discovery**
- Las métricas se exportan en formato **Prometheus**

---

**Última actualización:** Enero 2026
