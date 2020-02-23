-- 人员信息
CREATE TABLE acsqs201903.study_user (
  id varchar(32) NOT NULL PRIMARY KEY,
  userName varchar(32) NOT NULL,
  passWord varchar(50) NOT NULL,
  realName varchar(32) DEFAULT NULL
)

-- 假期申请表
CREATE TABLE apply_vacation {
 id varchar2(20) NOT NULL PRIMARY KEY,
 apply_no varchar2(20) NOT NULL,
 apply_time_length INTEGER NOT null,
 apply_time DATE NOT NULL,
 apply_name varchar2(10) NOT null
}