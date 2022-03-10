package net.thumbtack.school.store.models;

import lombok.Data;
import net.thumbtack.school.store.dto.ProductReviewDto;

import java.util.UUID;

@Data
public class ProductReview {
    private final String id = UUID.randomUUID().toString();
    private String productId;
    private int grade;
    private boolean recommendToFriends;
    private boolean chooseAgain;
    private User user;

    public ProductReview(String productId, int grade, boolean recommendToFriends, boolean chooseAgain, User user) {
        this.productId = productId;
        this.grade = grade;
        this.recommendToFriends = recommendToFriends;
        this.chooseAgain = chooseAgain;
        this.user = user;
    }
    public ProductReview(ProductReviewDto reviewDto, User user){
        this.productId = reviewDto.getProductId();
        this.grade = reviewDto.getGrade();
        this.recommendToFriends = reviewDto.isRecommendToFriends();
        this.chooseAgain = reviewDto.isChooseAgain();
        this.user = user;
    }

    public ProductReview() {
    }
}
