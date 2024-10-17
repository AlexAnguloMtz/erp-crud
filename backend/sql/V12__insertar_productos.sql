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

INSERT INTO unidad_inventario(nombre) VALUES
('Unidad'),
('Kilogramo');

INSERT INTO producto(unidad_inventario_id, categoria_producto_id, marca_id, nombre, sku, precio_venta_centavos) VALUES
-- Productos de Bimbo
(1, (SELECT id FROM categoria_producto WHERE nombre='Panadería' LIMIT 1), (SELECT id FROM marca WHERE nombre='Bimbo' LIMIT 1), 'Pan de caja 675 GR', 'BIM00123', 4017),
(1, (SELECT id FROM categoria_producto WHERE nombre='Panadería' LIMIT 1), (SELECT id FROM marca WHERE nombre='Bimbo' LIMIT 1), 'Mantecadas 345 GR', 'BIM00124', 2534),
-- Productos de Lala
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Lala' LIMIT 1), 'Leche Entera 1L', 'LAL00123', 2013),
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Lala' LIMIT 1), 'Yogur Natural 1L', 'LAL00124', 3054),
-- Productos de Carnes Selectas
(2, (SELECT id FROM categoria_producto WHERE nombre='Carnes y pescado' LIMIT 1), (SELECT id FROM marca WHERE nombre='Carnes Selectas' LIMIT 1), 'Diezmillo de res', 'CAS00123', 9056),
(2, (SELECT id FROM categoria_producto WHERE nombre='Carnes y pescado' LIMIT 1), (SELECT id FROM marca WHERE nombre='Carnes Selectas' LIMIT 1), 'Pollo pechuga', 'CAS00124', 8074),
-- Productos de Frutas Frescas
(2, (SELECT id FROM categoria_producto WHERE nombre='Frutas y verduras' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frutas Frescas' LIMIT 1), 'Manzana', 'FFR00123', 1584),
(2, (SELECT id FROM categoria_producto WHERE nombre='Frutas y verduras' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frutas Frescas' LIMIT 1), 'Plátano', 'FFR00124', 1292),
-- Productos de La Costeña
(1, (SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='La Costeña' LIMIT 1), 'Salsa de tomate 255 GR', 'LC004848', 1825),
(1, (SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='La Costeña' LIMIT 1), 'Frijoles 300 GR', 'LC004849', 1574),
-- Productos de Pepsi
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pepsi' LIMIT 1), 'Pepsi 355ML', 'PEP00123', 1867),
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pepsi' LIMIT 1), 'Pepsi 600ML', 'PEP00124', 2535),
-- Productos de Coca-Cola
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Coca-Cola' LIMIT 1), 'Coca-Cola 355ML', 'COC00123', 1883),
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Coca-Cola' LIMIT 1), 'Coca-Cola 600ML', 'COC00124', 2517),
-- Productos de Sabritas
(1, (SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Sabritas' LIMIT 1), 'Papitas Sabritas 600 GR', 'SAB00123', 1575),
(1, (SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Sabritas' LIMIT 1), 'Doritos 540 GR', 'SAB00124', 1864),
-- Productos de Frescolita
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frescolita' LIMIT 1), 'Frescolita 1L', 'FRES0123', 2043),
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Frescolita' LIMIT 1), 'Frescolita Zero 355 ML', 'FRES0124', 1857),
-- Productos de Cielito Lindo
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Cielito Lindo' LIMIT 1), 'Leche Entera 1L', 'CEL00123', 1924),
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Cielito Lindo' LIMIT 1), 'Yogur Natural 1L', 'CEL00124', 3063),
-- Productos de Maseca
(2, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maseca' LIMIT 1), 'Harina de maíz', 'MAS00123', 3500),
(2, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maseca' LIMIT 1), 'Tortillas', 'MAS00124', 6000),
-- Productos de Nescafé
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Nescafé' LIMIT 1), 'Café Tradicional 280 GR', 'NES00123', 2264),
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Nescafé' LIMIT 1), 'Café Descafeinado 275 GR', 'NES00124', 2414),
-- Productos de Herdez
(1, (SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Herdez' LIMIT 1), 'Salsa verde 250 GR', 'HER00123', 1878),
(1, (SELECT id FROM categoria_producto WHERE nombre='Conservas y salsas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Herdez' LIMIT 1), 'Salsa roja 250 GR', 'HER00124', 1857),
-- Productos de Pasta San Giorgio
(1, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pasta San Giorgio' LIMIT 1), 'Spaghetti 250 G', 'PAS00123', 3024),
(1, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Pasta San Giorgio' LIMIT 1), 'Fusilli 250 G', 'PAS00124', 3042),
-- Productos de Yakult
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Yakult' LIMIT 1), 'Yogurt bebible 350ML', 'YAK00123', 2046),
(1, (SELECT id FROM categoria_producto WHERE nombre='Lácteos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Yakult' LIMIT 1), 'Yakult bebible 600ML', 'YAK00124', 3068),
-- Productos de Galletas Emperador
(1, (SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Galletas Emperador' LIMIT 1), 'Galletas Clásicas 345 GR', 'GAL00123', 1536),
(1, (SELECT id FROM categoria_producto WHERE nombre='Snacks y golosinas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Galletas Emperador' LIMIT 1), 'Galletas Chocolate 400 GR', 'GAL00124', 1847),
-- Productos de Maruchan
(1, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maruchan' LIMIT 1), 'Sopa instantánea 260 GR', 'MAR00123', 1558),
(1, (SELECT id FROM categoria_producto WHERE nombre='Abarrotes secos' LIMIT 1), (SELECT id FROM marca WHERE nombre='Maruchan' LIMIT 1), 'Sopa con Pollo 245 GR', 'MAR00124', 1696),
-- Productos de Atole Abuelita
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Atole Abuelita' LIMIT 1), 'Atole Tradicional 500 GR', 'ATO00123', 2024),
(1, (SELECT id FROM categoria_producto WHERE nombre='Bebidas' LIMIT 1), (SELECT id FROM marca WHERE nombre='Atole Abuelita' LIMIT 1), 'Atole Chocolate 500 GR', 'ATO00124', 2235);

INSERT INTO stock(producto_id, cantidad, sucursal_id) VALUES
(1, 25, 1),
(2, 40, 1),
(3, 30, 1),
(4, 20, 1),
(5, 15, 1),
(6, 35, 1),
(7, 50, 1),
(8, 45, 2),
(9, 55, 2),
(10, 30, 2),
(11, 60, 2),
(12, 50, 2),
(13, 40, 3),
(14, 35, 3),  
(15, 55, 3),  
(16, 25, 3),  
(17, 20, 3), 
(18, 15, 3),  
(19, 30, 3),  
(20, 25, 3),  
(21, 45, 3),
(22, 50, 3), 
(23, 35, 3), 
(24, 20, 4),  
(25, 40, 4),  
(26, 30, 4),  
(27, 25, 5),  
(28, 15, 5),  
(29, 20, 5),  
(30, 30, 5), 
(31, 55, 5),  
(32, 50, 6),  
(33, 40, 6),  
(34, 35, 6), 
(35, 30, 7),  
(36, 20, 7);