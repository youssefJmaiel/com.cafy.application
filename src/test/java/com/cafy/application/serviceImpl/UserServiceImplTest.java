package com.cafy.application.serviceImpl;

import com.cafy.application.CofeManagmentApplication;
import com.cafy.application.entity.User;
import com.cafy.application.request.UserConverter;
import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.request.UserRequestConverter;
import com.cafy.application.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = CofeManagmentApplication.class)
@Transactional
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void signUp() {
    }

    @Test
    void login() {
    }

    @Test
    void getAllUser() {
        List<UserDto> userDtos = userService.getAll();
        org.junit.jupiter.api.Assertions.assertFalse(userDtos.isEmpty());
        userDtos.forEach(System.out::println);
    }

    @Test
    void update() {
    }

    @Test
    void checkToken() {
    }

    @Test
    void changePassword() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void addNewUser() throws SQLException, IOException  {
        UserRequest userRequest = UserRequest.builder()
                .email("email@gmail.com")
                .contactNumber("2558855688")
                .name("john")
                .role("user")
                .password("1234566")
                .status("false")
                .build();
        ResponseEntity<UserDto> saveUser = userService.create(userRequest);
        log.info("user has been created");
        Assertions.assertNotNull(saveUser);
        System.out.println(saveUser);
    }



    @Test
    void deleteUser() {
    }
}