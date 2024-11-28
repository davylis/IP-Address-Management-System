package com.example.ip_management_system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.repositories.IpPoolRepository;

@DataJpaTest
public class IpAddressRepoTest {

    @Autowired
    private IpAddressRepository ipAddressRepository;

    @Autowired
    private IpPoolRepository ipPoolRepository;

    @Test
    public void findByIpPoolIdShouldReturnIpAddresses() {
        // Setup: Create and save an IpPool and IpAddress
        IpPool ipPool = new IpPool();
        ipPool.setName("Pool123");
        ipPool.setStartIp("192.168.1.1");
        ipPool.setEndIp("192.168.1.255");
        ipPool.setDescription("Test Pool Description");
        ipPool.setGateway("12345");
        ipPoolRepository.save(ipPool);

        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("192.168.1.10");
        ipAddress.setHostname("test-host");
        ipAddress.setIpPool(ipPool);
        ipAddressRepository.save(ipAddress);

        // Test: Find IP addresses by pool ID
        List<IpAddress> ipAddresses = ipAddressRepository.findByIpPoolId(ipPool.getId());

        // Assertions
        assertThat(ipAddresses).isNotEmpty();
        assertThat(ipAddresses.get(0).getIp()).isEqualTo("192.168.1.10");
        assertThat(ipAddresses.get(0).getHostname()).isEqualTo("test-host");
    }

}