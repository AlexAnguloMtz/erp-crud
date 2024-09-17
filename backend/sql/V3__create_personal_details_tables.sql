CREATE TABLE user_address(
    id UUID PRIMARY KEY,
    state_id VARCHAR(4) NOT NULL,
    city VARCHAR(60) NOT NULL,
    district VARCHAR(60) NOT NULL,
    street VARCHAR(60) NOT NULL,
    street_number VARCHAR(10) NOT NULL,
    zip_code CHAR(5) NOT NULL CHECK (zip_code ~ '^\d{5}$'),
    FOREIGN KEY (state_id) REFERENCES state(id)
);

CREATE TABLE user_personal_details(
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL,
    address_id UUID NOT NULL,
    name VARCHAR(60) NOT NULL,
    last_name VARCHAR(60) NOT NULL,
    phone CHAR(10) NOT NULL CHECK (phone ~ '^\d{10}$'),
    FOREIGN KEY (account_id) REFERENCES auth_user(id),
    FOREIGN KEY (address_id) REFERENCES user_address(id)
);