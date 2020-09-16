package com.thoughtworks.rslist.api;


import com.thoughtworks.rslist.dto.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

	private Map<String, User> userList = new HashMap<>();

	public Map<String, User> getUserList() {
		return userList;
	}

	public void addUser(String Username, User user) {
		userList.put(Username, user);
	}
}
