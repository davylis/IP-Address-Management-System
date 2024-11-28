package com.example.ip_management_system;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.ip_management_system.models.User;
import com.example.ip_management_system.repositories.UserRepository;

@DataJpaTest
public class UserRepoTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsernameShouldReturnUser() {
        // Arrange: Create and save a User
        User user = new User();
        user.setUsername("admin");
        user.setRole("ADMIN");
        userRepository.save(user);

        // Act: Find the user by username
        User foundUser = userRepository.findByUsername("admin");

        // Assert: Verify user is found
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("admin");
    }

    @Test
    public void testCreateNewUser() {
        // Arrange: Create a new User
        User user = new User();
        user.setUsername("user2");
        user.setRole("USER");

        // Act: Save the user
        userRepository.save(user);

        // Assert: Verify user ID is not null
        assertThat(user.getId()).isNotNull();
    }

    @Test
    public void testDeleteExistingUser() {
        // Arrange: Create and save a User
        User user = new User();
        user.setUsername("user");
        user.setRole("USER");
        userRepository.save(user);

        // Act: Delete the user by username
        User savedUser = userRepository.findByUsername("user");
        userRepository.deleteById(savedUser.getId());

        // Assert: Verify the user no longer exists
        User deletedUser = userRepository.findByUsername("user");
        assertThat(deletedUser).isNull();
    }
}