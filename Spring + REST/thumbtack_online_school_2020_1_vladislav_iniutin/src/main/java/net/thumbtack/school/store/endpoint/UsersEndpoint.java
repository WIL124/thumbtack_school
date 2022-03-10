package net.thumbtack.school.store.endpoint;

import lombok.AllArgsConstructor;
import net.thumbtack.school.store.dto.UserDto;
import net.thumbtack.school.store.models.User;
import net.thumbtack.school.store.service.StoreService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/users")
public class UsersEndpoint {
    private final StoreService service;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> allUsers() {
        return service.getAllUsers();
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@PathVariable String id) {
        return service.getUser(id);
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public User create(@RequestBody @Valid UserDto userDto) {
        return service.createUser(userDto);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable String id) {
        return service.deleteUser(id);
    }
}
