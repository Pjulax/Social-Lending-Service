package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;
import io.micrometer.core.instrument.config.validate.InvalidReason;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.AuctionRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.OfferRepository;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserService;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request.TransactionRequest;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.clock.Clock;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation of
 * @see LoanService
 */
@RequiredArgsConstructor
@Service
public class LoanServiceImpl implements LoanService {

    private final AuctionRepository auctionRepository;
    private final UserService userService;
    private final OfferRepository offerRepository;
    private final LoanRepository loanRepository;
    private final InstallmentRepository installmentRepository;
    private final Clock clock;
    private final BankService bankService;


    @Override
    public List<LoanDTO> getLoansByBorrower() {
        List<Loan> loans = loanRepository.findAllByBorrower(userService.whoami());
        if(loans.isEmpty())
            return List.of();
        return loans.stream().map(LoanDTO::fromDomain).collect(Collectors.toList());
    }

    @Override
    public List<LoanDTO> getAllInvestments() {
        List<Loan> loans = loanRepository.findAllByLender(userService.whoami());
        if(loans.isEmpty())
            return List.of();
        return loans.stream().map(LoanDTO::fromDomain).collect(Collectors.toList());
    }

    @Override
    public LoanDTO acceptOffer(LoanService.Command.AcceptOffer acceptOffer) {
        Auction auction = findAuction(acceptOffer);
        Offer offer = findOffer(acceptOffer, auction);
        transferMoneyToBorrower(auction,offer);
        auction.close();
        auction = auctionRepository.save(auction);
        return createLoan(auction, offer);
    }

    private Auction findAuction(@NonNull Command.AcceptOffer acceptOffer) {
        Auction auction = auctionRepository.findById(acceptOffer.getAuctionId())
                .orElseThrow(() -> new NoSuchElementException("Auction with that id doesn't exist"));
        if(auctionRepository.findByIdAndBorrower(auction.getId(), userService.whoami()).isEmpty())
            throw new IllegalArgumentException("Auction doesn't belong to logged user");
        return auction;
    }

    private Offer findOffer(Command.AcceptOffer acceptOffer, Auction auction) {
        Offer offer = offerRepository.findById(acceptOffer.getOfferId())
                .orElseThrow(() -> new NoSuchElementException("Offer with that id doesn't exist"));
        if(!offer.getAuction().getId().equals(auction.getId()))
            throw new IllegalArgumentException("Offer wasn't placed to provided auction");
        return offer;
    }

    private void transferMoneyToBorrower(Auction auction, Offer offer) {
        bankService.transfer(TransactionRequest.builder()
                .sourceAccountNumber(offer.getLender().getAccount())
                .targetAccountNumber(auction.getBorrower().getAccount())
                .amount(auction.getLoanAmount().doubleValue()).build());
    }

    private LoanDTO createLoan(Auction auction, Offer offer) {
        User lender = offer.getLender();
        Loan loan = Loan.builder()
                .borrower(userService.whoami())
                .lender(lender)
                .acceptedInterest(offer.getAnnualPercentageRate())
                .startDate(new Date(clock.millis()))
                .takenAmount(auction.getLoanAmount().doubleValue())
                .installments(createInstallmentsSchedule(
                        new Date(clock.millis()),
                        auction.getLoanAmount().doubleValue(),
                        offer.getAnnualPercentageRate(),
                        auction.getNumberOfInstallments()))
                .build();
        return LoanDTO.fromDomain(loanRepository.save(loan));
    }

    private List<Installment> createInstallmentsSchedule(Date startDate, Double takenAmount, Double acceptedInterest, Integer numberOfInstallments) {
        if(numberOfInstallments < 1)
            return List.of();
        Calendar loanStartDate = Calendar.getInstance();
        loanStartDate.setTime(startDate);
        BigDecimal baseAmountToPayForLoan = BigDecimal.valueOf(takenAmount).setScale(2,RoundingMode.HALF_UP);
        BigDecimal totalAmountRate = calculateTotalAmountRate(acceptedInterest, numberOfInstallments);
        BigDecimal baseAmount = baseAmountToPayForLoan.divide(BigDecimal.valueOf(numberOfInstallments),2, RoundingMode.HALF_UP);

        BigDecimal totalAmountToPayForLoan = baseAmountToPayForLoan.multiply(totalAmountRate).setScale(2,RoundingMode.HALF_UP);
        BigDecimal totalAmount = totalAmountToPayForLoan.divide(BigDecimal.valueOf(numberOfInstallments),2, RoundingMode.HALF_UP);
        BigDecimal interestAmount = totalAmount.subtract(baseAmount).setScale(2,RoundingMode.HALF_UP);

        return calculateInstallmentsSchedule(numberOfInstallments, loanStartDate, totalAmountToPayForLoan, baseAmount, interestAmount, totalAmount);
    }

