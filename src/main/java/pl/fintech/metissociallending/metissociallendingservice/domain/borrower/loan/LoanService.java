package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import java.util.List;
/**
 * Borrower can accept offer, pay for next installment,
 * get his all loans and user can get his investments
 * service updates loans status to check whether
 * are missed or to calculate fines
 * @see LoanServiceImpl
 */
public interface LoanService {
    LoanDTO acceptOffer(LoanService.Command.AcceptOffer acceptOffer);
    void payNextInstallment(LoanService.Command.PayNextInstallment payNextInstallment);
    List<LoanDTO> getLoansByBorrower();
    List<LoanDTO> getAllInvestments();

    void updateLoansStatus();

    interface Command {
        interface AcceptOffer extends LoanService.Command {
            Long getAuctionId();
            Long getOfferId();
        }
        interface PayNextInstallment extends  LoanService.Command {
            Long getLoanId();
            Double getAmount();
        }
    }
}
