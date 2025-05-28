DROP DATABASE IF EXISTS MovieTicket;
CREATE DATABASE MovieTicket;
USE MovieTicket;

-- 用户表
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       nickname VARCHAR(100),
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(100) NOT NULL,
                       role VARCHAR(20) NOT NULL DEFAULT 'user'
);

-- 电影表
CREATE TABLE movies (
                        id INT AUTO_INCREMENT PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        release_date DATE NOT NULL,
                        duration INT,
                        genre VARCHAR(100),
                        director VARCHAR(255),
                        description TEXT,
                        release_status ENUM('released', 'upcoming', 'not_released'),
                        rating DECIMAL(3,1),
                        poster_url VARCHAR(255),
                        trailer_url VARCHAR(255)
);

-- 电影院表
CREATE TABLE cinemas (
                         id INT AUTO_INCREMENT PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         location VARCHAR(255),
                         contact_number VARCHAR(20),
                         capacity INT,
                         description TEXT
);

-- 影厅表
CREATE TABLE halls (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       cinema_id INT NOT NULL,
                       name VARCHAR(100) NOT NULL,
                       description TEXT,
                       seat_count INT, -- 添加座位总数字段
                       FOREIGN KEY (cinema_id) REFERENCES cinemas(id) ON DELETE CASCADE
);

-- 座位表
CREATE TABLE seats (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       hall_id BIGINT NOT NULL,
                       seat_number VARCHAR(20) NOT NULL,
                       is_occupied BOOLEAN DEFAULT FALSE,
                       FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE CASCADE
);

-- 排片表
CREATE TABLE schedules (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           movie_id INT NOT NULL,
                           cinema_id INT NOT NULL,
                           hall_id BIGINT NOT NULL,
                           start_time DATETIME NOT NULL,
                           end_time DATETIME NOT NULL,
                           price DECIMAL(10, 2) NOT NULL,
                           status ENUM('available', 'cancelled', 'completed') DEFAULT 'available',
                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                           FOREIGN KEY (movie_id) REFERENCES movies(id) ON DELETE CASCADE,
                           FOREIGN KEY (cinema_id) REFERENCES cinemas(id) ON DELETE CASCADE,
                           FOREIGN KEY (hall_id) REFERENCES halls(id) ON DELETE CASCADE
);

-- 订单表
CREATE TABLE orders (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        user_id BIGINT NOT NULL,
                        schedule_id BIGINT NOT NULL,
                        seat_numbers VARCHAR(255) NOT NULL,
                        total_price DECIMAL(10, 2) NOT NULL,
                        status ENUM('pending', 'paid', 'cancelled', 'completed') DEFAULT 'pending',
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                        FOREIGN KEY (schedule_id) REFERENCES schedules(id) ON DELETE CASCADE
);

-- 支付表
CREATE TABLE payments (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          order_id BIGINT NOT NULL,
                          payment_method ENUM('credit_card', 'debit_card', 'paypal', 'wechat', 'alipay') NOT NULL,
                          amount DECIMAL(10, 2) NOT NULL,
                          status ENUM('pending', 'success', 'failed') DEFAULT 'pending',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
);

