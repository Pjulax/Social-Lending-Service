package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDetailsDTO;

public interface UserService {
    User createUser(Command.CreateUser createUserCommand);
    String login(Query.Login login);
    User whoami();
    UserDetailsDTO getUserDetails();

    interface Command {
        interface CreateUser extends Command{
             String getUsername();
             String getPassword();
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
