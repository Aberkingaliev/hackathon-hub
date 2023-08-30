CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE roles(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    role VARCHAR NOT NULL
);

CREATE TABLE users(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    fullName VARCHAR,
    email VARCHAR UNIQUE NOT NULL UNIQUE,
    password TEXT NOT NULL,
    isActivated BOOLEAN DEFAULT FALSE,
    teamId UUID DEFAULT NULL
);


CREATE TABLE user_to_role(
    user_id UUID REFERENCES users(id),
    role_id UUID REFERENCES roles(id),
    PRIMARY KEY(user_id, role_id)
);


CREATE INDEX idx_users_email ON users(email);