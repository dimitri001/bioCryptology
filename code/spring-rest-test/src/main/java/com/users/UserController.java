package com.users;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.users.domain.User;

@RestController
public class UserController {

    @Autowired
    private UserRepository repository;
    
    // Find
    @GetMapping("/users")
    List<User> findAll() {
    	List<User> listUsers = repository.findAll();
        return listUsers;
    }

    // Save
    @PostMapping("/users")
    //return 201 instead of 200
    @ResponseStatus(HttpStatus.CREATED)
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Find
    @GetMapping("/users/{id}")
    User findOne(@PathVariable Long id) throws Exception {
        return repository.findById(id)
                .orElseThrow(() -> new Exception("Error with the id : "+id));
    }

    // Save or update
    @PutMapping("/users/{id}")
    User saveOrUpdate(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(x -> {
                    x.setName(newUser.getName());
                    x.setEmail(newUser.getEmail());
                    x.setAge(newUser.getAge());
                    x.setSex(newUser.getSex());
                  //  x.setActivated(newUser.getActivated());
                    return repository.save(x);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteBook(@PathVariable Long id) {
        repository.deleteById(id);
    }

    
}
