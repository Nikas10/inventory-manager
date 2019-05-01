package com.quartet.inventorydemo.model;

import com.quartet.inventorydemo.util.IdNotNull;
import com.quartet.inventorydemo.util.IdNull;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Pattern;
import org.hibernate.annotations.GenericGenerator;

@ApiModel(
    description =
        "This entity/form represents user of system. Contains info to log in and other information related to user")
@Entity(name = "Account")
@Table(name = "quartet_account", schema = "public")
public class Account extends History implements Serializable {

  @ApiModelProperty(hidden = true)
  @Null(groups = IdNull.class, message = "Trying to persist probably existing resource on create")
  @NotNull(groups = IdNotNull.class, message = "Resource id must be not null on update")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO, generator = "pg-uuid")
  @GenericGenerator(
      name = "pg-uuid",
      strategy = "uuid2",
      parameters =
      @org.hibernate.annotations.Parameter(
          name = "uuid_gen_strategy_class",
          value = "com.quartet.inventorydemo.model.id.PostgreSQLUUIDGenerationStrategy"))
  @Column(name = "id", nullable = false, updatable = false, unique = true)
  private UUID id;

  @ApiModelProperty(position = 1, notes = "First name")
  @NotBlank(message = "First name must be not empty")
  @Column(name = "first_name", nullable = false)
  private String firstName;

  @ApiModelProperty(position = 2, notes = "Middle name")
  @NotNull
  @Column(name = "middle_name", nullable = false)
  private String middleName = "";

  @ApiModelProperty(position = 3, notes = "Family name")
  @NotBlank(message = "Last(family) name must be not empty")
  @Column(name = "last_name", nullable = false)
  private String lastName;

  @ApiModelProperty(position = 4, required = true, notes = "Account login")
  @NotBlank(message = "Login must be not empty")
  @Column(name = "login", nullable = false, unique = true)
  private String login;

  @ApiModelProperty(position = 5, required = true, notes = "Account password")
  @NotBlank(message = "Password must be not empty")
  @Column(name = "password", nullable = false)
  private String password;

  @ApiModelProperty(hidden = true)
  @Pattern(
      regexp = "(^user$|^staff$|^admin$)",
      flags = Pattern.Flag.CASE_INSENSITIVE,
      message = "Role must be one of: user, staff or admin")
  @Column(name = "role", nullable = false)
  private String role = "user";

  @ApiModelProperty(position = 6, notes = "Email address")
  @Email(message = "Email must be valid")
  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @ApiModelProperty(hidden = true)
  @ManyToMany(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
      name = "quartet_account__quartet_holder",
      joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "id", nullable = false),
      inverseJoinColumns =
      @JoinColumn(name = "holder_id", referencedColumnName = "id", nullable = false))
  private Set<Holder> holders;

  @ApiModelProperty(hidden = true)
  @OneToMany(
      mappedBy = "account",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
  private Set<Requisition> requisitions;

  private Account() {
  }

  public Account(
      @NotBlank(message = "First name must be not empty") String firstName,
      @NotNull String middleName,
      @NotBlank(message = "Last(family) name must be not empty") String lastName,
      @NotBlank(message = "Login must be not empty") String login,
      @NotBlank(message = "Password must be not empty") String password,
      @Pattern(
          regexp = "(^user$|^staff$|^admin$)",
          flags = Pattern.Flag.CASE_INSENSITIVE,
          message = "Role must be one of: user, staff or admin")
          String role,
      @Email(message = "Email must be valid") String email) {
    this.firstName = firstName;
    this.middleName = middleName;
    this.lastName = lastName;
    this.login = login;
    this.password = password;
    this.role = role;
    this.email = email;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof Account)) {
      return false;
    }
    Account account = (Account) o;
    return Objects.equals(id, account.id)
        && firstName.equals(account.firstName)
        && middleName.equals(account.middleName)
        && lastName.equals(account.lastName)
        && login.equals(account.login)
        && password.equals(account.password)
        && role.equals(account.role)
        && email.equals(account.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(firstName, middleName, lastName, login, password, role, email);
  }

  public UUID getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(@NotBlank(message = "First name must be not empty") String firstName) {
    this.firstName = firstName;
  }

  public String getMiddleName() {
    return middleName;
  }

  public void setMiddleName(@NotNull String middleName) {
    this.middleName = middleName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(
      @NotBlank(message = "Last(family) name must be not empty") String lastName) {
    this.lastName = lastName;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(@NotBlank(message = "Login must be not empty") String login) {
    this.login = login;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(@NotBlank(message = "Password must be not empty") String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(
      @Pattern(
          regexp = "(user|staff|admin)",
          flags = Pattern.Flag.CASE_INSENSITIVE,
          message = "Role must be one of: user, staff or admin")
          String role) {
    this.role = role;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(@Email(message = "Email must be valid") String email) {
    this.email = email;
  }

  public Set<Holder> getHolders() {
    return holders;
  }

  public Set<Requisition> getRequisitions() {
    return requisitions;
  }
}
