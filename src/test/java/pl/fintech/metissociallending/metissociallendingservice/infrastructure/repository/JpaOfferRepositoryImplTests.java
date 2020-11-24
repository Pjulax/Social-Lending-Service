package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
public class JpaOfferRepositoryImplTests {

    @Autowired
    JpaOfferRepositoryImpl jpaOfferRepository;

    @Test
    @Order(1)
    public void contextLoads() {
        assertThat(jpaOfferRepository).isNotNull();
    }
}
