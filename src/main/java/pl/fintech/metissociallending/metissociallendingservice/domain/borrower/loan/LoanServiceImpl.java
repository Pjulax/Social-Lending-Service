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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.util.*;

@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final AuctionRepository auctionRepository;
    private final UserService userService;
    private final OfferRepository offerRepository;
    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
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
        if(numberOfInstallments < 1)
            return List.of();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        BigDecimal amountToPay = BigDecimal.valueOf(takenAmount);
        BigDecimal amountToPayMonthly = amountToPay.divide(BigDecimal.valueOf(numberOfInstallments),2, RoundingMode.HALF_UP);
        BigDecimal interestAmount = amountToPayMonthly.multiply(BigDecimal.valueOf(acceptedInterest)).setScale(2,RoundingMode.HALF_UP);
        BigDecimal totalAmount = amountToPayMonthly.add(interestAmount).setScale(2,RoundingMode.HALF_UP);
        amountToPay = amountToPay.multiply(BigDecimal.ONE.add(BigDecimal.valueOf(acceptedInterest)));
        List<Installment> installments = getInstallments(numberOfInstallments, calendar, amountToPay, amountToPayMonthly, interestAmount, totalAmount);
        return installments;
    }

    private List<Installment> getInstallments(Integer numberOfInstallments, Calendar calendar, BigDecimal amountToPay, BigDecimal amountToPayMonthly, BigDecimal interestAmount, BigDecimal totalAmount) {
        List<Installment> installments = new LinkedList<Installment>();
        for(long index = 0; index < numberOfInstallments; index++){
            calendar.add(Calendar.MONTH, 1);
            Installment installment = Installment.builder()
                    .index(index)
                    .due(calendar.getTime())
                    .amount(amountToPayMonthly)
                    .interest(interestAmount)
                    .fine(BigDecimal.ZERO)
                    .total(totalAmount)
                    .left(amountToPay.subtract(amountToPayMonthly.add(interestAmount).multiply(BigDecimal.valueOf(index+1))))
                    .status(InstallmentStatus.NOT_PAID)
                    .build();
            installment = installmentRepository.save(installment);
            installments.add(installment);
        }
        return installments;
    }

}