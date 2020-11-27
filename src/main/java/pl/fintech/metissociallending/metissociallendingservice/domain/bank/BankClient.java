package pl.fintech.metissociallending.metissociallendingservice.domain.bank;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.requestEntity.AccountEntityRequest;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.requestEntity.MyAccountRequestBody;

@FeignClient(name="bank", url = "https://hltechbank.thebe-team.sit.fintechchallenge.pl/")
public interface BankClient {

    @PostMapping("accounts")
    ResponseEntity<Void> accounts(@RequestHeader("Authorization") String header, AccountEntityRequest accountEntityRequest);

    @GetMapping("accounts/{account_number}")
    ResponseEntity<AccountDTO> myAccount(@RequestHeader("Authorization") String header, @PathVariable(name = "account_number") String account_number);

    @PostMapping("payments")
    ResponseEntity<Void> payment(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequestBody myAccountRequestBody);

    @PostMapping("withdrawals")
    ResponseEntity<Void> withdraw(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequestBody myAccountRequestBody);
}
