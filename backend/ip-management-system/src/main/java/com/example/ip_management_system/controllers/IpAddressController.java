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
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.ip_management_system.repositories.IpPoolRepository;
import com.example.ip_management_system.repositories.ServiceRepository;
import com.example.ip_management_system.repositories.IpAddressRepository;
import com.example.ip_management_system.models.IpAddress;
import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.models.Service;
import com.example.ip_management_system.models.ServiceStatus;

@Controller
public class IpAddressController {

    @Autowired
    private IpAddressRepository ipAddressRepo;

    @Autowired
    private IpPoolRepository ipPoolRepo;

    @Autowired
    private ServiceRepository serviceRepo;

    //show ip's for a specific pool
    @RequestMapping(value = "/ippools/{id}", method = RequestMethod.GET)
    public String showIpsInPool(@PathVariable("id") Long id, Model model) {
        List<IpAddress> ipAddresses = ipAddressRepo.findByIpPoolId(id);
        model.addAttribute("ipAddresses", ipAddresses);
        model.addAttribute("ipPoolId", id);
        IpPool ipPool = ipPoolRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID"));
        model.addAttribute("ipPool", ipPool);

        for (IpAddress ip : ipAddresses){
            List<Service> services = serviceRepo.findByIpAddress(ip);
            ip.setServices(services);
        }

        return "ipaddresses";
    }

    //add new ip address to a pool
    @RequestMapping("/addip/{ipPoolId}")
    public String addIpForm(@PathVariable("ipPoolId") Long ipPoolId, Model model) {
        IpAddress ipAddress = new IpAddress();
        ipAddress.setIpPool(ipPoolRepo.findById(ipPoolId).orElseThrow());
        model.addAttribute("ipAddress", ipAddress);
        model.addAttribute("serviceStatusValues", ServiceStatus.values());
        return "addipaddress";
    }

    //save new ip address
    @RequestMapping(value = "/saveip", method = RequestMethod.POST)
    public String saveIp(@ModelAttribute IpAddress ipAddress) {
        ipAddressRepo.save(ipAddress);
        return "redirect:/ippool/" + ipAddress.getIpPool().getId();
    }

    //edit ip address
    @RequestMapping(value = "/editip/{id}", method = RequestMethod.GET)
    public String editIpForm(@PathVariable("id") Long id, Model model) {
        IpAddress ipAddress = ipAddressRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        model.addAttribute("ipAddress", ipAddress);
        return "editipaddress";
    }

    //update ip address
    @RequestMapping(value = "/updateip/{id}", method = RequestMethod.POST)
    public String updateIp(@PathVariable("id") Long id, @ModelAttribute IpAddress ipAddress) {
        IpAddress existingIp = ipAddressRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        existingIp.setIp(ipAddress.getIp());
        existingIp.setHostname(ipAddress.getHostname());
        ipAddressRepo.save(existingIp);
        return "redirect:/ippool/" + existingIp.getIpPool().getId();
    }

    //delete ip address by id
    @RequestMapping(value = "/deleteip/{id}", method = RequestMethod.GET)
    public String deleteIp(@PathVariable("id") Long id) {
        IpAddress ipAddress = ipAddressRepo.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Invalid IP address Id: " + id));
        Long ipPoolId = ipAddress.getIpPool().getId();
        ipAddressRepo.deleteById(id);
        return "redirect:/ippoollist"+ipPoolId;
    }

    // RESTful service to get all ip addresses
    @RequestMapping(value="/ipaddresses", method=RequestMethod.GET)
    public @ResponseBody List<IpAddress> getAllIpAddresses() {
        return ipAddressRepo.findAll();
    }

    // RESTful service to get an ip address by ID
    @RequestMapping(value="/ipaddresses/{id}", method=RequestMethod.GET)
    public @ResponseBody IpAddress getIpAddressById(@PathVariable("id") Long id) {
        return ipAddressRepo.findById(id).orElse(null);
    }

    // RESTful service to add a new ip address
    @RequestMapping(value="/ipaddresses/add", method=RequestMethod.POST)
    public @ResponseBody IpAddress addIpAddress(@RequestBody IpAddress newIpAddress) {
        return ipAddressRepo.save(newIpAddress);
    }

    // RESTful service to delete an ip by ID
    @RequestMapping(value="ipaddresses/delete/{id}", method=RequestMethod.GET)
    public @ResponseBody String deleteIpAddress(@PathVariable("id") Long id) {
        ipAddressRepo.deleteById(id);
        return "IP Address deleted";
    }
}
