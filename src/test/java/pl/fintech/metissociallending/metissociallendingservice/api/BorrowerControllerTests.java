package pl.fintech.metissociallending.metissociallendingservice.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJdbc
@AutoConfigureMockMvc
public class BorrowerControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BorrowerController borrowerController;

    @Test
    public void contextLoads() {
        assertThat(borrowerController).isNotNull();
    }

    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @Test
    public void whenRequestHelloBorrowerItAsksWithHello() throws Exception{
        this.mockMvc.perform(get("/api/borrower/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome!")));

    }
}
