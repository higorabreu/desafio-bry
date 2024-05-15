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

import com.bry.crud.domain.user.User;
import com.bry.crud.domain.user.UserRepository;
import com.bry.crud.dto.RequestCreateUser;
import com.bry.crud.dto.RequestUpdateUser;
import com.bry.crud.service.exceptions.InvalidCpfException;
import com.bry.crud.service.exceptions.UserAlreadyExistsException;
import com.bry.crud.service.exceptions.UserCreationFailureException;
import com.bry.crud.service.exceptions.UserDeleteFailureException;
import com.bry.crud.service.exceptions.UserFetchException;
import com.bry.crud.service.exceptions.UserNotFoundException;
import com.bry.crud.service.exceptions.UserUpdateFailureException;
import com.bry.crud.util.CPFValidator;

import lombok.SneakyThrows;


@Service
public class UserService {

  private static Logger logger = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository repository;

  // getUserById  
  public Optional<User> getUserById(String id) {
    Optional<User> userOptional = repository.findById(id);
      return userOptional;
  }

  // getAllUsers
  public List<User> getAllUsers() throws UserFetchException {
    try {
        List<User> users = repository.findAll();

        if (users.isEmpty()) {
            return Collections.emptyList();
        } else {
            users.forEach(user -> user.setCpf(user.obfuscateCpf()));
            return users;
        }
    } catch (Exception e) {
        logger.error("Failed to fetch users", e);
        throw new UserFetchException();
    }
}

  // createUser
  public void createUser(RequestCreateUser data) throws UserCreationFailureException, UserAlreadyExistsException, InvalidCpfException {
    if (!CPFValidator.isValid(data.cpf())) {
      throw new InvalidCpfException();
    }
    Optional<User> userOptional = repository.findByCpf(data.cpf());
    if (userOptional.isPresent()) {
      logger.warn("Cpf already exists");
      throw new UserAlreadyExistsException();
    }
    User newUser = new User(data);
    try {
      repository.save(newUser);
    } catch (Exception e) {
        throw new UserCreationFailureException();
    }
  }

  // createUsers - cria usuários a partir de uma lista
  public void createUsers(List<RequestCreateUser> data) throws UserCreationFailureException, UserAlreadyExistsException, InvalidCpfException {
    for (RequestCreateUser user : data) {
      createUser(user);
    }
  }

  // createUsersParallel - cria threads diferentes
  @SneakyThrows
  public void createUsersParallel(List<RequestCreateUser> data) {
    logger.info(String.format("Main Thread ID: %d", Thread.currentThread().getId()));
    for (RequestCreateUser user : data) {
      Thread thread = new Thread(() -> createUserAsync(user));
      thread.start();
    }
  }

  // createUsersParallel - cria um usuário de forma assíncrona
  @SneakyThrows
  public void createUserAsync(RequestCreateUser data) {
    logger.info(String.format("Thread ID: %d", Thread.currentThread().getId()));
    createUser(data);
  }

  // updateUser
  public void updateUser(RequestUpdateUser data) throws UserUpdateFailureException, UserNotFoundException {
    User user = new User(data);
    Optional<User> existingUserOptional = repository.findById(user.getId());
    if (existingUserOptional.isPresent()) {
        User existingUser = existingUserOptional.get();
      try {
        existingUser.setName(user.getName());
        existingUser.setPicture(user.getPicture());
        repository.save(existingUser);
      } catch (Exception e) {
          logger.error("Failed to update user", e);
          throw new UserUpdateFailureException();
      }
    } else {
        throw new UserNotFoundException();
    }
  }

  // updateUsers - atualiza usuários a partir de uma lista
  public void updateUsers(List<RequestUpdateUser> data) throws UserUpdateFailureException, UserNotFoundException {
    for (RequestUpdateUser user : data) {
      updateUser(user);
    }
  }

  // deleteUser
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
