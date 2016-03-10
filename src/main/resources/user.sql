drop database if exists test;

create database test character set utf8;

use test;

create table user (
	id varchar(255) unique,
	username varchar(255) not null,
	password varchar(255) not null
) default charset=utf8;

insert into user values('1', 'rookiefly1', 'a');
insert into user values('2', 'rookiefly2', 'b');
insert into user values('3', 'rookiefly3', 'c');