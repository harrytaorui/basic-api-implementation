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
  public List<RsEvent> getRsEventByRange(
          @RequestParam(required = false) Integer start,
          @RequestParam(required = false) Integer end) {
    if (start == null || end == null) {
      return rsList;
    }
    return rsList.subList(start - 1, end);

  }

  @PostMapping("/rs/event")
  public void addRsEvent(@Valid @RequestBody RsEvent rsEvent) throws JsonProcessingException {
    User user = rsEvent.getUser();
    if (userService.getUserList().get(user.getUserName()) == null) {
      userService.addUser(user.getUserName(), rsEvent.getUser());
    }
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/event/{id}")
  public void modifyEvent(@PathVariable int id, @RequestBody RsEvent requestEvent) {

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
  }


  @DeleteMapping("/rs/event")
  public void deleteEvent(@RequestParam int id) {
    int index = id - 1;
    if (isInList(index)) {
      rsList.remove(index);
    }
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
