package com.bry.crud.domain.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RequestUser(
  @NotBlank
  String name, 
  @NotNull
  String cpf) {

}
