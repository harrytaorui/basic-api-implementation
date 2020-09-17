package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.dto.RsEvent;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	UserService userService;

	@Autowired
	RsEventService rsEventService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	RsEventRepository rsEventRepository;

	@BeforeEach
	void startUp() {
		userRepository.deleteAll();
		rsEventRepository.deleteAll();
		UserEntity user = UserEntity.builder()
				.userName("user1")
				.age(19)
				.gender("male")
				.email("1234567@qq.com")
				.phone("12211333333")
				.build();
		userRepository.save(user);
		RsEventEntity rsEventEntity = RsEventEntity.builder()
				.user(user)
				.keyword("1")
				.eventName("下雨了")
				.build();
		rsEventRepository.save(rsEventEntity);

	}

//
//	@Test
//	void should_get_one_rs_event() throws Exception {
//		mockMvc.perform(get("/rs/1"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.eventName", is("第一条事件")))
//				.andExpect(jsonPath("$.keyword", is("1")));
//		mockMvc.perform(get("/rs/2")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("第二条事件"))).
//				andExpect(jsonPath("$.keyword", is("2")));
//		mockMvc.perform(get("/rs/3")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("第三条事件"))).
//				andExpect(jsonPath("$.keyword", is("3")));
//	}
//
//	@Test
//	void should_get_rs_event_by_range() throws Exception {
//		mockMvc.perform(get("/rs/list?start=1&end=3")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$", hasSize(3))).
//				andExpect(jsonPath("$[0].eventName", is("第一条事件"))).
//				andExpect(jsonPath("$[0].keyword", is("1"))).
//				andExpect(jsonPath("$[1].eventName", is("第二条事件"))).
//				andExpect(jsonPath("$[1].keyword", is("2"))).
//				andExpect(jsonPath("$[2].eventName", is("第三条事件"))).
//				andExpect(jsonPath("$[2].keyword", is("3")));
//	}
//
//	@Test
//	void should_add_rs_event() throws Exception {
//
//		RsEvent rsEvent = new RsEvent("第四条事件", "4", 1);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isCreated());
//		mockMvc.perform(get("/rs/4")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("第四条事件"))).
//				andExpect(jsonPath("$.keyword", is("4")));
//
//	}
//
//	@Test
//	void add_event_user_can_not_be_null() throws Exception {
//		RsEvent rsEvent = new RsEvent("第四条事件", "4", null);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isBadRequest());
//	}
//
//	@Test
//	void add_event_EventName_can_not_be_null() throws Exception {
//		RsEvent rsEvent = new RsEvent(null, "1", 1);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isBadRequest());
//	}
//
//	@Test
//	void add_event_keyword_can_not_be_null() throws Exception {
//		RsEvent rsEvent = new RsEvent("第四条事件", null, 1);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isBadRequest());
//	}
//
//	@Test
//	void add_event_with_existed_User() throws Exception {
//		User defaultUser = new User("tao", 19, "male", "1234567@qq.com", "12211333333");
//		userService.addUser(defaultUser);
//		RsEvent rsEvent = new RsEvent("第四条事件", "4", 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isCreated());
//		assertEquals(userService.getUserList().size(),1);
//	}
//
//	@Test
//	void add_event_with_New_User() throws Exception {
//		User defaultUser = new User("tao", 19, "male", "1234567@qq.com", "12211333333");
//		userService.addUser(defaultUser);
//		RsEvent rsEvent = new RsEvent("第四条事件", "4", 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isCreated());
//		assertEquals(userService.getUserList().size(),2);
//		Assertions.assertTrue(userService.getUserList()
//				.stream().anyMatch(e->e.getUserName().equals("小王")));
//	}
//
//	@Test
//	void should_modify_Rs_Event_No_Keyword() throws Exception {
//		RsEvent rsEvent = new RsEvent("汪峰上热搜了", null, 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isOk());
//		mockMvc.perform(get("/rs/1")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("汪峰上热搜了"))).
//				andExpect(jsonPath("$.keyword", is("1")));
//	}
//
//
//	@Test
//	void should_modify_Rs_Event_No_Name() throws Exception {
//		RsEvent rsEvent = new RsEvent(null, "2", 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isOk());
//		mockMvc.perform(get("/rs/1")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("第一条事件"))).
//				andExpect(jsonPath("$.keyword", is("2")));
//	}
//
//	@Test
//	void should_modify_Rs_Event_With_Both_Param() throws Exception {
//		RsEvent rsEvent = new RsEvent("汪峰上热搜了", null, 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(put("/rs/event/1").content(json).contentType(MediaType.APPLICATION_JSON)).
//				andExpect(status().isOk());
//		mockMvc.perform(get("/rs/1")).
//				andExpect(status().isOk()).
//				andExpect(jsonPath("$.eventName", is("汪峰上热搜了"))).
//				andExpect(jsonPath("$.keyword", is("1")));
//	}
//
//	@Test
//	void should_delete_Rs_Event() throws Exception {
//		mockMvc.perform(delete("/rs/event?id=1")).
//				andExpect(status().isOk());
//		mockMvc.perform(get("/rs/1"))
//				.andExpect(status().isOk())
//				.andExpect(jsonPath("$.eventName", not("第一条事件")))
//				.andExpect(jsonPath("$.keyword", not("1")));
//	}
//
//	@Test
//	void should_return_index_when_add_event() throws Exception {
//		RsEvent rsEvent = new RsEvent("第四条事件", "4", 1);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json)
//				.contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isCreated())
//				.andExpect(header().string("index", "3"));
//	}
//
//	@Test
//	void should_ignore_user_when_get_event() throws Exception {
//		mockMvc.perform(get("/rs/1")).andExpect(jsonPath("$", not(hasKey("user"))));
//	}
//
//
//	@Test
//	void should_return_invalid_request_param_when_start_end_invalid() throws Exception {
//		mockMvc.perform(get("/rs/list?start=-1")).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.error", is("invalid request param")));
//
//	}
//
//	@Test
//	void should_return_invalid_index_when_index_invalid() throws Exception {
//		mockMvc.perform(get("/rs/-1")).andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.error", is("invalid index")));
//	}
//
//	@Test
//	void should_return_invalid_param_when_param_invalid() throws Exception {
//		RsEvent rsEvent = new RsEvent("第四条事件", null, 1);
//		String json = rsEvent.toJsonWithUser();
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.error", is("invalid param")));
//	}
//
//	@Test
//	void should_add_event_when_user_exist() throws Exception {
//		UserEntity user = UserEntity.builder()
//				.userName("user1")
//				.age(19)
//				.gender("male")
//				.email("1234567@qq.com")
//				.phone("12211333333")
//				.build();
//		userRepository.save(user);
//		RsEvent rsEvent = new RsEvent("第四条事件", "2", 1);
//		String json = getJsonString(rsEvent);
//		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON))
//				.andExpect(status().isBadRequest())
//				.andExpect(jsonPath("$.error", is("invalid param")));
//		List<RsEventEntity> rsEvents = rsEventRepository.findAll();
//		assertEquals(rsEvents.size(),1);
//
//	}


	@Test
	void should_add_rsEvent_when_user_exist() throws Exception {
		RsEvent rsEvent = RsEvent.builder().eventName("花木兰电影上映").keyword("2")
				.userId(1).build();
		String jsonString = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		assertEquals(rsEventEntityList.size(), 2);
		assertEquals(rsEventEntityList.get(1).getEventName(), "花木兰电影上映");
	}

	@Test
	void should_fail_add_rsEvent_when_user_not_exist() throws Exception {
		RsEvent rsEvent = RsEvent.builder().eventName("花木兰电影上映").keyword("2")
				.userId(2).build();
		String jsonString = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		assertEquals(rsEventEntityList.size(), 1);
	}

	private String getJsonString(RsEvent rsEvent) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(rsEvent);
	}

	private User createUser() {
		return new User("小王", 19, "male", "1234567@qq.com", "12211333333");
	}
}
