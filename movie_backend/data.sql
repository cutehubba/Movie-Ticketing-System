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

-- 插入初始用户数据
INSERT INTO `users` VALUES (1, 'zhangsan', '张三', 'zhangsan@qq.com', '$2a$10$Jz2LEBlFvTZQnFzjHmMrheJCXwEbSlQXvXJNSK7ohYG81VNGtzdzu', 'user');
INSERT INTO `users` VALUES (2, 'admin', 'admin', 'admin@qq.com', '$2a$10$Jz2LEBlFvTZQnFzjHmMrheJCXwEbSlQXvXJNSK7ohYG81VNGtzdzu', 'admin');


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
                                                                 (2, '影厅2', '光影时代影城的影厅2', 80);

-- 插入初始座位数据
INSERT INTO `seats` VALUES (1, 1, '座位1', 0);
INSERT INTO `seats` VALUES (2, 1, '座位2', 0);
INSERT INTO `seats` VALUES (3, 1, '座位3', 0);
INSERT INTO `seats` VALUES (4, 1, '座位4', 0);
INSERT INTO `seats` VALUES (5, 1, '座位5', 0);
INSERT INTO `seats` VALUES (6, 1, '座位6', 0);
INSERT INTO `seats` VALUES (7, 1, '座位7', 0);
INSERT INTO `seats` VALUES (8, 1, '座位8', 0);
INSERT INTO `seats` VALUES (9, 1, '座位9', 0);
INSERT INTO `seats` VALUES (10, 1, '座位10', 0);
INSERT INTO `seats` VALUES (11, 1, '座位11', 0);
INSERT INTO `seats` VALUES (12, 1, '座位12', 0);
INSERT INTO `seats` VALUES (13, 1, '座位13', 0);
INSERT INTO `seats` VALUES (14, 1, '座位14', 0);
INSERT INTO `seats` VALUES (15, 1, '座位15', 1);
INSERT INTO `seats` VALUES (16, 1, '座位16', 0);
INSERT INTO `seats` VALUES (17, 1, '座位17', 0);
INSERT INTO `seats` VALUES (18, 1, '座位18', 0);
INSERT INTO `seats` VALUES (19, 1, '座位19', 0);
INSERT INTO `seats` VALUES (20, 1, '座位20', 0);
INSERT INTO `seats` VALUES (21, 1, '座位21', 0);
INSERT INTO `seats` VALUES (22, 1, '座位22', 0);
INSERT INTO `seats` VALUES (23, 1, '座位23', 0);
INSERT INTO `seats` VALUES (24, 1, '座位24', 0);
INSERT INTO `seats` VALUES (25, 1, '座位25', 0);
INSERT INTO `seats` VALUES (26, 1, '座位26', 0);
INSERT INTO `seats` VALUES (27, 1, '座位27', 0);
INSERT INTO `seats` VALUES (28, 1, '座位28', 0);
INSERT INTO `seats` VALUES (29, 1, '座位29', 0);
INSERT INTO `seats` VALUES (30, 1, '座位30', 0);
INSERT INTO `seats` VALUES (31, 1, '座位31', 0);
INSERT INTO `seats` VALUES (32, 1, '座位32', 0);
INSERT INTO `seats` VALUES (33, 1, '座位33', 0);
INSERT INTO `seats` VALUES (34, 1, '座位34', 0);
INSERT INTO `seats` VALUES (35, 1, '座位35', 0);
INSERT INTO `seats` VALUES (36, 1, '座位36', 0);
INSERT INTO `seats` VALUES (37, 1, '座位37', 0);
INSERT INTO `seats` VALUES (38, 1, '座位38', 0);
INSERT INTO `seats` VALUES (39, 1, '座位39', 0);
INSERT INTO `seats` VALUES (40, 1, '座位40', 0);
INSERT INTO `seats` VALUES (41, 1, '座位41', 0);
INSERT INTO `seats` VALUES (42, 1, '座位42', 0);
INSERT INTO `seats` VALUES (43, 1, '座位43', 1);
INSERT INTO `seats` VALUES (44, 1, '座位44', 0);
INSERT INTO `seats` VALUES (45, 1, '座位45', 0);
INSERT INTO `seats` VALUES (46, 1, '座位46', 0);
INSERT INTO `seats` VALUES (47, 1, '座位47', 0);
INSERT INTO `seats` VALUES (48, 1, '座位48', 0);
INSERT INTO `seats` VALUES (49, 1, '座位49', 0);
INSERT INTO `seats` VALUES (50, 1, '座位50', 0);
INSERT INTO `seats` VALUES (51, 1, '座位51', 0);
INSERT INTO `seats` VALUES (52, 1, '座位52', 0);
INSERT INTO `seats` VALUES (53, 1, '座位53', 0);
INSERT INTO `seats` VALUES (54, 1, '座位54', 1);
INSERT INTO `seats` VALUES (55, 1, '座位55', 1);
INSERT INTO `seats` VALUES (56, 1, '座位56', 0);
INSERT INTO `seats` VALUES (57, 1, '座位57', 0);
INSERT INTO `seats` VALUES (58, 1, '座位58', 1);
INSERT INTO `seats` VALUES (59, 1, '座位59', 0);
INSERT INTO `seats` VALUES (60, 1, '座位60', 0);
INSERT INTO `seats` VALUES (61, 1, '座位61', 0);
INSERT INTO `seats` VALUES (62, 1, '座位62', 0);
INSERT INTO `seats` VALUES (63, 1, '座位63', 0);
INSERT INTO `seats` VALUES (64, 1, '座位64', 0);
INSERT INTO `seats` VALUES (65, 1, '座位65', 0);
INSERT INTO `seats` VALUES (66, 1, '座位66', 0);
INSERT INTO `seats` VALUES (67, 1, '座位67', 0);
INSERT INTO `seats` VALUES (68, 1, '座位68', 0);
INSERT INTO `seats` VALUES (69, 1, '座位69', 0);
INSERT INTO `seats` VALUES (70, 1, '座位70', 0);
INSERT INTO `seats` VALUES (71, 1, '座位71', 0);
INSERT INTO `seats` VALUES (72, 1, '座位72', 0);
INSERT INTO `seats` VALUES (73, 1, '座位73', 0);
INSERT INTO `seats` VALUES (74, 1, '座位74', 0);
INSERT INTO `seats` VALUES (75, 1, '座位75', 0);
INSERT INTO `seats` VALUES (76, 1, '座位76', 0);
INSERT INTO `seats` VALUES (77, 1, '座位77', 0);
INSERT INTO `seats` VALUES (78, 1, '座位78', 0);
INSERT INTO `seats` VALUES (79, 1, '座位79', 0);
INSERT INTO `seats` VALUES (80, 1, '座位80', 0);
INSERT INTO `seats` VALUES (81, 1, '座位81', 0);
INSERT INTO `seats` VALUES (82, 1, '座位82', 0);
INSERT INTO `seats` VALUES (83, 1, '座位83', 0);
INSERT INTO `seats` VALUES (84, 1, '座位84', 1);
INSERT INTO `seats` VALUES (85, 1, '座位85', 0);
INSERT INTO `seats` VALUES (86, 1, '座位86', 0);
INSERT INTO `seats` VALUES (87, 1, '座位87', 0);
INSERT INTO `seats` VALUES (88, 1, '座位88', 0);
INSERT INTO `seats` VALUES (89, 1, '座位89', 0);
INSERT INTO `seats` VALUES (90, 1, '座位90', 0);
INSERT INTO `seats` VALUES (91, 1, '座位91', 0);
INSERT INTO `seats` VALUES (92, 1, '座位92', 0);
INSERT INTO `seats` VALUES (93, 1, '座位93', 0);
INSERT INTO `seats` VALUES (94, 1, '座位94', 0);
INSERT INTO `seats` VALUES (95, 1, '座位95', 0);
INSERT INTO `seats` VALUES (96, 1, '座位96', 0);
INSERT INTO `seats` VALUES (97, 1, '座位97', 0);
INSERT INTO `seats` VALUES (98, 1, '座位98', 0);
INSERT INTO `seats` VALUES (99, 1, '座位99', 0);
INSERT INTO `seats` VALUES (100, 1, '座位100', 0);
INSERT INTO `seats` VALUES (101, 2, '座位1', 0);
INSERT INTO `seats` VALUES (102, 2, '座位2', 0);
INSERT INTO `seats` VALUES (103, 2, '座位3', 0);
INSERT INTO `seats` VALUES (104, 2, '座位4', 0);
INSERT INTO `seats` VALUES (105, 2, '座位5', 0);
INSERT INTO `seats` VALUES (106, 2, '座位6', 0);
INSERT INTO `seats` VALUES (107, 2, '座位7', 0);
INSERT INTO `seats` VALUES (108, 2, '座位8', 0);
INSERT INTO `seats` VALUES (109, 2, '座位9', 0);
INSERT INTO `seats` VALUES (110, 2, '座位10', 0);
INSERT INTO `seats` VALUES (111, 2, '座位11', 0);
INSERT INTO `seats` VALUES (112, 2, '座位12', 0);
INSERT INTO `seats` VALUES (113, 2, '座位13', 0);
INSERT INTO `seats` VALUES (114, 2, '座位14', 0);
INSERT INTO `seats` VALUES (115, 2, '座位15', 0);
INSERT INTO `seats` VALUES (116, 2, '座位16', 0);
INSERT INTO `seats` VALUES (117, 2, '座位17', 0);
INSERT INTO `seats` VALUES (118, 2, '座位18', 0);
INSERT INTO `seats` VALUES (119, 2, '座位19', 0);
INSERT INTO `seats` VALUES (120, 2, '座位20', 0);
INSERT INTO `seats` VALUES (121, 2, '座位21', 0);
INSERT INTO `seats` VALUES (122, 2, '座位22', 0);
INSERT INTO `seats` VALUES (123, 2, '座位23', 0);
INSERT INTO `seats` VALUES (124, 2, '座位24', 0);
INSERT INTO `seats` VALUES (125, 2, '座位25', 0);
INSERT INTO `seats` VALUES (126, 2, '座位26', 0);
INSERT INTO `seats` VALUES (127, 2, '座位27', 0);
INSERT INTO `seats` VALUES (128, 2, '座位28', 0);
INSERT INTO `seats` VALUES (129, 2, '座位29', 0);
INSERT INTO `seats` VALUES (130, 2, '座位30', 0);
INSERT INTO `seats` VALUES (131, 2, '座位31', 1);
INSERT INTO `seats` VALUES (132, 2, '座位32', 0);
INSERT INTO `seats` VALUES (133, 2, '座位33', 0);
INSERT INTO `seats` VALUES (134, 2, '座位34', 0);
INSERT INTO `seats` VALUES (135, 2, '座位35', 0);
INSERT INTO `seats` VALUES (136, 2, '座位36', 0);
INSERT INTO `seats` VALUES (137, 2, '座位37', 0);
INSERT INTO `seats` VALUES (138, 2, '座位38', 0);
INSERT INTO `seats` VALUES (139, 2, '座位39', 0);
INSERT INTO `seats` VALUES (140, 2, '座位40', 1);
INSERT INTO `seats` VALUES (141, 2, '座位41', 0);
INSERT INTO `seats` VALUES (142, 2, '座位42', 0);
INSERT INTO `seats` VALUES (143, 2, '座位43', 0);
INSERT INTO `seats` VALUES (144, 2, '座位44', 0);
INSERT INTO `seats` VALUES (145, 2, '座位45', 0);
INSERT INTO `seats` VALUES (146, 2, '座位46', 0);
INSERT INTO `seats` VALUES (147, 2, '座位47', 0);
INSERT INTO `seats` VALUES (148, 2, '座位48', 0);
INSERT INTO `seats` VALUES (149, 2, '座位49', 1);
INSERT INTO `seats` VALUES (150, 2, '座位50', 0);
INSERT INTO `seats` VALUES (151, 2, '座位51', 0);
INSERT INTO `seats` VALUES (152, 2, '座位52', 0);
INSERT INTO `seats` VALUES (153, 2, '座位53', 0);
INSERT INTO `seats` VALUES (154, 2, '座位54', 0);
INSERT INTO `seats` VALUES (155, 2, '座位55', 0);
INSERT INTO `seats` VALUES (156, 2, '座位56', 1);
INSERT INTO `seats` VALUES (157, 2, '座位57', 0);
INSERT INTO `seats` VALUES (158, 2, '座位58', 0);
INSERT INTO `seats` VALUES (159, 2, '座位59', 0);
INSERT INTO `seats` VALUES (160, 2, '座位60', 0);
INSERT INTO `seats` VALUES (161, 2, '座位61', 0);
INSERT INTO `seats` VALUES (162, 2, '座位62', 0);
INSERT INTO `seats` VALUES (163, 2, '座位63', 1);
INSERT INTO `seats` VALUES (164, 2, '座位64', 0);
INSERT INTO `seats` VALUES (165, 2, '座位65', 0);
INSERT INTO `seats` VALUES (166, 2, '座位66', 0);
INSERT INTO `seats` VALUES (167, 2, '座位67', 0);
INSERT INTO `seats` VALUES (168, 2, '座位68', 0);
INSERT INTO `seats` VALUES (169, 2, '座位69', 0);
INSERT INTO `seats` VALUES (170, 2, '座位70', 0);
INSERT INTO `seats` VALUES (171, 2, '座位71', 0);
INSERT INTO `seats` VALUES (172, 2, '座位72', 0);
INSERT INTO `seats` VALUES (173, 2, '座位73', 0);
INSERT INTO `seats` VALUES (174, 2, '座位74', 0);
INSERT INTO `seats` VALUES (175, 2, '座位75', 0);
INSERT INTO `seats` VALUES (176, 2, '座位76', 1);
INSERT INTO `seats` VALUES (177, 2, '座位77', 0);
INSERT INTO `seats` VALUES (178, 2, '座位78', 1);
INSERT INTO `seats` VALUES (179, 2, '座位79', 0);
INSERT INTO `seats` VALUES (180, 2, '座位80', 0);

-- 插入初始排片数据
INSERT INTO `schedules` VALUES (1, 1, 1, 1, '2025-06-13 07:41:00', '2025-06-26 07:41:00', 60.00, 'available', '2025-06-02 15:41:31', '2025-06-02 15:41:31');
INSERT INTO `schedules` VALUES (2, 3, 2, 2, '2025-06-27 07:42:00', '2025-06-19 07:42:00', 70.00, 'available', '2025-06-02 15:41:50', '2025-06-02 15:42:35');

-- 插入初始订单数据
INSERT INTO `orders` VALUES (1, 1, 1, '座位54,座位55', 120.00, 'paid', '2025-06-02 07:43:00', '2025-06-02 07:43:02');
INSERT INTO `orders` VALUES (3, 1, 2, '座位28', 70.00, 'cancelled', '2025-06-02 15:43:17', '2025-06-02 15:43:34');
INSERT INTO `orders` VALUES (4, 1, 1, '座位47', 60.00, 'pending', '2025-06-02 15:43:26', '2025-06-02 15:43:26');
