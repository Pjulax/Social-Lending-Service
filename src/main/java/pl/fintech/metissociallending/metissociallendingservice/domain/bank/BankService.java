package pl.fintech.metissociallending.metissociallendingservice.domain.bank;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;

/**
 * to get accounts balance or to make transfers bank service is used
 * <p><ul>
 *  <li>get account balance</li>
 *  <li>withdraw cash</li>
 *  <li>deposit cash</li>
 *  <li>make transfer</li>
 * </ul></p>
 * @see pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.BankServiceImpl
 */
public interface BankService {
    String createBankAccount(Command.CreateBankAccount createBankAccount);
    AccountDTO getAccountDetails(Command.GetAccountDetails getAccountDetails);
    void withdrawFromAccount(Command.WithdrawFromAccount withdrawFromAccount);
    void depositToAccount(Command.DepositToAccount depositToAccount);
    void transfer(Command.Transfer transfer);

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
        interface Transfer {
            String getSourceAccountNumber();
            String getTargetAccountNumber();
            Double getAmount();
        }
    }
}