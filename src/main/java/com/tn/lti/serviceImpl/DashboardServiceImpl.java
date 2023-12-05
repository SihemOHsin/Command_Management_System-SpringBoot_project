package com.tn.lti.serviceImpl;

import com.tn.lti.dao.BillDao;
import com.tn.lti.dao.CategoryDao;
import com.tn.lti.dao.ProductDao;
import com.tn.lti.rest.DashboardRest;
import com.tn.lti.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    ProductDao productDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    BillDao billDao;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map=new HashMap<>();
        map.put("category",categoryDao.count());
        map.put("product",productDao.count());
        map.put("bill",billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
            }
        }
