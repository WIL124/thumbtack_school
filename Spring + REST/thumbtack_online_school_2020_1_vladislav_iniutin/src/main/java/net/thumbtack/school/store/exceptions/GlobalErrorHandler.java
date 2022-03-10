package net.thumbtack.school.store.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public MyError handleValidation(MethodArgumentNotValidException exc) {
        final MyError error = new MyError();
        exc.getBindingResult().getFieldErrors().forEach(fieldError -> error.getAllErrors().add(String.format("%s:%s", fieldError.getField(), fieldError.getDefaultMessage())));
        return error;
    }
    @ExceptionHandler({ResponseStatusException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyError handleValidation(ResponseStatusException exc) {
        final MyError error = new MyError();
        error.getAllErrors().add(exc.getMessage());
        return error;
    }
    @ExceptionHandler({HttpClientErrorException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public MyError handleValidation(HttpClientErrorException exc) {
        final MyError error = new MyError();
        error.getAllErrors().add(exc.getMessage());
        return error;
    }
    @ExceptionHandler({Exception.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public MyError handleValidation(Exception exc) {
        final MyError error = new MyError();
        error.getAllErrors().add(exc.getMessage());
        return error;
    }

    public static class MyError {
        private final List<String> allErrors = new ArrayList<>();

        public List<String> getAllErrors() {
            return allErrors;
        }
    }
}
