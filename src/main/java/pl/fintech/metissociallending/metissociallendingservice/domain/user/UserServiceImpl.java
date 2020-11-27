package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDetailsDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankClient;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.requestEntity.AccountEntityRequest;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.AES;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.jwt.JwtTokenProvider;

import java.util.LinkedList;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES aes;
    //private final BankService bankService;
    private final BankClient bankClient;
    private final String basicAuthHeader;

    @Override
    public User createUser(UserService.Command.CreateUser createUserCommand) {
        if(userRepository.findByUsername(createUserCommand.getUsername()).isPresent())
            throw new IllegalArgumentException("User with that username already exists");
        //String account = bankService.createAccount(createUserCommand.getUsername()+"-account");
        String account = bankClient
                .accounts(basicAuthHeader,AccountEntityRequest.builder().name(createUserCommand.getUsername()+"-account").build())
                .getHeaders().getLocation().getPath().substring("/accounts/".length());

        LinkedList<Role> roles = new LinkedList<Role>();
        roles.add(Role.ROLE_CLIENT);
        User user = User.builder()
                .username(createUserCommand.getUsername())
                .password(passwordEncoder.encode(createUserCommand.getPassword()))
                .balance(0.0d)
                .roles(roles)
                .name(createUserCommand.getName())
                .cardNumber(aes.encrypt(createUserCommand.getCardNumber()))
                .cvc(aes.encrypt(createUserCommand.getCvc()))
                .expiry(createUserCommand.getExpiry())
                .account(account).build();
        return userRepository.save(user);
    }
    @Override
    public void deleteUser(Command.DeleteUser deleteUser){
        if(userRepository.findByUsername(deleteUser.getUsername()).isPresent())
            userRepository.deleteByUsername(deleteUser.getUsername());
        else
            throw new NoSuchElementException("User doesn't exists");
    }
    @Override
    public String login(Query.Login login) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        return jwtTokenProvider.createToken(login.getUsername(), userRepository.findByUsername(login.getUsername()).get().getRoles());
    }
    public User search(Query.Search searchQuery) {
        return userRepository.findByUsername(searchQuery.getUsername()).orElseThrow();
    }
    @Override
    public User whoami(){
        return search(()->SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public UserDetailsDTO getUserDetails() {
        User user = whoami();
        return new UserDetailsDTO(user.getUsername(), user.getAccount(), hideCardCredentials(aes.decrypt(user.getCardNumber())), user.getName(), "***", user.getExpiry(), user.getBalance());
    }
    private String hideCardCredentials(String card){
        return card.substring(0, 3).concat("*".repeat(9)).concat(card.substring(card.length()-4));
    }

}
