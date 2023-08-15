CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TYPE role_enum AS ENUM ('admin', 'user');

CREATE TABLE users(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    fullName VARCHAR,
    email VARCHAR UNIQUE NOT NULL UNIQUE,
    password VARCHAR NOT NULL,
    isActivated BOOLEAN DEFAULT FALSE,
    teamId UUID DEFAULT NULL,
    role role_enum DEFAULT 'user'
);


CREATE INDEX idx_users_email ON users(email);