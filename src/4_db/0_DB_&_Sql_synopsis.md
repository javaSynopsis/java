Это конспект по базам данных SQL и вообще принципу работы баз данных

# Частые вопросы на собеседовании
* Типы данных в SQL базах
* как работает `LIKE` и примеры
* Как работает `UNION` и `UNION ALL` и примеры
* Все типы `JOIN` и примеры
* SQL подзапрос и пример
* консистентность
* для чего в базах ключи и связи (соблюдение консистентности)
* индексы, один индекс по нескольким столбцам, несколько индексов в одной табл., в чем минусы индексов
* уровни нормализации
* `select for update`

# Типы данных

**Типы**
* `DATE` - format YYYY-MM-DD
* `DATETIME` - format: YYYY-MM-DD HH:MI:SS
* `TIMESTAMP` - format: YYYY-MM-DD HH:MI:SS
* `YEAR` - format YYYY or YY
* `DEC(2,3)`
* `INT`
* `CHAR`
* `VARCHAR(n)` - изменяет свою длину при вставке в нее значения с 0 до n (динамический массив)

**Заметки про типы данных**
* **CHAR vs VARCHAR(n)** - на CHAR не тратит время CPU чтобы изменять длину, зато нету экономии в памяти.
* **Даты в SQL вставляются в виде строк**
    ```sql
    INSERT INTO "Тест"
    VALUES(3, '2004-08-12');
    ```
* **TIMESTAMP vs DATETIME**
  1. `TIMESTAMP` обычно используют для хранения истории дат изменений или логирования; DATETIME для хранения конкретно даты как данных.
  2. `TIMESTAMP` is 4 bytes Vs 8 bytes for DATETIME. Т.е. TIMESTAMP ограничено 1970-2038 потому что int (по крайней мере в СТАРЫХ DB)
  3. поведение TIMESTAMP зависит от time zone сервера
* `DEC` и `INT` типы записываются в без апострафов, остальные с апострафом
  * `'Сэм\'ло'` - апостраф спец символ (или удвоением `'Сэм''ло'`)

