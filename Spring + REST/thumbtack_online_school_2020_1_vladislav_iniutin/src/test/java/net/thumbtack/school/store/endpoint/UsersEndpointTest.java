package net.thumbtack.school.store.endpoint;

import net.thumbtack.school.store.dto.UserDto;
import net.thumbtack.school.store.models.Education;
import net.thumbtack.school.store.models.Sex;
import net.thumbtack.school.store.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static net.thumbtack.school.store.endpoint.TestUtils.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class UsersEndpointTest {
    private final RestTemplate template = new RestTemplate();
    private final String URL = "http://localhost:8080/api/users";

    @Test
    void createUser_success() {
        User user = createUser();
        User createdUser = template.getForObject(URL + "/{id}", User.class, user.getId());
        assertEquals(createdUser, user);
    }

    @Test
    void createUser_wrong_incorrectCardNumber() {
        try {
            UserDto userDto = new UserDto("1999", Sex.MALE, "1234567890123456", "true", "false", Education.UNIVERSITY, "Omsk");
            userDto.setCardNumber("123456");
            template.postForObject(URL, userDto, User.class);
            fail();
        } catch (HttpClientErrorException exc) {
            assertTrue(exc.getResponseBodyAsString().contains("Incorrect card number"));
        }
    }

    @Test
    void deleteUser_success() {
        User user = createUser();
        template.delete(URL + "/{id}", user.getId());
        try {
            template.getForObject(URL + "/{id}", User.class, user.getId());
        } catch (HttpClientErrorException ex) {
            assertEquals(ex.getStatusCode().value(), 404);
        }
    }

    @Test
    void deleteUser_error_wrongURL() {
        User user = createUser();
        try {
            template.delete(URL + "/{id}" + "a", user.getId());
            fail();
        } catch (HttpClientErrorException ex) {
            assertEquals(ex.getStatusCode().value(), 404);
        }
    }
}