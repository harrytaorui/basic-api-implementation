package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMvc.perform(get("/rs/1")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName", is("第一条事件")));
        mockMvc.perform(get("/rs/2")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName", is("第二条事件")));
        mockMvc.perform(get("/rs/3")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName", is("第三条事件")));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMvc.perform(get("/rs/list?start=1&end=3")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$",hasSize(3))).
                andExpect(jsonPath("$[0].eventName", is("第一条事件"))).
                andExpect(jsonPath("$[1].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[2].eventName", is("第三条事件")));
    }

    @Test
    void should_add_rs_event() throws Exception {

        RsEvent rsEvent = new RsEvent("第四条事件","");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(rsEvent);
        mockMvc.perform(post("/rs/event").content(json).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$", hasSize(4))).
                andExpect(jsonPath("$[0].eventName", is("第一条事件"))).
                andExpect(jsonPath("$[1].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[2].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[3].eventName", is("第四条事件")));

    }


    @Test
    void should_modify_Rs_Event_No_Keyword() throws Exception {
        mockMvc.perform(put("/rs/event?id=1&name=汪峰上热搜了")).
                andExpect(status().isOk());
        mockMvc.perform(get("/rs/1")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName",is("汪峰上热搜了"))).
                andExpect(jsonPath("$.keyWord",is("1")));
    }

    @Test
    void should_modify_Rs_Event_No_Name() throws Exception {
        mockMvc.perform(put("/rs/event?id=1&keyword=2")).
                andExpect(status().isOk());
        mockMvc.perform(get("/rs/1")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName",is("第一条事件"))).
                andExpect(jsonPath("$.keyWord",is("2")));
    }

    @Test
    void should_modify_Rs_Event_With_Both_Param() throws Exception {
        mockMvc.perform(put("/rs/event?id=1&name=汪峰上热搜了&keyword=2")).
                andExpect(status().isOk());
        mockMvc.perform(get("/rs/1")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.eventName",is("汪峰上热搜了"))).
                andExpect(jsonPath("$.keyWord",is("2")));
    }

    @Test
    void should_delete_Rs_Event() throws Exception {
        mockMvc.perform(delete("/rs/event?id=1")).
                andExpect(status().isOk());
        mockMvc.perform(get("/rs/list")).
                andExpect(status().isOk()).
                andExpect(jsonPath("$",hasSize(2))).
                andExpect(jsonPath("$[0].eventName", is("第二条事件"))).
                andExpect(jsonPath("$[0].keyword", is("2"))).
                andExpect(jsonPath("$[1].eventName", is("第三条事件"))).
                andExpect(jsonPath("$[1].keyword", is("3")));
    }

}