# Примеры запросов SQL с поянением
```sql
/* Создание базы */
CREATE DATABASE drink;

/* выбор для использования */
USE drink;

/* Например */
CREATE DATABASE IF NOT EXISTS drink;
USE drink;

/* Удаляет базу */
DROP DATABASE [IF EXISTS] db_name;

/*Удаление таблиц, можно удалить только когда все связи установленные через ALTER TABLE удалены*/
DROP TABLE "Sales";

/*Выборка NULL, обычным способом (=0 или =NULL) не выбирается*/
SELECT drink
FROM info
WHERE cat IS NULL;

/*LIKE*/
/*% - любые символы*/
/*_ - один любой символ*/
/*для сравнения с учетом регистра LIKE BINARY, НО по умолчанию регистр и так учитывается*/
SELECT *
FROM my_cont
WHERE location LIKE '%CA';

/*
    REGEXP - регулярные выражения
    REGEXP является независимой от регистра для нормальных строк (т.е. строк не с двоичными данными)
*/
SELECT *
FROM pet
WHERE name REGEXP '^b';

/*
сравнение с учетом регистра в MySQL
для сравнения с учетом регистра используется REGEXP BINARY
*/
SELECT "a" REGEXP "A", "a" REGEXP BINARY "A"; -- -> 1 0

/*
Можно так проверять регулярки, без обращения к базе
*/
mysql> SELECT 'aXbc' REGEXP '[a-dXYZ]';                 -- -> 1
mysql> SELECT 'aXbc' REGEXP '^[a-dXYZ]$';               -- -> 0

/*Замена >=30 <=60*/
/*Можно использовать с буквами
WHERE BETWEEN 'Д' AND 'О' - все записи начинающиеся с Д, О и всех букв между ними;
*/
SELECT *
FROM drink
WHERE column_name BETWEEN 30 AND 60;

/*NOT следует за WHERE и используется с IN, BETWEEN, LIKE*/
SELECT name
FROM black
WHERE column_name NOT IN ('jon', 'sam');

-- Она считает число значений в данном столбце, или число строк в таблице.
-- Когда она считает значения столбца, она используется с DISTINCT чтобы производить счет чисел различных значений в данном поле.
SELECT COUNT (DISTINCT snum)
FROM Orders;

-- Чтобы подсчитать общее число строк в таблице, исполяьзуйте функцию COUNT со звездочкой вместо имени поля
-- COUNT со звездочкой включает и NULL и дубликаты, по этой причине DISTINCT не может быть исполяьзован. 
SELECT COUNT (*) FROM Customers;

/*
ВКЛЮЧЕНИЕ ДУБЛИКАТОВ В АГРЕГАТНЫЕ ФУНКЦИИ 
Агрегатные функции могут также ( в большинстве реализаций ) использовать аргумент ALL, который помещается перед именем поля
Различия между ALL и * когда они используются с COUNT -
* ALL использует имя_поля как аргумент.
* ALL не может подсчитать значения NULL. 
Пока * является единственным аргументом который включает NULL значения, и он используется только с COUNT; функции отличные от COUNT игнорируют значения NULL в любом случае. Следующая команда подсчитает(COUNT) число не-NULL значений в поле rating в таблице Заказчиков ( включая повторения ):
*/
SELECT COUNT ( ALL rating ) FROM Customers;

/*удаление всех записей с условием*/
DELETE
FROM clow
WHERE act = "scrym";

INSERT INTO clow(column1, column2)
VALUES('qwe', 'ert');

/*вставляет сразу все строки в таблицу из другой таблицы, можно из нескольких таблиц (это не подзапрос, а синтасис такой)*/
INSERT INTO "result"(отзыв, дата, имя_клиента)
SELECT "Отзывы".*, NULL /*имя клиента null*/
FROM "Отзывы"
WHERE "Отзывы"."Код клиента" = 3;

/*SELECT ... INTO - Вставляет данные в новую таблицу Persons_Backup*/
SELECT LastName,Firstname
INTO Persons_Backup
FROM Persons
WHERE City='Sandnes';

/*Можно и в другую базу данных*/
SELECT *
INTO Persons_Backup IN 'Backup.mdb'
FROM Persons;

/*Можно использовать основные операции*/
UPDATE dog
SET "type" = "type" + 3, "doc" = 'rrr'
WHERE type = 'asd';

/*новый_столбец получает значение зависящее от др. столбцов*/
UPDATE dog
SET new_column =
CASE
	WHEN column1 = 2
		THEN val2
	WHEN column2 = 'qwe'
		THEN val3
	ELSE other_val
END;
/*WHERE условие можно писать после END*/

-- Создание таблиц
CREATE TABLE "Отзывы"(
	"Код отзыва" INT NOT NULL IDENTITY PRIMARY KEY UNIQUE,
	"Код диска" INT NOT NULL,
	"Код клиента" INT NOT NULL REFERENCES "Клиенты",
	"Отзыв" VARCHAR(50) DEFAULT NULL,
	"Дата отзыва" VARCHAR(50) NOT NULL
);

CREATE TABLE "Отзывы"(
	"Код отзыва" INT NOT NULL IDENTITY,
	"Код диска" INT NOT NULL,
	PRIMARY KEY("Код отзыва")
);

CREATE TABLE "Отзывы"(
	"Код отзыва" INT NOT NULL IDENTITY,
	"Код диска" INT NOT NULL,
	CONSTRAINT FK_Заказ_Клиенты
	FOREIGN KEY ("Код диска")
	REFERENCES "Клиенты" ("Код клиента");
);

-- Изменение таблиц
ALTER TABLE "Заказ"
ADD CONSTRAINT FK_Заказ_Клиенты
FOREIGN KEY ("Код диска")
REFERENCES "Клиенты" ("Код клиента");

ALTER TABLE "Заказ"
ADD COLUMN cont INT NOT NULL IDENTITY FIRST,
ADD PRIMARY KEY (cont);

ALTER TABLE "Заказ"
ADD COLUMN cont VARCHAR(50)
AFTER first_name;
/* BEFORE FIRST LAST */

/*
CHANGE - имя и тип столбца
MODIFY - тип и позиция
ADD - добавление + тип
DROP - удаление столбцов
*/

/*переименование табл*/
ALTER TABLE proj
RENAME TO ropo;

/*переименование столбца в proj*/
ALTER TABLE "Заказ"
CHANGE COLUMN "number" "proj" INT NOT NULL IDENTITY,
ADD PRIMARY KEY("proj");

/* Увеличиваем тип, динну строки */
ALTER TABLE table_proj
MODIFY COLUMN proj VARCHAR(120);

ALTER TABLE table_proj
MODIFY COLUMN proj AFTER con_name;

ALTER TABLE table_proj
DROP COLUMN proj;

ALTER TABLE "Заказ"
DROP CONSTRAINT FK_Заказ_Клиенты;


ALTER TABLE Persons
ADD CHECK (P_Id>0);

/*Для создания нескольких CHECK используйте следующий синтаксис SQL*/
ALTER TABLE Persons
ADD CONSTRAINT chk_Person CHECK (P_Id>0 AND City='Sandnes');

/*Для MySQL*/
ALTER TABLE Persons
DROP CHECK chk_Person;

/*ASC - сортировка по возрастанию
DESC - убыванию(обратная по алфавиту)

order+by+10 - сортировка только 10 строк?
такое используется в инъекциях, чтобы узнать количество строк (если не вернуло ошибку, то строк именно столько)
пример http://www.descom.ch/main.php?id=18646644’+order+by+10/* (возможно нужны пробелы)
*/
SELECT  title,  purchased
FROM  movie_table
ORDER  BY  title  ASC,  purchased  DESC;

SELECT SUM(sales)
FROM cook
WHERE name = "nik";/*Результат одно число*/

/*Возвращается только одна запись title (о есть только разные названия)*/
SELECT title
FROM job
GROUP BY title
ORDER BY title;

SELECT SUM(sales)
FROM cook
GROUP BY name;/*Результат числа для групп*/

SELECT AVG(sales)
FROM cook
GROUP BY name;

SELECT MIN(sales)
FROM cook
GROUP BY name;

/*
WHERE - это ограничивающее выражение. Оно выполняется до того, как будет получен результат операции.

HAVING - фильтрующее выражение. Оно применяется к результату операции и выполняется уже после того как этот результат будет получен, в отличии от where.
Выражения WHERE используются вместе с операциями SELECT, UPDATE, DELETE, в то время как HAVING только с SELECT и предложением GROUP BY.

WHERE и HAVING можно использовать в одном запросе, тогда первым выполнится WHERE, а после фильтрации выполнится HAVING.

Например, WHERE НЕЛЬЗЯ использовать таким образом:
*/
SELECT name, SUM(salary)
FROM Employees
WHERE SUM(salary) > 1000
GROUP BY name;

/*В данном случае больше подходит HAVING:
*/
SELECT name, SUM(salary)
FROM Employees
GROUP BY name
HAVING SUM(salary) > 1000;

/*
То есть, использовать WHERE в запросах с агрегатными функциями нельзя, для этого и был введен HAVING.
*/
SELECT MAX(sales)
FROM cook
GROUP BY name;

/*Количество столбцов, NULL не считается*/
SELECT COUNT(sales)
FROM cook;

/*Количество столбцов в группе, NULL не считается*/
SELECT COUNT(sales)
GROUP BY name
FROM cook;

/*Количество НЕ повторяющихся столбцов*/
SELECT COUNT(DISTINCT sale)
FROM cook;

/*Только 2 записи начиная с пятой, нумерация с 0*/
SELECT COUNT(DISTINCT "sale")
FROM "cook"
LIMIT 5,2;

/*Обьединение вывода разных таблиц*/
SELECT "title"
FROM "current"
UNION
SELECT "title"
FROM "job"
SELECT "title"
FROM "desired"
ORDER BY "title";/*Только одна сортировка на все UNION*/

/*Включая дубликаты*/
SELECT "title"
FROM "current"
UNION ALL
SELECT "title"
FROM "job";
/*При обьединении INT и VARCHAR(20) он может быть преобразован в VARCHAR*/

/*
Подзапросы могут использоваться как
SELECT
SELECT список_столбцов
условия WHERE и FROM

И командах INSERT, DELETE, UPDATE, SELECT

Обычно возвращается одно значение, кроме IN
*/

SELECT mc.name,
(SELECT "state"
 FROM zip_code
 WHERE mc.zip_code = zip_code) AS "state"
FROM my_contacts mc;

/*CHECK*/
CREATE TABLE Persons
(
	P_Id int NOT NULL CHECK (P_Id>0),
	LastName varchar(255) NOT NULL,
	FirstName varchar(255),
	Address varchar(255),
	City varchar(255)
)

CREATE TABLE Persons
(
	P_Id int NOT NULL,
	LastName varchar(255) NOT NULL,
	FirstName varchar(255),
	Address varchar(255),
	City varchar(255),
	CONSTRAINT chk_Person CHECK (P_Id>0 AND City='Sandnes')
)


-- вставка нескольких значений в insert into
INSERT INTO MyTable ( Column1, Column2 )
VALUES
	( Value1, Value2 ),
	( Value1, Value2 ),
	('Robert', 'Smith');

-- вставка нескольких значений в insert into (используя union all)
INSERT INTO MyTable  (FirstCol, SecondCol)
    SELECT  'First' ,1
    	UNION ALL
	SELECT  'Second' ,2
	    UNION ALL
	SELECT  'Third' ,3

-- вставка нескольких значений в insert into (из НЕСКОЛЬКИХ таблиц)
INSERT INTO c (aID, bID) 
     SELECT a.ID, B.ID 
     FROM A, B 
     WHERE A.Name='Me'
     AND B.Class='Math';

    -- или
	INSERT INTO other_table (name, age, sex, city, id, number, nationality)
	SELECT name, age, sex, city, p.id, number, n.nationality
	FROM table_1 p
	INNER JOIN table_2 a ON a.id = p.id
	INNER JOIN table_3 b ON b.id = p.id
	...
	INNER JOIN table_n x ON x.id = p.id
	
-- вставка нескольких значений в insert into (готовых)
INSERT INTO `docs` (`id`, `rev`, `content`) VALUES
  ('1', '1', 'The earth is flat'),
  ('2', '1', 'One hundred angels can dance on the head of a pin'),
  ('1', '2', `The earth is flat and rests on a bull\'s horn`),
  ('1', '3', 'The earth is like a ball.');

