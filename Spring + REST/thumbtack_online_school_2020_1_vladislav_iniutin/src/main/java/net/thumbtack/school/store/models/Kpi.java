package net.thumbtack.school.store.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Kpi {
    private String productId;
    private double rating;
    private double satisfaction;
    private double attractiveness;
}
