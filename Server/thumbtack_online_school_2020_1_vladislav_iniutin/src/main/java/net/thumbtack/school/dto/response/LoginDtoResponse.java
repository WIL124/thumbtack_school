package net.thumbtack.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginDtoResponse extends DtoResponseBase {
    private String token;
}
