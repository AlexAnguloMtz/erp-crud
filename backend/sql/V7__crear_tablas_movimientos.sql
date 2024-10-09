CREATE TABLE tipo_movimiento_inventario(
    id SERIAL PRIMARY KEY,
    descripcion VARCHAR(60) NOT NULL
);

CREATE TABLE movimiento_inventario(
    id SERIAL PRIMARY KEY,
    responsable_id INT NOT NULL,
    tipo_movimiento_inventario_id INT NOT NULL,
    creado TIMESTAMPTZ NOT NULL,
    observaciones VARCHAR(255) NULL,
    FOREIGN KEY (responsable_id) REFERENCES cuenta(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (tipo_movimiento_inventario_id) REFERENCES tipo_movimiento_inventario(id) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE movimiento_inventario_producto(
    id SERIAL PRIMARY KEY,
    producto_id INT NOT NULL,
    movimiento_inventario_id INT NOT NULL,
    cantidad INTEGER NOT NULL CHECK (cantidad >= 0),
    FOREIGN KEY (movimiento_inventario_id) REFERENCES movimiento_inventario(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON UPDATE CASCADE ON DELETE CASCADE
);

ALTER TABLE tipo_movimiento_inventario ADD CONSTRAINT uk_tipo_movimiento_inventario_descripcion UNIQUE (descripcion);