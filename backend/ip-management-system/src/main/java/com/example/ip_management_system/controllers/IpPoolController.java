package com.example.ip_management_system.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;


@Controller
public class IpPoolController {

    @Autowired
    private IpPoolRepository ipPoolRepo;


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
    public String save(IpPool ipPool, Model model) {
        System.out.println("IpPool being saved: " + ipPool);
        ipPoolRepo.save(ipPool);
        return "redirect:/ippoollist";
    }

    @RequestMapping(value="/updateippool/{id}", method=RequestMethod.POST)
    public String update(@PathVariable("id") Long id, @ModelAttribute IpPool updatedIpPool, Model model) {
        IpPool existingPool = ipPoolRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID: " + id));

        existingPool.setName(updatedIpPool.getName());
        existingPool.setStartIp(updatedIpPool.getStartIp());
        existingPool.setEndIp(updatedIpPool.getEndIp());
        existingPool.setDescription(updatedIpPool.getDescription());

        ipPoolRepo.save(existingPool);
        return "redirect:/ippoollist";
    }
    
    //delete ip pool by id
    @RequestMapping(value="/deleteippool/{id}", method=RequestMethod.GET)
    public String deleteIpPool(@PathVariable("id") Long id, Model model) {
        ipPoolRepo.deleteById(id);
        return "redirect:/ippoollist";
    }

    //show edit ip pool form
    @RequestMapping(value="/editippool/{id}", method=RequestMethod.GET)
    public String editIpPool(@PathVariable("id") Long id, Model model) {
        IpPool ipPool = ipPoolRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid IP pool Id"+id));
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
