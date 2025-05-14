package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private UserController controller;

    @BeforeEach
    void setUp() {
        controller = new UserController();
    }

    @Test
    void shouldCreateValidEntity() {
        User validEntity = User.builder()
                .login("Chynar")
                .email("email@mail.ru")
                .birthday(LocalDate.of(2003, 3, 24))
                .build();
        User created = controller.create(validEntity);
        assertNotNull(created.getId());
        assertTrue(controller.findAll().contains(created));
    }

    @Test
    void shouldThrowWhenCreateInvalidEntity() {
        User invalidEntity = User.builder()
                .name("Илья")
                .email("emailmail.ru")
                .birthday(LocalDate.of(2026, 3, 24))
                .build();
        assertThrows(ValidationException.class, () -> controller.create(invalidEntity));
    }


    @Test
    void updateUser_ShouldChangeNameAndKeepSameId() {
        User validEntity = User.builder()
                .login("Chynar")
                .email("email@mail.ru")
                .birthday(LocalDate.of(2003, 3, 24))
                .build();
        User created = controller.create(validEntity);
        created.setName("Новое имя");
        User updated = controller.update(created);

        assertEquals(created.getId(), updated.getId());
        assertEquals(1L, controller.findAll().size());
        assertEquals("Новое имя", updated.getName());
    }

    @Test
    void createUser_WhenNameNotProvided_ShouldSetLoginAsName() {
        User validEntity = User.builder()
                .login("Chynar")
                .email("email@mail.ru")
                .birthday(LocalDate.of(2003, 3, 24))
                .build();
        User created = controller.create(validEntity);
        assertEquals("Chynar", created.getName());
    }

}
