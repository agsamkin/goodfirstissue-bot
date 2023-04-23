package agsamkin.code.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @GetMapping(path = "/ping")
    public ResponseEntity<?> ping() {
        return ResponseEntity.ok().build();
    }
}
