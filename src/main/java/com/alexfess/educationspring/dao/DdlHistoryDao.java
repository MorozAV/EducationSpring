package com.alexfess.educationspring.dao;

import com.alexfess.educationspring.model.DdlHistory;

import java.util.List;

/**
 * DAO для работы с историей DDL операций
 */
public interface DdlHistoryDao {
    /**
     * @return Получить все записи
     */
    List<DdlHistory> findAll();

    /**
     * Получить запись по ID
     *
     * @param id идентификатор записи
     * @return запись по ID
     */
    DdlHistory findById(long id);

    /**
     * Сохранить новую запись
     *
     * @param ddlHistory запись для сохранения
     */
    void save(DdlHistory ddlHistory);

    /**
     * Обновить существующую запись
     *
     * @param ddlHistory запись для обновления
     */
    void update(DdlHistory ddlHistory);

    /**
     * Удалить запись по ID
     *
     * @param id идентификатор записи
     */
    void delete(long id);

    /**
     * Найти записи по типу DDL операции
     *
     * @param ddlType тип DDL операции
     * @return список записей
     */
    List<DdlHistory> findByDdlType(String ddlType);

    /**
     * Найти записи по имени пользователя
     *
     * @param userName имя пользователя
     * @return список записей
     */
    List<DdlHistory> findByUserName(String userName);

    /**
     * Получить общее количество записей
     *
     * @return количество записей
     */
    long count();
}
