package pl.fintech.metissociallending.metissociallendingservice.infrastructure.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;

/**
 * Every 10 seconds system checks if installment
 * is missed and calculates fines.
 * Scheduler also checks whether auction is closed.
 */
@Component
@RequiredArgsConstructor
public class LoanScheduler {
    private final LoanService loanService;
    private final BorrowerService borrowerService;

    @Scheduled(cron="*/10 * * * * *")
    private void updateLoans(){
        loanService.updateLoansStatus();
    }

    @Scheduled(cron="*/10 * * * * *")
    private void updateAllAuctionsStatus(){
        borrowerService.updateAllAuctionStatus();
    }

}
