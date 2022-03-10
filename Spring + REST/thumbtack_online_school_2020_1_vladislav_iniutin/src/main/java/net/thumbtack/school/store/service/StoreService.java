package net.thumbtack.school.store.service;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dao.KpiDao;
import net.thumbtack.school.store.dao.ProductDao;
import net.thumbtack.school.store.dao.ProductReviewDao;
import net.thumbtack.school.store.dao.UserDao;
import net.thumbtack.school.store.dto.KpiDto;
import net.thumbtack.school.store.dto.ProductDto;
import net.thumbtack.school.store.dto.ProductReviewDto;
import net.thumbtack.school.store.dto.UserDto;
import net.thumbtack.school.store.models.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

import static net.thumbtack.school.store.UserGroupReportCreator.*;
import static net.thumbtack.school.store.KpiCalculator.convertOptionalToDouble;

@Service
@AllArgsConstructor
public class StoreService {
    private final UserDao userDao;
    private final ProductDao productDao;
    private final ProductReviewDao reviewDao;
    private final KpiDao kpiDao;

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public List<Product> getAllProducts() {
        return productDao.findAll();
    }

    public Product getProduct(String id) {
        Product product = productDao.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        return product;
    }

    public User getUser(String id) {
        return userDao.findById(id);
    }

    public User createUser(UserDto userDto) {
        User user = new User(userDto);
        userDao.insert(user);
        return user;
    }

    public Product createProduct(ProductDto productDto) {
        Product product = new Product(productDto);
        productDao.insert(product);
        return product;
    }

    public ProductReview createReview(ProductReviewDto productReviewDto) {
        Product product = productDao.findById(productReviewDto.getProductId());
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        User user = userDao.findById(productReviewDto.getUserId());
        ProductReview review = new ProductReview(productReviewDto, user);
        reviewDao.insert(review);
        return review;
    }

    public Product updateProduct(ProductDto productDto, String productId) {
        Product product = productDao.findById(productId);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        updateProductFromDto(product, productDto);
        return product;
    }

    public String deleteUser(String id) {
        User user = userDao.findById(id);
        userDao.delete(user);
        return "user deleted";
    }

    public String deleteProduct(String id) {
        Product product = productDao.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        productDao.delete(product);
        return "deleted";
    }

    public String calculateProductKpi(String id) throws ResponseStatusException {
        Product product = productDao.findById(id);
        validNotNull(product);
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
        Kpi kpi = new Kpi(id, rating, satisfaction, attractiveness);
        kpiDao.update(kpi);
        return "calculated";
    }

    public KpiDto getProductKpi(String id) {
        Kpi kpi = kpiDao.findById(id);
        return new KpiDto(kpi);
    }

    public String calculateProductReport(String id) {
        Product product = productDao.findById(id);
        if (product == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        List<ProductReview> reviews = reviewDao.findAll().stream()
                .filter(review -> review.getProductId().equalsIgnoreCase(product.getId()))
                .collect(Collectors.toList());

        double youngUsersRating = averageRatingByUsersAge(reviews, 0, 20);
        double adultUsersRating = averageRatingByUsersAge(reviews, 20, 50);
        double oldUsersRating = averageRatingByUsersAge(reviews, 50, 500);

        double singleMenRating = averageRatingByUsersSexAndMaritalStatus(reviews, Sex.MALE, false);
        double singleWomenRating = averageRatingByUsersSexAndMaritalStatus(reviews, Sex.FEMALE, false);
        double marriedWomenRating = averageRatingByUsersSexAndMaritalStatus(reviews, Sex.FEMALE, true);
        double marriedMenRating = averageRatingByUsersSexAndMaritalStatus(reviews, Sex.MALE, true);

        double universityAndHasChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.UNIVERSITY, true);
        double universityAndHasNotChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.UNIVERSITY, false);
        double anotherAndHasChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.ANOTHER, true);
        double anotherAndHasNotChildren = averageRatingByUsersEducationAndWithChild(reviews, Education.ANOTHER, false);

        ProductReport report = new ProductReport(product.getId(), youngUsersRating, adultUsersRating, oldUsersRating, singleMenRating, singleWomenRating, marriedWomenRating,
                marriedMenRating, universityAndHasChildren, universityAndHasNotChildren, anotherAndHasChildren, anotherAndHasNotChildren);
        productDao.updateReport(report);
        return "calculated";
    }

    public ProductReport getProductReport(String productId) {
        return productDao.productReportByProductId(productId);
    }

    private void updateProductFromDto(Product product, ProductDto dto) {
        product.setPrice(dto.getPrice());
        product.setAddress(dto.getAddress());
        product.setCountry(dto.getCountry());
        product.setDescription(dto.getDescription());
        product.setCompany(dto.getCompany());
    }
    private static void validNotNull(Object obj) throws ResponseStatusException{
        if (obj == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "not found");
        }
    }
}
