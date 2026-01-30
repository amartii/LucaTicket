# API Gateway LucaTicket - Resumen de Configuración

## Estado: COMPLETADO

He configurado completamente el API Gateway para el sistema LucaTicket.

---

## Cambios Realizados

### **pom.xml**

| Campo                 | Antes                        | Después                        |
| --------------------- | ---------------------------- | ------------------------------- |
| **GroupId**     | `com.ejemplos.spring`      | `com.lucatic`                 |
| **ArtifactId**  | `Spring19e-Gateway`        | `luca-gateway`                |
| **Name**        | `Spring19e-Gateway`        | `luca-gateway`                |
| **Description** | `proyecto WEB con SP Boot` | `API Gateway para LucaTicket` |

**Dependencias verificadas:**

- `spring-cloud-starter-gateway` ✓
- `spring-cloud-starter-netflix-eureka-client` ✓
- `spring-cloud-starter` ✓
- `spring-boot-starter-actuator` ✓

---

### **Clase Principal**

**Archivo:** `src/main/java/com/lucatic/gateway/LucaGatewayApplication.java`

```java
package com.lucatic.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class LucaGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(LucaGatewayApplication.class, args);
    }
}
```

---

### **application.yml** (ACTUALIZADO)

**Ubicación:** `src/main/resources/application.yml`

```yaml
server:
  port: 8282

spring:
  application:
    name: luca-gateway
  config:
    import: "optional:configserver:"
  cloud:
    gateway:
      routes:
        - id: eventos-service
          uri: lb://eventos-service
          predicates:
            - Path=/eventos/**
          filters:
            - StripPrefix=0
        - id: compra-service
          uri: lb://compra-service
          predicates:
            - Path=/pasarela/**
          filters:
            - StripPrefix=0

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka
    register-with-eureka: true
    fetch-registry: true
```

---

### **Rutas Configuradas**

| Ruta en Gateway                           | Método             | Destino                | Puerto Gateway |
| ----------------------------------------- | ------------------- | ---------------------- | -------------- |
| `http://localhost:8282/eventos`         | GET/POST/PUT/DELETE | eventos-service (8081) | **8282** |
| `http://localhost:8282/eventos/{id}`    | GET/DELETE          | eventos-service (8081) | **8282** |
| `http://localhost:8282/pasarela/compra` | POST                | compra-service (8082)  | **8282** |

---

## Arquitectura

```
┌─────────────────────┐
│   Cliente HTTP      │
│   (puerto 8080)     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│   luca-gateway      │
│   (API Gateway)     │
└──────────┬──────────┘
           │
    ┌──────┴──────┐
    │             │
    ▼             ▼
┌──────────┐  ┌──────────┐
│ eventos- │  │ compra-  │
│ service  │  │ service  │
└──────────┘  └──────────┘
    │             │
    └──────┬──────┘
           ▼
    ┌─────────────────┐
    │ Eureka Server   │
    │ :8761/eureka    │
    └─────────────────┘
```
