DELETE FROM user_roles;
DELETE FROM meals;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin'),
  ('OtherUser', 'otherUser@yandex.ru', 'password');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001),
  ('ROLE_USER', 100002);

INSERT INTO meals (user_id, date_time, description, calories) VALUES
(100000, '2019-06-06 08:00:00', 'kashki bahnul', 500),
(100000, '2019-06-06 13:00:00', 'supchik', 1500),
(100000, '2019-06-06 19:00:00', 'burgerok', 2500),
(100002, '2019-06-06 08:00:00', 'kashki bahnul', 500),
(100002, '2019-06-06 15:01:00', 'mars', 300),
(100002, '2019-06-06 15:02:00', 'bounty', 300);
