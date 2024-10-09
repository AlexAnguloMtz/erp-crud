CREATE TABLE cuenta_sucursal(
    cuenta_id INT NOT NULL,
    sucursal_id INT NOT NULL,
    PRIMARY KEY (cuenta_id, sucursal_id),
    FOREIGN KEY (cuenta_id) REFERENCES cuenta(id) ON UPDATE CASCADE ON DELETE CASCADE,
    FOREIGN KEY (sucursal_id) REFERENCES sucursal(id) ON UPDATE CASCADE ON DELETE CASCADE
);