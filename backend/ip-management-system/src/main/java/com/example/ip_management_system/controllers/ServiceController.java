package com.example.ip_management_system.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ip_management_system.repositories.IpPoolRepository;
import com.example.ip_management_system.repositories.ServiceRepository;
import com.example.ip_management_system.models.Service;

@Controller
public class ServiceController {

    @Autowired
    private ServiceRepository serviceRepo;

    @Autowired
    private IpPoolRepository ipPoolRepo;

    // Show all services
    @RequestMapping(value={"/", "servicelist"})
    public String getServices(Model model) {
        model.addAttribute("services", serviceRepo.findAll());
        return "servicelist";
    }

    // Show add service form
    @RequestMapping("/addservice")
    public String addServiceForm(Model model) {
        model.addAttribute("service", new Service());
        model.addAttribute("ippools", ipPoolRepo.findAll()); // For associating with IpPool
        return "addservice";
    }

    // Save new service
    @RequestMapping(value="/saveservice", method=RequestMethod.POST)
    public String saveService(Service service) {
        serviceRepo.save(service);
        return "redirect:/servicelist";
    }

    // Delete service by ID
    @RequestMapping(value="/deleteservice/{id}", method=RequestMethod.GET)
    public String deleteService(@PathVariable("id") Long id) {
        serviceRepo.deleteById(id);
        return "redirect:/servicelist";
    }

    // Edit an existing service
    @RequestMapping(value="/editservice/{id}", method=RequestMethod.GET)
    public String editService(@PathVariable("id") Long id, Model model) {
        Service service = serviceRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid service Id: " + id));
        model.addAttribute("service", service);
        model.addAttribute("ippools", ipPoolRepo.findAll()); // For associating with IpPool
        return "editservice";
    }

    // RESTful service to get all services
    @RequestMapping(value="/services", method=RequestMethod.GET)
    public @ResponseBody List<Service> serviceListRest() {
        return serviceRepo.findAll();
    }

    // RESTful service to get a service by ID
    @RequestMapping(value="/service/{id}", method=RequestMethod.GET)
    public @ResponseBody Service findServiceRest(@PathVariable("id") Long id) {
        Optional<Service> service = serviceRepo.findById(id);
        return service.orElse(null);
    }

    // RESTful service to add a new service
    @RequestMapping(value="/service/add", method=RequestMethod.POST)
    public @ResponseBody Service addService(@RequestBody Service newService) {
        return serviceRepo.save(newService);
    }

    // RESTful service to delete a service by ID
    @RequestMapping(value="service/delete/{id}", method=RequestMethod.GET)
    public @ResponseBody String deleteServiceRest(@PathVariable("id") Long id) {
        serviceRepo.deleteById(id);
        return "Service deleted";
    }
}
