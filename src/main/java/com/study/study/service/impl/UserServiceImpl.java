package com.study.study.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.study.study.dao.User;
import com.study.study.mapper.UserMapper;
import com.study.study.service.UserService;

import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserMapper userMapper;

	@Override
	public User insertUser() {
//		User user = new User();
//		user.setId("3");
//		user.setPassWord("11111");
//		user.setUserName("test");
//		user.setRealName("test");
		
//		userMapper.insert(user);
//		Example example = new Example(User.class);
//		Criteria criteria = example.createCriteria();
//		criteria.andEqualTo("id", "1");
		return userMapper.selectByKey("1");
		//return user;
	}
}
