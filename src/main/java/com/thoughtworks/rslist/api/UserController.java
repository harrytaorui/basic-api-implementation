package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;


@RestController
public class UserController {

	@Autowired
	UserService userService;


	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers() {
		return ResponseEntity.ok().body(userService.getUserList());
	}

	@PostMapping("/user/register")
	public ResponseEntity register(@Valid @RequestBody User user) {
		userService.addUser(user);
		int index = userService.getUserList().indexOf(user);
		return ResponseEntity.status(201).header("index",String.valueOf(index)).build();
	}


}
