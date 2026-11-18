# Roadmap — Sistema de Gestión para Ferretería

Estado real del proyecto. Se actualiza al cerrar cada tarea.

## Hecho (documentado en README.md)
- [x] Arquitectura API Gateway + BFF + microservicios con Eureka, con diagramas C2 y C3
- [x] Un microservicio por dominio: auth, product, inventory, sales, notification, supplier, purchase, report y pricing
- [x] Base de datos por servicio y scripts de inicialización en postgres/init
- [x] Autenticación con JWT y documentación de la API con Swagger
- [x] Orquestación con Docker Compose y observabilidad con GlitchTip

## Siguiente (propuesto, sin fecha comprometida)
- [ ] Pipeline de CI con GitHub Actions: compilar y probar cada servicio en cada Pull Request
- [ ] Pruebas automatizadas por servicio y pruebas de contrato entre BFF y servicios
- [ ] README corto por microservicio (cómo correrlo y qué expone)
- [ ] Revisión de seguridad de JWT (expiración, validación de audiencia y emisor)
- [ ] Interfaz web o móvil que consuma el BFF

## Fuera de alcance por ahora
- Despliegue en producción y datos reales del negocio
