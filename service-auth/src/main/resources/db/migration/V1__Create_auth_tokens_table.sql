CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE authTokens(
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    userId UUID,
    accessToken TEXT,
    refreshToken TEXT UNIQUE NOT NULL,
    createdAt TIMESTAMP NOT NULL
);