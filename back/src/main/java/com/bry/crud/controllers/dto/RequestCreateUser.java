package com.bry.crud.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestCreateUser(
  @NotBlank
  String name, 
  @NotNull
  String cpf,
  @NotNull
  String picture
  )
  {}
