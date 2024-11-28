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
@Import(TestSecurityConfig.class) // To bypass security during tests
public class IpAddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllIpAddresses() throws Exception {
        this.mockMvc.perform(get("/ipaddresses/api/ipaddresses"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testGetIpAddressById() throws Exception {
        // Ensure an IP address with ID 1 exists in the database
        this.mockMvc.perform(get("/ipaddresses/api/ipaddresses/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1))
                    .andExpect(jsonPath("$.ip").exists());
    }

    @Test
    public void testAddIpAddress() throws Exception {
        String newIpJson = """
        {
            "ip": "192.168.0.10",
            "hostname": "test-host",
            "ipPool": {
                "id": 1
            }
        }
        """;

        this.mockMvc.perform(post("/ipaddresses/api/ipaddresses/add")
                .contentType("application/json")
                .content(newIpJson))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ip").value("192.168.0.10"))
                .andExpect(jsonPath("$.hostname").value("test-host"));
    }

    @Test
    public void testDeleteIpAddress() throws Exception {
        // Ensure an IP address with ID 1 exists for deletion
        this.mockMvc.perform(get("/ipaddresses/api/ipaddresses/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));

        this.mockMvc.perform(get("/ipaddresses/api/ipaddresses/delete/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("IP Address deleted")));

        // Confirm deletion
        this.mockMvc.perform(get("/ipaddresses/api/ipaddresses/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(""));
    }

    @Test
    public void testShowIpsInPool() throws Exception {
        // Ensure an IP pool with ID 1 has some IPs
        this.mockMvc.perform(get("/ipaddresses/1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("ipAddresses")));
    }

    @Test
    public void testAddIpFormRestrictedToAdmin() throws Exception {
        this.mockMvc.perform(get("/ipaddresses/add/1"))
                    .andDo(print())
                    .andExpect(status().isForbidden());
    }

    @Test
    public void testSaveIpAddressRestrictedToAdmin() throws Exception {
        String newIpForm = """
        {
            "ip": "192.168.0.20",
            "hostname": "test-host-save",
            "ipPool": {
                "id": 1
            }
        }
        """;

        this.mockMvc.perform(post("/ipaddresses/save")
                .contentType("application/json")
                .content(newIpForm))
                .andDo(print())
                .andExpect(status().isForbidden());
    }
}
