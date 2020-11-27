package pl.fintech.metissociallending.metissociallendingservice.domain.bank.requestEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MyAccountRequestBody {
    private String accountNumber;
    private Double amount;
}
