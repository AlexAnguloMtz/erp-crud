CREATE TABLE rol(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL,
    nombre_canonico VARCHAR(20) NOT NULL
);

CREATE TABLE cuenta(
    id SERIAL PRIMARY KEY,
    correo VARCHAR(255) NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol_id INT NOT NULL,
    FOREIGN KEY (rol_id) REFERENCES rol(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE rol ADD CONSTRAINT uk_rol_nombre UNIQUE (nombre);

ALTER TABLE rol ADD CONSTRAINT uk_rol_nombre_canonico UNIQUE (nombre_canonico);

ALTER TABLE cuenta ADD CONSTRAINT uk_cuenta_correo UNIQUE (correo);