package com.example.ip_management_system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;
import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.repositories.ServiceRepository;

@DataJpaTest
public class ServiceRepoTest {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private IpAddressRepository ipAddressRepository;

    @Test
    public void findByIpAddressIdShouldReturnServices() {
        // Setup: Create and save an IpAddress and a Service
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("192.168.1.1");
        ipAddress.setHostname("test-host");
        ipAddressRepository.save(ipAddress);

        Service service = new Service();
        service.setName("Test Service");
        service.setPort(8080);
        service.setStatus(ServiceStatus.ACTIVE);
        service.setIpAddress(ipAddress);
        serviceRepository.save(service);

        // Test: Find services by IP address ID
        List<Service> services = serviceRepository.findByIpAddressId(ipAddress.getId());

        // Assertions
        assertThat(services).isNotEmpty();
        assertThat(services.get(0).getName()).isEqualTo("Test Service");
        assertThat(services.get(0).getPort()).isEqualTo(8080);
    }

    @Test
    public void findByIpAddressShouldReturnServices() {
        // Setup: Create and save an IpAddress and a Service
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("192.168.2.1");
        ipAddress.setHostname("unique-host");
        ipAddressRepository.save(ipAddress);

        Service service = new Service();
        service.setName("Another Service");
        service.setPort(9090);
        service.setStatus(ServiceStatus.INACTIVE);
        service.setIpAddress(ipAddress);
        serviceRepository.save(service);

        // Test: Find services by IP address entity
        List<Service> services = serviceRepository.findByIpAddress(ipAddress);

        // Assertions
        assertThat(services).isNotEmpty();
        assertThat(services.get(0).getName()).isEqualTo("Another Service");
        assertThat(services.get(0).getStatus()).isEqualTo(ServiceStatus.INACTIVE);
    }

    @Test
    public void testCreateNewService() {
        // Setup: Create and save an IpAddress and a Service
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("10.0.0.1");
        ipAddress.setHostname("new-host");
        ipAddressRepository.save(ipAddress);

        Service service = new Service();
        service.setName("New Test Service");
        service.setPort(7000);
        service.setStatus(ServiceStatus.ACTIVE);
        service.setIpAddress(ipAddress);

        serviceRepository.save(service);

        // Assertions
        assertThat(service.getId()).isNotNull();
        assertThat(service.getName()).isEqualTo("New Test Service");
        assertThat(service.getPort()).isEqualTo(7000);
    }

    @Test
    public void testDeleteService() {
        // Setup: Create and save an IpAddress and a Service
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIp("10.0.0.2");
        ipAddress.setHostname("delete-host");
        ipAddressRepository.save(ipAddress);

        Service service = new Service();
        service.setName("Delete Me Service");
        service.setPort(6000);
        service.setStatus(ServiceStatus.ACTIVE);
        service.setIpAddress(ipAddress);
        serviceRepository.save(service);

        // Test: Delete the service
        serviceRepository.deleteById(service.getId());

        // Assertions: Ensure the service no longer exists
        List<Service> services = serviceRepository.findByIpAddressId(ipAddress.getId());
        assertThat(services).isEmpty();
    }

    @Test
    public void testFindAllServices() {
        // Setup: Create and save multiple Services
        IpAddress ip1 = new IpAddress();
        ip1.setIp("10.0.0.3");
        ip1.setHostname("host1");
        ipAddressRepository.save(ip1);

        IpAddress ip2 = new IpAddress();
        ip2.setIp("10.0.0.4");
        ip2.setHostname("host2");
        ipAddressRepository.save(ip2);

        Service service1 = new Service();
        service1.setName("Service 1");
        service1.setPort(5000);
        service1.setStatus(ServiceStatus.ACTIVE);
        service1.setIpAddress(ip1);
        serviceRepository.save(service1);

        Service service2 = new Service();
        service2.setName("Service 2");
        service2.setPort(4000);
        service2.setStatus(ServiceStatus.INACTIVE);
        service2.setIpAddress(ip2);
        serviceRepository.save(service2);

        // Test: Find all services
        List<Service> services = serviceRepository.findAll();

        // Assertions
        assertThat(services).hasSize(2);
    }
}