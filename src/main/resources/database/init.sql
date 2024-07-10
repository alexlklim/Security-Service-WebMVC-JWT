
INSERT IGNORE INTO users (
                   id, created, updated, last_activity, is_active,
                   firstname, lastname,
                   email, password,
                   roles)
VALUES (1, NOW(), NOW(), NOW(), true,
        'Alex', 'Klim', 'alex@gmail.com', '$2a$10$G7/RXIL6FTjldvXU60lM9OkZNH/DeniXHbskTUyQ7lVpU/C..Aeb2',
        'ADMIN');