-- используем AS чтобы вставить значение по умолчанию ('NOT_VIEWED' as STATE)
-- также вставляет ВСЕ id из НЕСКОЛЬКИХ табл, как все-со-всеми. Т.е. id обеих табл будут вставлены
insert into REPORT_STATE
SELECT USER_INFO.ID as USER_ID,  REPORT.ID as REPORT_ID, 'NOT_VIEWED' as STATE
from USER_INFO, REPORT; //тут выплонение для всех записей из USER_INFO, потом для всех REPORT (как цикл)

-- добавление нескольких столбцов за 1 команду
alter table
   table_name
add
   (
   column1_name column1_datatype column1_constraint, 
   column2_name column2_datatype column2_constraint,
   column3_name column3_datatype column3_constraint
   );


-- указание в update другой табл в качестве источника
UPDATE t1 SET c1=c1+1 WHERE c2=(SELECT MAX(c2) FROM t1);

-- Удалить все строки табл.
TRUNCATE [TABLE] tbl_name;


```

**Представления**
```sql
/*ПРЕДСТАВЛЕНИЯ*/

CREATE VIEW Londonstaff 
AS SELECT * 
FROM Salespeople 
WHERE city = 'London';

CREATE VIEW Bonus 
	AS SELECT DISTINCT snum, sname 
	FROM Elitesalesforce a 
	WHERE 10 < = 
	(SELECT COUNT (*) 
		FROM Elitesalestorce b 
		WHERE a.snum = b.snum); 

