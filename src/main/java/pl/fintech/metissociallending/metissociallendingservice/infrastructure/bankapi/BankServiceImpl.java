package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity.AccountNameRequestEntity;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity.MyAccountRequestEntity;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity.TransactionRequestEntity;

/**
 * Service making operations on bank api provided by FinTech Challenge hosts.
 * If we had more time we would handle exceptions, messages, codes more fancy,
 * now handling is raw but working properly, because all is happening
 * in exception handler.
 */
@RequiredArgsConstructor
@Service
public class BankServiceImpl implements BankService {

    private final BankClient bankClient;
    private final String basicAuthHeader;

    @Override
    public String createBankAccount(Command.CreateBankAccount createBankAccount) {
        ResponseEntity<Void> response;
            AccountNameRequestEntity accountNameRequestEntity = AccountNameRequestEntity.builder().name(createBankAccount.getUsername() + "-account").build();
            response = bankClient.createAccount(basicAuthHeader, accountNameRequestEntity);
        return response.getHeaders().getLocation().getPath().substring("/accounts/".length());
    }

    @Override
    public AccountDTO getAccountDetails(Command.GetAccountDetails getAccountDetails) {
        ResponseEntity<AccountDTO> response = bankClient.getAccountData(basicAuthHeader, getAccountDetails.getAccountNumber());
        return response.getBody();
    }

    @Override
    public void withdrawFromAccount(Command.WithdrawFromAccount withdrawFromAccount) {
        MyAccountRequestEntity myAccountRequestEntity = MyAccountRequestEntity.builder()
                .accountNumber(withdrawFromAccount.getAccountNumber())
                .amount(withdrawFromAccount.getAmount()).build();
        bankClient.withdraw(basicAuthHeader, myAccountRequestEntity);
    }

    @Override
    public void depositToAccount(Command.DepositToAccount depositToAccount) {
        MyAccountRequestEntity myAccountRequestEntity = MyAccountRequestEntity.builder()
                .accountNumber(depositToAccount.getAccountNumber())
                .amount(depositToAccount.getAmount()).build();
        bankClient.deposit(basicAuthHeader, myAccountRequestEntity);
    }

    @Override
    public void transfer(Command.Transfer transfer) {
        TransactionRequestEntity transactionRequestEntity = TransactionRequestEntity.builder()
                .sourceAccountNumber(transfer.getSourceAccountNumber())
                .targetAccountNumber(transfer.getTargetAccountNumber())
                .amount(transfer.getAmount()).build();
        bankClient.createTransaction(basicAuthHeader, transactionRequestEntity);
    }
}
