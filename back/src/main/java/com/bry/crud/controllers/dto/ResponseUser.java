package com.bry.crud.controllers.dto;

public record ResponseUser(
  String id,
  String name, 
  String cpf,
  String picture
  )
  {}