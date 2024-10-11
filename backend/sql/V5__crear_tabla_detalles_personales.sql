CREATE TABLE domicilio_personal(
    id SERIAL PRIMARY KEY,
    colonia VARCHAR(60) NOT NULL,
    calle VARCHAR(60) NOT NULL,
    numero_calle VARCHAR(10) NOT NULL,
    codigo_postal CHAR(5) NOT NULL CHECK (codigo_postal ~ '^\d{5}$')
);

CREATE TABLE detalles_personales(
    id SERIAL PRIMARY KEY,
    cuenta_id INT NOT NULL,
    domicilio_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    apellido VARCHAR(60) NOT NULL,
    telefono CHAR(10) NOT NULL CHECK (telefono ~ '^\d{10}$'),
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (domicilio_id) REFERENCES domicilio_personal(id) ON UPDATE CASCADE ON DELETE CASCADE
);