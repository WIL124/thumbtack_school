package net.thumbtack.school.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Bid {
    private Integer offer;
    private Integer lotID;
    private Integer buyerId;
}
