-- Insertar domicilios
INSERT INTO domicilio_personal(colonia, calle, numero_calle, codigo_postal) VALUES 
('Centro', 'Avenida 1', '101', '01010'),
('Roma', 'Calle 2', '202', '02020'),
('Polanco', 'Calle 3', '303', '03030'),
('Condesa', 'Avenida 4', '404', '04040'),
('Santa Fe', 'Calle 5', '505', '05050'),
('Coyoacán', 'Calle 6', '606', '06060'),
('Tlalpan', 'Calle 7', '707', '07070'),
('Gustavo A. Madero', '8', '808', '08080'),
('Iztapalapa', 'Calle 9', '909', '09090'),
('Xochimilco', 'Avenida 10', '1000', '10101'),
('Roma', 'Paseo de la Reforma', '123', '83000'),
('Condesa', 'Avenida Veracruz', '456', '83200'),
('Juárez', 'Avenida 16 de Septiembre', '789', '83400'),
('Narvarte', 'Calle José María Vértiz', '101', '83500'),
('Del Valle', 'Calle Adolfo Prieto', '112', '83600'),
('Polanco', 'Avenida Presidente Masaryk', '131', '83700'),
('Santa Fe', 'Calle Guillermo González Camarena', '415', '83800'),
('Centro', 'Calle Madero', '161', '83900'),
('Tacubaya', 'Calle Pátzcuaro', '718', '84000'),
('Lomas de Chapultepec', 'Calle José María Castorena', '910', '84100'),
('San Ángel', 'Calle Diego Rivera', '222', '84200'),
('Tlalpan', 'Calle San Fernando', '333', '84300'),
('Coyoacán', 'Calle Centenario', '444', '84400'),
('Iztacalco', 'Calle 5 de Febrero', '555', '84500'),
('Venustiano Carranza', 'Avenida Moctezuma', '666', '84600'),
('Azcapotzalco', 'Calle Prolongación 16 de Septiembre', '777', '84700'),
('Magdalena Contreras', 'Calle San Mateo', '888', '84800'),
('Xochimilco', 'Calle Rastro', '999', '84900'),
('La Merced', 'Calle 5 de Febrero', '111', '85000'),
('Tacuba', 'Avenida 16 de Septiembre', '123', '85100'),
('Lindavista', 'Mariposa', '123', '07760'),
('Tlalpan', 'San Andrés', '456', '14000'),
('Coyoacán', 'Malintzin', '789', '04000'),
('San Jerónimo', 'Jesús del Monte', '321', '10200'),
('San Miguel Chapultepec', 'De la Palma', '654', '11850');

