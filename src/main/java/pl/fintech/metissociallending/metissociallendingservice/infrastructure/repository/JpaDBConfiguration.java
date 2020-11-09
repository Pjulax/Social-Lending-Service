package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {JpaUserRepositoryImpl.class, JpaAuctionRepositoryImpl.class, JpaOfferRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {JpaUserRepositoryImpl.class, JpaAuctionRepositoryImpl.class, JpaOfferRepositoryImpl.class})
public class JpaDBConfiguration {

    @Bean
    JpaAuctionRepositoryImpl auctionRepository(JpaAuctionRepositoryImpl.JpaAuctionRepo jpaAuctionRepo){
        return new JpaAuctionRepositoryImpl(jpaAuctionRepo);
    }
    @Bean
    JpaUserRepositoryImpl userRepository(JpaUserRepositoryImpl.JpaUserRepo jpaUserRepo){
        return new JpaUserRepositoryImpl(jpaUserRepo);
    }
    @Bean
    JpaOfferRepositoryImpl jpaOfferRepository(JpaOfferRepositoryImpl.JpaOfferRepo jpaOfferRepo){
        return new JpaOfferRepositoryImpl(jpaOfferRepo);
    }
}
