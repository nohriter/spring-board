INSERT INTO member (login_id, nickname, is_deleted)
VALUES
    ('nohri@gmail.com','노리', false),
    ('abc@gmail.com','홍길동', false),
    ('pepepe@gmail.com', '손흥민', false),
    ('4007068@naver.com', '손흥민', false);

INSERT INTO category (category_type)
VALUES ('general');

INSERT INTO category (category_type)
VALUES ('question');

INSERT INTO category (category_type)
VALUES ('information');

INSERT INTO post (category_id, created_at, view_count, member_id, content, title, is_deleted) VALUES
(1, '2024-08-22 00:00:01', 10, 1, '1 번째 글 내용', '1 번째 글 제목', false),
(2, '2024-08-22 00:00:02', 11, 2, '2 번째 글 내용', '2 번째 글 제목', false),
(3, '2024-08-22 00:00:03', 12, 3, '3 번째 글 내용', '3 번째 글 제목', false),
(1, '2024-08-22 00:00:04', 13, 1, '4 번째 글 내용', '4 번째 글 제목', false),
(2, '2024-08-22 00:00:05', 14, 2, '5 번째 글 내용', '5 번째 글 제목', false),
(3, '2024-08-22 00:00:06', 15, 3, '6 번째 글 내용', '6 번째 글 제목', false),
(1, '2024-08-22 00:00:07', 16, 1, '7 번째 글 내용', '7 번째 글 제목', false),
(2, '2024-08-22 00:00:08', 17, 2, '8 번째 글 내용', '8 번째 글 제목', false),
(3, '2024-08-22 00:00:09', 18, 3, '9 번째 글 내용', '9 번째 글 제목', false),
(1, '2024-08-22 00:00:10', 19, 1, '10 번째 글 내용', '10 번째 글 제목', false);

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
