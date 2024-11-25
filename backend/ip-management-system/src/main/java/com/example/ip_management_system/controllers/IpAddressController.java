package com.example.ip_management_system.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String addIpForm(@PathVariable("ipPoolId") Long ipPoolId, Model model) {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpPool(ipPoolRepo.findById(ipPoolId).orElseThrow());
        model.addAttribute("ipAddress", ipAddress);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "addipaddress";
    }

    //save new ip address
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveIp(@ModelAttribute IpAddress ipAddress, @RequestParam Long ipPoolId) {
        IpPool ipPool = ipPoolRepo.findById(ipPoolId).orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID: " + ipPoolId));
        ipAddress.setIpPool(ipPool);

        ipAddressRepo.save(ipAddress);
        return "redirect:/ipaddresses/" + ipAddress.getIpPool().getId();
    }

    //edit ip address
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editIpForm(@PathVariable("id") Long id, Model model) {
        IpAddress ipAddress = ipAddressRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        model.addAttribute("ipAddress", ipAddress);
        return "editipaddress";
    }

    //update ip address
    @RequestMapping(value = "/update/{id}", method = RequestMethod.POST)
    public String updateIp(@PathVariable("id") Long id, @ModelAttribute IpAddress ipAddress) {
        IpAddress existingIp = ipAddressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        existingIp.setIp(ipAddress.getIp());
        existingIp.setHostname(ipAddress.getHostname());
        ipAddressRepo.save(existingIp);
        return "redirect:/ipaddresses/" + existingIp.getIpPool().getId();
    }

    //delete ip address by id
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String deleteIp(@PathVariable("id") Long id) {
        IpAddress existingIp = ipAddressRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));

        List<Service> services = serviceRepo.findByIpAddressId(id);
        serviceRepo.deleteAll(services);

        ipAddressRepo.delete(existingIp);
        
        return "redirect:/ipaddresses/" + existingIp.getIpPool().getId();
    }

    // RESTful service to get all ip addresses
    @RequestMapping(value="/api/ipaddresses", method=RequestMethod.GET)
    public @ResponseBody List<IpAddress> getAllIpAddresses() {
        return ipAddressRepo.findAll();
    }

    // RESTful service to get an ip address by ID
    @RequestMapping(value="/api/ipaddresses/{id}", method=RequestMethod.GET)
    public @ResponseBody IpAddress getIpAddressById(@PathVariable("id") Long id) {
        return ipAddressRepo.findById(id).orElse(null);
    }

    // RESTful service to add a new ip address
    @RequestMapping(value="/api/ipaddresses/add", method=RequestMethod.POST)
    public @ResponseBody IpAddress addIpAddress(@RequestBody IpAddress newIpAddress) {
        return ipAddressRepo.save(newIpAddress);
    }

    // RESTful service to delete an ip by ID
    @RequestMapping(value="/api/ipaddresses/delete/{id}", method=RequestMethod.GET)
    public @ResponseBody String deleteIpAddress(@PathVariable("id") Long id) {
        ipAddressRepo.deleteById(id);
        return "IP Address deleted";
    }
}