-- 插入初始电影数据
INSERT INTO movies (title, release_date, duration, genre, director, description, release_status, rating, poster_url, trailer_url) VALUES
                                                                                                                                      ('追风少年', '2024-05-01', 120, '动作', '李雷', '一位年轻人在速度中找到自由的故事。', 'released', 8.5, 'https://bpic.588ku.com/art_origin_min_pic/22/06/11/f2dcd45dc7f45a579ac482e8514e628c.jpg', 'https://example.com/trailers/1.mp4'),
                                                                                                                                      ('时间边界', '2025-07-15', 135, '科幻', '张芸', '科学家在时间旅行中面临伦理困境。', 'upcoming', NULL, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSFqh4z1IqL0jNSPuwGBuP1F8-cRnH9cNBrTg&s', 'https://example.com/trailers/2.mp4'),
                                                                                                                                      ('深海之心', '2023-11-20', 110, '剧情', '王涛', '一位潜水员在深海中找到内心的宁静。', 'released', 7.9, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRIvRphRRGRQRL2iturxt8QyYU0sLcPd6OglQ&s', 'https://example.com/trailers/3.mp4');

-- 插入初始电影院数据
INSERT INTO cinemas (name, location, contact_number, capacity, description) VALUES
                                                                                ('星耀国际影城', '北京市朝阳区建国路88号', '010-88886666', 300, '一家配备IMAX和4D影厅的高端影院。'),
                                                                                ('光影时代影城', '上海市徐汇区漕溪北路28号', '021-77778888', 250, '现代化影院，提供舒适座椅与优质音效体验。');

-- 插入初始影厅数据
INSERT INTO halls (cinema_id, name, description, seat_count) VALUES
                                                                 (1, '影厅1', '星耀国际影城的影厅1', 100),
                                                                 (2, '影厅1', '光影时代影城的影厅1', 80);

-- 插入影厅1的座位数据
INSERT INTO seats (hall_id, seat_number, is_occupied) VALUES
                                                          (1, '座位1', FALSE),
                                                          (1, '座位2', FALSE),
                                                          (1, '座位3', FALSE),
                                                          (1, '座位4', FALSE),
                                                          (1, '座位5', FALSE),
                                                          (1, '座位6', FALSE),
                                                          (1, '座位7', FALSE),
                                                          (1, '座位8', FALSE),
                                                          (1, '座位9', FALSE),
                                                          (1, '座位10', FALSE),
                                                          (1, '座位11', FALSE),
                                                          (1, '座位12', FALSE),
                                                          (1, '座位13', FALSE),
                                                          (1, '座位14', FALSE),
                                                          (1, '座位15', FALSE),
                                                          (1, '座位16', FALSE),
                                                          (1, '座位17', FALSE),
                                                          (1, '座位18', FALSE),
                                                          (1, '座位19', FALSE),
                                                          (1, '座位20', FALSE),
                                                          (1, '座位21', FALSE),
                                                          (1, '座位22', FALSE),
                                                          (1, '座位23', FALSE),
                                                          (1, '座位24', FALSE),
                                                          (1, '座位25', FALSE),
                                                          (1, '座位26', FALSE),
                                                          (1, '座位27', FALSE),
                                                          (1, '座位28', FALSE),
                                                          (1, '座位29', FALSE),
                                                          (1, '座位30', FALSE),
                                                          (1, '座位31', FALSE),
                                                          (1, '座位32', FALSE),
                                                          (1, '座位33', FALSE),
                                                          (1, '座位34', FALSE),
                                                          (1, '座位35', FALSE),
                                                          (1, '座位36', FALSE),
                                                          (1, '座位37', FALSE),
                                                          (1, '座位38', FALSE),
                                                          (1, '座位39', FALSE),
                                                          (1, '座位40', FALSE),
                                                          (1, '座位41', FALSE),
                                                          (1, '座位42', FALSE),
                                                          (1, '座位43', FALSE),
                                                          (1, '座位44', FALSE),
                                                          (1, '座位45', FALSE),
                                                          (1, '座位46', FALSE),
                                                          (1, '座位47', FALSE),
                                                          (1, '座位48', FALSE),
                                                          (1, '座位49', FALSE),
                                                          (1, '座位50', FALSE),
                                                          (1, '座位51', FALSE),
                                                          (1, '座位52', FALSE),
                                                          (1, '座位53', FALSE),
                                                          (1, '座位54', FALSE),
                                                          (1, '座位55', FALSE),
                                                          (1, '座位56', FALSE),
                                                          (1, '座位57', FALSE),
                                                          (1, '座位58', FALSE),
                                                          (1, '座位59', FALSE),
                                                          (1, '座位60', FALSE),
                                                          (1, '座位61', FALSE),
                                                          (1, '座位62', FALSE),
                                                          (1, '座位63', FALSE),
                                                          (1, '座位64', FALSE),
                                                          (1, '座位65', FALSE),
                                                          (1, '座位66', FALSE),
                                                          (1, '座位67', FALSE),
                                                          (1, '座位68', FALSE),
                                                          (1, '座位69', FALSE),
                                                          (1, '座位70', FALSE),
                                                          (1, '座位71', FALSE),
                                                          (1, '座位72', FALSE),
                                                          (1, '座位73', FALSE),
                                                          (1, '座位74', FALSE),
                                                          (1, '座位75', FALSE),
                                                          (1, '座位76', FALSE),
                                                          (1, '座位77', FALSE),
                                                          (1, '座位78', FALSE),
                                                          (1, '座位79', FALSE),
                                                          (1, '座位80', FALSE),
                                                          (1, '座位81', FALSE),
                                                          (1, '座位82', FALSE),
                                                          (1, '座位83', FALSE),
                                                          (1, '座位84', FALSE),
                                                          (1, '座位85', FALSE),
                                                          (1, '座位86', FALSE),
                                                          (1, '座位87', FALSE),
                                                          (1, '座位88', FALSE),
                                                          (1, '座位89', FALSE),
                                                          (1, '座位90', FALSE),
                                                          (1, '座位91', FALSE),
                                                          (1, '座位92', FALSE),
                                                          (1, '座位93', FALSE),
                                                          (1, '座位94', FALSE),
                                                          (1, '座位95', FALSE),
                                                          (1, '座位96', FALSE),
                                                          (1, '座位97', FALSE),
                                                          (1, '座位98', FALSE),
                                                          (1, '座位99', FALSE),
                                                          (1, '座位100', FALSE);

-- 插入影厅2的座位数据
INSERT INTO seats (hall_id, seat_number, is_occupied) VALUES
                                                          (2, '座位1', FALSE),
                                                          (2, '座位2', FALSE),
                                                          (2, '座位3', FALSE),
                                                          (2, '座位4', FALSE),
                                                          (2, '座位5', FALSE),
                                                          (2, '座位6', FALSE),
                                                          (2, '座位7', FALSE),
                                                          (2, '座位8', FALSE),
                                                          (2, '座位9', FALSE),
                                                          (2, '座位10', FALSE),
                                                          (2, '座位11', FALSE),
                                                          (2, '座位12', FALSE),
                                                          (2, '座位13', FALSE),
                                                          (2, '座位14', FALSE),
                                                          (2, '座位15', FALSE),
                                                          (2, '座位16', FALSE),
                                                          (2, '座位17', FALSE),
                                                          (2, '座位18', FALSE),
                                                          (2, '座位19', FALSE),
                                                          (2, '座位20', FALSE),
                                                          (2, '座位21', FALSE),
                                                          (2, '座位22', FALSE),
                                                          (2, '座位23', FALSE),
                                                          (2, '座位24', FALSE),
                                                          (2, '座位25', FALSE),
                                                          (2, '座位26', FALSE),
                                                          (2, '座位27', FALSE),
                                                          (2, '座位28', FALSE),
                                                          (2, '座位29', FALSE),
                                                          (2, '座位30', FALSE),
                                                          (2, '座位31', FALSE),
                                                          (2, '座位32', FALSE),
                                                          (2, '座位33', FALSE),
                                                          (2, '座位34', FALSE),
                                                          (2, '座位35', FALSE),
                                                          (2, '座位36', FALSE),
                                                          (2, '座位37', FALSE),
                                                          (2, '座位38', FALSE),
                                                          (2, '座位39', FALSE),
                                                          (2, '座位40', FALSE),
                                                          (2, '座位41', FALSE),
                                                          (2, '座位42', FALSE),
                                                          (2, '座位43', FALSE),
                                                          (2, '座位44', FALSE),
                                                          (2, '座位45', FALSE),
                                                          (2, '座位46', FALSE),
                                                          (2, '座位47', FALSE),
                                                          (2, '座位48', FALSE),
                                                          (2, '座位49', FALSE),
                                                          (2, '座位50', FALSE),
                                                          (2, '座位51', FALSE),
                                                          (2, '座位52', FALSE),
                                                          (2, '座位53', FALSE),
                                                          (2, '座位54', FALSE),
                                                          (2, '座位55', FALSE),
                                                          (2, '座位56', FALSE),
                                                          (2, '座位57', FALSE),
                                                          (2, '座位58', FALSE),
                                                          (2, '座位59', FALSE),
                                                          (2, '座位60', FALSE),
                                                          (2, '座位61', FALSE),
                                                          (2, '座位62', FALSE),
                                                          (2, '座位63', FALSE),
                                                          (2, '座位64', FALSE),
                                                          (2, '座位65', FALSE),
                                                          (2, '座位66', FALSE),
                                                          (2, '座位67', FALSE),
                                                          (2, '座位68', FALSE),
                                                          (2, '座位69', FALSE),
                                                          (2, '座位70', FALSE),
                                                          (2, '座位71', FALSE),
                                                          (2, '座位72', FALSE),
                                                          (2, '座位73', FALSE),
                                                          (2, '座位74', FALSE),
                                                          (2, '座位75', FALSE),
                                                          (2, '座位76', FALSE),
                                                          (2, '座位77', FALSE),
                                                          (2, '座位78', FALSE),
                                                          (2, '座位79', FALSE),
                                                          (2, '座位80', FALSE);