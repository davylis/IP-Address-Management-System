package com.example.ip_management_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ip_management_system.models.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByIpAddressId(Long ipAddressId);
}

