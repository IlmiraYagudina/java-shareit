package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

/**
 * Класс-сервис для управления сущностями пользователей
 */
public interface UserService {
    /**
     * Метод создаёт пользователя и добавляет его в хранилище
     * @param user данные пользователя
     * @return возвращает созданного пользователя
     */
    UserDto create(UserDto user);

    /**
     * Метод обновляет данные пользователя в хранилище
     * @param id идентификатор пользователя
     * @return возвращает новые данные о пользователе
     */
    UserDto update(Long id, UserDto user);

    /**
     * Метод получения пользователя по идентификатору
     * @param id идентификатор пользователя
     * @return возвращает полученного пользователя
     */
    UserDto getById(Long id);

    /**
     * Метод удаления пользователя по идентификатору
     * @param id идентификатор пользователя
     */
    void deleteById(Long id);

    /**
     * Метод получения списка пользователей
     * @return возвращает список пользователей из хранилища
     */
    List<UserDto> getUsers();
}