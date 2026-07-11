INSERT INTO supplier (id, name, rut, contact_name, phone, email, address, active, created_at)
VALUES
    (gen_random_uuid(), 'Ferretería El Tornillo Ltda.', '76.111.222-3', 'Juan Pérez', '+56 9 1111 2222', 'contacto@eltornillo.cl', 'Av. Industrial 450, Santiago', TRUE, NOW()),
    (gen_random_uuid(), 'Distribuidora Maderas del Sur', '77.222.333-4', 'María Soto', '+56 9 2222 3333', 'ventas@maderasdelsur.cl', 'Camino Real 1200, Temuco', TRUE, NOW()),
    (gen_random_uuid(), 'Importadora Herramientas Andinas', '78.333.444-5', 'Pedro Ramírez', '+56 9 3333 4444', 'info@herramientasandinas.cl', 'Los Aromos 890, Concepción', TRUE, NOW());
