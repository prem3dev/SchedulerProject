use scheduler;
CREATE TABLE authors
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '작성자 식별자',
    name VARCHAR(20) NOT NULL COMMENT '작성자 성함',
    creation_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성 날짜',
    modification_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '수정 날짜'
);

CREATE TABLE schedules
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '일정 식별자',
    password VARCHAR(20) NOT NULL COMMENT '일정 비밀번호',
    todo VARCHAR(500) NOT NULL COMMENT '일정 내용',
    author_id BIGINT COMMENT '작성자 식별자',
    FOREIGN KEY (author_id) REFERENCES authors(id)
);

DELIMITER $$

CREATE TRIGGER update_author_modification_timestamp
    AFTER UPDATE ON schedules
    FOR EACH ROW
BEGIN
    UPDATE authors
    set modification_timestamp = CURRENT_TIMESTAMP
    WHERE id = NEW.author_id;
end $$

DELIMITER ;

ALTER TABLE authors ADD COLUMN email VARCHAR(30) NOT NULL;
ALTER TABLE authors MODIFY COLUMN creation_timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE schedules
    ADD FOREIGN KEY (author_id)
        REFERENCES authors(id)
        ON DELETE CASCADE;

ALTER TABLE schedules MODIFY COLUMN todo VARCHAR(200) NOT NULL COMMENT '일정내용';
ALTER TABLE authors MODIFY COLUMN email VARCHAR(30) NOT NULL COMMENT '작성자 email';