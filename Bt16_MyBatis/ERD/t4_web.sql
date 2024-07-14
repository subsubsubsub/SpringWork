DROP TABLE IF EXISTS t4_post;

CREATE TABLE t4_post
(
    id      INT          NOT NULL AUTO_INCREMENT,
    user    VARCHAR(20)  NOT NULL,
    subject VARCHAR(200) NOT NULL,
    content LONGTEXT     NULL    ,
    viewcnt INT          NULL     DEFAULT 0,
    regdate DATETIME     NULL     DEFAULT now(),
    PRIMARY KEY (id)
);