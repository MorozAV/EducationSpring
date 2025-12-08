package com.alexfess.educationspring;

import javax.naming.NamingException;
import javax.servlet.annotation.WebServlet;

import com.alexfess.educationspring.dao.DdlHistoryDao;
import com.alexfess.educationspring.dao.impl.DdlHistoryDaoImpl;
import com.alexfess.educationspring.model.DdlHistory;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import lombok.extern.java.Log;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Log
@Theme("mytheme")
public class MyUI extends UI {

    private DdlHistoryDaoImpl dao;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        dao = new DdlHistoryDaoImpl();
        final VerticalLayout layout = new VerticalLayout();
        
        final TextField name = new TextField();
        name.setCaption("Type your name here:");

        Button button = new Button("Click Me");
        button.addClickListener(e -> {
            layout.addComponent(new Label("Thanks " + name.getValue() 
                    + ", it works!" + testDb()));
        });

        final TextField id = new TextField("Укажите ID для поиска по нему: ");
        final Button btFindById = new Button("Найти по id");
        btFindById.addClickListener(e -> {
            try {
                Optional<DdlHistory> row =  dao.findById(Long.parseLong(id.getValue()));
                if(row.isPresent()) {
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
                if(userName.isEmpty()) {
                    Notification.show("Имя пользователя не может быть пустым");
                    return;
                }
                List<DdlHistory> rows =  dao.findByUserName(userName);
                if(!rows.isEmpty()) {
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
                if(ddlType.isEmpty()) {
                    Notification.show("Тип операции не может быть пустым");
                    return;
                }
                List<DdlHistory> rows =  dao.findByDdlType(ddlType);
                if(!rows.isEmpty()) {
                    Notification.show("Найдено записей: " + rows.size());
                } else {
                    Notification.show("Записи не найдены");
                }
            } catch (Exception ex) {
                log.severe(ex.getMessage());
                Notification.show("Ошибка: " + ex.getMessage(), Notification.Type.ERROR_MESSAGE);
            }
        });
        layout.addComponents(name, button, id, btFindById, tfUserName, btFindByUserName, tfDdlType, btFindByDdlType);
        
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }

    private String testDb() {
        List<DdlHistory> records = dao.findAll();
        return "Найдено записей: " + records.size();
    }
}
