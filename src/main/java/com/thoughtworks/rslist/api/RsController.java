package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.exceptions.CommonError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
public class RsController {

  private List<RsEvent> rsList = initRsList();

  private List<RsEvent> initRsList() {
    List<RsEvent> tempList = new ArrayList<>();
    tempList.add(new RsEvent("第一条事件", "1"));
    tempList.add(new RsEvent("第二条事件", "2"));
    tempList.add(new RsEvent("第三条事件", "3"));
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
  public void addRsEvent(@RequestBody String rsEventString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    RsEvent rsEvent = objectMapper.readValue(rsEventString, RsEvent.class);
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/event")
  public void modifyEvent(@RequestBody String json) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Map<String,String> map = objectMapper.readValue(json,Map.class);
    int index = Integer.parseInt(map.get("id")) - 1;
    String name = map.get("name");
    String keyword = map.get("keyword");
    if (isInList(index)) {
      RsEvent rsEvent = rsList.get(index);
      if (name != null) {
        rsEvent.setEventName(name);
      }
      if (keyword != null){
        rsEvent.setKeyword(keyword);
      }
    }
  }

  private boolean isInList(int index) {
    return index >= 0 && index < rsList.size();
  }

  @DeleteMapping("/rs/event")
  public void deleteEvent(@RequestParam int id) {
    int index = id - 1;
    if (isInList(index)) {
      rsList.remove(index);
    }
  }

  @ExceptionHandler(IndexOutOfBoundsException.class)
  private ResponseEntity<CommonError> handleIndexOutOfBoundsException(IndexOutOfBoundsException exception) {
    CommonError commonError = new CommonError();
    commonError.setError("invalid request param");
    return ResponseEntity.badRequest().body(commonError);
  }
}
