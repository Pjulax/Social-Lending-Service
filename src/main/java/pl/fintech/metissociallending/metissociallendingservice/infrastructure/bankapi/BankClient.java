package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.request.*;

@FeignClient(name="bank", url = "https://hltechbank.thebe-team.sit.fintechchallenge.pl/")
public interface BankClient {

    @PostMapping("transactions")
    ResponseEntity<Void> createTransaction(@RequestHeader("Authorization") String header, @RequestBody TransactionRequest transactionRequest);

    @PostMapping("payments")
    ResponseEntity<Void> deposit(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequest myAccountRequest);

    @PostMapping("withdrawals")
    ResponseEntity<Void> withdraw(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequest myAccountRequest);

    @GetMapping("accounts/{account_number}")
    ResponseEntity<AccountDTO> getAccountData(@RequestHeader("Authorization") String header, @PathVariable(name = "account_number") String account_number);

    @PostMapping("accounts")
    ResponseEntity<Void> createAccount(@RequestHeader("Authorization") String header, @RequestBody AccountNameRequest accountNameRequest);
}