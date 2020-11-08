package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {JpaBorrowerRepositoryImpl.class, JpaAuctionRepositoryImpl.class}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {JpaBorrowerRepositoryImpl.class, JpaAuctionRepositoryImpl.class})
public class JpaDBConfiguration {

    @Bean
    JpaAuctionRepositoryImpl auctionRepository(JpaAuctionRepositoryImpl.JpaAuctionRepo jpaAuctionRepo){
        return new JpaAuctionRepositoryImpl(jpaAuctionRepo);
    }
    @Bean
    JpaBorrowerRepositoryImpl borrowerRepository(JpaBorrowerRepositoryImpl.JpaBorrowerRepo jpaBorrowerRepo){
        return  new JpaBorrowerRepositoryImpl(jpaBorrowerRepo);
    }
}
