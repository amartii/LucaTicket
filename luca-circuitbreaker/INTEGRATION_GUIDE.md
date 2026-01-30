# 🔧 Guía de Integración - LUCA Circuit Breaker

## Cómo Integrar Circuit Breaker en Tus Servicios

Si deseas que tus servicios usen Circuit Breaker desde luca-circuitbreaker, sigue estos pasos:

### Opción 1: Usar luca-circuitbreaker como Proxy

Si tienes un cliente externo (ej: Angular frontend), puedes apuntar a luca-circuitbreaker:

```typescript
// Antes: Llamada directa
this.http.get('http://localhost:8081/eventos')

// Después: Llamada a través de Circuit Breaker
this.http.get('http://localhost:8083/api/eventos')
```

**Ventajas:**

- ✅ Protección centralizada
- ✅ Un único punto de configuración
- ✅ Logs centralizados

### Opción 2: Integración Desde Otra Microservicio

Si quieres que otra microservicio use Circuit Breaker:

#### Paso 1: Agregar Dependencias

En el `pom.xml` del servicio que llama:

```xml
<!-- OpenFeign -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- Resilience4j -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>

<!-- AOP -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
```

#### Paso 2: Crear Feign Client

```java
@FeignClient(name = "luca-circuitbreaker")
public interface CircuitBreakerFeignClient {
  
    @GetMapping("/api/eventos/{id}")
    Object getEvento(@PathVariable Long id);
  
    @GetMapping("/api/compras")
    Object getAllCompras();
}
```

#### Paso 3: Usar en Service

```java
@Service
public class MyService {
  
    @Autowired
    private CircuitBreakerFeignClient client;
  
    public Object getProtectedEvento(Long id) {
        return client.getEvento(id);
    }
}
```

---

## Flujo de Comunicación de Servicios

```
┌─────────────────────────────────────┐
│  luca-gateway (8282)                │ ← Enrutamiento inicial
└──────────────┬──────────────────────┘
               │
        ┌──────┴──────┐
        ↓             ↓
    ┌───────────────┐ ┌──────────────┐
    │eventos-service│ │compra-service│
    │(8081)         │ │(8082)        │
    └──────┬────────┘ └──────┬───────┘
           │                 │
           │ (opcional)      │ (opcional)
           └────────┬────────┘
                    ↓
        ┌────────────────────────────┐
        │luca-circuitbreaker (8083)  │ ← Protección inteligente
        └────────────────────────────┘
```

---

## Configuración en Diferentes Escenarios

### Escenario 1: Desarrollo Local

Todos los servicios en localhost:

```yaml
# application.yml en luca-circuitbreaker
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Escenario 2: Docker Compose

```yaml
version: '3.8'
services:
  eureka-server:
    image: openjdk:21-jdk
    ports:
      - "8761:8761"
  
  eventos-service:
    image: openjdk:21-jdk
    environment:
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
  
  luca-circuitbreaker:
    image: openjdk:21-jdk
    environment:
      EUREKA_CLIENT_SERVICEURL_DEFAULTZONE: http://eureka-server:8761/eureka/
    ports:
      - "8083:8083"
```

### Escenario 3: Producción (Cloud)

```properties
# application-prod.properties
eureka.client.service-url.defaultZone=https://eureka-prod.mycompany.com/eureka/
management.endpoints.web.exposure.include=health,info,metrics
```

---

## Testing del Circuit Breaker

### Test 1: Simular Servicio Caído

```bash
# Terminal 1: Iniciar luca-circuitbreaker
mvn spring-boot:run -pl luca-circuitbreaker

# Terminal 2: Apagar eventos-service (si está corriendo)
# Detener el proceso manualmente

# Terminal 3: Hacer llamada
curl -X GET http://localhost:8083/api/eventos

# Resultado esperado:
# {
#   "error": "Servicio de eventos no disponible en este momento"
# }
```

### Test 2: Verificar Recuperación

```bash
# Volver a iniciar eventos-service
mvn spring-boot:run -pl eventos-service

# Esperar 5 segundos (waitDurationInOpenState)

# Hacer llamada nuevamente
curl -X GET http://localhost:8083/api/eventos

# Resultado esperado: Funcionamiento normal
```

### Test 3: Monitorear Estado

```bash
# En una terminal, monitorear cada 2 segundos
watch -n 2 'curl -s http://localhost:8083/actuator/circuitbreakers | jq'

# En otra terminal, generar carga
for i in {1..100}; do
  curl -X GET http://localhost:8083/api/eventos &
done
```

---

## Logs Importantes

### Ver logs de Circuit Breaker

```bash
# En tiempo real
tail -f target/luca-circuitbreaker.log

# Buscar errores específicos
grep "CircuitBreaker" target/luca-circuitbreaker.log
grep "fallback" target/luca-circuitbreaker.log
```

### Ejemplo de logs

```
2026-01-30 10:15:23 [INFO ] Obteniendo evento con ID: 1
2026-01-30 10:15:24 [ERROR] Circuit Breaker activado. Servicio no disponible
2026-01-30 10:15:29 [INFO ] Transición a HALF_OPEN
2026-01-30 10:15:30 [INFO ] Circuito cerrado - servicio recuperado
```

---

## Ajuste Fino de Configuración

### Para servicios críticos (baja tolerancia a fallos)

```yaml
eventosServiceCB:
  failureRateThreshold: 30          # Más sensible
  waitDurationInOpenState: 10s      # Espera más
  minimumNumberOfCalls: 10          # Evalúa más llamadas
```

### Para servicios tolerantes (alta tolerancia a fallos)

```yaml
compraServiceCB:
  failureRateThreshold: 70          # Menos sensible
  waitDurationInOpenState: 3s       # Espera menos
  minimumNumberOfCalls: 2           # Evalúa menos llamadas
```

---

## Monitoreo en Tiempo Real

### Dashboard de Métricas

```bash
# Ver todas las métricas
curl -s http://localhost:8083/actuator/metrics | jq

# Ver métricas específicas
curl -s http://localhost:8083/actuator/metrics/resilience4j.circuitbreaker.calls | jq

# Ver estado de circuitos
curl -s http://localhost:8083/actuator/circuitbreakers | jq
```

### Integración con Prometheus (opcional)

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

Luego acceder a: `http://localhost:8083/actuator/prometheus`

---

## Troubleshooting de Integración

### Error: "FeignException: 503 Service Unavailable"

Significa que el circuit breaker está OPEN. Verificar:

```bash
# 1. Eureka status
curl http://localhost:8761

# 2. Servicio está registrado
curl http://localhost:8761/eureka/apps/eventos-service

# 3. Circuit breaker state
curl http://localhost:8083/actuator/circuitbreakers
```

### Error: "Cannot resolve host name"

Verificar conectividad:

```bash
# Desde luca-circuitbreaker
ping eventos-service
curl http://eventos-service:8081/eventos
```

### Lentitud en respuestas

Ajustar timeouts:

```yaml
resilience4j:
  timelimiter:
    instances:
      eventosServiceCB:
        timeout-duration: 10s  # Aumentar de 5s
```


---
