package com.example.demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/live")
public class LiveController {
    
    @GetMapping
    public ResponseEntity<Object> live(){
        return ResponseEntity.status(200).body("");
    }
}
