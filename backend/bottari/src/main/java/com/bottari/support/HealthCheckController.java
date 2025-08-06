package com.bottari.support;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController implements HealthCheckApiDocs {

    @GetMapping("/health")
    @Override
    public ResponseEntity<Void> healthCheck(){
        return ResponseEntity.ok().build();
    }
}
