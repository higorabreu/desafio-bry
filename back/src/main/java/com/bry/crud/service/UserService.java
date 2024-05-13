package com.bry.crud.service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import com.bry.crud.controllers.dto.RequestCreateUser;
import com.bry.crud.controllers.dto.RequestUser;
import com.bry.crud.domain.user.User;
import com.bry.crud.domain.user.UserRepository;
import com.bry.crud.service.exceptions.UserAlreadyExistsException;
import com.bry.crud.service.exceptions.UserCreationFailureException;
import com.bry.crud.service.exceptions.UserDeleteFailureException;
import com.bry.crud.service.exceptions.UserFetchException;
import com.bry.crud.service.exceptions.UserNotFoundException;
import com.bry.crud.service.exceptions.UserUpdateFailureException;


@Service
public class UserService {

  private static Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository repository;

  public Optional<User> getUserById(String id) {
    Optional<User> userOptional = repository.findById(id);
      return userOptional;
  }

  public List<User> getAllUsers(int page, int size) throws UserFetchException{
    try {
      Pageable pageable = PageRequest.of(page, size);
      Page<User> usersPage = repository.findAll(pageable);

      if (usersPage.isEmpty()) {
        return Collections.emptyList();
        } else {
          List<User> users = usersPage.getContent();
          users.forEach(user -> user.setCpf(user.obfuscateCpf()));
          return users;
        }
    } catch (Exception e){
        logger.error("Failed to fetch users", e);
        throw new UserFetchException();
    }
  }

  public void createUser(RequestCreateUser data) throws UserCreationFailureException, UserAlreadyExistsException {
    Optional<User> userOptional = repository.findByCpf(data.cpf());
    if (userOptional.isPresent()) {
      logger.warn("Cpf already exists");
      throw new UserAlreadyExistsException();
    }
    User newUser = new User(data);
    try {
      repository.save(newUser);
    } catch (Exception e) {
        logger.error("Failed to create user", e);
        throw new UserCreationFailureException();
    }
  }

  public void updateUser(User user) throws UserUpdateFailureException, UserNotFoundException {
    Optional<User> existingUserOptional = repository.findById(user.getId());
    if (existingUserOptional.isPresent()) {
        User existingUser = existingUserOptional.get();
      try {
        existingUser.setName(user.getName());
        repository.save(existingUser);
      } catch (Exception e) {
          logger.error("Failed to update user", e);
          throw new UserUpdateFailureException();
      }
    } else {
        throw new UserNotFoundException();
    }
  }

  public void deleteUser(String id) throws UserNotFoundException, UserDeleteFailureException {
    Optional<User> existingUserOptional = repository.findById(id);
    if (existingUserOptional.isPresent()) {
      try {
        repository.deleteById(id);
      } catch (Exception e) {
          logger.error("Failed to delete user", e);
          throw new UserDeleteFailureException();
      }
    } else {
        throw new UserNotFoundException();
    }
  }
  
}
