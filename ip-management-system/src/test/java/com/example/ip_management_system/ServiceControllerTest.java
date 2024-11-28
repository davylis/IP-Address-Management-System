package com.example.ip_management_system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testShowServicesForIp() throws Exception {
        Long ipAddressId = 1L; // Replace with a valid IP address ID from your database

        mockMvc.perform(get("/services/" + ipAddressId))
                .andExpect(status().isOk())
                .andExpect(view().name("services"))
                .andExpect(model().attributeExists("services"))
                .andExpect(model().attributeExists("ipAddressId"))
                .andExpect(model().attributeExists("ipPoolId"));
    }

    @Test
    public void testAddServiceFormUnauthorized() throws Exception {
        Long ipAddressId = 1L; // Replace with a valid IP address ID from your database

        mockMvc.perform(get("/services/add/" + ipAddressId))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testSaveServiceUnauthorized() throws Exception {
        Long ipAddressId = 1L; // Replace with a valid IP address ID from your database

        Service service = new Service();
        service.setName("Test Service");
        service.setPort(8080);
        service.setDescription("This is a test service");
        service.setStatus(ServiceStatus.ACTIVE);

        mockMvc.perform(post("/services/save/" + ipAddressId)
                        .param("name", service.getName())
                        .param("port", String.valueOf(service.getPort()))
                        .param("description", service.getDescription())
                        .param("status", service.getStatus().name()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testEditServiceUnauthorized() throws Exception {
        Long serviceId = 1L; // Replace with a valid Service ID from your database

        mockMvc.perform(get("/services/edit/" + serviceId))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testDeleteServiceUnauthorized() throws Exception {
        Long serviceId = 1L; // Replace with a valid Service ID from your database

        mockMvc.perform(get("/services/delete/" + serviceId))
                .andExpect(status().isForbidden());
    }

}