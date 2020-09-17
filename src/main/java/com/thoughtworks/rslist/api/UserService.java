package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.dto.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

	private List<User> userList = new ArrayList<>();

	public List<User> getUserList() {
		return userList;
	}

	public void addUser(User user) {
		userList.add(user);
	}

	public User convertEntityToUser(UserEntity userEntity) {
		return User.builder()
				.userName(userEntity.getUserName())
				.age(userEntity.getAge())
				.email(userEntity.getEmail())
				.phone(userEntity.getPhone())
				.gender(userEntity.getGender())
				.build();
	}

	public UserEntity createUserEntity(User user) {
		return UserEntity.builder()
				.userName(user.getUserName())
				.age(user.getAge())
				.email(user.getEmail())
				.phone(user.getPhone())
				.gender(user.getGender())
				.build();
	}
}
