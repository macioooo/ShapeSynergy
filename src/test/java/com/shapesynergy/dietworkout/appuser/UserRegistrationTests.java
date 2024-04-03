package com.shapesynergy.dietworkout.appuser;

import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserRegistrationTests {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserService appUserService;

    @Test
    public void testCheckIfUserExists_ExistingUser_ReturnsTrue() {
        // Arrange
        String email = "existing@example.com";
        when(appUserRepository.findByEmail(email)).thenReturn(new AppUser());

        // Act
        boolean userExists = appUserService.checkIfUserExists(email);

        // Assert
        assertTrue(userExists);
    }

    @Test
    public void testCheckIfUserExists_NonExistingUser_ReturnsFalse() {
        // Arrange
        String email = "nonexisting@example.com";
        when(appUserRepository.findByEmail(email)).thenReturn(null);

        // Act
        boolean userExists = appUserService.checkIfUserExists(email);

        // Assert
        assertFalse(userExists);
    }



}
