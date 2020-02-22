CREATE TABLE acsqs201903.study_user (
  id varchar(32) NOT NULL PRIMARY KEY,
  userName varchar(32) NOT NULL,
  passWord varchar(50) NOT NULL,
  realName varchar(32) DEFAULT NULL
)