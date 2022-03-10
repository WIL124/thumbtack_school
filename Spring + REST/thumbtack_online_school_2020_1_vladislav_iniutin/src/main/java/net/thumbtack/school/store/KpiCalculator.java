package net.thumbtack.school.store;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dao.ProductDao;
import net.thumbtack.school.store.dao.ProductReviewDao;
import net.thumbtack.school.store.dao.UserDao;
import net.thumbtack.school.store.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

@Component
@Order(2)
@AllArgsConstructor
public class KpiCalculator implements CommandLineRunner {
    private final ProductDao productDao;
    private final ProductReviewDao reviewDao;
    private final UserDao userDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(KpiCalculator.class);

    @Override
    public void run(String... args) {
        LOGGER.debug("Start KpiCalculator");
        for (Product product : productDao.findAll()) {
            List<ProductReview> reviews = reviewDao.findAll().stream()  //All reviews of this product
                    .filter(review -> Objects.equals(product.getId(), review.getProductId()))
                    .collect(Collectors.toList());
            OptionalDouble ratingOpt = reviews.stream()
                    .mapToDouble(ProductReview::getGrade)
                    .average();
            OptionalDouble satisfactionOpt = reviews.stream()
                    .mapToDouble(review -> review.getGrade() * (review.isChooseAgain() ? 0.8 : 0.2))
                    .average();
            OptionalDouble attractivenessOpt = reviews.stream()
                    .mapToDouble(review -> review.isRecommendToFriends() ? 1 : 0)
                    .average();
            double rating = convertOptionalToDouble(ratingOpt);
            double satisfaction = convertOptionalToDouble(satisfactionOpt) * 100;
            double attractiveness = convertOptionalToDouble(attractivenessOpt) * 100;
            LOGGER.info("\nProduct " + product.getId() + " Report:" +
                    "\nAverage grade: " + new DecimalFormat("0.00").format(rating) +
                    "\nSatisfaction index: " + (int) satisfaction +
                    "\nAttractiveness index: " + (int) attractiveness
            );
        }
        LOGGER.debug("Finish KpiCalculator");
        System.out.println(new Gson().toJson(userDao.findAll()) );
    }

    public static double convertOptionalToDouble(OptionalDouble od) {
        return od.isPresent() ? od.getAsDouble() : 0;
    }
}
