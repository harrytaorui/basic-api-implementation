package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.DataSource.DataSourceConfig;
import com.thoughtworks.rslist.Entity.VoteEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.Repository.VoteRepository;
import com.thoughtworks.rslist.Service.RsEventService;
import com.thoughtworks.rslist.Service.UserService;
import com.thoughtworks.rslist.Service.VoteService;
import com.thoughtworks.rslist.dto.VoteRecord;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class VoteController {
	private final int PAGE_SIZE = 5;
	private final UserRepository userRepository;
	private final RsEventRepository rsEventRepository;
	private final VoteRepository voteRepository;
	ApplicationContext context = new AnnotationConfigApplicationContext(DataSourceConfig.class);
	RsEventService rsEventService = context.getBean(RsEventService.class);
	UserService userService = context.getBean(UserService.class);
	VoteService voteService = context.getBean(VoteService.class);


	public VoteController(UserRepository userRepository,
	                      RsEventRepository rsEventRepository,
	                      VoteRepository voteRepository) {
		this.userRepository = userRepository;
		this.rsEventRepository = rsEventRepository;
		this.voteRepository = voteRepository;
	}

	@GetMapping("/vote")
	public ResponseEntity<List<VoteRecord>> getVote(@RequestParam int userId,
	                                                @RequestParam int eventId,
	                                                @RequestParam(defaultValue = "1")
			                                                int pageIndex) {
		Pageable pageable = PageRequest.of(pageIndex - 1, PAGE_SIZE);
		List<VoteEntity> voteResult = voteRepository.getRecordByUserIdAndEventId(userId, eventId, pageable);
		List<VoteRecord> voteRecords = voteResult.stream().map(vote -> voteService.convertEntityToVote(vote))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(voteRecords);
	}

	@GetMapping("/vote/time")
	public ResponseEntity<List<VoteRecord>> getTimeRangeVote
			(@RequestParam
			 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
			 @RequestParam
			 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
		List<VoteEntity> voteResult = voteRepository.findAllByVoteTimeBetween(startTime, endTime);
		List<VoteRecord> voteRecords = voteResult.stream().map(vote -> voteService.convertEntityToVote(vote))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body(voteRecords);
	}
}
