CREATE TABLE marca(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE unidad_inventario(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(20) NOT NULL
);

CREATE TABLE categoria_producto(
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(60) NOT NULL
);

CREATE TABLE producto(
    id SERIAL PRIMARY KEY,
    sku VARCHAR(8) NOT NULL CHECK (LENGTH(sku) = 8),
    categoria_producto_id INT NOT NULL,
    marca_id INT NOT NULL,
    unidad_inventario_id INT NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    precio_venta_centavos INT NOT NULL,
    imagen VARCHAR(255) NULL,
    FOREIGN KEY (categoria_producto_id) REFERENCES categoria_producto(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (unidad_inventario_id) REFERENCES unidad_inventario(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (marca_id) REFERENCES marca(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE stock(
    producto_id INT NOT NULL,
    sucursal_id INT NOT NULL,
    cantidad INT NOT NULL CHECK (cantidad >= 0),
    PRIMARY KEY (producto_id, sucursal_id),
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursal(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE marca ADD CONSTRAINT uk_marca_nombre UNIQUE (nombre);

ALTER TABLE categoria_producto ADD CONSTRAINT uk_categoria_producto_nombre UNIQUE (nombre);

ALTER TABLE producto ADD CONSTRAINT uk_producto_sku UNIQUE (sku);