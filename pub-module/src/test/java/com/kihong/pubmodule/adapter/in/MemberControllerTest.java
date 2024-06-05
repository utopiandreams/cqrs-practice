package com.kihong.pubmodule.adapter.in;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kihong.pubmodule.domain.EmployeeCreate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup(){

    }

    @Test
    void integrateTest_저장_요청_후_읽기_성공() throws Exception {
        // given
        EmployeeCreate employeeCreate = EmployeeCreate.builder()
                .name("김기홍")
                .department("FT2-3")
                .build();

        MvcResult mvcResult = mockMvc.perform(post("/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreate)))
                .andExpect(status().is(200))
                .andReturn();

        String id = mvcResult.getResponse().getContentAsString();

        mockMvc.perform(get("/employee/" + id))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("김기홍"))
                .andExpect(jsonPath("$.department").value("FT2-3"))
                .andDo(MockMvcResultHandlers.print());
    }



}