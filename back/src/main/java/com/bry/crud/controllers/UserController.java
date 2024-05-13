package com.bry.crud.controllers;

import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bry.crud.controllers.dto.RequestCreateUser;
import com.bry.crud.controllers.dto.RequestUpdateUser;
import com.bry.crud.controllers.dto.ResponseUser;
import com.bry.crud.domain.user.User;
import com.bry.crud.service.UserService;
import com.bry.crud.service.exceptions.UserAlreadyExistsException;
import com.bry.crud.service.exceptions.UserCreationFailureException;
import com.bry.crud.service.exceptions.UserDeleteFailureException;
import com.bry.crud.service.exceptions.UserFetchException;
import com.bry.crud.service.exceptions.UserNotFoundException;
import com.bry.crud.service.exceptions.UserUpdateFailureException;
import com.bry.crud.util.CPFValidator;
import com.bry.crud.util.PictureConverter;

import jakarta.validation.Valid;

@RestController
public class UserController {

  @Autowired
  private UserService userService;

  // GET /users/:page
  @GetMapping("users/{page}")
  public ResponseEntity<List<ResponseUser>> getAllUsers(
        @PathVariable int page,
        @RequestParam(defaultValue = "2") int size) {
      try {
        List<User> users = userService.getAllUsers(page, size);
        if (users.isEmpty()) {
          return ResponseEntity.noContent().build();
        } else {
          List<ResponseUser> responseUsers = new ArrayList<>();
          for (User user : users) {
            String picture = PictureConverter.byteArrayToBase64(user.getPicture());
            ResponseUser responseUser = new ResponseUser(user.getId(), user.getName(), user.obfuscateCpf(), picture);
            responseUsers.add(responseUser);
          }
          return ResponseEntity.ok(responseUsers);
        }
      } catch (UserFetchException ex){ 
          return ResponseEntity.internalServerError().body(null);
      }
  }
  
  // GET /user/:id
  @GetMapping("/user/{id}")
  public ResponseEntity<ResponseUser> getUserById(@PathVariable("id") String id) {
    Optional<User> userOptional = userService.getUserById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();
      String picture = PictureConverter.byteArrayToBase64(user.getPicture());
      ResponseUser responseUser = new ResponseUser(user.getId(), user.getName(), user.obfuscateCpf(), picture);
      return ResponseEntity.ok(responseUser);
    } else {
        return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    }
  }

  // POST /user
  @PostMapping("/user")
  public ResponseEntity<String> createUser(@RequestBody @Valid RequestCreateUser data) {
    if (!CPFValidator.isValid(data.cpf())) {
      return ResponseEntity.badRequest().body("CPF is not valid");
    }
    try {
      userService.createUser(data);
    } catch (UserCreationFailureException ex) {
        return ResponseEntity.internalServerError().body("Failed to create user");
    } catch (UserAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("CPF already exists");
    }
    return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
  }

  // PUT /user
  @PutMapping("/user")
  public ResponseEntity<String> updateUser(@RequestBody @Valid RequestUpdateUser data) {
    try {
      userService.updateUser(new User(data));
    } catch (UserNotFoundException ex) {
      return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    } catch (UserUpdateFailureException ex) {
      return ResponseEntity.internalServerError().body("Failed to update user");
    }
    return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
  }

  // DELETE /user/:id
  @DeleteMapping("/user/{id}")
  public ResponseEntity<String> deleteUser(@PathVariable("id") String id) {
    try {
      userService.deleteUser(id);
    } catch (UserNotFoundException ex) {
      return ResponseEntity.notFound().build(); // Retorna 404 se o usuário não for encontrado
    } catch (UserDeleteFailureException ex) {
      return ResponseEntity.internalServerError().body("Failed to delete user");
    }
    return ResponseEntity.ok().build(); // Retorna uma resposta de sucesso
  }
}