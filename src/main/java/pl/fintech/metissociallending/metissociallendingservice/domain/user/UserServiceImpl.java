package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.TransactionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDetailsDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request.MyAccountRequest;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.AES;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.jwt.JwtTokenProvider;

import java.util.*;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AES aes;
    private final BankService bankService;

    @Override
    public User createUser(UserService.Command.CreateUser createUserCommand) {
        if(userRepository.findByUsername(createUserCommand.getUsername()).isPresent())
            throw new IllegalArgumentException("User with that username already exists");

        String account = bankService.createBankAccount(createUserCommand::getUsername);
        LinkedList<Role> roles = new LinkedList<>();
        roles.add(Role.ROLE_CLIENT);
        User user = User.builder()
                .username(createUserCommand.getUsername())
                .password(passwordEncoder.encode(createUserCommand.getPassword()))
                .roles(roles)
                .name(createUserCommand.getName())
                .cardNumber(aes.encrypt(createUserCommand.getCardNumber()))
                .cvc(aes.encrypt(createUserCommand.getCvc()))
                .expiry(createUserCommand.getExpiry())
                .account(account).build();
        return userRepository.save(user);
    }
    @Override
    public void depositToBank(Command.DepositToBank depositToBank) {
        User user = whoami();
        bankService.depositToAccount(MyAccountRequest.builder().accountNumber(user.getAccount())
                .amount(depositToBank.getAmount()).build());
    }
    @Override
    public void withdrawFromBank(Command.WithdrawFromBank withdrawFromBank) {
        User user = whoami();
        bankService.withdrawFromAccount(MyAccountRequest.builder().accountNumber(user.getAccount())
                .amount(withdrawFromBank.getAmount()).build());
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
        AccountDTO account = getAccountDetailsFromBank();
        return new UserDetailsDTO(user.getUsername(), user.getAccount(), hideCard(aes.decrypt(user.getCardNumber())), user.getName(), "***", user.getExpiry(), account.getAccountBalance(), account.getTransactions());
    }
    private String hideCard(String card){
        return card.substring(0, 3).concat("*".repeat(9)).concat(card.substring(card.length()-4));
    }
    private AccountDTO getAccountDetailsFromBank() {
        User user = whoami();
        AccountDTO accountDetails = bankService.getAccountDetails(user::getAccount);
        accountDetails.setTransactions(getIndexedTransactions(accountDetails.getTransactions()));
        return accountDetails;
    }
    private List<TransactionDTO> getIndexedTransactions(List<TransactionDTO> transactions) {
        transactions.sort(Comparator.comparing(TransactionDTO::getTimestamp));
        long index = 0;
        for(TransactionDTO transaction : transactions){
            transaction.setIndex(index);
            index++;
        }
        return transactions;
    }
}
