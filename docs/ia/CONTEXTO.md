# Contexto — Sistema de Gestión para Ferretería

## El problema
Una ferretería de barrio administra inventario en papel, registra productos a mano, anota ventas en cuadernos, controla el stock manualmente y no tiene alertas de stock bajo. Eso provoca pérdida de documentos, errores de digitación y falta de trazabilidad.

## Objetivo
Digitalizar y automatizar las operaciones críticas (productos, inventario, ventas) centralizando la información y mejorando la trazabilidad, la disponibilidad y la confiabilidad de los datos.

## Servicios y responsabilidad
- ms-auth: autenticación y generación de JWT
- ms-product: catálogo de productos
- ms-inventory: control de inventario y stock
- ms-sales: registro de ventas
- ms-notification: notificaciones (por ejemplo, stock bajo)
- ms-supplier: proveedores
- ms-purchase: compras
- ms-report: reportes
- ms-pricing: precios
- bff: capa intermedia entre el cliente y los servicios
- apigateway y eureka: entrada única y descubrimiento de servicios

## Restricciones
- Datos de ejemplo únicamente: no subir datos reales del negocio ni de clientes.
- Cada servicio es independiente y tiene su propia base de datos.
- Las credenciales y secretos no se versionan.
