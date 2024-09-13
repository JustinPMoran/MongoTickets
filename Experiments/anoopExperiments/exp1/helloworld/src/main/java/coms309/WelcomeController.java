package coms309;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
class WelcomeController {

    @GetMapping("/")
    public String welcome() {
        return "Hello, welcome to Anoops world ";
    }

    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello, welcome to Anoops world: " + name;
    }
}