/*При изменении представлений запрос перенаправляется к таблицам из которых оно состоит и меняются таблицы*/
UPDATE Salesown 
SET city = 'Palo Alto' 
WHERE snum = 1004;

/*
ОБЪЕДИНЕНИЕ (UNION) и ОБЪЕДИНЕНИЕ ВСЕГО (UNIOM ALL) не разрешаются.
УПОРЯДОЧЕНИЕ ПО(ORDER BY) никогда не используется в определении представлений.
*/

DROP VIEW < view_name >

/*ОПРЕДЕЛЕНИЕ МОДИФИЦИРУЕМОСТИ ПРЕДСТАВЛЕНИЯ*/
/* Оно должно выводиться в одну и только в одну базовую таблицу.
/* Оно должно содержать первичный ключ этой таблицы ( это технически не предписывается стандартом ANSI, но было бы неплохо придерживаться этого).*/
/* Оно не должно иметь никаких полей, которые бы являлись агрегатными функциями.*/
/* Оно не должно содержать DISTINCT в своем определении.*/
/* Оно не должно использовать GROUP BY или HAVING в своем определении.*/
/* Оно не должно использовать подзапросы ( это - ANSI_ограничение которое не предписано для некоторых реализаций )*/
/* Оно может быть использовано в другом представлении, но это представле- ние должно также быть модифицируемыми.*/
/* Оно не должно использовать константы, строки, или выражения значений ( например: comm * 100 ) среди выбранных полей вывода.*/
/* Для INSERT, оно должно содержать любые пол основной таблицы которые имеют ограничение NOT NULL, если другое ограничение по умолчанию, не определено.*/

