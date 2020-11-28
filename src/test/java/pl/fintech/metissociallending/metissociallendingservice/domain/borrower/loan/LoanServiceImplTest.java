package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private UserService userService;
    @Mock
    private BankService bankService;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private InstallmentRepository installmentRepository;
    @Mock
    private Clock clock;
    @InjectMocks
    private LoanServiceImpl loanService;


    @SneakyThrows
    @ParameterizedTest
    @CsvFileSource(resources = "/testData/loan-service-test.csv")
    @WithMockUser(username = "user", password = "name")
    void acceptOffer(BigDecimal loanAmount, Double annualPercentageRate, Integer numberOfInstallments, Long durationOfLoanInDays) {
        Long baseDate = 2000000000000L;
        Long auctionId = 1L;
        Long offerId = 1L;
        String description = "Sample description";
        Date beginDate = new Date(baseDate);
        Date endDate = new Date(baseDate + 86400 * durationOfLoanInDays);
        Date offerDate = new Date(baseDate + 86400);
        Long loanDate = baseDate + 86400;

        User lender = User.builder()
                .build();

        User borrower = User.builder()
                .build();

        Auction auction = Auction.builder()
                .id(auctionId)
                .borrower(borrower)
                .loanAmount(loanAmount)
                .beginDate(beginDate)
                .endDate(endDate)
                .numberOfInstallments(numberOfInstallments)
                .isClosed(true)
                .description(description)
                .build();

        Offer offer = Offer.builder()
                .id(offerId)
                .auction(auction)
                .lender(lender)
                .date(offerDate)
                .annualPercentageRate(annualPercentageRate)
                .build();

        User user = User.builder()
                .username("user")
                .password("name")
                .build();

        when(userService.whoami()).thenReturn(user);
        when(clock.millis()).thenReturn(loanDate);
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.ofNullable(auction));
        when(auctionRepository.findByIdAndBorrower(auctionId, user)).thenReturn(Optional.ofNullable(auction));
        when(auctionRepository.save(auction)).thenReturn(auction);
        when(offerRepository.findById(offerId)).thenReturn(Optional.ofNullable(offer));
        doNothing().when(bankService).transfer(any());
        Loan fakeLoan = Loan.builder().build();


        final Loan[] loan = new Loan[1];
        when(loanRepository.save(fakeLoan)).thenAnswer(i -> {
            loan[0] = (Loan) i.getArguments()[0];
            return loan[0];
        });

        Installment fakeInstallment = Installment.builder().build();
        when(installmentRepository.save(fakeInstallment)).thenAnswer(i -> i.getArguments()[0]);

        LoanDTO loanDTO = loanService.acceptOffer(new LoanService.Command.AcceptOffer() {

            @Override
            public Long getAuctionId() {
                return auctionId;
            }

            @Override
            public Long getOfferId() {
                return offerId;
            }
        });

        //TODO add amountLeft to not be null. Dunno how
        when(loanRepository.findAll()).thenReturn(List.of(loan));
        loanService.updateLoansStatus();

        //TODO for testing purposes
        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(loanDTO));

        //TODO check installments data
        assertEquals(loanAmount.doubleValue(), loanDTO.getTakenAmount());
        assertEquals(annualPercentageRate, loanDTO.getAcceptedInterest());
        assertEquals(offerDate, loanDTO.getStartDate());
    }
}
