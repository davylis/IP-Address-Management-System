package com.example.ip_management_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.example.ip_management_system.repositories.ServiceRepository;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.ip_management_system.models.Service;
import org.springframework.ui.Model;


@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepo;

    @RequestMapping("/services/{ipAddressId}")
    public String showServicesForIp(@PathVariable("ipAddressId") Long ipAddressId, Model model) {
        List<Service> services = serviceRepo.findByIpAddressId(ipAddressId);
        model.addAttribute("services", services);
        return "services";
    }
    
}