/*WITH CHECK OPTION - запросы на изменение представления которые всеравно исчезнут (где rating не = 300) теперь будут отклоняться*/
CREATE VIEW Highratings
	AS SELECT cnum, rating
	FROM Customers
	WHERE rating = 300
	WITH CHECK OPTION;

-- статья по VIEW и его поведение, например нельзя вставить значение стоблца если во VIEW оно отфильтровано в WHERE https://habrahabr.ru/post/47031/
```

**Процедуры**
```sql
/*ПРОЦЕДУРЫ MS SQL*/

CREATE PROC my_proc4
  @t VARCHAR(20), @p FLOAT
AS
UPDATE Товар SET Цена=Цена*(1-@p)
  WHERE Тип=@t
  
EXEC my_proc4 'Вафли',0.05 или
EXEC my_proc4 @t='Вафли', @p=0.05

CREATE PROC my_proc5
  @t VARCHAR(20)='Конфеты',
  @p FLOAT=0.1
AS
UPDATE Товар SET Цена=Цена*(1-@p)
  WHERE Тип=@t
/*Вызов*/
EXEC my_proc5 'Вафли',0.05 или
EXEC my_proc5 @t='Вафли', @p=0.05 или
EXEC my_proc5 @p=0.05
EXEC my_proc5

/*Процедура с входными и выходными параметрами.*/
CREATE PROC my_proc6
  @m INT,
  @s FLOAT OUTPUT
AS
SELECT @s=Sum(Товар.Цена*Сделка.Количество)
  FROM Товар INNER JOIN Сделка
  ON Товар.КодТовара=Сделка.КодТовара
  GROUP BY Month(Сделка.Дата)
  HAVING Month(Сделка.Дата)=@m
/*Вызов*/
DECLARE @st FLOAT
EXEC my_proc6 1,@st OUTPUT
SELECT @st
```
**ALL, ANY (SOME), EXISTS**
```sql
--Все записи где рейтинг большие всех записей
SELECT name
FROM "table"
WHERE rating > ALL (SELECT rating FROM "table2"
					WHERE rating > 3 AND rating < 7);
					
--Все записи где рейтинг больше хотя бы одного рейтинга из другой таблиицы
SELECT name
FROM "table"
WHERE rating > ANY (SELECT rating FROM "table2"
					WHERE rating > 3 AND rating < 7);

select sname
from студент
where EXISTS (select * from группа where gname='ПМ-21' and группа.gnum = студент.sgrp);

/*
--Замена IN на EXISTS--
Список студентов группы 'ПМ-21'. Второе это сравнение на первичный/вторичный ключ разных таблиц.
ТО ЕСТЬ можно использовать столбец из внешней таблицы внутри подзапроса.
*/
SELECT "имя_студента"
FROM "Студент"
WHERE EXISTS (SELECT *
				FROM "Группа"
				WHERE "Группа"."имя_группы" = 'ПМ-21'
				AND "Группа"."номер_группы" = "Студент"."номер_группы");
