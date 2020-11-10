package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    @Override
    public User createUser(UserService.Command.CreateUser createUserCommand) {
        User user = new User();
        return userRepository.save(user);
    }

}
