package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class UserDetailsDTO {
    String name;
    String account;
    Double balance;
}
