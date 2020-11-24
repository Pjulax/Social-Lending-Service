package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@Transactional()
public class JpaUserRepositoryImplTests {

    @Autowired
    UserRepository jpaUserRepository;

    @Test
    public void contextLoads() {
        assertThat(jpaUserRepository).isNotNull();
    }

    @Test
    public void should_find_no_users_if_repository_is_empty() {
        List<User> users = jpaUserRepository.findAll();
        assertThat(users).isEmpty();
    }

}