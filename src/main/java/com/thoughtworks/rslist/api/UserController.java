package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exceptions.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RsEventRepository rsEventRepository;

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok().body(new ArrayList<>(userService.getUserList()));
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUser(@PathVariable int userId) {
		Optional<UserEntity> result = userRepository.findById(userId);
		if (!result.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		UserEntity userEntity = result.get();
		return ResponseEntity.ok().body(userService.convertEntityToUser(userEntity));
	}

	@PostMapping("/users")
	public ResponseEntity register(@Valid @RequestBody User user, BindingResult bindingResult) {
		if (bindingResult.getFieldErrors().size() > 0) {
			throw new MyException("invalid user");
		}
		UserEntity userEntity = userService.createUserEntity(user);
		userRepository.save(userEntity);
		return ResponseEntity.status(201).header("index", String.valueOf(userEntity.getId())).build();
	}


	@Transactional
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@DeleteMapping("/users/{userId}")
	public void deleteUser(@PathVariable int userId) {
		Optional<UserEntity> userEntity = userRepository.findById(userId);
		if (!userEntity.isPresent()) {
			throw new MyException("user not exist");
		}
		userRepository.deleteById(userId);
	}

}
