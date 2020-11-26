package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;

import java.util.List;

public interface LoanService {
    LoanDTO acceptOffer(LoanService.Command.AcceptOffer acceptOffer);
    void payNextInstallment(LoanService.Command.PayNextInstallment payNextInstallment);
    List<LoanDTO> getLoansByBorrower();

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
