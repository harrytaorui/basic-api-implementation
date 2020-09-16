package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import com.thoughtworks.rslist.exceptions.CommonError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class RsController {

  @Autowired
  UserService userService;

  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> tempList = new ArrayList<>();
    User defaultUser = new User("tao", 19, "male", "1234567@qq.com", "12211333333");
    tempList.add(new RsEvent("第一条事件", "1", defaultUser));
    tempList.add(new RsEvent("第二条事件", "2", defaultUser));
    tempList.add(new RsEvent("第三条事件", "3", defaultUser));
    return tempList;
  }

  @GetMapping("/rs/{index}")
  public RsEvent getRsEvent(@PathVariable int index) {
    return rsList.get(index - 1);
  }

  @GetMapping("/rs/list")
  public ResponseEntity<List<RsEvent>> getRsEventByRange(
          @RequestParam(required = false) Integer start,
          @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return ResponseEntity.ok().body(rsList);
    }
    if (isInList(start)&&isInList(end)){
      return ResponseEntity.ok().body((rsList.subList(start - 1, end)));
    }
    return ResponseEntity.badRequest().build();


  }

  @PostMapping("/rs/event")
  public ResponseEntity addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
    User user = rsEvent.getUser();
    boolean exist = userService.getUserList().stream()
            .anyMatch(e->e.getUserName().equals(user.getUserName()));
    if (!exist) {
      userService.addUser(user);
    }
    rsList.add(rsEvent);
    return ResponseEntity.status(201)
            .header("index",String.valueOf(rsList.indexOf(rsEvent))).build();
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

  @ExceptionHandler(IndexOutOfBoundsException.class)
  private ResponseEntity<CommonError> handleIndexOutOfBoundsException(IndexOutOfBoundsException exception) {
    CommonError commonError = new CommonError();
    commonError.setError("invalid request param");
    return ResponseEntity.badRequest().body(commonError);
  }
}
