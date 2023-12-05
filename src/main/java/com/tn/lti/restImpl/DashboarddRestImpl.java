package com.tn.lti.restImpl;

import com.tn.lti.rest.DashboardRest;
import com.tn.lti.service.DashboardService;
import com.tn.lti.rest.DashboardRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DashboarddRestImpl implements DashboardRest {

    @Autowired
    DashboardService dashboardService;

    @Override
    public ResponseEntity<Map<String, Object>> getCount() {

            return dashboardService.getCount();

    }
}
