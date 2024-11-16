package com.example.ip_management_system.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.repositories.ServiceRepository;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMethod;




@Controller
@RequestMapping("/services")
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private IpAddressRepository ipAddressRepo;

    //show services for a specific ip address
    @RequestMapping("/{ipAddressId}")
    public String showServicesForIp(@PathVariable("ipAddressId") Long ipAddressId, Model model) {
        List<Service> services = serviceRepo.findByIpAddressId(ipAddressId);
        model.addAttribute("services", services);
        model.addAttribute("ipAddressId", ipAddressId);
        return "services";
    }

    //add new service form
    @RequestMapping(value = "/addservice/{ipAddressId}", method=RequestMethod.GET)
    public String addServiceForm(@PathVariable("ipAddressId") Long ipAddressId, Model model) {
        Service service = new Service();
        service.setIpAddress(ipAddressRepo.findById(ipAddressId).orElseThrow(() -> new IllegalArgumentException("Invalid IP Address Id: " + ipAddressId)));
        model.addAttribute("service", service);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "addservice";
    }

    //save new service
    @RequestMapping(value="/saveip", method=RequestMethod.POST)
    public String saveService(@ModelAttribute Service service) {
        serviceRepo.save(service);
        return "redirect:/services/" + service.getIpAddress().getId();
    }

    //edit service
    @RequestMapping(value = "/editservice/{serviceId}", method=RequestMethod.GET)
    public String editService(@PathVariable("serviceId") Long id, Model model) {
        Service service = serviceRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        model.addAttribute("service", service);
        return "editservice";
    }

    //update service
    @RequestMapping(value="/updateservice/{id}", method = RequestMethod.POST)
    public String updateService(@PathVariable("id") Long id, @ModelAttribute Service service) {
        Service exsistingService = serviceRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        exsistingService.setName(service.getName());
        exsistingService.setPort(service.getPort());
        exsistingService.setDescription(service.getDescription());
        exsistingService.setUrlLink(service.getUrlLink());
        exsistingService.setStatus(service.getStatus());
        serviceRepo.save(exsistingService);
        return "redirect:/services/" + exsistingService.getIpAddress().getId();
    }

    //delete service
    @RequestMapping(value = "/deleteip/{id}", method=RequestMethod.GET)
    public String deleteService(@PathVariable("id") Long id) {
        Service service = serviceRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid Service Id: " + id));
        Long ipAddressId = service.getIpAddress().getId();
        ipAddressRepo.deleteById(id);
        return "redirect:/services"+ipAddressId;

    
    }
}