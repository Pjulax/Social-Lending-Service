package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class LoanServiceImplTest {

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private UserService userService;
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
    @Test
    @WithMockUser(username = "user", password = "name")
    void acceptOffer() {
        Long auctionId = 1L;
        Long offerId = 1L;
        BigDecimal loanAmount = new BigDecimal(100000);
        Double annualPercentageRate = 10d;
        Integer numberOfInstallments = 10;
        Date beginDate = new Date(2000000000000L);
        Date endDate = new Date(2001000000000L);
        Date offerDate = new Date(2000200000000L);
        Long loanDate = 2000500000000L;
        Boolean isClosed = true;
        String description = "Sample description";

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
                .isClosed(isClosed)
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
        Loan fakeLoan = Loan.builder().build();
        when(loanRepository.save(fakeLoan)).thenAnswer(i -> i.getArguments()[0]);
        Installment fakeInstallment = Installment.builder().build();
        when(installmentRepository.save(fakeInstallment)).thenAnswer(i -> i.getArguments()[0]);
        LoanDTO loan = loanService.acceptOffer(new LoanService.Command.AcceptOffer() {

            @Override
            public Long getAuctionId() {
                return auctionId;
            }

            @Override
            public Long getOfferId() {
                return offerId;
            }
        });

        ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
        System.out.println(objectWriter.writeValueAsString(loan));
    }
}
