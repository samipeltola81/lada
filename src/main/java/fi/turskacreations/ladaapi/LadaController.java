package fi.turskacreations.ladaapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LadaController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PostMapping("/login")
    public void login(Principal principal) {
        // not accessed, mainly for swagger-documentation purposes
    }

}
