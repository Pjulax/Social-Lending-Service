package pl.fintech.metissociallending.metissociallendingservice.infrastructure.bankapi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Base64Utils;

@Configuration
public class BasicAuthHeaderConfig {

    @Bean
    public String basicAuthHeader(){
        String username = "example";
        String password = "example";
        byte[] encodedBytes = Base64Utils.encode((username + ":" + password).getBytes());
        return "Basic " + new String(encodedBytes);
    }

}
