create or replace TRIGGER moroz.trg_ddl_trig_hist
    AFTER DDL
    ON DATABASE
declare
    PRAGMA AUTONOMOUS_TRANSACTION;

    v_ddl CLOB := EMPTY_CLOB;
    li ora_name_list_t;
    l_n number;
BEGIN
    /* sys создает системные временные таблицы для sql запросов */
    IF ora_dict_obj_owner = 'SYS' THEN
        return;

    END IF;

    /* Получим текст DDL */
    l_n := ora_sql_txt(li);
    for i in 1 .. l_n loop
            v_ddl := v_ddl || TO_CLOB(TO_CHAR(li(i)));
        end loop;

    /* запишем историю */
    INSERT INTO moroz.ddl_hist(object_type, owner, object_name, USER_NAME, DDL_DATE, DDL_TYPE, CLIENT_HOST, CLIENT_OS_USER, client_ip_address,CLIENT_MODULE, DDL_TXT, stack)
    VALUES(UPPER(ora_dict_obj_type), UPPER(ora_dict_obj_owner), UPPER(ora_dict_obj_name), ora_login_user, SYSDATE, ora_sysevent, SYS_CONTEXT('USERENV','HOST'),SYS_CONTEXT ('USERENV', 'OS_USER'), nvl( SYS_CONTEXT('USERENV','IP_ADDRESS'),ora_client_ip_address),SYS_CONTEXT('USERENV','MODULE'), v_ddl, dbms_utility.format_call_stack);
    commit;

    /* любые ошибки игнорируем, что бы не завалить БД целиком */
EXCEPTION WHEN OTHERS THEN
--  null;
    wms_app.ERRPKG.REPORT_AND_STOP(-20000, 'ddl_lor_error: ' || sqlcode || ' ' || sqlerrm);
END trg_ddl_trig_hist;