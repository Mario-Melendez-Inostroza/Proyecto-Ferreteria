# AGENTS.md — Sistema de Gestión para Ferretería

Guía de entrada para agentes de IA (Claude Code u otros) y para cualquier persona que abra este repositorio. El detalle de la arquitectura, los diagramas y los flujos está en README.md; las reglas de trabajo, en docs/ia/.

## Qué es
Solución basada en microservicios para una ferretería de barrio que hoy lleva inventario, productos y ventas en papel. Digitaliza y centraliza esas operaciones.

## Arquitectura (resumen)
Cliente → API Gateway → BFF → microservicios registrados en Eureka. Cada servicio tiene su propia base de datos (database per service) y la autenticación usa JWT.

## Mapa del repositorio
- apigateway/ : punto de entrada único
- eureka/ : registro y descubrimiento de servicios
- bff/ : Backend For Frontend; delega en clientes HTTP hacia cada servicio
- ms-auth/ (8081), ms-product/ (8082), ms-inventory/, ms-sales/, ms-notification/, ms-supplier/, ms-purchase/, ms-report/, ms-pricing/ : microservicios de negocio
- postgres/init/ : scripts de inicialización de las bases de datos
- docker-compose.yml : levanta todo el sistema
- docs/ : documentación del proyecto; docs/ia/ : contexto y reglas para trabajar con IA

## Stack
Java, Spring Boot (Gradle), Spring Cloud Eureka, PostgreSQL, JWT, Swagger, Docker Compose, GlitchTip para observabilidad.

## Comandos
Construcción, arranque de contenedores y verificación: ver las secciones Construcción, Levantar Contenedores y Verificar Estado del README.md.

## Antes de empezar una tarea, leer
1. docs/ia/CONTEXTO.md  2. docs/ia/REGLAS.md  3. docs/ia/ROADMAP.md  4. docs/ia/DECISIONES.md

## Antes de dar algo por terminado
- El servicio modificado compila y arranca con docker compose
- No se rompió el contrato con el BFF ni con otros servicios
- No hay secretos ni datos reales en el código
- Se actualizó README.md, ROADMAP.md o DECISIONES.md si algo cambió
