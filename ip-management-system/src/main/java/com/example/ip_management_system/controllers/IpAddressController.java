package com.example.ip_management_system.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.example.ip_management_system.repositories.IpPoolRepository;
import com.example.ip_management_system.repositories.ServiceRepository;
import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;

@Controller
@RequestMapping("/ipaddresses")
public class IpAddressController {

    @Autowired
    private IpAddressRepository ipAddressRepo;

    @Autowired
    private IpPoolRepository ipPoolRepo;

    @Autowired
    private ServiceRepository serviceRepo;

    //show ip's for a specific pool
    @RequestMapping("/{ipPoolId}")
    public String showIpsInPool(@PathVariable("ipPoolId") Long id, Model model) {
        IpPool ipPool = ipPoolRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID"));
        List<IpAddress> ipAddresses = ipAddressRepo.findByIpPoolId(id);

        model.addAttribute("ipAddresses", ipAddresses);
        model.addAttribute("ipPoolId", id);
        model.addAttribute("ipPool", ipPool);

        for (IpAddress ip : ipAddresses){
            List<Service> services = serviceRepo.findByIpAddress(ip);
            ip.setServices(services);
        }

        return "ipaddresses";
    }

    //add new ip address to a pool
    @RequestMapping("/add/{ipPoolId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addIpForm(@PathVariable Long ipPoolId, Model model) {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpPool(ipPoolRepo.findById(ipPoolId).orElseThrow());
        model.addAttribute("ipAddress", ipAddress);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "addipaddress";
    }

    //save new ip address
    @PostMapping("/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveIp(@ModelAttribute IpAddress ipAddress, @RequestParam Long ipPoolId) {
        IpPool ipPool = ipPoolRepo.findById(ipPoolId).orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID: " + ipPoolId));
        ipAddress.setIpPool(ipPool);

        ipAddressRepo.save(ipAddress);
        return "redirect:/ipaddresses/" + ipAddress.getIpPool().getId();
    }

    //edit ip address
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editIpForm(@PathVariable Long id, Model model) {
        IpAddress ipAddress = ipAddressRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        model.addAttribute("ipAddress", ipAddress);
        return "editipaddress";
    }

    //update ip address
    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String updateIp(@PathVariable Long id, @ModelAttribute IpAddress ipAddress) {
        IpAddress existingIp = ipAddressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        existingIp.setIp(ipAddress.getIp());
        existingIp.setHostname(ipAddress.getHostname());
        ipAddressRepo.save(existingIp);
        return "redirect:/ipaddresses/" + existingIp.getIpPool().getId();
    }

    //delete ip address by id
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteIp(@PathVariable Long id) {
        IpAddress existingIp = ipAddressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));

        List<Service> services = serviceRepo.findByIpAddressId(id);
        serviceRepo.deleteAll(services);

        ipAddressRepo.delete(existingIp);
        
        return "redirect:/ipaddresses/" + existingIp.getIpPool().getId();
    }

    // RESTful service to get all ip addresses
    @GetMapping("/api/ipaddresses")
    public @ResponseBody List<IpAddress> getAllIpAddresses() {
        return ipAddressRepo.findAll();
    }

    // RESTful service to get an ip address by ID
    @GetMapping("/api/ipaddresses/{id}")
    public @ResponseBody IpAddress getIpAddressById(@PathVariable Long id) {
        return ipAddressRepo.findById(id).orElse(null);
    }

    // RESTful service to add a new ip address
    @PostMapping("/api/ipaddresses/add")
    public @ResponseBody IpAddress addIpAddress(@RequestBody IpAddress newIpAddress) {
        return ipAddressRepo.save(newIpAddress);
    }

    // RESTful service to delete an ip by ID
    @GetMapping("/api/ipaddresses/delete/{id}")
    public @ResponseBody String deleteIpAddress(@PathVariable Long id) {
        ipAddressRepo.deleteById(id);
        return "IP Address deleted";
    }
}
