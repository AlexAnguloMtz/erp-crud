CREATE TABLE domicilio_sucursal(
    id SERIAL PRIMARY KEY,
    colonia VARCHAR(60) NOT NULL,
    calle VARCHAR(60) NOT NULL,
    numero_calle VARCHAR(10) NOT NULL,
    codigo_postal CHAR(5) NOT NULL CHECK (codigo_postal ~ '^\d{5}$')
);

CREATE TABLE tipo_sucursal(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL,
    descripcion VARCHAR(300) NOT NULL
);

CREATE TABLE sucursal(
    id SERIAL PRIMARY KEY,
    domicilio_id INT NOT NULL,
    tipo_sucursal_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    telefono CHAR(10) NOT NULL CHECK (telefono ~ '^\d{10}$'),
    FOREIGN KEY (domicilio_id) REFERENCES domicilio_sucursal(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (tipo_sucursal_id) REFERENCES tipo_sucursal(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE sucursal ADD CONSTRAINT uk_sucursal_nombre UNIQUE (nombre);