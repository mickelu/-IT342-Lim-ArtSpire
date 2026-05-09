package edu.cit.lim.artspire.auth;

import edu.cit.lim.artspire.TestData;
import edu.cit.lim.artspire.auth.dto.AuthResponse;
import edu.cit.lim.artspire.auth.dto.LoginRequest;
import edu.cit.lim.artspire.auth.dto.RegisterRequest;
import edu.cit.lim.artspire.user.User;
import edu.cit.lim.artspire.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
    }

    @Test
    void register_whenEmailIsNew_savesEncodedUserAndReturnsSuccessResponse() {
        RegisterRequest request = TestData.validRegisterRequest();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getName()).isEqualTo(TestData.VALID_NAME);
        assertThat(savedUser.getEmail()).isEqualTo(TestData.VALID_EMAIL);
        assertThat(savedUser.getPassword()).isNotEqualTo(TestData.VALID_PASSWORD);
        assertThat(encoder.matches(TestData.VALID_PASSWORD, savedUser.getPassword())).isTrue();
        assertThat(savedUser.getCreatedAt()).isNotNull();

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("User registered successfully");
        assertThat(response.getName()).isEqualTo(TestData.VALID_NAME);
        assertThat(response.getEmail()).isEqualTo(TestData.VALID_EMAIL);
    }

    @Test
    void register_whenEmailAlreadyExists_throwsBadRequestAndDoesNotSave() {
        RegisterRequest request = TestData.validRegisterRequest();
        when(userRepository.findByEmail(request.getEmail()))
                .thenReturn(Optional.of(TestData.user("encoded")));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.register(request))
                .satisfies(exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(BAD_REQUEST);
                    assertThat(exception.getReason()).isEqualTo("Email already exists");
                });

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_whenCredentialsAreValid_returnsSuccessResponse() {
        LoginRequest request = TestData.validLoginRequest();
        User user = TestData.user(encoder.encode(TestData.VALID_PASSWORD));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        AuthResponse response = authService.login(request);

        assertThat(response.isSuccess()).isTrue();
        assertThat(response.getMessage()).isEqualTo("Login successful");
        assertThat(response.getName()).isEqualTo(TestData.VALID_NAME);
        assertThat(response.getEmail()).isEqualTo(TestData.VALID_EMAIL);
    }

    @Test
    void login_whenEmailDoesNotExist_throwsUnauthorized() {
        LoginRequest request = TestData.validLoginRequest();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.login(request))
                .satisfies(exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(UNAUTHORIZED);
                    assertThat(exception.getReason()).isEqualTo("Invalid credentials");
                });
    }

    @Test
    void login_whenPasswordDoesNotMatch_throwsUnauthorized() {
        LoginRequest request = TestData.validLoginRequest();
        User user = TestData.user(encoder.encode("different-password"));
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));

        assertThatExceptionOfType(ResponseStatusException.class)
                .isThrownBy(() -> authService.login(request))
                .satisfies(exception -> {
                    assertThat(exception.getStatusCode()).isEqualTo(UNAUTHORIZED);
                    assertThat(exception.getReason()).isEqualTo("Invalid credentials");
                });
    }
}
