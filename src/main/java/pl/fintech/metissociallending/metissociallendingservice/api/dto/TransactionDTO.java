package pl.fintech.metissociallending.metissociallendingservice.api.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    @JsonIgnore
    private Long id;
    private Long index;
    private String type;
    private Double amount;
    @JsonIgnore
    private String referenceId;
    private Date timestamp;
}