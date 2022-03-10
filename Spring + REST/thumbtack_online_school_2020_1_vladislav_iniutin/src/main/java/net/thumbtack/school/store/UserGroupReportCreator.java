package net.thumbtack.school.store;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dao.ProductDao;
import net.thumbtack.school.store.dao.ProductReviewDao;
import net.thumbtack.school.store.models.Education;
import net.thumbtack.school.store.models.Product;
import net.thumbtack.school.store.models.ProductReview;
import net.thumbtack.school.store.models.Sex;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static net.thumbtack.school.store.KpiCalculator.convertOptionalToDouble;


@Component
@Order(3)
@AllArgsConstructor
public class UserGroupReportCreator implements CommandLineRunner {
    private ProductDao productDao;
    private ProductReviewDao reviewDao;
    @Override
    public void run(String... args) {
        List<ProductReview> allReviews = new ArrayList<>(reviewDao.findAll());
        List<Product> products = productDao.findAll();
        for (Product product : products) {
            List<ProductReview> reviews = allReviews.stream().filter(review -> review.getProductId().equalsIgnoreCase(product.getId())).collect(Collectors.toList());
            double youngUsersRating = averageRatingByUsersAge(reviews, 0, 20);
            double adultUsersRating = averageRatingByUsersAge(reviews, 20, 50);
            double oldUsersRating = averageRatingByUsersAge(reviews, 50, 500);

            double singleMenRating = averageRatingByUsersSexAndMaritalStatus(reviews,Sex.MALE, false);
            double singleWomenRating = averageRatingByUsersSexAndMaritalStatus(reviews,Sex.FEMALE, false);
            double marriedWomenRating = averageRatingByUsersSexAndMaritalStatus(reviews,Sex.FEMALE, true);
            double marriedMenRating = averageRatingByUsersSexAndMaritalStatus(reviews,Sex.MALE, true);

            double universityAndHasChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.UNIVERSITY, true);
            double universityAndHasNotChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.UNIVERSITY, false);
            double anotherAndHasChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.ANOTHER, true);
            double anotherAndHasNotChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.ANOTHER, false);

            System.out.println("\n\n\nProduct " + product.getId() + " report:\n" +
                    "\nYoung users average rating: " + youngUsersRating +
                    "\nAdult users average rating: " + adultUsersRating +
                    "\nOld users average rating: " + oldUsersRating +

                    "\n\nUsers with higher education and with children average rating: " + universityAndHasChildren +
                    "\nUsers with higher education and without children average rating: " + universityAndHasNotChildren +
                    "\nUsers with another education and without children average rating: " + anotherAndHasNotChildren +
                    "\nUsers with another education and with children average rating: " + anotherAndHasChildren +

                    "\n\nSingle men average rating: " + singleMenRating +
                    "\nSingle women average rating: " + singleWomenRating +
                    "\nMarried men average rating: " + marriedMenRating +
                    "\nMarried women average rating: " + marriedWomenRating);
        }
    }
    public static double averageRatingByUsersAge(List<ProductReview> reviews, int under, int over){
        OptionalDouble result = reviews.stream()
                .filter(review -> review.getUser().getYear() > under)
                .filter(review -> review.getUser().getYear() <= over)
                .mapToDouble(ProductReview::getGrade)
                .average();
         return convertOptionalToDouble(result);
    }
    public static double averageRatingByUsersSexAndMaritalStatus(List<ProductReview> reviews, Sex sex, boolean isMarried){
        OptionalDouble result = reviews.stream()
                .filter(review -> review.getUser().getSex() == sex)
                .filter(review -> review.getUser().isMarried() == isMarried)
                .mapToDouble(ProductReview::getGrade)
                .average();
        return convertOptionalToDouble(result);
    }
    public static double averageRatingByUsersEducationAndWithChild(List<ProductReview> reviews, Education education, boolean isHasChildren){
        OptionalDouble result = reviews.stream()
                .filter(review -> review.getUser().getEducation() == education)
                .filter(review -> review.getUser().isHasChildren() == isHasChildren)
                .mapToDouble(ProductReview::getGrade)
                .average();
        return convertOptionalToDouble(result);
    }
}

