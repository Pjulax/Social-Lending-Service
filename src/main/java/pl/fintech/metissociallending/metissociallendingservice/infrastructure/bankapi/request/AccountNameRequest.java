package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountNameRequest {
    private final String name;
}
