# Reglas para la IA — Sistema de Gestión para Ferretería

## Arquitectura
- Un servicio nunca accede a la base de datos de otro: se comunica por API.
- El cliente habla con el API Gateway y el BFF, no directamente con los microservicios.
- No agregar servicios, colas ni tecnologías nuevas sin una decisión registrada en docs/ia/DECISIONES.md. Preferir lo simple y convencional.
- Mantener la separación por capas: controller, service, repository, con DTO distintos de las entidades.

## Seguridad
- La autorización se valida en el servidor con JWT; el cliente nunca decide permisos.
- Sin secretos, claves ni contraseñas en el repositorio: variables de entorno.
- Sin datos personales reales en scripts de inicialización ni en ejemplos.

## Cambios
- Cambios pequeños y enfocados: un tema por commit, con mensaje claro.
- Los contratos entre BFF y servicios no se rompen sin actualizar ambos lados y la documentación.
- Los cambios de esquema van en postgres/init o en migraciones, nunca a mano.

## Verificación
- Nada se da por bueno sin ejecutarlo: compilar el servicio y levantarlo con Docker Compose.
- Probar el flujo afectado de punta a punta (por ejemplo, login, crear producto, registrar venta) usando Swagger.
- La IA propone y escribe; una persona revisa cada cambio antes de integrarlo.
