package net.thumbtack.school.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Buyer extends User {
    private Set<Bid> bidSet = new HashSet<>();
    public Buyer(String name, String surname, String patronymic, String login, String password) {
        this.setName(name);
        this.setSurname(surname);
        this.setPatronymic(patronymic);
        this.setLogin(login);
        this.setPassword(password);
    }
}
