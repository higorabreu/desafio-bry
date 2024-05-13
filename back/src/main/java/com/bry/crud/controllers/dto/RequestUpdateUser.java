package com.bry.crud.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestUpdateUser(
  @NotNull
  String id,
  @NotBlank
  String name, 
  @NotNull
  String cpf) {

}
