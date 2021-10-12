DROP TABLE IF EXISTS groups CASCADE;

CREATE TABLE groups(
    id SERIAL NOT NULL,
    name varchar(150),
    CONSTRAINT PK_groups PRIMARY KEY(id)
);

DROP TABLE IF EXISTS students CASCADE;

CREATE TABLE students(
    id SERIAL NOT NULL,
    first_name varchar(150),
    last_name varchar(150),
    group_id int,
    CONSTRAINT PK_students PRIMARY KEY(id),
    CONSTRAINT FK_students_groups FOREIGN KEY (group_id) REFERENCES groups(id)
);

DROP TABLE IF EXISTS teachers_courses CASCADE;

DROP TABLE IF EXISTS teachers CASCADE;

CREATE TABLE teachers(
    id SERIAL NOT NULL,
    first_name varchar(150),
    last_name varchar(150),
    CONSTRAINT PK_teachers PRIMARY KEY(id)
);

DROP TABLE IF EXISTS courses CASCADE;

CREATE TABLE courses(
    id SERIAL NOT NULL,
    name varchar(150),
    CONSTRAINT PK_courses PRIMARY KEY(id)
);

CREATE TABLE teachers_courses(
    teacher_id int NOT NULL,
    course_id int NOT NULL,
    CONSTRAINT PK_teachers_courses PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT FK_teachers FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    CONSTRAINT FK_courses FOREIGN KEY (course_id) REFERENCES courses(id)
);