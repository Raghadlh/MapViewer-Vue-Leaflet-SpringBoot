// here
package com.raghad.mapviewerbackend.Controller;

import com.raghad.mapviewerbackend.Model.MyAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raghad.mapviewerbackend.Model.MyAppUser;

@RestController
public class RegisterationController {
     @Autowired
     private MyAppUserRepository myAppUserRepository;

     @Autowired
     private PasswordEncoder passwordEncoder;

    @PostMapping (value= "/req/signup", consumes ="application/json")
    public MyAppUser creatUser(@RequestBody MyAppUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return myAppUserRepository.save(user);


    }
    
}
