package com.example.ip_management_system.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ip_management_system.models.IpPool;

public interface IpPoolRepository extends JpaRepository<IpPool, Long> {

}
