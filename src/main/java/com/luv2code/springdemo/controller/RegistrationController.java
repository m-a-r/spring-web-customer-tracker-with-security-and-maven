package com.luv2code.springdemo.controller;

import com.luv2code.springdemo.user.CrmUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private UserDetailsManager userDetailsManager;

    private BCryptPasswordEncoder passwordEncoder;

    private Map<String, String> roles;

    public RegistrationController() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);

        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @PostConstruct
    private void loadRoles() {
        roles = new HashMap<>();

        roles.put("", "");
        roles.put("ROLE_EMPLOYEE", "Employee");
        roles.put("ROLE_MANAGER", "Manager");
        roles.put("ROLE_ADMIN", "Admin");
    }

    @GetMapping("/showRegistrationForm")
    public String showRegistrationForm(Model theModel) {

        CrmUser crmUser = new CrmUser();

        theModel.addAttribute("crmUser", crmUser);
        theModel.addAttribute("roles", roles);

        return "registration-form";
    }

    @PostMapping("/processRegistrationForm")
    public String processRegistrationForm(@Valid @ModelAttribute CrmUser crmUser,
                                          BindingResult theBindingResult, Model theModel) {

        // form validation
        if (theBindingResult.hasErrors()) {
            theModel.addAttribute("crmUser", crmUser);
            theModel.addAttribute("roles", roles);
            theModel.addAttribute("registrationError", "Registration error(s)");
            return "registration-form";
        }

        // check if user exist
        boolean userExists = userDetailsManager.userExists(crmUser.getUserName());
        if (userExists) {
            theModel.addAttribute("registrationError", "User already exists");
            theModel.addAttribute("crmUser", crmUser);
            theModel.addAttribute("roles", roles);
            return "registration-form";
        }

        // encrypt password
        String encodedPassword = passwordEncoder.encode(crmUser.getPassword());

        // prepend the encoding alghoritm id
        String prependedEncryptedPassword = "{bcrypt}" + encodedPassword;

        // assign user role(s)
//        List<GrantedAuthority> authorityList = new ArrayList<>();
//        SimpleGrantedAuthority authEmployee = new SimpleGrantedAuthority("ROLE_EMPLOYEE");
//        authorityList.add(authEmployee);
        List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList("ROLE_EMPLOYEE");
        if(!crmUser.getFormRole().equals("ROLE_EMPLOYEE")) {
            SimpleGrantedAuthority authFormRole = new SimpleGrantedAuthority(crmUser.getFormRole());
            authorityList.add(authFormRole);
        }

//        prependedEncryptedPassword = "{bcrypt}$2a$04$eFytJDGtjbThXa80FyOOBuFdK2IwjyWefYkMpiBEFlpBwDH.5PM0K";

        // create User object from spring security framework
        User user = new User(crmUser.getUserName(), prependedEncryptedPassword, authorityList);

        // save user in the database
        userDetailsManager.createUser(user);

        return "registration-confirmation";
    }
}
