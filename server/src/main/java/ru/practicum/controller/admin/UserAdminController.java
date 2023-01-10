package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import org.springframework.web.bind.annotation.*;
import ru.practicum.model.User;
import ru.practicum.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

    private final UserService userService;

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PostMapping("/body")
    public List<User> get(@RequestBody GetUsers getUsers) {
        return userService.getUsers(getUsers.getIds(), getUsers.getFrom(), getUsers.getSize());
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class GetUsers {
    private List<Long> ids;
    private int from;
    private int size;
}
