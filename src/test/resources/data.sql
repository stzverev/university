DROP TABLE IF EXISTS tabletime CASCADE;
DROP TABLE IF EXISTS teachers_courses CASCADE;
DROP TABLE IF EXISTS groups_courses CASCADE;
DROP TABLE IF EXISTS courses CASCADE;
DROP TABLE IF EXISTS teachers CASCADE;
DROP TABLE IF EXISTS students CASCADE;
DROP TABLE IF EXISTS groups CASCADE;

CREATE TABLE groups(
    id SERIAL NOT NULL,
    name varchar(150),
    CONSTRAINT PK_groups PRIMARY KEY(id)
);

CREATE TABLE students(
    id SERIAL NOT NULL,
    first_name varchar(150),
    last_name varchar(150),
    group_id int,
    CONSTRAINT PK_students PRIMARY KEY(id),
    CONSTRAINT FK_students_groups FOREIGN KEY (group_id) REFERENCES groups(id)
);

CREATE TABLE teachers(
    id SERIAL NOT NULL,
    first_name varchar(150),
    last_name varchar(150),
    CONSTRAINT PK_teachers PRIMARY KEY(id)
);

CREATE TABLE courses(
    id SERIAL NOT NULL,
    name varchar(150),
    CONSTRAINT PK_courses PRIMARY KEY(id)
);

CREATE TABLE teachers_courses(
    teacher_id int NOT NULL,
    course_id int NOT NULL,
    CONSTRAINT PK_teachers_courses PRIMARY KEY (teacher_id, course_id),
    CONSTRAINT FK_teachers_courses_teachers FOREIGN KEY (teacher_id) REFERENCES teachers(id),
    CONSTRAINT FK_teachers_courses_courses FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE groups_courses (
    group_id int NOT NULL,
    course_id int NOT NULL,
    CONSTRAINT PK_groups_courses PRIMARY KEY (group_id, course_id),
    CONSTRAINT FK_groups_courses_groups FOREIGN KEY (group_id) REFERENCES groups(id),
    CONSTRAINT FK_groups_courses_courses FOREIGN KEY (course_id) REFERENCES courses(id)
);

CREATE TABLE tabletime(
    id SERIAL NOT NULL,
    date_time timestamp NOT NULL,
    group_id int NOT NULL,
    course_id int NOT NULL,
    teacher_id int NOT null,
    CONSTRAINT PK_tabletime PRIMARY KEY (id),
    CONSTRAINT FK_tabletime_groups FOREIGN KEY (group_id) REFERENCES groups(id),
    CONSTRAINT FK_tabletime_courses FOREIGN KEY (course_id) REFERENCES courses(id),
    CONSTRAINT FK_tabletime_teachers FOREIGN KEY (teacher_id) REFERENCES teachers(id)   
)
