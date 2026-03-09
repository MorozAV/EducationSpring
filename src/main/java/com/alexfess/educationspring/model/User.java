package com.alexfess.educationspring.model;

import lombok.Value;

import java.io.Serializable;

/**
 * Класс для хранения информации о пользователе
 */
@Value
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    public String login;
    public int empId;
    public String empName;
    public boolean fired;
}
