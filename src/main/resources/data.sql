INSERT INTO credentials (password, role, username)
VALUES ('$2a$10$d.6.91/U.U61RjS6498Wbe/x5LwTqhuAz3Szro.TIkGv6sCs36D3e', 'USER', 'TheDanger'),
       ('$2a$10$KDf/A9pvZgMNQUXa9vLVHeIebJAs7cMJrESJNk4C2aFSEg2apVyDq', 'USER', 'TheOneWhoKnocks');
INSERT INTO users (real_name, credential_id)
VALUES ('Jon', 1),
       ('Egor', 2);
INSERT INTO disks (name)
VALUES ('Emperor''s chosen'),
       ('Seeking truth'),
       ('Unknown hero'),
       ('Eternal bliss');
INSERT INTO taken_items (is_free, current_owner_id, disk_id, original_owner_id)
VALUES ('true', null, 1, 1),
       ('true', null, 2, 2),
       ('false', 1, 3, 2),
       ('false', 2, 4, 1);