```

**В MySQL**
* `DROP SCHEMA` синоним `DROP DATABASE` в MySQL 5.0.2
* Если база данных удаляется, привеленгии её пользователей НЕ удаляются
* Если база данных по умолчанию удаляется, то база данных по умолчанию unset (сбрасывается использование командой use) а функция DATABASE() возвращает NULL

# ON UPDATE и ON DELETE (cascade операции)
`ON UPDATE` и `ON DELETE` - устанавливают поведение полей таблицы связанных с родительскими полями (id <- id)

**Q.** На данный момент, у нас есть защита целостности данных на случай каких-либо манипуляций с таблицами-потомками, но что если внести изменения в родительскую таблицу? Как нам быть уверенными, что таблицы-потомки в курсе всех изменений в родительской таблице?  
**A.** MySQL позволяет нам контролировать таблицы-потомки во время обновления или удаления данных в родительской таблице с помощью подвыражений: ON UPDATE и ON DELETE. MySQL поддерживает 5 действий, которые можно использовать в выражениях ON UPDATE и/или ON DELETE.
* `CASCADE`: если связанная запись родительской таблицы обновлена или удалена, и мы хотим чтобы соответствующие записи в таблицах-потомках также были обновлены или удалены. Что происходит с записью в родительской таблице, тоже самое произойдет с записью в дочерних таблицах. Однако не забывайте, что здесь можно легко попасться в ловушку бесконечного цикла.
* `SET NULL`:если запись в родительской таблице обновлена или удалена, а мы хоти чтобы в дочерней таблице некоторым занчениям было присвоено NULL (конечно если поле таблицы это позволяет)
* `NO ACTION`: смотри RESTRICT
* `RESTRICT`:если связанные записи родительской таблицы обновляются или удаляются со значениями которые уже/еще содержатся в соответствующих записях дочерней таблицы, то база данных не позволит изменять записи в родительской таблице. Обе команды NO ACTION и RESTRICT эквивалентны отсутствию подвыражений ON UPDATE or ON DELETE для внешних ключей.
* `SET DEFAULT`:На данный момент эта команда распознается парсером, но движок InnoDB никак на нее не реагирует.

```sql
CREATE TABLE invoice (
inv_id  INT AUTO_INCREMENT NOT NULL,
usr_id  INT NOT NULL,
prod_id  INT NOT NULL,
quantity INT NOT NULL,
PRIMARY KEY(inv_id),
FOREIGN KEY (usr_id) REFERENCES usr(usr_id)
  ON UPDATE CASCADE
  ON DELETE RESTRICT,
FOREIGN KEY (prod_id) REFERENCES product(prod_id)
  ON UPDATE CASCADE
  ON DELETE RESTRICT
) ENGINE=InnoDB CHARACTER SET=UTF8;
```

# MERGE & ON DUPLICATE KEY UPDATE
MERGE нету в MySQL - слияние двух таблиц. Т.е. insert или update из одной в другую
Вместо неё используется `INSERT ... ON DUPLICATE KEY UPDATE`

**merge** - это когда, если в добавляемой табл уже есть такое же значение в unique или primary столбце, то оно будет заменено на другое по правило которое написано после key update

**Пример:**
```sql
INSERT INTO t1 (a,b,c) VALUES (1,2,3)
ON DUPLICATE KEY UPDATE c=c+1;
```

# Какие кодировки и что значат
Источники: [тут](https://stackoverflow.com/a/496361)

**UTF-8 vs UTF-16 vs UTF-32**
* **UTF-8** has an advantage in the case where ASCII characters represent the majority of characters in a block of text, because UTF-8 encodes all characters into 8 bits (like ASCII). It is also advantageous in that a UTF-8 file containing only ASCII characters has the same encoding as an ASCII file.
* **UTF-16** is better where ASCII is not predominant, since it uses 2 bytes per character, primarily. UTF-8 will start to use 3 or more bytes for the higher order characters where UTF-16 remains at just 2 bytes for most characters.
* **UTF-32** will cover all possible characters in 4 bytes. This makes it pretty bloated. I ca not think of any advantage to using it.

**UTF-8 vs UTF-16 vs UTF-32**
* **UTF-8**: Variable-width encoding, backwards compatible with ASCII. ASCII characters (U+0000 to U+007F) take 1 byte, code points U+0080 to U+07FF take 2 bytes, code points U+0800 to U+FFFF take 3 bytes, code points U+10000 to U+10FFFF take 4 bytes. Good for English text, not so good for Asian text.
* **UTF-16**: Variable-width encoding. Code points U+0000 to U+FFFF take 2 bytes, code points U+10000 to U+10FFFF take 4 bytes. Bad for English text, good for Asian text.
* **UTF-32**: Fixed-width encoding. All code points take four bytes. An enormous memory hog, but fast to operate on. Rarely used.

**Пример работы с кодировками (CHARACTER SET) и COLLATE в SQL**
```sql
CREATE TABLE `employee` (
	`employee_id` BIGINT(10) NOT NULL AUTO_INCREMENT,
	`firstname` VARCHAR(50) NULL DEFAULT NULL,
	`lastname` VARCHAR(50) NULL DEFAULT NULL,
	`birth_date` DATE NOT NULL,
	`cell_phone` VARCHAR(15) NOT NULL,
	PRIMARY KEY (`employee_id`)
)
COLLATE='latin1_swedish_ci'
ENGINE=InnoDB
ROW_FORMAT=DEFAULT
AUTO_INCREMENT=216

