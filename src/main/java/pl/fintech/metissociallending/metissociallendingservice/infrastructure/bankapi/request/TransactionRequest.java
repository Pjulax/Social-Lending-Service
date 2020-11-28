package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;

@Getter
@Builder
public class TransactionRequest implements BankService.Command.Transfer {
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final Double amount;
}
