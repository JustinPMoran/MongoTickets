package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Simple Hello World Controller to display the string returned
 *
 * @author Pablo Leguizamo
 */

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to V2 of the backend";
    }
}
