package com.example.ip_management_system;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class) // Import the test security config to bypass login
public class IpPoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllIpPools() throws Exception {
        this.mockMvc.perform(get("/ippools/api/ippools"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetIpPoolById() throws Exception {
        // Ensure a valid ID exists in your database for this test
        this.mockMvc.perform(get("/ippools/api/ippool/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.name").exists());
    }

    @Test
    public void testAddIpPool() throws Exception {
        String newIpPoolJson = """
        {
            "name": "Test Pool",
            "description": "This is a test IP pool",
            "startIp": "192.168.0.1",
            "endIp": "192.168.0.255",
            "gateway": "192.168.0.1"
        }
        """;

        this.mockMvc.perform(post("/ippools/api/ippool/add")
                .contentType("application/json")
                .content(newIpPoolJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Pool"));
    }

    @Test
    public void testDeleteIpPool() throws Exception {
        String newIpPoolJson = """
        {
            "name": "Pool to Delete",
            "description": "Temporary pool for deletion",
            "startIp": "192.168.1.1",
            "endIp": "192.168.1.255",
            "gateway": "192.168.1.1"
        }
        """;

        this.mockMvc.perform(post("/ippools/api/ippool/add")
                .contentType("application/json")
                .content(newIpPoolJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Pool to Delete"));

        this.mockMvc.perform(get("/ippools/api/ippool/delete/2"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("IP Pool deleted")));

        this.mockMvc.perform(get("/ippools/api/ippool/2"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
    }

    @Test
    public void testRestrictedAddFormAccess() throws Exception {
        this.mockMvc.perform(get("/ippools/add"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
    }
}