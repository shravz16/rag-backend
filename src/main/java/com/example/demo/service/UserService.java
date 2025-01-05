package com.example.demo.service;

import com.example.demo.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Find all users
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Find user by ID
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Find user by username
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Find users created after date
    public List<User> findUsersCreatedAfter(LocalDateTime date) {
        return userRepository.findByCreatedAtAfter(date);
    }

    // Find users with unique number greater than
    public List<User> findUsersWithNumberGreaterThan(Long number) {
        return userRepository.findUsersWithNumberGreaterThan(number);
    }

    // Find users by username pattern
    public List<User> findUsersByUsernamePattern(String pattern) {
        return userRepository.findByUsernamePattern(pattern);
    }


    // Create new user
    public User createUser(User user) {
        // Set creation timestamp
        Long lastUniqueNumber = userRepository.findTopByOrderByUniqueNumberDesc()
                .map(User::getUniqueNumber)
                .orElse(0L);
        user.setUniqueNumber(lastUniqueNumber + 1);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    // Update existing user
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update user fields
        user.setFilecreatedat(userDetails.getFilecreatedat());
        // Add any other fields you want to update

        // Set update timestamp if you have one

        return userRepository.save(user);
    }
}
