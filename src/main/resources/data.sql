INSERT INTO member (login_id, nickname, is_deleted, is_email_verified, password)
VALUES
    ('admin@gmail.com','뽀로로', false, true, '$2a$10$/UmJ6bLbTxI.4bEnk8BNwuSOAQInf0wKyvklE42YtQqhGoYKIgysm'),
    ('sub@gmail.com','하츄핑', false, true, '$2a$10$/UmJ6bLbTxI.4bEnk8BNwuSOAQInf0wKyvklE42YtQqhGoYKIgysm'),
    ('test@gmail.com', '사이타마', false, true, '$2a$10$/UmJ6bLbTxI.4bEnk8BNwuSOAQInf0wKyvklE42YtQqhGoYKIgysm');

INSERT INTO board (title)
VALUES
    ('notice'),
    ('free'),
    ('gallery');

INSERT INTO category (category_type, board_id)
VALUE
    ('notice', 1),
    ('must', 1),
    ('information', 2),
    ('question', 2),
    ('funny', 3),
    ('food', 3);

INSERT INTO post (board_id, category_id, created_at, view_count, member_id, content, title, is_deleted) VALUES
(1, 1, '2024-08-22 00:00:01', 10, 1, '1 번째 글 내용', '1 번째 글 제목', false),
(1, 1, '2024-08-22 00:00:02', 11, 2, '2 번째 글 내용', '2 번째 글 제목', false),
(1, 2, '2024-08-22 00:00:03', 12, 3, '3 번째 글 내용', '3 번째 글 제목', false),
(1, 1, '2024-08-22 00:00:04', 13, 1, '4 번째 글 내용', '4 번째 글 제목', false),
(1, 2, '2024-08-22 00:00:05', 14, 2, '5 번째 글 내용', '5 번째 글 제목', false),
(1, 1, '2024-08-22 00:00:06', 15, 3, '6 번째 글 내용', '6 번째 글 제목', false),
(1, 2, '2024-08-22 00:00:07', 16, 1, '7 번째 글 내용', '7 번째 글 제목', false),
(1, 2, '2024-08-22 00:00:08', 17, 2, '8 번째 글 내용', '8 번째 글 제목', false),
(1, 2, '2024-08-22 00:00:09', 18, 3, '9 번째 글 내용', '9 번째 글 제목', false),
(2, 3, '2024-08-22 00:00:10', 19, 1, '10 번째 글 내용', '20 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:01', 10, 1, '21 번째 글 내용', '21 번째 글 제목', false),
(2, 3, '2024-08-22 00:00:02', 11, 2, '22 번째 글 내용', '22 번째 글 제목', false),
(2, 3, '2024-08-22 00:00:03', 12, 3, '23 번째 글 내용', '23 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:04', 13, 1, '24 번째 글 내용', '24 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:05', 14, 2, '25 번째 글 내용', '25 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:06', 15, 3, '26 번째 글 내용', '26 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:07', 16, 1, '27 번째 글 내용', '27 번째 글 제목', false),
(2, 3, '2024-08-22 00:00:08', 17, 2, '28 번째 글 내용', '28 번째 글 제목', false),
(2, 3, '2024-08-22 00:00:09', 18, 3, '29 번째 글 내용', '29 번째 글 제목', false),
(2, 4, '2024-08-22 00:00:10', 19, 1, '30 번째 글 내용', '30 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:01', 10, 1, '31 번째 글 내용', '31 번째 글 제목', false),
(3, 6, '2024-08-22 00:00:02', 11, 2, '32 번째 글 내용', '32 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:03', 12, 3, '33 번째 글 내용', '33 번째 글 제목', false),
(3, 6, '2024-08-22 00:00:04', 13, 1, '34 번째 글 내용', '34 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:05', 14, 2, '35 번째 글 내용', '35 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:06', 15, 3, '36 번째 글 내용', '36 번째 글 제목', false),
(3, 6, '2024-08-22 00:00:07', 16, 1, '37 번째 글 내용', '37 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:08', 17, 2, '38 번째 글 내용', '38 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:09', 18, 3, '39 번째 글 내용', '39 번째 글 제목', false),
(3, 5, '2024-08-22 00:00:10', 19, 1, '40 번째 글 내용', '40 번째 글 제목', false);

-- 댓글 생성
INSERT INTO comment (content, post_id, member_id, is_reply, created_at, is_deleted)
VALUES ('첫 번째 댓글입니다.', 8, 1, false, CURRENT_TIMESTAMP(), false);

-- -- 댓글 생성
-- INSERT INTO comment (content, post_id, member_id, is_reply, created_at)
-- VALUES ('두 번째 댓글입니다.', 12, 2, false, CURRENT_TIMESTAMP());

-- 답글 생성
INSERT INTO comment (content, post_id, member_id, parent_id, is_reply, created_at, PARENT_NICKNAME, is_deleted)
VALUES ('첫 번째 답글입니다.', 8, 1, 1, true, CURRENT_TIMESTAMP(), '노리', false);

INSERT INTO comment (content, post_id, member_id, parent_id, is_reply, created_at, PARENT_NICKNAME, is_deleted)
VALUES ('두 번째 답글입니다.', 8, 1, 1, true, CURRENT_TIMESTAMP(), '노리', false);
