DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students(
    id SERIAL NOT NULL,
    first_name varchar(150),
    last_name varchar(150),
    CONSTRAINT PK_students PRIMARY KEY(id)
);