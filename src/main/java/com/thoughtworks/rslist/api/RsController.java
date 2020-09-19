package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import com.thoughtworks.rslist.dto.VoteRecord;
import com.thoughtworks.rslist.exceptions.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RestController
public class RsController {

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

	@GetMapping("/rs/{id}")
	public ResponseEntity<RsEvent> getRsEvent(@PathVariable int id) {
		Optional<RsEventEntity> result = rsEventRepository.findById(id);
		if (!result.isPresent()) {
			throw new MyException("invalid index");
		}
		RsEventEntity resultEvent = result.get();
		RsEvent rsEvent = rsEventService.convertEntityToEvent(resultEvent);
		return ResponseEntity.ok().body(rsEvent);
	}

	@GetMapping("/rs/list")
	public ResponseEntity<List<RsEvent>> getRsEventByRange(
			@RequestParam(required = false) Integer start,
			@RequestParam(required = false) Integer end) {
		List<RsEventEntity> list = rsEventRepository.findAll();
		start = start == null ? 0 : start - 1;
		end = end == null ? list.size() : end;
		if (!isInList(start) || !isInList(end - 1)) {
			throw new MyException("invalid request param");
		}
		List<RsEvent> result = list.stream()
				.map(e -> rsEventService.convertEntityToEvent(e))
				.collect(Collectors.toList());
		return ResponseEntity.ok().body((result.subList(start, end)));
	}

	@Transactional
	@PostMapping("/rs/event")
	public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent,
	                                 BindingResult bindingResult)
			throws JsonProcessingException {
		if (bindingResult.getFieldErrors().size() > 0) {
			throw new MyException(bindingResult.getAllErrors().toString());
		}
		RsEventEntity rsEventEntity = rsEventService.convertEventToEntity(rsEvent);
		if (!userRepository.existsById(rsEventEntity.getUser().getId())) {
			return ResponseEntity.badRequest().build();
		}
		rsEventRepository.save(rsEventEntity);
		return ResponseEntity.status(201)
				.header("index", String.valueOf(rsEventEntity.getId())).build();
	}

//  @PutMapping("/rs/event/{id}")
//  public ResponseEntity modifyEvent(@PathVariable int id, @RequestBody RsEvent requestEvent) {
//
//    int index = id - 1;
//    if (!isInList(index)) {
//      throw new IndexOutOfBoundsException();
//    }
//    RsEvent rsEvent = .get(index);
//    if (requestEvent.getEventName() != null) {
//      rsEvent.setEventName(requestEvent.getEventName());
//    }
//    if (requestEvent.getKeyword() != null) {
//      rsEvent.setKeyword(requestEvent.getKeyword());
//    }
//    return ResponseEntity.ok().build();
//  }

	@Transactional
	@PatchMapping("/rs/{rsEventId}")
	public ResponseEntity updateEvent(@PathVariable int rsEventId,
	                                  @RequestBody UpdateEvent updateEvent) {
		int userId = updateEvent.getUserId();
		if (!userRepository.existsById(userId)) {
			return ResponseEntity.badRequest().build();
		}
		Optional<RsEventEntity> result = rsEventRepository.findById(rsEventId);
		if (!result.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		RsEventEntity entity = result.get();
		if (entity.getUser().getId() != userId) {
			return ResponseEntity.badRequest().build();
		}
		String eventName, keyword;
		if ((eventName = updateEvent.getEventName()) != null) {
			entity.setEventName(eventName);
		}
		if ((keyword = updateEvent.getKeyword()) != null) {
			entity.setKeyword(keyword);
		}
		rsEventRepository.save(entity);
		return ResponseEntity.ok().build();
	}

	@Transactional
	@DeleteMapping("/rs/event")
	public ResponseEntity deleteEvent(@RequestParam int id) {
		if (isInList(id)) {
			rsEventRepository.deleteById(id);
		}
		return ResponseEntity.ok().build();
	}

	@Transactional
	@PostMapping("/rs/vote/{rsEventId}")
	public ResponseEntity voteEvent(@PathVariable int rsEventId,
	                                @RequestBody VoteRecord record) {
		Optional<RsEventEntity> eventResult = rsEventRepository.findById(rsEventId);
		if (!eventResult.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		RsEventEntity rsEventEntity = eventResult.get();
		Optional<UserEntity> result = userRepository.findById(record.getUserId());
		if (!result.isPresent()) {
			return ResponseEntity.badRequest().build();
		}
		UserEntity userEntity = result.get();
		int remainVotes = userEntity.getVotes();
		int requestVotes = record.getVoteNum();
		if (remainVotes < requestVotes) {
			return ResponseEntity.badRequest().build();
		}
		//decrease user vote
		userEntity.setVotes(remainVotes - requestVotes);
		userRepository.save(userEntity);
		//increase event vote
		rsEventEntity.setVotes(rsEventEntity.getVotes() + requestVotes);
		rsEventRepository.save(rsEventEntity);
		//save vote record
		VoteEntity voteEntity = VoteEntity.builder()
				.user(userEntity)
				.voteNum(requestVotes)
				.voteTime(record.getVoteTime())
				.build();
		voteRepository.save(voteEntity);
		return ResponseEntity.status(201).build();
	}

	private boolean isInList(int index) {
		return index >= 0 && index < rsEventRepository.findAll().size();
	}

}
