package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;

@Getter
@Builder
public class MyAccountRequestEntity implements BankService.Command.WithdrawFromAccount, BankService.Command.DepositToAccount {
    private final String accountNumber;
    private final Double amount;
}
