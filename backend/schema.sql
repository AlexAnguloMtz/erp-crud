CREATE TABLE roles(
    role_id UUID PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    canonical_name VARCHAR(20) NOT NULL
);

CREATE TABLE users (
    user_id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id UUID NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
);

CREATE TABLE states(
    state_id VARCHAR(4) PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE addresses(
    address_id UUID PRIMARY KEY,
    state_id VARCHAR(4) NOT NULL,
    city VARCHAR(60) NOT NULL,
    district VARCHAR(60) NOT NULL,
    street VARCHAR(60) NOT NULL,
    street_number VARCHAR(10) NOT NULL,
    zip_code CHAR(5) NOT NULL CHECK (zip_code ~ '^\d{5}$'),
    FOREIGN KEY (state_id) REFERENCES states(state_id)
);

CREATE TABLE personal_details(
    personal_details_id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    address_id UUID NOT NULL,
    name VARCHAR(60) NOT NULL,
    lastName VARCHAR(60) NOT NULL,
    phone CHAR(10) NOT NULL CHECK (phone ~ '^\d{10}$'),
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (address_id) REFERENCES addresses(address_id)
);

CREATE TABLE product_categories(
    product_category_id UUID PRIMARY KEY,
    name VARCHAR(60) NOT NULL
);

CREATE TABLE products(
    product_id UUID PRIMARY KEY,
    product_category_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (product_category_id) REFERENCES product_categories(product_category_id)
);

CREATE TABLE stock(
    stock_id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE TABLE stock_movement_type(
    stock_movement_type_id UUID PRIMARY KEY,
    description VARCHAR(60) NOT NULL
);

CREATE TABLE stock_movements(
    stock_movement_id UUID PRIMARY KEY,
    responsible_id UUID NOT NULL,
    stock_movement_type_id UUID NOT NULL,
    timestamp TIMESTAMPTZ NOT NULL,
    FOREIGN KEY (responsible_id) REFERENCES users(user_id),
    FOREIGN KEY (stock_movement_type_id) REFERENCES stock_movement_type(stock_movement_type_id)
);

CREATE TABLE stock_movement_product_details(
    stock_movement_product_details_id UUID PRIMARY KEY,
    product_id UUID NOT NULL,
    stock_movement_id UUID NOT NULL,
    quantity INTEGER NOT NULL CHECK (quantity >= 0),
    FOREIGN KEY (stock_movement_id) REFERENCES stock_movements(stock_movement_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

CREATE UNIQUE INDEX idx_users_email ON users (email);
CREATE UNIQUE INDEX idx_states_name ON states (name);
CREATE UNIQUE INDEX idx_product_categories_name ON product_categories(name);
CREATE UNIQUE INDEX idx_stock_movement_type_description ON stock_movement_type(description);