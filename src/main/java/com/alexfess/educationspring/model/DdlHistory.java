package com.alexfess.educationspring.model;


import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.NotNull;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.StringJoiner;

/**
 * Класс для хранения истории изменений в базе данных
 */
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DdlHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * идентификатор
     */
    private long id;
    /**
     * Тип объекта: 'TABLE', 'SYNONYM', 'INDEX' и так далее
     */
    @Nullable
    private String objectType;
    /**
     * Схема владелец объекта
     */
    @Nullable
    private String owner;
    /**
     * Название объекта
     */
    @Nullable
    private String objectName;
    /**
     * Пользователь, под которым было произведено изменение
     */
    @Nullable
    private String userName;
    /**
     * Дата изменения
     */
    @NotNull
    private LocalDateTime ddlDate;
    /**
     * Тип изменения: 'CREATE', 'ALTER', 'DROP' и так далее
     */
    @NotNull
    private String ddlType;
    /**
     * Операционная система пользователя
     */
    @NotNull
    private String clientOsUser;
    /**
     * Текст изменения, в clob формате
     */
    @NotNull
    private String ddlTxt;
    /**
     * Стек вызова
     */
    @NotNull
    private String stack;
    /**
     * IP адрес клиента
     */
    @Nullable
    private String clientIpAddress;
    /**
     * Хост клиента
     */
    @NotNull
    private String clientHost;
    /**
     * Модуль (программное обеспечение) клиента
     */
    @Nullable
    private String clientModule;

    @Override
    public String toString() {
        return new StringJoiner(", ", DdlHistory.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("objectType='" + objectType + "'")
                .add("owner='" + owner + "'")
                .add("objectName='" + objectName + "'")
                .add("userName='" + userName + "'")
                .add("ddlDate=" + ddlDate)
                .add("ddlType='" + ddlType + "'")
                .add("clientOsUser='" + clientOsUser + "'")
                .add("ddlTxt='" + (ddlTxt.length() > 100 ? ddlTxt.substring(0, 100) + "..." : ddlTxt) + "'")
                .add("stack='" + stack + "'")
                .add("clientIpAddress='" + clientIpAddress + "'")
                .add("clientHost='" + clientHost + "'")
                .add("clientModule='" + clientModule + "'")
                .toString();
    }
}
