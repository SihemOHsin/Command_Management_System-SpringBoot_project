package com.tn.lti.dao;

import com.tn.lti.POJO.Category;
import org.apache.coyote.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryDao extends JpaRepository<Category,Integer> {


    public List<Category> getAllCategory();
}
