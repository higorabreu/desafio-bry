package com.bry.crud.domain.user;

import jakarta.persistence.*;
import lombok.*;

@Table(name="users")
@Entity(name="user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  private String name;

  private String cpf;

  public User(RequestUser requestUser) {
    this.name = requestUser.name();
    this.cpf = requestUser.cpf();
  }

  public String obfuscateCpf() {
    String cpf = this.cpf;
    int length = cpf.length();
    StringBuilder obfuscatedCpf = new StringBuilder();

    for (int i = 0; i < length - 4; i++) {
        obfuscatedCpf.append('*');
    }
    obfuscatedCpf.append(cpf.substring(length - 4));
    return obfuscatedCpf.toString();
  }
}
