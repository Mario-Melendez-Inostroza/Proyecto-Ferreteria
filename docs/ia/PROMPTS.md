# Plantillas de instrucciones — Sistema de Gestión para Ferretería

Siempre empezar con: "Lee AGENTS.md y docs/ia/ antes de empezar".

## Nueva funcionalidad en un servicio
> Lee AGENTS.md y docs/ia/. Necesito [funcionalidad] en [ms-...]. Indica qué endpoints, DTO y reglas de negocio cambian y si afecta al BFF. Propón un plan corto antes de escribir código y verifica compilando y probando en Swagger.

## Error o comportamiento raro
> Lee AGENTS.md. Pasa esto: [descripción, servicio y pasos]. Diagnostica la causa leyendo el código y los registros antes de cambiar nada y explica el arreglo mínimo.

## Pruebas
> Escribe pruebas para [clase o endpoint] del servicio [ms-...] cubriendo caso feliz, validaciones y errores. No cambies código de producción.

## Revisión de seguridad
> Revisa [servicio] según docs/ia/REGLAS.md: validación de JWT, autorización por rol, secretos en el repositorio y datos expuestos en respuestas. Lista hallazgos por prioridad.

## Preparar integración
> Verifica que todo compile y levante con Docker Compose, que no haya secretos ni datos reales y que README.md y ROADMAP.md reflejen el estado. Resume los cambios para revisarlos.
