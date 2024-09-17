CREATE TABLE brand(
    id UUID PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE product_category(
    id UUID PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE product(
    id UUID PRIMARY KEY,
    product_category_id UUID NOT NULL,
    brand_id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    FOREIGN KEY (product_category_id) REFERENCES product_category(id),
    FOREIGN KEY (brand_id) REFERENCES brand(id)
);

CREATE TABLE stock(
    id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    FOREIGN KEY (product_id) REFERENCES product(id)
);

ALTER TABLE brand ADD CONSTRAINT uk_brand_name UNIQUE (name);

ALTER TABLE product_category ADD CONSTRAINT uk_product_category_name UNIQUE (name);