SELECT * FROM users;

SELECT * FROM friendships;

SELECT * FROM users 
INNER JOIN friendships ON users.email = friendships.user1 OR users.email = friendships.user2
WHERE users.email = 'e2';

ALTER TABLE friendships
DROP CONSTRAINT IF EXISTS fk_user1,
ADD CONSTRAINT fk_user1 FOREIGN KEY (user1) REFERENCES users(email) ON DELETE CASCADE;

ALTER TABLE friendships
DROP CONSTRAINT IF EXISTS fk_user2,
ADD CONSTRAINT fk_user2 FOREIGN KEY (user2) REFERENCES users(email) ON DELETE CASCADE;

