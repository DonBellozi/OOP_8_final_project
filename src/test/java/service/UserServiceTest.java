import model.User;
import org.junit.jupiter.api.Test;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {

    @Test
    void registerAndLogin() {
        UserService us = new UserService();
        String login = "testuser_" + System.nanoTime();
        assertTrue(us.register(login, "pass"));
        User u = us.login(login, "pass");
        assertNotNull(u);
        assertEquals(login, u.login);
    }

    @Test
    void loginFailsOnWrongPassword() {
        UserService us = new UserService();
        String login = "testuser_" + System.nanoTime();
        us.register(login, "pass");
        assertNull(us.login(login, "wrong"));
    }
}
