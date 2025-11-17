package com.doublepartmers.sistema_ticket.Service.Impl;

import com.doublepartmers.sistema_ticket.DTO.User.UserDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserRequestDto;
import com.doublepartmers.sistema_ticket.DTO.User.UserResponseDto;
import com.doublepartmers.sistema_ticket.Domain.Model.User;
import com.doublepartmers.sistema_ticket.Exception.NotFoundException;
import com.doublepartmers.sistema_ticket.Mapper.UserMapper;
import com.doublepartmers.sistema_ticket.Repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void create() {
        UserRequestDto request = new UserRequestDto(
                "Edyson",
                "Leal",
                "edyson@double.com",
                "123456"
        );

        User mockUser = new User();
        mockUser.setId("user123");
        mockUser.setNombres("Edyson");
        mockUser.setApellidos("Leal");
        mockUser.setEmail("edysonleal@double.com");
        mockUser.setPassword("1234");
        mockUser.setFechaCreacion(LocalDateTime.now());
        mockUser.setFechaActualizacion(LocalDateTime.now());

        // No existe email
        when(userRepository.findByEmail("edyson@double.com"))
                .thenReturn(Optional.empty());

        // Mock estático de UserMapper
        try (MockedStatic<UserMapper> mapper = Mockito.mockStatic(UserMapper.class)) {

            mapper.when(() -> UserMapper.toEntity(request)).thenReturn(mockUser);
            mapper.when(() -> UserMapper.toResponse(mockUser))
                    .thenReturn(new UserResponseDto(
                            mockUser.getId(),
                            mockUser.getNombres(),
                            mockUser.getApellidos(),
                            mockUser.getEmail(),
                            mockUser.getFechaCreacion(),
                            mockUser.getFechaActualizacion()
                    ));

            when(userRepository.save(mockUser)).thenReturn(mockUser);

            UserResponseDto response = userService.create(request);

            assertEquals("user123", response.id());
            assertEquals("Edyson", response.nombres());
            verify(userRepository).save(mockUser);
        }
    }

    @Test
    void create_ShouldThrow_WhenEmailExists() {

        UserRequestDto request = new UserRequestDto(
                "Edyson",
                "Leal",
                "edyson@double.com",
                "1234"
        );

        when(userRepository.findByEmail("edyson@double.com"))
                .thenReturn(Optional.of(new User()));

        assertThrows(IllegalArgumentException.class,
                () -> userService.create(request));
    }

    // --------------------------------------------------------
    // UPDATE
    // --------------------------------------------------------
    @Test
    void update() {

        UserRequestDto request = new UserRequestDto(
                "Fabian",
                "Marin",
                "edysonleal@double.com",
                "pass"
        );

        User existing = new User();
        existing.setId("user123");
        existing.setFechaCreacion(LocalDateTime.now().minusDays(1));

        when(userRepository.findById("user123")).thenReturn(Optional.of(existing));
        when(userRepository.save(existing)).thenReturn(existing);

        try (MockedStatic<UserMapper> mapper = Mockito.mockStatic(UserMapper.class)) {

            mapper.when(() -> UserMapper.updateEntity(any(User.class), any(UserRequestDto.class)))
                    .thenAnswer(invocation -> {
                        User u = invocation.getArgument(0);
                        UserRequestDto req = invocation.getArgument(1);

                        u.setNombres(req.nombres());
                        u.setApellidos(req.apellidos());
                        u.setEmail(req.email());
                        u.setFechaActualizacion(LocalDateTime.now());
                        return null;
                    });

            mapper.when(() -> UserMapper.toResponse(existing))
                    .thenAnswer(invocation -> new UserResponseDto(
                            existing.getId(),
                            existing.getNombres(),
                            existing.getApellidos(),
                            existing.getEmail(),
                            existing.getFechaCreacion(),
                            existing.getFechaActualizacion()
                    ));

            UserResponseDto response = userService.update("user123", request);

            assertEquals("Fabian", response.nombres());
            assertEquals("edysonleal@double.com", response.email());
        }
    }

    @Test
    void update_ShouldThrowWhenNotFound() {
        when(userRepository.findById("id")).thenReturn(Optional.empty());

        UserRequestDto request = new UserRequestDto("Fabian", "Marin", "fabian@double.com", "123");

        assertThrows(NotFoundException.class,
                () -> userService.update("id", request));
    }

    // --------------------------------------------------------
    // GET BY ID
    // --------------------------------------------------------
    @Test
    void getById() {

        User user = new User();
        user.setId("user123");
        user.setNombres("Test");
        user.setApellidos("User");
        user.setEmail("mail@mail.com");
        user.setFechaCreacion(LocalDateTime.now());
        user.setFechaActualizacion(LocalDateTime.now());

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        try (MockedStatic<UserMapper> mapper = Mockito.mockStatic(UserMapper.class)) {

            mapper.when(() -> UserMapper.toResponse(user))
                    .thenReturn(new UserResponseDto(
                            user.getId(),
                            user.getNombres(),
                            user.getApellidos(),
                            user.getEmail(),
                            user.getFechaCreacion(),
                            user.getFechaActualizacion()
                    ));

            UserResponseDto result = userService.getById("user123");

            assertEquals("user123", result.id());
        }
    }

    @Test
    void getById_ShouldThrow_NotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById("user123"));
    }

    // --------------------------------------------------------
    // GET ALL
    // --------------------------------------------------------
    @Test
    void getAll() {

        User u1 = new User(); u1.setId("1");
        User u2 = new User(); u2.setId("2");

        when(userRepository.findAll()).thenReturn(List.of(u1, u2));

        try (MockedStatic<UserMapper> mapper = Mockito.mockStatic(UserMapper.class)) {

            mapper.when(() -> UserMapper.toSummary(u1))
                    .thenReturn(new UserDto("1", "N1", "A1", "mail1"));
            mapper.when(() -> UserMapper.toSummary(u2))
                    .thenReturn(new UserDto("2", "N2", "A2", "mail2"));

            List<UserDto> result = userService.getAll();

            assertEquals(2, result.size());
        }
    }

    // --------------------------------------------------------
    // GET ENTITY BY ID
    // --------------------------------------------------------
    @Test
    void getEntityById() {

        User user = new User();
        user.setId("user123");

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        User result = userService.getEntityById("user123");

        assertEquals("user123", result.getId());
    }

    @Test
    void getEntityById_ShouldThrowNotFound() {
        when(userRepository.findById("user123")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> userService.getEntityById("user123"));
    }





//    @Test
//    void update() {
//    }
//
//    @Test
//    void getById() {
//    }
//
//    @Test
//    void getAll() {
//    }
//
//    @Test
//    void getEntityById() {
//    }
}