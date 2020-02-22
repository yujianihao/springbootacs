package com.study.study.mapper;

import org.springframework.stereotype.Repository;

import com.study.study.dao.User;

import tk.mybatis.mapper.common.Mapper;

@Repository
public interface UserMapper extends Mapper<User>{

	public void inserUser(User user);
	
	public User selectByKey(String id);
}
