-- Insertar domicilios
INSERT INTO domicilio(calle, numero_calle, colonia, codigo_postal) VALUES 
('Calle Francisco Monteverde', 'Av. Constituyentes 517', 'Ley 57', '83100'),
('Sinoquipe', '540', 'Lomas de Madrid', '83104'),
('Blvd. las Torres', '86', 'Lomas de Linda Vista', '83147'),
('Av. Cuatro 64 esq Calle Dos', '', 'Bugambilia, Encanto II', '83140'),
('Blvd. Solidaridad', '#738', 'Norberto Ortega', '83116');

-- Insertar sucursales
INSERT INTO sucursal(domicilio_id, nombre) VALUES 
((SELECT id FROM domicilio WHERE colonia='Ley 57'), 'Ley 57'),
((SELECT id FROM domicilio WHERE colonia='Lomas de Madrid'), 'Lomas de Madrid'),
((SELECT id FROM domicilio WHERE colonia='Lomas de Linda Vista'), 'Abarrey Las Torres'),
((SELECT id FROM domicilio WHERE colonia='Bugambilia, Encanto II'), 'Bugambilias'),
((SELECT id FROM domicilio WHERE colonia='Norberto Ortega'), 'Solidaridad');
