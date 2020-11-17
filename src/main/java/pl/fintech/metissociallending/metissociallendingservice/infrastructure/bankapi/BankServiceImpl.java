package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi;
import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.fintech.metissociallending.metissociallendingservice.domain.bank.BankService;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final RestTemplate restTemplate;

    // TODO
    // 1.account should be stored as encrypted
    // 2.these three lines below have to be replaced by read from properties
    // 3.create encrypted credentials
    // 4.exception should be handle when server doesnt respond or error is sent
    final private String username = "";
    final private String password = "";
    final private String bankURL = "https://hltechbank.thebe-team.sit.fintechchallenge.pl/";

    @Override
    public String createAccount(String name) {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("Authorization", "Basic ZXhhbXBsZTpleGFtcGxl");
        headers.setContentType(MediaType.APPLICATION_JSON);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name",name);
        System.out.println(jsonObject.toString());
        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(),headers);

        return restTemplate.exchange(bankURL+"/accounts", HttpMethod.POST, entity, String.class)
                .getHeaders()
                .getLocation()
                .getPath()
                .substring("/accounts/".length());

    }
}