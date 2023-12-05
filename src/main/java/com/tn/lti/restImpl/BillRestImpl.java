package com.tn.lti.restImpl;

import com.tn.lti.constents.CommandConstants;
import com.tn.lti.POJO.Bill;
import com.tn.lti.rest.BillRest;
import com.tn.lti.serviceImpl.BillserviceImpl;
import com.tn.lti.utils.CommandeUtils;
import com.tn.lti.rest.BillRest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
@Slf4j
@RestController
public class BillRestImpl implements BillRest {

    @Autowired
    BillserviceImpl billservice;
    /*private BillserviceImpl billservice;

    public BillRestImpl( BillserviceImpl billservice) {
        this.billservice=billservice;
    }*/

    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {

        try{
            return billservice.generateReport(requestMap);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try{
               return billservice.getBills();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<byte[]> getpdf(Map<String, Object> requestMap) {
        log.info("inside get pdf reqst : {}",requestMap);
        try {
            return billservice.getPdf(requestMap);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
        return  billservice.deleteBill(id);
        }catch (Exception e){
            e.printStackTrace();
        }

        return CommandeUtils.getResponseEntity(CommandConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
