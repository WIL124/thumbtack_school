package net.thumbtack.school.store.dao;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.database.Database;
import net.thumbtack.school.store.models.Product;
import net.thumbtack.school.store.models.ProductReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class ProductDao implements Dao<Product> {
    private final Database database;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductDao.class);

    @Override
    public Product findById(String id) {
        return database.productById(id);
    }

    @Override
    public List<Product> findAll() {
        return database.allProducts();
    }

    @Override
    public void insert(Product obj) {
        database.insertProduct(obj);
        LOGGER.debug("Product {} was inserted", obj);
    }

    @Override
    public void delete(Product obj) {
        database.deleteProduct(obj);
    }

    public void updateReport(ProductReport report) {
        database.updateProductReport(report);
    }

    public ProductReport productReportByProductId(String id) {
        return database.reportByProductId(id);
    }
}
