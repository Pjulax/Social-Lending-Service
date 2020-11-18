package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;

import java.time.Clock;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final AuctionRepository auctionRepository;
    private final UserService userService;
    private final OfferRepository offerRepository;
    private final LoanRepository loanRepository;
    private final UserRepository userRepository;
    private final Clock clock;

    @Override
    public Loan acceptOffer(LoanService.Command.AcceptOffer acceptOffer) {
        Auction auction = auctionRepository.findById(acceptOffer.getAuctionId()).orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));
        if(!userService.whoami().getAuctions().contains(auction))
            throw new IllegalArgumentException("Auction doesn't belong to logged user");
        Offer offer = offerRepository.findById(acceptOffer.getOfferId()).orElseThrow(() -> new NoSuchElementException("Offer with that id doesn't exist"));
        if(!offer.getAuction().getId().equals(auction.getId()))
            throw new IllegalArgumentException("Offer wasn't placed to provided auction");
        User lender = userRepository.findByOffer(offer).orElseThrow();
        Loan loan = Loan.builder()
                .borrower(userService.whoami())
                .lender(lender)
                .acceptedInterest(offer.getAnnualPercentageRate())
                .startDate(new Date(clock.millis()))//TODO later we can chose date, right now it is set since now
                .takenAmount(auction.getLoanAmount().doubleValue())
                .installments(createInstallments(new Date(clock.millis()), auction.getLoanAmount().doubleValue(), offer.getAnnualPercentageRate(), auction.getNumberOfInstallments()))
                .build();
       return loanRepository.save(loan);
    }

    @Override
    public List<Loan> getLoansByBorrower() {
        return loanRepository.findAllByBorrower(userService.whoami());
    }

    private List<Installment> createInstallments(Date startDate, Double takenAmount, Double acceptedInterest, Integer numberOfInstallments) {
        return List.of(); // TODO
    }

}
