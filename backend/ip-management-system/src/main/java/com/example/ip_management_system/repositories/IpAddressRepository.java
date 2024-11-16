package com.example.ip_management_system.repositories;

import com.example.ip_management_system.models.IpAddress;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IpAddressRepository extends JpaRepository<IpAddress, Long> {
    List<IpAddress> findByIpPoolId(Long ipPoolId);
    IpAddress findByIp(String ip);
    void deleteByIp(String ip);
    
}
