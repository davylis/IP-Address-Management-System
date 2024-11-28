package com.example.ip_management_system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    
}