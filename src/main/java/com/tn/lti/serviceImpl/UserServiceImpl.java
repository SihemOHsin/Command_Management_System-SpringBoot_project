package com.tn.lti.serviceImpl;

import com.google.common.base.Strings;
import com.tn.lti.JWT.CustomerUsersDetailsService;
import com.tn.lti.JWT.JwtFilter;
import com.tn.lti.JWT.JwtUtil;
import com.tn.lti.POJO.User;
import com.tn.lti.dao.UserDao;
import com.tn.lti.service.UserService;
import com.tn.lti.utils.CommandeUtils;
import com.tn.lti.constents.CommandConstants;
import com.tn.lti.utils.EmailUtils;
import com.tn.lti.wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager  authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signu {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userDao.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromMap(requestMap));
                    return CommandeUtils.getResponseEntity("Successfully registered", HttpStatus.OK);

                } else {
                    return CommandeUtils.getResponseEntity("Email already exists.", HttpStatus.BAD_REQUEST);
                }

            } else {
                return CommandeUtils.getResponseEntity(CommandConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignUpMap(Map<String, String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("contactNumber") &&
                requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        return false;
    }

    private User getUserFromMap(Map<String, String> requestMap) {
        User user = new User();
        user.setName(requestMap.get("name"));
        user.setContactNumber(requestMap.get("contactNumber"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;

    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                  new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password"))
            );
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus()
                        .equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                    HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}", HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Crediantials."+"\"}", HttpStatus.BAD_REQUEST);

    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(userDao.getAllUser(),HttpStatus.OK);

            } else{
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try{
            if(jwtFilter.isAdmin()){
               Optional<User> optional = userDao.findById(Integer.parseInt(requestMap.get("id")));
               if (!optional.isEmpty()){
                   userDao.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                   sendMailToAllAdmin(requestMap.get("status"), optional.get().getEmail(), userDao.getAllAdmin());
                   return CommandeUtils.getResponseEntity("User Status Updated Successfully",HttpStatus.OK);
               }else{
                   return CommandeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
               }

            }else{
                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex) {
            ex.printStackTrace();
    }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
}


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if(status!= null && status.equalsIgnoreCase("true")){
            emailUtils.sendSimpleMessage (jwtFilter.getCurrentUser(), "Account Approved", "USER:- "+user + "\n is approved by \nADMIN:-" + jwtFilter.getCurrentUser() , allAdmin);

        } else{
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:-" + user + " \n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser() , allAdmin);

        }
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return CommandeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changPassword(Map<String, String> requestMap) {
        try{
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if(!userObj.equals(null) ) {
                if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                    userObj.setPassword(requestMap.get("newPassword"));
                    userDao.save(userObj);
                    return CommandeUtils.getResponseEntity("Password updated successfully.", HttpStatus.OK);
                }
                return CommandeUtils.getResponseEntity("Incorrect Old Password",HttpStatus.BAD_REQUEST);
            }
                return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = userDao.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(),"Credentials by command management system",user.getPassword());
            return CommandeUtils.getResponseEntity("Check your mail Credentials.", HttpStatus.OK);
        }catch(Exception ex){
            ex.printStackTrace();
        }
            return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

