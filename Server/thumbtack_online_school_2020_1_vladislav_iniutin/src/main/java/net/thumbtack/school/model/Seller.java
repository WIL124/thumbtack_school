package net.thumbtack.school.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Seller extends User {
    private Set<Lot> userLots = new HashSet<>();

    public Seller(String name, String surname, String patronymic, String login, String password) {
        this.setName(name);
        this.setSurname(surname);
        this.setPatronymic(patronymic);
        this.setLogin(login);
        this.setPassword(password);
    }

    public Set<Lot> getUserLots() {
        return userLots;
    }
}