-- Изменение кодировки, и collation:
ALTER DATABASE <database_name>
CHARACTER SET utf8
COLLATE utf8_unicode_ci;
		
-- Изменение для табл:
ALTER TABLE <table_name> CONVERT TO CHARACTER SET utf8 COLLATE utf8_unicode_ci;
ALTER TABLE <table_name> MODIFY <column_name> VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci;

-- Можно использовать только для конкретных запросов конкретный collation
-- https://dev.mysql.com/doc/refman/5.7/en/charset-collate.html
	
SELECT k
FROM t1
ORDER BY k COLLATE latin1_german2_ci;

SELECT MAX(k COLLATE latin1_german2_ci)
FROM t1;

SELECT k
FROM t1
GROUP BY k
HAVING k = _latin1 'Müller' COLLATE latin1_german2_ci;

-- Источник: https://stackoverflow.com/a/2344130
```

# Кодировки в MySQL
utf8_general_ci, utf8_unicode_ci, utf8-bin и др кодировки

Character Sets - set of symbols and encodings
collation - set of rules for comparing characters in a character set (set of language-specific rules)

**Коротко:**
1. лучше использовать для MySQL БД кодировку utf8mb4 (поддерживает Китайский полностью, emoji и т.д.) и collation (правило сравнения) utf8mb4_unicode_520_ci
      Запрос:
          CREATE DATABASE db_name
              CHARACTER SET utf8mb4
              COLLATE utf8mb4_unicode_520_ci;
2. по умолчанию в MySQL при сравнении строк регистр может не учитываться (зависит от кодировки)

**encodings:**
* utf8_general_ci - быстрее (не чувствительна к регистру)
* utf8_unicode_ci - точнее (не чувствительна к регистру)
* utf8-bin - чувствительна к регистру при сравнении строк	
* utf16 and utf32 - are variants on utf8; there is virtually no use for them.
* ucs2 - is closer to "Unicode" than "utf8"; there is virtually no use for it.
* utf8mb4 - (доступна с MySQL 5.5.3 on 2010-03-24) рекомендуется, лучшая современная замена utf8 (возможно по умолчанию в новых версиях MySQL)

**collation:**
* utf8mb4_unicode_520_ci - рекомендуется, используют с кодировкой utf8mb4

In general, utf8_general_ci is faster than utf8_unicode_ci, but less correct.

As for utf8_bin: Both utf8_general_ci and utf8_unicode_ci perform case-insensitive comparison. In constrast, utf8_bin is case-sensitive (among other differences), because it compares the binary values of the characters.

# Схемы
Схема - как namespace для БД - база.схема.таблица. В MySql схем нету и схемами там называются таблицы.

Обычно в DB которые поддерживают схемы есть схема по умолчанию, может называться `public` и если схема не указана, то таблицы будут создаваться в `public` схеме. На схемы как и на таблицы можно назначать пользователей, ролик и пр (прав доступа).

```sql
CREATE SCHEMA myschema
CREATE TABLE myschema.mytable (
 ...
);
```

# DDL, DML, DCL, TCL
* DDL is Data Definition Language - it is used to define data structures: create table, alter table, ...
* DML is Data Manipulation Language - it is used to manipulate data itself: insert, update, delete, ...
* DCL is Data Control Language: GRANT, REVOKE
* TCL is Transaction Control Language: COMMIT, ROLLBACK, SAVEPOINT, SET TRANSACTION

# Тригеры (trigers)
**trigger** - хранимая процедура, выполнение которой можно повесить на события. Синтаксис зависит от БД.
Часто события это: INSERT, DELETE, UPDATE. Список событий зависит от БД.

1. **Schema-level triggers** - запускаются на действия с данными БД
* After Creation
* Before Alter
* After Alter
* Before Drop
* After Drop
* Before Insert   
2. **System-level triggers** - на действия самой системой БД: вход, выход, запуск
    ```sql
    /* Триггер на уровне таблицы */
    CREATE OR REPLACE TRIGGER DistrictUpdatedTrigger
    AFTER UPDATE ON district
    BEGIN 
    insert into info values ('table "district" has changed');
    END;

    /* Триггер на уровне строки */
    // FOR EACH ROW - ключевое слово, для каждой БД разное
    CREATE OR REPLACE TRIGGER DistrictUpdatedTrigger
    AFTER UPDATE ON district FOR EACH ROW
    BEGIN 
    insert into info values ('one string in table "district" has changed');
    END;
    ```

# Индексы (Index, Indecies)
**index** нужно создавать на как можно меньшем количестве столбцов, иначе могут начаться тормоза. Создавать нужно для столбца по которому чаще всего идет поиск?

**update** для табл. с индексами будет дольше (т.е. тоже возможны тормоза)

```sql
CREATE INDEX idx_lastname
ON Persons (LastName); 

