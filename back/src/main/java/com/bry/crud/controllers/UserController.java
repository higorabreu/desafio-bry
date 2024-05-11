package com.bry.crud.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bry.crud.domain.user.RequestUser;
import com.bry.crud.domain.user.User;
import com.bry.crud.domain.user.UserRepository;
import com.bry.crud.util.CPFValidator;

import jakarta.validation.Valid;

@RestController
public class UserController {

  @Autowired
  private UserRepository repository;

  // GET /users/:page
  @GetMapping("users/{page}")
  public ResponseEntity getAllUsers(
        @PathVariable int page,
        @RequestParam(defaultValue = "1") int size) {
  
      Pageable pageable = PageRequest.of(page, size);
      Page<User> usersPage = repository.findAll(pageable);
  
      if (usersPage.isEmpty()) {
          return ResponseEntity.noContent().build();
      } else {
          List<User> users = usersPage.getContent();
          users.forEach(user -> user.setCpf(user.obfuscateCpf()));
          return ResponseEntity.ok(users);
      }
  }

  // GET /user/:id
  @GetMapping("/user/{id}")
  public ResponseEntity getUserById(@PathVariable("id") String id) {
    Optional<User> userOptional = repository.findById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      user.setCpf(user.obfuscateCpf());
      return ResponseEntity.ok(user);
    } else {
        return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    }
  }

  // POST /user
  @PostMapping("/user")
  public ResponseEntity createUser(@RequestBody @Valid RequestUser data) {
    if (!CPFValidator.isValid(data.cpf())) {
      return ResponseEntity.badRequest().body("CPF is not valid");
    }
    User existingUser = repository.findByCpf(data.cpf());
    if (existingUser != null) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF already exists");
    } else {
      User newUser = new User(data);
      repository.save(newUser);
      return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
    }
  }

  // PUT /user
  @PutMapping("/user")
  public ResponseEntity updateUser(@RequestBody @Valid RequestUser data) {
    User existingUser = repository.findByCpf(data.cpf());
    if (existingUser == null) {
        return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    } else {
      existingUser.setName(data.name());
      repository.save(existingUser);
      return ResponseEntity.ok(existingUser);
    }
  }

  // DELETE /user/:id
  @DeleteMapping("/user/{id}")
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