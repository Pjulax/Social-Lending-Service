package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.*;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public User createUser(@RequestBody UserDTO userDTO){
        return userService.createUser(userDTO);
    }

    @PostMapping("/signin")
    public String login(@RequestBody UserLoginDTO userDTO){
        return userService.login(userDTO);
    }

    @GetMapping("/me")
    public UserDetailsDTO getUserDetails(){
        return userService.getUserDetails();
    }

    @PostMapping("/bank/deposit")
    public void deposit(@RequestParam Double amount) {
        userService.depositToBank(() -> amount);
    }

    @PostMapping("/bank/withdraw")
    public void withdraw(@RequestParam Double amount) {
        userService.withdrawFromBank(() -> amount);
    }
}
