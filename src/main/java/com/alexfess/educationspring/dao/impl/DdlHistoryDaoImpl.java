package com.alexfess.educationspring.dao.impl;

import com.alexfess.educationspring.DataProvider;
import com.alexfess.educationspring.dao.DdlHistoryDao;
import com.alexfess.educationspring.model.DdlHistory;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
/**
 * Реализация {@link DdlHistoryDao}, которая предоставляет методы для доступа к записям истории DDL.
 * Этот класс обрабатывает операции с базой данных, связанные с сущностями {@link DdlHistory}.
 */
@Log
public class DdlHistoryDaoImpl implements DdlHistoryDao {

    @Override
    public @NotNull List<DdlHistory> findAll() {
        final String query = "select * from moroz.ddl_history";
        List<DdlHistory> list = new ArrayList<>();
        try (Connection cn = DataProvider.getConnection();
             PreparedStatement ps = cn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
            log.info("Found " + list.size() + " records");
        } catch (Exception e) {
            //Не бросаем ошибку дальше, чтобы вернуть пустой список
            log.severe("Error fetching all records: " + e.getMessage());
        }
        return list;
    }

    /**
     * Преобразует строку из ResultSet в объект DdlHistory.
     *
     * @param rs ResultSet, содержащий данные строки
     * @return объект DdlHistory, заполненный данными из ResultSet
     * @throws SQLException если возникает ошибка при доступе к ResultSet
     */
    private static @NotNull DdlHistory mapRow(ResultSet rs) throws SQLException {
        return new DdlHistory(rs.getLong("id")
                , rs.getString("object_type")
                , rs.getString("owner")
                , rs.getString("object_name")
                , rs.getString("user_name")
                , rs.getTimestamp("ddl_date").toLocalDateTime()
                , rs.getString("ddl_type")
                , rs.getString("client_os_user")
                , rs.getString("ddl_txt")
                , rs.getString("stack")
                , rs.getString("client_ip_address")
                , rs.getString("client_host")
                , rs.getString("client_module"));
    }

    @Override
    public Optional<DdlHistory> findById(long id) {
        return Optional.empty();
    }

    @Override
    public void save(@NotNull DdlHistory ddlHistory) {

    }

    @Override
    public void update(@NotNull DdlHistory ddlHistory) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public @NotNull List<DdlHistory> findByDdlType(@NotNull String ddlType) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<DdlHistory> findByUserName(@NotNull String userName) {
        return Collections.emptyList();
    }

    @Override
    public long count() {
        return 0;
    }
}
