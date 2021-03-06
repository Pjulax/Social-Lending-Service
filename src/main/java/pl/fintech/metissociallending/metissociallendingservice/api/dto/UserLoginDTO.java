package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

@Getter
@Setter
@NoArgsConstructor
public class UserLoginDTO implements UserService.Query.Login {
    private String username;
    private String password;
}
