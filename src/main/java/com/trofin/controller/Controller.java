package com.trofin.controller;

import com.trofin.entity.User;
import com.trofin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "all-users";
    }

    @GetMapping(value = "/updateInfo")
    public String showUserInfo(@RequestParam("userId") int id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "user-info";
    }

    @PostMapping(value = "/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/";
    }

    @GetMapping("/addNewUser")
    public String addNewUser(Model model) {
        model.addAttribute("user", new User());
        return "user-info";
    }

    @GetMapping(value = "/deleteUser")
    public String deleteEmployee(@RequestParam("userId") int id) {
        userService.deleteUser(id);
        return "redirect:/";
    }
}
