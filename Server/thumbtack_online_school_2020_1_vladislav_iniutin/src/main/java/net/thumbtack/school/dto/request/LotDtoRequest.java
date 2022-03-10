package net.thumbtack.school.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LotDtoRequest {
    private String name;
    private String description;
    private String[] category;
    private int startPrice;
    private int minPrice;
    private int maxPrice;
}
