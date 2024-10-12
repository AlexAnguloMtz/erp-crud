CREATE TABLE marca(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE categoria_producto(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE producto(
    id SERIAL PRIMARY KEY,
    categoria_producto_id INT NOT NULL,
    marca_id INT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    FOREIGN KEY (categoria_producto_id) REFERENCES categoria_producto(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (marca_id) REFERENCES marca(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE stock(
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE marca ADD CONSTRAINT uk_marca_nombre UNIQUE (nombre);

ALTER TABLE categoria_producto ADD CONSTRAINT uk_categoria_producto_nombre UNIQUE (nombre);