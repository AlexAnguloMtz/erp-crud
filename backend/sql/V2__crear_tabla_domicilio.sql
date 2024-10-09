CREATE TABLE domicilio(
    id SERIAL PRIMARY KEY,
    colonia VARCHAR(60) NOT NULL,
    calle VARCHAR(60) NOT NULL,
    numero_calle VARCHAR(60) NOT NULL,
    codigo_postal CHAR(5) NOT NULL CHECK (codigo_postal ~ '^\d{5}$')
);