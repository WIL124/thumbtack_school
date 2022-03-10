package net.thumbtack.school.store.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.thumbtack.school.store.models.Kpi;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KpiDto {
    private double rating;
    private double satisfaction;
    private double attractiveness;

    public KpiDto(Kpi kpi) {
        rating = kpi.getRating();
        satisfaction = kpi.getSatisfaction();
        attractiveness = kpi.getAttractiveness();
    }
}
