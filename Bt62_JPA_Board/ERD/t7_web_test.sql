SELECT TABLE_NAME
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'db326'
  AND TABLE_NAME LIKE 't7_%'
;

# 외래 키 제약 조건 비활성화
SET FOREIGN_KEY_CHECKS = 0;

# 테이블 삭제 (순서 주의)
DROP TABLE IF EXISTS t7_comment;
DROP TABLE IF EXISTS t7_attachment;
DROP TABLE IF EXISTS t7_post;
DROP TABLE IF EXISTS t7_user_authorities;
DROP TABLE IF EXISTS t7_user;
DROP TABLE IF EXISTS t7_authority;

# 외래 키 제약 조건 활성화
# SET FOREIGN_KEY_CHECKS = 1;
desc t7_authority;
SELECT * FROM t7_authority;
SELECT * FROM t7_user ORDER BY id DESC;
SELECT * FROM t7_user_authorities;
SELECT * FROM t7_post ORDER BY id DESC;
SELECT * FROM t7_comment ORDER BY id DESC;
SELECT * FROM t7_attachment ORDER BY id DESC;

