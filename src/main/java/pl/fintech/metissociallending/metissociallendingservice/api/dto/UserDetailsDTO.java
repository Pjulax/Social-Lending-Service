package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsDTO {
    String username;
    String account;
    String cardNumber;
    String name;
    String cvc;
    String expiry;
    Double balance;
    List<TransactionDTO> transactions;
}
