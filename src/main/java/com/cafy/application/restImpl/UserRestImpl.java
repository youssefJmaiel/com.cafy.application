package com.cafy.application.restImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.dao.UserDao;
import com.cafy.application.dao.specification.UserSpecification;
import com.cafy.application.entity.User;
import com.cafy.application.request.UserConverter;
import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.rest.UserRest;
import com.cafy.application.service.UserService;
import com.cafy.application.utils.CofeUtils;
import com.cafy.application.wrapper.UserWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UserRestImpl implements UserRest {


    @Autowired
    private UserService userService;
    @Autowired
    private UserDao userDao;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        try {
            return userService.signUp(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        try {
            return userService.login(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            return userService.getAllUser();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            return userService.update(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        try {
            return userService.checkToken();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            return userService.changePassword(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            return userService.forgotPassword(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addNewUser(Map<String, String> requestMap) {
        try {
            return userService.addNewUser(requestMap);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }



//    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE, path = "/v1/createUser")
//    public ResponseEntity<UserDto> create( @RequestBody UserRequest ownerRequest) {
//        log.info("Create new owner");
//        UserDto ownerDto = userService.create(ownerRequest);
//        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/owners/{id}").build().expand(ownerDto.getId()).toUri();
//        return ResponseEntity.created(location).body(ownerDto);
//    }
    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            return userService.deleteUser(id);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public UserDto create(UserRequest ownerRequest) {
        UserDto ownerDto = userService.create(ownerRequest);
        final URI location = ServletUriComponentsBuilder.fromCurrentServletMapping().path("/api/v1/owners/{id}").build().expand(ownerDto.getId()).toUri();
        return ownerDto;
    }



    @Override
    public List<UserDto> getAll() {
        List<User> owners = userDao.findAll();
        List<UserDto> ownerDtos = owners.stream().map(owner -> UserConverter.newInstance().convert(owner)).collect(Collectors.toList());
        return ownerDtos;
    }

    @Override
    public Page<UserDto> getUsers(String name, String contactNumber, String email, Pageable pageable, PagedResourcesAssembler<UserDto> assembler) {
        Specification<User> userSpecification = Specification.where(UserSpecification.perSearch(name, email, contactNumber));
        Page<User> all = userDao.findAll(userSpecification, pageable);
        return all.map(UserConverter.newInstance()::convert);
    }

//    @Override
//    public ResponseEntity<PagedResources<Resource<UserDto>>> getCooptations(String name, String contactNumber, String email, Pageable pageable, PagedResourcesAssembler<UserDto> assembler) {
//        Specification<User> userSpecification = Specification.where(UserSpecification.perSearch( name, email, contactNumber));
//        Page<User> all = userDao.findAll(userSpecification, pageable);
//        return  all.map(UserConverter.newInstance()::convert);
//    }




//    @Override
//    public Page<User> getUsersFilterApi(String name, String email, String contactNumber, Pageable pageable) {
//        Specification<com.cafy.application.entity.User> userSpecification = Specification.where(UserSpecification.perSearch(name, email, contactNumber));
//        org.springframework.data.domain.Page<com.cafy.application.entity.User> all = userDao.findAll(userSpecification, pageable);
//        Page<User> ResponseEntity;
//        return ResponseEntity<P>;
//    }


//    @Override
//    public ResponseEntity<Pageable<Resource<User>>> getUsers(String name, String email, String contactNumber, Pageable pageable) {
//        Specification<com.cafy.application.entity.User> userSpecification = Specification.where(UserSpecification.perSearch(name, email, contactNumber));
//        org.springframework.data.domain.Page<com.cafy.application.entity.User> all = userDao.findAll(userSpecification, pageable);
//        return (ResponseEntity<Pageable<Resource<User>>>) all;
//    }

}
