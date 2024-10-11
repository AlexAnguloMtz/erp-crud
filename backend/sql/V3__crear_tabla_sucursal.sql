CREATE TABLE domicilio_sucursal(
    id SERIAL PRIMARY KEY,
    colonia VARCHAR(60) NOT NULL,
    calle VARCHAR(60) NOT NULL,
    numero_calle VARCHAR(10) NOT NULL,
    codigo_postal CHAR(5) NOT NULL CHECK (codigo_postal ~ '^\d{5}$')
);

CREATE TABLE sucursal(
    id SERIAL PRIMARY KEY,
    domicilio_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    FOREIGN KEY (domicilio_id) REFERENCES domicilio_sucursal(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE sucursal ADD CONSTRAINT uk_sucursal_nombre UNIQUE (nombre);