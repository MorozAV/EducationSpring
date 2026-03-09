package com.alexfess.educationspring.ui.component;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.*;

public class StatusBarWithMenu extends HorizontalLayout {

    private final Label lbUserName;
    private final Button btLogin;
    private final MenuBar menuBar;     // Меню пользователя

    public StatusBarWithMenu() {

        setWidth("100%");
        setSpacing(true);
        setMargin(false);
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        lbUserName = new Label("Не авторизован");
        lbUserName.setWidth("100%");

        // Кнопка "Войти" (видна только для гостей)
        btLogin = new Button("Войти");
        btLogin.setVisible(true);

        // Меню пользователя (видно только после входа)
        menuBar = new MenuBar();
        menuBar.setVisible(false);

        // Корневой пункт меню (иконка или слово)
        MenuBar.MenuItem rootItem = menuBar.addItem("", VaadinIcons.MENU, null);

        rootItem.addItem("Профиль", VaadinIcons.USER, this::onProfile);
        rootItem.addSeparator();
        rootItem.addItem("Настройки", VaadinIcons.COG, this::onSettings);
        rootItem.addSeparator();
        rootItem.addItem("Выйти", VaadinIcons.SIGN_OUT, this::onLogout);

        HorizontalLayout rightControls = new HorizontalLayout(btLogin, menuBar);
        rightControls.setSpacing(true);
        rightControls.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);

        addComponents(lbUserName, rightControls);

        // Лейбл растягиваем
        setExpandRatio(lbUserName, 1);
    }

    // ======== API для MyUI ======== //

    public void showGuestMode() {
        lbUserName.setContentMode(ContentMode.TEXT);
        lbUserName.setValue("Не авторизован");
        btLogin.setVisible(true);
        menuBar.setVisible(false);
    }

    public void showUserMode(String username) {
        lbUserName.setContentMode(ContentMode.HTML);
        lbUserName.setValue("<span style='color:red; font-weight:bold;'>" + username + "</span>");
        btLogin.setVisible(false);
        menuBar.setVisible(true);
    }

    public Button getLoginButton() {
        return btLogin;
    }

    public MenuBar getMenuBar() {
        return menuBar;
    }

    // ======== Обработчики меню ======== //

    private void onProfile(MenuBar.MenuItem item) {
        Notification.show("Профиль пока не реализован");
    }

    private void onSettings(MenuBar.MenuItem item) {
        Notification.show("Настройки пока не реализованы");
    }

    private void onLogout(MenuBar.MenuItem item) {
        // Вызов обработчика выхода через UI
        if (logoutListener != null) logoutListener.run();
    }

    // Позволяет MyUI подписаться на событие logout
    private Runnable logoutListener;

    public void setLogoutListener(Runnable listener) {
        this.logoutListener = listener;
    }
}

