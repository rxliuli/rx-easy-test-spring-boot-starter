create table user
(
  id            int primary key auto_increment not null,
  username      varchar(20)                    not null,
  password      varchar(20)                    not null,
  lastLoginTime datetime                       not null
);
