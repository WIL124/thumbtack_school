package net.thumbtack.school.interfaces;

import net.thumbtack.school.server.ServerException;
import net.thumbtack.school.model.User;

public interface UserDao {

    String login(String login, String password) throws ServerException;
    User getUserByToken(String token) throws ServerException;

    void register(User user) throws ServerException;

    void logout(String token) throws ServerException;
}
