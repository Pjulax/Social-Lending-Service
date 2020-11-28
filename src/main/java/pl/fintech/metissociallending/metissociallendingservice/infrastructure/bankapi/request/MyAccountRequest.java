package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;

@Getter
@Builder
public class MyAccountRequest implements BankService.Command.WithdrawFromAccount, BankService.Command.DepositToAccount {
    private final String accountNumber;
    private final Double amount;
}
