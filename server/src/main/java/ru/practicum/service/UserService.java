package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.user.User;
import ru.practicum.repository.UserRepository;
import ru.practicum.validation.ValidationMaster;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(long userId) {
        ValidationMaster.checkIds(userId);
        userRepository.deleteById(userId);
    }

    public List<User> getUsers(List<Long> ids, int from, int size) {
        if (ids == null) {
            return userRepository.findAll(ValidationMaster.checkPaginationParams(from, size)).toList();
        } else {
            if (ids.isEmpty()) {
                return userRepository.findAll(ValidationMaster.checkPaginationParams(from, size)).toList();
            } else {
                return userRepository.findAllById(ids);
            }
        }
    }
}
