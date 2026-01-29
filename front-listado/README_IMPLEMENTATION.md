# Front-Listado - Documentación de Implementación

## Descripción General

**Front-Listado** es la aplicación frontend del proyecto **LucaTicket**, una plataforma de venta de entradas para eventos. Esta aplicación Angular 17 proporciona una interfaz moderna y responsiva para:

- Visualizar un listado de eventos disponibles
- Expandir detalles de cada evento
- Navegar hacia la sección de compra de entradas
- Gestionar favoritos

---

## Estructura del Proyecto

```
front-listado/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── listado-eventos/        # Componente principal de listado
│   │   │   ├── compras/                # Componente de compra de entradas
│   │   │   └── favoritos/              # Componente de eventos favoritos
│   │   ├── services/
│   │   │   └── eventos.service.ts      # Servicio HTTP para obtener eventos
│   │   ├── models/
│   │   │   └── evento.ts               # Interface del modelo Evento
│   │   ├── app.ts                      # Componente raíz
│   │   ├── app.routes.ts               # Configuración de rutas
│   │   ├── app.config.ts               # Configuración de la aplicación
│   │   ├── app.html                    # Template principal
│   │   └── app.scss                    # Estilos globales
│   ├── main.ts                         # Punto de entrada
│   └── index.html                      # HTML base
├── public/
│   └── assets/
│       └── eventos/                    # Imágenes de eventos (evento01.png - evento12.png)
├── package.json
└── angular.json
```

---

## Componentes Principales

### 1. **ListadoEventosComponent**

**Ubicación:** `src/app/components/listado-eventos/`

El componente principal que gestiona la visualización de eventos en un grid responsivo.

#### Características:

- **Grid Layout:** 2 columnas en todos los dispositivos
- **Acordeón Expandible:** Solo un evento se expande a la vez, ocupando 2 columnas de ancho
- **Mapeo de Imágenes:** Cada evento muestra su imagen correspondiente (evento01.png a evento12.png)
- **Información Expandible:** Al expandir un evento, se muestran:
  - Imagen del evento
  - Descripción completa
  - Fecha y hora
  - Localidad y recinto
  - Precio mínimo y máximo

#### Métodos Clave:

```typescript
toggleEvento(id: number): void
// Alterna entre expandir y colapsar un evento

getImagenEvento(id: number): string
// Mapea el ID del evento a su imagen correspondiente (ej: ID 1 → evento01.png)

cargarEventos(): void
// Obtiene los eventos del backend mediante el servicio

irAComprar(): void
// Navega al componente de compras

irAFavoritos(): void
// Navega al componente de favoritos
```

### 2. **ComprasComponent**

**Ubicación:** `src/app/components/compras/`

Componente para la compra de entradas (en desarrollo).

### 3. **FavoritosComponent**

**Ubicación:** `src/app/components/favoritos/`

Componente para gestionar eventos favoritos (en desarrollo).

---

## Servicios

### **EventosService**

**Ubicación:** `src/app/services/eventos.service.ts`

Servicio HTTP que gestiona la comunicación con el backend.

#### Métodos:

```typescript
getEventos(): Observable<Evento[]>
// Obtiene la lista completa de eventos desde http://localhost:8081/eventos
```

**Configuración:**

- **Base URL:** `http://localhost:8081/eventos`
- **Método:** GET
- **Respuesta:** Array de objetos `Evento`

---

## Modelos

### **Evento Interface**

**Ubicación:** `src/app/models/evento.ts`

```typescript
export interface Evento {
  id: number;              // Identificador único
  nombre: string;          // Nombre del evento
  descripcion: string;     // Descripción detallada
  fechaEvento: string;     // Fecha en formato string
  horaEvento: string;      // Hora en formato string
  precioMinimo: number;    // Precio de entrada más baja
  precioMaximo: number;    // Precio de entrada más alta
  localidad: string;       // Ciudad/localidad del evento
  genero: string;          // Género/categoría del evento
  recinto: string;         // Nombre del recinto/sala
  foto: string;            // Nombre del archivo de foto
}
```

---

## Diseño y Estilos

### **Tema de Color**

- **Fondo oscuro:** `#0f1419`
- **Cards:** `#1a1f2e`
- **Acento primario:** `#4a7cff` (azul)
- **Acento secundario:** `#357abd`
- **Texto primario:** `#e8eef5` (blanco)
- **Texto secundario:** `#a8b5cc` (gris)

### **Layout Responsivo**

- **Grid de eventos:** 2 columnas en todos los dispositivos
- **Cards expandibles:** Ocupan 2 columnas cuando se expanden
- **Animaciones:** Transiciones suaves en expansión/colapso
- **Overflow visible:** Permite que el contenido expandido no se corte

### **Animaciones**

- **fadeInUp:** Entrada del listado de eventos
- **slideIn:** Entrada individual de cada evento
- **slideDown:** Expansión del contenido del evento
- **pageEnter:** Transición entre páginas

---

## Flujo de Datos

