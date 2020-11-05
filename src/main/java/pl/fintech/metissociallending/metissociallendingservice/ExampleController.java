package pl.fintech.metissociallending.metissociallendingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class ExampleController {

    @Value("${hello.value:value}")
    private String version;

    @GetMapping
    public String example() {

        return "Hello " + version + " FinTech!";
    }

}
