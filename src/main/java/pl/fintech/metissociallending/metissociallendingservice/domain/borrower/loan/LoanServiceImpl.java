package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
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
    private final Clock clock;

    @Override
    public Loan acceptOffer(LoanService.Command.AcceptOffer acceptOffer) {
        Auction auction = findAuction(acceptOffer);
        Offer offer = findOffer(acceptOffer, auction);
        return createLoan(auction, offer);
    }

    private Loan createLoan(Auction auction, Offer offer) {
        User lender = offer.getLender();
        Loan loan = Loan.builder()
                .borrower(userService.whoami())
                .lender(lender)
                .acceptedInterest(offer.getAnnualPercentageRate())
                .startDate(new Date(clock.millis()))//TODO later we can chose date, right now it is set since now
                .takenAmount(auction.getLoanAmount().doubleValue())
                .installments(createInstallmentsSchedule(new Date(clock.millis()), auction.getLoanAmount().doubleValue(), offer.getAnnualPercentageRate(), auction.getNumberOfInstallments()))
                .build();
        return loanRepository.save(loan);
    }

    private Offer findOffer(Command.AcceptOffer acceptOffer, Auction auction) {
        Offer offer = offerRepository.findById(acceptOffer.getOfferId()).orElseThrow(() -> new NoSuchElementException("Offer with that id doesn't exist"));
        if(!offer.getAuction().getId().equals(auction.getId()))
            throw new IllegalArgumentException("Offer wasn't placed to provided auction");
        return offer;
    }

    private Auction findAuction(@NonNull Command.AcceptOffer acceptOffer) {
        Auction auction = auctionRepository.findById(acceptOffer.getAuctionId()).orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));

        if(auctionRepository.findByIdAndBorrower(auction.getId(), userService.whoami()).isEmpty())
            throw new IllegalArgumentException("Auction doesn't belong to logged user");
        return auction;
    }

    @Override
    public List<Loan> getLoansByBorrower() {
        return loanRepository.findAllByBorrower(userService.whoami());
    }

    private List<Installment> createInstallmentsSchedule(Date startDate, Double takenAmount, Double acceptedInterest, Integer numberOfInstallments) {
        if(numberOfInstallments < 1)
            return List.of();
        Calendar loanStartDate = Calendar.getInstance();
        loanStartDate.setTime(startDate);
        BigDecimal totalAmountToPayForLoan = BigDecimal.valueOf(takenAmount);
        BigDecimal baseAmount = totalAmountToPayForLoan.divide(BigDecimal.valueOf(numberOfInstallments),2, RoundingMode.HALF_UP);
        BigDecimal interestAmount = baseAmount.multiply(BigDecimal.valueOf(acceptedInterest)).setScale(2,RoundingMode.HALF_UP);
        BigDecimal totalAmount = baseAmount.add(interestAmount).setScale(2,RoundingMode.HALF_UP);
        totalAmountToPayForLoan = BigDecimal.ONE.add(BigDecimal.valueOf(acceptedInterest)).multiply(totalAmountToPayForLoan);
        return calculateInstallmentsSchedule(numberOfInstallments, loanStartDate, totalAmountToPayForLoan, baseAmount, interestAmount, totalAmount);
    }

    private List<Installment> calculateInstallmentsSchedule(Integer numberOfInstallments, Calendar paymentDate, BigDecimal totalAmountToPayForLoan, BigDecimal baseAmount, BigDecimal interestAmount, BigDecimal totalAmount) {
        List<Installment> installments = new LinkedList<>();
        BigDecimal leftAmountToPayForLoan = totalAmountToPayForLoan;
        for(long index = 0; index < numberOfInstallments; index++){
            paymentDate.add(Calendar.MONTH, 1);
            leftAmountToPayForLoan = leftAmountToPayForLoan.subtract(totalAmount);
            if(index == numberOfInstallments-1){
                baseAmount = baseAmount.add(leftAmountToPayForLoan);
                totalAmount = totalAmount.add(leftAmountToPayForLoan);
                leftAmountToPayForLoan = BigDecimal.ZERO;
            }
            Installment installment = createInstallment(index, paymentDate.getTime(), leftAmountToPayForLoan, baseAmount, interestAmount, totalAmount);
            installments.add(installment);
        }
        return installments;
    }

    private Installment createInstallment(long index, Date paymentDate, BigDecimal leftAmountToPayForLoan, BigDecimal baseAmount, BigDecimal interestAmount, BigDecimal totalAmount) {
        Installment installment = Installment.builder()
                .index(index)
                .due(paymentDate)
                .amount(baseAmount)
                .interest(interestAmount)
                .fine(BigDecimal.ZERO)
                .total(totalAmount)
                .left(leftAmountToPayForLoan)
                .status(InstallmentStatus.WAITING)
                .build();
        installment = installmentRepository.save(installment);
        return installment;
    }

}