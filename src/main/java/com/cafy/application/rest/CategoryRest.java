package com.cafy.application.rest;

import com.cafy.application.entity.Category;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/category")
public interface CategoryRest {

    @PostMapping("/add")
    ResponseEntity<String> addNewCategory(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/getAllCategory")
    ResponseEntity<List<Category>> getAllCategory(@RequestParam(required = false) String filterValue);

    @PostMapping("/updateCategory")
    ResponseEntity<String> updateCategory(@RequestBody(required = true) Map<String,String> requestMap);
}
