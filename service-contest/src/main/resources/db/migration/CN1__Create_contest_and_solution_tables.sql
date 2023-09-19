CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

  CREATE TABLE IF NOT EXISTS contest_category (
      id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
      category_name VARCHAR(255) NOT NULL UNIQUE
  );

  CREATE TYPE contest_status AS ENUM ('OPEN_TO_SOLUTIONS', 'WINNER_IDENTIFYING', 'FINISHED');
  CREATE TYPE solution_status AS ENUM ('PENDING', 'WON');

  CREATE TABLE IF NOT EXISTS contests (
      id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
      status contest_status NOT NULL DEFAULT 'OPEN_TO_SOLUTIONS',
      owner_id UUID NOT NULL,
      name VARCHAR NOT NULL,
      description TEXT NOT NULL,
      end_date TIMESTAMP NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS contest_to_category (
      contest_id UUID NOT NULL,
      category_id UUID NOT NULL,
      PRIMARY KEY (contest_id, category_id),
      FOREIGN KEY (contest_id) REFERENCES contests (id) ON DELETE CASCADE,
      FOREIGN KEY (category_id) REFERENCES contest_category (id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS solutions (
      id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
      contest_id UUID NOT NULL,
      team_id UUID NOT NULL,
      status solution_status NOT NULL DEFAULT 'PENDING',
      name VARCHAR NOT NULL,
      description TEXT NOT NULL,
      url VARCHAR,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (contest_id) REFERENCES contests (id) ON DELETE CASCADE,
      FOREIGN KEY (team_id) REFERENCES teams (id) ON DELETE CASCADE
  );

  CREATE TABLE IF NOT EXISTS comment_to_solution (
      id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
      solution_id UUID NOT NULL,
      author_id UUID NOT NULL,
      comment TEXT NOT NULL,
      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
      FOREIGN KEY (solution_id) REFERENCES solutions (id) ON DELETE CASCADE,
      FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE
  );