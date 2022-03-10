package net.thumbtack.school.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDtoRequest {
    private String login;
    private String password;
}
