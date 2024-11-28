package com.example.ip_management_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.repositories.ServiceRepository;
import org.springframework.web.bind.annotation.*;
import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private IpAddressRepository ipAddressRepo;

    // show services for a specific ip address
    @RequestMapping("/{ipAddressId}")
    public String showServicesForIp(@PathVariable Long ipAddressId, Model model) {
        List<Service> services = serviceRepo.findByIpAddressId(ipAddressId);
        model.addAttribute("services", services);
        model.addAttribute("ipAddressId", ipAddressId);

        IpAddress ipAddress = ipAddressRepo.findById(ipAddressId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid IP Address ID: " + ipAddressId));
        Long ipPoolId = ipAddress.getIpPool().getId();
        model.addAttribute("ipPoolId", ipPoolId);

        return "services";
    }

    // add new service form
    @RequestMapping("/add/{ipAddressId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addServiceForm(@PathVariable Long ipAddressId, Model model) {
        Service service = new Service();
        service.setIpAddress(ipAddressRepo.findById(ipAddressId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid IP Address Id: " + ipAddressId)));
        model.addAttribute("service", service);
        model.addAttribute("ipAddressId", ipAddressId);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "addservice";
    }

    // save new service
    @PostMapping("/save/{ipAddressId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveService(@PathVariable Long ipAddressId, @ModelAttribute Service service) {
        IpAddress existingIp = ipAddressRepo.findById(ipAddressId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID: " + ipAddressId));
        service.setIpAddress(existingIp);

        serviceRepo.save(service);
        return "redirect:/services/" + service.getIpAddress().getId();
    }

    // edit service
    @GetMapping("/edit/{serviceId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editService(@PathVariable("serviceId") Long id, Model model) {
        Service service = serviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        if (service.getStatus() == null) {
            service.setStatus(ServiceStatus.INACTIVE);
        }
        model.addAttribute("service", service);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "editservice";
    }

    // update service
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateService(@PathVariable Long id, @ModelAttribute Service service) {
        Service exsistingService = serviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        exsistingService.setName(service.getName());
        exsistingService.setPort(service.getPort());
        exsistingService.setDescription(service.getDescription());
        exsistingService.setUrlLink(service.getUrlLink());
        exsistingService.setStatus(service.getStatus());
        serviceRepo.save(exsistingService);
        return "redirect:/services/" + exsistingService.getIpAddress().getId();
    }

    // delete service
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteService(@PathVariable Long id) {
        Service service = serviceRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        Long ipAddressId = service.getIpAddress().getId();
        serviceRepo.deleteById(id);
        return "redirect:/services/" + ipAddressId;

    }
}