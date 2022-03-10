package net.thumbtack.school.store.dao;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.database.Database;
import net.thumbtack.school.store.models.ProductReview;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductReviewDao implements Dao<ProductReview> {
    private final Database database;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductReview.class);

    @Override
    public ProductReview findById(String id) {
        ProductReview productReview = database.reviewById(id);
        if (productReview == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        return productReview;
    }

    @Override
    public List<ProductReview> findAll() {
        return database.allReviews();
    }

    @Override
    public void insert(ProductReview obj) {
        database.insertReviews(obj);
        LOGGER.debug("Review {} was inserted", obj);
    }

    @Override
    public void delete(ProductReview obj) {
        database.deleteReview(obj);
    }
}
