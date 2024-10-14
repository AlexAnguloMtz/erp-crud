INSERT INTO marca(nombre) VALUES 
('Bimbo'), 
('Lala'), 
('Carnes Selectas'), 
('Frutas Frescas'), 
('La Costeña'), 
('Pepsi'), 
('Coca-Cola'), 
('Sabritas'), 
('Frescolita'), 
('Cielito Lindo'), 
('Maseca'), 
('Nescafé'), 
('Herdez'), 
('Pasta San Giorgio'), 
('Yakult'), 
('Galletas Emperador'), 
('Maruchan'), 
('Atole Abuelita'), 
('Tortillas El Pajarito'), 
('Cremas El Mexicano');

INSERT INTO categoria_producto(nombre) VALUES 
('Panadería'),
('Lácteos'),
('Carnes y pescado'),
('Frutas y verduras'),
('Abarrotes secos'),
('Conservas y salsas'),
('Bebidas'),
('Snacks y golosinas'),
('Limpieza'),
('Cuidado personal');

INSERT INTO producto(categoria_producto_id, marca_id, nombre) VALUES 
-- Productos de Bimbo
((SELECT id FROM categoria_producto WHERE nombre='Panadería' LIMIT 1), (SELECT id FROM marca WHERE nombre='Bimbo' LIMIT 1), 'Pan de caja grande'),
((SELECT id FROM categoria_producto WHERE nombre='Panadería' LIMIT 1), (SELECT id FROM marca WHERE nombre='Bimbo' LIMIT 1), 'Pan dulce'),
-- Productos de Lala
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Lala' LIMIT 1), 'Leche Entera 1L'),
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Lala' LIMIT 1), 'Yogur Natural 1L'),
-- Productos de Carnes Selectas
((SELECT id FROM categoria_producto WHERE nombre='Carnes y pescado' LIMIT 1), (SELECT id FROM marca WHERE nombre='Carnes Selectas' LIMIT 1), 'Diezmillo de res'),
((SELECT id FROM categoria_producto WHERE nombre='Carnes y pescado' LIMIT 1), (SELECT id FROM marca WHERE nombre='Carnes Selectas' LIMIT 1), 'Pollo pechuga'),
-- Productos de Frutas Frescas
((SELECT id FROM categoria_producto WHERE nombre='Frutas y verduras' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frutas Frescas' LIMIT 1), 'Manzana'),
((SELECT id FROM categoria_producto WHERE nombre='Frutas y verduras' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frutas Frescas' LIMIT 1), 'Plátano'),
-- Productos de La Costeña
((SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='La Costeña' LIMIT 1), 'Salsa de tomate'),
((SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='La Costeña' LIMIT 1), 'Frijoles'),
-- Productos de Pepsi
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pepsi' LIMIT 1), 'Pepsi 355ML'),
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pepsi' LIMIT 1), 'Pepsi 600ML'),
-- Productos de Coca-Cola
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Coca-Cola' LIMIT 1), 'Coca-Cola 355ML'),
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Coca-Cola' LIMIT 1), 'Coca-Cola 600ML'),
-- Productos de Sabritas
((SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Sabritas' LIMIT 1), 'Papitas Sabritas'),
((SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Sabritas' LIMIT 1), 'Doritos'),
-- Productos de Frescolita
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frescolita' LIMIT 1), 'Frescolita 1L'),
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frescolita' LIMIT 1), 'Frescolita Zero 355 ML'),
-- Productos de Cielito Lindo
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Cielito Lindo' LIMIT 1), 'Leche 1L'),
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Cielito Lindo' LIMIT 1), 'Yogur Natural 1L'),
-- Productos de Maseca
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maseca' LIMIT 1), 'Harina de maíz 1 KG'),
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maseca' LIMIT 1), 'Tortillas'),
-- Productos de Nescafé
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Nescafé' LIMIT 1), 'Café Tradicional'),
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Nescafé' LIMIT 1), 'Café Descafeinado'),
-- Productos de Herdez
((SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Herdez' LIMIT 1), 'Salsa verde'),
((SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Herdez' LIMIT 1), 'Salsa roja'),
-- Productos de Pasta San Giorgio
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pasta San Giorgio' LIMIT 1), 'Spaghetti 250 G'),
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pasta San Giorgio' LIMIT 1), 'Fusilli 250 G'),
-- Productos de Yakult
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Yakult' LIMIT 1), 'Yogurt bebible 350ML'),
((SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Yakult' LIMIT 1), 'Yakult bebible 600ML'),
-- Productos de Galletas Emperador
((SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Galletas Emperador' LIMIT 1), 'Galletas Clásicas'),
((SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Galletas Emperador' LIMIT 1), 'Galletas Chocolate'),
-- Productos de Maruchan
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maruchan' LIMIT 1), 'Sopa instantánea'),
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maruchan' LIMIT 1), 'Sopa con Pollo'),
-- Productos de Atole Abuelita
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Atole Abuelita' LIMIT 1), 'Atole Tradicional'),
((SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Atole Abuelita' LIMIT 1), 'Atole Chocolate'),
-- Productos de Tortillas El Pajarito
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Tortillas El Pajarito' LIMIT 1), 'Tortillas de maíz'),
((SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Tortillas El Pajarito' LIMIT 1), 'Tortillas de harina');

INSERT INTO stock(producto_id, cantidad, sucursal_id) VALUES
((SELECT id FROM producto WHERE nombre='Pan de caja grande' LIMIT 1), 25, 1),
((SELECT id FROM producto WHERE nombre='Pan dulce' LIMIT 1), 40, 1),
((SELECT id FROM producto WHERE nombre='Leche Entera 1L' LIMIT 1), 30, 1),
((SELECT id FROM producto WHERE nombre='Yogur Natural 1L' LIMIT 1), 20, 1),
((SELECT id FROM producto WHERE nombre='Diezmillo de res' LIMIT 1), 15, 1),
((SELECT id FROM producto WHERE nombre='Pollo pechuga' LIMIT 1), 35, 1),
((SELECT id FROM producto WHERE nombre='Manzana' LIMIT 1), 50, 1),
((SELECT id FROM producto WHERE nombre='Plátano' LIMIT 1), 45, 2),
((SELECT id FROM producto WHERE nombre='Salsa de tomate' LIMIT 1), 55, 2),
((SELECT id FROM producto WHERE nombre='Frijoles' LIMIT 1), 30, 2),
((SELECT id FROM producto WHERE nombre='Pepsi 355ML' LIMIT 1), 60, 2),
((SELECT id FROM producto WHERE nombre='Pepsi 600ML' LIMIT 1), 50, 2),
((SELECT id FROM producto WHERE nombre='Coca-Cola 355ML' LIMIT 1), 40, 3),
((SELECT id FROM producto WHERE nombre='Coca-Cola 600ML' LIMIT 1), 35, 3),
((SELECT id FROM producto WHERE nombre='Papitas Sabritas' LIMIT 1), 55, 3),
((SELECT id FROM producto WHERE nombre='Doritos' LIMIT 1), 25, 3),
((SELECT id FROM producto WHERE nombre='Frescolita 1L' LIMIT 1), 20, 3),
((SELECT id FROM producto WHERE nombre='Frescolita Zero 355 ML' LIMIT 1), 15, 3),
((SELECT id FROM producto WHERE nombre='Leche 1L' LIMIT 1), 30, 3),
((SELECT id FROM producto WHERE nombre='Yogur Natural 1L' LIMIT 1), 25, 3),
((SELECT id FROM producto WHERE nombre='Harina de maíz 1 KG' LIMIT 1), 45, 3),
((SELECT id FROM producto WHERE nombre='Tortillas' LIMIT 1), 50, 3),
((SELECT id FROM producto WHERE nombre='Café Tradicional' LIMIT 1), 35, 3),
((SELECT id FROM producto WHERE nombre='Café Descafeinado' LIMIT 1), 20,4 ),
((SELECT id FROM producto WHERE nombre='Salsa verde' LIMIT 1), 40, 4),
((SELECT id FROM producto WHERE nombre='Salsa roja' LIMIT 1), 30, 4),
((SELECT id FROM producto WHERE nombre='Spaghetti 250 G' LIMIT 1), 25, 5),
((SELECT id FROM producto WHERE nombre='Fusilli 250 G' LIMIT 1), 15, 5),
((SELECT id FROM producto WHERE nombre='Yogurt bebible 350ML' LIMIT 1), 20, 5),
((SELECT id FROM producto WHERE nombre='Yakult bebible 600ML' LIMIT 1), 30, 5),
((SELECT id FROM producto WHERE nombre='Galletas Clásicas' LIMIT 1), 55, 5),
((SELECT id FROM producto WHERE nombre='Galletas Chocolate' LIMIT 1), 50, 6),
((SELECT id FROM producto WHERE nombre='Sopa instantánea' LIMIT 1), 40, 6),
((SELECT id FROM producto WHERE nombre='Sopa con Pollo' LIMIT 1), 35, 6),
((SELECT id FROM producto WHERE nombre='Atole Tradicional' LIMIT 1), 30, 7),
((SELECT id FROM producto WHERE nombre='Atole Chocolate' LIMIT 1), 20, 7),
((SELECT id FROM producto WHERE nombre='Tortillas de maíz' LIMIT 1), 60, 8),
((SELECT id FROM producto WHERE nombre='Tortillas de harina' LIMIT 1), 50, 8);
