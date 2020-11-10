package pl.fintech.metissociallending.metissociallendingservice.domain.user;

public interface UserService {
    User createUser(Command.CreateUser createUserCommand);
    String login(Command.Login login);
    interface Command {
        interface CreateUser extends Command{
             String getUsername();
             String getPassword();
        }
        interface Login extends Command{
            String getUsername();
            String getPassword();
        }
    }
}
