package net.thumbtack.school.store;

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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@Order(1)
@AllArgsConstructor
public class DataPopulator implements CommandLineRunner {
    private final ProductDao productDao;
    private final ProductReviewDao reviewDao;
    private final UserDao userDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(DataPopulator.class);

    @Override
    public void run(String... args) {
        LOGGER.debug("Start DataPopulator");
        List<User> users = Arrays.asList(
                new User(19, Sex.FEMALE, new BigInteger("1234567890123456"), false, false, Education.ANOTHER, "Omsk"),
                new User(23, Sex.MALE, new BigInteger("1234567890123456"), true, false, Education.UNIVERSITY, "Saratov"),
                new User(40, Sex.FEMALE, new BigInteger("1234567890123456"), true, true, Education.UNIVERSITY, "Moscow"),
                new User(65, Sex.MALE, new BigInteger("1234567890123456"), false, true, Education.ANOTHER, "Omsk"),
                new User(42, Sex.MALE, new BigInteger("1234567890123456"), true, false, Education.ANOTHER, "Omsk")
        );
        List<Product> products = Arrays.asList(
                new Product(19.9, "Bread", "Russia", "MegaBread", "Omsk, Lenina 2"),
                new Product(59.9, "Milk", "Russia", "DairyPlant", "Omsk, Zavertyaeva 47"),
                new Product(299.9, "Sausage", "Russia", "SibKolbases", "Omsk, K.Marksa 37"),
                new Product(119.9, "Chocolate", "Switzerland", "Milka", "Zurich, Bahnhofstrasse 55")
        );
        List<ProductReview> reviews = new ArrayList<>();
        for (Product product : products) {
            for (User user : users) {
                int grade = (int) (Math.random() * 5) + 1;
                boolean recommendToFriends = Math.random() < 0.5;
                boolean chooseAgain = Math.random() < 0.5;
                reviews.add(new ProductReview(product.getId(), grade, recommendToFriends, chooseAgain, user));
            }
        }
        users.forEach(userDao::insert);
        products.forEach(productDao::insert);
        reviews.forEach(reviewDao::insert);
        LOGGER.debug("Finish DataPopulator");
    }
}
