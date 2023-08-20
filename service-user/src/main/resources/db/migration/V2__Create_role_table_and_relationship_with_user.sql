CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    role role_enum NOT NULL
);


CREATE TABLE user_to_role(
    user_id UUID REFERENCES users(id),
    role_id UUID REFERENCES roles(id),
    PRIMARY KEY(user_id, role_id)
);

ALTER TABLE users DROP COLUMN role;