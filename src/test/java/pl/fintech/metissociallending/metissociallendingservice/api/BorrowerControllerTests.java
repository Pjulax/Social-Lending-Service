package pl.fintech.metissociallending.metissociallendingservice.api;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.AuctionDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.BorrowerService;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @MockBean
    private BorrowerService borrowerService;

    @MockBean
    private LoanService loanService;

    @Test
    public void contextLoads() {
        assertThat(borrowerController).isNotNull();
    }

    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    @Test
    @SneakyThrows
    public void whenRequestHelloBorrowerItAsksWithHello() {
        this.mockMvc.perform(get("/api/borrower/hello"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Welcome!")));

    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldCreateNewAuction() {
        AuctionDTO auctionDTO = new AuctionDTO();
        auctionDTO.setNumberOfInstallments(24);
        auctionDTO.setLoanAmount(1200d);
        auctionDTO.setEndDate("10/10/2021 20:30");
        ObjectMapper mapper = new ObjectMapper();

        when(borrowerService.createNewAuctionSinceNow(auctionDTO)).thenReturn(Auction.builder().build());

        this.mockMvc.perform(post("/api/borrower/auctions")
        .contentType(MediaType.APPLICATION_JSON)
        .content(mapper.writeValueAsString(auctionDTO)))
                .andExpect(status().isOk());
    }
    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldPayNextInstallment()  {
        doNothing().when(loanService).payNextInstallment(any());
        double amount = 100d;
        this.mockMvc.perform(post("/api/borrower/loans/1/pay-next-installment?amount="+amount))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldGetBorrowerLoans() {
        List<LoanDTO> loanDTO;
        LoanDTO loanDTO1 = LoanDTO.builder().amountLeft(100d).build();
        LoanDTO loanDTO2 = LoanDTO.builder().amountLeft(120d).build();
        loanDTO = List.of(loanDTO1, loanDTO2);
        when(loanService.getLoansByBorrower()).thenReturn(loanDTO);

        ResultActions resultActions = this.mockMvc.perform(get("/api/borrower/loans"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONArray loanDTOS = new JSONArray(contentAsString);
        for(int i = 0; i<loanDTOS.length();i++){
            JSONObject obj = loanDTOS.getJSONObject(i);
            assertEquals(loanDTO.get(i).getAmountLeft(), obj.getDouble("amountLeft"));
        }
    }
    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldAcceptOffer()  {
        LoanDTO loanDTO = LoanDTO.builder().id(1L).amountLeft(100d).build();
        when(loanService.acceptOffer(any())).thenReturn(loanDTO);
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);
        ResultActions resultActions = this.mockMvc.perform(post("/api/borrower/auctions/1/accept-offer?offer_id=1"))
                .andDo(print())
                .andExpect(status().isOk());
        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(contentAsString);
        Double amountLeft = obj.getDouble("amountLeft");
        assertEquals(loanDTO.getAmountLeft(), amountLeft);
    }

    @Test
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldGetBorrowerAuction() throws Exception {
        Auction auction1 = Auction.builder().loanAmount(BigDecimal.valueOf(89d)).description("First Auction").build();
        Auction auction2 = Auction.builder().loanAmount(BigDecimal.valueOf(120d)).description("First Auction").build();
        List<Auction> auctions = List.of(auction1, auction2);
        when(borrowerService.getAllAuctions()).thenReturn(auctions);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions resultActions = this.mockMvc.perform(get("/api/borrower/loans"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Auction[] auctionsResponse = mapper.readValue(contentAsString, Auction[].class);
        for(int i = 0; i<auctionsResponse.length;i++){
            assertEquals(auctions.get(i).getDescription(), auctionsResponse[i].getDescription());
            assertEquals(auctions.get(i).getLoanAmount(), auctionsResponse[i].getLoanAmount());
        }
    }
}
