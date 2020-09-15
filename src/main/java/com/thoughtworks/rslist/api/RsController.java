package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
  public void modifyEvent(@RequestParam int id,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) String keyword) {
    int index = id - 1;
    if (isInList(index)) {
      RsEvent rsEvent = rsList.get(index);
      if (name != null) {
        rsEvent.setEventName(name);
      }
      if (keyword != null){
        rsEvent.setKeyWord(keyword);
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
}
