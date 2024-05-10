package com.bry.crud.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bry.crud.domain.user.RequestUser;
import com.bry.crud.domain.user.User;
import com.bry.crud.domain.user.UserRepository;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired
  private UserRepository repository;

  @GetMapping
  public ResponseEntity getAllUsers() {
    var allUsers = repository.findAll();
    return ResponseEntity.ok(allUsers);
  }

   @PostMapping
    public ResponseEntity createUser(@RequestBody @Valid RequestUser data) {
      User newUser = new User(data);
      System.out.println(newUser);
      repository.save(newUser);
      return ResponseEntity.ok().build();
    }

}