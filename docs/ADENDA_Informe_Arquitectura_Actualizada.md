# Adenda al Informe Técnico — Arquitectura Actualizada

> **Nota para el equipo:** El informe original (`Informe_ProyectoFerreteria_Eva3.pdf`) es un documento PDF sin fuente editable (.docx) disponible en el repositorio, por lo que no puede modificarse directamente. Este documento contiene el texto de reemplazo para las secciones afectadas por la incorporación de API Gateway, Eureka, GlitchTip y 4 microservicios nuevos. Debe incorporarse al documento fuente (Word/Google Docs) antes de regenerar el PDF final.

---

## Reemplazo de la Sección 4 — Arquitectura de Software

El sistema fue desarrollado utilizando una arquitectura de microservicios, donde cada servicio cumple una responsabilidad específica y puede desplegarse de forma independiente. La arquitectura incorpora los siguientes componentes de infraestructura:

- **API Gateway** (puerto 8080): único punto de entrada público al sistema. Reenvía toda solicitud entrante hacia el BFF, resuelto dinámicamente vía Eureka. No implementa lógica de negocio ni seguridad propia.
- **Eureka Server** (puerto 8761): servicio de registro y descubrimiento (Service Discovery). Todos los demás componentes (Gateway, BFF y los 9 microservicios de negocio) se registran en Eureka al arrancar, y se localizan entre sí por nombre lógico en lugar de direcciones fijas.
- **Backend For Frontend (BFF)** (puerto 8090, interno): conserva toda la lógica de orquestación y seguridad. Recibe las solicitudes reenviadas por el Gateway, valida el token JWT mediante el microservicio de autenticación (ms-auth) y redirige la petición al microservicio correspondiente, resuelto vía Eureka + Spring Cloud LoadBalancer (`@LoadBalanced RestClient.Builder`). De esta forma, el cliente no necesita conocer la ubicación ni el funcionamiento interno de cada servicio.

La lógica de negocio se encuentra distribuida en los siguientes **9 microservicios**:

- **ms-auth:** administra el registro de usuarios, autenticación y validación de tokens JWT.
- **ms-product:** gestiona la información de los productos.
- **ms-inventory:** administra el inventario y el control de stock.
- **ms-sales:** registra las ventas y coordina las operaciones relacionadas con el proceso de venta.
- **ms-notification:** administra las notificaciones generadas por el sistema.
- **ms-supplier:** administra el registro de proveedores (nombre, RUT, contacto, estado).
- **ms-purchase:** gestiona órdenes de compra a proveedores; al confirmar una orden, incrementa el stock correspondiente en ms-inventory.
- **ms-report:** agrega información de ventas, inventario y productos para generar reportes de solo lectura (resumen de ventas, top de productos, valorización de inventario, alertas de stock bajo). **No posee base de datos propia**, ya que su función es exclusivamente de agregación en tiempo real sobre datos que ya existen en otros microservicios.
- **ms-pricing:** calcula descuentos por volumen o categoría sobre un precio y cantidad recibidos como entrada, con reglas configurables. **No posee base de datos propia**, dado que es un servicio de cálculo puro sin estado persistente.

Cada microservicio con persistencia fue desarrollado con Spring Boot 3.3.4, posee su propia base de datos PostgreSQL siguiendo el patrón Database per Service, administra sus migraciones mediante Flyway y puede ejecutarse de manera independiente utilizando Docker Compose.

La comunicación entre el Gateway, el BFF y los microservicios —así como entre microservicios (por ejemplo, ms-sales → ms-inventory, ms-purchase → ms-supplier)— se realiza mediante Spring Cloud Netflix Eureka para el descubrimiento de servicios y Spring Cloud LoadBalancer para resolver, en tiempo de ejecución, la instancia real detrás de cada nombre lógico, en lugar de utilizar direcciones fijas.

Adicionalmente, el sistema incorpora **GlitchTip** (autohospedado mediante Docker Compose, compatible con el SDK de Sentry) como plataforma de observabilidad centralizada: el Gateway, el BFF y los 9 microservicios envían allí sus errores y trazas, sin dejar de registrar sus logs por consola mediante SLF4J/Logback.

A continuación, se presentan los diagramas C4 actualizados correspondientes a la arquitectura del sistema (ver README del repositorio para la versión editable en Mermaid).

---

## Reemplazo de la Sección 5 — Base de Datos y Migraciones (Flyway)

El proyecto implementa el patrón Database per Service, donde cada microservicio que requiere persistencia administra su propia base de datos PostgreSQL de forma independiente. Los microservicios de agregación/cálculo puro (ms-report, ms-pricing) **no poseen base de datos**, evitando duplicar información que ya reside en otro microservicio — una extensión consistente del mismo patrón aplicado a los 7 microservicios restantes.

Para el control de versiones del esquema de base de datos se utilizó Flyway, permitiendo gestionar las migraciones de manera ordenada y reproducible. Cada microservicio con persistencia incorpora una migración inicial (V1__...) encargada de crear la estructura necesaria para su funcionamiento, separando estrictamente las instrucciones DDL (creación de tablas) de las DML (datos de prueba/seed), cuando corresponde.

