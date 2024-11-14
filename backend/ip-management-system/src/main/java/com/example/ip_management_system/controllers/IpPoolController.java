package com.example.ip_management_system.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;
import com.example.ip_management_system.repositories.ServiceRepository;

@Controller
public class IpPoolController {

    @Autowired
    private IpPoolRepository ipPoolRepo;

    @Autowired
    private ServiceRepository serviceRepo;

    //show all ip pools
    @RequestMapping(value={"/", "ippoollist"})
    public String getIpPools(Model model) {
        model.addAttribute("ippools", ipPoolRepo.findAll());
        return "ippoollist";
    }
 
    //show add ip pool form
   @RequestMapping("/addippool")
   public String addIpPoolForm(Model model) {
        model.addAttribute("ipPool", new IpPool());
       return "addippool";
   }
   
    //save new ip pool
    @RequestMapping(value="/save", method=RequestMethod.POST)
    public String save(IpPool ipPool) {
        ipPoolRepo.save(ipPool);
        return "redirect:/ippoollist";
    }
    
    //delete ip pool by id
    @RequestMapping(value="/deleteippool/{id}", method=RequestMethod.GET)
    public String deleteIpPool(@PathVariable("id") Long id, Model model) {
        ipPoolRepo.deleteById(id);
        return "redirect:/ippoollist";
    }

    //edit an existing ip pool
    @RequestMapping(value="/edit/{id}", method=RequestMethod.GET)
    public String editIpPool(@PathVariable("id") Long id, Model model) {
        IpPool ipPool = ipPoolRepo.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Invalid IP pool Id"+id));
            model.addAttribute("ipPool", ipPool);
            return "editippool";
    }

    //RESTful service to get all ip pools
    @RequestMapping(value="/ippools", method=RequestMethod.GET)
    public @ResponseBody List<IpPool> ipPoolListRest() {
        return ipPoolRepo.findAll();
    }
    
    //RESTful service to get an ip pool by id
    @RequestMapping(value="/ippool/{id}", method=RequestMethod.GET)
    public @ResponseBody IpPool findIpPoolRest(@PathVariable("id") Long id) {
        Optional<IpPool> ipPool = ipPoolRepo.findById(id);
        return ipPool.orElse(null);
    }
    
    //RESTful service to add a new ip pool
    @RequestMapping(value="/ippool/add", method=RequestMethod.POST)
    public @ResponseBody IpPool addIpPool(@RequestBody IpPool newIpPool) {
        return ipPoolRepo.save(newIpPool);
    }
    
    //RESTful service to delete an ip pool by id
    @RequestMapping(value="ippool/delete/{id}", method=RequestMethod.GET)
    public @ResponseBody String deleteIpPoolRest(@PathVariable("id") Long id) {
        ipPoolRepo.deleteById(id);
        return "IP Pool deleted";
    }
    
}
