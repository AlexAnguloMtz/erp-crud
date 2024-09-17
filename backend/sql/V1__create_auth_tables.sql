CREATE TABLE auth_role(
    id UUID PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    canonical_name VARCHAR(20) NOT NULL
);

CREATE TABLE auth_user(
    id UUID PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    auth_role_id UUID NOT NULL,
    FOREIGN KEY (auth_role_id) REFERENCES auth_role(id)
);

ALTER TABLE auth_role ADD CONSTRAINT uk_auth_role_name UNIQUE (name);

ALTER TABLE auth_role ADD CONSTRAINT uk_auth_role_canonical_name UNIQUE (canonical_name);

ALTER TABLE auth_user ADD CONSTRAINT uk_auth_user_email UNIQUE (email);