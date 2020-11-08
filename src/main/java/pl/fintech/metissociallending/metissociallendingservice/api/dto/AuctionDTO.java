package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuctionDTO {
    private Double loanAmount;
    private String endDate;
    private String beginLoanDate;
    private String endLoanDate;
    private Double installmentsFrequencyInYears;
    private Long borrowerId;
}
