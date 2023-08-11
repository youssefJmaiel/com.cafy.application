package com.cafy.application.serviceImpl;

import com.cafy.application.dao.BillDao;
import com.cafy.application.dao.CategoryDao;
import com.cafy.application.dao.ProductDao;
import com.cafy.application.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;

    @Autowired
    BillDao billDao;
    @Override
    public ResponseEntity<Map<String, Object>> getCount() {
        Map<String,Object> map = new HashMap<>();
        map.put("Category ",categoryDao.count());
        map.put("Product ",productDao.count());
        map.put("Bill ",billDao.count());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }


}
