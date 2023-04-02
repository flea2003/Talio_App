package server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class SomeController {

    /**
     * sets the body of the url page to "Talio app-74"
     * used for checking if the server is for this app
     * @return the body
     */
    @GetMapping("/")
    @ResponseBody
    public String index() {
        return "Talio app-74";
    }
}