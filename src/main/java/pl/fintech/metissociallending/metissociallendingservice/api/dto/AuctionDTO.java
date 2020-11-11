package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
public class AuctionDTO implements BorrowerService.Command.CreateNewAuctionSinceNow {
    private Double loanAmount;
    private String endDate;
    private Integer numberOfInstallments;

    @Override
    public BigDecimal getLoanAmount() {
        return BigDecimal.valueOf(loanAmount);
    }

    @Override
    public Date getEndDate() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("dd/MM/yyyy HH:mm").parse(endDate);
        }catch (ParseException e){
            System.out.println(e);
        }
        return date;
    }

    @Override
    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

}
