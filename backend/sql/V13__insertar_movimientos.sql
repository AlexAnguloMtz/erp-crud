-- Insertar tipos de movimiento de inventario
INSERT INTO tipo_movimiento_inventario (descripcion) VALUES
('Entrada de mercancía'),
('Salida de mercancía'),
('Devolución'),
('Ajuste positivo'),
('Ajuste negativo');

-- Insertar 10 movimientos en movimiento_inventario
INSERT INTO movimiento_inventario (responsable_id, tipo_movimiento_inventario_id, creado, observaciones) VALUES
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Ingreso de productos al inventario para reabastecimiento.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Salida de productos para pedido de cliente.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Transferencia de productos entre almacenes.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Devolución de productos dañados al proveedor.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Ajuste de inventario por revisión periódica.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Ingreso de productos por devolución de cliente.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Salida de productos para promoción especial.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Ajuste de stock por errores en inventario.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Ingreso de productos de nuevo proveedor.'),
    ( (SELECT id FROM cuenta ORDER BY RANDOM() LIMIT 1), (SELECT id FROM tipo_movimiento_inventario ORDER BY RANDOM() LIMIT 1), NOW(), 'Salida de productos para evento promocional.');

-- Insertar productos relacionados
INSERT INTO movimiento_inventario_producto (producto_id, movimiento_inventario_id, cantidad)
SELECT 
    (SELECT id FROM producto ORDER BY RANDOM() LIMIT 1) AS producto_id,
    mi.id AS movimiento_id,
    1 AS cantidad
FROM 
    movimiento_inventario mi
CROSS JOIN LATERAL generate_series(1, 3) -- Generar 3 productos por movimiento
LIMIT 30; -- 10 movimientos * 3 productos
