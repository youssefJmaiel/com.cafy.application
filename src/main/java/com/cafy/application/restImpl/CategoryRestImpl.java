package com.cafy.application.restImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.entity.Category;
import com.cafy.application.rest.CategoryRest;
import com.cafy.application.service.CategoryService;
import com.cafy.application.service.UserService;
import com.cafy.application.utils.CofeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class CategoryRestImpl implements CategoryRest {
    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            return categoryService.addNewCategory(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            return categoryService.getAllCategory(filterValue);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            return categoryService.updateCategory(requestMap);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
