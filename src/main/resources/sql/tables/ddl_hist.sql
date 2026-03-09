create table moroz.ddl_hist
(
    id                number generated always as identity not null primary key,
    object_type       varchar2(4000 byte),
    owner             varchar2(4000 byte),
    object_name       varchar2(4000 byte),
    user_name         varchar2(4000 byte),
    ddl_date          date,
    ddl_type          varchar2(4000 byte),
    client_os_user    varchar2(4000 byte),
    ddl_txt           clob,
    stack             varchar2(4000 byte),
    client_ip_address varchar2(11 byte),
    client_host       varchar2(4000 byte),
    client_module     varchar2(4000 byte)
);
/
comment on column moroz.ddl_hist.client_ip_address is 'ip адрес пользователя';
comment on column moroz.ddl_hist.client_host is 'Название ПК клиента';
/