package net.thumbtack.school.store.dao;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.database.Database;
import net.thumbtack.school.store.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
@AllArgsConstructor
public class UserDao implements Dao<User>{
    private final Database database;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDao.class);

    @Override
    public User findById(String id) {
        User user = database.userById(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "product not found");
        }
        return user;
    }

    @Override
    public List<User> findAll() {
        return database.allUsers();
    }

    @Override
    public void insert(User obj) {
        database.insertUser(obj);
        LOGGER.debug("User {} was inserted", obj);
    }

    @Override
    public void delete(User obj) {
        database.deleteUser(obj);
    }
}
