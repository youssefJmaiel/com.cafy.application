package com.cafy.application.serviceImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.dao.UserDao;
import com.cafy.application.dao.specification.UserSpecification;
import com.cafy.application.entity.User;
import com.cafy.application.jwt.CustomerUserDetailsService;
import com.cafy.application.jwt.JwtFilter;
import com.cafy.application.jwt.JwtUtils;
import com.cafy.application.request.UserConverter;
import com.cafy.application.request.UserDto;
import com.cafy.application.request.UserRequest;
import com.cafy.application.request.UserRequestConverter;
import com.cafy.application.service.UserService;
import com.cafy.application.utils.CofeUtils;
import com.cafy.application.wrapper.EmailUtils;
import com.cafy.application.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
//import org.springframework.hateoas.PagedResources;
//import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
   @Autowired
    UserDao userDao;

   @Autowired
   AuthenticationManager authenticationManager;
    @Autowired
    CustomerUserDetailsService customerUserDetailsService;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    EmailUtils emailUtils;

    @Autowired
    UserSpecification userSpecification;
    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signup {}",requestMap);
        try {

            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return  CofeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CofeUtils.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);

                }
            } else {
                return CofeUtils.getResponseEntity(CofeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"),requestMap.get("password"))
            );
            if (auth.isAuthenticated()){
                if (customerUserDetailsService.getUserDetails().getStatus().equalsIgnoreCase("true")){
                   return new ResponseEntity<String>("{\"token\":\""+
                           jwtUtils.generateToken(customerUserDetailsService.getUserDetails().getEmail(),
                                   customerUserDetailsService.getUserDetails().getRole())+ "\"}",
                    HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",HttpStatus.BAD_REQUEST);
                }

            }

        }catch (Exception ex){
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);

            }else {
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new  ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
           Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
           if (!optional.isEmpty()){
               userDao.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get("id")));
               sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),userDao.getAllAdmin());
               return  CofeUtils.getResponseEntity("User Status Updated Successfully",HttpStatus.OK);
           }else {
            return  CofeUtils.getResponseEntity("User id doesn't not exist",HttpStatus.OK);
           }
            }else {
                return CofeUtils.getResponseEntity(CofeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> checkToken() {
       return CofeUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
         User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
         if (!userObj.equals(null)){
             if (userObj.getPassword().equals(requestMap.get("oldPassword"))){
                 userObj.setPassword(requestMap.get("newPassword"));
                 userDao.save(userObj);
                 return CofeUtils.getResponseEntity("Password Updated Successfully",HttpStatus.OK);
             }
             return CofeUtils.getResponseEntity("Incorrect Old Password",HttpStatus.BAD_REQUEST);
         }
            return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user = userDao.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(),"Set Your Credentials",user.getPassword());
            return CofeUtils.getResponseEntity("Check your mail for Credentials",HttpStatus.OK);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addNewUser(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (validateUserMap(requestMap,false)){
                    userDao.save(getUserFromMap(requestMap));
                    sendMailUserAdded(requestMap.get("name"),requestMap.get("name"),userDao.getAllAdmin());
                    return CofeUtils.getResponseEntity("User Added Successfully",HttpStatus.OK);
                }
                return CofeUtils.getResponseEntity(CofeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else {
                return CofeUtils.getResponseEntity(CofeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteUser(Integer id) {
        try {
            if (jwtFilter.isAdmin()){
                Optional optional = userDao.findById(id);
                if (!optional.isEmpty()){
                    User user = userDao.findByIds(id);
                    sendMailDeleteUser(user.getName(),userDao.getAllAdmin());
                    userDao.deleteById(id);

                    return CofeUtils.getResponseEntity("User Deleted successfully",HttpStatus.OK);
                }else {
                    return CofeUtils.getResponseEntity("User id does not exist",HttpStatus.OK);
                }

            }else {
                return CofeUtils.getResponseEntity(CofeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @Override
    public UserDto create(UserRequest ownerRequest) {
        User owner = UserRequestConverter.newInstance().convert(ownerRequest);
        User savedOwner = userDao.save(owner);
        return UserConverter.newInstance().convert(savedOwner);
    }

    @Override
    public List<UserDto> getAll() {
       List<User> owners = userDao.findAll();
        List<UserDto> ownerDtos = owners.stream().map(owner -> UserConverter.newInstance().convert(owner)).collect(Collectors.toList());
        return ownerDtos;
    }



//    @Override
//    public ResponseEntity<List<UserWrapper>> getUsersFilterApi(String name, String email, String contactNumber, Pageable pageable) {
//        try {
//            if (jwtFilter.isAdmin()){
//                return new ResponseEntity<>(userSpecification.perSearch(name,email,contactNumber),HttpStatus.OK);
//
//                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.OK);
//
//            }else {
//                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
//            }
//
//        }catch (Exception ex){
//            ex.printStackTrace();
//        }
//        return new  ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status!=null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Approved","USER: "+user+"\n is approved by \nADMIN: "+jwtFilter.getCurrentUser(),allAdmin);

        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Account Disabled","USER: "+user+"\n is disabled by \nADMIN: "+jwtFilter.getCurrentUser(),allAdmin);

        }
    }

    private void sendMailUserAdded(String name,String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (name!=null ){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"New User","USER: "+user+"\n is Added by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);

        }else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Delete User","USER: "+user+"\n is deleted by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);

        }
    }

    private void sendMailDeleteUser(String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (user!=null ){
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(),"Delete User","USER : "+user+"\n is deleted by \nADMIN:-"+jwtFilter.getCurrentUser(),allAdmin);
        }
    }

    private boolean validateUserMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;

            }else if (!validateId){
                return true;
            }
        }
        return false;

    }

    private boolean validateSignUpMap(Map<String,String> requestMap){
      if(  requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
                && requestMap.containsKey("email") && requestMap.containsKey("password"))

          return true;

      return false;
    }



    private User getUserFromMap(Map<String,String> requestMap){
        User user = new  User();
        user.setName(requestMap.get("name"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setRole("user");
        user.setStatus("false");

        return user;
    }




}
