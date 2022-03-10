package net.thumbtack.school.store.endpoint;

import net.thumbtack.school.store.dto.ProductReviewDto;
import net.thumbtack.school.store.models.Product;
import net.thumbtack.school.store.models.ProductReview;
import net.thumbtack.school.store.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static net.thumbtack.school.store.endpoint.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductReviewEndpointTest {
    private final RestTemplate template = new RestTemplate();
    private final String URL = "http://localhost:8080/api/products";

    @Test
    void createReview_success() {
        Product product = createProduct();
        User user = createUser();
        ProductReviewDto reviewDto = new ProductReviewDto(product.getId(), 4, true, false, user.getId());
        ProductReview productReview = template.postForObject(URL + "/{id}", reviewDto, ProductReview.class, product.getId());
        assert productReview != null;
    }

    @Test
    void createReview_error_wrongGrade() {
        try {
            Product product = createProduct();
            User user = createUser();
            ProductReviewDto reviewDto = new ProductReviewDto(product.getId(), 6, true, false, user.getId());
            template.postForObject(URL + "/{id}", reviewDto, ProductReview.class, product.getId());
            fail();
        } catch (HttpClientErrorException ex) {
            assertTrue(ex.getResponseBodyAsString().contains("grade must be between 1 and 5"));
        }
    }


}