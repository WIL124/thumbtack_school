package net.thumbtack.school.store.dto;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductReviewDto {
    private String productId;
    @Min(value = 1, message = "grade must be between 1 and 5")
    @Max(value = 5, message = "grade must be between 1 and 5")
    @NotNull (message = "grade is a mandatory parameter")
    private int grade;
    @NotNull (message = "recommendToFriends is a mandatory parameter")
    private boolean recommendToFriends;
    @NotNull (message = "chooseAgain is a mandatory parameter")
    private boolean chooseAgain;
    @NotBlank (message = "parameter userId cannot be empty")
    private String userId;
}
