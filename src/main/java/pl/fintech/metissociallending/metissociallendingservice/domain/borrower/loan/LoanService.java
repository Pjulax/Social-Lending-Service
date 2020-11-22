package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import java.util.List;

public interface LoanService {
    Loan acceptOffer(LoanService.Command.AcceptOffer acceptOffer);

    List<Loan> getLoansByBorrower();

    interface Command {
        interface AcceptOffer extends LoanService.Command {
            Long getAuctionId();
            Long getOfferId();
        }
    }
}
