package net.thumbtack.school.store.endpoint;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dto.ProductReviewDto;
import net.thumbtack.school.store.models.ProductReview;
import net.thumbtack.school.store.service.StoreService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products/{productId}")
public class ProductReviewEndpoint {
    private final StoreService service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductReview createReview(@PathVariable("productId") String productId, @RequestBody @Valid ProductReviewDto productReviewDto){
        productReviewDto.setProductId(productId);
        System.out.println(productReviewDto);
        return service.createReview(productReviewDto);
    }
}
