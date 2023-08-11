package com.cafy.application.serviceImpl;

import com.cafy.application.constents.CofeConstants;
import com.cafy.application.dao.CategoryDao;
import com.cafy.application.entity.Category;
import com.cafy.application.jwt.JwtFilter;
import com.cafy.application.service.CategoryService;
import com.cafy.application.utils.CofeUtils;
import com.cafy.application.view.iCategoryView;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;
    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (valideCategoryMap(requestMap,false)){
                    categoryDao.save(getCategoryFromMap(requestMap,false));
                    return CofeUtils.getResponseEntity("Category Added Successfully",HttpStatus.OK);
                }

            }else {
                return CofeUtils.getResponseEntity(CofeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }


        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try {
            if (!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){

                return new ResponseEntity<List<Category>>(categoryDao.getAllCategory(),HttpStatus.OK);
            }
                return new ResponseEntity<>(categoryDao.findAll(),HttpStatus.OK);

        }catch (Exception ex){
         ex.printStackTrace();
        }
        return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()){
                if (valideCategoryMap(requestMap,true)){
                   Optional optional = categoryDao.findById(Integer.parseInt(requestMap.get("id")));
                   if (!optional.isEmpty()){
                       categoryDao.save(getCategoryFromMap(requestMap,true));
                       return CofeUtils.getResponseEntity("Category Updated Successfully",HttpStatus.OK);

                   }else {
                       return CofeUtils.getResponseEntity("Category id doesn't exist",HttpStatus.OK);
                   }
                }
                return CofeUtils.getResponseEntity(CofeConstants.INVALID_DATA,HttpStatus.BAD_REQUEST);

            }else {
                return CofeUtils.getResponseEntity(CofeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CofeUtils.getResponseEntity(CofeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean valideCategoryMap(Map<String, String> requestMap, boolean validateId) {
        if (requestMap.containsKey("name")){
            if (requestMap.containsKey("id") && validateId){
                return true;
            }else if(!validateId) {
                return true;
            }
        }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap,Boolean isAdd){
        Category category = new Category();
        if (isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }



}
