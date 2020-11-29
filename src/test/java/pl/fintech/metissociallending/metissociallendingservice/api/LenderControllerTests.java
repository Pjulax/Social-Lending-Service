package pl.fintech.metissociallending.metissociallendingservice.api;

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
import pl.fintech.metissociallending.metissociallendingservice.api.dto.LoanDTO;
import pl.fintech.metissociallending.metissociallendingservice.api.dto.SubmitOfferDTO;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.Auction;
import pl.fintech.metissociallending.metissociallendingservice.domain.borrower.loan.LoanService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.LenderService;
import pl.fintech.metissociallending.metissociallendingservice.domain.lender.Offer;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureJdbc
@AutoConfigureMockMvc
public class LenderControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LenderController lenderController;

    @MockBean
    private LenderService lenderService;

    @MockBean
    private LoanService loanService;

    @Test
    public void contextLoads() {
        assertThat(lenderController).isNotNull();
    }


    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldSubmitOffer(){
        SubmitOfferDTO submitOfferDTO = new SubmitOfferDTO(1L, 0.2);
        Offer offer = new Offer(1L, Auction.builder().build(), User.builder().build(), new Date(1606668952),0.2);
        when(lenderService.submitOffer(any())).thenReturn(offer);
        ObjectMapper mapper = new ObjectMapper();

        ResultActions resultActions = this.mockMvc.perform(post("/api/lender/offers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(submitOfferDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONObject obj = new JSONObject(contentAsString);
        Double amountLeft = obj.getDouble("annualPercentageRate");
        assertEquals(offer.getAnnualPercentageRate(), amountLeft);
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldLenderGetOffers(){
        Offer offer1 = Offer.builder().annualPercentageRate(0.2d).build();
        Offer offer2 = Offer.builder().annualPercentageRate(0.3d).build();
        List<Offer> offers = List.of(offer1, offer2);
        when(lenderService.getAllOffers()).thenReturn(offers);

        ResultActions resultActions = this.mockMvc.perform(get("/api/lender/auctions"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONArray auctionsResponse = new JSONArray(contentAsString);
        for(int i = 0; i<auctionsResponse.length();i++){
            JSONObject obj = auctionsResponse.getJSONObject(i);
            assertEquals(offers.get(i).getAnnualPercentageRate(), obj.getDouble("annualPercentageRate"));
        }

    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldCancelOffer(){
        doNothing().when(lenderService).cancelOffer(any());
        this.mockMvc.perform(delete("/api/lender/offers/1"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldGetAvailableAuctions(){
        Auction auction1 = Auction.builder().loanAmount(BigDecimal.valueOf(89d)).description("First Auction").build();
        Auction auction2 = Auction.builder().loanAmount(BigDecimal.valueOf(120d)).description("First Auction").build();
        List<Auction> auctions = List.of(auction1, auction2);
        when(lenderService.getAllAvailableAuctions()).thenReturn(auctions);

        ResultActions resultActions = this.mockMvc.perform(get("/api/lender/auctions"))
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = resultActions.andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        JSONArray auctionsResponse = new JSONArray(contentAsString);
        for(int i = 0; i<auctionsResponse.length();i++){
            JSONObject obj = auctionsResponse.getJSONObject(i);
            assertEquals(auctions.get(i).getLoanAmount().doubleValue(), obj.getDouble("loanAmount"));
            assertEquals(auctions.get(i).getDescription(), obj.getString("description"));
        }

    }


    @Test
    @SneakyThrows
    @WithMockUser(username = "user", password = "name", roles = {"CLIENT"})
    public void shouldGetInvestments(){
        List<LoanDTO> loanDTO;
        LoanDTO loanDTO1 = LoanDTO.builder().amountLeft(100d).build();
        LoanDTO loanDTO2 = LoanDTO.builder().amountLeft(120d).build();
        loanDTO = List.of(loanDTO1, loanDTO2);
        when(loanService.getLoansByBorrower()).thenReturn(loanDTO);

        ResultActions resultActions = this.mockMvc.perform(get("/api/lender/investments"))
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


}
