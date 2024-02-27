package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

/**
 * Класс-хранилище данных о пользователях
 */
public interface UserRepository extends JpaRepository<User, Long> {
}