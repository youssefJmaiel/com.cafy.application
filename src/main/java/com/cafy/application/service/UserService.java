package com.cafy.application.service;

import com.cafy.application.entity.User;
import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.wrapper.UserWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public interface UserService {
    @CrossOrigin(origins = "http://localhost:4200")
    ResponseEntity<String> signUp(@RequestBody Map<String,String> requestMap);


    ResponseEntity<String> login(Map<String, String> requestMap);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String> update(Map<String, String> requestMap);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(Map<String, String> requestMap);

    ResponseEntity<String> forgotPassword(Map<String, String> requestMap);

    ResponseEntity<String> addNewUser(Map<String, String> requestMap);

    ResponseEntity<String> deleteUser(Integer id);

    public UserDto create( @RequestBody UserRequest userRequest);

    public List<UserDto> getAll();




}
