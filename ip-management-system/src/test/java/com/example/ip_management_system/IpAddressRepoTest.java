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
        ipPool.setName("Test Pool");
        ipPool.setStartIp("192.168.1.1");
        ipPool.setEndIp("192.168.1.255");
        ipPool.setDescription("Test Pool Description");
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

    @Test
    public void findByIpShouldReturnIpAddress() {
        // Setup: Create and save an IpAddress
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("192.168.2.20");
        ipAddress.setHostname("unique-host");
        ipAddressRepository.save(ipAddress);

        // Test: Find IP address by IP
        IpAddress foundIpAddress = ipAddressRepository.findByIp("192.168.2.20");

        // Assertions
        assertThat(foundIpAddress).isNotNull();
        assertThat(foundIpAddress.getHostname()).isEqualTo("unique-host");
    }

    @Test
    public void deleteByIpShouldRemoveIpAddress() {
        // Setup: Create and save an IpAddress
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("192.168.3.30");
        ipAddress.setHostname("delete-host");
        ipAddressRepository.save(ipAddress);

        // Test: Delete IP address by IP
        ipAddressRepository.deleteByIp("192.168.3.30");

        // Assertions: Verify the IP address no longer exists
        IpAddress deletedIpAddress = ipAddressRepository.findByIp("192.168.3.30");
        assertThat(deletedIpAddress).isNull();
    }

    @Test
    public void testCreateNewIpAddress() {
        // Setup: Create and save an IpAddress
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("10.0.0.10");
        ipAddress.setHostname("new-host");

        ipAddressRepository.save(ipAddress);

        // Assertions
        assertThat(ipAddress.getId()).isNotNull();
    }

    @Test
    public void testFindAllIpAddresses() {
        // Setup: Create and save multiple IP addresses
        IpAddress ip1 = new IpAddress();
        ip1.setIp("10.0.0.1");
        ip1.setHostname("host1");
        ipAddressRepository.save(ip1);

        IpAddress ip2 = new IpAddress();
        ip2.setIp("10.0.0.2");
        ip2.setHostname("host2");
        ipAddressRepository.save(ip2);

        // Test: Find all IP addresses
        List<IpAddress> ipAddresses = ipAddressRepository.findAll();

        // Assertions
        assertThat(ipAddresses).hasSize(2);
    }
}