-- Insert roles
INSERT INTO roles (name) VALUES ('ROLE_USER'), ('ROLE_MODERATOR'), ('ROLE_ADMIN');

-- Insert users
INSERT INTO users (username, email, password, profileimage) VALUES
('user1', 'user1@example.com', '$2a$10$dZ2jrNd7j1hFq76gZaeRA.L4e3Jllud21ND/wUm4UaISa/bPc02AC', 'https://www.gravatar.com/avatar/user1@example.com?s=120&d=identicon'), --password1
('user2', 'user2@example.com', '$2a$10$1E1OgIO6u.eSGSI1qpS1n.EMOESzcjbnCWuLw02KXap0zYIPzy5bC', 'https://www.gravatar.com/avatar/user2@example.com?s=120&d=identicon'),--password2
('user3', 'user3@example.com', '$2a$10$6gxxTWcpxWQclZUIY1UrYuMnq0ehksQ.N5kDE2eJp0PBzSJziMv.y', 'https://www.gravatar.com/avatar/user3@example.com?s=120&d=identicon'); --password3

-- Assign roles to users
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), (2, 1), (3, 2), (3, 3);

-- Insert chats
INSERT INTO chats (content, created_at, updated_at) VALUES
('Chat 1', NOW(), NOW()),
('Chat 2', NOW(), NOW());

-- Insert messages
INSERT INTO messages (content, created_at, updated_at, chat_id, user_id) VALUES
('Message 1', NOW(), NOW(), 1, 1),
('Message 2', NOW(), NOW(), 1, 2),
('Message 3', NOW(), NOW(), 2, 1),
('Message 4', NOW(), NOW(), 2, 2);

-- Insert user relations
INSERT INTO user_relations (user_id, friend_id) VALUES
(1, 2), -- user1 is friends with user2
(1, 3), -- user1 is friends with user3
(2, 3); -- user2 is friends with user3