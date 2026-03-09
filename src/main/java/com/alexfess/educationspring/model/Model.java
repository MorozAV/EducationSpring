package com.alexfess.educationspring.model;

import com.alexfess.educationspring.dao.impl.DdlHistoryDaoImpl;
import lombok.Getter;

import javax.naming.NamingException;
import java.io.Serializable;
import java.sql.SQLException;

/**
 * Модель для хранения данных и бизнес логики
 */
public class Model  implements Serializable {
    private static final long serialVersionUID = 1L;
    @Getter
    private final DdlHistoryDaoImpl ddlHistoryDao = initDdlHistoryDao();

    private DdlHistoryDaoImpl initDdlHistoryDao() {
        return new DdlHistoryDaoImpl();
    }

    public long getCountDdlHistory() {
        return getDdlHistoryDao().count();
    }

    public void delete(long id) throws SQLException, NamingException {
        getDdlHistoryDao().delete(id);
    }
}
