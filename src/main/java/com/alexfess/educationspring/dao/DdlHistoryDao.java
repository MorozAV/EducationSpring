package com.alexfess.educationspring.dao;

import com.alexfess.educationspring.model.DdlHistory;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * DAO для работы с историей DDL операций
 */
public interface DdlHistoryDao {
    /**
     * @return Получить все записи, если записей нет, то вернуть пустой список
     */
    @NotNull
    List<DdlHistory> findAll();

    /**
     * Получить запись по ID
     *
     * @param id идентификатор записи
     * @return запись по ID
     */
    Optional<DdlHistory> findById(long id);

    /**
     * Сохранить новую запись
     *
     * @param ddlHistory запись для сохранения
     */
    void save(@NotNull DdlHistory ddlHistory);

    /**
     * Обновить существующую запись
     *
     * @param ddlHistory запись для обновления
     */
    void update(@NotNull DdlHistory ddlHistory);

    /**
     * Удалить запись по ID
     *
     * @param id идентификатор записи
     */
    void delete(long id);

    /**
     * Найти записи по типу DDL операции
     *
     * @param ddlType тип DDL операции, не пустая строка
     * @return список записей. Если записей нет, то вернуть пустой список.
     */
    @NotNull
    List<DdlHistory> findByDdlType(@NotNull String ddlType);

    /**
     * Найти записи по имени пользователя
     *
     * @param userName имя пользователя. Не пустая строка
     * @return список записей. Если записей нет, то вернуть пустой список.
     */
    @NotNull
    List<DdlHistory> findByUserName(@NotNull String userName);

    /**
     * Получить общее количество записей
     *
     * @return количество записей
     */
    long count();
}
