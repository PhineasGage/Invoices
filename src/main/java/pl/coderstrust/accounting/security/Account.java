package pl.coderstrust.accounting.security;

import org.springframework.data.annotation.Id;

public class Account {

  @Id
  private String id;
  private String username;
  private String password;

  public Account() {
  }

  public Account(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }
}