-- Insertar detalles personales
INSERT INTO detalles_personales(cuenta_id, domicilio_id, nombre, apellido, telefono) VALUES 
((SELECT id FROM cuenta WHERE correo='fernanda_montoya@yandex.com'), 1, 'Fernanda', 'Montoya', '5552345678'),
((SELECT id FROM cuenta WHERE correo='nidia_luna@yahoo.com'), 2, 'Nidia', 'Luna', '5553456789'),
((SELECT id FROM cuenta WHERE correo='juan_laureles@outlook.com'), 3, 'Juan', 'Laureles', '5554567890'),
((SELECT id FROM cuenta WHERE correo='daniel_jimenez@gmail.com'), 4, 'Daniel', 'Jiménez', '5555678901'),
((SELECT id FROM cuenta WHERE correo='gabriela_ortiz2@yahoo.com'), 5, 'Gabriela', 'Ortiz', '5556789012'),
((SELECT id FROM cuenta WHERE correo='laura_arce@gmail.com'), 6, 'Laura', 'Arce', '5557890123'),
((SELECT id FROM cuenta WHERE correo='mario_monteverde4@outlook.com'), 7, 'Mario', 'Monteverde', '5558901234'),
((SELECT id FROM cuenta WHERE correo='cesar_diaz@outlook.com'), 8, 'César', 'Díaz', '5559012345'),
((SELECT id FROM cuenta WHERE correo='margarita_juarez@yandex.com'), 9, 'Margarita', 'Juárez', '5560123456'),
((SELECT id FROM cuenta WHERE correo = 'juanperez@gmail.com'), 10, 'Juan', 'Pérez', '5551234567'),
((SELECT id FROM cuenta WHERE correo = 'marialopez@gmail.com'), 11, 'María', 'López', '5552345678'),
((SELECT id FROM cuenta WHERE correo = 'carlosgarcia@gmail.com'), 12, 'Carlos', 'Garcia', '5553456789'),
((SELECT id FROM cuenta WHERE correo = 'sofiahernandez@gmail.com'), 13, 'Sofía', 'Hernandez', '5554567890'),
((SELECT id FROM cuenta WHERE correo = 'luisrodriguez@gmail.com'), 14, 'Luis', 'Rodriguez', '5555678901'),
((SELECT id FROM cuenta WHERE correo = 'catalinajimenez@gmail.com'), 15, 'Catalina', 'Jimenez', '5556789012'),
((SELECT id FROM cuenta WHERE correo = 'alejandrosanchez@gmail.com'), 16, 'Alejandro', 'Sanchez', '5557890123'),
((SELECT id FROM cuenta WHERE correo = 'fernandovaldez@gmail.com'), 17, 'Fernando', 'Valdez', '5558901234'),
((SELECT id FROM cuenta WHERE correo = 'andreaortiz@gmail.com'), 18, 'Andrea', 'Ortiz', '5559012345'),
((SELECT id FROM cuenta WHERE correo = 'davidcarrillo@gmail.com'), 19, 'David', 'Carrillo', '5550123456'),
((SELECT id FROM cuenta WHERE correo = 'jesusmartinez@yahoo.com'), 20, 'Jesus', 'Martinez', '5551234567'),
((SELECT id FROM cuenta WHERE correo = 'paolagonzalez@yahoo.com'), 21, 'Paola', 'Gonzalez', '5552345678'),
((SELECT id FROM cuenta WHERE correo = 'juliocastro@yahoo.com'), 22, 'Julio', 'Castro', '5553456789'),
((SELECT id FROM cuenta WHERE correo = 'marcelarojas@yahoo.com'), 23, 'Marcel', 'Rojas', '5554567890'),
((SELECT id FROM cuenta WHERE correo = 'ricardomorales@yahoo.com'), 24, 'Ricardo', 'Morales', '5555678901'),
((SELECT id FROM cuenta WHERE correo = 'isabellapaz@yahoo.com'), 25, 'Isabella', 'Paz', '5556789012'),
((SELECT id FROM cuenta WHERE correo = 'emiliagonzalez@yandex.com'), 26, 'Emilia', 'Gonzalez', '5557890123'),
((SELECT id FROM cuenta WHERE correo = 'lilianarivera@yandex.com'), 27, 'Liliana', 'Rivera', '5558901234'),
((SELECT id FROM cuenta WHERE correo = 'joseperez@yandex.com'), 28, 'Jose', 'Perez', '5559012345'),
((SELECT id FROM cuenta WHERE correo = 'luismartinez@yandex.com'), 29, 'Luis', 'Martinez', '5550123456'),
((SELECT id FROM cuenta WHERE correo = 'salvadorcastillo@protonmail.com'), 30, 'Salvador', 'Castillo', '5551234567'),
((SELECT id FROM cuenta WHERE correo = 'veronicagarcia@protonmail.com'), 31, 'Veronica', 'Garcia', '5552345678'),
((SELECT id FROM cuenta WHERE correo = 'pablomartinez@protonmail.com'), 32, 'Pablo', 'Martinez', '5553456789'),
((SELECT id FROM cuenta WHERE correo = 'robertohernandez@protonmail.com'), 33, 'Roberto', 'Hernandez', '5554567890'),
((SELECT id FROM cuenta WHERE correo = 'valentinatapia@protonmail.com'), 34, 'Valentina', 'Tapia', '5555678901'),
((SELECT id FROM cuenta WHERE correo = 'marianavazquez@protonmail.com'), 35, 'Mariana', 'Vazquez', '5556789012');
