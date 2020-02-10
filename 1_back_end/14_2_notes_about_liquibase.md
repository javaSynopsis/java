liquibase - утилита для наката новых изменений БД поверх старых. История имзменений хранится в спец. табл. `databasechangelog` и `databasechangeloglock` которая хранится в БД куда идет накат. Ключи записей о измененной структуре БД это имя изменившего и id изменения. Изменения описываются тегами в xml и/или вставками sql в тег `<sql>` или подключением внешнего файла sql. Данные в таблицы справочников заливаются через внешние `csv` файлы. Теги liquibase могут описать не все особенности определенных БД, тогда для спец. команд нужно использовать sql.

Файлы xml с описанием changeSet распалагаются в каталоге `resources` проекта Spring. В корне есть xml в который делается include файлов конкретных изменений, т.е. каждый новый разработчик добавляет свое изменение новым файлов. В id изменения иногда принято писать дату.

Каталог в resources с liquibase называют `changelog`, в нем же располагают каталог `distributeddata` со справочными данными с `csv` файлами внутри.

В корне кроме обычного xml располагают xml для тестовых данных. В maven описаны параметры для liquibase и при запуске одним из параметров передается имя файла (тестовый или нет).

Для разных БД атрибуты в xml преобразуются в разные команды, это нужно смотреть в документации. Например autoIncrement=true создает разные структуры для mysql и postgress.

Запускать liquibase можно или напрямую командой, как .jar программу или установив maven плагин, тогда в IDE в разделе Plugins появится цель liquibase:update и по ней можно кликнуть 2 раза для запуска. Можно запускать и командой `mvn liquibase ...`

Переменные указанные в `property` можно использовать внутри любого xml из liquibase, напр. как prefix.

При ошибке liuibase могут не отпустить lock БД и тогда запуск наката перестанет работать. Чтобы отпустить принудительно можно или вручную поменять данные в `databasechangeloglock` или запустить liquibase со специальной командой.

При изменении `changeSet` в xml накат не произойдет заново, т.к. хэши **id** этих накатов остались прежними. Нужно изменить **именно id** чтобы накаты перенакатились. При этом при запуске через maven плагин возможно нужно пересобрать проект перед запуском иначе maven не увидит изменений. 

В `preConditions` идет проверка нужно ли выполнять changeSet. Например нужно ли удалять sequence если он существует. Или например проверка выполнялся ли этот же changeSet (т.е. старый вариант этого же changeSet до изменения и затирания его новым) на стэнде в продакшене через `changeSetExecuted` (указываем тут старый файл). В `onFail` задается что делать при ошибки, **HALT** (default) - остановится, **MARK_RAN** - продолжить, **WARN** - вывести предупреждение.
```xml
<property name="prefix" value="my_prefix"/>

<changeSet id="my_id" author="name">
    <preConditions onFail="MARK_RAN">
        <!-- если выполнения прошлой версии changelog не было -->
        <not>
            <changeSetExecuted
                id="old_id"
                author="old_name"
                changeLogFile="db/changelog/v1.0/old_file.xml"/>
        </not>
        
    </preConditions>
    <sql>...</sql>
    <loadUpdateData
            tableName="${prefix}_mytabe"
            primaryKey="id"
            file="distributeddata/mytabe.csv"/>
    <comment>
        some comment here
    </comment>
</changeSet>
```

Структура каталогов `resources` в проекте Spring.
```
resources
    db
        changelog
            distributeddata
                some1.csv
                some2.csv
            v1.0
                create_table.xml
                alter_table.xml
            v2.0
                alter_smth.xml
```

**Note.** В liquibase есть особенность указание путей, вместо слэша `/` там можно использовать точку `.` например `db.changelog` тоже самое что и `db/changelog`.

**В тестах.** liquibase скрипты могут накатываться перед запуском интеграционных тестов, если они подключены к тестам. При этом путь к файлу liquibase по умолчанию `db/changelog/db.changelog-master.yaml`. В тестовом `changelog-master.yaml` можно сделать `include` файла `changelog` из `main` каталога проекта (не тестового варианта, а реального скрипта создающего структуру DB), а вторым include сделать дополнительный тестовый `changelog` (например чтобы наполнить DB тестовыми данными).
```yml
databaseChangeLog:
- include:
    file: ../../main/resources/db/changelog/db.changelog-master.yaml
- include:
    file: db/changelog/marketplace/sampleData.yaml
```

В тестах liquibase включен по умолчанию, чтобы запуск был автоматическим при старте и схема DB накатывалась сама.
```yml
liquibase:
  enabled: true
```

**В maven** при запуске можно передать переменные среды от которых зависят настройки liquibase. Например: `mvn -Dliquibase.host=localhost:9999 -Duser=user -Dpassword=123`
```xml
</project>
<!-- ... -->
    <build>
        <plugins>
            <plugin>
                <groupId>org.liquibase</groupId>
                <artifactId>liquibase-maven-plugin</artifactId>
                <version>${liquibase.version}</version>
                <inherited>false</inherited>
                <configuration>
                    <outputFileEncoding>UTF-8</outputFileEncoding>
                    <driver>org.postgresql.Driver</driver>
                    <url>
                        jdbc:postgresql://${liquibase.host}:${liquibase.port}/${liquibase.db}?currentSchema=${liquibase.schema},extensions
                    </url>
                    <username>${liquibase.user}</username>
                    <password>${liquibase.password}</password>
                    <changeLogFile>${liquibase.changeLogFile}</changeLogFile>
                    <promptOnNonLocalDatabase>false</promptOnNonLocalDatabase>
                </configuration>
            </plugin>
        </plugins>
    </build>
<!-- ... -->
</project>
```
Если запуск liquibase будет на нескольких разных DB и есть специфичные SQL запросы, то можно указать на какой выполнять какие `changeSet` (например можно указать специфичные SQL для h2 для тестов).
```xml
<!-- Выполнится только если DB будет h2 (например для unit тестов в приложении) -->
<changeSet author="user1" id="h2_id" dbms="h2">
    <sql>...</sql>
</changeSet>

<!-- Выполнится только если DB будет postgresql -->
<changeSet author="user2" id="postgresql_id" dbms="postgresql">
    <sql>...</sql>
</changeSet>
```
В liquibase можно указывать [context](https://www.liquibase.org/documentation/contexts.html) к которому относится `changeSet`
```xml
<changeSet id="00000000000001" author="jhipster" context="DEV">...</changeSet>
```