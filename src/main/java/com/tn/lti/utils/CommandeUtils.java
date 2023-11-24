package com.tn.lti.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class CommandeUtils {

    private CommandeUtils(){

    }

    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpStatus){
        return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}", httpStatus);

    }
}
