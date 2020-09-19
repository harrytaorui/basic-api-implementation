package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Entity.VoteEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Repository.VoteRepository;
import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UpdateEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.dto.VoteRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

	@Autowired
	VoteRepository voteRepository;

	ObjectMapper objectMapper = new ObjectMapper();

	@BeforeEach
	void startUp() {

		UserEntity user = UserEntity.builder()
				.userName("user1")
				.age(19)
				.gender("male")
				.email("1234567@qq.com")
				.phone("12211333333")
				.votes(10)
				.build();
		userRepository.save(user);
		RsEventEntity rsEventEntity1 = RsEventEntity.builder()
				.user(user)
				.keyword("1")
				.eventName("下雨了")
				.votes(0)
				.build();
		rsEventRepository.save(rsEventEntity1);
		RsEventEntity rsEventEntity2 = RsEventEntity.builder()
				.user(user)
				.keyword("2")
				.eventName("打雷了")
				.votes(2)
				.build();
		rsEventRepository.save(rsEventEntity2);

	}

	@AfterEach
	void tearDown() {
		voteRepository.deleteAll();
		rsEventRepository.deleteAll();
		userRepository.deleteAll();


	}

	@Test
	void should_get_one_rs_event() throws Exception {
		mockMvc.perform(get("/rs/2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.eventName", is("下雨了")))
				.andExpect(jsonPath("$.keyword", is("1")))
				.andExpect(jsonPath("$.id", is(2)))
				.andExpect(jsonPath("$.votes", is(0)));
	}

	@Test
	void should_get_rs_event_by_range() throws Exception {
		mockMvc.perform(get("/rs/list?start=1&end=2"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].eventName", is("下雨了")))
				.andExpect(jsonPath("$[0].keyword", is("1")))
				.andExpect(jsonPath("$[0].id", is(2)))
				.andExpect(jsonPath("$[0].votes", is(0)))
				.andExpect(jsonPath("$[1].eventName", is("打雷了")))
				.andExpect(jsonPath("$[1].keyword", is("2")))
				.andExpect(jsonPath("$[1].id", is(3)))
				.andExpect(jsonPath("$[1].votes", is(2)));
	}

	@Test
	void should_add_rs_event() throws Exception {

		RsEvent rsEvent = new RsEvent("第四条事件", "4", createUser(), 2, 1, 4);
		String json = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
				andExpect(status().isCreated());
		mockMvc.perform(get("/rs/4"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.eventName", is("第四条事件")))
				.andExpect(jsonPath("$.keyword", is("4")))
				.andExpect(jsonPath("$.id", is(4)))
				.andExpect(jsonPath("$.votes", is(2)));
	}

	@Test
	void add_event_user_can_not_be_null() throws Exception {
		RsEvent rsEvent = new RsEvent("第四条事件", "4", null, 2, 1, 4);
		String json = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
				andExpect(status().isBadRequest());
	}

	@Test
	void add_event_EventName_can_not_be_null() throws Exception {
		RsEvent rsEvent = new RsEvent(null, "4", createUser(), 2, 1, 4);
		String json = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
				andExpect(status().isBadRequest());
	}

	@Test
	void add_event_keyword_can_not_be_null() throws Exception {
		RsEvent rsEvent = new RsEvent("第四条事件", null, createUser(), 2, 1, 4);
		String json = rsEvent.toJsonWithUser();
		mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
				andExpect(status().isBadRequest());
	}

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


	@Test
	void should_update_event_if_event_created_by_user() throws Exception {
		UpdateEvent event = new UpdateEvent("流星", "3", 1);
		String jsonString = objectMapper.writeValueAsString(event);
		mockMvc.perform(patch("/rs/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		RsEventEntity entity = rsEventEntityList.get(0);
		assertEquals(entity.getEventName(), "流星");
		assertEquals(entity.getKeyword(), "3");
	}

	@Test
	void should_only_update_eventName_if_keyword_null() throws Exception {
		UpdateEvent event = new UpdateEvent("流星", null, 1);
		String jsonString = objectMapper.writeValueAsString(event);
		mockMvc.perform(patch("/rs/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		RsEventEntity entity = rsEventEntityList.get(0);
		assertEquals(entity.getEventName(), "流星");
		assertEquals(entity.getKeyword(), "1");
	}

	@Test
	void should_only_update_keyword_if_eventName_null() throws Exception {
		UpdateEvent event = new UpdateEvent(null, "3", 1);
		String jsonString = objectMapper.writeValueAsString(event);
		mockMvc.perform(patch("/rs/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		RsEventEntity entity = rsEventEntityList.get(0);
		assertEquals(entity.getEventName(), "下雨了");
		assertEquals(entity.getKeyword(), "3");
	}

	@Test
	void should_fail_update_if_userId_is_wrong() throws Exception {
		UpdateEvent event = new UpdateEvent("流星", "3", 2);
		String jsonString = objectMapper.writeValueAsString(event);
		mockMvc.perform(patch("/rs/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
		List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
		RsEventEntity entity = rsEventEntityList.get(0);
		assertEquals(entity.getEventName(), "下雨了");
		assertEquals(entity.getKeyword(), "1");
	}

	@Test
	void should_vote_when_remain_votes_greater_than_voteNum() throws Exception {
		VoteRecord record = new VoteRecord(5, 1,LocalDateTime.now());
		String jsonString = objectMapper.writeValueAsString(record);
		mockMvc.perform(post("/rs/vote/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
		VoteEntity voteEntity = voteRepository.findAll().get(0);
		UserEntity userEntity = userRepository.findAll().get(0);
		assertEquals(voteEntity.getVoteNum(), 5);
		assertEquals(voteEntity.getUser().getId(), 1);
		assertEquals(userEntity.getVotes(), 5);
	}

	@Test
	void should_fail_vote_when_remain_votes_less_than_voteNum() throws Exception {
		VoteRecord record = new VoteRecord(11, 1, LocalDateTime.now());
		String jsonString = objectMapper.writeValueAsString(record);
		mockMvc.perform(post("/rs/vote/2").content(jsonString)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

	private User createUser() {
		return new User("小王", 19, "male", "1234567@qq.com", "12211333333");
	}
}
