package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.TransactionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.UserDetailsDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.AES;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration
public class UserServiceImplTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private BankService bankService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AES aes;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void shouldCreateUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("example")
                .password("example")
                .name("Example Exampling")
                .cardNumber("1234-1234-1234-1234")
                .expiry("05/22")
                .cvc("123")
                .build();
        User user = User.builder()
                .username("example")
                .password("example")
                .name("Example Exampling")
                .account("a1g2-fsha-suh1z-kasd-alkya")
                .cardNumber("1234-1234-1234-1234")
                .expiry("05/22")
                .cvc("123")
                .roles(List.of(Role.ROLE_CLIENT))
                .build();
        given(userRepository.findByUsername(userDTO.getUsername())).willReturn(Optional.empty());
        given(bankService.createBankAccount(any())).willReturn(user.getAccount());
        given(passwordEncoder.encode(userDTO.getPassword())).willReturn(user.getPassword());
        given(aes.encrypt(any(String.class))).willAnswer(i -> i.getArguments()[0]);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        User createdUser = userService.createUser(userDTO);

        assertEquals(user.getUsername(),createdUser.getUsername());
        assertEquals(user.getPassword(),createdUser.getPassword());
        assertEquals(user.getName(),createdUser.getName());
        assertEquals(user.getAccount(),createdUser.getAccount());
        assertEquals(user.getCardNumber(),createdUser.getCardNumber());
        assertEquals(user.getExpiry(),createdUser.getExpiry());
        assertEquals(user.getCvc(),createdUser.getCvc());
        assertEquals(user.getRoles(),createdUser.getRoles());
    }

    @Test
    public void shouldNotCreateAgainExistingUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("example")
                .build();
        User user = User.builder().build();
        when(userRepository.findByUsername(userDTO.getUsername())).thenReturn(Optional.of(user));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(userDTO));
    }

    @Test
    public void shouldNotCreateUserWhenBankApiResponseWithError() {
        UserDTO userDTO = UserDTO.builder()
                .username("example")
                .build();
        given(userRepository.findByUsername(userDTO.getUsername())).willReturn(Optional.empty());
        when(bankService.createBankAccount(any())).thenThrow(FeignException.class);

        assertThrows(FeignException.class,() -> userService.createUser(userDTO));
    }

    @Test
    @WithMockUser(username = "example", password = "example")
    public void shouldGetUserDetailsWithIndexedTransactions() {
        long baseDate = 2000000000000L;
        long dateOffset = 50000L;

        User user = User.builder()
                .username("example")
                .name("Example Exampling")
                .account("a1g2-fsha-suh1z-kasd-alkya")
                .cardNumber("1234-1234-1234-1234")
                .expiry("05/22")
                .cvc("123")
                .build();

        TransactionDTO transaction1 = new TransactionDTO(3L, null, "DEBIT", 5820.35, "referenceId3", new Date(baseDate + dateOffset * 2));
        TransactionDTO transaction2 = new TransactionDTO(4L, null, "CREDIT", 124.12, "referenceId4", new Date(baseDate + dateOffset * 3));
        TransactionDTO transaction3 = new TransactionDTO(2L, null, "CREDIT", 10.0, "referenceId2", new Date(baseDate + dateOffset));
        TransactionDTO transaction4 = new TransactionDTO(5L, null, "DEBIT", 782.73, "referenceId5", new Date(baseDate + dateOffset * 4));
        TransactionDTO transaction5 = new TransactionDTO(1L, null, "CREDIT", 150.0, "referenceId1", new Date(baseDate));
        AccountDTO accountDTO = new AccountDTO("example-account", "a1g2-fsha-suh1z-kasd-alkya",
                new LinkedList<>(List.of(transaction1, transaction2, transaction3, transaction4, transaction5)),
                6318.96);

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getUsername());
        given(bankService.getAccountDetails(any())).willReturn(accountDTO);
        when(aes.decrypt(any(String.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDetailsDTO preparedUserDetails = userService.getUserDetails();

        accountDTO.getTransactions().sort(Comparator.comparing(TransactionDTO::getTimestamp));

        assertEquals(user.getUsername(), preparedUserDetails.getUsername());
        assertEquals(user.getAccount(), preparedUserDetails.getAccount());
        assertEquals(user.getCardNumber().substring(0, 3), preparedUserDetails.getCardNumber().substring(0, 3));
        assertEquals(user.getCardNumber().substring(user.getCardNumber().length() - 4), preparedUserDetails.getCardNumber().substring(preparedUserDetails.getCardNumber().length() - 4));
        assertEquals(user.getName(), preparedUserDetails.getName());
        assertEquals(accountDTO.getAccountBalance(), preparedUserDetails.getBalance());
        for (int i = 0; i < accountDTO.getTransactions().size(); i++){
            assertEquals(accountDTO.getTransactions().get(i).getTimestamp(), preparedUserDetails.getTransactions().get(i).getTimestamp());
            assertEquals(i, preparedUserDetails.getTransactions().get(i).getIndex());
        }
    }

    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldThrowOnGetUserDetailsWhenBankApiResponsesWithError() {
        User user = User.builder()
                .username("example")
                .name("Example Exampling")
                .account("a1g2-fsha-suh1z-kasd-alkya")
                .cardNumber("1234-1234-1234-1234")
                .expiry("05/22")
                .cvc("123")
                .build();

        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        given(userRepository.findByUsername(any(String.class))).willReturn(Optional.of(user));
        given(SecurityContextHolder.getContext().getAuthentication().getName()).willReturn(user.getUsername());
        given(bankService.getAccountDetails(any())).willThrow(FeignException.class);

        assertThrows(FeignException.class, () -> userService.getUserDetails());
    }

}
