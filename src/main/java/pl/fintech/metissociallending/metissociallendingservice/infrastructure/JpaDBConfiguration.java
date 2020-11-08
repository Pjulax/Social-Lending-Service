package pl.fintech.metissociallending.metissociallendingservice.infrastructure;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackageClasses = {}, considerNestedRepositories = true)
@EntityScan(basePackageClasses = {})
public class JpaDBConfiguration {
//    @Bean
//    JpaMyOrderRepositoryImpl myOrderRepository(JpaMyOrderRepositoryImpl.JpaOrderRepo jpaOrderRepo) {
//        return new JpaMyOrderRepositoryImpl(jpaOrderRepo);
//    }
}
