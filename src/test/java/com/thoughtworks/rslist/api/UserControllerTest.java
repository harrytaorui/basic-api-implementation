package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RsEventRepository rsEventRepository;

	@BeforeEach
	void startUp() {
		userRepository.deleteAll();
		UserEntity user1 = UserEntity.builder()
				.userName("user1")
				.age(19)
				.gender("male")
				.email("1234567@qq.com")
				.phone("12211333333")
				.build();
		userRepository.save(user1);
		UserEntity user2 = UserEntity.builder()
				.userName("user2")
				.age(19)
				.gender("male")
				.email("1234567@qq.com")
				.phone("12211333333")
				.build();
		userRepository.save(user2);
		RsEventEntity rsEventEntity1 = RsEventEntity.builder()
				.user(user1)
				.keyword("1")
				.eventName("下雨了")
				.build();
		rsEventRepository.save(rsEventEntity1);
		RsEventEntity rsEventEntity2 = RsEventEntity.builder()
				.user(user1)
				.keyword("2")
				.eventName("没下雨")
				.build();
		rsEventRepository.save(rsEventEntity2);
		RsEventEntity rsEventEntity3 = RsEventEntity.builder()
				.user(user2)
				.keyword("3")
				.eventName("雨")
				.build();
		rsEventRepository.save(rsEventEntity3);
	}

	@Test
	void should_Register_User() throws Exception {
		String jsonString = getJsonString(new User("harry", 19, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		List<UserEntity> users = userRepository.findAll();
		assertEquals(2, users.size());
		assertEquals("harry", users.get(1).getUserName());
	}


	@Test
	void should_Fail_when_Username_is_Empty() throws Exception {
		String jsonString = getJsonString(new User("", 19, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Username_is_Longer_than_8_Chars() throws Exception {
		String jsonString = getJsonString(new User("harrytaorui", 19, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Gender_is_Empty() throws Exception {
		String jsonString = getJsonString(new User("harry", 19, "", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Age_is_Empty() throws Exception {
		String jsonString = getJsonString(new User("harry", null, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Age_is_Lower_than_18() throws Exception {
		String jsonString = getJsonString(new User("harry", 11, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Age_is_higher_than_100() throws Exception {
		String jsonString = getJsonString(new User("harry", 110, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Email_is_in_Wrong_Format() throws Exception {
		String jsonString = getJsonString(new User("harry", 18, "male", "123456", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Phone_number_is_Empty() throws Exception {
		String jsonString = getJsonString(new User("harry", 18, "male", "1234567@qq.com", ""));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Phone_number_has_wrong_digits() throws Exception {
		String jsonString = getJsonString(new User("harry", 18, "male", "1234567@qq.com", "1221133333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_Fail_when_Phone_number_not_Starts_with_1() throws Exception {
		String jsonString = getJsonString(new User("harry", 18, "male", "1234567@qq.com", "22211133333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	@Test
	void should_return_index_when_create_user() throws Exception {
		String jsonString = getJsonString(new User("harry", 19, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().string("index", "0"));
	}

	@Test
	void should_return_invalid_user_when_user_invalid() throws Exception {
		String jsonString = getJsonString(new User("harry", 11, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error", is("invalid user")));
	}

	@Test
	void should_return_all_users() throws Exception {
		userService.addUser(new User("harry", 19, "male", "1234567@qq.com", "12211333333"));
		userService.addUser(new User("wang", 22, "male", "311111@qq.com", "12211334567"));
		mockMvc.perform(get("/users")).andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].user_name", is("harry")))
				.andExpect(jsonPath("$[0].user_age", is(19)))
				.andExpect(jsonPath("$[0].user_gender", is("male")))
				.andExpect(jsonPath("$[0].user_email", is("1234567@qq.com")))
				.andExpect(jsonPath("$[0].user_phone", is("12211333333")))
				.andExpect(jsonPath("$[1].user_name", is("wang")))
				.andExpect(jsonPath("$[1].user_age", is(22)))
				.andExpect(jsonPath("$[1].user_gender", is("male")))
				.andExpect(jsonPath("$[1].user_email", is("311111@qq.com")))
				.andExpect(jsonPath("$[1].user_phone", is("12211334567")));
	}

	@Test
	void should_get_user_by_id() throws Exception {
		mockMvc.perform(get("/users/1")).andExpect(status().isOk())
				.andExpect(jsonPath("$.user_name", is("user1")))
				.andExpect(jsonPath("$.user_age", is(19)))
				.andExpect(jsonPath("$.user_gender", is("male")))
				.andExpect(jsonPath("$.user_email", is("1234567@qq.com")))
				.andExpect(jsonPath("$.user_phone", is("12211333333")));
	}


	@Test
	void should_delete_user_by_id() throws Exception {
		mockMvc.perform(delete("/user/delete1")).andExpect(status().isNoContent());
		List<UserEntity> users = userRepository.findAll();
		assertEquals(users.size(),1);
	}

	@Test
	void should_delete_user_and_event() throws Exception {
		mockMvc.perform(delete("/user/delete1")).andExpect(status().isNoContent());
		List<UserEntity> users = userRepository.findAll();
		List<RsEventEntity> events = rsEventRepository.findAll();
		assertEquals(events.size(),1);
		assertNotEquals(events.get(0).getUser().getUserName(),"user1");

	}

	private String getJsonString(User user) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(user);
	}


}
