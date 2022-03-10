package net.thumbtack.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import net.thumbtack.school.model.Bid;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class LotFullInfoDtoResponse extends DtoResponseBase {
    private String name;
    private String description;
    private String[] category;
    private int startPrice;
    private int currentPrice;
    private int minPrice;
    private int maxPrice;
    private int ownerId;
    private boolean isAcceptBids;
    private boolean onSale;
    private Set<Bid> bids;
    private String status;
}
