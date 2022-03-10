package net.thumbtack.school.store.endpoint;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dto.KpiDto;
import net.thumbtack.school.store.dto.ProductDto;
import net.thumbtack.school.store.models.Product;
import net.thumbtack.school.store.models.ProductReport;
import net.thumbtack.school.store.service.StoreService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/products")
public class ProductEndpoint {
    private final StoreService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Product> readAll() {
        return service.getAllProducts();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Product read(@PathVariable String id) {
        return service.getProduct(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product create(@RequestBody @Valid ProductDto productDto) {
        return service.createProduct(productDto);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Product update(@RequestBody @Valid ProductDto productDto, @PathVariable String id) {
        return service.updateProduct(productDto, id);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public String delete(@PathVariable String id) {
        return service.deleteProduct(id);
    }

    @PostMapping(path = "/{id}/kpi", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateKpi(@PathVariable String id) {
        return service.calculateProductKpi(id);
    }

    @GetMapping(path = "/{id}/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductReport readReport(@PathVariable String id) {
        return service.getProductReport(id);
    }

    @PostMapping(path = "/{id}/report", produces = MediaType.APPLICATION_JSON_VALUE)
    public String calculateReport(@PathVariable String id) {
        return service.calculateProductReport(id);

    }

    @GetMapping(path = "/{id}/kpi", produces = MediaType.APPLICATION_JSON_VALUE)
    public KpiDto readKpi(@PathVariable String id) {
        return service.getProductKpi(id);
    }

}
