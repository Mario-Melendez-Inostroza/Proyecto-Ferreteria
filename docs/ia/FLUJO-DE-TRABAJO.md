# Flujo de trabajo con IA — Sistema de Gestión para Ferretería

La IA no recuerda conversaciones anteriores: lo que está escrito en AGENTS.md y docs/ia/ es lo que lee al empezar cada tarea.

## Ciclo de cada tarea
1. Especificar: qué servicio cambia y cómo se verá terminado.
2. La IA propone un plan corto, indicando qué servicios y contratos toca; una persona lo aprueba.
3. Implementar en pasos pequeños, un servicio a la vez.
4. Verificar ejecutando: compilar, levantar con Docker Compose y probar el flujo en Swagger.
5. Una persona revisa el cambio.
6. Documentar: actualizar README.md, ROADMAP.md y DECISIONES.md si corresponde.

## Quién hace qué
- La IA (Claude Code): propone estructura, escribe código y pruebas, diagnostica errores leyendo registros y código, y ejecuta las verificaciones.
- La persona (Mario): define los requisitos, aprueba planes, revisa cada cambio, prueba los flujos y decide qué se integra.

## Cómo se controlan los errores de la IA
- Nada se da por bueno sin ejecutarlo.
- Los cambios son pequeños y revisables.
- Se revisa que no se rompan los contratos entre servicios.
- Antes de publicar se revisa que no haya secretos ni datos reales.
