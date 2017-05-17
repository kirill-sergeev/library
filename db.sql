DROP SCHEMA public CASCADE;
CREATE SCHEMA public;

CREATE TABLE roles (
  id    SMALLSERIAL PRIMARY KEY,
  title VARCHAR(10) NOT NULL
);

CREATE TABLE users (
  id                SERIAL PRIMARY KEY,
  email             VARCHAR(255) NOT NULL UNIQUE,
  password          VARCHAR(255) NOT NULL,
  name              VARCHAR(50)  NOT NULL,
  activation_token  VARCHAR(255)          DEFAULT NULL UNIQUE,
  auth_token        VARCHAR(255)          DEFAULT NULL UNIQUE,
  reset_token       VARCHAR(255)          DEFAULT NULL UNIQUE,
  registration_date DATE         NOT NULL DEFAULT now(),
  last_visit        DATE                  DEFAULT NULL,
  enabled           BOOLEAN      NOT NULL DEFAULT FALSE,
  role_id           SMALLINT     NOT NULL DEFAULT 1 REFERENCES roles (id)
);

CREATE TABLE authors (
  id   SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE genres (
  id    SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE publishers (
  id    SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE books (
  id               SERIAL PRIMARY KEY,
  quantity         SMALLINT     NOT NULL DEFAULT 0,
  available        SMALLINT     NOT NULL DEFAULT 0,
  title            VARCHAR(255) NOT NULL UNIQUE,
  isbn             VARCHAR(13)           DEFAULT NULL UNIQUE,
  publisher_id     INT REFERENCES publishers (id),
  publication_date DATE                  DEFAULT NULL,
  description      TEXT
);

CREATE TABLE orders (
  id            SERIAL PRIMARY KEY,
  user_id       INT NOT NULL REFERENCES users (id),
  order_date    DATE DEFAULT NULL,
  expected_date DATE DEFAULT NULL,
  return_date   DATE DEFAULT NULL
);

CREATE TABLE fines (
  id       SERIAL PRIMARY KEY,
  order_id INT     NOT NULL REFERENCES orders (id),
  cost     DECIMAL     NOT NULL,
  paid     BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE books_authors (
  book_id   INT REFERENCES books (id) ON DELETE CASCADE,
  author_id INT REFERENCES authors (id) ON DELETE CASCADE,
  PRIMARY KEY (book_id, author_id)
);

CREATE TABLE books_genres (
  book_id  INT REFERENCES books (id) ON DELETE CASCADE,
  genre_id INT REFERENCES genres (id) ON DELETE CASCADE,
  PRIMARY KEY (book_id, genre_id)
);

CREATE TABLE books_orders (
  book_id  INT REFERENCES books (id) ON DELETE CASCADE,
  order_id INT REFERENCES orders (id) ON DELETE CASCADE,
  PRIMARY KEY (book_id, order_id)
);

