package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Test
	void should_Register_User() throws Exception {
		String jsonString = getJsonString(new User("harry", 19, "male", "1234567@qq.com", "12211333333"));
		mockMvc.perform(post("/user/register").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
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
				.andExpect(header().string("index","0"));
	}


	@Test
	void should_return_all_users() throws Exception {
		userService.addUser(new User("harry", 19, "male", "1234567@qq.com", "12211333333"));
		userService.addUser(new User("wang", 22, "male", "311111@qq.com", "12211334567"));
		mockMvc.perform(get("/users")).andExpect(jsonPath("$",hasSize(2)))
				.andExpect(jsonPath("$[0].userName",is("harry")))
				.andExpect(jsonPath("$[0].age",is(19)))
				.andExpect(jsonPath("$[0].gender",is("male")))
				.andExpect(jsonPath("$[0].email",is("1234567@qq.com")))
				.andExpect(jsonPath("$[0].phone",is("12211333333")))
				.andExpect(jsonPath("$[1].userName",is("wang")))
				.andExpect(jsonPath("$[1].age",is(22)))
				.andExpect(jsonPath("$[1].gender",is("male")))
				.andExpect(jsonPath("$[1].email",is("311111@qq.com")))
				.andExpect(jsonPath("$[1].phone",is("12211334567")));
	}

	private String getJsonString(User user) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(user);
	}


}
