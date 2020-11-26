package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements UserService.Command.CreateUser{
    private String username;
    private String password;
    private String name;
    private String cardNumber;
    private String expiry;
    private String cvc;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getCardNumber() {
        return cardNumber;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getExpiry() {
        return expiry;
    }

    @Override
    public String getCvc() {
        return cvc;
    }

}
