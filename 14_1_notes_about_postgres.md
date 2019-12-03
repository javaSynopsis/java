Краткие заметки из практики по postgress

# Про БД
# Особенности isolation levels
# Особенности

**смотрим конфигурацию**
* `SHOW config_file;`
* `SHOW ALL;`
* `SELECT * FROM pg_settings;`

**postgresql.conf**
* `log_statement = 'all'` включаем лог запросов

**создаем специальную схему для особенных функций SQL**
```sql
create schema extensions;
grant usage on schema extensions to public;
grant execute on all functions in schema extensions to public;
alter default privileges in schema extensions grant execute on functions to public;
alter default privileges in schema extensions grant usage on types to public;
```

**расположение конфигов**
* `/var/lib/postgresql/data/postgresql.conf`

**права на файл postgresql.conf (и другие) после его изменения или подмены должны быть изменены**
* `chown -R postgres:$(id -gn postgres) /var/lib/postgresql/data/postgresql.conf`

**включаем спец. табл в которой хранится статистика запросов**
1. `shared_preload_libraries="pg_stat_statements"` значение в `postgresql.conf`
2. `CREATE EXTENSION pg_stat_statements` создаем табл
3. `select calls, rows, query from pg_stat_statements where query like '%from ccy_%' order by calls desc` вытаскиваем значения
4. когда нужно чистим старые данные командой `select pg_stat_statements_reset()`