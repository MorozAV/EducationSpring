package com.alexfess.educationspring.dao;

import com.alexfess.educationspring.model.User;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * DAO для работы с пользователями
 */
public interface UserDao {
    /**
     * Поиск пользователя по логину и паролю
     * @param login логин
     * @param password пароль
     * @return возвращает пользователя если он найден, иначе возвращается пустой Optional
     */
    Optional<User> findByLoginAndPassword(@NotNull String login, @NotNull String password);
}
