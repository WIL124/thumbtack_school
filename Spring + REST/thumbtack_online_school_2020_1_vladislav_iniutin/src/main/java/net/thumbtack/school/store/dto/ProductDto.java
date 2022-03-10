package net.thumbtack.school.store.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
    @Min(value = 1, message = "price must be more than 1")
    @NotNull (message = "price is a mandatory parameter")
    private double price;
    @NotBlank (message = "parameter description cannot be empty")
    private String description;
    @NotBlank(message = "parameter country cannot be empty")
    private String country;
    @NotBlank(message = "parameter company cannot be empty")
    private String company;
    @NotBlank(message = "parameter address cannot be empty")
    private String address;
}
