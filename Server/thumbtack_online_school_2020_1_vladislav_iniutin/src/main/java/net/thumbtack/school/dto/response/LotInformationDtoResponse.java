package net.thumbtack.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LotInformationDtoResponse extends DtoResponseBase {
    private String name;
    private String description;
    private String[] category;
    private int startPrice;
    private int currentPrice;
    private int minPrice;
    private int maxPrice;
    private String status;
}
