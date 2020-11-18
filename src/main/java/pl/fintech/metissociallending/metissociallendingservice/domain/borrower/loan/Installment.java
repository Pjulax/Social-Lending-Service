package pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode(of = "id")
@RequiredArgsConstructor
public class Installment {
    private final Long id;
}
