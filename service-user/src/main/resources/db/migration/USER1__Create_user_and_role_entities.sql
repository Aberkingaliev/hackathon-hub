CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS roles(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    role VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    full_name VARCHAR,
    email VARCHAR UNIQUE NOT NULL UNIQUE,
    password TEXT NOT NULL,
    is_activated BOOLEAN DEFAULT FALSE
);


CREATE TABLE IF NOT EXISTS user_to_role(
    user_id UUID REFERENCES users(id),
    role_id UUID REFERENCES roles(id),
    PRIMARY KEY(user_id, role_id)
);