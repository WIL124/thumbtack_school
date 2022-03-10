package net.thumbtack.school.store.models;

import lombok.Data;
import net.thumbtack.school.store.dto.UserDto;

import java.math.BigInteger;
import java.util.UUID;

@Data
public class User {
    private final String id = UUID.randomUUID().toString();
    private int year;
    private Sex sex;
    private BigInteger cardNumber;
    private boolean hasChildren;
    private boolean isMarried;
    private Education education;
    private String city;

    public User(int year, Sex sex, BigInteger cardNumber, boolean hasChildren, boolean isMarried, Education education, String city) {
        this.year = year;
        this.sex = sex;
        this.cardNumber = cardNumber;
        this.hasChildren = hasChildren;
        this.isMarried = isMarried;
        this.education = education;
        this.city = city;
    }

    public User(UserDto userDto) {
        this.year = Integer.parseInt(userDto.getYear());
        this.sex = userDto.getSex();
        this.cardNumber = new BigInteger(userDto.getCardNumber());
        this.hasChildren = Boolean.parseBoolean(userDto.getHasChildren());
        this.isMarried = Boolean.parseBoolean(userDto.getIsMarried());
        this.education = userDto.getEducation();
        this.city = userDto.getCity();
    }

    public User() {
    }
}
