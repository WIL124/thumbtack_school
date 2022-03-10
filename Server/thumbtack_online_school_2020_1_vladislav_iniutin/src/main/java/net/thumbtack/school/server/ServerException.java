package net.thumbtack.school.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServerException extends Exception{
    private final ErrorCode errorCode;
}
