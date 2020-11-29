package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

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
    @CsvFileSource(resources = "/testData/loanServiceTest.csv")
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


    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotAcceptOfferWhenAuctionDoesntExist(){
        when(auctionRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, ()->loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public Long getOfferId() {
                return 2L;
            }
        }));
    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotAcceptOfferWhenAuctionDoesntBelongToUser(){
        given(auctionRepository.findById(any())).willReturn(Optional.of(Auction.builder().build()));
        lenient().when(auctionRepository.findByIdAndBorrower(1L, User.builder().username("user").build())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, ()->loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public Long getOfferId() {
                return 2L;
            }
        }));
    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotAcceptOfferWhenOfferDoesntExist(){
        User user = User.builder().username("user").build();
        given(userService.whoami()).willReturn(user);
        given(auctionRepository.findById(any())).willReturn(Optional.of(Auction.builder().id(1L).build()));
        when(auctionRepository.findByIdAndBorrower(1L, user)).thenReturn(Optional.of(Auction.builder().build()));
        lenient().when(offerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, ()->loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public Long getOfferId() {
                return 2L;
            }
        }));
    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotAcceptOfferWhenOfferIsNotPlacedToProvidedAuction(){
        User user = User.builder().username("user").build();
        given(userService.whoami()).willReturn(user);
        given(auctionRepository.findById(any())).willReturn(Optional.of(Auction.builder().id(1L).build()));
        given(auctionRepository.findByIdAndBorrower(1L, user)).willReturn(Optional.of(Auction.builder().build()));
        given(offerRepository.findById(2L)).willReturn(Optional.of(Offer.builder().auction(Auction.builder().id(2L).build()).build()));
        lenient().when(offerRepository.findByIdAndLender(2L, user)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, ()->loanService.acceptOffer(new LoanService.Command.AcceptOffer() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public Long getOfferId() {
                return 2L;
            }
        }));

    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotPayForInstallmentOfNotExistingLoan(){
        when(loanRepository.findByIdAndBorrower(any(), any())).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, ()->loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return 1L;
            }

            @Override
            public Double getAmount() {
                return 100.0d;
            }
        }));

    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotPayForNextInstallmentForPaidLoans(){
        User borrower = User.builder().username("user").build();
        User lender = User.builder().username("lender").build();

       // given(clock.millis()).willReturn(1609597538L);
        given(userService.whoami()).willReturn(borrower);

        Installment ins1 = new Installment(1L,0L, new Date(1609189538), BigDecimal.valueOf(334.16), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.16),BigDecimal.valueOf(668.33),InstallmentStatus.PAID);
        Installment ins2 = new Installment(2L,1L, new Date(1611867938), BigDecimal.valueOf(334.16), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.16),BigDecimal.valueOf(334.17),InstallmentStatus.PAID);
        Installment ins3 = new Installment(3L,2L, new Date(1614546338), BigDecimal.valueOf(334.17), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.17),BigDecimal.valueOf(0),InstallmentStatus.PAID);
        List<Installment> installments = List.of(ins1,ins2,ins3);

        Loan loan = Loan.builder()
                .id(1L)
                .acceptedInterest(0.1d)
                .amountLeft(1002.5)
                .borrower(borrower)
                .lender(lender)
                .startDate(new Date(1606597538))
                .takenAmount(1000d)
                .installments(installments)
                .build();

        when(loanRepository.findByIdAndBorrower(1L, borrower)).thenReturn(Optional.of(loan));
        assertThrows(NoSuchElementException.class , ()-> loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return 1L;
            }

            @Override
            public Double getAmount() {
                return 334.16;
            }
        }));


    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldNotPayForNextInstallmentThatAmountGivenDiffersFromInstallment(){
        User borrower = User.builder().username("user").build();
        User lender = User.builder().username("lender").build();

        given(clock.millis()).willReturn(1613597538L);
        given(userService.whoami()).willReturn(borrower);

        Installment ins1 = new Installment(1L,0L, new Date(1609189538), BigDecimal.valueOf(334.16), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.16),BigDecimal.valueOf(668.33),InstallmentStatus.PAID);
        Installment ins2 = new Installment(2L,1L, new Date(1611867938), BigDecimal.valueOf(334.16), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.16),BigDecimal.valueOf(334.17),InstallmentStatus.PAID);
        Installment ins3 = new Installment(3L,2L, new Date(1614546338), BigDecimal.valueOf(334.17), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(334.17),BigDecimal.valueOf(0),InstallmentStatus.PENDING);
        List<Installment> installments = List.of(ins1,ins2,ins3);

        Loan loan = Loan.builder()
                .id(1L)
                .acceptedInterest(0.1d)
                .amountLeft(1002.5)
                .borrower(borrower)
                .lender(lender)
                .startDate(new Date(1606597538))
                .takenAmount(1000d)
                .installments(installments)
                .build();

        when(loanRepository.findByIdAndBorrower(1L, borrower)).thenReturn(Optional.of(loan));
        assertThrows(ValidationException.class , ()-> loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return 1L;
            }

            @Override
            public Double getAmount() {
                return 334.20;
            }
        }));

    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldPayForInstallmentThatIsMissedWithFine(){
        User borrower = User.builder().username("user").build();
        User lender = User.builder().username("lender").build();

        given(clock.millis()).willReturn(1613597538L);
        given(userService.whoami()).willReturn(borrower);

        Installment ins1 = new Installment(1L,0L, new Date(1609189538), BigDecimal.valueOf(336.93), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.93),BigDecimal.valueOf(668.33),InstallmentStatus.PAID);
        Installment ins2 = new Installment(2L,1L, new Date(1611867938), BigDecimal.valueOf(336.93), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.93),BigDecimal.valueOf(334.17),InstallmentStatus.PAID);
        Installment ins3 = new Installment(3L,2L, new Date(1614546338), BigDecimal.valueOf(336.94), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.94),BigDecimal.valueOf(0),InstallmentStatus.PENDING);
        List<Installment> installments = List.of(ins1,ins2,ins3);

        Loan loan = Loan.builder()
                .id(1L)
                .acceptedInterest(0.1d)
                .amountLeft(1010.8)
                .borrower(borrower)
                .lender(lender)
                .startDate(new Date(1606597538))
                .takenAmount(1000d)
                .installments(installments)
                .build();

        when(installmentRepository.save(any(Installment.class))).thenAnswer(i->i.getArguments()[0]);
        when(loanRepository.findByIdAndBorrower(1L, borrower)).thenReturn(Optional.of(loan));
        loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return 1L;
            }

            @Override
            public Double getAmount() {
                return 339.73;
            }
        });
        verify(bankService).transfer(any());
        ins3.changeToPaid();
        assertEquals(InstallmentStatus.PAID, ins3.getStatus());
        verify(installmentRepository).save(ins3);
    }
    @Test
    @WithMockUser(username = "user", password = "name")
    public void shouldPayForInstallmentThatIsPending(){
        User borrower = User.builder().username("user").build();
        User lender = User.builder().username("lender").build();

        given(clock.millis()).willReturn(1613597538L);
        given(userService.whoami()).willReturn(borrower);

        Installment ins1 = new Installment(1L,0L, new Date(1609189538), BigDecimal.valueOf(336.93), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.93),BigDecimal.valueOf(668.33),InstallmentStatus.PAID);
        Installment ins2 = new Installment(2L,1L, new Date(1611867938), BigDecimal.valueOf(336.93), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.93),BigDecimal.valueOf(334.17),InstallmentStatus.PAID);
        Installment ins3 = new Installment(3L,2L, new Date(1614546338), BigDecimal.valueOf(336.94), BigDecimal.valueOf(.1d), BigDecimal.valueOf(0), BigDecimal.valueOf(336.94),BigDecimal.valueOf(0),InstallmentStatus.MISSED);
        List<Installment> installments = List.of(ins1,ins2,ins3);

        Loan loan = Loan.builder()
                .id(1L)
                .acceptedInterest(0.1d)
                .amountLeft(1010.8)
                .borrower(borrower)
                .lender(lender)
                .startDate(new Date(1606597538))
                .takenAmount(1000d)
                .installments(installments)
                .build();

        when(installmentRepository.save(any(Installment.class))).thenAnswer(i->i.getArguments()[0]);
        when(loanRepository.findByIdAndBorrower(1L, borrower)).thenReturn(Optional.of(loan));
        loanService.payNextInstallment(new LoanService.Command.PayNextInstallment() {
            @Override
            public Long getLoanId() {
                return 1L;
            }

            @Override
            public Double getAmount() {
                return 339.73;
            }
        });
        verify(bankService).transfer(any());
        ins3.changeToPaid();
        assertEquals(InstallmentStatus.PAID, ins3.getStatus());
        verify(installmentRepository).save(ins3);
    }

}
