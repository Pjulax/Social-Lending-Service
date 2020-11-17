package pl.fintech.metissociallending.metissociallendingservice.domain.bank;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.requestEntity.AccountEntityRequest;

@FeignClient(name="bank", url = "https://hltechbank.thebe-team.sit.fintechchallenge.pl/")
public interface BankClient {

    @PostMapping("accounts")
    ResponseEntity<Void> accounts(@RequestHeader("Authorization") String header, AccountEntityRequest accountEntityRequest);
}
