package pl.fintech.metissociallending.metissociallendingservice.domain.lender;

import io.micrometer.core.instrument.config.validate.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.SubmitOfferDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class LenderServiceImplTests {

    @Mock
    private AuctionRepository auctionRepository;
    @Mock
    private OfferRepository offerRepository;
    @Mock
    private Clock clock;
    @Mock
    private UserService userService;
    @InjectMocks
    private LenderServiceImpl lenderService;

    @Test
    @WithMockUser(username = "user", password = "name")
    void shouldSubmitOffer(){
        Long auctionId = 1L;
        Long offerId = 1L;
        Double proposedAnnualPercentageRate = 0.05;

        SubmitOfferDTO submitOfferDTO = new SubmitOfferDTO(auctionId, proposedAnnualPercentageRate);

        User lender = User.builder()
                .username("lender")
                .build();

        User borrower = User.builder()
                .username("borrower")
                .build();

        Auction auction = Auction.builder()
                .id(auctionId)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1500.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(12)
                .isClosed(false)
                .description("simple description")
                .build();

        Offer offer = Offer.builder()
                .id(offerId)
                .auction(auction)
                .lender(lender)
                .date(new Date(154352525L))
                .annualPercentageRate(proposedAnnualPercentageRate)
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(java.util.Optional.ofNullable(auction));
        when(userService.whoami()).thenReturn(lender);
        when(auctionRepository.findByIdAndBorrower(auctionId, lender)).thenReturn(Optional.empty());
        when(offerRepository.save(any(Offer.class))).thenAnswer(i -> i.getArguments()[0]);
        when(clock.millis()).thenReturn(154352525L);

        Offer submittedOffer = lenderService.submitOffer(submitOfferDTO);

        assertEquals(offer.getDate(),submittedOffer.getDate());
        assertEquals(offer.getAnnualPercentageRate(),submittedOffer.getAnnualPercentageRate());
        assertEquals(offer.getLender(),submittedOffer.getLender());
        assertEquals(offer.getAuction(),submittedOffer.getAuction());
    }

    @Test
    @WithMockUser(username = "user", password = "name")
    void shouldNotSubmitOfferToHisOwnAuction(){
        Long auctionId = 1L;
        Double proposedAnnualPercentageRate = 0.05;

        SubmitOfferDTO submitOfferDTO = new SubmitOfferDTO(auctionId, proposedAnnualPercentageRate);

        User lender = User.builder()
                .username("lender")
                .build();

        User borrower = User.builder()
                .username("borrower")
                .build();

        Auction auction = Auction.builder()
                .id(auctionId)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1500.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(12)
                .isClosed(false)
                .description("simple description")
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(java.util.Optional.ofNullable(auction));
        when(userService.whoami()).thenReturn(lender);
        when(auctionRepository.findByIdAndBorrower(auctionId, lender)).thenReturn(Optional.ofNullable(auction));

        assertThrows(IllegalArgumentException.class, () -> lenderService.submitOffer(submitOfferDTO));
    }


    @ParameterizedTest
    @CsvFileSource(resources = "/testData/lender-service-test.csv")
    @WithMockUser(username = "user", password = "name")
    void shouldNotSubmitOfferWithNonPositiveAnnualRate(Double annualRate){
        Long auctionId = 1L;
        SubmitOfferDTO submitOfferDTO = new SubmitOfferDTO(auctionId, annualRate);

        User lender = User.builder()
                .username("lender")
                .build();

        User borrower = User.builder()
                .username("borrower")
                .build();

        Auction auction = Auction.builder()
                .id(auctionId)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1500.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(12)
                .isClosed(false)
                .description("simple description")
                .build();

        when(auctionRepository.findById(auctionId)).thenReturn(java.util.Optional.ofNullable(auction));
        when(userService.whoami()).thenReturn(lender);
        when(auctionRepository.findByIdAndBorrower(auctionId, lender)).thenReturn(Optional.empty());

        assertThrows(ValidationException.class, () -> lenderService.submitOffer(submitOfferDTO));
    }

    @Test
    @WithMockUser(username = "user", password = "name")
    void shouldNotCancelNotExistingOffer(){
        Long offerId = 1L;

        when(offerRepository.findById(offerId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lenderService.cancelOffer(() -> offerId));
    }

    @Test
    @WithMockUser(username = "user", password = "name")
    void shouldNotCancelNotOwnedOffer(){
        Long offerId = 1L;
        Double proposedAnnualPercentageRate = 0.05;

        User lender = User.builder()
                .username("lender")
                .build();

        User borrower = User.builder()
                .username("borrower")
                .build();

        Auction auction = Auction.builder()
                .id(1L)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1500.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(12)
                .isClosed(false)
                .description("simple description")
                .build();


        Offer offer = Offer.builder()
                .id(offerId)
                .auction(auction)
                .lender(lender)
                .date(new Date(154352525L))
                .annualPercentageRate(proposedAnnualPercentageRate)
                .build();

        when(offerRepository.findById(offerId)).thenReturn(Optional.ofNullable(offer));
        when(userService.whoami()).thenReturn(lender);
        when(offerRepository.findByIdAndLender(offerId, lender)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> lenderService.cancelOffer(() -> offerId));
    }

    @Test
    @WithMockUser(username = "user", password = "name")
    void shouldGetNotClosedAndNotLenderAuctions(){
        User lender = User.builder()
                .username("lender")
                .build();

        User borrower = User.builder()
                .username("borrower")
                .build();

        Auction auction1 = Auction.builder()
                .id(1L)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(4000.0))
                .beginDate(new Date(152351562))
                .endDate(new Date(165352525))
                .numberOfInstallments(4)
                .isClosed(false)
                .description("simple description1")
                .build();
        Auction auction2 = Auction.builder()
                .id(2L)
                .borrower(lender)
                .loanAmount(BigDecimal.valueOf(2500.0))
                .beginDate(new Date(152522525))
                .endDate(new Date(163452525))
                .numberOfInstallments(10)
                .isClosed(false)
                .description("simple description2")
                .build();
        Auction auction3 = Auction.builder()
                .id(3L)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1800.0))
                .beginDate(new Date(157878525))
                .endDate(new Date(167456525))
                .numberOfInstallments(7)
                .isClosed(true)
                .description("simple description3")
                .build();
        Auction auction4 = Auction.builder()
                .id(4L)
                .borrower(lender)
                .loanAmount(BigDecimal.valueOf(1200.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(10)
                .isClosed(true)
                .description("simple description4")
                .build();

        List<Auction> allAuctions = new LinkedList<Auction>(List.of(auction1,auction2,auction3,auction4));
        List<Auction> lenderAuctions = List.of(auction2,auction4);
        List<Auction> notLenderOpenAuctions = List.of(auction1);

        when(auctionRepository.findAll()).thenReturn(allAuctions);
        when(userService.whoami()).thenReturn(lender);
        when(auctionRepository.findAllByBorrower(lender)).thenReturn(lenderAuctions);

        List<Auction> allAvailableAuctions = lenderService.getAllAvailableAuctions();

        assertEquals(notLenderOpenAuctions, allAvailableAuctions);
        assertEquals(notLenderOpenAuctions.get(0), allAvailableAuctions.get(0));
        assertEquals(notLenderOpenAuctions.get(0).getId(), allAvailableAuctions.get(0).getId());
        assertEquals(notLenderOpenAuctions.get(0).getBeginDate(), allAvailableAuctions.get(0).getBeginDate());

    }

    @Test
    //@CsvFileSource(resources = "/testData/lender-service-test.csv")
    @WithMockUser(username = "user", password = "name")
    void shouldNotGetClosedOrLenderAuctions(){
        User lender = User.builder()
                .id(1L)
                .username("lender")
                .build();

        User borrower = User.builder()
                .id(2L)
                .username("borrower")
                .build();

        Auction auction1 = Auction.builder()
                .id(1L)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(4000.0))
                .beginDate(new Date(152351562))
                .endDate(new Date(165352525))
                .numberOfInstallments(4)
                .isClosed(false)
                .description("simple description1")
                .build();

        Auction auction2 = Auction.builder()
                .id(2L)
                .borrower(lender)
                .loanAmount(BigDecimal.valueOf(2500.0))
                .beginDate(new Date(152522525))
                .endDate(new Date(163452525))
                .numberOfInstallments(10)
                .isClosed(false)
                .description("simple description2")
                .build();

        Auction auction3 = Auction.builder()
                .id(3L)
                .borrower(borrower)
                .loanAmount(BigDecimal.valueOf(1800.0))
                .beginDate(new Date(157878525))
                .endDate(new Date(167456525))
                .numberOfInstallments(7)
                .isClosed(true)
                .description("simple description3")
                .build();

        Auction auction4 = Auction.builder()
                .id(4L)
                .borrower(lender)
                .loanAmount(BigDecimal.valueOf(1200.0))
                .beginDate(new Date(152352525))
                .endDate(new Date(164352525))
                .numberOfInstallments(10)
                .isClosed(true)
                .description("simple description4")
                .build();

        List<Auction> allAuctions = new LinkedList<Auction>(List.of(auction1,auction2,auction3,auction4));
        List<Auction> lenderAuctions = List.of(auction2,auction4);
        List<Auction> notLenderOpenAuctions = List.of(auction1);

        when(auctionRepository.findAll()).thenReturn(allAuctions);
        when(userService.whoami()).thenReturn(lender);
        when(auctionRepository.findAllByBorrower(lender)).thenReturn(lenderAuctions);

        List<Auction> allAvailableAuctions = lenderService.getAllAvailableAuctions();

        for(Auction auction : allAvailableAuctions) {
            assertNotEquals(lender, auction.getBorrower());
            assertNotEquals(true, auction.getIsClosed());
        }
    }


}
