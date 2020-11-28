package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountNameRequestEntity {
    private String name;
}
