package agsamkin.code.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {
    @RequestMapping(path = "/ping")
    public String ping() {
        return "OK";
    }
}
