package gr.ckaraiskos.candlefactory.candle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Routes the root path to the bundled React app so users do not hit the
 * Whitelabel error page when visiting "/".
 */
@Controller
public class RootController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }
}
