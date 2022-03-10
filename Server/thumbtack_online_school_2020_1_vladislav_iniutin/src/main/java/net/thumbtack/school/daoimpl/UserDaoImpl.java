package net.thumbtack.school.daoimpl;

import net.thumbtack.school.database.Database;
import net.thumbtack.school.server.ServerException;
import net.thumbtack.school.interfaces.UserDao;
import net.thumbtack.school.model.User;

public class UserDaoImpl implements UserDao {

    @Override
    public String login(String login, String password) throws ServerException {
        return Database.DATABASE.loginUser(login, password);
    }

    @Override
    public User getUserByToken(String token) throws ServerException {
        return Database.DATABASE.getUserByToken(token);
    }

    @Override
    public void register(User user) throws ServerException {
        Database.DATABASE.registerUser(user);
    }

    @Override
    public void logout(String token) throws ServerException {
        Database.DATABASE.logoutUser(token);
    }
}
