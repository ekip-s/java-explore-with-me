package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.User;
import ru.practicum.repository.UserRepository;
import org.springframework.data.domain.Page;

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
        userRepository.deleteById(userId);
    }

    public List<User> getUsers(List<Long> ids, int from, int size) {
        Pageable page = PageRequest.of(from, size);
        if(ids == null) {
            return userRepository.findAll(page).toList();
        } else {
            return userRepository.findAllById(ids);
        }
    }
}
