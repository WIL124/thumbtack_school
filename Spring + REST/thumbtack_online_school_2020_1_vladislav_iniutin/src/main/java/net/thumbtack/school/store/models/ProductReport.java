package net.thumbtack.school.store.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductReport {
    private String productId;
    private double youngUsersRating;
    private double adultUsersRating;
    private double oldUsersRating;
    private double singleMenRating;
    private double singleWomenRating;
    private double marriedWomenRating;
    private double marriedMenRating;
    private double universityAndHasChildren;
    private double universityAndHasNotChildren;
    private double anotherAndHasChildren;
    private double anotherAndHasNotChildren;
}
