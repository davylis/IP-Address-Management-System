package com.example.ip_management_system.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RootController {

   @GetMapping("/")
   public String redirectToIpPools() {
      return "redirect:/ippools/"; // Ensure this redirects to the right path
   }
}
