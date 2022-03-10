package net.thumbtack.school.store.endpoint;

import net.thumbtack.school.store.dto.ProductDto;
import net.thumbtack.school.store.dto.UserDto;
import net.thumbtack.school.store.models.Education;
import net.thumbtack.school.store.models.Product;
import net.thumbtack.school.store.models.Sex;
import net.thumbtack.school.store.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;


class TestUtils {
    private final static RestTemplate template = new RestTemplate();
    private final static String URL = "http://localhost:8080/api/";

    @Test
    protected static User createUser() {
        UserDto userDto = new UserDto("1999", Sex.MALE, "1234567890123456", "true", "false", Education.UNIVERSITY, "Omsk");
        User user = template.postForObject(URL + "users", userDto, User.class);
        assert user != null;
        return user;
    }

    protected static Product createProduct() {
        ProductDto productDto = new ProductDto(44.99, "Bounty chocolate", "Russia", "Mars", "Moscow, Lenina 3");
        Product product = template.postForObject(URL + "products", productDto, Product.class);
        assert product != null;
        return product;
    }

}
