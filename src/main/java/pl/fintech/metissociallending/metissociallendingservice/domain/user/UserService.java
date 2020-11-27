package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import org.springframework.http.ResponseEntity;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.TransactionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDetailsDTO;

public interface UserService {
    User createUser(Command.CreateUser createUserCommand);
    String login(Query.Login login);
    User whoami();
    UserDetailsDTO getUserDetails();
    void deleteUser(Command.DeleteUser deleteUser);
    AccountDTO getAccountDetailsFromBank();
    void depositToBank(Command.DepositToBank depositToBank);
    void withdrawFromBank(Command.WithdrawFromBank withdrawFromBank);

    interface Command {
        interface CreateUser extends Command{
             String getUsername();
             String getPassword();
             String getCardNumber();
             String getName();
             String getExpiry();
             String getCvc();
        }
        interface DeleteUser extends Command{
            String getUsername();
        }
        interface DepositToBank extends Command{
            Double getAmount();
        }
        interface WithdrawFromBank extends Command{
            Double getAmount();
        }
    }
    interface  Query {
        interface Login extends Query{
            String getUsername();
            String getPassword();
        }
        interface Search extends  Query{
            String getUsername();
        }
    }
}
