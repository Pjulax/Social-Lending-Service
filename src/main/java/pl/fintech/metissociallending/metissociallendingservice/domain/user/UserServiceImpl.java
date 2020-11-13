package pl.fintech.metissociallending.metissociallendingservice.domain.user;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.fintech.metissociallending.metissociallendingservice.api.exception.ExistingObjectException;
import pl.fintech.metissociallending.metissociallendingservice.infrastructure.security.jwt.JwtTokenProvider;

import java.util.LinkedList;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public User createUser(UserService.Command.CreateUser createUserCommand) {
        if(userRepository.findByUsername(createUserCommand.getUsername()).isPresent())
            throw new ExistingObjectException("User with that username already exists");
        LinkedList<Role> roles = new LinkedList<Role>();
        roles.add(Role.ROLE_CLIENT);
        User user = User.builder().username(createUserCommand.getUsername())
                                  .password(passwordEncoder.encode(createUserCommand.getPassword()))
                                  .roles(roles).build();
        return userRepository.save(user);
    }
    @Override
    public String login(Query.Login login) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login.getUsername(), login.getPassword()));
        return jwtTokenProvider.createToken(login.getUsername(), userRepository.findByUsername(login.getUsername()).get().getRoles());
    }
    public User search(Query.Search searchQuery) {
        return userRepository.findByUsername(searchQuery.getUsername()).orElseThrow();
    }
    @Override
    public User whoami(){
        return search(()->SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
