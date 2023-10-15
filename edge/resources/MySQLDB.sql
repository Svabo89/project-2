CREATE DATABASE MySQLDB;
USE MySQLDB;
CREATE TABLE STUDENT (
	ID VARCHAR(1),
	FullName VARCHAR(1),
);

CREATE TABLE INSTRUCTOR (
	FullName VARCHAR(1),
	ID DOUBLE,
CONSTRAINT INSTRUCTOR_PK PRIMARY KEY (ID)
);

CREATE TABLE COURSE (
	ID VARCHAR(1),
	Name VARCHAR(1),
	InstructorID VARCHAR(1),
);

