package net.thumbtack.school.model;

import lombok.Data;

@Data
public abstract class User {
    private int id;
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String password;
}
