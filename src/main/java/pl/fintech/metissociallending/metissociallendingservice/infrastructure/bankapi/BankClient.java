package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import pl.fintech.metissociallending.metissociallendingservice.api.dto.AccountDTO;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi.entity.*;

@FeignClient(name="bank", url = "https://hltechbank.thebe-team.sit.fintechchallenge.pl/")
public interface BankClient {

    @PostMapping("transactions")
    ResponseEntity<Void> createTransaction(@RequestHeader("Authorization") String header, @RequestBody TransactionRequestEntity transactionRequestEntity);

    @PostMapping("payments")
    ResponseEntity<Void> deposit(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequestEntity myAccountRequestEntity);

    @PostMapping("withdrawals")
    ResponseEntity<Void> withdraw(@RequestHeader("Authorization") String header, @RequestBody MyAccountRequestEntity myAccountRequestEntity);

    @GetMapping("accounts/{account_number}")
    ResponseEntity<AccountDTO> getAccountData(@RequestHeader("Authorization") String header, @PathVariable(name = "account_number") String account_number);

    @PostMapping("accounts")
    ResponseEntity<Void> createAccount(@RequestHeader("Authorization") String header, @RequestBody AccountNameRequestEntity accountNameRequestEntity);
}