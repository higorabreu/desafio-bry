package com.bry.crud.domain.user;


import java.sql.Types;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;

import com.bry.crud.controllers.dto.RequestCreateUser;
import com.bry.crud.controllers.dto.RequestUpdateUser;
import com.bry.crud.util.PictureConverter;

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

  @Lob // large object
  @JdbcTypeCode(Types.BINARY)
  private byte[] picture;

  public User(RequestUpdateUser requestUser) {
    this.id = requestUser.id();
    this.name = requestUser.name();
    this.cpf = requestUser.cpf();
    this.picture = PictureConverter.base64ToByteArray(requestUser.picture());
    //this.picture = requestUser.picture().getBytes();
  }
  public User(RequestCreateUser requestUser) {
    this.name = requestUser.name();
    this.cpf = requestUser.cpf();
    this.picture = PictureConverter.base64ToByteArray(requestUser.picture());
    //this.picture = requestUser.picture().getBytes();
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
