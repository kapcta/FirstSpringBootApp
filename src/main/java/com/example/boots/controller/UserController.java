package com.example.boots.controller;

import com.example.boots.entity.Role;
import com.example.boots.entity.User;
import com.example.boots.repository.UserRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public String userList(Model model) {
        Iterable<User> users = userRepository.findAll();
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("/{id}")
    public String userEdit(@PathVariable(value = "id") Long id, Model model) {
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("user", user);


        //model.addAttribute("roles", Role.values());
        return "user-edit-form";
    }

    @PostMapping("/{id}")
    public String userSave(
            @PathVariable(value = "id") Long id,
            @RequestParam String username,
            @RequestParam String password,
            Model model
            /*@RequestParam Map<String, String> form,
            @RequestParam User user

             */
    ) {
        User userForChange = userRepository.findById(id).orElseThrow();
        userForChange.setUsername(username);
        userForChange.setPassword(password);
       /* Set<String> roles = Arrays.stream(Role.values()).map(Role::name).collect(Collectors.toSet());

        user.getRoles().clear();
        for (String key :
                form.keySet()) {
            if(roles.contains(key))
                user.getRoles().add(Role.valueOf(key));

        }

        */

        userRepository.save(userForChange);

        return "redirect:/user";
    }

    @PostMapping("/{id}/remove")
    public String deleteUser(@PathVariable(value = "id") Long id) {
        User userDeleting = userRepository.findById(id).orElseThrow();
        userRepository.delete(userDeleting);
        return "redirect:/user";
    }
}
