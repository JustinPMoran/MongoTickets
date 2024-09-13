package coms309;

import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
class WelcomeController {

    // Default get endpoint
    @GetMapping("/")
    public String welcome() {
        return "Hello and welcome to COMS 309";
    }

    // Path parameters are used to request specific resources
    @GetMapping("/{name}")
    public String welcome(@PathVariable String name) {
        return "Hello and welcome to COMS 309: " + name;
    }

    // URL Query Parameters are used to filter search / endpoint data
    @GetMapping("/greet")
    public String greet(@RequestParam("daytime") String daytime) {
        return Objects.equals(daytime, "morning") ? "Good Morning" : "Good Evening";
    }

    // Data is sent in the body when there's more data to be sent
    // MyDate object is JSON with string and int datatypes
    @GetMapping("/date")
    public String formatDate(@RequestBody() MyDate date) {
        date.convertFormat();
        return date.toString();
    }



}
