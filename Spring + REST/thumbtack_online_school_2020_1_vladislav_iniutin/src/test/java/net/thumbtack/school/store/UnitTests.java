package net.thumbtack.school.store;

import com.google.gson.Gson;
import net.thumbtack.school.store.endpoint.ProductEndpoint;
import net.thumbtack.school.store.service.StoreService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = ProductEndpoint.class)
public class UnitTests {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private Gson gson;
    @MockBean
    private StoreService storeService;

    @Test
    public void testCalculateKpi() throws Exception {
        String testId = "testId";
        String falseId = "falseId";
        mvc.perform(post("/api/products/{id}/kpi", testId)).andExpect(status().isOk());
        verify(storeService, times(1)).calculateProductKpi(testId);
    }

    @Test
    public void testGetKpi() throws Exception {
        String testId = "testId";
        mvc.perform(get("/api/products/{id}/kpi", testId));
        verify(storeService, times(1)).getProductKpi(testId);
    }

    @Test
    public void testCalculateReport() throws Exception {
        String testId = "testId";
        mvc.perform(post("/api/products/{id}/report", testId));
        verify(storeService, times(1)).calculateProductReport(testId);
    }

    @Test
    public void testGetProductReport() throws Exception {
        String testId = "testId";
        mvc.perform(get("/api/products/{id}/report", testId));
        verify(storeService, times(1)).getProductReport(testId);
    }
}
