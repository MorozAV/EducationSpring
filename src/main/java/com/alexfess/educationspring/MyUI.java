package com.alexfess.educationspring;

import javax.servlet.annotation.WebServlet;

import com.alexfess.educationspring.model.Model;
import com.alexfess.educationspring.model.User;
import com.alexfess.educationspring.ui.LoginForm;
import com.alexfess.educationspring.dao.impl.DdlHistoryDaoImpl;
import com.alexfess.educationspring.model.DdlHistory;
import com.alexfess.educationspring.ui.component.StatusBarWithMenu;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import lombok.extern.java.Log;

import java.util.List;
import java.util.Optional;

/**
 * This UI is the application entry point. A UI may either represent a browser window (or tab) or some part of an HTML
 * page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be overridden to add component
 * to the user interface and initialize non-component functionality.
 */
@Log
@Theme("mytheme")
public class MyUI extends UI {

    private DdlHistoryDaoImpl dao;
    private StatusBarWithMenu statusBar;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        Presenter presenter = new Presenter(this);
        dao = new DdlHistoryDaoImpl();
    }

    public void showMainScreen(Presenter presenter) {
        final VerticalLayout root = new VerticalLayout();
        final VerticalLayout layout = new VerticalLayout();

        Button button = new Button("Click Me for get count rows in ddl hist table");
        button.addClickListener(e -> presenter.firstButtonClicked());

        final TextField id = new TextField("Укажите ID для поиска по нему: ");
        final Button btFindById = new Button("Найти по id");
        btFindById.addClickListener(e -> {
            try {
                Optional<DdlHistory> row = dao.findById(Long.parseLong(id.getValue()));
                if (row.isPresent()) {
                    Notification.show("Найдено запись: " + row.get().toString());
                } else {
                    Notification.show("Запись не найдена");
                }
            } catch (Exception ex) {
                log.severe(ex.getMessage());
                Notification.show("Ошибка: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });

        final TextField tfUserName = new TextField("Укажите имя пользователя для поиска по нему: ");
        final Button btFindByUserName = new Button("Найти по имени пользователя");
        btFindByUserName.addClickListener(e -> {
            try {
                String userName = tfUserName.getValue();
                if (userName.isEmpty()) {
                    Notification.show("Имя пользователя не может быть пустым");
                    return;
                }
                List<DdlHistory> rows = dao.findByUserName(userName);
                if (!rows.isEmpty()) {
                    Notification.show("Найдено записей: " + rows.size());
                } else {
                    Notification.show("Записи не найдены");
                }
            } catch (Exception ex) {
                log.severe(ex.getMessage());
                Notification.show("Ошибка: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        final TextField tfDdlType = new TextField("Укажите тип операции для поиска по ней: ");
        final Button btFindByDdlType = new Button("Найти по типу операции");
        btFindByDdlType.addClickListener(e -> {
            try {
                String ddlType = tfDdlType.getValue();
                if (ddlType.isEmpty()) {
                    Notification.show("Тип операции не может быть пустым");
                    return;
                }
                List<DdlHistory> rows = dao.findByDdlType(ddlType);
                if (!rows.isEmpty()) {
                    Notification.show("Найдено записей: " + rows.size());
                } else {
                    Notification.show("Записи не найдены");
                }
            } catch (Exception ex) {
                log.severe(ex.getMessage());
                Notification.show("Ошибка: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        final TextField idForDelete = new TextField("Укажите ID для удаления по нему: ");
        final Button btDelete = new Button("Удалить по id");
        btDelete.addClickListener(e -> presenter.deleteById(idForDelete.getValue()));
        layout.addComponents(button, id, btFindById, tfUserName, btFindByUserName, tfDdlType, btFindByDdlType, idForDelete, btDelete);
        statusBar = new StatusBarWithMenu();
        statusBar.getLoginButton().addClickListener(e -> loginOn());

        statusBar.setLogoutListener(() -> {
            statusBar.showGuestMode();
        });
        root.addComponents(layout, statusBar);
        root.setExpandRatio(layout, 1);
        root.setSizeFull();
        setContent(root);
    }

    public void showFirstButtonClicked(long count) {
        Notification.show("Thanks , it works!" + count + " rows in ddl_hist table");
    }

    /**
     * Показать сообщение об ошибке в UI и залогировать его
     * @param errorMessage текст сообщения об ошибке
     */
    public void showErrorMessage(String errorMessage) {
        Notification.show("Error: " + errorMessage, Notification.Type.ERROR_MESSAGE);
        log.severe(errorMessage);
    }

    /**
     * Показать информационное сообщение в UI и залогировать его
     * @param message текст информационного сообщения
     */
    public void showInfoMessage(String message) {
        Notification.show(message, Notification.Type.HUMANIZED_MESSAGE);
        log.info(message);
    }

    public void showWarningMessage(String message) {
        Notification.show(message, Notification.Type.WARNING_MESSAGE);
        log.info(message);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private void loginOn() {
        LoginForm loginForm = new LoginForm();
        loginForm.setEvent(() -> {
            statusBar.showUserMode(UI.getCurrent().getSession().getAttribute(User.class).getEmpName());
        });
        this.addWindow(loginForm);
    }
}
