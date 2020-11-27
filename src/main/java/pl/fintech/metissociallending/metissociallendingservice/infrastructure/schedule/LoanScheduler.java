package pl.fintech.metissociallending.metissociallendingservice.infrastructure.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;

@Component
@RequiredArgsConstructor
public class LoanScheduler {
    private final LoanService loanService;

    @Scheduled(cron="*/10 * * * * *")
    private void updateLoans(){
        loanService.updateLoansStatus();
    }

}
