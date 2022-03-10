package net.thumbtack.school.store.endpoint;


import net.thumbtack.school.store.dto.ProductDto;
import net.thumbtack.school.store.models.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static net.thumbtack.school.store.endpoint.TestUtils.*;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ProductEndpointTest {
    private final RestTemplate template = new RestTemplate();
    private final String URL = "http://localhost:8080/api/products";

    @Test
    void create_successful() {
        ProductDto productDto = getProductDto();
        Product product = template.postForObject(URL, productDto, Product.class);
        assert product != null;
        assertNotNull(product.getId());
    }

    @Test
    void create_error_lowPrice() {
        try {
            ProductDto productDto = getProductDto();
            productDto.setPrice(0);
            template.postForObject(URL, productDto, Product.class);
            fail();
        }catch (HttpClientErrorException exc) {
            assertTrue(exc.getResponseBodyAsString().contains("price must be more than 1"));
        }
    }

    @Test
    void update_success() {
        Product product = createProduct();
        product.setPrice(100);
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        HttpEntity<Product> entity = new HttpEntity<>(product, headers);
        template.put(URL + "/{id}", entity, product.getId());
        Product productUpdated = template.getForObject(URL + "/{id}", Product.class, product.getId());
        assert productUpdated != null;
        assertEquals(product.getPrice(), productUpdated.getPrice());
    }

    @Test
    void update_error_wrongDescription() {
        try {
            Product product = createProduct();
            product.setDescription(null);
            MultiValueMap<String, String> headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            HttpEntity<Product> entity = new HttpEntity<>(product, headers);
            template.put(URL + "/{id}", entity, product.getId());
            fail();
        } catch (HttpClientErrorException exc) {
            assertEquals(exc.getStatusCode().value(), 400);
            assertTrue(exc.getResponseBodyAsString().contains("description cannot be empty"));
        }
    }

    @Test
    void delete_success() {
            Product product = createProduct();
            template.delete(URL + "/{id}", product.getId());
        try {
            template.getForObject(URL + "/{id}", Product.class, product.getId());
            fail();
        } catch (HttpClientErrorException exc) {
            assertEquals(exc.getStatusCode().value(), 404);
        }
    }

    @Test
    void delete_error_notFound() {
        try {
            Product product = createProduct();
            template.delete(URL + "/{id}", product.getId() + "a");
            fail();
        } catch (HttpClientErrorException exc) {
            assertEquals(exc.getStatusCode().value(), 404);
        }
    }

    protected static ProductDto getProductDto() {
        return new ProductDto(44.99, "Bounty chocolate", "Russia", "Mars", "Moscow, Lenina 3");
    }
}