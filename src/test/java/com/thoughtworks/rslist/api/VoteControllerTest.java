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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteControllerTest {

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
		VoteEntity voteEntity1 = VoteEntity.builder()
				.voteNum(1)
				.voteTime(LocalDateTime.now())
				.build();
		VoteEntity voteEntity2 = VoteEntity.builder()
				.voteNum(2)
				.voteTime(LocalDateTime.now())
				.build();
		voteRepository.save(voteEntity1);
		voteRepository.save(voteEntity2);
	}

	@AfterEach
	void tearDown() {
		userRepository.deleteAll();
		rsEventRepository.deleteAll();
		voteRepository.deleteAll();
	}

	@Test
	void should_get_vote_in_page_by_userId_eventId() throws Exception {
		mockMvc.perform(get("/vote").param("userId", "1").param("eventId", "2"))
				.andExpect(status().isOk());
	}

	@Test
	void should_get_vote_record_in_time_range() throws Exception {
		mockMvc.perform(get("/vote/time").param("startTime", LocalDateTime.MIN.toString()).param("endTime", LocalDateTime.now().toString()))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].voteNum", is(1)))
				.andExpect(jsonPath("$[1].voteNum", is(2)));

	}

}
