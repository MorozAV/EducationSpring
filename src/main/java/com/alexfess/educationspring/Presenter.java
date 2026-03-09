package com.alexfess.educationspring;

import com.alexfess.educationspring.dao.impl.DdlHistoryDaoImpl;
import com.alexfess.educationspring.model.Model;
import com.alexfess.educationspring.model.User;
import com.alexfess.educationspring.ui.LoginForm;
import com.vaadin.ui.UI;
import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import javax.naming.NamingException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Objects;
import java.util.logging.Level;

@Log
public class Presenter implements Serializable {
    private static final long serialVersionUID = 1L;
    private final MyUI view;
    private final Model model;

    public Presenter(MyUI view) {
        this.view = view;
        this.model = new Model();
        showMainScreen();
    }

    private void showMainScreen() {
        view.showMainScreen(this);
    }

    public void firstButtonClicked() {
        long count = model.getCountDdlHistory();
        view.showFirstButtonClicked(count);
    }

    /**
     * Удалить запись по ID
     * @param value идентификатор записи указанный пользователем, без проверки корректности
     */
    public void deleteById(String value) {
        int empId = getForceEmp(() -> deleteById(value));
        if (empId == 0) {
            view.showErrorMessage("Для выполнения операции необходимо авторизоваться.");
        } else {
            if (StringUtils.isNotBlank(value)) {
                try {
                    long id = Long.parseLong(value);
                    model.delete(id);
                    view.showInfoMessage("Запись с идентификатором " + id + " успешно удалена");
                } catch (NumberFormatException e) {
                    log.warning("Некорректный идентификатор записи: " + value + " employeeId=" + empId);
                    view.showWarningMessage("Некорректный идентификатор записи: " + value);
                } catch (SQLException | NamingException e) {
                    log.log(Level.SEVERE, "SQL ошибка при удалении: " + value + " employeeId=" + empId, e);
                    view.showWarningMessage("SQL ошибка при удалении записи: " + value);
                } catch (RuntimeException e) {
                    log.warning("Запись : " + value + " для удаления не найдена. Возможно удалена ранее.");
                    view.showErrorMessage("Запись : " + value + " для удаления не найдена. Возможно удалена ранее.");
                }
            } else {
                view.showErrorMessage("Идентификатор записи не может быть пустым");
            }
        }
    }

    /**
     * Возвращает идентификатор текущего пользователя в текущей схеме
     *
     * @return идентификатор пользователя в текущей сессии
     */
    public static int getForceEmp(Runnable runnable) {
        User user = UI.getCurrent().getSession().getAttribute(User.class);
        if (user == null) {
            LoginForm loginForm = new LoginForm();
            loginForm.setEvent(new LoginForm.SignOnEvent() {

                private static final long serialVersionUID = 171127L;

                @Override
                public void fire() {
                    log.log(Level.FINE, "checkAccess - " + (user != null ? user.toString() : "null") + " - runnable.run");
                    runnable.run();
                }
            });
            UI.getCurrent().addWindow(loginForm);
            log.log(Level.FINE, "checkAccess - " + (user != null ? user.toString() : "null") + " - return false");
            return 0;
        }
        return user.getEmpId();
    }
}
