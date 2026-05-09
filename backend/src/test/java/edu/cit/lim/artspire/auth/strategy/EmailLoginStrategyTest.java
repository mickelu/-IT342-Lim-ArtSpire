package edu.cit.lim.artspire.auth.strategy;

import edu.cit.lim.artspire.TestData;
import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.user.User;
import edu.cit.lim.artspire.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

class EmailLoginStrategyTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final EmailLoginStrategy strategy = new EmailLoginStrategy(userRepository, encoder);

    @Test
    void login_whenPasswordMatches_returnsUser() {
        LoginRequest request = TestData.validLoginRequest();
        User expectedUser = TestData.user(encoder.encode(TestData.VALID_PASSWORD));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(expectedUser));

        User user = strategy.login(request);

        assertThat(user).isSameAs(expectedUser);
    }

    @Test
    void login_whenUserIsMissing_throwsUnauthorized() {
        LoginRequest request = TestData.validLoginRequest();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> strategy.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode", "reason")
                .containsExactly(UNAUTHORIZED, "Invalid credentials");
    }

    @Test
    void login_whenPasswordIsInvalid_throwsUnauthorized() {
        LoginRequest request = TestData.validLoginRequest();
        User user = TestData.user(encoder.encode("not-the-password"));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> strategy.login(request))
                .isInstanceOf(ResponseStatusException.class)
                .extracting("statusCode", "reason")
                .containsExactly(UNAUTHORIZED, "Invalid credentials");
    }
}
