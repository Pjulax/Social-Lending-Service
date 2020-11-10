package pl.fintech.metissociallending.metissociallendingservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public User createUser(@RequestBody UserDTO userDTO){
        return userService.createUser(new UserService.Command.CreateUser() {

            @Override
            public String getUsername() {
                return userDTO.getUsername();
            }

            @Override
            public String getPassword() {
                return userDTO.getPassword();
            }

        });
    }

    @PostMapping("/signin")
    public String login(@RequestBody UserDTO userDTO){
        return userService.login(new UserService.Command.Login() {
            @Override
            public String getUsername() {
                return userDTO.getUsername();
            }

            @Override
            public String getPassword() {
                return userDTO.getPassword();
            }
        });
    }

}
