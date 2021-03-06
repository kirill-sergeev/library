CREATE DATABASE library;
CREATE USER kirill WITH password 'kirill';
CREATE ROLE webapp;
GRANT SELECT, INSERT, DELETE, UPDATE ON ALL TABLES IN SCHEMA public TO webapp;
GRANT webapp TO kirill;
GRANT postgres TO kirill;

SHOW SERVER_ENCODING;
SHOW CLIENT_ENCODING;

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
  last_visit        DATE                  DEFAULT now(),
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
  reader_id     INT     NOT NULL REFERENCES users (id),
  librarian_id  INT              DEFAULT NULL REFERENCES users (id),
  internal      BOOLEAN NOT NULL DEFAULT FALSE,
  order_date    DATE             DEFAULT NULL,
  expected_date DATE             DEFAULT NULL,
  return_date   DATE             DEFAULT NULL
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

-- --------------
CREATE OR REPLACE FUNCTION trg_available_books()
  RETURNS TRIGGER AS $$
BEGIN
  NEW.available = NEW.quantity;
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_available_books
ON books;
CREATE TRIGGER trg_available_books
BEFORE INSERT ON books
FOR EACH ROW
WHEN (NEW.available = 0)
EXECUTE PROCEDURE trg_available_books();

-- --------------

CREATE OR REPLACE FUNCTION trg_useless_authors()
  RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM authors
  WHERE id NOT IN (SELECT author_id
                   FROM books_authors);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_useless_authors_on_books
ON books;
CREATE TRIGGER trg_useless_authors_on_books
AFTER UPDATE OR DELETE ON books
FOR EACH STATEMENT
EXECUTE PROCEDURE trg_useless_authors();
-- --------------

CREATE OR REPLACE FUNCTION trg_useless_genres()
  RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM genres
  WHERE id NOT IN (SELECT genre_id
                   FROM books_genres);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_useless_genres_on_books
ON books;
CREATE TRIGGER trg_useless_genres_on_books
AFTER UPDATE OR DELETE ON books
FOR EACH STATEMENT
EXECUTE PROCEDURE trg_useless_genres();
-- -----

CREATE OR REPLACE FUNCTION trg_useless_publishers()
  RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM publishers
  WHERE id NOT IN (SELECT publisher_id
                   FROM books);
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_useless_publishers_on_books
ON books;
CREATE TRIGGER trg_useless_publishers_on_books
AFTER UPDATE OR DELETE ON books
FOR EACH STATEMENT
EXECUTE PROCEDURE trg_useless_publishers();
-- ------

CREATE OR REPLACE FUNCTION trg_unconfirmed_users()
  RETURNS TRIGGER AS $$
BEGIN
  DELETE FROM users
  WHERE id IN (SELECT id
               FROM users
               WHERE activation_token IS NOT NULL AND registration_date < now() - INTERVAL '7 days');
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_unconfirmed_users
ON users;
CREATE TRIGGER trg_unconfirmed_users
BEFORE INSERT ON users
FOR EACH STATEMENT
EXECUTE PROCEDURE trg_unconfirmed_users();

-- ----------

INSERT INTO roles (title) VALUES ('READER'), ('LIBRARIAN'), ('ADMIN');
INSERT INTO users (email, password, name, role_id, enabled)
VALUES ('kirill@kiril.com', '$31$16$swNTVBk_Hfj8wsqtWyy6EhmJBuckYl9LA_kl2NzRPYg', 'Kirill Sergeev', 2, TRUE),
  ('ivan@ivan.ua', '$31$16$swNTVBk_Hfj8wsqtWyy6EhmJBuckYl9LA_kl2NzRPYg', 'Ivan Ivanov', 1, TRUE);

INSERT INTO authors (name)
VALUES ('Heather Lindsey'), ('Alice Y. Lang'), ('Sopoline Y. Floyd'), ('Kylan Carter'), ('Eric Mclean'),
  ('Quintessa Franks'), ('Brooke Taylor'), ('Kieran Matthews'), ('Clementine C. Dawson'), ('Megan Hurst'),
  ('Chaim X. Guy'), ('Pearl Carson'), ('Kai Christian'), ('Igor Cervantes'), ('Shad Brock'), ('Deborah Wright'),
  ('Kyle B. Best'), ('Ainsley Alford'), ('Ulla Barton'), ('Brennan Mcpherson'), ('Noah F. Hensley'),
  ('Zenaida B. Wynn'), ('Lenore Tran'), ('Scarlet Ward'), ('Yuri Weeks'), ('Aaron K. Schroeder'), ('Karina A. Barry'),
  ('Jasmine A. Holloway'), ('Heather Z. Ayala'), ('Bert P. Stanley'), ('Maile Kent'), ('Tatiana D. Watkins'),
  ('Andrew Abbott'), ('Lawrence Chase'), ('Gisela Vinson'), ('Amity X. Joyce'), ('Dustin P. Mcgee'), ('Uriah F. Riley'),
  ('Henry E. Hopkins'), ('Linus Alford'), ('Jakeem Valentine'), ('Tatiana P. Tran'), ('Meredith Day'),
  ('Zeph C. Harrison'), ('Ian Q. Walters'), ('Calvin Nelson'), ('Lucas Lancaster'), ('Noble Pierce'), ('Kyra Lang'),
  ('Victor Thomas'), ('Blake O. Park'), ('Bethany Hammond'), ('Amaya J. Berg'), ('Chelsea Freeman'), ('Ray Q. Ross'),
  ('Berk K. Fitzpatrick'), ('Astra Norman'), ('Plato Doyle'), ('Samantha Mueller'), ('Alexa Goodwin'),
  ('Raphael M. Gill'), ('Rafael Rojas'), ('Fitzgerald B. Frank'), ('Garrison Q. Hicks'), ('Amir K. Pratt'),
  ('Cruz Barrera'), ('Sasha Frederick'), ('Glenna T. Norris'), ('Jermaine Wilkinson'), ('Wallace Kemp'),
  ('Shelly Gross'), ('Garth P. Barnett'), ('Geraldine D. Brooks'), ('Riley Kirkland'), ('Steel Black'),
  ('Chancellor Rosa'), ('Simone P. Benton'), ('Keith Jenkins'), ('Aphrodite H. Miller'), ('Emery P. Hammond'),
  ('Ethan Ayers'), ('Holmes J. Mckay'), ('Linda Hale'), ('Jennifer T. Silva'), ('Fletcher L. Black'), ('Tara Mcmahon'),
  ('Susan I. Velez'), ('Blaine Bartlett'), ('Jackson B. Ashley'), ('Nathan Q. Fischer'), ('Keith I. Potter'),
  ('Hall Mullen'), ('Freya Stout'), ('Brent C. Kelly'), ('Aimee L. Wilkinson'), ('Freya R. Perry'), ('Idola T. Bender'),
  ('Jeanette Cobb'), ('Kyla Nixon'), ('Calista Burris');
