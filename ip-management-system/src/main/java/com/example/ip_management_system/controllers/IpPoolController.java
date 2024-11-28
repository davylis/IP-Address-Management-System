package com.example.ip_management_system.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.ip_management_system.models.IpPool;
import com.example.ip_management_system.repositories.IpPoolRepository;

@Controller
@RequestMapping("/ippools")
public class IpPoolController {

    @Autowired
    private IpPoolRepository ipPoolRepo;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    // show all ip pools
    @GetMapping({ "", "/" })
    public String getIpPools(Model model) {
        model.addAttribute("ippools", ipPoolRepo.findAll());
        return "ippoollist";
    }

    // show add ip pool form
    @GetMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addIpPoolForm(Model model) {
        model.addAttribute("ipPool", new IpPool());
        return "addippool";
    }

    // save new ip pool
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/save")
    public String save(IpPool ipPool, Model model) {
        System.out.println("IpPool being saved: " + ipPool);
        ipPoolRepo.save(ipPool);
        return "redirect:/ippools/";
    }

    @PostMapping("/update/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String update(@PathVariable Long id, @ModelAttribute IpPool updatedIpPool, Model model) {
        IpPool existingPool = ipPoolRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid IP Pool ID: " + id));

        existingPool.setName(updatedIpPool.getName());
        existingPool.setStartIp(updatedIpPool.getStartIp());
        existingPool.setEndIp(updatedIpPool.getEndIp());
        existingPool.setDescription(updatedIpPool.getDescription());

        ipPoolRepo.save(existingPool);
        return "redirect:/ippools/";
    }

    // delete ip pool by id
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteIpPool(@PathVariable Long id, Model model) {
        ipPoolRepo.deleteById(id);
        return "redirect:/ippools";
    }

    // show edit ip pool form
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editIpPool(@PathVariable Long id, Model model) {
        IpPool ipPool = ipPoolRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid IP pool Id" + id));
        model.addAttribute("ipPool", ipPool);
        return "editippool";
    }

    // RESTful service to get all ip pools
    @GetMapping("/api/ippools")
    public @ResponseBody List<IpPool> ipPoolListRest() {
        return ipPoolRepo.findAll();
    }

    // RESTful service to get an ip pool by id
    @GetMapping("/api/ippool/{id}")
    public @ResponseBody IpPool findIpPoolRest(@PathVariable Long id) {
        Optional<IpPool> ipPool = ipPoolRepo.findById(id);
        return ipPool.orElse(null);
    }

    // RESTful service to add a new ip pool
    @PostMapping("/api/ippool/add")
    public @ResponseBody IpPool addIpPool(@RequestBody IpPool newIpPool) {
        return ipPoolRepo.save(newIpPool);
    }

    // RESTful service to delete an ip pool by id
    @GetMapping("/api/ippool/delete/{id}")
    public @ResponseBody String deleteIpPoolRest(@PathVariable Long id) {
        ipPoolRepo.deleteById(id);
        return "IP Pool deleted";
    }

}
