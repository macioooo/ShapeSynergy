package com.shapesynergy.dietworkout.appuser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.shapesynergy.dietworkout.appuser.service.AppUserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class UserInfoTests {

    @Mock
    private AppUserRepository appUserRepository;

    @InjectMocks
    private AppUserService appUserService;


    @Test
    public void testGetUserInfo_ReturnsUserInfo() {
        // Arrange
        AppUser appUser = new AppUser();
        appUser.setAge(30);
        // Mock the behavior of appUserRepository.findById(id_user) to return a mocked AppUser object
        when(appUserRepository.findById(anyLong())).thenReturn(Optional.of(appUser));

        // Act
        AppUserDTO appUserDTO = appUserService.getUserInfo(1L);

        // Assert
        assertEquals(30, appUserDTO.getAge());
    }
}
