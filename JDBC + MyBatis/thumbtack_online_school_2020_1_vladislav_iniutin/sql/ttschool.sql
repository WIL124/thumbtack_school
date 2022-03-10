DROP DATABASE IF EXISTS ttschool;
CREATE DATABASE ttschool;
USE ttschool;

CREATE TABLE school (
id int auto_increment,
name varchar(50) not null,
year int not null,
primary key (id),
unique KEY (name, year)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `group` (
id int auto_increment,
name varchar(50) not null,
room varchar(50) not null,
school_id int not null,
primary key (id),
foreign key (school_id) references school (id) on delete cascade
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE trainee (
id int auto_increment,
firstName varchar(50) not null,
lastName varchar(50) not null,
rating int,
group_id int default null,
primary key (id),
foreign key (group_id) references `group` (id) on delete cascade
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE subject (
id int auto_increment,
name varchar(50) not null,
primary key (id)
)ENGINE=INNODB DEFAULT CHARSET=utf8;

create table group_subject (
group_id int not null,
subject_id int not null,
foreign key (group_id) references `group` (id) on delete cascade,
foreign key (subject_id) references subject (id) on delete cascade
)ENGINE=INNODB DEFAULT CHARSET=utf8;
