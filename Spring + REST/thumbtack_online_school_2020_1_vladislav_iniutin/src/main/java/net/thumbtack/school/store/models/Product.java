package net.thumbtack.school.store.models;

import lombok.Data;
import net.thumbtack.school.store.dto.ProductDto;

import java.util.UUID;

@Data
public class Product {
    private final String id = UUID.randomUUID().toString();
    private double price;
    private String description;
    private String country;
    private String company;
    private String address;

    public Product(ProductDto productDto) {
        this.price = productDto.getPrice();
        this.description = productDto.getDescription();
        this.country = productDto.getCountry();
        this.company = productDto.getCompany();
        this.address = productDto.getAddress();
    }

    public Product(double price, String description, String country, String company, String address) {
        this.price = price;
        this.description = description;
        this.country = country;
        this.company = company;
        this.address = address;
    }

    public Product() {
    }
}