| Microservicio | Base de datos | Migraciones |
|---|---|---|
| ms-auth | auth_db | V1 (DDL) |
| ms-product | product_db | V1 (DDL), V2 (DML — seed de productos) |
| ms-inventory | inventory_db | V1 (DDL), V2 (DML — seed de inventario) |
| ms-sales | sales_db | V1 (DDL) |
| ms-notification | notification_db | V1 (DDL) |
| ms-supplier | supplier_db | V1 (DDL), V2 (DML — seed de proveedores) |
| ms-purchase | purchase_db | V1 (DDL — órdenes de compra e ítems) |
| ms-report | *(sin base de datos)* | — |
| ms-pricing | *(sin base de datos)* | — |

---

## Reemplazo de la Sección 6 — Pruebas Unitarias y Repositorio (GitHub)

Para asegurar el correcto funcionamiento de la aplicación se desarrolló una estrategia de pruebas unitarias en los 12 proyectos que componen el sistema (Eureka, API Gateway, BFF y los 9 microservicios de negocio). Las pruebas fueron implementadas utilizando JUnit 5, Mockito y H2 como base de datos en memoria para los escenarios de prueba, mientras que JaCoCo se empleó para generar reportes de cobertura del código, **con un umbral mínimo del 40% verificado automáticamente en cada build** (tarea `jacocoTestCoverageVerification`).

La estructura de pruebas considera distintos niveles de validación: pruebas de carga del contexto de Spring (ApplicationTests), pruebas de controladores (ControllerTests), pruebas de servicios (ServiceTests), pruebas de repositorios (RepositoryTests, en los microservicios con base de datos) y validaciones de los DTO mediante Bean Validation (DtoValidationTests).

| Proyecto | ApplicationTest | ControllerTest | ServiceTest | RepositoryTest | DTO ValidationTest |
|---|---|---|---|---|---|
| eureka | Sí | — | — | — | — |
| apigateway | Sí | — | — | — | — |
| bff | Sí | Sí (10 controllers) | Sí (9 services) | N/A (sin BD) | Sí |
| ms-auth | Sí | Sí | Sí | Sí | Sí |
| ms-product | Sí | Sí | Sí | Sí | Sí |
| ms-inventory | Sí | Sí | Sí | Sí | Sí |
| ms-sales | Sí | Sí | Sí | Sí | Sí |
| ms-notification | Sí | Sí | Sí | Sí | Sí |
| ms-supplier | Sí | Sí | Sí | Sí | Sí |
| ms-purchase | Sí | Sí | Sí | Sí | Sí |
| ms-report | Sí | Sí | Sí | N/A (sin BD) | N/A |
| ms-pricing | Sí | Sí | Sí | N/A (sin BD) | Sí |

---

## Reemplazo de la Sección 9 — Documentación de API (Swagger/Postman)

Todos los microservicios de negocio, el BFF y el API Gateway incorporan documentación automática de sus endpoints mediante OpenAPI/Swagger. Con la incorporación del API Gateway, el acceso externo al sistema se realiza a través de un único punto de entrada (puerto 8080); el resto de los servicios (BFF y los 9 microservicios) opera en la red interna de Docker y su Swagger UI es accesible directamente en su puerto cuando se ejecutan fuera de contenedor para pruebas locales.

| Servicio | Puerto | Swagger UI (local) |
|---|---|---|
| API Gateway | 8080 | *(forwarder puro, sin endpoints propios que documentar)* |
| BFF | 8090 | http://localhost:8090/swagger-ui.html |
| ms-auth | 8081 | http://localhost:8081/swagger-ui.html |
| ms-product | 8082 | http://localhost:8082/swagger-ui.html |
| ms-inventory | 8083 | http://localhost:8083/swagger-ui.html |
| ms-sales | 8084 | http://localhost:8084/swagger-ui.html |
| ms-notification | 8085 | http://localhost:8085/swagger-ui.html |
| ms-supplier | 8086 | http://localhost:8086/swagger-ui.html |
| ms-purchase | 8087 | http://localhost:8087/swagger-ui.html |
| ms-report | 8088 | http://localhost:8088/swagger-ui.html |
| ms-pricing | 8091 | http://localhost:8091/swagger-ui.html |

---

## Nueva Sección — Observabilidad (Eureka + GlitchTip)

El sistema incorpora dos componentes de observabilidad e infraestructura adicionales a los definidos originalmente:

- **Eureka Server**, que centraliza el registro y descubrimiento de todos los servicios, permitiendo que la comunicación entre ellos se resuelva dinámicamente por nombre lógico en lugar de direcciones fijas — requisito para que el sistema pueda escalar horizontalmente sin reconfiguración manual.
- **GlitchTip**, plataforma de observabilidad autohospedada (compatible con el SDK de Sentry) que centraliza la captura de errores del API Gateway, el BFF y los 9 microservicios. Se despliega junto con el resto del stack mediante Docker Compose, con su propia base de datos PostgreSQL y Redis, sin depender de ningún servicio externo de terceros. Los logs de la aplicación continúan mostrándose por consola mediante SLF4J/Logback de forma simultánea a su envío a GlitchTip.

---

## Ajuste a la Conclusión

Agregar el siguiente párrafo al final de la Conclusión original:

> Posteriormente, la arquitectura fue extendida incorporando un API Gateway como único punto de entrada público, un servidor Eureka para el registro y descubrimiento dinámico de servicios, cuatro microservicios adicionales (ms-supplier, ms-purchase, ms-report y ms-pricing) que amplían el dominio de negocio hacia la gestión de proveedores, compras y reportería, y una plataforma de observabilidad centralizada (GlitchTip) para la captura de errores en producción. Esta evolución mantiene intacta la lógica de negocio original y el patrón Database per Service, demostrando la capacidad del sistema para escalar en número de servicios sin comprometer su mantenibilidad.
