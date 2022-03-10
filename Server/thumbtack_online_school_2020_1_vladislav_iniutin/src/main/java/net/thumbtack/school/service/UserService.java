package net.thumbtack.school.service;

import net.thumbtack.school.dto.request.LogoutDtoRequest;
import net.thumbtack.school.dto.response.EmptyDtoResponse;
import net.thumbtack.school.interfaces.UserDao;
import net.thumbtack.school.daoimpl.UserDaoImpl;
import net.thumbtack.school.dto.request.LoginDtoRequest;
import net.thumbtack.school.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.dto.response.LoginDtoResponse;
import net.thumbtack.school.interfaces.UserMapper;
import net.thumbtack.school.server.ServerException;
import net.thumbtack.school.server.ErrorCode;
import net.thumbtack.school.model.User;
import net.thumbtack.school.server.ServerResponse;

public class UserService extends ServiceTools {
    private final UserDao userDao = new UserDaoImpl();

    public ServerResponse registerUser(String requestJsonString) {
        try {
            RegisterUserDtoRequest dtoRequest = fromJsonToClass(requestJsonString, RegisterUserDtoRequest.class);
            userValidate(dtoRequest);
            User user = UserMapper.INSTANCE.userFromUserDto(dtoRequest);
            userDao.register(user);
            return new ServerResponse(new EmptyDtoResponse());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse loginUser(String requestJsonString) {
        try {
            LoginDtoRequest loginDtoRequest = fromJsonToClass(requestJsonString, LoginDtoRequest.class);
            validateLogin(loginDtoRequest);
            String token =userDao.login(loginDtoRequest.getLogin(), loginDtoRequest.getPassword());
            LoginDtoResponse response = new LoginDtoResponse(token);
            return new ServerResponse(response);
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse logoutUser(String requestJsonString){
        try {
            LogoutDtoRequest logoutDtoRequest = fromJsonToClass(requestJsonString, LogoutDtoRequest.class);
            userDao.logout(logoutDtoRequest.getToken());
            return new ServerResponse(new EmptyDtoResponse());
        }catch (ServerException ex){
            return new ServerResponse(ex);
        }
    }

    private static void validateLogin(LoginDtoRequest dto) throws ServerException {
        if (isNull(dto.getLogin())) {
            throw new ServerException(ErrorCode.USER_WRONG_LOGIN);
        }
        if (isNull(dto.getPassword())) {
            throw new ServerException(ErrorCode.USER_WRONG_PASSWORD);
        }
    }

}
