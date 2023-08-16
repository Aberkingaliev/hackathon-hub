CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE authTokens(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    userId UUID,
    accessToken VARCHAR,
    refreshToken VARCHAR UNIQUE,
    createdAt TIMESTAMP NOT NULL
);