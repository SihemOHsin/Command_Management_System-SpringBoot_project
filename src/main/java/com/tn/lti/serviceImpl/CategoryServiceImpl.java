package com.tn.lti.serviceImpl;

import com.google.common.base.Strings;
import com.tn.lti.JWT.JwtFilter;
import com.tn.lti.constents.CommandConstants;
import com.tn.lti.dao.CategoryDao;
import com.tn.lti.POJO.Category;
import com.tn.lti.service.CategoryService;
import com.tn.lti.utils.CommandeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    CategoryDao categoryDao;
    @Override
    public ResponseEntity<String> addCategory(Map<String, String> requestmap) {
        try{
       if(jwtFilter.isAdmin()){
           if(validateCategoryMap(requestmap,false) ){
               categoryDao.save(getCategoryFromMap(requestmap,false));
               return CommandeUtils.getResponseEntity("Category added Successfully ",HttpStatus.OK);
           }
           //else{
               //return CommandeUtils.getResponseEntity("Invalid Request please try again",HttpStatus.BAD_REQUEST);
           //}
           }
           else{
               return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
           }

        }catch(Exception e){
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            log.info(filterValue);
                log.info("in getallcategory");

            if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                log.info("inside the if");
                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
            }
            return new ResponseEntity<List<Category>>(categoryDao.findAll(),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestmap) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateCategoryMap(requestmap,true)){
                    Optional optionalCategory=categoryDao.findById(Integer.parseInt(requestmap.get("id")));
                    if(!optionalCategory.isEmpty()){
                        categoryDao.save(getCategoryFromMap(requestmap,true));
                        return CommandeUtils.getResponseEntity("Category Updated Successfully ",HttpStatus.OK);
                    }else {
                        return CommandeUtils.getResponseEntity("Category id does not exist in the data base ",HttpStatus.OK);
                    }
                }
                return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.BAD_REQUEST);
            }else {
                return CommandeUtils.getResponseEntity(CommandConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestmap, boolean validateId) {
        if(requestmap.containsKey("name")){
            if(requestmap.containsKey("id") && validateId){
                return true;
            }else if(!validateId){
                return true;
            }
        }
        return false;
    }
    private Category getCategoryFromMap(Map<String,String> requestMap ,boolean isAdd){

        Category category=new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
            category.setName(requestMap.get("name"));
        }else {
            category.setName(requestMap.get("name"));
        }
        return category;

    }
}
