package com.bry.crud.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bry.crud.domain.user.RequestUser;
import com.bry.crud.domain.user.User;
import com.bry.crud.domain.user.UserRepository;
import com.bry.crud.util.CPFValidator;

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
    if (!CPFValidator.isValid(data.cpf())) {
      return ResponseEntity.badRequest().body("CPF is not valid");
    }
    User existingUser = repository.findByCpf(data.cpf());
    if (existingUser != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF already exists");
    }
    User newUser = new User(data);
    repository.save(newUser);
    return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
  }

  @PutMapping
  public ResponseEntity updateUser(@RequestBody @Valid RequestUser data) {
    User existingUser = repository.findByCpf(data.cpf());
    if (existingUser == null) {
        return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    }
    existingUser.setName(data.name());
    repository.save(existingUser);

    return ResponseEntity.ok(existingUser);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity deleteUser(@PathVariable("id") String id) {
    Optional<User> userOptional = repository.findById(id);
      if (userOptional.isPresent()) {
        repository.deleteById(id);
        return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
    } else {
        return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    }
  }

}