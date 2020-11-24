package pl.fintech.metissociallending.metissociallendingservice.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.Role;
import pl.fintech.metissociallending.metissociallendingservice.domain.user.User;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JpaUserRepositoryImplTests {

    @Autowired
    JpaUserRepositoryImpl jpaUserRepository;

    @Test
    @Order(1)
    public void contextLoads() {
        assertThat(jpaUserRepository).isNotNull();
    }

    @Test
    @Order(2)
    public void whenNotAdded_thenRepositoryIsEmpty() {
        List<User> users = jpaUserRepository.findAll();
        assertThat(users).isEmpty();
    }

    @Test
    @Order(3)
    public void whenUserSaved_thenFindsByName() {
        LinkedList<Role> roles = new LinkedList<Role>();
        roles.add(Role.ROLE_CLIENT);
        User userBefore = User.builder()
                .username("example")
                .password("password")
                .account("aaaa-bbbb-cccc-dddd")
                .balance(0.0d)
                .roles(roles)
                .build();
        jpaUserRepository.save(userBefore);
        User userAfter = jpaUserRepository.findByUsername("example").orElseThrow(() -> new IllegalArgumentException("Not found user with name 'example'"));
        assertThat(userAfter.getUsername()).isEqualTo("example");
        assertThat(userAfter.getPassword()).isEqualTo("password");
        assertThat(userAfter.getAccount()).isEqualTo("aaaa-bbbb-cccc-dddd");
        assertThat(userAfter.getBalance()).isEqualTo(0.0d);
        assertThat(userAfter.getRoles()).isEqualTo(roles);
    }

    @Test
    @Order(4)
    public void whenUserSaved_thenFindsById() {
        LinkedList<Role> roles = new LinkedList<Role>();
        roles.add(Role.ROLE_CLIENT);
        User userBefore = User.builder()
                .username("example2")
                .password("password")
                .account("aaaa-bbbb-cccc-dddd")
                .balance(0.0d)
                .roles(roles)
                .build();
        userBefore = jpaUserRepository.save(userBefore);
        User userAfter = jpaUserRepository.findById(userBefore.getId()).orElseThrow(() -> new IllegalArgumentException("Not found user with name 'example2'"));
        assertThat(userAfter.getUsername()).isEqualTo("example2");
        assertThat(userAfter.getPassword()).isEqualTo("password");
        assertThat(userAfter.getAccount()).isEqualTo("aaaa-bbbb-cccc-dddd");
        assertThat(userAfter.getBalance()).isEqualTo(0.0d);
        assertThat(userAfter.getRoles()).isEqualTo(roles);
    }

    @Test
    @Order(5)
    public void whenNoUserSaved_thenFindByIdGivesEmpty() {
            assertThat(jpaUserRepository.findById(1L)).isEmpty();
    }

    @Test
    @Order(6)
    public void whenIdGivenIsNull_thenThrowsIllegalArgumentException() {
        try{
            jpaUserRepository.findById(null);
        }
        catch (Exception ex) {
            assertThat(ex).hasRootCauseExactlyInstanceOf(IllegalArgumentException.class);
        }
    }
}