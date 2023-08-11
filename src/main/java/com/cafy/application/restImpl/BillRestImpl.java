package com.cafy.application.restImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.entity.Bill;
import com.cafy.application.rest.BillRest;
import com.cafy.application.service.BillService;
import com.cafy.application.utils.CofeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class BillRestImpl implements BillRest {
    @Autowired
    BillService billService;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> requestMap) {
        try {
            return billService.generateReport(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            return billService.getBills();

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> requestMap) {
        try {
            return billService.getPdf(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            return billService.deleteBill(id);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
