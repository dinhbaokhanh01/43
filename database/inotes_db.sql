CREATE DATABASE IF NOT EXISTS inotes_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE inotes_db;

DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS note_type;

CREATE TABLE note_type (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE notes (
    id      INT AUTO_INCREMENT PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    content TEXT,
    type_id INT,
    CONSTRAINT fk_notes_type FOREIGN KEY (type_id) REFERENCES note_type(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO note_type (name, description) VALUES
 ('Cá nhân',  'Ghi chú đời sống cá nhân'),
 ('Công việc','Ghi chú liên quan đến công việc'),
 ('Học tập',  'Ghi chú học tập, nghiên cứu');

INSERT INTO notes (title, content, type_id) VALUES
 ('Họp giao ban đầu tuần', 'Chuẩn bị báo cáo tuần trước và kế hoạch tuần này.', 2),
 ('Ôn tập Design Pattern Servlet', 'Strategy, Factory, Singleton, MVC.', 3);
