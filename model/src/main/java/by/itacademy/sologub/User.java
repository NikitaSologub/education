package by.itacademy.sologub;

import by.itacademy.sologub.role.Role;

import java.time.LocalDate;
import java.util.Objects;

public abstract class User {
    private int id;
    private String firstname;
    private String lastname;
    private String patronymic;
    private LocalDate dateOfBirth;
    private Role role;
    private Credential credential;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId() == user.getId() && getFirstname().equals(user.getFirstname()) && getLastname().equals(user.getLastname()) && getPatronymic().equals(user.getPatronymic()) && getDateOfBirth().equals(user.getDateOfBirth()) && getRole() == user.getRole() && getCredential().equals(user.getCredential());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstname(), getLastname(), getPatronymic(), getDateOfBirth(), getRole(), getCredential());
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", firstname='" + firstname + '\'' + ", lastname='" + lastname + '\'' +
                ", patronymic='" + patronymic + '\'' + ", dateOfBirth=" + dateOfBirth +
                ", role=" + role + ", credential=" + credential + '}';
    }
}