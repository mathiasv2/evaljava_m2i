package edu.ban7.chatbotmsnmsii2527.unit;

import edu.ban7.chatbotmsnmsii2527.controller.AppUserController;
import edu.ban7.chatbotmsnmsii2527.mock.MockAppUserDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class AppUserControllerUnitTest {

    @Test
    public void getByNotExistingId_shouldReturn404() {
        AppUserController controller = new AppUserController(new MockAppUserDao());
        Assertions.assertEquals(HttpStatus.NOT_FOUND,controller.get(99).getStatusCode());
    }

    @Test
    public void getExistingId_shouldReturn200() {
        AppUserController controller = new AppUserController(new MockAppUserDao());

        Assertions.assertEquals(HttpStatus.OK,controller.get(1).getStatusCode());
    }

}
