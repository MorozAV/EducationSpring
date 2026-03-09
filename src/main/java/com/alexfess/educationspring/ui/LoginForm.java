package com.alexfess.educationspring.ui;

import com.alexfess.educationspring.dao.impl.UserDaoImpl;
import com.alexfess.educationspring.model.User;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import lombok.extern.java.Log;

import java.io.Serializable;
import java.util.Optional;

/**
 * Форма для ввода логина и пароля
 */
@Log
public class LoginForm extends Window implements Button.ClickListener {

    private static final long serialVersionUID = 1L;

    private SignOnEvent event;
    private final TextField tfUser;
    private final PasswordField tfPwd;
//    @Deprecated
//    public static String ATTR_LOGIN = "login";
//    @Deprecated
//    public static String ATTR_EMP_ID = "emp_id";
//    @Deprecated
//    public static String ATTR_EMP_NAME = "emp_name";

    public LoginForm() {
        super.setCaption("Аутентификация");
        VerticalLayout lo = new VerticalLayout();
        lo.setSpacing(true);
        lo.setMargin(true);

        tfUser = new TextField("Имя пользователя:");
        tfPwd = new PasswordField("Пароль:");
        Button buttonOk = new Button("OK", this);

        tfUser.addShortcutListener(new Button.ClickShortcut(buttonOk, KeyCode.ENTER));
        tfPwd.addShortcutListener(new Button.ClickShortcut(buttonOk, KeyCode.ENTER));

        lo.addComponent(tfUser);
        lo.addComponent(tfPwd);
        lo.addComponent(buttonOk);
        lo.setComponentAlignment(buttonOk, Alignment.MIDDLE_CENTER);
        tfUser.focus();
        super.setContent(lo);
        super.center();
        super.setModal(true);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {

        if (tfUser.getValue().trim().isEmpty()) {
            tfUser.focus();
            return;
        }
        if (tfPwd.getValue().trim().isEmpty()) {
            tfPwd.focus();
            return;
        }
        UserDaoImpl userDao = new UserDaoImpl();
        Optional<User> user = userDao.findByLoginAndPassword(tfUser.getValue(), tfPwd.getValue());
        if (user.isPresent()) {
            if (user.get().isFired()) {
                log.info("User " + user.get().getLogin() + " is fired");
                Notification.show("Ошибка аутентификации: сотрудник уволен", Notification.Type.ERROR_MESSAGE);
                return;
            }
            VaadinService.getCurrentRequest().getWrappedSession().setAttribute("userName", user.get().getEmpName());
            UI.getCurrent().getSession().setAttribute(User.class, user.get());
            super.close();
        } else {
            Notification.show("Ошибка аутентификации: неверный логин или пароль", Notification.Type.ERROR_MESSAGE);
        }



//        MyUI ui = (MyUI) UI.getCurrent();
                // depricated
//                HashMap attr = new HashMap();
//                attr.put(ATTR_LOGIN, tfUser.getValue());
//                attr.put(ATTR_EMP_ID, rs.getString("emp_id"));
//                attr.put(ATTR_EMP_NAME, name);
//                ui.loadEmp(attr);



//        ui.removeWindow(LoginForm.this);

        if (LoginForm.this.event != null) {
            LoginForm.this.event.fire();
        }
    }

    public void setEvent(SignOnEvent event) {
        this.event = event;

    }

    public interface SignOnEvent extends Serializable {

        public void fire();
    }
}

