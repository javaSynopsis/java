Источники: [тут](https://habr.com/ru/post/353238/), [тут](https://habr.com/ru/post/346634/), [тут](https://habr.com/ru/post/309556/), [тут понятно написано](https://habr.com/ru/post/337306/)

# Использование по шагам
1. Установить docker
2. Запустить контейнер, при первом запуске он автоматически скачается, а потом запустится в текущей консоли и для управление запущенным образом нужно открыть новую консоль (name задает имя, чтобы потом использовать его в командах; MYSQL_ROOT_PASSWORD задает пароль и это обычная переменная среды которую тут использует mysql)
    ```
    docker run --name ms -p 3306:3306 -e MYSQL_ROOT_PASSWORD=password mysql
    ```
3. Список запущенных образов получим командой `docker ps`
3. Запустить командную оболочку запущенного образа, чтобы управлять им (ms тут это имя образа)
    ```
    docker exec -it ms bash
    ```
5. В случае mysql внутри bash подключаемся с указанием пароля (без пробела после `-p`)
    ```
    mysql -u root -ppassword
    ```

# Операции с файлами контейнера
**Note.** В Windows некоторые команды не выполняются из bash консоли которая ставится c git клиентом, нужна родная консоль Windows.

* `docker ps` смотрим id контейнера
* `docker cp 6a8151dc5b6b:/var/lib/postgresql/data/postgresql.conf ./postgresql.conf` копирование из контейнера с id `6a8151dc5b6b`
* `docker cp ./postgresql.conf 6a8151dc5b6b:/var/lib/postgresql/data/postgresql.conf` копирование из контейнера
* **команды в bash внутри самого контейнера**
  * `chown -R postgres:$(id -gn postgres) /var/lib/postgresql/data/postgresql.conf` меняем права на файл внутри контейнера
  * `ls -la /var/lib/postgresql/data/postgresql.conf` узнаем права на файл в контейнере чтобы знать на что их поменять
* `docker export 6a8151dc5b6b > contents.tar` сохраняем всю файловую систему

# docker compose
https://stackoverflow.com/a/41912295