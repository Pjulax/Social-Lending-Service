package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private String name;
    private String number;
    private List<TransactionDTO> transactions;
    private Double accountBalance;
}