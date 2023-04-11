package server.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import server.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    AdminService adminService;

    /**
     * checks if the password is correct
     * @param password the password to be checked
     * @return a response (bad request or ok)
     */
    @GetMapping("/password/{password}")
    public ResponseEntity<Boolean> checkPassword(@PathVariable("password") String password) {

        if(!adminService.checkPassword(password)) {
            return ResponseEntity.ok(false);
        }
        return ResponseEntity.ok(true);

    }
}