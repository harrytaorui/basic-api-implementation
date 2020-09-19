package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.DataSource.DataSourceConfig;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Repository.VoteRepository;
import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exceptions.MyException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
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

	private final UserRepository userRepository;
	private final RsEventRepository rsEventRepository;
	private final VoteRepository voteRepository;
	ApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
	RsEventService rsEventService = context.getBean(RsEventService.class);
	UserService userService = context.getBean(UserService.class);

	public UserController(UserRepository userRepository,
	                      RsEventRepository rsEventRepository,
	                      VoteRepository voteRepository) {
		this.userRepository = userRepository;
		this.rsEventRepository = rsEventRepository;
		this.voteRepository = voteRepository;
	}

	@GetMapping("/users")
	public ResponseEntity<List<User>> getAllUsers() {
		return ResponseEntity.ok().body(new ArrayList<>(userService.getUserList()));
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<User> getUser(@PathVariable int id) {
		Optional<UserEntity> result = userRepository.findById(id);
		if (!result.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		UserEntity userEntity = result.get();
		return ResponseEntity.ok().body(userService.convertEntityToUser(userEntity));
	}

	@PostMapping("/user/register")
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
	@DeleteMapping("/user/delete{id}")
	public void deleteUser(@PathVariable int id) {
		Optional<UserEntity> userEntity = userRepository.findById(id);
		if (!userEntity.isPresent()) {
			throw new MyException("user not exist");
		}
		userRepository.deleteById(id);
	}

}
