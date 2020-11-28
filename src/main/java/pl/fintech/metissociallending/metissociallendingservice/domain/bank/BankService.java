package pl.fintech.metissociallending.metissociallendingservice.domain.bank;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;

public interface BankService {
    String createBankAccount(Command.CreateBankAccount createBankAccount);
    AccountDTO getAccountDetails(Command.GetAccountDetails getAccountDetails);
    void withdrawFromAccount(Command.WithdrawFromAccount withdrawFromAccount);
    void depositToAccount(Command.DepositToAccount depositToAccount);
    void createTransaction(Command.CreateTransaction createTransaction);

    interface Command {
        interface CreateBankAccount {
            String getUsername();
        }
        interface GetAccountDetails {
            String getAccountNumber();
        }
        interface WithdrawFromAccount {
            String getAccountNumber();
            Double getAmount();
        }
        interface DepositToAccount {
            String getAccountNumber();
            Double getAmount();
        }
        interface CreateTransaction {
            String getSourceAccountNumber();
            String getTargetAccountNumber();
            Double getAmount();
        }
    }
}