package com.tn.lti.service;

import com.tn.lti.POJO.Category;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

public interface CategoryService {

    public ResponseEntity<String> addCategory(Map<String,String> requestmap);

    ResponseEntity<List<Category>> getAllCategory(String filterValue);

    public ResponseEntity<String> updateCategory(Map<String, String> requestmap);
}