    /**
     * This method is calculating rate, which is a multiplicand of base amount to calculate total loan and installment amount.
     * @param acceptedInterest      interest rate for loan
     * @param numberOfInstallments  number of installments for loan
     * @return                      BigDecimal multiplicand value with precision up to 12 decimal places
     */
    private BigDecimal calculateTotalAmountRate(Double acceptedInterest, int numberOfInstallments) {
        double totalAmountDouble = Math.pow((1+acceptedInterest),((double)numberOfInstallments/12));
        return BigDecimal.valueOf(totalAmountDouble).setScale(12,RoundingMode.HALF_UP);
    }

    /**
     * This method is creating list of scheduled installments using prepared constant values and calculating payment date and amount left to pay.
     * @param numberOfInstallments      number of installments for loan
     * @param paymentDate               base date when loan has started
     * @param totalAmountToPayForLoan   base and interest amount to pay for loan
     * @param baseAmount                base amount per installment
     * @param interestAmount            interest amount per installment
     * @param totalAmount               sum of base and interest amount per installment
     * @return                          List of scheduled installments
     */
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
                .status(InstallmentStatus.PENDING)
                .build();
        installment = installmentRepository.save(installment);
        return installment;
    }

    @Override
    public void payNextInstallment(LoanService.Command.PayNextInstallment payNextInstallment) {
        Optional<Loan> loanOptional = loanRepository.findByIdAndBorrower(payNextInstallment.getLoanId(), userService.whoami());
        if (loanOptional.isEmpty())
            throw new IllegalArgumentException("Loan is not found");
        Loan loan = loanOptional.get();
        List<Installment> installments = loan.getInstallments();
        Installment nextInstallment = null;
        for (Installment installment : installments) {
            if (!installment.getStatus().equals(InstallmentStatus.PAID)) {
                nextInstallment = installment;
                break;
            }
        }
        if (nextInstallment == null)
            throw new NoSuchElementException("There is no next installment to pay");
        boolean canBePaid = nextInstallment.isGivenAmountEqualToInstallmentAmount(new Date(clock.millis()), loan.getAcceptedInterest(), payNextInstallment.getAmount());
        if (canBePaid) {
            bankService.transfer(TransactionRequest.builder()
                    .sourceAccountNumber(loan.getBorrower().getAccount())
                    .targetAccountNumber(loan.getLender().getAccount())
                    .amount(nextInstallment.getTotal().doubleValue()).build());
            nextInstallment.changeToPaid();
        } else
            throw new ValidationException(Validated.invalid(
                    "Amount",
                    payNextInstallment.getAmount(),
                    (" invalid amount to pay actual is " + nextInstallment.getTotal().setScale(2, RoundingMode.HALF_UP).toString()),
                    InvalidReason.MALFORMED));
        installmentRepository.save(nextInstallment);
    }

    private void checkStatus(Loan loan){
        List<Installment> installments = loan.getInstallments();
        for (Installment installment : installments) {
            installment.checkStatus(new Date(clock.millis()));
            installmentRepository.save(installment);
        }
    }

    /**
     *Executed every time user wants to see his loan installments or pay for installment in order to validate current price
     */
    private void countTotal(Loan loan){ // totals = fines and interests
        List<Installment> installments = loan.getInstallments();
        for (Installment installment : installments) {
            if(!installment.getStatus().equals(InstallmentStatus.PAID))
                installment.countTotal(new Date(clock.millis()), loan.getAcceptedInterest());
            installmentRepository.save(installment);
        }
    }

    @Override
    public void updateLoansStatus(){
        List<Loan> loans = loanRepository.findAll();
        for (Loan loan : loans) {
            updateLoanStatus(loan);
        }
    }

    private void updateLoanStatus(Loan loan){
        checkStatus(loan);
        countTotal(loan);
        loan.calculateLeft();
        loanRepository.save(loan);
    }

}