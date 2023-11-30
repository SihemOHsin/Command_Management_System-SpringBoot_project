package com.tn.lti.restImpl;

import com.tn.lti.constents.CommandConstants;
import com.tn.lti.POJO.Category;
import com.tn.lti.rest.CategoryRest;
import com.tn.lti.serviceImpl.CategoryServiceImpl;
import com.tn.lti.utils.CommandeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class CategoryRestImpl implements CategoryRest {

    @Autowired
    CategoryServiceImpl categoryService;

    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestmap) {
        try{
            log.info("in the add category function ");

            return categoryService.addCategory(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {

        log.info(filterValue);

        try {
         return  categoryService.getAllCategory(filterValue);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestmap) {
        try {
         return categoryService.updateCategory(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
