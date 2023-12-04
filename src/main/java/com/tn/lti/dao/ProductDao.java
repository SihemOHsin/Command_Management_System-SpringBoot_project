package com.tn.lti.dao;

import com.tn.lti.POJO.Product;
import com.tn.lti.wrapper.ProductWrapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

public interface ProductDao extends JpaRepository<Product,Integer> {

    List<ProductWrapper> getAllProduct();

    @Transactional
    @Modifying
    Integer updateStatus(@Param("status") String status,@Param("id") Integer id);

    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);

    ProductWrapper getProductById(@Param("id") Integer id);
}
