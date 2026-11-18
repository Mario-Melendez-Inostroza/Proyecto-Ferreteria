# Decisiones — Sistema de Gestión para Ferretería

Registro corto de decisiones de diseño y su motivo.

## 1. Microservicios por dominio
Productos, inventario, ventas, compras y demás evolucionan por separado y pueden fallar sin tumbar todo el sistema. Costo asumido: más piezas que operar.

## 2. API Gateway + BFF
El gateway es la única entrada; el BFF adapta las respuestas a lo que necesita el cliente y concentra las llamadas a los servicios.

## 3. Eureka para descubrimiento de servicios
Los servicios se registran y se encuentran por nombre, sin direcciones fijas.

## 4. Database per service
Cada servicio es dueño de sus datos. Evita acoplamiento por base de datos compartida.

## 5. JWT para autenticación
Tokens sin estado, validados en el servidor, emitidos por ms-auth.

## 6. Docker Compose
Un solo comando levanta el sistema completo en local y deja el entorno reproducible.

## 7. Swagger y GlitchTip
Swagger documenta y permite probar la API; GlitchTip centraliza los errores para diagnosticar problemas.

## Cómo agregar una decisión
Anotar el problema, la opción elegida y por qué, en pocas líneas. Si se revierte una decisión, no se borra: se marca como reemplazada.
