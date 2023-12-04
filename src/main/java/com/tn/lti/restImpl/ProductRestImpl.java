package com.tn.lti.restImpl;

import com.tn.lti.constents.CommandConstants;
import com.tn.lti.rest.ProductRest;
import com.tn.lti.serviceImpl.ProductServiceImpl;
import com.tn.lti.utils.CommandeUtils;
import com.tn.lti.wrapper.ProductWrapper;
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
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductServiceImpl productService;


    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestmap) {
        try{
            log.info("work well");
            return productService.addNewProduct(requestmap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try{
            return productService.getAllProduct();
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
        try{
            log.info("work");
            return productService.updateProduct(requestMap);
        }catch (Exception e ){
            e.printStackTrace();
        }

    return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try{
          return   productService.deleteProduct(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> requestMap) {
        try{
          return   productService.updateStatus(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getProductByCategory(Integer id) {
        try {
            return productService.getProductByCategory(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ProductWrapper> getProductById(Integer id) {
        try{
            return productService.getProductById(id);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ProductWrapper(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
