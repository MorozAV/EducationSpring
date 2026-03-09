package com.alexfess.educationspring.dao.impl;

import com.alexfess.educationspring.DataProvider;
import com.alexfess.educationspring.dao.UserDao;
import com.alexfess.educationspring.model.User;
import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 * Реализация {@link UserDao}, которая предоставляет методы для доступа к данным пользователей.
 * Этот класс обрабатывает операции с базой данных, связанные с сущностями {@link User}.
 */
@Log
public class UserDaoImpl implements UserDao {
    @Override
    public Optional<User> findByLoginAndPassword(@NotNull String login, @NotNull String password) {
        final String sql = "select p.login,p.emp_id,e.emp_name,\n"
                + "case when ve.department_id = 0 then 1 else 0 end fired\n"
                + "from factory_hope.emp_pwd p \n"
                + "join factory_hope.emp e on e.id_emp=p.emp_id \n"
                + "left join webcontrol.v_hr_sklad_emps ve on ve.id_emp = e.id_emp \n"
                + "where upper(login)= ? and pwd= ?";
        try (Connection cn = DataProvider.getConnection(); PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, login.toUpperCase());
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    log.info("User by login " + login + " found");
                    return Optional.of(mapRow(rs));
                }
            }
        } catch (Exception e) {
            log.severe("Error finding user by login " + login + ": " + e.getMessage());
        }
        log.info("User by login " + login + " not found");
        return Optional.empty();
    }

    private static @NotNull User mapRow(ResultSet rs) throws SQLException {
        return new User(rs.getString("login"), rs.getInt("emp_id"), rs.getString("emp_name"), rs.getBoolean("fired"));
    }
}
