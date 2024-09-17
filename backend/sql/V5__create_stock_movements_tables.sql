CREATE TABLE stock_movement_type(
    id UUID PRIMARY KEY,
    description VARCHAR(60) NOT NULL
);

CREATE TABLE stock_movement(
    id UUID PRIMARY KEY,
    responsible_id UUID NOT NULL,
    stock_movement_type_id UUID NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    observations VARCHAR(255) NULL,
    FOREIGN KEY (responsible_id) REFERENCES auth_user(id),
    FOREIGN KEY (stock_movement_type_id) REFERENCES stock_movement_type(id)
);

CREATE TABLE stock_movement_product(
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    stock_movement_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    FOREIGN KEY (stock_movement_id) REFERENCES stock_movement(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

ALTER TABLE stock_movement_type ADD CONSTRAINT uk_stock_movement_type_description UNIQUE (description);