```
┌─────────────────────────────────────────────────────────────┐
│                    ListadoEventosComponent                  │
│                                                             │
│  1. ngOnInit() → cargarEventos()                            │
│  2. EventosService.getEventos() → HTTP GET                 │
│  3. Backend (http://localhost:8081/eventos) responde       │
│  4. Array de eventos se almacena en this.eventos           │
│  5. ChangeDetectorRef.detectChanges() actualiza el view    │
│  6. Template renderiza el grid de eventos                  │
│                                                             │
│  Usuario interactúa:                                        │
│  7. Click en evento → toggleEvento(id)                      │
│  8. eventoExpandido cambia → Template reacciona            │
│  9. evento-card recibe clase .expanded                      │
│  10. grid-column: span 2 hace que ocupe 2 columnas         │
└─────────────────────────────────────────────────────────────┘
```

---

## Cómo Ejecutar

### **Requisitos:**

- Node.js 18+ instalado
- Backend EventosService ejecutándose en `http://localhost:8081`

### **Instalación:**

```bash
cd front-listado
npm install
```

### **Desarrollo:**

```bash
npm start
# La aplicación se abrirá en http://localhost:4200
```

### **Build para producción:**

```bash
npm run build
# Los archivos compilados estarán en dist/front-listado
```

---

## Características Principales

### **1. Grid Responsivo**

- 2 columnas en todos los dispositivos
- Diseño limpio y organizado
- Máxima utilización del espacio

### **2. Acordeón Expandible**

- Solo un evento expandido a la vez
- El evento expandido ocupa 2 columnas de ancho
- Los eventos adyacentes no se ven afectados
- Transiciones suaves

### **3. Mapeo Automático de Imágenes**

- Cada evento ID se mapea a su imagen correspondiente
- ID 1 → evento01.png
- ID 2 → evento02.png
- ... hasta ID 12 → evento12.png

### **4. Información Detallada**

Al expandir un evento se muestra:

- Imagen del evento
- Descripción completa
- Fecha y hora
- Localidad y recinto
- Rango de precios
- Botones de acción (Comprar, Favoritos)

### **5. Navegación**

- Botón "Comprar Entradas" → Navega a `/compras`
- Botón "Añadir a Favoritos" → Navega a `/favoritos`
- Navbar para cambiar entre secciones

### **6. Tema Oscuro**

- Interfaz moderna con tema oscuro
- Buena legibilidad
- Reduce fatiga visual

---

## 🔧 Tecnologías Utilizadas

| Tecnología                  | Versión    | Uso                       |
| ---------------------------- | ----------- | ------------------------- |
| **Angular**            | 17+         | Framework frontend        |
| **TypeScript**         | 5+          | Lenguaje de programación |
| **RxJS**               | Observables | Manejo de peticiones HTTP |
| **SCSS**               | Estilos     | Diseño y responsividad   |
| **Bootstrap/CSS Grid** | Layout      | Sistema de grid           |
| **HttpClient**         | Angular     | Comunicación HTTP        |

---

## Integración con Backend

### **Endpoint Utilizado:**

```
GET http://localhost:8081/eventos
```

### **Respuesta Esperada:**

```json
[
  {
    "id": 1,
    "nombre": "Concierto de Rock",
    "descripcion": "Gran evento musical...",
    "fechaEvento": "2024-02-15",
    "horaEvento": "20:00",
    "precioMinimo": 25.00,
    "precioMaximo": 85.00,
    "localidad": "Madrid",
    "genero": "Rock",
    "recinto": "Palacio Vistalegre",
    "foto": "evento01.png"
  },
  ...
]
```

---

## Troubleshooting

### **Los eventos no aparecen**

- Verifica que el backend está corriendo en `http://localhost:8081`
- Abre la consola (F12) y busca errores de CORS
- Revisa que `EventosService` tiene la URL correcta

### **Las imágenes no se cargan**

- Verifica que las imágenes están en `public/assets/eventos/`
- Nombres deben ser: `evento01.png`, `evento02.png`, etc.
- Revisa la consola para errores 404

### **El grid no se ve como esperado**

- Borra caché del navegador (Ctrl+Shift+R)
- Verifica que el SCSS se compiló correctamente
- Abre DevTools y revisa los estilos aplicados

---

## Notas de Implementación

### **ChangeDetectorRef**

Se utiliza `ChangeDetectorRef.detectChanges()` después de recibir los datos del servicio para asegurar que Angular detecte los cambios y actualice la vista.

### **Mapeo de Imágenes**

El método `getImagenEvento()` utiliza `padStart(2, '0')` para garantizar que los IDs se conviertan al formato correcto:

- 1 → "01"
- 12 → "12"

### **Grid CSS**

```scss
.eventos-accordion {
  display: grid;
  grid-template-columns: repeat(2, 1fr);  // 2 columnas iguales
  gap: 1rem;
}

.evento-card.expanded {
  grid-column: span 2;  // Ocupa 2 columnas cuando se expande
}
```

---

## Desarrollo Futuro

- [ ] Implementar carrito de compras
- [ ] Sistema de favoritos persistente
- [ ] Filtrado y búsqueda de eventos
- [ ] Paginación
- [ ] Validación de formularios
- [ ] Integración con pasarela de pagos
