package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import io.micrometer.core.instrument.config.validate.InvalidReason;
import io.micrometer.core.instrument.config.validate.Validated;
import io.micrometer.core.instrument.config.validate.ValidationException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.fintech.metissociallending.metissociallendingservice.api.exception.LengthExceededException;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
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
    @Min(3)
    @Max(255)
    private String description;

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
            throw new ValidationException(Validated.invalid("Auction end date", endDate, " must be in format 'dd/MM/yyyy HH:mm'", InvalidReason.MALFORMED));
        }
        return date;
    }

    @Override
    public Integer getNumberOfInstallments() {
        return numberOfInstallments;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
