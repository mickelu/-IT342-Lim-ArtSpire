package edu.cit.lim.artspire.user;

import edu.cit.lim.artspire.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmail_whenUserExists_returnsUser() {
        User user = TestData.user("encoded-password");
        userRepository.saveAndFlush(user);

        Optional<User> foundUser = userRepository.findByEmail(TestData.VALID_EMAIL);

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo(TestData.VALID_NAME);
        assertThat(foundUser.get().getEmail()).isEqualTo(TestData.VALID_EMAIL);
        assertThat(foundUser.get().getCreatedAt()).isEqualTo(LocalDateTime.of(2026, 1, 1, 10, 0));
    }

    @Test
    void findByEmail_whenUserDoesNotExist_returnsEmptyOptional() {
        Optional<User> foundUser = userRepository.findByEmail("missing@example.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    void save_whenUserIsPersisted_assignsId() {
        User user = TestData.user("encoded-password");

        User savedUser = userRepository.saveAndFlush(user);

        assertThat(savedUser.getId()).isNotNull();
    }
}
