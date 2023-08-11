package com.cafy.application.rest;

import com.cafy.application.entity.Product;
import com.cafy.application.wrapper.ProductWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/product")
public interface ProductRest {

    @PostMapping("/addProduct")
    ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestMap);

    @GetMapping("/getAllProducts")
    ResponseEntity<List<ProductWrapper>> getAllProducts();

    @PostMapping("/updateProduct")
    ResponseEntity<String> updateProduct(@RequestBody Map<String,String> requestMap);

    @PostMapping("/deleteProduct/{id}")
    ResponseEntity<String> deleteProduct(@PathVariable Integer id);

    @PostMapping("/updateStatus")
    ResponseEntity<String> updateStatus(@RequestBody Map<String,String> requestMap);

    @GetMapping("/getByCategory/{id}")
    ResponseEntity<List<ProductWrapper>> getByCategory(@PathVariable Integer id);

    @GetMapping("/getProductById/{id}")
    ResponseEntity<ProductWrapper> getProductById(@PathVariable Integer id);
}
