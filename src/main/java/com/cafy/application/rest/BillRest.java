package com.cafy.application.rest;

import com.cafy.application.entity.Bill;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/bill")
public interface BillRest {

    @PostMapping("/generateReport")
    ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestMap);

    @GetMapping("/getBills")
    ResponseEntity<List<Bill>> getBills();

    @PostMapping("/getPdf")
    ResponseEntity<byte[]> getPdf(@RequestBody Map<String,Object> requestMap);

    @PostMapping("/deleteBill/{id}")
    ResponseEntity<String> deleteBill(@PathVariable Integer id);
}
