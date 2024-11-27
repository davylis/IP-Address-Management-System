package com.example.ip_management_system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;
import com.example.ip_management_system.models.User;
import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.repositories.IpPoolRepository;
import com.example.ip_management_system.repositories.ServiceRepository;
import com.example.ip_management_system.repositories.UserRepository;

@SpringBootApplication
public class IpManagementSystemApplication {
	private static final Logger log = LoggerFactory.getLogger(IpManagementSystemApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(IpManagementSystemApplication.class, args);
	}

	@Bean

	CommandLineRunner initDatabase(IpPoolRepository ipPoolRepository, IpAddressRepository ipAddressRepository,
			ServiceRepository serviceRepository, UserRepository userRepository) {
		return args -> {
			// Create IpPools
			IpPool ipPool1 = new IpPool();
			ipPool1.setName("Corporate Pool");
			ipPool1.setDescription("Main pool for corporate devices");
			ipPool1.setStartIp("192.168.0.1");
			ipPool1.setEndIp("192.168.0.255");
			ipPool1.setGateway("192.168.0.1");

			IpPool ipPool2 = new IpPool();
			ipPool2.setName("Test Pool");
			ipPool2.setDescription("Temporary pool for testing");
			ipPool2.setStartIp("10.0.0.1");
			ipPool2.setEndIp("10.0.0.255");
			ipPool2.setGateway("10.0.0.1");

			// Create IP Addresses and associate them with IpPools
			IpAddress ip1 = new IpAddress();
			ip1.setIp("192.168.0.10");
			ip1.setHostname("host1");
			ip1.setPort(8080);
			ip1.setIpPool(ipPool1);

			IpAddress ip2 = new IpAddress();
			ip2.setIp("192.168.0.11");
			ip2.setHostname("host2");
			ip2.setPort(9090);
			ip2.setIpPool(ipPool2);

			IpAddress ip3 = new IpAddress();
			ip3.setIp("10.0.0.10");
			ip3.setHostname("host3");
			ip3.setPort(6060);
			ip3.setIpPool(ipPool2);

			// Create Services and associate them with IpAddresses
			Service service1 = new Service();
			service1.setIpAddress(ip1);
			service1.setPort(80);
			service1.setName("Web Server");
			service1.setDescription("HTTP Service");
			service1.setUrlLink("http://192.168.0.10");
			service1.setStatus(ServiceStatus.ACTIVE);

			Service service12 = new Service();
			service12.setIpAddress(ip1);
			service12.setPort(8080);
			service12.setName("Web Server 2");
			service12.setDescription("HTTP Service");
			service12.setUrlLink("http://192.168.0.10");
			service12.setStatus(ServiceStatus.ACTIVE);

			Service service2 = new Service();
			service2.setIpAddress(ip2);
			service2.setPort(21);
			service2.setName("FTP Server");
			service2.setDescription("File Transfer Protocol");
			service2.setUrlLink("ftp://192.168.0.11");
			service2.setStatus(ServiceStatus.ACTIVE);

			Service service3 = new Service();
			service3.setIpAddress(ip3);
			service3.setPort(53);
			service3.setName("DNS Server");
			service3.setDescription("Domain Name System");
			service3.setUrlLink("dns://10.0.0.10");
			service3.setStatus(ServiceStatus.INACTIVE);



			// create users
			User user1 = new User("user", "{bcrypt}$2a$06$3jYRJrg0ghaaypjZ/.g4SethoeA51ph3UD4kZi9oPkeMTpjKU5uo6",
					"user@email.fi", "ROLE_USER");
			User user2 = new User("admin", "{bcrypt}$2a$10$0MMwY.IQqpsVc1jC8u7IJ.2rT8b0Cd3b3sfIBGV2zfgnPGtT4r0.C",
					"admin@email.fi", "ROLE_ADMIN");

			// // Save ipPools		
			// ipPoolRepository.save(ipPool1);
			// ipPoolRepository.save(ipPool2);
			// // Save ipAddresses
			// ipAddressRepository.save(ip1);
			// ipAddressRepository.save(ip2);
			// ipAddressRepository.save(ip3);
			// // Save Services
			// serviceRepository.save(service1);
			// serviceRepository.save(service12);
			// serviceRepository.save(service2);
			// serviceRepository.save(service3);
			// // Save Users
			// userRepository.save(user1);
			// userRepository.save(user2);

			// Fetch and log all records
			log.info("fetch all IP Pools");
			for (IpPool ipPool : ipPoolRepository.findAll()) {
				log.info(ipPool.toString());
			}

			log.info("fetch all IP Addresses");
			for (IpAddress ipAddress : ipAddressRepository.findAll()) {
				log.info(ipAddress.toString());
			}

			log.info("fetch all Services");
			for (Service service : serviceRepository.findAll()) {
				log.info(service.toString());
			}

		};
	};
}
