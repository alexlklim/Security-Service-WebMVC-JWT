
DROP TABLE token;
DROP TABLE users;





DELETE from token WHERE token.user_id = (
    SELECT id FROM users WHERE email = 'klimenko.tetiana3@gmail.com'
    );

DELETE from users WHERE email = 'klimenko.tetiana3@gmail.com';
