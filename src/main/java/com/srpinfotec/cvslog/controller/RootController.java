package com.srpinfotec.cvslog.controller;

import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<?> rootUrl() {
        return ResponseEntity.ok(new RootResponse());
    }

    @Getter
    private static class RootResponse{
        private final String whoAmI = "CVS Commit Log Server";
    }
}
