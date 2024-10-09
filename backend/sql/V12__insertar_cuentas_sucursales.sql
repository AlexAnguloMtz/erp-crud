INSERT INTO cuenta_sucursal(cuenta_id, sucursal_id) VALUES 
((SELECT id FROM cuenta WHERE correo='fernanda_montoya@yandex.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Lomas de Madrid' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='nidia_luna@yahoo.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Abarrey Las Torres' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='juan_laureles@outlook.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Bugambilias' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='daniel_jimenez@gmail.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Solidaridad' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='gabriela_ortiz2@yahoo.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Ley 57' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='laura_arce@gmail.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Lomas de Madrid' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='mario_monteverde4@outlook.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Abarrey Las Torres' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='cesar_diaz@outlook.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Bugambilias' LIMIT 1)),
((SELECT id FROM cuenta WHERE correo='margarita_juarez@yandex.com' LIMIT 1), (SELECT id FROM sucursal WHERE nombre='Solidaridad' LIMIT 1));
