package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;



@RestController
public class UserController {

	@Autowired
	UserService userService;

	@PostMapping("/user/register")
	public ResponseEntity register(@Valid @RequestBody User user) {
		userService.addUser(user);
		int index = userService.getUserList().indexOf(user);
		return ResponseEntity.status(201).header("index",String.valueOf(index)).build();
	}

}
