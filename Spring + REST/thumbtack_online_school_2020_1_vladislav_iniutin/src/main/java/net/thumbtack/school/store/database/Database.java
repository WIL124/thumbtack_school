package net.thumbtack.school.store.database;

import net.thumbtack.school.store.models.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class Database {
    private final Map<String, Product> products = new ConcurrentHashMap<>();
    private final Map<String, ProductReview> reviews = new ConcurrentHashMap<>();
    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, Kpi> productKpiMap = new ConcurrentHashMap<>();
    private final Map<String, ProductReport> productReportMap = new ConcurrentHashMap<>();

    public Product productById(String id) {
        return products.get(id);
    }

    public List<Product> allProducts() {
        return new ArrayList<>(products.values());
    }

    public void insertProduct(Product obj) {
        products.put(obj.getId(), obj);
    }

    public User userById(String id) {
        return users.get(id);
    }

    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    public void insertUser(User obj) {
        users.put(obj.getId(), obj);
    }

    public ProductReview reviewById(String id) {
        return reviews.get(id);
    }

    public List<ProductReview> allReviews() {
        return new ArrayList<>(reviews.values());
    }

    public void insertReviews(ProductReview obj) {
        reviews.put(obj.getId(), obj);
    }

    public void deleteUser(User user) {
        allReviews().forEach(this::deleteReview);
        users.remove(user.getId());
    }

    public void deleteProduct(Product product) {
        allReviews().forEach(this::deleteReview);
        products.remove(product.getId());
    }

    public void deleteReview(ProductReview review) {
        reviews.remove(review.getId());
    }

    public Kpi kpiByProductId(String productId) {
        return productKpiMap.get(productId);
    }

    public void updateKpi(Kpi kpi) {
        productKpiMap.put(kpi.getProductId(), kpi);
    }
    public ProductReport reportByProductId(String productId) {
        return productReportMap.get(productId);
    }

    public void updateProductReport(ProductReport report) {
        productReportMap.put(report.getProductId(), report);
    }
}
