package com.alexfess.educationspring.dao.impl;

import com.alexfess.educationspring.DataProvider;
import com.alexfess.educationspring.dao.DdlHistoryDao;
import com.alexfess.educationspring.model.DdlHistory;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import javax.naming.NamingException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Реализация {@link DdlHistoryDao}, которая предоставляет методы для доступа к записям истории DDL.
 * Этот класс обрабатывает операции с базой данных, связанные с сущностями {@link DdlHistory}.
 */
@Log
public class DdlHistoryDaoImpl implements DdlHistoryDao {

    @Override
    public @NotNull List<DdlHistory> findAll() {
        final String query = "select * from moroz.ddl_hist";
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
        final String sql = "SELECT * FROM moroz.DDL_HIST WHERE ID = ?";

        try (Connection cn = DataProvider.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("Found record by id=" + id);
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (Exception e) {
            log.severe("Error finding record by id=" + id + ": " + e.getMessage());
        }
        log.info("Record not found by id=" + id);
        return Optional.empty();
    }

    @Override
    public void save(@NotNull DdlHistory ddlHistory) {

    }

    @Override
    public void update(@NotNull DdlHistory ddlHistory) {

    }

    /**
     * Удаляет запись из таблицы moroz.ddl_hist по заданному идентификатору.
     * @param id    идентификатор записи для удаления
     */
    @Override
    public void delete(long id) throws SQLException, NamingException {
        String sql = "delete from moroz.ddl_hist where id = ?";
        try (Connection cn = DataProvider.getConnection()) {
            try (PreparedStatement ps = cn.prepareStatement(sql)) {
                ps.setLong(1, id);
                int countDeletedRows = ps.executeUpdate();
                if (countDeletedRows == 1) {
                    cn.commit();
                    log.info("Строка ddl_hist с id=" + id + " успешно удалена.");
                } else {
                    log.severe("Ошибка при удалении ddl_hist: " + countDeletedRows + " строк удалено, ожидалось 1. id=" + id);
                    cn.rollback();
                    throw new RuntimeException("Ошибка при удалении ddl_hist: " + countDeletedRows + " строк удалено, ожидалось 1. id=" + id);
                }
            } catch (SQLException e) {
                cn.rollback();
                log.log(Level.SEVERE,"Исключение при удалении ddl_hist by id=" + id, e);
                throw e;
            }
        }
    }

    @Override
    public @NotNull List<DdlHistory> findByDdlType(@NotNull String ddlType) {
        String sql = "select * from moroz.ddl_hist where ddl_type = ? order by id";
        List<DdlHistory> list = new ArrayList<>();
        try(Connection cn = DataProvider.getConnection();
            PreparedStatement ps = cn.prepareStatement(sql)){
            ps.setString(1, ddlType);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    list.add(mapRow(rs));
                }
                log.info("Found by ddlType="+ddlType + ": " + list.size() + " records");
                return list;
            }
        }
        catch(Exception e){
            log.severe("Error finding records by ddlType=" + ddlType + ": " + e.getMessage());
        }
        log.info("Not found by ddlType="+ddlType + " records");
        return list;
    }

    @Override
    public @NotNull List<DdlHistory> findByUserName(@NotNull String userName) {
        String sql = "select * from moroz.ddl_hist where upper(user_name) like ? order by id";
        List<DdlHistory> list = new ArrayList<>();
        try (Connection cn = DataProvider.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, "%" + userName.toUpperCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
                log.info("Found by userName=" + userName + ": " + list.size() + " records");
                return list;
            }
        } catch (Exception e) {
            log.severe("Error finding records by userName=" + userName + ": " + e.getMessage());
        }
        log.info("Not found by userName=" + userName + " records");
        return list;
    }

    @Override
    public long count() {
        String sql = "select count(*) from moroz.ddl_hist";

        try (Connection cn = DataProvider.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                long count = rs.getLong(1);  // Получаем первую колонку
                log.info("Total records count: " + count);
                return count;
            }
        } catch (Exception e) {
            log.severe("Error counting records: " + e.getMessage());
        }
        return 0;  // Возвращаем 0 при ошибке
    }
}
