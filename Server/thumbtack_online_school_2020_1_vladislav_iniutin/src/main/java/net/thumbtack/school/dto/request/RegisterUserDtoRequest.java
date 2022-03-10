package net.thumbtack.school.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterUserDtoRequest {
    private String name;
    private String surname;
    private String patronymic;
    private String login;
    private String password;
    private String status; // buyer, seller, auctioneer
}
