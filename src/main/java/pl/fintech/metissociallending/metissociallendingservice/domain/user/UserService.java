package pl.fintech.metissociallending.metissociallendingservice.domain.user;

public interface UserService {
    User createUser(Command.CreateUser createUserCommand);
    interface Command {
        interface CreateUser extends Command{
             String getName();
        }
    }
}
