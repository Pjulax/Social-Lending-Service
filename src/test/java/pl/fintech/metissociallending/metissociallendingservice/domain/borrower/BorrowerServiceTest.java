package pl.fintech.metissociallending.metissociallendingservice.domain.borrower;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionWithOffersDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserServiceImpl;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.ClockImpl;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
public class BorrowerServiceTest {

    @Mock
    UserServiceImpl userService;

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    OfferRepository offerRepository;

    @Mock
    ClockImpl clock;

    @InjectMocks
    private BorrowerServiceImpl borrowerService;

    @ParameterizedTest
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @CsvFileSource(resources = "/testData/createAuction.csv")
    public void shouldCreateAuctionForAuthorizedUser(String auctionDescription, String endDate, Double loanAmount, Integer numberOfInstallment ){
        User user = User.builder().username("user").build();
        when(userService.whoami()).thenReturn(user);

        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setDescription(auctionDescription);
        auctionDTO.setEndDate(endDate);
        auctionDTO.setLoanAmount(loanAmount);
        auctionDTO.setNumberOfInstallments(numberOfInstallment);
        Auction auction = new Auction(1L, user, auctionDTO.getLoanAmount(), new Date(),auctionDTO.getEndDate(), auctionDTO.getNumberOfInstallments(),false, auctionDTO.getDescription());

        when(auctionRepository.save(any())).thenReturn(auction);
        Auction createdAuction = borrowerService.createNewAuctionSinceNow(auctionDTO);

        assertEquals(user.getUsername(), createdAuction.getBorrower().getUsername());
        assertEquals(auction.getLoanAmount(), createdAuction.getLoanAmount());
        assertEquals(auction.getNumberOfInstallments(), createdAuction.getNumberOfInstallments());
        assertEquals(auction.getEndDate(), createdAuction.getEndDate());
    }

    @ParameterizedTest
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @CsvFileSource(resources = "/testData/addDescriptionAuction.csv")
    public void shouldAddDescriptionForExistingAuction(Double loanAmount, Long startTime, Long endTime,  Integer numberOfInstallment, String desc){
        User user = User.builder().username("user").build();
        when(userService.whoami()).thenReturn(user);

        Auction auction = new Auction(1L, BigDecimal.valueOf(loanAmount), new Date(startTime), new Date(endTime),numberOfInstallment );
        when(auctionRepository.save(any(Auction.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.findByIdAndBorrower(1L,user)).thenReturn(Optional.of(auction));

        Auction auctionWithDesc = borrowerService.addAuctionDescription(new BorrowerService.Command.AddAuctionDescription() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public String getDescription() {
                return desc;
            }
        });

        assertEquals(auction.getDescription(), auctionWithDesc.getDescription());
    }

    @Test
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldNotAddDescriptionWhenAuctionDoesntExist(){
        when(auctionRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->borrowerService.addAuctionDescription(new BorrowerService.Command.AddAuctionDescription() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public String getDescription() {
                return "new desc";
            }
        }) );
    }

    @ParameterizedTest
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @CsvFileSource(resources = "/testData/addDescriptionAuction.csv")
    public void shouldNotAddDescriptionWhenUserHasNotGotAuction(Double loanAmount, Long startTime, Long endTime,  Integer numberOfInstallment, String desc){
        Auction auction = new Auction(1L, BigDecimal.valueOf(loanAmount), new Date(startTime), new Date(endTime),numberOfInstallment );

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.findByIdAndBorrower(1L, null)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, ()->borrowerService.addAuctionDescription(new BorrowerService.Command.AddAuctionDescription() {
            @Override
            public Long getAuctionId() {
                return 1L;
            }

            @Override
            public String getDescription() {
                return desc;
            }
        }) );
    }

    @Test
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldGetAuctionBelongingOnlyToBorrower(){
        User user = User.builder().username("user").build();

        List<Auction> auctions = new ArrayList<>();
        Auction a1 = new Auction(1L, BigDecimal.valueOf(1000L), new Date(1600574351), new Date(1606574351),20 );
        Auction a2 = new Auction(2L, BigDecimal.valueOf(1500L), new Date(1600574351), new Date(1606574351),25 );
        auctions.add(a1);
        auctions.add(a2);

        given(userService.whoami()).willReturn(user);
        when(auctionRepository.findAllByBorrower(user)).thenReturn(auctions);
        assertEquals(borrowerService.getAllAuctions(), auctions);
    }

    @ParameterizedTest
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @CsvFileSource(resources = "/testData/closedAuction.csv")
    public void shouldGetAuctionThatIsClosed(long clocktime, long begintime, long endtime, Double amount, String desc, int numberOfInstallment){
        User user = User.builder().username("user").build();

        when(clock.millis()).thenReturn(clocktime);

        Auction auction = Auction.builder()
                .borrower(user)
                .beginDate(new Date(begintime))
                .endDate(new Date(endtime))
                .loanAmount(BigDecimal.valueOf(amount))
                .id(1L)
                .description(desc)
                .isClosed(false)
                .numberOfInstallments(numberOfInstallment)
                .build();
        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));
        when(auctionRepository.save(any(Auction.class)))
                .thenAnswer(i -> i.getArguments()[0]);
        when(offerRepository.findAllByAuction(any())).thenReturn(List.of());

        AuctionWithOffersDTO auctionWithOffersDTO = borrowerService.getAuctionById(1L);

        assertEquals(true, auctionWithOffersDTO.isClosed());
        assertEquals(auction.getDescription(), auctionWithOffersDTO.getDescription());
        assertEquals(auction.getEndDate(), auctionWithOffersDTO.getEndDate());
        assertEquals(auction.getNumberOfInstallments(), auctionWithOffersDTO.getNumberOfInstallments());

    }

    @ParameterizedTest
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @CsvFileSource(resources = "/testData/openAuction.csv")
    public void shouldGetAuctionThatIsOpen(long clocktime, long begintime, long endtime, Double amount, String desc, int numberOfInstallment){
        User user = User.builder().username("user").build();

        Auction auction = Auction.builder()
                .borrower(user)
                .beginDate(new Date(begintime))
                .endDate(new Date(endtime))
                .loanAmount(BigDecimal.valueOf(amount))
                .id(1L)
                .description(desc)
                .isClosed(false)
                .numberOfInstallments(numberOfInstallment)
                .build();

        when(auctionRepository.findById(1L)).thenReturn(Optional.of(auction));

        AuctionWithOffersDTO auctionWithOffersDTO = borrowerService.getAuctionById(1L);
        assertEquals(false, auctionWithOffersDTO.isClosed());
        assertEquals(auction.getDescription(), auctionWithOffersDTO.getDescription());
        assertEquals(auction.getEndDate(), auctionWithOffersDTO.getEndDate());
        assertEquals(auction.getNumberOfInstallments(), auctionWithOffersDTO.getNumberOfInstallments());
    }
}
