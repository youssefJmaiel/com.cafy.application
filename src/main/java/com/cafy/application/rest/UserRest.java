package com.cafy.application.rest;

import com.cafy.application.entity.User;
import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.wrapper.UserWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.hateoas.Resource;
//import org.springframework.hateoas.PagedResources;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequestMapping(path = "/user")

public interface UserRest {

    @CrossOrigin(origins = "http://localhost:8082",methods = {RequestMethod.GET,RequestMethod.POST})
    @PostMapping(path = "/signup")
    public ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping("/update")
    public ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);
    @GetMapping("/checkToken")
    public ResponseEntity<String> checkToken();

    @PostMapping("/changePassword")
    public ResponseEntity<String> changePassword(@RequestBody Map<String,String> requestMap);

    @PostMapping("/forgotPassword")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String,String> requestMap);

    @PostMapping("/addUser")
    ResponseEntity<String> addNewUser(@RequestBody Map<String,String> requestMap);

    @PostMapping("/deleteUser/{id}")
    ResponseEntity<String> deleteUser(@PathVariable Integer id);

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, path = "/v1/createOwners")
    public UserDto create( @RequestBody UserRequest ownerRequest);

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/owners")
    public List<UserDto> getAll();

    @GetMapping(produces = APPLICATION_JSON_VALUE, path = "/v1/users/searchFullText")
    public Page<UserDto> getUsers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "contactNumber", required = false) String contactNumber,
            @RequestParam(value = "email", required = false) String email,
            Pageable pageable, PagedResourcesAssembler<UserDto> assembler );


}
