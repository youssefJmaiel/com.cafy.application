package com.cafy.application.rest;

import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.serviceImpl.UserServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(value = {SpringExtension.class})
@SpringBootTest
@Slf4j
class UserRestTest{

    private static final String API_BASE_OWNERS = "/user/v1/owners";

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    protected MockMvc mockMvc;

    @Test
    void create() throws Exception {

        doReturn(getUserDto()).when(userService).create(any(UserRequest.class));
        mockMvc.perform(post(API_BASE_OWNERS)
                        .content(new ObjectMapper().writeValueAsString(UserRequest.builder().build()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

    }

    private UserDto getUserDto() {
        return UserDto.builder()
                .email("email@gmail.com")
                .contactNumber("2558855688")
                .name("john")
                .role("user")
                .password("1234566")
                .status("false")
                .build();
    }

    private UserRequest getOwner() {
        return UserRequest.builder()
                .email("email@gmail.com")
                .contactNumber("2558855688")
                .name("john")
                .role("user")
                .password("1234566")
                .status("false")
                .build();
    }
//    @Test
//    void create() throws Exception {
//        final OwnerRequest owner = getOwner();
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.registerModule(new JavaTimeModule());
//        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        String petJsonString = mapper.writeValueAsString(owner);
//        mockMvc.perform(post(API_BASE_OWNERS)
//                        .content(petJsonString)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isCreated())
//                .andDo(print());
//    }
}