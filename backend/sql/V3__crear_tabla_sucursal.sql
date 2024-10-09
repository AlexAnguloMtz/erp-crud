CREATE TABLE sucursal(
    id SERIAL PRIMARY KEY,
    domicilio_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    FOREIGN KEY (domicilio_id) REFERENCES domicilio(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE sucursal ADD CONSTRAINT uk_sucursal_nombre UNIQUE (nombre);