DROP INDEX index_name ON table_name;

-- дубликаты запрещены
CREATE UNIQUE INDEX index_name
ON table_name (column1, column2, ...)
```

# Основные типы JOIN

**Заметки:**
1. **inner join** это синоним `where`, `inner join` считается лучше читаемым
2. просто `join` это синоним для `inner join`
3. **cross join** не бесполезен (как некоторые пишут), он может быть использован например для заполнение сетки значениями из табл. Для примера две табл size и color одежды, результат все размеры и цвета.
4. **self join** применим когда табл. ссылается сама на себя (будет работать быстрее чем `group by`). Самого оператора `self join` нету, нужно писать реализацию через: `where a.id <> b.id`
5. **left inner join** и **right inner join** ПОХОЖЕ не существует

**Типы join:**
1. **inner join** - пересечение
2. **left (outer) join** - пересечение, плюс все строки из 1ой (левой) табл, если on не выполняется для строк то пустые ячейки заполняются null
3. **right (outer) join** - пересечение, плюс все строки из 2ой (правой) табл, если on не выполняется для строк то пустые ячейки заполняются null
4. **full (outer) join** - как `left outer join` и `right outer join` одновременно, если on не выполняется для строк то пустые ячейки заполняются null
5. **cross join** - все строки обоих табл. со всеми (декартово произведение). Отличается от `full outer join` тем, что соединение с пустой табл. тоже пусто (в `full outer join` соединение с пустой табл. будет иметь строки только одной из табл). Для `cross join` не нужно указывать условие (то что после on), пример:
    ```sql
    select *
    from a cross join b
    ```
6. **self join** - пересечение табл с самой собой. Полезно например, чтобы выбрать из одной табл. уникальные пары значений из столбцов без `group by`. Т.е. `self join` применим когда табл. ссылается сама на себя. Пример (в `self join` неравенство вместо равенства в `where`):
    ```sql
    SELECT a.name, b.name
    FROM a, b
    WHERE a.id <> b.id
    AND a.City = b.City
    ORDER BY a.City;
    ```

**Дополнительные варианты:**
1. **вариант left (outer) join**, только исключая само пересечение, результат только левая часть строк. АНАЛОГИЧНО и для `right (outer) join`
    ```sql
    select *
    from a
    left join b
    on a.id = b.id
    where b.id is null;
    ```
2. **вариант full (outer) join**, только исключая само пересечение, результат только левая и правая часть строк
    ```sql
    SELECT a.name, b.name
    FROM a
    FULL OUTER JOIN a
    ON a.id = b.id
    where a.id is null or b.id is null;
    ```

**Пример нескольких inner join сразу:**
```sql
select *
from a
inner join b on a.id = b.id
inner join c on b.id = c.id;
```
    
**Пример inner join через where:**
```sql
select *
from a, b, c
where a.id = b.id and b.id = c.id;
```