INSERT INTO publishers (title)
VALUES ('Pretium Aliquet Company'), ('Magnis Dis Associates'), ('Blandit Nam Ltd'), ('Nunc Interdum Corp.'),
  ('Eu Inc.'), ('Aliquam Enim Nec Ltd'), ('Neque Sed Eget Company'), ('Quam LLC'), ('Malesuada Vel PC'),
  ('Nibh Associates'), ('Tellus Aenean Egestas PC'), ('In Faucibus Inc.'), ('Vestibulum Corporation'), ('Laoreet LLP'),
  ('Gravida Praesent Eu Consulting'), ('Diam Consulting'), ('Consectetuer Euismod Inc.'),
  ('Vitae Orci Phasellus Associates'), ('Felis Purus Consulting'), ('Ultricies Ornare Elit Corp.');

INSERT INTO genres (title) VALUES ('fiction'), ('comedy'), ('drama'), ('horror'), ('non-fiction'), ('realistic
fiction'), ('romance'), ('satire'), ('tragedy'), ('tragicomedy'), ('fantasy'), ('mythology'), ('classic'),
  ('detective'), ('fairy tale'), ('science fiction'), ('humor'), ('legend'), ('memoir'), ('folklore');

INSERT INTO books (quantity, title, isbn, publisher_id, publication_date, description) VALUES
  (13, 'Poederlee', 4283993937075, 14, '1978-03-09',
   'facilisis eget, ipsum. Donec sollicitudin adipiscing ligula. Aenean gravida nunc sed pede. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin vel arcu eu odio tristique pharetra. Quisque ac libero nec ligula consectetuer rhoncus. Nullam velit dui,'),
  (18, 'Ferrazzano', 2580075550824, 20, '2001-07-10', 'Duis volutpat nunc sit amet metus. Aliquam erat volutpat. Nulla facilisis. Suspendisse commodo tincidunt nibh. Phasellus nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor lorem, eget mollis lectus pede et risus. Quisque libero lacus,'),
  (14, 'Bonlez', 8015949431807, 20, '1952-09-28', 'fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse non leo. Vivamus nibh dolor, nonummy ac, feugiat non, lobortis quis, pede. Suspendisse dui. Fusce diam nunc, ullamcorper eu, euismod ac, fermentum vel,'),
  (9, 'Kingston', 1730432227254, 2, '1977-02-09', 'nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse non leo. Vivamus nibh dolor, nonummy ac, feugiat non, lobortis quis, pede. Suspendisse'),
  (3, 'Brunn am Gebirge', 7547961298377, 11, '1979-12-06', 'neque et nunc. Quisque ornare tortor at risus. Nunc ac sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse'),
  (16, 'Omaha', 3840310443193, 3, '2014-11-10', 'mi. Aliquam gravida mauris ut mi. Duis risus odio, auctor vitae, aliquet nec, imperdiet nec, leo. Morbi neque tellus, imperdiet non, vestibulum nec, euismod in, dolor. Fusce feugiat. Lorem ipsum'),
  (5, 'Clearwater Municipal District', 8755562968553, 3, '2014-10-23', 'Duis risus odio, auctor vitae, aliquet nec, imperdiet nec, leo. Morbi neque tellus, imperdiet non, vestibulum nec, euismod in, dolor. Fusce feugiat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit.'),
  (6, 'Shippagan', 2663412258029, 1, '2002-04-18', 'sit amet, consectetuer adipiscing elit. Curabitur sed tortor. Integer aliquam adipiscing lacus. Ut nec urna et arcu imperdiet ullamcorper. Duis at lacus. Quisque purus sapien, gravida non, sollicitudin a, malesuada id, erat. Etiam vestibulum massa'),
  (19, 'Clydebank', 6617617163807, 4, '1990-03-17', 'vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse non leo. Vivamus nibh dolor, nonummy ac, feugiat non, lobortis quis, pede. Suspendisse dui. Fusce diam nunc, ullamcorper eu, euismod ac, fermentum vel, mauris. Integer sem elit, pharetra ut, pharetra sed, hendrerit a, arcu. Sed et'),
  (17, 'Lauterach', 4889152232558, 18, '1971-10-28', 'sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra nibh. Aliquam ornare, libero at auctor ullamcorper, nisl arcu iaculis enim, sit amet ornare lectus justo eu arcu. Morbi sit amet massa. Quisque porttitor eros nec tellus. Nunc lectus pede, ultrices a, auctor non,'),
  (7, 'Traiskirchen', 2913059543818, 16, '1951-09-25', 'ullamcorper. Duis at lacus. Quisque purus sapien, gravida non, sollicitudin a, malesuada id, erat. Etiam vestibulum massa rutrum magna. Cras convallis convallis dolor. Quisque tincidunt pede ac urna. Ut tincidunt vehicula risus. Nulla eget metus eu erat semper rutrum. Fusce dolor quam, elementum at, egestas a, scelerisque sed,'),
  (15, 'Åkersberga', 6373491130769, 20, '1987-05-26', 'dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse non leo. Vivamus nibh dolor, nonummy ac, feugiat non, lobortis quis, pede. Suspendisse dui. Fusce'),
  (20, 'Isca sullo Ionio', 8449626188726, 10, '1985-08-16', 'felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo ipsum. Suspendisse non leo. Vivamus nibh dolor, nonummy ac, feugiat non, lobortis quis, pede. Suspendisse dui. Fusce diam nunc, ullamcorper eu, euismod ac, fermentum vel, mauris. Integer sem elit, pharetra ut, pharetra'),
  (4, 'Kruibeke', 2348166234791, 1, '1979-06-17', 'lacus, varius et, euismod et, commodo at, libero. Morbi accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus vulputate, nisi sem semper erat, in consectetuer ipsum nunc id enim. Curabitur massa. Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem'),
  (0, 'Baden', 5988978520036, 18, '2016-09-22', 'lectus pede, ultrices a, auctor non, feugiat nec, diam. Duis mi enim, condimentum eget, volutpat ornare, facilisis eget, ipsum. Donec sollicitudin adipiscing ligula. Aenean gravida nunc sed pede. Cum sociis natoque penatibus et magnis dis'),
  (8, 'Motala', 6704879343510, 4, '1984-06-01', 'dignissim pharetra. Nam ac nulla. In tincidunt congue turpis. In condimentum. Donec at arcu. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Donec tincidunt. Donec vitae erat vel pede blandit congue. In scelerisque scelerisque dui. Suspendisse'),
  (5, 'Yorkton', 3842192873359, 19, '1984-02-26', 'eu dui. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aenean eget magna. Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem'),
  (18, 'Ligosullo', 2866929616779, 14, '1979-04-28', 'Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem, sit amet ultricies sem magna nec quam. Curabitur vel lectus. Cum sociis natoque penatibus'),
  (20, 'Cache Creek', 6226597443223, 16, '2002-07-12', 'rutrum eu, ultrices sit amet, risus. Donec nibh enim, gravida sit amet, dapibus id, blandit at, nisi. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin vel nisl. Quisque fringilla euismod enim. Etiam gravida molestie arcu. Sed eu nibh vulputate mauris'),
  (10, 'Tresigallo', 6551579445601, 16, '1983-06-09', 'nisl arcu iaculis enim, sit amet ornare lectus justo eu arcu. Morbi sit amet massa. Quisque porttitor eros nec tellus. Nunc lectus pede, ultrices a, auctor non, feugiat nec, diam. Duis mi enim, condimentum'),
  (10, 'Goulburn', 5533749382943, 15, '1952-02-03', 'metus facilisis lorem tristique aliquet. Phasellus fermentum convallis ligula. Donec luctus aliquet odio. Etiam ligula tortor, dictum eu, placerat eget, venenatis a, magna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Etiam laoreet, libero et tristique pellentesque, tellus sem mollis dui, in sodales elit erat vitae risus. Duis a'),
  (10, 'Knokke-Heist', 1712795246392, 6, '1973-12-06', 'eu dui. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Aenean eget magna. Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem,'),
  (12, 'Cache Creek', 1309156779199, 4, '1987-01-13', 'libero est, congue a, aliquet vel, vulputate eu, odio. Phasellus at augue id ante dictum cursus. Nunc mauris elit, dictum eu, eleifend nec, malesuada ut, sem. Nulla interdum. Curabitur dictum. Phasellus in felis. Nulla tempor augue ac ipsum. Phasellus vitae mauris sit amet lorem'),
  (4, 'Virton', 2584766771644, 17, '2008-10-23', 'sem eget massa. Suspendisse eleifend. Cras sed leo. Cras vehicula aliquet libero. Integer in magna. Phasellus dolor elit, pellentesque a, facilisis non, bibendum sed, est. Nunc laoreet lectus quis massa. Mauris vestibulum, neque sed dictum'),
  (7, 'Castel Volturno', 1765986263752, 15, '1996-12-21', 'erat vitae risus. Duis a mi fringilla mi lacinia mattis. Integer eu lacus. Quisque imperdiet, erat nonummy ultricies ornare, elit elit fermentum risus, at fringilla purus mauris a nunc. In at pede. Cras vulputate velit eu sem.'),
  (6, 'Columbia', 8975669428707, 2, '2011-12-16', 'Duis risus odio, auctor vitae, aliquet nec, imperdiet nec, leo. Morbi neque tellus, imperdiet non, vestibulum nec, euismod in, dolor. Fusce feugiat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam auctor, velit eget laoreet posuere, enim nisl'),
  (5, 'Vihari', 6157241001725, 1, '1975-12-09', 'et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus. Nullam scelerisque neque sed sem egestas blandit. Nam nulla magna, malesuada vel, convallis in, cursus et, eros. Proin ultrices. Duis volutpat nunc sit amet metus. Aliquam erat'),
  (7, 'Oban', 4499342292547, 2, '2000-08-02', 'aliquet diam. Sed diam lorem, auctor quis, tristique ac, eleifend vitae, erat. Vivamus nisi. Mauris nulla. Integer urna. Vivamus molestie dapibus ligula. Aliquam erat volutpat. Nulla dignissim. Maecenas ornare egestas ligula. Nullam feugiat placerat velit.'),
  (15, 'Ninhue', 7539595447481, 16, '1965-04-17', 'aliquet magna a neque. Nullam ut nisi a odio semper cursus. Integer mollis. Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non nisi. Aenean eget metus. In nec orci. Donec nibh. Quisque'),
  (18, 'Launceston', 3113037448376, 12, '1977-01-22', 'fringilla. Donec feugiat metus sit amet ante. Vivamus non lorem vitae odio sagittis semper. Nam tempor diam dictum sapien. Aenean massa. Integer vitae nibh. Donec est mauris, rhoncus id, mollis nec, cursus a, enim. Suspendisse aliquet, sem ut cursus luctus,'),
  (6, 'Indore', 1840795222669, 6, '1971-02-06', 'id, blandit at, nisi. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin vel nisl. Quisque fringilla euismod enim. Etiam gravida molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies'),
  (13, 'İskenderun', 2758659984916, 20, '1964-02-28', 'accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus vulputate, nisi sem semper erat, in consectetuer ipsum nunc id enim. Curabitur massa. Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem ut dolor dapibus'),
  (17, 'Belgrave', 3748168520629, 3, '2004-11-18', 'ligula. Aenean euismod mauris eu elit. Nulla facilisi. Sed neque. Sed eget lacus. Mauris non dui nec urna suscipit nonummy. Fusce fermentum fermentum arcu. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Phasellus ornare. Fusce mollis. Duis sit amet diam eu dolor egestas rhoncus. Proin'),
  (7, 'Galbiate', 3740833383053, 13, '2015-01-11', 'tempus scelerisque, lorem ipsum sodales purus, in molestie tortor nibh sit amet orci. Ut sagittis lobortis mauris. Suspendisse aliquet molestie tellus. Aenean egestas hendrerit neque. In ornare sagittis felis. Donec tempor, est ac mattis'),
  (4, 'Jerez de la Frontera', 4993615828454, 16, '1985-06-07', 'fermentum risus, at fringilla purus mauris a nunc. In at pede. Cras vulputate velit eu sem. Pellentesque ut ipsum ac mi eleifend egestas. Sed pharetra, felis eget varius ultrices, mauris ipsum porta elit, a feugiat tellus lorem eu metus. In lorem.'),
  (7, 'Sint-Ulriks-Kapelle', 4616567775607, 14, '1977-12-06', 'Morbi non sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra nibh. Aliquam ornare, libero at auctor ullamcorper, nisl arcu iaculis enim, sit amet ornare lectus justo eu arcu. Morbi sit amet massa. Quisque porttitor eros nec tellus. Nunc'),
  (20, 'Portezuelo', 3218715656549, 6, '1974-07-14', 'id nunc interdum feugiat. Sed nec metus facilisis lorem tristique aliquet. Phasellus fermentum convallis ligula. Donec luctus aliquet odio. Etiam ligula tortor, dictum eu, placerat eget, venenatis a, magna. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Etiam'),
  (14, 'Mannekensvere', 8907962393016, 6, '2003-08-24', 'pharetra, felis eget varius ultrices, mauris ipsum porta elit, a feugiat tellus lorem eu metus. In lorem. Donec elementum, lorem ut aliquam iaculis, lacus pede sagittis augue, eu tempor erat neque non quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus.'),
  (1, 'Palermo', 2815698679536, 19, '1993-06-09', 'mattis. Cras eget nisi dictum augue malesuada malesuada. Integer id magna et ipsum cursus vestibulum. Mauris magna. Duis dignissim tempor arcu. Vestibulum ut eros non enim commodo hendrerit. Donec porttitor tellus non magna. Nam ligula elit, pretium et, rutrum non, hendrerit id, ante. Nunc mauris'),
  (8, 'Waalwijk', 5436025075615, 14, '1988-06-18', 'orci luctus et ultrices posuere cubilia Curae; Donec tincidunt. Donec vitae erat vel pede blandit congue. In scelerisque scelerisque dui. Suspendisse ac metus vitae velit egestas lacinia. Sed congue, elit sed consequat auctor, nunc nulla vulputate dui, nec tempus mauris'),
  (14, 'Sijsele', 7681357927621, 18, '1969-02-25', 'Phasellus ornare. Fusce mollis. Duis sit amet diam eu dolor egestas rhoncus. Proin nisl sem, consequat nec, mollis vitae, posuere at, velit. Cras lorem lorem, luctus ut, pellentesque eget, dictum placerat, augue. Sed'),
  (11, 'Oelegem', 3003726843744, 9, '1988-09-01', 'et ultrices posuere cubilia Curae; Donec tincidunt. Donec vitae erat vel pede blandit congue. In scelerisque scelerisque dui. Suspendisse ac metus vitae velit egestas lacinia. Sed congue, elit sed consequat auctor, nunc nulla vulputate dui, nec tempus mauris erat eget ipsum. Suspendisse sagittis. Nullam vitae diam. Proin dolor. Nulla semper'),
  (5, 'Alassio', 7844115145505, 16, '1989-01-08', 'tortor at risus. Nunc ac sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere'),
  (11, 'Suxy', 4598161749542, 3, '1988-09-28', 'risus. Nulla eget metus eu erat semper rutrum. Fusce dolor quam, elementum at, egestas a, scelerisque sed, sapien. Nunc pulvinar arcu et pede. Nunc sed orci lobortis augue scelerisque mollis. Phasellus libero mauris, aliquam eu, accumsan sed, facilisis'),
  (1, 'Trochu', 8470853801817, 12, '1962-04-17', 'sagittis. Duis gravida. Praesent eu nulla at sem molestie sodales. Mauris blandit enim consequat purus. Maecenas libero est, congue a, aliquet vel, vulputate eu, odio. Phasellus at augue id ante dictum cursus. Nunc mauris elit,'),
  (0, 'Dijon', 7292381942272, 19, '1953-11-23', 'ridiculus mus. Proin vel arcu eu odio tristique pharetra. Quisque ac libero nec ligula consectetuer rhoncus. Nullam velit dui, semper et, lacinia vitae, sodales at, velit. Pellentesque ultricies dignissim lacus. Aliquam rutrum lorem ac risus. Morbi metus. Vivamus euismod urna. Nullam lobortis quam'),
  (19, 'Villers-la-Loue', 3953161191195, 7, '1951-08-12', 'tortor. Integer aliquam adipiscing lacus. Ut nec urna et arcu imperdiet ullamcorper. Duis at lacus. Quisque purus sapien, gravida non, sollicitudin a, malesuada id, erat. Etiam vestibulum massa rutrum magna. Cras convallis convallis dolor. Quisque tincidunt pede ac urna. Ut tincidunt vehicula risus. Nulla eget'),
  (10, 'Duncan', 5632480677217, 9, '1952-01-19', 'lorem, auctor quis, tristique ac, eleifend vitae, erat. Vivamus nisi. Mauris nulla. Integer urna. Vivamus molestie dapibus ligula. Aliquam erat volutpat. Nulla dignissim. Maecenas ornare egestas ligula. Nullam feugiat placerat velit. Quisque varius. Nam porttitor scelerisque neque.'),
  (4, 'Ferlach', 8078925319016, 14, '1974-04-10', 'sociosqu ad litora torquent per conubia nostra, per inceptos hymenaeos. Mauris ut quam vel sapien imperdiet ornare. In faucibus. Morbi vehicula. Pellentesque tincidunt tempus risus. Donec egestas. Duis ac arcu. Nunc mauris. Morbi non sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra nibh. Aliquam ornare, libero'),
  (13, 'Crato', 7656802438200, 7, '1970-01-06', 'Cras eget nisi dictum augue malesuada malesuada. Integer id magna et ipsum cursus vestibulum. Mauris magna. Duis dignissim tempor arcu. Vestibulum ut eros non enim commodo hendrerit. Donec porttitor tellus non magna. Nam ligula elit, pretium et, rutrum non, hendrerit id, ante. Nunc mauris sapien, cursus'),
  (7, 'Montereale', 1378664020448, 7, '2012-08-19', 'mauris sapien, cursus in, hendrerit consectetuer, cursus et, magna. Praesent interdum ligula eu enim. Etiam imperdiet dictum magna. Ut tincidunt orci quis lectus. Nullam suscipit, est ac facilisis facilisis, magna tellus faucibus leo, in lobortis tellus justo sit amet nulla. Donec non justo. Proin non massa'),
  (14, 'Curicó', 2038037635386, 9, '2000-07-16', 'ac, eleifend vitae, erat. Vivamus nisi. Mauris nulla. Integer urna. Vivamus molestie dapibus ligula. Aliquam erat volutpat. Nulla dignissim. Maecenas ornare egestas ligula. Nullam feugiat placerat velit. Quisque varius. Nam porttitor scelerisque neque. Nullam nisl. Maecenas malesuada fringilla est. Mauris eu'),
  (11, 'Owen Sound', 1582288827747, 16, '1974-02-18', 'Suspendisse aliquet, sem ut cursus luctus, ipsum leo elementum sem, vitae aliquam eros turpis non enim. Mauris quis turpis vitae purus gravida sagittis. Duis gravida. Praesent eu nulla at sem molestie sodales. Mauris blandit enim consequat purus. Maecenas libero est, congue a,'),
  (10, 'Jerez de la Frontera', 4993094693869, 17, '2018-01-09', 'vitae purus gravida sagittis. Duis gravida. Praesent eu nulla at sem molestie sodales. Mauris blandit enim consequat purus. Maecenas libero est, congue a, aliquet vel, vulputate eu, odio. Phasellus at augue id ante dictum cursus. Nunc mauris'),
  (15, 'Keiem', 8310388207436, 5, '2007-11-29', 'odio. Phasellus at augue id ante dictum cursus. Nunc mauris elit, dictum eu, eleifend nec, malesuada ut, sem. Nulla interdum. Curabitur dictum. Phasellus in felis. Nulla tempor augue ac ipsum. Phasellus vitae mauris sit amet'),
  (12, 'Griesheim', 1237799420952, 4, '1967-01-14', 'et ipsum cursus vestibulum. Mauris magna. Duis dignissim tempor arcu. Vestibulum ut eros non enim commodo hendrerit. Donec porttitor tellus non magna. Nam ligula elit, pretium et, rutrum non, hendrerit id, ante. Nunc mauris sapien, cursus in, hendrerit consectetuer, cursus'),
  (15, 'Leganés', 6689983982593, 4, '1983-05-05', 'Phasellus dolor elit, pellentesque a, facilisis non, bibendum sed, est. Nunc laoreet lectus quis massa. Mauris vestibulum, neque sed dictum eleifend, nunc risus varius orci, in consequat enim diam vel arcu. Curabitur ut'),
  (20, 'Karapınar', 3997666377574, 20, '1979-12-17', 'gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo'),
  (19, 'Chhindwara', 6565260488540, 10, '1982-06-10', 'eget magna. Suspendisse tristique neque venenatis lacus. Etiam bibendum fermentum metus. Aenean sed pede nec ante blandit viverra. Donec tempus, lorem fringilla ornare placerat, orci lacus vestibulum lorem, sit amet ultricies sem magna nec quam. Curabitur vel lectus. Cum sociis natoque penatibus et magnis'),
  (3, 'Pietrasanta', 7866797748953, 2, '1987-11-20', 'dapibus quam quis diam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut nisi a odio semper cursus. Integer mollis. Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non nisi. Aenean eget metus.'),
  (4, 'Leiden', 1598010636866, 3, '1956-01-02', 'sodales purus, in molestie tortor nibh sit amet orci. Ut sagittis lobortis mauris. Suspendisse aliquet molestie tellus. Aenean egestas hendrerit neque. In ornare sagittis felis. Donec tempor, est ac mattis semper, dui lectus rutrum urna, nec luctus felis purus ac tellus. Suspendisse sed'),
  (12, 'Kaster', 4359261687845, 7, '1977-01-14', 'aliquam iaculis, lacus pede sagittis augue, eu tempor erat neque non quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus. Nullam scelerisque neque sed sem egestas blandit. Nam nulla magna, malesuada vel, convallis in, cursus et,'),
  (10, 'Kessenich', 6902709092945, 5, '2012-11-21', 'ultrices a, auctor non, feugiat nec, diam. Duis mi enim, condimentum eget, volutpat ornare, facilisis eget, ipsum. Donec sollicitudin adipiscing ligula. Aenean gravida nunc sed pede. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin vel arcu eu odio tristique pharetra. Quisque ac'),
  (10, 'Jodhpur', 1499615691602, 11, '1952-05-15', 'Curabitur massa. Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem ut dolor dapibus gravida. Aliquam tincidunt, nunc ac mattis ornare, lectus ante dictum mi, ac mattis velit justo nec ante. Maecenas mi felis, adipiscing fringilla, porttitor vulputate, posuere vulputate, lacus. Cras interdum. Nunc sollicitudin commodo'),
  (17, 'Gadag Betigeri', 2122103922069, 11, '1983-03-27', 'eleifend nec, malesuada ut, sem. Nulla interdum. Curabitur dictum. Phasellus in felis. Nulla tempor augue ac ipsum. Phasellus vitae mauris sit amet lorem semper auctor. Mauris vel turpis. Aliquam adipiscing lobortis risus. In mi pede, nonummy'),
  (5, 'Argyle', 7528833173216, 12, '2005-05-17', 'nostra, per inceptos hymenaeos. Mauris ut quam vel sapien imperdiet ornare. In faucibus. Morbi vehicula. Pellentesque tincidunt tempus risus. Donec egestas. Duis ac arcu. Nunc mauris. Morbi non sapien molestie orci tincidunt adipiscing. Mauris molestie pharetra nibh. Aliquam ornare, libero at auctor ullamcorper, nisl arcu'),
  (3, 'Sachs Harbour', 2164061326533, 7, '1997-03-18', 'non, lobortis quis, pede. Suspendisse dui. Fusce diam nunc, ullamcorper eu, euismod ac, fermentum vel, mauris. Integer sem elit, pharetra ut, pharetra sed, hendrerit a, arcu. Sed et libero. Proin mi. Aliquam gravida mauris ut mi. Duis risus odio, auctor vitae, aliquet nec,'),
  (7, 'Bad Ischl', 3884281918406, 12, '1952-08-05', 'libero. Proin mi. Aliquam gravida mauris ut mi. Duis risus odio, auctor vitae, aliquet nec, imperdiet nec, leo. Morbi neque tellus, imperdiet non, vestibulum nec, euismod in, dolor. Fusce feugiat. Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aliquam auctor, velit eget laoreet posuere, enim nisl elementum purus,'),
  (9, 'Asti', 2666923705488, 7, '1992-02-22', 'sollicitudin a, malesuada id, erat. Etiam vestibulum massa rutrum magna. Cras convallis convallis dolor. Quisque tincidunt pede ac urna. Ut tincidunt vehicula risus. Nulla eget metus eu erat semper rutrum. Fusce dolor quam, elementum at, egestas a, scelerisque sed, sapien.'),
  (8, 'Campbelltown', 3855180781334, 20, '1964-12-14', 'amet, consectetuer adipiscing elit. Curabitur sed tortor. Integer aliquam adipiscing lacus. Ut nec urna et arcu imperdiet ullamcorper. Duis at lacus. Quisque purus sapien, gravida non, sollicitudin a, malesuada id, erat. Etiam vestibulum massa rutrum magna. Cras convallis convallis dolor. Quisque tincidunt pede ac urna. Ut tincidunt vehicula risus. Nulla'),
  (17, 'Montpelier', 8704218596220, 4, '1980-10-10', 'Fusce aliquet magna a neque. Nullam ut nisi a odio semper cursus. Integer mollis. Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non nisi. Aenean eget metus. In nec orci. Donec nibh.'),
  (2, 'Tamworth', 3394180074334, 12, '1969-03-24', 'Fusce aliquam, enim nec tempus scelerisque, lorem ipsum sodales purus, in molestie tortor nibh sit amet orci. Ut sagittis lobortis mauris. Suspendisse aliquet molestie tellus. Aenean egestas hendrerit neque. In ornare'),
  (10, 'La Valle/Wengen', 6155287146568, 18, '2002-05-31', 'quis diam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Fusce aliquet magna a neque. Nullam ut nisi a odio semper cursus. Integer mollis. Integer tincidunt aliquam arcu. Aliquam ultrices iaculis odio. Nam interdum enim non'),
  (1, 'Castelbaldo', 8653316359967, 10, '1953-09-01', 'orci, adipiscing non, luctus sit amet, faucibus ut, nulla. Cras eu tellus eu augue porttitor interdum. Sed auctor odio a purus. Duis elementum, dui quis accumsan convallis, ante lectus convallis est, vitae sodales nisi magna sed dui. Fusce aliquam, enim nec tempus'),
  (0, 'Serik', 3925587654114, 9, '1956-08-15', 'semper egestas, urna justo faucibus lectus, a sollicitudin orci sem eget massa. Suspendisse eleifend. Cras sed leo. Cras vehicula aliquet libero. Integer in magna. Phasellus dolor elit, pellentesque a, facilisis non, bibendum'),
  (20, 'Emarèse', 2764642551541, 7, '2018-02-25', 'vulputate, risus a ultricies adipiscing, enim mi tempor lorem, eget mollis lectus pede et risus. Quisque libero lacus, varius et, euismod et, commodo at, libero. Morbi accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus vulputate, nisi sem semper erat, in consectetuer ipsum nunc'),
  (0, 'Harlech', 2281890582293, 1, '2011-09-18', 'nec enim. Nunc ut erat. Sed nunc est, mollis non, cursus non, egestas a, dui. Cras pellentesque. Sed dictum. Proin eget odio. Aliquam vulputate ullamcorper magna. Sed eu eros. Nam consequat dolor vitae dolor. Donec fringilla. Donec feugiat metus sit'),
  (8, 'Somma Lombardo', 5289198424667, 2, '1985-03-09', 'neque pellentesque massa lobortis ultrices. Vivamus rhoncus. Donec est. Nunc ullamcorper, velit in aliquet lobortis, nisi nibh lacinia orci, consectetuer euismod est arcu ac orci. Ut semper pretium neque. Morbi quis urna. Nunc quis arcu vel quam dignissim pharetra. Nam ac nulla. In tincidunt congue turpis. In'),
  (6, 'Campos dos Goytacazes', 2998174697160, 20, '1998-03-11', 'ipsum non arcu. Vivamus sit amet risus. Donec egestas. Aliquam nec enim. Nunc ut erat. Sed nunc est, mollis non, cursus non, egestas a, dui. Cras pellentesque. Sed dictum. Proin eget odio. Aliquam vulputate ullamcorper magna. Sed eu eros. Nam consequat'),
  (10, 'Norman Wells', 5999302882701, 9, '1955-12-13', 'sem elit, pharetra ut, pharetra sed, hendrerit a, arcu. Sed et libero. Proin mi. Aliquam gravida mauris ut mi. Duis risus odio, auctor vitae, aliquet nec, imperdiet nec, leo. Morbi neque tellus, imperdiet non, vestibulum nec, euismod in, dolor. Fusce feugiat. Lorem ipsum dolor'),
  (9, 'Yellowknife', 3465525757521, 3, '2003-06-01', 'lorem eu metus. In lorem. Donec elementum, lorem ut aliquam iaculis, lacus pede sagittis augue, eu tempor erat neque non quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus. Nullam scelerisque neque'),
  (12, 'Veenendaal', 3132215540856, 10, '2001-06-21', 'malesuada fringilla est. Mauris eu turpis. Nulla aliquet. Proin velit. Sed malesuada augue ut lacus. Nulla tincidunt, neque vitae semper egestas, urna justo faucibus lectus, a sollicitudin orci sem eget massa. Suspendisse eleifend. Cras sed leo.'),
  (13, 'Devizes', 6638747826219, 1, '2014-12-10', 'nisl. Nulla eu neque pellentesque massa lobortis ultrices. Vivamus rhoncus. Donec est. Nunc ullamcorper, velit in aliquet lobortis, nisi nibh lacinia orci, consectetuer euismod est arcu ac orci. Ut semper pretium neque. Morbi quis'),
  (15, 'Rocky View', 5284917749465, 8, '1994-09-08', 'porttitor interdum. Sed auctor odio a purus. Duis elementum, dui quis accumsan convallis, ante lectus convallis est, vitae sodales nisi magna sed dui. Fusce aliquam, enim nec tempus scelerisque, lorem ipsum sodales purus, in molestie'),
  (19, 'Chennai', 7909895617515, 5, '2004-03-27', 'pede et risus. Quisque libero lacus, varius et, euismod et, commodo at, libero. Morbi accumsan laoreet ipsum. Curabitur consequat, lectus sit amet luctus vulputate, nisi sem semper erat, in consectetuer ipsum nunc id enim. Curabitur massa. Vestibulum accumsan neque et nunc. Quisque ornare tortor at risus. Nunc ac sem'),
  (13, 'Centa San Nicolò', 3762628953904, 12, '1996-03-06', 'consequat purus. Maecenas libero est, congue a, aliquet vel, vulputate eu, odio. Phasellus at augue id ante dictum cursus. Nunc mauris elit, dictum eu, eleifend nec, malesuada ut, sem. Nulla interdum. Curabitur dictum. Phasellus in felis. Nulla tempor augue ac ipsum. Phasellus vitae mauris sit amet lorem semper'),
  (9, 'Gilly', 5955751750618, 17, '2013-12-25', 'Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Proin vel nisl. Quisque fringilla euismod enim. Etiam gravida molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla ante, iaculis nec, eleifend non, dapibus rutrum,'),
  (3, 'Colina', 2961858201772, 14, '1999-08-04', 'non massa non ante bibendum ullamcorper. Duis cursus, diam at pretium aliquet, metus urna convallis erat, eget tincidunt dui augue eu tellus. Phasellus elit pede, malesuada vel, venenatis vel, faucibus id, libero. Donec consectetuer mauris id sapien. Cras dolor dolor, tempus non, lacinia at, iaculis quis, pede. Praesent eu dui.'),
  (9, 'San Clemente', 5394068602473, 18, '2007-06-06', 'elementum, lorem ut aliquam iaculis, lacus pede sagittis augue, eu tempor erat neque non quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus.'),
  (12, 'Quinchao', 7139127902687, 11, '2000-04-03', 'quam. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Aliquam fringilla cursus purus. Nullam scelerisque neque sed sem egestas blandit. Nam nulla magna, malesuada vel, convallis in, cursus et, eros. Proin ultrices. Duis volutpat nunc sit amet metus. Aliquam erat'),
  (2, 'Bremen', 7497502367944, 19, '1997-08-12', 'scelerisque, lorem ipsum sodales purus, in molestie tortor nibh sit amet orci. Ut sagittis lobortis mauris. Suspendisse aliquet molestie tellus. Aenean egestas hendrerit neque. In ornare sagittis felis. Donec tempor, est ac mattis semper, dui lectus'),
  (11, 'Appels', 8478233616800, 14, '1994-02-22',
   'consectetuer adipiscing elit. Etiam laoreet, libero et tristique pellentesque, tellus sem mollis dui, in sodales elit erat vitae risus. Duis a mi fringilla mi lacinia mattis. Integer eu lacus. Quisque imperdiet, erat nonummy ultricies'),
  (14, 'Bundaberg', 1280864257365, 6, '2006-12-11',
   'Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Etiam laoreet, libero et tristique pellentesque, tellus sem mollis dui, in sodales elit erat vitae risus. Duis a mi fringilla mi lacinia mattis. Integer eu lacus. Quisque imperdiet, erat'),
  (10, 'Linkhout', 3265463449061, 15, '1998-05-18',
   'lobortis tellus justo sit amet nulla. Donec non justo. Proin non massa non ante bibendum ullamcorper. Duis cursus, diam at pretium aliquet, metus urna convallis erat, eget tincidunt dui augue eu tellus. Phasellus elit pede,'),
  (18, 'Val Rezzo', 5360848262906, 20, '2001-02-19',
   'cursus et, eros. Proin ultrices. Duis volutpat nunc sit amet metus. Aliquam erat volutpat. Nulla facilisis. Suspendisse commodo tincidunt nibh. Phasellus nulla. Integer vulputate, risus a ultricies adipiscing, enim mi tempor lorem, eget mollis lectus pede et'),
  (20, 'Maranguape', 5714786246419, 18, '1985-01-24',
   'Donec est mauris, rhoncus id, mollis nec, cursus a, enim. Suspendisse aliquet, sem ut cursus luctus, ipsum leo elementum sem, vitae aliquam eros turpis non enim. Mauris quis turpis vitae purus gravida sagittis. Duis gravida. Praesent eu nulla at sem molestie sodales. Mauris blandit'),
  (5, 'Machynlleth', 7188169397414, 3, '1968-03-30',
   'magnis dis parturient montes, nascetur ridiculus mus. Proin vel arcu eu odio tristique pharetra. Quisque ac libero nec ligula consectetuer rhoncus. Nullam velit dui, semper et, lacinia vitae, sodales at, velit. Pellentesque ultricies dignissim lacus. Aliquam rutrum lorem ac risus. Morbi metus. Vivamus euismod urna.'),
  (2, 'Norman', 1686960421502, 11, '2000-11-16',
   'sapien. Aenean massa. Integer vitae nibh. Donec est mauris, rhoncus id, mollis nec, cursus a, enim. Suspendisse aliquet, sem ut cursus luctus, ipsum leo elementum sem, vitae aliquam eros turpis non enim. Mauris quis turpis vitae purus'),
  (14, 'Schönebeck', 5085774149746, 1, '1994-06-19',
   'magnis dis parturient montes, nascetur ridiculus mus. Proin vel nisl. Quisque fringilla euismod enim. Etiam gravida molestie arcu. Sed eu nibh vulputate mauris sagittis placerat. Cras dictum ultricies ligula. Nullam enim. Sed nulla'),
  (4, 'Town of Yarmouth', 2604286268353, 20, '1984-03-07',
   'sed dictum eleifend, nunc risus varius orci, in consequat enim diam vel arcu. Curabitur ut odio vel est tempor bibendum. Donec felis orci, adipiscing non, luctus sit amet, faucibus ut, nulla. Cras eu tellus eu augue porttitor interdum. Sed auctor')
ON CONFLICT DO NOTHING;

INSERT INTO "books_authors" (book_id, author_id)
VALUES (1, 46), (2, 11), (3, 56), (4, 29), (5, 22), (6, 78), (7, 80), (8, 80), (9, 20), (10, 35), (11, 48), (12, 58),
  (13, 34), (14, 76), (15, 93), (16, 12), (17, 14), (18, 89), (19, 14), (20, 69), (21, 38), (22, 81), (24, 63),
  (24, 74), (25, 62), (26, 29), (27, 98), (28, 66), (29, 97), (30, 10), (31, 63), (32, 60), (33, 31), (34, 20),
  (35, 24), (36, 20), (37, 18), (38, 95), (39, 22), (40, 91), (41, 45), (42, 43), (43, 9), (44, 57), (45, 54), (46, 96),
  (47, 55), (48, 69), (49, 8), (50, 79), (51, 10), (52, 9), (53, 47), (55, 13), (55, 3), (56, 48), (57, 47), (58, 97),
  (59, 4), (60, 95), (61, 30), (62, 49), (63, 26), (64, 91), (65, 13), (66, 18), (67, 91), (68, 6), (69, 57), (70, 67),
  (71, 14), (72, 40), (73, 39), (74, 28), (75, 93), (76, 72), (77, 92), (78, 96), (79, 30), (80, 23), (81, 21),
  (82, 28), (83, 8), (84, 77), (85, 45), (86, 49), (87, 50), (88, 8), (89, 42), (90, 20), (91, 97), (92, 99), (93, 27),
  (94, 26), (95, 8), (96, 20), (97, 69), (98, 69), (99, 18), (100, 64)
ON CONFLICT DO NOTHING;
INSERT INTO "books_authors" (book_id, author_id)
VALUES (1, 14), (2, 81), (3, 96), (4, 56), (5, 8), (6, 36), (7, 57), (8, 12), (9, 63), (10, 63), (11, 96), (12, 13),
  (13, 10), (14, 56), (15, 13), (16, 49), (17, 70), (18, 33), (19, 47), (20, 4), (21, 10), (22, 39), (24, 39), (24, 57),
  (25, 16), (26, 37), (27, 86), (28, 6), (29, 51), (30, 47), (31, 12), (32, 97), (33, 6), (34, 82), (35, 1), (36, 3),
  (37, 28), (38, 58), (39, 54), (40, 28), (41, 19), (42, 30), (43, 10), (44, 24), (45, 58), (46, 24), (47, 27),
  (48, 70), (49, 13), (50, 56), (51, 65), (52, 99), (53, 21), (53, 92), (55, 99), (56, 84), (57, 59), (58, 20),
  (59, 87), (60, 60), (61, 83), (62, 27), (63, 7), (64, 65), (65, 18), (66, 69), (67, 43), (68, 34), (69, 28), (70, 10),
  (71, 53), (72, 64), (73, 79), (74, 27), (75, 40), (76, 15), (77, 71), (78, 89), (79, 83), (80, 43), (81, 75),
  (82, 66), (83, 98), (84, 44), (85, 22), (86, 44), (87, 11), (88, 46), (89, 13), (90, 53), (91, 90), (92, 7), (93, 19),
  (94, 3), (95, 88), (96, 77), (97, 8), (98, 21), (99, 77), (100, 97)
ON CONFLICT DO NOTHING;

INSERT INTO "books_genres" (book_id, genre_id)
VALUES (1, 3), (2, 1), (3, 9), (4, 17), (5, 11), (6, 13), (7, 20), (8, 7), (9, 11), (10, 5), (11, 14), (12, 7),
  (13, 16), (14, 17), (15, 14), (16, 19), (17, 6), (18, 11), (19, 13), (20, 6), (21, 18), (22, 10), (24, 2), (24, 11),
  (25, 16), (26, 3), (27, 11), (28, 11), (29, 9), (30, 9), (31, 11), (32, 3), (33, 14), (34, 12), (35, 5), (36, 10),
  (37, 2), (38, 7), (39, 5), (40, 18), (41, 8), (42, 8), (43, 18), (44, 16), (45, 5), (46, 13), (47, 14), (48, 7),
  (49, 9), (50, 5), (51, 17), (52, 16), (53, 11), (51, 13), (55, 1), (56, 14), (57, 13), (58, 9), (59, 4), (60, 14),
  (61, 8), (62, 9), (63, 14), (64, 7), (65, 2), (66, 20), (67, 4), (68, 18), (69, 1), (70, 12), (71, 9), (72, 17),
  (73, 1), (74, 3), (75, 14), (76, 19), (77, 15), (78, 12), (79, 17), (80, 8), (81, 7), (82, 17), (83, 18), (84, 7),
  (85, 16), (86, 14), (87, 10), (88, 17), (89, 4), (90, 8), (91, 13), (92, 3), (93, 6), (94, 17), (95, 17), (96, 13),
  (97, 5), (98, 17), (99, 12), (100, 3)
ON CONFLICT DO NOTHING;
INSERT INTO "books_genres" (book_id, genre_id)
VALUES (1, 4), (2, 16), (3, 1), (4, 7), (5, 2), (6, 8), (7, 8), (8, 20), (9, 2), (10, 10), (11, 2), (12, 14), (13, 5),
  (14, 8), (15, 7), (16, 7), (17, 15), (18, 19), (19, 18), (20, 5), (21, 18), (22, 1), (24, 11), (24, 12), (25, 11),
  (26, 10), (27, 6), (28, 8), (29, 11), (30, 1), (31, 9), (32, 2), (33, 9), (34, 19), (35, 16), (36, 19), (37, 4),
  (38, 11), (39, 16), (40, 11), (41, 5), (42, 2), (43, 6), (44, 17), (45, 3), (46, 4), (47, 2), (48, 5), (49, 9),
  (50, 6), (51, 4), (52, 14), (53, 3), (57, 15), (55, 5), (56, 20), (57, 5), (58, 13), (59, 4), (60, 6), (61, 6),
  (62, 17), (63, 4), (64, 20), (65, 16), (66, 11), (67, 15), (68, 19), (69, 12), (70, 1), (71, 12), (72, 5), (73, 16),
  (74, 1), (75, 9), (76, 17), (77, 13), (78, 20), (79, 20), (80, 20), (81, 3), (82, 12), (83, 5), (84, 11), (85, 1),
  (86, 10), (87, 9), (88, 6), (89, 6), (90, 17), (91, 8), (92, 10), (93, 14), (94, 1), (95, 18), (96, 7), (97, 12),
  (98, 20), (99, 6), (100, 12)
ON CONFLICT DO NOTHING;

SELECT
  o.*,
  array_agg(DISTINCT bo.book_id) AS books
FROM orders o, books_orders bo
WHERE o.id = bo.order_id AND order_date IS NOT NULL AND return_date IS NULL
GROUP BY o.id