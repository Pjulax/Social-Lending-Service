package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;

@Getter
@Builder
public class TransactionRequestEntity implements BankService.Command.Transfer {
    private final String sourceAccountNumber;
    private final String targetAccountNumber;
    private final Double amount;
}
