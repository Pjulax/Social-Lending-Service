package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class Installment {
    private final Long id;
    private final Long index;
    private final Date due;
    private final BigDecimal amount; // base amount
    private final BigDecimal interest; // counted in value not percentage
    private BigDecimal fine;
    private BigDecimal total; // amount + interest + fine
    private BigDecimal left; // every month how much left from loan to payback
    private InstallmentStatus status;




    // changes to missed if installment isn't paid before due time
    public InstallmentStatus checkStatus(Date now){
        if(!status.equals(InstallmentStatus.PAID)) {
            if(now.getTime() > due.getTime()) {
               status = InstallmentStatus.MISSED;
            }
        }
        return status;
    }

    /**
     * pay first counts total and it changes status to PAID. Does nothing when is already PAID
     * @param now - time when we pay
     * @param fineInterest - annual fine interests count in rate ex 0.05 is 5%
     */
    public boolean pay(Date now, double fineInterest, double amount){
        if(!status.equals(InstallmentStatus.PAID)) {
            countTotal(now, fineInterest);
            if(total.setScale(2, RoundingMode.HALF_UP).doubleValue()==amount) {
                status = InstallmentStatus.PAID;
                return true;
            }
        }
        return false;
    }

    /**
     * Always is counted anew
     * @param now - difference between now and due is used to count fine
     * @param fineInterest - annual interests count in rate ex 0.05 is 5%
     */
    public void countTotal(Date now, double fineInterest){
        countFine(now,fineInterest);
        total = fine.add(interest).add(amount);
    }

    /**
     * Always is counted anew  equation: fine =
     * @param now - difference between now and due is used to count fine
     * @param fineInterests - annual fine interests count in rate ex 0.05 is 5%
     */
    public void countFine(Date now, double fineInterests){
        fine = countInterestValue(now, fineInterests);
    }

    //counts value of interests with given interest and difference between now and due

    private BigDecimal countInterestValue(Date now, double loaninterest){
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(now);
        Calendar calDue = Calendar.getInstance();
        calDue.setTime(due);
        int years = calNow.get(Calendar.YEAR)-calDue.get(Calendar.YEAR);
        int months = calNow.get(Calendar.MONTH)-calDue.get(Calendar.MONTH);
        // months or years have to be greater than 0 but no can be less than 0
        if(years>=0&&months>=0||years>0) {
            double timeInYears = (double) years + (months+1.0d) / 12;
            return amount.multiply(BigDecimal.valueOf(Math.pow((1+loaninterest),timeInYears))).subtract(amount);
        }
        return BigDecimal.ZERO;
    }

}
