package net.thumbtack.school.store.dao;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.database.Database;
import net.thumbtack.school.store.models.Kpi;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@AllArgsConstructor
public class KpiDao {
    private final Database database;

    public void update(Kpi kpi) {
        database.updateKpi(kpi);
    }

    public Kpi findById(String id) {
        Kpi kpi = database.kpiByProductId(id);
        if (kpi == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "kpi not found");
        }
        return kpi;
    }
}
