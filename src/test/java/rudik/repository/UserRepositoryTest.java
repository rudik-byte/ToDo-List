package rudik.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    public void getUserByExistingEmailTest() {
        Assertions.assertEquals(4, userRepository.getUserByEmail("mike@mail.com").getId());
    }

    @Test
    public void getUserByNotExistingEmailTest() {
        Assertions.assertNull(userRepository.getUserByEmail("victoriya@gmail.com"));
    }
}
