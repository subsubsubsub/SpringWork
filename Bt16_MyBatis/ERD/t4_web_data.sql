-- 기존테이블 삭제 (데이터 초기화를 위한 sql)
DELETE FROM t4_post;
ALTER TABLE t4_post AUTO_INCREMENT = 1;
-- 샘플 글
INSERT INTO t4_post (user, subject, content) VALUES
    ('회원1', '제목입니다1', '내용입니다1'),
    ('회원2', '제목입니다2', '내용입니다2'),
    ('회원3', '제목입니다3', '내용입니다3'),
    ('회원4', '제목입니다4', '내용입니다4')
;
