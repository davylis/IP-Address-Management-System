package com.example.ip_management_system.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;

@Service
public class IpPoolService {
    @Autowired
    private IpPoolRepository ipPoolRepository;

    public List<IpPool> getAllIpPools(){
        return ipPoolRepository.findAll();
    }

    public IpPool createIpPool(IpPool ipPool) {
        return ipPoolRepository.save(ipPool);
    }

}
