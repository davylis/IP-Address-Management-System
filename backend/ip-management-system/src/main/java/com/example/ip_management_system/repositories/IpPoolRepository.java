package com.example.ip_management_system.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ip_management_system.models.IpPool;
import org.springframework.stereotype.Repository;

@Repository
public interface IpPoolRepository extends JpaRepository<IpPool, Long> {
    List<IpPool> findByName(String name);

    List<IpPool> findByStartIp(String startIp);

    List<IpPool> findByStartIpAndEndIp(String startIp, String endIp);
}
