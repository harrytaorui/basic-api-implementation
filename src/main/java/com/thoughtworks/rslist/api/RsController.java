package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.Entity.RsEventEntity;
import com.thoughtworks.rslist.Entity.UserEntity;
import com.thoughtworks.rslist.Repository.RsEventRepository;
import com.thoughtworks.rslist.Repository.UserRepository;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exceptions.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RestController
public class RsController {

  @Autowired
  UserService userService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RsEventRepository rsEventRepository;

  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> tempList = new ArrayList<>();
    User defaultUser = new User("tao", 19, "male", "1234567@qq.com", "12211333333");
    return tempList;
  }

  @JsonView(RsEvent.withOutUser.class)
  @GetMapping("/rs/{index}")
  public ResponseEntity<RsEvent> getRsEvent(@PathVariable int id) {
    Optional<RsEventEntity> result = rsEventRepository.findById(id);
    if (!result.isPresent()){
      throw new MyException("invalid index");
    }
    RsEventEntity resultEvent = result.get();
    UserEntity user = resultEvent.getUser();
    RsEvent rsEvent = RsEvent.builder().eventName(resultEvent.getEventName())
            .keyword(resultEvent.getKeyword())
            .userId(user.getId()).build();
    return ResponseEntity.ok().body(rsEvent);
  }

  @JsonView(RsEvent.withOutUser.class)
  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(
          @RequestParam(required = false) Integer start,
          @RequestParam(required = false) Integer end) {
    start = start == null ? 0 : start - 1;
    end = end == null ? rsList.size() : end;
    if (!isInList(start) || !isInList(end - 1)) {
      throw new MyException("invalid request param");
    }
    return ResponseEntity.ok().body((rsList.subList(start, end)));
  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent,
                                   BindingResult bindingResult)
          throws JsonProcessingException {
    if (bindingResult.getFieldErrors().size() > 0) {
      throw new MyException("invalid param");
    }
    if (!userRepository.existsById(rsEvent.getUserId())){
      return ResponseEntity.badRequest().build();
    }
    RsEventEntity rsEventEntity = RsEventEntity.builder()
            .eventName(rsEvent.getEventName())
            .keyword(rsEvent.getKeyword())
            .user(UserEntity.builder().build())
            .build();
    rsEventRepository.save(rsEventEntity);
    return ResponseEntity.status(201)
            .header("index", String.valueOf(rsEventEntity.getId())).build();
  }

  @PutMapping("/rs/event/{id}")
  public ResponseEntity modifyEvent(@PathVariable int id, @RequestBody RsEvent requestEvent) {

    int index = id - 1;
    if (!isInList(index)) {
      throw new IndexOutOfBoundsException();
    }
    RsEvent rsEvent = rsList.get(index);
    if (requestEvent.getEventName() != null) {
      rsEvent.setEventName(requestEvent.getEventName());
    }
    if (requestEvent.getKeyword() != null){
      rsEvent.setKeyword(requestEvent.getKeyword());
    }
    return ResponseEntity.ok().build();
  }


  @DeleteMapping("/rs/event")
  public ResponseEntity deleteEvent(@RequestParam int id) {
    int index = id - 1;
    if (isInList(index)) {
      rsList.remove(index);
    }
    return ResponseEntity.ok().build();
  }

  private boolean isInList(int index) {
    return index >= 0 && index < rsList.size();
  }

}
