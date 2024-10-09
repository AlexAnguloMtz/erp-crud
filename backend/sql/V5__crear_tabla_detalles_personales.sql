CREATE TABLE detalles_personales(
    id SERIAL PRIMARY KEY,
    cuenta_id INT NOT NULL,
    domicilio_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    telefono CHAR(10) NOT NULL CHECK (telefono ~ '^\d{10}$'),
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (domicilio_id) REFERENCES domicilio(id) ON UPDATE CASCADE ON DELETE CASCADE
);