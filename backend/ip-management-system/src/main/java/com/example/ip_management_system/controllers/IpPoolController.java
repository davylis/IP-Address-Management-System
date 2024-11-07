package com.example.ip_management_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.*;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;

@RestController
@RequestMapping("/api/ip-pools")
public class IpPoolController {

    @Autowired
    private IpPoolRepository ipPoolRepo;

    @GetMapping
    public List<IpPool> getAllIpPools(){
        return ipPoolRepo.findAll();
    }
    
    @PostMapping
    public IpPool createIpPool(@RequestBody IpPool ipPool) {
        return ipPoolRepo.save(ipPool);
    }

}
