**Note.** Тем кто решится читать эту статью. Это личные заметки, переводы различных материалов. Раздел "Еще раз про транзакции" и все что ниже более актуально и перекрывает инфу которая выше, если в ней есть противоречия.

- [Hibernate](#hibernate)
- [Hibernate vs JPA](#hibernate-vs-jpa)
- [Transaction](#transaction)
  - [В SQL](#В-sql)
  - [В Spring](#В-spring)
  - [В Spring Data](#В-spring-data)
  - [В Spring Boot](#В-spring-boot)
  - [О том как работают транзакции в Spring](#О-том-как-работают-транзакции-в-spring)
    - [Open Session in View (OSIV) паттерн](#open-session-in-view-osiv-паттерн)
  - [Isolation level](#isolation-level)
    - [Isolation level для денег и пр.](#isolation-level-для-денег-и-пр)
  - [Propagation level](#propagation-level)
  - [Global (XA) vs local transactions vs distributed transactions](#global-xa-vs-local-transactions-vs-distributed-transactions)
  - [Описание](#Описание)
  - [optimistic lock](#optimistic-lock)
  - [pessimistic lock](#pessimistic-lock)
  - [Атрибуты `@Version` для **optimistic lock**](#Атрибуты-version-для-optimistic-lock)
  - [Lock в SQL](#lock-в-sql)
- [Transaction vs Lock](#transaction-vs-lock)
- [Cache](#cache)
  - [Подключение и примеры](#Подключение-и-примеры)
  - [Когда не нужно использовать кэш L2 и Query cache](#Когда-не-нужно-использовать-кэш-l2-и-query-cache)
  - [Cache region](#cache-region)
  - [Стратегии кэширования](#Стратегии-кэширования)
- [Entity](#entity)
  - [Entity описание, операции, требования](#entity-описание-операции-требования)
  - [Custom types полей](#custom-types-полей)
- [Naming strategies](#naming-strategies)
- [AttributeConverter](#attributeconverter)
- [Generated properties](#generated-properties)
- [`@GeneratorType` annotation](#generatortype-annotation)
- [Сложные структуры](#Сложные-структуры)
  - [`@Embeddable`](#embeddable)
  - [`@ElementCollection`](#elementcollection)
  - [Типы связей Entity](#Типы-связей-entity)
    - [Примеры типов связей Entity](#Примеры-типов-связей-entity)
  - [Mapped Superclass](#mapped-superclass)
  - [Inheritance Mapping Strategies](#inheritance-mapping-strategies)
- [Fetching (Стратегии загрузки коллекций в Hibernate)](#fetching-Стратегии-загрузки-коллекций-в-hibernate)
  - [Типы fetch](#Типы-fetch)
  - [fetching стратегии](#fetching-стратегии)
  - [N + 1 selects problem](#n--1-selects-problem)
  - [JOIN FETCH (решение проблемы N + 1 selects)](#join-fetch-решение-проблемы-n--1-selects)
  - [List vs Set](#list-vs-set)
  - [Entity Graph (решение проблемы N + 1 selects)](#entity-graph-решение-проблемы-n--1-selects)
  - [Hibernate fetch стратегии, отличие от JPA: `FetchMode`, `SUBSELECT`, `@BatchSize`](#hibernate-fetch-стратегии-отличие-от-jpa-fetchmode-subselect-batchsize)
    - [`@BatchSize` c `FetchMode.SELECT`](#batchsize-c-fetchmodeselect)
    - [`FetchMode.SUBSELECT`](#fetchmodesubselect)
  - [@LazyCollection](#lazycollection)
- [Mapping](#mapping)
  - [Mapping annotations](#mapping-annotations)
  - [Типы Collection в связях](#Типы-collection-в-связях)
  - [`AttributeOverride`](#attributeoverride)
- [Persistence Context](#persistence-context)
  - [Hibernate Entity Lifecycle](#hibernate-entity-lifecycle)
  - [Методы Session (включая JPA vs Hibernate методы)](#Методы-session-включая-jpa-vs-hibernate-методы)
- [Bootstrap](#bootstrap)
- [Exceptions и их обработка](#exceptions-и-их-обработка)
- [Настройка Hibernate и Spring](#Настройка-hibernate-и-spring)
  - [Как Transaction работает внутри](#Как-transaction-работает-внутри)
  - [Старый вариант с HibernateUtil](#Старый-вариант-с-hibernateutil)
  - [Вариант с ручным созданием разных Bean в `@Configuration` для Hibernate + Spring](#Вариант-с-ручным-созданием-разных-bean-в-configuration-для-hibernate--spring)
  - [Конфигурация в Spring Boot через файл настроек](#Конфигурация-в-spring-boot-через-файл-настроек)
- [Misc](#misc)
  - [Кавычки в именах Entity, использование зарезервированных имен](#Кавычки-в-именах-entity-использование-зарезервированных-имен)
  - [Тип в котором хранить деньги](#Тип-в-котором-хранить-деньги)
  - [Особенности работы с БД MySQL](#Особенности-работы-с-БД-mysql)
- [Про Hibernate Criteria API vs JPA Criteria API](#Про-hibernate-criteria-api-vs-jpa-criteria-api)
- [JPA Criteria API](#jpa-criteria-api)
  - [Базовые](#Базовые)
  - [Как работать с этим в целом (примеры)](#Как-работать-с-этим-в-целом-примеры)
- [Старый Hibernate Criteria API](#Старый-hibernate-criteria-api)
  - [Пример старого Hibernate Criteria API (чтобы отличать его на глаз)](#Пример-старого-hibernate-criteria-api-чтобы-отличать-его-на-глаз)
- [Specification (и ее связь с Criteria API), использование с find методом из Spring Data JPA](#specification-и-ее-связь-с-criteria-api-использование-с-find-методом-из-spring-data-jpa)
- [specification-arg-resolver](#specification-arg-resolver)
- [Еще про транзакции (из блога Vlad Mihalcea)](#Еще-про-транзакции-из-блога-vlad-mihalcea)
  - [Блокировки и то как они относятся к транзакциям](#Блокировки-и-то-как-они-относятся-к-транзакциям)
  - [Lock Scope](#lock-scope)
  - [Lock Timeout](#lock-timeout)
  - [Phisical and logical transaction](#phisical-and-logical-transaction)
  - [Использование блокировок и транзакций на практике](#Использование-блокировок-и-транзакций-на-практике)
  - [Entity state transitions](#entity-state-transitions)
  - [flush strategies](#flush-strategies)
  - [dirty checking mechanism](#dirty-checking-mechanism)
  - [Isolation level](#isolation-level-1)
  - [Transaction pitfalls (возможные проблемы)](#transaction-pitfalls-возможные-проблемы)
- [Про equals и @NaturalId](#Про-equals-и-naturalid)
- [Кэширование и регионы кэша](#Кэширование-и-регионы-кэша)
- [MVCC vs 2PL и про то что MVCC даже с Serializable isolation level может не предотвратить Phantom Read](#mvcc-vs-2pl-и-про-то-что-mvcc-даже-с-serializable-isolation-level-может-не-предотвратить-phantom-read)
- [Связи](#Связи)
  - [One-To-Many](#one-to-many)
  - [One-To-One](#one-to-one)
  - [Many-To-Many](#many-to-many)
- [Как правильно делать merge](#Как-правильно-делать-merge)
- [ResultTransformer и про то что он делает SQL запросы эффективнее](#resulttransformer-и-про-то-что-он-делает-sql-запросы-эффективнее)
- [DTO projections и Tuple](#dto-projections-и-tuple)
- [StatelessSession](#statelesssession)
- [Зачем нужно делать Detached объекты в hibernate Session](#Зачем-нужно-делать-detached-объекты-в-hibernate-session)
- [Hypersistence Optimizer](#hypersistence-optimizer)
- [@DynamicUpdate](#dynamicupdate)
- [Collection vs Bag vs List vs Map vs Set](#collection-vs-bag-vs-list-vs-map-vs-set)
- [Sorting vs Ordering](#sorting-vs-ordering)
- [Типы id и их генерация](#Типы-id-и-их-генерация)
- [Использование дат в Entity](#Использование-дат-в-entity)
- [Авто подстановка в поле Entity текущей даты при сохранении Entity](#Авто-подстановка-в-поле-entity-текущей-даты-при-сохранении-entity)

# Hibernate

- **Прим.**
  - Этот гайд написан по [доке](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-provided) в будущем его дополнить читая доку с начала до конца.
  - Полностью перенести инфу https://habr.com/post/265061/
  - Короткая официальная статья о том как работает Hibernate и его transactions https://developer.jboss.org/wiki/SessionsAndTransactions?_sscc=t#This_is_all_very_difficult_cant_this_be_done_easier
  - Описаны различные алгоритмы, transaction, locks https://vladmihalcea.com

**Hibernate ORM** - это реализация JPA интерфейса. Прослойка между JDBC и application.

**Domain Model** - связь между таблицами и классами Java. Обязана иметь id поле. Hibernate лучше работает если связываемый класс это Plain Old Java Object (POJO) / JavaBean. Но можно использовать любой класс.

- **Types**
  
  - **Value types** - типы которые встраиваются в Entity
  - **Entity types** - обычные Entity

- **Основные классы** (в пакете `org.hibernate.*`):
  
  - **SessionFactory** - thread-safe (and immutable) представляет собой mapping для domain model к database. Это фабрика для `Session` классов. Нужно создавать **1ну** на все application, создание трудозатратно. `SessionFactory` создает services которые Hibernate использует во всех `Session(s)` таких как кэш L2, connection pools, transaction system integrations, etc.
  - **Session** - single-threaded, short-lived object, представлена как "Unit of Work" (единица работы) из паттерна PoEAA (Patterns of Enterprise Application Architecture). `Session` это обертка для JDBC `java.sql.Connection` и играет роль фабрики для `org.hibernate.Transaction`. Она реализует защиту от `repeatable read` для **persistence context** (aka first level cache или кэш L1). Реализует паттерн **Unit of Work** (группу действие, которые или выполнятся все, или откатятся). Можно сказать, что Session это "ворота в DB": делает map для Entity, выполняет очередь SQL (сразу или lazy).
  - **Transaction** - single-threaded, short-lived object используется приложением чтобы разграничить рамки физической transaction. API которое изолирует application от незкоуровневого управления транзакциями.

- **session-per-operation** - антипаттерн, потому что Session не синхронизированный объект и возможны race condition и др. проблемы.

- **session-per-application** - антипаттерн, вместо него рекомендуют использовать кэш L2, т.к. session может выполнятся в разных потоках, то так лучше не делать. Исключение если в application только 1 поток, тогда можно попробовать использовать (т.е. Session можно использовать только в одном потоке)

- **session-per-request** (на практике используется это) - **стандартный** паттерн

- **session-per-request-with-detached-objects** - user загружает данные из DB, session заканчивается пока user **долго принимает решение** (**long conversation**), когда user продолжает что-то делать начинается уже другая session вместо того чтобы держать открытой все это время первую session (так понял описание я) 

- **session-per-conversation** - паттерн, размер Session больше обычного и охватывает несколько transactions, но flush() делается после завершения всех transactions

- **Методы Session**
  
  - **sessionFactory.getCurrentSession()** - если Session не создан, то создастся, иначе получим ссылку на созданную. Не нужно делать **close()** и **flush()** (он будет сделан после **commit()** transaction)
  - **sessionFactory.openSession()** - нужно самому **close()** в finally блоке

# Hibernate vs JPA

**Hibernate vs JPA.** JPA это набор interfaces. Hibernate это implementation of JPA. Подключив Hibernate можно использовать только JPA тогда код будет совместим с другими реализациями JPA. А можно использовать Hibernate specific функции, у них больше возможностей и часто лучше реализация, но код будет не совместим с другими реализациями JPA.

Для mapping обычно используют JPA и там где JPA реализаций не достаточно, то Hibernate annotation. Даже в документации избегают xml mapping.

- **Аналоги классов Hibernate и JPA**
  - **SessionFactory** эквивалентно `EntityManagerFactory` в JPA. Реализация `SessionFactory` эквивалентна реализации `EntityManagerFactory`.
  - **Session** эквивалентно `EntityManager` из JPA
  - **Transaction** эквивалентно `EntityTransaction` из JPA
  - **ImplicitNamingStrategy** и **PhysicalNamingStrategy** эквивалентно `ImplicitNamingStrategyJpaCompliantImpl` из JPA. JPA не разделяет имена на logical and physical (т.е. в чистом JPA только 1ин один класс-генератор имен).

***

**Конвертация `EntityManager` в `Session` в Hibernate.** Например когда Hibernate работает в **JPA-mode** в котором нету доступа к методам Hibernate.

```java
EntityManager em = ...
Session session = em.unwrap(Session.class);

// прим. можно unpacking в Connection
Connection cn = em.unwrap(Connection.class);
```

***

| Hibernate LockMode                                                                                                                                         | JPA LockModeType            |
| ---------------------------------------------------------------------------------------------------------------------------------------------------------- | --------------------------- |
| NONE                                                                                                                                                       | NONE                        |
| OPTIMISTIC READ                                                                                                                                            | OPTIMISTIC                  |
| OPTIMISTIC_FORCE_INCREMENT, WRITE                                                                                                                          | OPTIMISTIC_FORCE_INCREMENT  |
| PESSIMISTIC_READ                                                                                                                                           | PESSIMISTIC_READ            |
| PESSIMISTIC_WRITE, ~~UPGRADE~~ UPGRADE_NOWAIT, UPGRADE_SKIPLOCKED                                                                                          | PESSIMISTIC_WRITE           |
| PESSIMISTIC_FORCE_INCREMENT, ~~FORCE~~                                                                                                                     | PESSIMISTIC_FORCE_INCREMENT |
| ***                                                                                                                                                        |                             |
|  |                             |
|                                                                                                                                                         |                             |
|                                                                                                                                           |                             |
|                                                          |                             |

# Transaction

**JPA vs JDO** (Java Data Objects). JDO это спецификация не только для реляционных DB, но и других типов DB. JPA можно рассматривать как подмножество JDO.

**Transaction** - это единица работы обладающая ACID. Еще говорят, что она выполняет Unit of Work.

**auto-commit** для transactions отключен по умолчанию (самим Hibernate или JEE в случае JEE окружения). Его можно включить, но он медленне обычных transactions.

`@Transaction` - есть реализация JPA и Spring. При изпользовании Spring нужно использовать реализацию из Spring. Делает AOP обертку вокруг метода в котором происходит транзакция (над которым проставлена аннотация), эта обертка начинает, завершает, откатывает транзакцию автоматически. Проставлять эту аннотацию можно над методами слоя `@Serive` или слоя `@Repository`, но в стандартном Spring MVC проставляют **только над `@Service`** (потому что метод `@Service` это единица работы).  
**Tip.** При использовании Spring аннотация **принадлежит Spring** и нужно использовать ее (`org.springframework.transaction.annotation.Transactional`), реализована с использованием его AOP. При использовании JavaEE нужно использовать аннотацию `javax.transaction.Transactional`.

- **programmatic transaction management** - вызов методов `getTransaction`, `commit`, `close`, `rollback` вручную
- **declarative transaction management** - использование аннотаций

Нужно помнить, что **в JPA может не сработать `@Transaction(readOnly=true)`** и транзакция всеравно будет способна изменять данные в DB, потому что JPA **(не точно!)** общая спецификация и в некоторых системах такое может быть позволено. **При этом** использование readOnly через JDBC (вызовм функций вручную) может работать как надо.

**JTA** (Java Transaction API) - API, часть Java EE для управления транзакциями. Состояние Transaction хранится в TLS (Thread Local Storage), поэтому она может быть распостранена (Propagation Level) на все методы в call-stack без явной передачи своего объекта-контекста. Может объединять несколько resources, если они использованы в одной транзакции (XA, Global transaction).

**Рекомендуется** использовать **Transaction** в JTA окружении (в application server) или использовать standalone реализацию JTA. Т.е. jar с реализацией может принадлежать application server или быть подключенной отдельно.

**JTA vs JDBC Hibernate transaction.** В JTA **сначало создается transaction** из JTA, потом Session, причем Hibernate привязывает Session к этой Transaction автоматически. Если JTA недоступно можно использовать Hibernate transaction поверх JDBC. В JDBC окружении **сначало создается Session**, а из нее обычная Transaction (внутри используется Thread из Java).

```java
// JTA transaction (может быть global или local)
UserTransaction tx = (UserTransaction)new InitialContext().lookup("java:comp/UserTransaction");
tx.begin();
// Do some work
factory.getCurrentSession().load(...);
tx.commit();
```

```java
// JDBC transaction (local only)
factory.getCurrentSession().beginTransaction();
// Do some work
factory.getCurrentSession().load(...);
factory.getCurrentSession().getTransaction().commit();
```

- bean-managed transactions (**BMT**) - 
- container-managed transactions (**CMT**) - 

## В SQL

Транзакции в SQL - можно начинать, завершать и откатывать транзакции SQL запросами (в каждой БД свой синтаксис).
Кроме команд ниже может использоваться: `SET TRANSACTION [ READ WRITE | READ ONLY ];` чтобы установить транзакцию глобально (видимо зависит от движка).

```sql
-- отключаем autocommit, чтобы запросы не комитились сразу, а только вконце транзакции
SET autocommit=OFF;

-- Установка уровня изоляции
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

-- выполняем транзакцию
START TRANSACTION READ ONLY;
SELECT @A:=SUM(salary) FROM table1 WHERE type=1;
UPDATE table2 SET summary=@A WHERE type=1;
COMMIT;

-- откат
ROLLBACK;
```

- `SET GLOBAL TRANSACTION ...;` - для всех session кроме уже запущенных
- `SET SESSION TRANSACTION ...;` - для текущей сессии

## В Spring

**TransactionManager** - это класс по которому создается `@Bean` (бин) для конфигурации транзакций. Это обертка поверх JDBC (точнее поверх **DataSource** из Spring, которая в свою очередь - обертка для JDBC с различными утилитами и `PlatformTransactionManager` если есть только один **DataSource** на все приложение) для транзакций. Используется в `@Transactional` аннотациях над методами `@Service`. DataSource взаимодействует с встроенными в DB менеджерами транзакций и уже сами DB выполняют транзакции (т.е. Spring **как бы** просто передает правила по которым DB должна работать с транзакциями, а сам ничего не делает).

**Несколько TransactionManager** возможно создать. Тогда в `@Transactional` аннотациях над методами `@Service` нужно указывать **имя TransactionManager**, которую нужно использовать в конкретном случае.

```java
// Пример настройки транзакций

// 1. Включить @EnableTransactionManagement в @Configuration

// 2. создание конфигурации менеджера транзакций
@Bean(name="order")
public HibernateTransactionManager txName() throws IOException {
    HibernateTransactionManager txName= new HibernateTransactionManager();
    txName.setSessionFactory(...);
    txName.setDataSource(...);
    return txName;
}
// 3. использование
public class TransactionalService {
    @Transactional("order")
    public void setSomething(String name) { ... }

    @Transactional("account")
    public void doSomething() { ... }
}
```

**В случае Spring JMS** - все тоже самое. Обертка поверх JMS внутри которого в Session из JMS выполняются транзакции через встроенный TransactionManager.

**Spring JTA vs EJB** (возможно инфа устарела). Гибкий rollback, не зависит от application server, не нужно JNDI, интеграция с разными технологиями.

- **типы TransactionManager:**
  
  - **LocalTransactionManager** - для 1 phase commit (local transaction)
  - **Global TransactionManager** - **TP monitor** (transaction processing monitor)

- **TransactionManager умеет**:
  
  - работа с несколькими DataSource одновременно
  - XA (global) транзакции
  - получать состояние транзакций (мониторинг)
  - откатывать транзакции
  - приостановку/возобновление, attach/detach к разным потокам (transaction propagation level)

- **типы TransactionManager** в Spring:
  
  - **DataSource** transaction manager (**JDBC**)
  - **Hibernate** transaction manager (**Hibernate**)
  - **JPA** transaction manager (**JPA**)
  - **JTA** transaction manager (**JTA**)
  - **JMS** transaction manager
  - **Jdo** transaction manager
  - **специфические для платформ**: WebLogic, WebSphere transaction managers etc

**PlatformTransactionManager** - центральный interface транзакций в Spring (для своей реализации рекомендуется наследовать AbstractPlatformTransactionManager, т.к. там готовые методы).
    - Имеет методы: getTransaction(TransactionDefenition), commit(status). rollback(status)
    - в getTransaction(...) можно задать **TransactionDefenition**, который описывает **isolation level** и **propagation level**

**При тестировании** часто используется rollback only флаг, чтобы все транзакции автоматически откатывались.

## В Spring Data

В **Spring Data** методы transactional по умолчанию. В том числе для `@RepositoryRestController`?

## В Spring Boot

Если **spring-data*** or **spring-tx** найдены в classpath, то `@EnableTransactionManagement` включается автоматически. Конфигурация происходит через класс `TransactionAutoConfiguration` из **spring-boot-autoconfigure.jar**.

```java
// примерный код конфигурации внутри Spring Boot
@Configuration
@ConditionalOnClass({PlatformTransactionManager.class})
@AutoConfigureAfter({JtaAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, Neo4jDataAutoConfiguration.class})
@EnableConfigurationProperties({TransactionProperties.class})
public class TransactionAutoConfiguration {
// ...
}
```

## О том как работают транзакции в Spring

Дополнить по этой ссылке: https://dzone.com/articles/how-does-spring-transactional

### Open Session in View (OSIV) паттерн

Эта проблема больше относится к **Spring,** но без **OSIV** использовать JPA трудно и для этого придется делать много **DTO** объектов на каждый запрос пользователя (это стандартный подход). И использовать mapper  - одну из Java библиотек (например Orca или MapStruct) которая делает set свойств начитанных Entity в DTO (т.е. только тех полей Entity которые нужно показать пользователю в отправленном клиенту объекте DTO).

Подробнее и с картинками о том как работает [тут](https://stackoverflow.com/a/37526397)

**Open Session in View** (OSIV) - паттерн, тесно связан процессом загрузки:

**Суть:** когда сервис начитал данные и отправил во view (страницу), то view может использовать свойства (переменные) полученных объектов (сами свойства могут быть другими объектами). Эти связанные с полученными объектами свойства должны бы подгрузиться как lazy, но транзакция которую начали в сервисе уже закончилась до того как отработало view.
Вот чтобы этого не произошло транзакция не завершается до того как завершит работу view и все запросы свойств полученных объектов превращаются в запросы внутри этой транзакции.

**Минусы:** если не понимать что делаешь можно получить проблемы вроде n+1 query. Если начитать много Entities может быть загружен кэш L1 и GC очистка для этих Entities во время выполнения не сработает.
**Плюсы:** код лучше читаем и меньше кода  
**Альтернатива:** писать запросы вручную.

Можно отключить **OSIV**, в Spring это не сделано по умолчанию для совместимости и чтобы не усложнять разработку. Реализовано это через `OpenEntityManagerInViewInterceptor`. Так `spring.jpa.open-in-view=false`. При этом при попытке использовать OSIV получим `LazyInitializationExceptions`.

**Совет по обходу OSIV** (на проверено):

1. Использовать `EntityGraph`. При этом сам `EntityGraph` можно указывать над методом репозитория или в параметре метода через неофициальную библиотеку [Spring Data JPA EntityGraph](https://github.com/Cosium/spring-data-jpa-entity-graph) (по мере выполнения, в Runtime)
2. Использовать `QueryDSL` вместе с `join`, при этом **не использовать** совместно `EntityGraph` и `join`

```java
// 1. EntityGraph
@Entity
@NamedEntityGraph(name = "Item.characteristics",
    attributeNodes = @NamedAttributeNode("characteristics")
)
public class Item {}

public interface ItemRepository extends JpaRepository<Item, Long> {
    @EntityGraph(value = "Item.characteristics")
    Item findByName(String name);
}

// 2. Spring Data JPA EntityGraph, в Runtime
productRepository.findByLabel("foo", EntityGraphs.named("Product.brand"));
```

## Isolation level

- **Read phenomena** - типы ошибочного чтения (проблемы параллельного чтения транзакций):
  
  - **lost update** -  при одновременном изменении одного блока данных разными транзакциями одно из изменений теряется. **Любой уровень изоляции кроме NONE из списка ниже предотвращает** `lost update`
  - **dirty reads** - чтение данных откатившейся транзакции A транзакцией B до отката A
  - **non-repeatable reads** - транзакция читает данные несколько раз, но получает разные данные. Потому что эта строка была изменена в промежутке между чтениями
  - **phantom reads** - транзакция читает строки по одному критерию несколько раз, в промежутках между чтениями происходит вставка строк подходящих под критерии. Один и тот же запрос дает разные результаты (фантомные)

- **Алгоритмы блокировки** - используются в **isolation level** блокируют таблицы или столбцы от записи и/или чтения несколькими транзакциями одновременно (изолируют)
  
  1. **Two-Phase Locking (2PL)** - алгоритм использующийся для **serializable isolation** level. Состоит из: shared (read) и exclusive (write) блокировок. Может быть применена на **ОДНУ** строку для предотвращения lost update, read, write. Или на **НЕСКОЛЬКО** строк для предотвращения phantom reads.
  2. **Multi-version concurrency control (MVCC)** - алгоритм использующий копию используемых данных DB для других транзакций пока и номер версии этих данных для проверки, на нем основан алгоритм **snapshot isolation** level. Но это ДРУГОЙ алгоритм.

- **Isolation level** - уровни изоляции такие как у JDBC
  
  - **none** - информирует, что драйвер не поддерживает транзакции, без изоляции
  - **read uncommitted** - no lock on table. Разрешает dirty, non-repeatable, phantom reads
  - **read committed** - lock on committed data. You can read the data that was only commited. запрещает dirty reads
  - **repeatable read** - lock on block of sql (which is selected by using select query). запрещает dirty и non-repeatable reads
  - **serializable** - lock on full table (on which Select query is fired). No other transaction can modify the data on the table. запрещает dirty, non-repeatable, phantom reads
  - **snapshot** - запрещает Dirty Reads, Lost Updates and Read Skews, но возможна Write Skews or Phantom Reads. На практике несработавшая транзакция просто откатится.

**snapshot** — данный вид изоляции не входит в рекомендации стандарта SQL 92, но он реализован во многих СУБД.  
Процессы-читатели не ждут завершения транзакций писателей, а считывают данные, точнее их версию, по состоянию на момент начала своей транзакции.  
На самом деле создается копия данных на момент начала транзакций, которая используется другими транзакциями. Этот уровень **слабее serializable**, но **сильнее остальных**. Хотя изоляция и не полная.  
Она НЕ блокирует другие транзакции от чтения данных. Другие транзакции меняющие данные НЕ блокируют snapshot транзакцию от чтения меняемых ими данных.

| Isolation level  | Lost updates | Dirty reads | Non-repeatable reads | Phantoms |
| ---------------- | ------------ | ----------- | -------------------- | -------- |
| Read Uncommitted | no           | yes         | yes                  | yes      |
| Read Committed   | no           | no          | yes                  | yes      |
| Repeatable Read  | no           | no          | no                   | yes      |
| Serializable     | no           | no          | no                   | no       |

- **Настройка**
  - Глобльно через `hibernate.connection.isolation=REPEATABLE_READ`
  - Отдельно
    
    ```java
          // READ COMMITTED - значение по умолчанию???
          @Transactional(isolation=Isolation.READ_COMMITTED)
          public void someTransactionalMethod(User user) {
          // Interact with testDAO
          }
    ```
    
### Isolation level для денег и пр.

Тут интересная табл.: https://developer.jboss.org/message/865105#865105

Рекомендуется использовать **Repeatable Read** and **Optimistic locking** (объявив поле `@Version`)
<br>
(**Optimistic locking** нужна, чтобы убедиться, что не будет update, если другая транзакция уже изменила данные.)

Для **Decision Center data access** рекомендуется **TRANSACTION_READ_COMMITTED**

## Propagation level

Источник [тут](https://stackoverflow.com/questions/8490852/spring-transactional-isolation-propagation)  
Про propagation level и их правильное использование [тут](https://www.ibm.com/developerworks/library/j-ts1/index.html)

Определяет как будет создаваться транзакция внутри транзакции. Методы DAO, т.е. слоя @Repository тоже могут быть отмечены аннотацией @Transactional. И Propagation определяет как будет ли создаваться новая транзакция если @Transactional есть и в слое @Service:

- Список:
  - **TIMEOUT_DEFAULT** (значение `-1`) - использовать propogation level по умолчанию (т.е. REQUIRED для Spring)
  - **Propagation.REQUIRED** (default) — выполняться в существующей транзакции, если она есть, иначе создавать новую
  - **Propagation.REQUIRES_NEW** — всегда выполняться в новой независимой транзакции. Если есть существующая, то она будет остановлена до окончания выполнения новой транзакции. Сохдается новая полностью независимая транзакция, commit или rollback которой произойдет независимо от внешней транзакции, у нее свой isolation level, locks etc. Внешняя транзакция будет остановлена вначале выполнения новой транзакции и возобновлена после. **Прим.** (не точно!) если будет exception в новой транзакции (`@Transaction(rollBackFor=...)` параметр для отката), то откат произойдет только в новой транзакции, но не в родительской к ней. Если Runtime exception, то откат будет, а если checked exception, то нет.
  - **Propagation.MANDATORY** — выполняться в существующей транзакции, если она есть, иначе (если ее нету) генерировать исключение.
  - **Propagation.SUPPORTS** — выполняться в существующей транзакции, если она есть, иначе выполняться вне транзакции (т.е. просто команды как буд-то транзакция не запущена).
  - **Propagation.NOT_SUPPORTED** — всегда выполняться вне транзакции. Если есть существующая, то она будет остановлена.
  - **Propagation.NESTED** — выполняться в существующей транзакции, если она есть. Если вложенная транзакция будет отменена, то это не повлияет на внешнюю транзакцию; если будет отменена внешняя транзакция, то будет отменена и вложенная. inner transaction часть внешней транзакции. Если текущей транзакции нет, то просто создаётся новая. Достигается путем установки savepoints вокруг вызова inner transaction. При этом inner transaction часть внешней транзакции и будет commited вконце внешней транзакции.
  - **Propagation.NEVER** — всегда выполнять вне транзакции, при наличии существующей генерировать исключение.

```
+-------+---------------------------+------------------------------------------------------------------------------------------------------+
| value |        Propagation        |                                             Description                                              |
+-------+---------------------------+------------------------------------------------------------------------------------------------------+
|    -1 | TIMEOUT_DEFAULT           | Use the default timeout of the underlying transaction system, or none if timeouts are not supported. |
|     0 | PROPAGATION_REQUIRED      | Support a current transaction; create a new one if none exists.                                      |
|     1 | PROPAGATION_SUPPORTS      | Support a current transaction; execute non-transactionally if none exists.                           |
|     2 | PROPAGATION_MANDATORY     | Support a current transaction; throw an exception if no current transaction exists.                  |
|     3 | PROPAGATION_REQUIRES_NEW  | Create a new transaction, suspending the current transaction if one exists.                          |
|     4 | PROPAGATION_NOT_SUPPORTED | Do not support a current transaction; rather always execute non-transactionally.                     |
|     5 | PROPAGATION_NEVER         | Do not support a current transaction; throw an exception if a current transaction exists.            |
|     6 | PROPAGATION_NESTED        | Execute within a nested transaction if a current transaction exists.                                 |
+-------+---------------------------+------------------------------------------------------------------------------------------------------+
```

**Проверить факт:**

- Если метод с @Transaction вызовется другим методом @Transaction **из того же** класса, то Propagation не сработает и метод выполнится в той же транзакции???

Пример:

```java
@Repository
class DAOA {
    // @Transactional в @Repository
    @Transactional
    List getUsers() {
        return Criteria.createCriteria(User.class).list();
    }
}

// @Transactional в @Service
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ConstraintViolationException.class})
public Long saveTask(Long userId) {
    Session session = sessionFactory.getCurrentSession();
    User user = userDao.getUserByLogin("user1");
    Tasks task = new Tasks();
    task.setName("Задача 1");
    // ...
    task.setUser(user);
    session.saveOrUpdate(task);
    return task.getTaskId();
}
```

## Global (XA) vs local transactions vs distributed transactions

- Источники:
  
  - http://samolisov.blogspot.com/2011/02/xa-jta-javase-spring-atomikos-2.html
  - https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-jta.html
  - https://docs.spring.io/spring/docs/2.0.8/reference/transaction.html

- **Коротко:**
  
  - **Global transaction** - может объединять в 1ой transaction несколько ресурсов: DB (JDBC connection pool если точнее), JMS etc. Реализована в JavaEE поэтому часто пишут "управляется в application server" потому что jar с реализацией это часть applicaton server, который реализует JavaEE???
  - **Local transaction** - работа с одним источником данных, напр. JDBC connection
  - **distributed transaction** (не относится к Spring JTA) - это когда не просто несколько источников данных, а несколько приложений на рзных applicaton server и в рамках этих приложений должна выполняться транзакция

**XA** (extended architecture) - **другой определение** (возможно неточное!). Это стандарт, который связывает источники данных (**XA-resources**) с transaction manager. Чтобы работало XA должно быть реализовано для DB.

- **Global** (**XA** (extended architecture), distributed) **transaction** - часть спецификации JTA (Java EE). Работает с несколькими источниками данных одновременно. Использует спецификация XA или его модицикации. Для работы СУБД, JMS и их драйвера (JDBC, JMS driver) должны поддерживать протокол XA из JTA. **Другими словами:** это транзакция внутри которой может выполнять много других транзакций к различным исчтоникам данных (DB или JMS).

Источники данных **должны реализовать XA object** (напр. класс XARessources). Чтобы можно было работать с XA. Для кажого источника данных свой XA. Общение с DB или JMS происходит через их XA объекты (напр. получение статусов транзакции).

**Использование Global transaction:** Spring (JtaTransactionManager) поддерживает **global transactions**. Поддержка включается сама, если используются реализации **Atomikos** or **Bitronix** или внутри подходящего Java EE Application Server (т.е. в classpath есть jar с реализацией XA и она настроена). Для **global transactions** можно исползовать стандартные средства как: `@Transactional`. Если нужно принудительно отключить **global transactions** и использовать **Local transaction**, то нужно установить `spring.jta.enabled=false`

- **XA** (extended architecture) - спецификация распределенных транзакций

- **XA-resources** - источники данных DB (JDBC connection pool если точнее), JMS etc

- **XA-transaction.** - завершается только если commit во все источники данных (XA-resources) завершился успешно, иначе rollback. Реализована через алгоритм **Two-Phase Commit** (2PC)

- **XA-transaction coordinator** - координатор XA транзакций

- **Как работает Two-Phase Commit** (2PC):
  
  - **Этап подготовки** (commit-request phase (or voting phase)) - **XA-transaction coordinator** рассылает команды (prepare message) к DB сделать транзакцию, у каждой транзакции свой номер (TID). Транзакции выполняются, но их состояние не фиксируется, хранится на диске.
  - **Этап фиксации** (commit phase) - если все транзакции успешны **XA-transaction coordinator** фиксирует их, если нет посылает команды к DB чтобы откатить.

XA **поддерживает и 1 phase commit** если есть только один источник данных (single resource).

- По умолчанию в Spring Boot есть jar реализией Global transaction?

- Как включить local transaction и какие у них плюсы/минусы?
  
  ## Two-Phase Locking (2PL)
  
  Тут будет описание алгоритма Two-Phase Locking (2PL) и его типов
  
  # Lock

- Источники:
  
  - https://www.oreilly.com/library/view/mysql-reference-manual/0596002653/ch06s07.html
  - http://www.mysqltutorial.org/mysql-table-locking/
  - Как 2х фазная блокировка использует блокировки DB https://en.wikipedia.org/wiki/Two-phase_locking
  - Что такое и как работают блокировки https://www.youtube.com/watch?v=a74V14OnDvw
  - Как транзакции используют блокировки https://docs.oracle.com/cd/A97335_02/apps.102/bc4j/developing_bc_projects/bc_atransactionsandlocks.htm
  - табл. блокировок и проблемы от которых они помогают https://stackoverflow.com/a/20532385
  - подробно о уровнях блокировки https://vladmihalcea.com/a-beginners-guide-to-java-persistence-locking/
  - интересная инфа https://en.wikibooks.org/wiki/Java_Persistence/Locking
  - отличный список типов блокировок jpa с описанием читать если нужно использовать на практике https://stackoverflow.com/questions/33062635/difference-between-lockmodetype-jpa

## Описание

**Locks** (блокировки, аналог mutex в программировании) - используются чтобы ограничить одновременный доступ к одним и тем же данным нескольким разным потокам (по сути монитор в программировании). Т.е. в SQL блокирует изменение и/или чтение одних и тех же данных разными запросами, чтобы запросы не читали и/или писали их одновременно.

Процесс изменения данных при работе с заблокировкнными ресурсами (табл. или строками табл.) называют тоже **transaction**. **Напр.** при optimistic locks если transaction (операция во время блокировки) обнаружила, что версия данных не подходит (уже была изменена кем-то), то она откатится (и выбросит исключение?).

**Реализация Locks** (optimistic и pessimistic) может быть разной в разных DBMS. Хотя общие черты есть. В некоторых статьях про блокировки написано, что они транзакции или учавствуют в транзакциях (**уточнить!**).

- Типы Locks
  - **optimistic** - предполагает, что данные изменяются редко и откатывать блокировку скорее всего не придется. Используют поле номера версии в табл. или поле содержащее дату последнего commit (поле с аннотацией `@Version`) чтобы определить не затерает ли текущее изменение блокированной табл. новые данные. При `commit()` происходит проверка версии в памяти и базе в случае совпадения сохраняется и увеличивает версию. В противном исключение `OptimisticLockException`
  - **pessimistic** - предполагает, что данные скорее всего будут кем-то меняться одновременно (операции с блокированной DB частые). Данные блокируются полностью пока не запрос не сделает unlock.

**Использование на практике** (КАК ПОНИМАЮ Я). Нужно использовать lock внутри transactions при этом isolation level у transaction должен быть не ниже **read commit** (как написано в некоторых советах). **transactions** предотвратят последствия ошибок, а **locks** могут предотвратить само появление ошибок.

**Read vs. Write locks** - **Read locks** это **shared locks**, который запрещает (блокирует) write lock, но не другие read locks. **Write locks** это **exclusive locks**, которые запрещают все locks (read и write locks). Запреты происходят для **других session** в SQL, пока **текущая session** в SQL не отпустит lock.

- **LockModeType** - для установки типа блокировки на строке, табл. или в методе операций (`save/load/...`)
  - **NONE**
  - **OPTIMISTIC** аналог **READ**
  - **OPTIMISTIC_FORCE_INCREMENT** аналог **WRITE** - с принудительным обновлением версии
  - **PESSIMISTIC_FORCE_INCREMENT** - с принудительным обновлением версии
  - **PESSIMISTIC_READ**
  - **PESSIMISTIC_WRITE**

**`... FOR UPDATE` vs `LOCK IN SHARE MODE`** - обе предотвращают изменения строк пока блокированные строки читаются. `LOCK IN SHARE MODE` не блокирует другие транзакции от чтения блокированной строки. `FOR UPDATE` - предотвращает блокирующие чтения (blocking reads) этой же блокированной строки, не-блокирующее чтение (non-locking reads) по прежнему может читать строку.

**`SELECT ... FOR UPDATE`** - это pessimistic write lock (не точно, [источник](https://stackoverflow.com/a/47476818))

- что за версия в pessimistic lock которую увеличивает `PESSIMISTIC_FORCE_INCREMENT`?

## optimistic lock

**Tip.** optimistic locks работает по умолчанию если в табл. есть поле помеченное `@Version`. Date тип версии (timestamps) менее надежен чем тип int.

**Как использовать optimistic locks.** При этом Entity должно иметь поля с версией табд. отмеченно `@Version` и оно не может быть null.

- Блокировать вручную `session.lock(person, LockModeType.OPTIMISTIC);`
- Блокировать при вызове операции `session.refresh(person, LockModeType.OPTIMISTIC_FORCE_INCREMENT);`
- Блокировать декларативно
  
  ```java
    interface WidgetRepository extends Repository<Widget, Long> {
        @Lock(LockModeType.OPTIMISTIC)
        Widget findOne(Long id);
    }
  ```

```java
// 1.
@Entity
class User implements Serializable {
    @Version
    int versionNum; //или java.util.Date

    @Id
    int id;
}

// 2.
@QueryHints(value = { @QueryHint(name = "hibernate.query.followOnLocking", value = "false")}, forCounting = false)
@Lock(LockModeType.PESSIMISTIC_WRITE)
User findUserById(@Param("id") String operatorId)
```

**Отключение Optimistic Lock для связанных Entity**

```java
class Membership {   
     // отключаем для User, будет действовать только на Membership
     @OptimisticLock(excluded=true)
     User user;       
}
```

## pessimistic lock

**Tip.** Hibernate не блокирует объекты в памяти (Entity) для pessimistic lock, используются блокировки только в DB.

- Cпособ вручную через `setLockOptions()`
    ```java
    User user = entityManager.createQuery(
        "select u from User u where id = :id", User.class)
        .setParameter("id", id);
        .unwrap( Query.class )
        .setLockOptions(
            new LockOptions( LockMode.PESSIMISTIC_WRITE )
                .setFollowOnLocking(false)
    ).getSingleResult();
    ```
```java
Session.lock(person, LockModeType.PESSIMISTIC_READ) //первый способ

Query q = em.createQuery(...);
Query.setLockMode(LockModeType.PESSIMISTIC_FORCE_INCREMENT) //второй способ
```
***

**Типы pessimistic lock** и табл. их использования:
| LockModeType                        | PESSIMISTIC_READ                                                 | PESSIMISTIC_WRITE                                                                                    |
| ----------------------------------- | ---------------------------------------------------------------- | ---------------------------------------------------------------------------------------------------- |
| **type**                            | **SHARED LOCK**                                                  | **EXCLUSIVE LOCK**                                                                                   |
| isReadOnly without additional locks | YES                                                              | NO                                                                                                   |
| dirty reads                         | NO                                                               | NO                                                                                                   |
| non-repeatable reads                | NO                                                               | NO                                                                                                   |
| how to update data                  | obtain PESSIMISTIC_WRITE                                         | ALLOWED                                                                                              |
| how to obtain lock                  | no one holds PESSIMISTIC_WRITE                                   | no one holds PESSIMISTIC_READ or PESSIMISTIC_WRITE                                                   |
| when to use                         | you want to ensure no dirty or non-repeatable reads are possible | when there is a high likelihood of deadlock or update failure among concurrent updating transactions |

***

## Атрибуты `@Version` для **optimistic lock**

Это атрибуты, которыми можно менять поведение `@Version` проставленной на поле версии табл.

**Атрибуты @Version (для типа int):**
| Имя           | Описание                                                                                                                                                                                                                             |
| ------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| column        | Имя колонки, в которой находится номер версии. Опционально, по-умолчанию такое же как у имени свойства.                                                                                                                              |
| name          | Имя свойства персистентного класса.                                                                                                                                                                                                  |
| type          | Тип номера версии. Опционально, по-умолчанию integer.                                                                                                                                                                                |
| access        | Стратегия Hibernate для доступа к значению свойства. Опционально, по-умолчанию property                                                                                                                                              |
| unsaved-value | Показывает, что экземпляр только что создан и тем самым не сохранен. Выделяет из отсоединенных сущностей (detached). Значение по-умолчанию, undefined, показывает, что свойство-идентификатор не должно использоваться. Опционально. |
| generated     | Показывает, что свойство версии должно генерироваться базой данных. Опционально, по-умолчанию never.                                                                                                                                 |
| insert        | Включать или нет колонку версии в выражение SQL-insert. По-умолчанию true, but вы можете поставить это в false если колонка в БД определена со значением по-умолчанию 0                                                              |
**Атрибуты @Version (для типа Date):**
| Имя           | Описание                                                                                                                                                                                                                                                                                                                                                                                                   |
| ------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| column        | Имя колонки, в которой находится временная метка. Опционально, по-умолчанию такое же, как и имя свойства.                                                                                                                                                                                                                                                                                                  |
| name          | Имя JavaBeans-свойства типа Date или Timestamp у персистентного свойства.                                                                                                                                                                                                                                                                                                                                  |
| access        | Стратегия, которую Hibernate использует для доступа к значению свойства. Опционально, по-умолчанию property.                                                                                                                                                                                                                                                                                               |
| unsaved-value | Показывает, что экземпляр только что создан и тем самым не сохранен. Выделяет из отсоединенных сущностей (detached). Значение по-умолчанию, undefined,показывает что свойство-идентификатор не должно использоваться. Опционально.                                                                                                                                                                         |
| source        | Извлекает ли Hibernate метку из БД или из текущей JVM. БД-метки вносят дополнительный оверхэд, т.к Hibernate нужно запрашивать БД каждый раз для определения инкремента. Однако, БД-метки более безопасны при использовании в кластеризованном окружении. Не все диалекты БД поддерживают извлечение текущих временных меток из БД. Другие могут быть небезопасны для блокировок, из-за нехватки точности. |
| generated     | Генерируется ли метки средствами БД. Опционально, по-умолчанию never.                                                                                                                                                                                                                                                                                                                                      |

## Lock в SQL

```sql
-- блокировка, разблокировка
LOCK TABLE tbl WRITE;
UNLOCK TABLES;

-- блокировка одной командой нескольких табл.
LOCK TABLES trans READ, customer WRITE;

-- инфа о процессах (сессиях в SQL), можно получить состояние
SHOW PROCESSLIST
```

# Transaction vs Lock

Этот раздел поясняющий понятия своими словами, компиляция советов из интернета.

- [подборка со stackoverflow](https://stackoverflow.com/questions/4226766/mysql-transactions-vs-locking-tables)
  
  - **lock и transaction** не связаны друг с другом. Внутри transaction можно использовать lock, чтобы блокировкть некоторые строки (для лучшего соблюдения ACID)
  - **lock предотвращает** изменения записей DB на которых сделан lock от изменения кем-то еще (т.е. используется чтобы предотвратить само появление ошибок)
  - **transaction предотвращает** следит за тем, чтобы конечный результат группы запросов был верен (т.е. откатывает все изменения в случае ошибки)
  - **lock** предотвращает **concurrent** операции (синхронизирует записи), **transaction** - **изолирует данные** (от ошибочного изменения)
  - **lock и transaction** ведут себя согласно спецификациям, но по разному. Можно добиться схожего поведения (блокировки данных) **и через lock, и transaction**.

- для чего вообще lock если можно проставить нужный isolation level в транзакция?

# Cache

1. **First Level Cache (L1)** - включен по умолчанию, кэш в пределлах сессии. Привязан к Session, его нельзя отключить.
2. **Second Level cache (L2)** - выключен по умолчанию, кэш между сессиями (уменьшает трафик к БД). Кэш привязан к фабрике сессий (поэтому существует между сессиями).
   - across session in an application
   - across applications
   - across clusters
3. **Query cache** (формально это расширение кэша L2, данные берутся из кэша L2) - выключен по умолчанию, кэш запросов, кэширует используя запрос в качестве ключа (как в Map); берет результат из кэша, если запросы совпадают. Эффективен для read-only операций. Используется для запросов, которые не сохраняются в L1 и L2

**Note:** связанные Entity с кэшируемыми Entity по умолчанию не кэшируются. Нужно указывать @Cache для них отдельно.

**Note:** В кэше объекты не хранятся, т.к. занимают много памяти. Сам кэш реализован что-то вроде Map в которой ключ объекта это id, а хранятся строки, числа.
В Query cache ключи это не id объектов, а параметры запроса.

**Note:** First Level Cache еще называют persistence context.
Т.е. кэш L1 это на самом деле одна из основ Hibernate ORM к которой привязаны объекты и транзакции???

**Note:** Hibernate ORM пытается достать запрошенный объект из кэша L1, если там нету, то из L2, если и там нету, то из БД.

**Note:** При каждом non hibernate запросе SQL происходит полная очистка кэша L2. Чтобы синхронизировать состояние табл. и кэша. Если мы знаем какие данные изменятся, то можно вручную указать region (набор данных) который нудно чистить, чтобы не чистить весь кэш L2. Делается это через методы: `addSynchronizedEntityClass, addSynchronizedQuerySpace etc`

**Note:** Аннотацию @Cache можно использовать над классами или полями.

**Note:** Collection Cache - это указание кэшировать в L2 поля которые Collection отмеченные связями @OneToMany etc, т.к. они не кэшируются даже если класс в котором они находятся отмечен кэшируемым.

- **Методы Session:**
  - flush() — синхронизирует объекты сессии с БД и в то же время обновляет сам кеш сессии.
  - evict() — нужен для удаления объекта из кеша cессии (напр. кода данные устарели, т.е. обновить принудительно).
  - contains() — определяет находится ли объект в кеше сессии или нет.
  - clear() — очищает весь кеш.

## Подключение и примеры

**1) First Level Cache (L1)**

```java
session = sessionFactory.openSession();
session.openTransaction();
User user = (User) session.get(User.class, 1);
user.setName("New Name");
user = (User) session.get(User.class, 1); //т.к. объект в кэше Level 1, то 2-го SELECT к базе нету
session.getTransaction().commit();
session.close();
```

**2) Second Level cache (L2)**

**Включение**

1. Установить: ```cache.provider_class=org.hibernate.cache.EnCacheProvider``` (или другую реализацию)
2. Добавить jar реализации L2 кэша
3. Установить `cache.use_second_level_cache=true`

**Библеотки с реализацией L2 кэша:**

1. **EHCache** - это быстрый и простой кэш. Он поддерживает read-only и read/write кэширование, а так же кэширование в память и на диск. Но не поддерживает кластеризацию.
2. **OSCache** - это другая opensource реализация кэша. Помимо всего, что поддерживает EHCache, эта реализация так же поддерживает кластеризацию через JavaGroups или JMS.
3. **SwarmCache** - это просто cluster-based решение, базирующееся на JavaGroups. Поддерживает read-only и нестрогое read/write кэширование. Этот тип кэширование полезен, когда количество операций чтения из БД превышает количество операций записи.
4. **JBoss TreeCache** - предоставляет полноценный кэш транзакции.
   
```java
// 1.
session = sessionFactory.openSession();
session.beginTransaction();
User user = (User) session.get(User.class, 1);
session.getTransaction().commit();
session.close();

// 1. Т. к. объект не изменился и включен кэш второго уровня, то и в другой сессии, объект достается из кэша.
session = sessionFactory.openSession();
session.beginTransaction();
User user = (User) session.get(User.class, 1); //тут SELECT не выполнится, достанется из кэша L2
session.getTransaction().commit();
session.close();
```

**3) Query cache (extension of L2)**

**Включение**
1. Включить кэш L2
2. Установить `cache.use_query_cache=true`
```java
session = sessionFactory.openSession();
session.beginTransaction();
Query query = session.createQuery("from User");
query.setCacheable(true); //кэшируем результат запроса с такими ПАРАМЕТРАМИ
List1 = query.list();
session.getTransaction().commit();
session.close();

session = sessionFactory.openSession();
session.beginTransaction();
Query query2 = session.createQuery("from User");
query2.setCacheable(true); //кэшируем результат запроса с такими ПАРАМЕТРАМИ
List2 = query2.list(); //т.к. user не менялся в базу отправить ТОЛЬКО один SELECT
session.getTransaction().commit();
session.close();
```

## Когда не нужно использовать кэш L2 и Query cache

1. Если данные изменяются другим application
2. Если кэшированные данные не переиспользуются (то и кэш не нужен)
3. Если есть системы аудита SQL запросов и БД (то кэш они не увидят, нужны реальные запросы к БД)
4. Когда кэш L1 содержит все запрашиваемые данные (тогда и L2 не нужен)
5. Когда читаемых данных из БД слишком много и кэш не справляется

## Cache region

**cache region** - это регион к которому привязан кэш (логическая область памяти).  Для каждого региона можна настроить свою политику кеширования. **Если регион не указано**, то используется регион по умолчанию с именем класса к которому применено кэширование: `region name == пакет + класс + имя_поля`  
Пример имени region: `org.models.MyEntity.myField`

**Note:**
Collection которые внутри классов привязаны к отдельным регионам (т.е. те `List/Set/Map/Collection` что со связями `@ManyToOne`, `@OneToMany` etc.). Поэтому на них не действует настройка класса в которых они расположены. И поэтому их нужно ОТДЕЛЬНО отмечать как кэшируемые. В большинстве случаев итмечать кэшируемыми Collections, если родительский класс кэшируем - это хорошо.

**Note:**
Кэш нужно чистить, если он устарел. Т.к. кэш L2 чиститься весь в ситуациях, когда данные БД изменены, чтобы синхронизироваться с кэшем. То чтобы не чистить весь кэш L2 можно указать конкретный region кэша (класс, поле, запрос?), который нужно почистить.

- **Для очистки кэша** используется метод evict() или указание CacheMode (storage CacheStoreMode и retrieval CacheRetrieveMode в JPA):
  
  1. `session.getSessionFactory().getCache().evictQueryRegion( "query.cache.person" );` - чистка по region
  2. `session.setCacheMode( CacheMode.REFRESH )` - установка режима кэша, который будет срабатывать автоматом
  3. `users.put( "javax.persistence.cache.storeMode" , CacheStoreMode.REFRESH );` - указание поведения кэша при добавлении в Map

- **Cache modes relationships** - это режимы автоматической работы кэша. Если их установить, то кэш будет очищаться автоматически в соотв. с режимом.
  Устанавливаются глобально или прямо в функции-операции. Список режимов:
  
  - **CacheMode.NORMAL**
  - **CacheMode.REFRESH**
  - **CacheMode.PUT**
  - **CacheMode.GET**
  - **CacheMode.IGNORE**

**Очистка кэша по region.** Т.е. методы очистки содержат слово synchronized в том числе в xml (в случае xml конфигов)
Т.е. synchronized - какой region синхронизировать с DB, т.е. обновить для него кэш:

```java
// чистим кэш L2 не весь, а только привязанный к классу Foo (т.е. только объект Foo удалиться)
nativeQuery.unwrap(org.hibernate.SQLQuery.class).addSynchronizedEntityClass(Foo.class);
```

**Можно назначать регион:**

```java
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "STATIC_DATA") //включаем кэш и назначение ему региона
query.setCacheRegion("STATIC_DATA"); // назначаем регион для Query
criteria.setCacheRegion("STATIC_DATA"); // назначаем регион для Criteria
```

## Стратегии кэширования

- **Типы**:
  
  1. **NONE**
  2. **READ_ONLY** - для данных, который часто читаются, но не меняются
  3. **NONSTRICT_READ_WRITE** - для данных, которые часто читаются и редко меняются; применяется когда для данных не будет конфликтующих транзакций
  4. **READ_WRITE** - для данных, которые часто читаются/пишутся. При этом нельзя использовать с serializable transaction isolation (т.е. менее надежная для транзакций)
  5. **TRANSACTIONAL** - Полностью защищенные транзакции. Для реализаций кэша вроде JBoss TreeCache (не во всех реализациях кэша есть этот тип)

- **Требования** для стратегий кэша:
  
  - **NONSTRICT_READ_WRITE, READ_WRITE**:
    1. Транзакция должна завершиться до (!) вызова Session.close() or Session.disconnect()
    2. Для JTA environment нужно настроить `hibernate.transaction.manager_lookup_class`
  - **READ_WRITE**:
    cache implementation класс должен поддерживать locking (блокировки). Встроенная реализация может не поддерживать.
  - **TRANSACTIONAL**:
    Для JTA environment нужно настроить `hibernate.transaction.manager_lookup_class`

- **Настройка стратегий кэша** (providers кэша):
  
  1. **hibernate.cache.use_minimal_puts** - оптимизирует L2 cache операции writes, но ценой более частых reads
  2. **Опции** - могут оптимизировать или форматировать данные ценой производительности других операций. Через свойство (или другое для hibernate) `javax.persistence.sharedCache.mode` 
     - **ENABLE_SELECTIVE** - по умолчанию. Не кэширует, пока entity не отметить вручную как @Cacheable
     - **DISABLE_SELECTIVE** - как ENABLE_SELECTIVE, только наоборот
     - **ALL** - принудительно кэшировать и не смотреть на @Cacheable
     - **NONE** - принудительно не кэшировать
       
       ```java
       @Entity
       @Cacheable //указываем что класс надо кэшировать
       @Cache( //специфическая аннотация hibernate для настройки кэша
       usage = CacheConcurrencyStrategy.READ_ONLY
       )
       class User {}
       ```
       
# Entity

## Entity описание, операции, требования

**Entity** это легковесный хранимый объект бизнес логики (persistent domain object).

Если понадобится посмотреть **возможные генерируемые типы полей в DB** установленные аннотацией `@Type`, то в [этой](http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#basic-provided) таблице (их много).

- **Возможные типы полей Entity в JPA** (помимо custom типов):
  
  1. примитивные типы и их обертки Java,
  2. String,
  3. реализующие Serializable интерфейс
  4. enums;
  5. entity types;
  6. embeddable классы
  7. java.math.BigInteger, java.math.BigDecimal
  8. java.util.Date, java.util.Calendar, java.sql.Date, java.sql.Time, java.sql.Timestamp
  9. Collection, Set, List, Map

- **Возможные типы primary key для Entity в JPA** - типы помимо тех что в этом списке **не будут переносимы** в другие DB. Только целочисленные типы **автогенерируемы**.
  
  1. примитивные типы и их обертки Java,
  2. String,
  3. BigDecimal и BigInteger,
  4. java.util.Date, java.sql.Date

- **По умолчанию для стобцов в JPA** (даже если аннотаций не стоит, но есть `@Entity`)
  
  1. `@Basic` над всеми столбцами
  2. `java.sql.Clob` или `java.sql.Blob`, то оно преобразуется в `@Lob`
  3. если тип свойства `Serializable`, то оно преобразуется как `@Basic` в столбец, содержащий объект в сериализованном виде;
  4. для свойства аннотированного как `@Embeddable`, его mapping получит значение `@Embedded`;

- **Что может Entity в JPA**:
  
  - наследовать non-entity classes
  - наследовать entity classes
  - наследоваться non-entity классом
  - быть abstract

- **Что обязан Entity в JPA**:
  
  - иметь `@Entity` аннотацию или аналог в xml
  - содержать хотя бы 1ин public или protected конструктор без аргументов
  - быть top-level class
  - не быть enum или interface
  - не быть final class
  - не содержать final methods или final variables на которых использован mapping
  - Если объект Entity класса будет передаваться по значению как отдельный объект (detached object), например через удаленный интерфейс (through a remote interface), он так же должен реализовывать Serializable интерфейс
  - все поля должны быть private
  - иметь id (primary key)

- **Типы элементов Entity в JPA**:
  
  1. property access - public свойства классов
  2. field access - get/set для private полей

- **Основные операции с Entity в JPA** - EntityManager это интерфейс, который описывает API для всех основных операций над Enitity, получение данных и других сущностей JPA. По сути главный API для работы с JPA. Основные операции:
  
  1. Для операций над Entity: persist (добавление Entity под управление JPA), merge (обновление), remove (удаления), refresh (обновление данных), detach (удаление из управление JPA), lock (блокирование Enity от изменений в других thread),
  2. Получение данных: find (поиск и получение Entity), createQuery, createNamedQuery, createNativeQuery, contains, createNamedStoredProcedureQuery, createStoredProcedureQuery
  3. Получение других сущностей JPA: getTransaction, getEntityManagerFactory, getCriteriaBuilder, getMetamodel, getDelegate
  4. Работа с EntityGraph: createEntityGraph, getEntityGraph
  5. Общие операции над EntityManager или всеми Entities: close, isOpen, getProperties, setProperty, clear

## Custom types полей

Помимо стандартных типов полей в Entity можно создать свои.

- **Использование:**
  1. реализуем обертки для преобразование в данные подходящтие для JDBC и обратно через методы wrap и unwrap
  2. регестрируем обертки в Hibernate и используем нужный тип для которых Hibernate будет использовать обертки

**Можно создать custom type:**

```java
// РЕАЛИЗАЦИЯ оберток
// реализуем AbstractStandardBasicType или AbstractSingleColumnStandardBasicType. Можно и BasicType, но там много лишних методов
public class BitSetType
        extends AbstractSingleColumnStandardBasicType<BitSet>
        implements DiscriminatorType<BitSet> {
    public static final BitSetType INSTANCE = new BitSetType();
    public BitSetType() {
        super( VarcharTypeDescriptor.INSTANCE, BitSetTypeDescriptor.INSTANCE );
    }
    @Override
    public BitSet stringToObject(String xml) throws Exception {
        return fromString( xml );
    }
    @Override
    public String objectToSQLString(BitSet value, Dialect dialect) throws Exception {
        return toString( value );
    }
    @Override
    public String getName() {
        return "bitset";
    }
}

// также реализуем AbstractTypeDescriptor для SQL
// содержит много методов. Главные это wrap для трансформации DBC column value object в BitSet (нужный тип). И unwrap чтобы использовать в PreparedStatement
public class BitSetTypeDescriptor extends AbstractTypeDescriptor<BitSet> { }

// СПОСОБ 1
// регистрация класса BitSetType
ServiceRegistry standardRegistry =
    new StandardServiceRegistryBuilder().build();
MetadataSources sources = new MetadataSources( standardRegistry );
MetadataBuilder metadataBuilder = sources.getMetadataBuilder();
metadataBuilder.applyBasicType( BitSetType.INSTANCE );

// использование
@Entity(name = "Product")
public static class Product {
    @Type( type = "bitset" ) private BitSet bitSet;
}

// СПОСОБ 2
// регистрация класса BitSetType
@Entity(name = "Product")
@TypeDef(
    name = "bitset",
    defaultForType = BitSet.class,
    typeClass = BitSetType.class
)

// использование
public static class Product {
    private BitSet bitSet;
}
```

# Naming strategies

Представлено классами `ImplicitNamingStrategy` и `PhysicalNamingStrategy` классами. Есть устаревшие NamingStrategy и ImprovedNamingStrategy классы. **В Spring Boot** устанавливается `spring.jpa.hibernate.naming.physical-strategy=com.mypackage.MyStrategy` и `hibernate.implicit_naming_strategy`.

**1. ImplicitNamingStrategy** - используется когда mapping не устанавливает имена явно таблице или столбцу. Hibernate имеет несколько out-of-the-box реализаций. Можно создать свое правило именования.  
Можно использовать `org.hibernate.boot.MetadataBuilder#applyImplicitNamingStrategy` во время Bootstrap (запуска), чтобы установить эту стратегию.

**Другими словами:** `ImplicitNamingStrategy` - это если в mapping не указано, что стобцу табл. `accountNumber` соответствует атрибут Entity класса `accountNumber`, то правило `ImplicitNamingStrategy` само подберет их соответствие.

**Стратегии** `ImplicitNamingStrategy` (пакет `org.hibernate.boot.model.naming.*`) - чтобы их установить используется свойство `hibernate.implicit_naming_strategy`. В качестве значения можно использовать "short name" (список ниже), полную ссылку на класс который реализует `ImplicitNamingStrategy` или FQN класса.

- **Список стратегий:**
  - **default** - класс `ImplicitNamingStrategyJpaCompliantImpl`
  - **jpa** - класс `ImplicitNamingStrategyJpaCompliantImpl` из JPA 2.0
  - **legacy-hbm** - оригинальная `NamingStrategy` стратегия из Hibernate (условно старая)
  - `ImplicitNamingStrategyLegacyHbmImpl`
  - **legacy-jpa** - `ImplicitNamingStrategyLegacyJpaImpl` старая стратегия JPA 1.0, которая плохо описана
  - **component-path** - `ImplicitNamingStrategyComponentPathImpl` почти как `ImplicitNamingStrategyJpaCompliantImpl`, только использует полные пути в именах вместо частичных окончаний (ending property part)

**2. PhysicalNamingStrategy** - задает имена database objects (tables, columns, foreign keys, etc). Идея в том чтобы не писать hard-code имена в mapping, а использовать общее правило. Например можно для атрибут Entity класса `accountNumber` использовать сокращение `acct_num` в табл.

**ImplicitNamingStrategy vs PhysicalNamingStrategy**. В некоторых случаях **ImplicitNamingStrategy** можно использовать для тех же целей что и **PhysicalNamingStrategy**, но не нужно этого делать, потому что идея в логическом разделении этих операций. **PhysicalNamingStrategy** будет применена даже если `@Column(name = "...")` задана. **ImplicitNamingStrategy** будет применена только если `@Column(name = "...")` не задана.

```java
public class CustomPhysicalNamingStrategy implements PhysicalNamingStrategy {
    // методы
}
```

# AttributeConverter

Позволяет объявить converter который будет применен к полю Entity.

**Например**, если поле содрежит сумму в деньгах, то чтобы конвертировать деньги в нужный формат поля Entity.

- Бывают - если тип поля Entity будет Immutable, то **Immutable**, если тип поля Mutable, то **Mutable**:
  - для **Immutable** types - String, a primitive wrapper (e.g. Integer, Long) an Enum type или другой не изменяемый Object, то можно только переназначить и нельзя менять сам объект
  - для **Mutable** types - их структуру можно менять и для них Hibernate будет менять значения в БД. Не смотря на то что для них работает L2 кэш, dirty checking etc они всеравно медленнее чем **Immutable**.
  - **Immutable vs Mutable** - предпочтительно использовать immutable, если это возможно. Это эффективнее.

```java
// создаем (пример для Immutable типа)
@Converter
public class PeriodStringConverter
        implements AttributeConverter<Period, String> {
    @Override
    public String convertToDatabaseColumn(Period attribute) {
        return attribute.toString();
    }
    @Override
    public Period convertToEntityAttribute(String dbData) {
        return Period.parse( dbData );
    }
}

// используем
@Entity(name = "Event")
public static class Event {
    @Convert(converter = PeriodStringConverter.class)
    private Period span;
}
```

# Generated properties

Обычно чтобы достать обновленные данные из DB нужно сделать `refresh`. `@Generated` позволяет Hibernate реагировать на изменение данных самой DB и автоматически обновлять эти данные в Entity. Наприме в случае когда данные изменены через SQL trigger.

- **GenerationTime** - указывает когда Hibernate должен автоматически обновлять данные
  - `NEVER` (default)
  - `INSERT` - только для insert, например для `creationTimestamp` своства
  - `ALWAYS` - для insert и update

```java
@Entity(name = "Person")
public static class Person {
    // поля firstName, middleNameN, ... properties here
    // ...
    @Generated( value = GenerationTime.ALWAYS )
    @Column(columnDefinition =
        "AS CONCAT(" +
        "    COALESCE(firstName, ''), " +
        "    COALESCE(' ' + middleName1, ''), " +
        "    COALESCE(' ' + middleName2, ''), " +
        "    COALESCE(' ' + middleName3, ''), " +
        "    COALESCE(' ' + middleName4, ''), " +
        "    COALESCE(' ' + middleName5, ''), " +
        "    COALESCE(' ' + lastName, '') " +
        ")")
    private String fullName;
}
```

# `@GeneratorType` annotation

Анотация позволяет задать свой генератор значений полей.

**Для чего:** Например в поле `updatedBy` делать `set()` текущего вошедшего User, доставая этот объект например из сессии Spring Security.

```java
// создаем
public static class LoggedUserGenerator implements ValueGenerator<MyType> {
    public MyType generateValue(Session session, Object owner){
        // return my-value;
    }
}

// используем
@Entity(name = "Person")
public static class Person {
    @GeneratorType(type = LoggedUserGenerator.class, when = GenerationTime.ALWAYS)
    private MyType updatedBy;
}
```

# Сложные структуры

## `@Embeddable`

Это class который не используется сам по себе, используется только совместно с Entity. Служит для выноса общих атрибутов нескольких классов в отдельный объект.

Если `@Embeddable` принадлежит нескольким Entity, то у каждого Entity будут свои копии этого `@Embeddable`.  
Указывать `@Embedded` не обязательно.

`@Parent` можно получить ссылку на родителя для `@Embeddable` из `@Embeddable`.

- **Что может в JPA:**
  - содержать базовые типы
  - содержать другой `@Embeddable`
  - иметь связи с другими Entity или коллекциями Entity, если он не primary key ли ключ map'ы
  - быть Collection, быть key или value в Map: `Map<String, MyEmbeddable> m;`
- **Что обязан в JPA:**
  - быть отмечен @Embeddable
  - иметь только такие же типы атрибутов как Entity, но без primary key

```java
    @Entity
    public class Person {
        @Embedded public Address address;
        public Address address2;
    }

    @Embeddable
    public class Address {
        // доступ к полю person.address.owner == person
        @Parent public Person owner;

        @Column(name = "publisher_name")
        private String name;
    }
```

## `@ElementCollection`

`@ElementCollection` для встраивание non-entity коллекций (`@OneToMany` для entity), имеет unidirectional связь. На самом деле генерируется отдельная таблица для коллекции с внешним ключем к ней. Не имеет отдельного lifecycle.

**Note из документации:** Set эффективен, ordered List менее эффективен, Bag (unordered Lists) самый не эффективный.

`@CollectionOfElements` это старый проприетарный аналог для `@ElementCollection`.

Есть мнение, что с @ElementCollection лучше хранить базовые типы и их оболочки: Integer, String etc. Вместо использования List<String> или Set<String>.

```java
class User {
    String name;
    @ElementCollection //эта
    Set<Address> addresses = new HashSet<>();
}

//как сделать во встраиваемой коллекции поле id, точнее таблице для неё (по умолчанию его там нет)
//@CollectionId - аннотация Hibernate
class User {
    String name;

    @ElementCollection
    @CollectionId //теперь таблица с коллекцией будет иметь столбец id
    private Collection<Address> address = new ArrayList<>();
}

//задаем столбцу первичного id красивое имя (аннотациями hibernate)
class User {
    String name;

    @ElementCollection
    @GenericGenerator(name = "hilo-gen", strategy = "hilo") //hilo - это тип hibernate
    @CollectionId(
        columns = {
            @Column(name = "ADDRESS_ID"
        },
        generator = "hilo-gen",
        type = @Type(type = "long")
    )
    private Collection<Address> address = new ArrayList<>();
}

// установка имен табл. и столбца
@ElementCollection
@CollectionTable(
    name = "book_author",
    joinColumns = @JoinColumn(name = "book_id")
)
private List<Author> authors = new ArrayList<>();

// установка имен табл. и столбца
@Temporal(TemporalType.TIMESTAMP)
@ElementCollection
@CollectionTable(name = "phone_register")
@Column(name = "since")
private Map<Phone, Date> phoneRegister = new HashMap<>();
```

## Типы связей Entity

- Существуют следующие четыре типа связей
  1. **OneToOne** (связь один к одному, то есть один объект Entity может связан не больше чем с один объектом другого Entity ),
  2. **OneToMany** (связь один ко многим, один объект Entity может быть связан с целой коллекцией других Entity),
  3. **ManyToOne** (связь многие к одному, обратная связь для OneToMany),
  4. **ManyToMany** (связь многие ко многим)
  5. **@ElementCollection** - выделяют в unidirectional связь
- Каждую из которых можно разделить ещё на два вида:
  1. Bidirectional - с двух сторон
  2. Unidirectional - с одной, Entity доступен только с одной стороны связаного Entity

**Bidirectional** — ссылка на связь устанавливается у всех Entity, то есть в случае OneToOne A-B в Entity A есть ссылка на Entity B, в Entity B есть ссылка на Entity A, Entity A считается владельцем этой связи (это важно для случаев каскадного удаления данных, тогда при удалении A также будет удалено B, но не наоборот).

**unidirectional**- ссылка на связь устанавливается только с одной стороны, то есть в случае OneToOne A-B только у Entity A будет ссылка на Entity B, у Entity B ссылки на A не будет. 

### Примеры типов связей Entity

## Mapped Superclass

**Mapped Superclass** это класс от которого наследуются Entity, он может содержать анотации JPA, однако сам такой класс не является Entity, ему не обязательно выполнять все требования установленные для Entity (например, он может не содержать первичного ключа). Такой класс не может использоваться в операциях EntityManager или Query. Такой класс должен быть отмечен аннотацией MappedSuperclass или соответственно описан в xml файле.

```java
//просто предок @Entity который нельзя использовать, может не иметь @Id
@MappedSuperclass
class User {}

@Entity
class MyUser extends User {}
```

## Inheritance Mapping Strategies

https://www.baeldung.com/hibernate-inheritance

- Стратегии - как наследуемые классы Entity будут связаны друг с другом в DB.
  - **MappedSuperclass** – the parent classes, can’t be entities
  - **single table strategy** (default) - все наследники в 1ой табл. **Плюс подхода:** скорость, **минус:** расход памяти на пустые столбцы всех наследников (даже если записи не существует пустой столбец для наследника будет существовать). Для наследованных табл. имя их классов хранится в столбце `DTYPE`.
  - **joined strategy** - своя табл на каждого наследника, id и общие для наследников поля будут в общей для всех наследников табл. Нумерация id будет общая. **Плюс** экономия памяти, **минус** скорость (от join всех табл.)
  - **tabel per class** - тоже что и **joined strategy**, только вместо табл. с общими полями будут связи с табл.-предком. **Плюс** еще большая экономия памяти. **Минус** плохой polymorphic relationships и много отдельных sql запросов или использования UNION.

**Особенности:**

* **single table** - быстрее всего, можно использовать **polymorphic queries**, в наследниках нельзя использовать **not null constraints** для столбцов. Это риск data **inconsistencies.** Предок не обязан иметь id.
* **joined** - рекомендуется, если нужно использовать **polymorphic queries**
* **tabel per class** - **polymorphic queries** для нее очень сложные (затратные) и поэтому нужно их избегать. Предок обязан иметь id. В этом случае super class тоже Entity.

**polymorphic queries** - это когда начитываем класс наследник Entity и Hibernate автоматически отдает нам всех Entity наследников.

Через предка `@MappedSuperclass` от которого наследуются Entity (стратегиями наследовния) можно начинать всех наследников (sub-class entities) через **polymorphic queries**. Даже через наследуемые **не Entity** классы и interfaces можно начинать наследников.

Если используемые в **polymorphic queries** классы не Entity, то в запросе нужно использовать fully qualified name (FQM, путь состоящий из названия пакета + имя класса), чтобы Hibernate мог их найти, т.к. класс не **managed.**

При этом начинатанные таблицы наследники будут автоматически join.

```java
// получим все записи таблиц наследников Person
assertThat(session.createQuery(
    "from com.baeldung.hibernate.pojo.inheritance.Person")
    .getResultList())
    .hasSize(1);

// или
session.find(Person.class, 1);
```

**Запретить Polymorphic Queries** можно указав параметр **@Polymorphism(type = PolymorphismType.EXPLICIT):**

```java
@Entity
@Polymorphism(type = PolymorphismType.EXPLICIT)
public class Bag implements Item { ...}
```

**Меняем имя столбца `DTYPE` через `@DiscriminatorColumn`:**

```java
// задаем имя вместо DTYPE для всех наследников этого класса
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "VEHICLE_TYPE", 
    discripinatorType = DiscripinatorType.STRING //тип столбца DTYPE
    //discripinatorType = DiscripinatorType.INTEGER
    //discripinatorType = DiscripinatorType.CHAR
)
class Vehicle {
    int id;
    String name;
}

// можно установаить в столбце DTYPE другое значение только для одного наследника
@Entity
@DiscriminatorValue("Bike")
class TwoWheelerVehicle extends Vehicle {
    private String SteeringWheel;
}
```

# Fetching (Стратегии загрузки коллекций в Hibernate)

Ссылки:

* https://docs.jboss.org/hibernate/orm/current/userguide/html_single/chapters/fetching/Fetching.html
* https://dou.ua/lenta/articles/jpa-fetch-types/
* https://dou.ua/lenta/articles/hibernate-fetch-types/

## Типы fetch

1. **FetchType.EAGER** — загружать коллекцию дочерних объектов сразу же, при загрузке родительских объектов.
2. **FetchType.LAZY** — загружать коллекцию дочерних объектов при первом обращении к ней (вызове get) — так называемая отложенная загрузка

## fetching стратегии

1. **SELECT** - загружает связанные **коллекции отдельными запросами** (целые коллекции, а не записи каждой из них)
2. **JOIN** - загружать все связанные коллекции **одним запросом используя LEFT JOIN**

## N + 1 selects problem

Если есть OneToMany связь между табл. A и B. И пользователь выберет все записи из A. То на выборку из A получится **1 запрос** в DB, и на выборку всех записей из B, которые привязаны к выбранным записям из A потратиться **N запросов**, по 1му на каждую сущность из B. Т.е. запросы в связанную табл. B не будут объединены в 1ин запрос. Получается **N + 1** запрос на выборку вместо 1го запроса.

**Как избежать:**

1. Не использовать **eager**, считается что eager почти не имеет реального применения (использовать lazy, lazy стоит по умолчанию)
2. Использовать **Query**, написанный вручную
3. Использовать **Join Fetch** / **Entity Graphs**. Для join fetch нужно **поставить eager** (логично, иначе запросы будут только по мере надобности)

## JOIN FETCH (решение проблемы N + 1 selects)

На самом деле для **JOIN FETCH** используется **LEFT JOIN**.

> **JPQL и JPA Criteria** по запросу **fetch join** вернут декартово произведение (cartesian product). **DISTINCT** может убрать дубли строк в cartesian product.

<sub>**Прим.** декартово произведение это **не тоже** что и **full outer join**. Возможно тут имеется ввиду что-то другое? (уточнить это). В документации про JOIN сказано: Inherently an EAGER style of fetching. The data to be fetched is obtained through the use of an **SQL outer join**.</sub>

> **Только одна связанная коллекция**, которая загружается стратегией JOIN может быть типа **java.util.List**, остальные коллекции должны быть типа **java.util.Set**. Иначе, будет выброшено исключение: `HibernateException: cannot simultaneously fetch multiple bags`

> При использовании стратегии загрузки JOIN методы **setMaxResults** и **setFirstResult** не добавят необходимых условий в сгенерированный SQL запрос. Результат SQL запроса будет содержать все строки без ограничения и смещения согласно firstResult/maxResults. Ограничение количества и смешение строк будет **применено в памяти** (но не к запросам). Также будет выведено предупреждение:
> 
> `WARN HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!`  
> 
> Если фильтрация в памяти вызывает проблемы, не используйте **setFirsResult**, **setMaxResults** и **getSingleResult** со стратегией загрузки JOIN.

В JPQL и JPA Criteria стратегия загрузки по умолчанию: **SELECT**

Пример:

```sql
-- Загрузит только Employee,
-- department будет загружен при первом запросе
FROM Employee emp
JOIN emp.department dep

-- Загрузит Employee и department сразу одим запросом
FROM Employee emp
JOIN FETCH emp.department dep
```

**1. JPQL пример**  
**Возможная проблема:** результат запроса будет содержать слишком большое количество записей.  
**Решение:** использовать 2ва запроса вместо одного <sub>(нет, ниже пример не об этом)</sub>

```java
// JOIN FETCH
List<Book> books = em.createQuery("select b from Book b left join fetch b.authors order by b.publicationDate")
    .getResultList();
assertEquals(3, books.size())

// JOIN FETCH + distinct (избавляемся от дублей)
List<Book> books = em.createQuery("select distinct b from Book b left join fetch b.authors order by b.publicationDate")
    .getResultList();
assertEquals(2, books.size());
```

**2. JPA Criteria пример**

```java
// JOIN FETCH
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Book> cq = cb.createQuery(Book.class);
Root<Book> book = cq.from(Book.class);
book.fetch(Book_.authors, JoinType.LEFT);
cq.orderBy(cb.asc(book.get(Book_.publicationDate)));
TypedQuery<Book> q = em.createQuery(cq);
List<Book> books = q.getResultList();      
assertEquals(3, books.size());

// JOIN FETCH + distinct (избавляемся от дублей)
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Book> cq = cb.createQuery(Book.class);
Root<Book> book = cq.from(Book.class);
cq.distinct(true);
book.fetch(Book_.authors, JoinType.LEFT);
cq.orderBy(cb.asc(book.get(Book_.publicationDate)));
TypedQuery<Book> q = em.createQuery(cq);
List<Book> books = q.getResultList();
assertEquals(2, books.size());
```

## List vs Set

Hibernate **не может** загрузить **несколько разных List** из одной Entity в одном запросе. Нужно или использовать несколько запросов в каждом из которых загрузить свой List **или** использовать Set.

Для использования Set нужно реализовать **equals** and **hashCode**, т.к. они используются в сравнении HashSet (сравнение по хэшам).

Для **Set** (unordered) **нельзя** использовать `@OrderBy` в отличии от **List** (ordered). Но можно использовать `@Sort` **вместе с** `SortedSet` (прим. есть еще `@SortComparator` для передачи `Comparator` и `@SortNatural` для использования с `Comparable`):

```java
@OneToMany
@OrderBy("lastname ASC")
public List<Rating> ratings;

@OneToMany
@Sort(type = SortType.COMPARATOR, comparator = TicketComparator.class)
public Set<Rating> ratings = new SortedSet<>();
```

## Entity Graph (решение проблемы N + 1 selects)

**Суть:** указываем какие из связанных сущностей (табл.) делать JOIN при запросе.

**Пример**

```java
// 1.
EntityGraph<Book> fetchAuthors = em.createEntityGraph(Book.class);
fetchAuthors.addSubgraph(Book_.authors);
List<Book> books = em.createQuery("select b from Book b order by b.publicationDate")
    .setHint("javax.persistence.fetchgraph", fetchAuthors)
    .getResultList();        
assertEquals(3, books.size());

// 2.
@Entity(name = "Project")
@NamedEntityGraph(name = "project.employees",
    attributeNodes = @NamedAttributeNode(
        value = "employees",
        subgraph = "project.employees.department"
    ),
    subgraphs = @NamedSubgraph(
        name = "project.employees.department",
        attributeNodes = @NamedAttributeNode( "department" )
    )
)
public static class Project {
    @Id
    private Long id;

    @ManyToMany
    private List<Employee> employees = new ArrayList<>();
}
```

## Hibernate fetch стратегии, отличие от JPA: `FetchMode`, `SUBSELECT`, `@BatchSize`

**Hibernate fetch vs JPA fetch:**  

1. **Hibernate Criteria** (речь не о HQL!) по умолчанию использует `FetchMode.JOIN` для `EAGER` и `FetchMode.SELECT` для `LAZY`
2. **HQL** при `@Fetch(FetchMode.JOIN)` игнорирует **LAZY** и **EAGER** и работает как `FetchMode.SELECT` в режиме **EAGER**. **Т.е.** HQL игнорирует `FetchMode.JOIN` и нужно использовать **join fetch** в **самом** HQL запросе
3. **Hibernate Criteria** при `@Fetch(FetchMode.JOIN)` работает в **EAGER**. (не точно, ПРОВЕРИТЬ!)

```java
// тут на самом деле работает как FetchMode.SELECT
// хотя помечено как FetchMode.JOIN
// чтобы работало как JOIN нужно использовать join fetch в самом запросе
List books = getCurrentSession().createQuery("select b from BookFetchModeJoin b").list();
assertEquals(4, books.size());

// вот так будет join fetch, поведение как FetchMode.JOIN
List books = getCurrentSession().createQuery("select b from BookFetchModeJoin b join fetch b.authors a").list(); 
```

### `@BatchSize` c `FetchMode.SELECT`

**@BatchSize** указывает сколько запросов использовать для загрузки связанных коллекций **каждой полученной записи** корнегово запроса.  
Т.е. если в табл. **A** 2 записи, и к каждой такой записи привязано по 4 записи из табл. **B**, то при `@BatchSize(size = 2)` будет `2 * 2 + 1 == 5` запросов вместо `2 * 4 + 1 == 9` запросов без `@BatchSize`.

Внутри **@BatchSize** использует **IN-restriction** (из SQL).

Может быть **EAGER** (2ой запрос выполнится сразу) или **LAZY** (второй запрос выполнится, когда данные понадобятся)

```java
@Entity
public class BookBatchSize extends AbstractBook {
    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 2)
    private List<Author> authors = new ArrayList<>();
}
```

### `FetchMode.SUBSELECT`

**FetchMode.SUBSELECT** использует 2 запроса, один для загрузки Entity и
второ в виде подзапроса для загрузки всех связанных записей.

Может быть **EAGER** (2ой запрос выполнится сразу) или **LAZY** (второй запрос выполнится, когда данные понадобятся)

```java
@Entity
public class BookFetchModeSubselect extends AbstractBook {

    @ManyToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    private List<Author> authors = new ArrayList<>();

    @ManyToMany
    @Fetch(FetchMode.SUBSELECT)
    private List<Category> categories = new ArrayList<>();

    /*...*/
}
```

## @LazyCollection

**@LazyCollection** - нету в JPA, из Hibernate ORM. Указывает как загружать связанную коллекцию.
**Значения:** (`TRUE` и `FALSE` - deprecated и нужно использовать значения из `FetchType`)

* **FetchType.LAZY** (`TRUE`) - как lazy
* **FetchType.EAGER** (`FALSE`) - как eager
* **EXTRA** - нету в JPA, не загружает Collection целиком даже когда к ней было первое обращение, каждый элемент коллекции загружается отдельным запросом. Работает только с **ordered collections** (`List` + `@OrderColumn` или `Map`), для `Bag` (unordered `List`) ведет себя как `FetchType.LAZY`
  
  ```java
  @Entity
  public static class Department {
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
    @OrderColumn(name = "order_id")
    @LazyCollection( LazyCollectionOption.EXTRA )
    private List<Employee> employees = new ArrayList<>();
  }
  ```

# Mapping

## Mapping annotations

Генерированные имена могут быть некрасивыми, чтобы использовать свои нужно указать их явно:

```java
@Table(name = "my_name") class MyTable {}
```

```java
@ManyToOne
@JoinColumn(name = "item_type_id")
```

## Типы Collection в связях

- Источник:
  - http://viralpatel.net/blogs/hibernate-one-to-many-xml-mapping-tutorial/
  - https://stackoverflow.com/questions/13812283/difference-between-set-and-bag-in-hibernate

- **Bag** - концепция, НЕУПОРЯДОЧЕННАЯ коллекция, которая может содержать дубликаты.
  - В Java (JPA) ее аналог это List
- **Set** - концепция, НЕУПОРЯДОЧЕННАЯ коллекция (но МОЖЕТ быть отсортирована - через спец. параметр), тоже что и Bag, но хранить может только уникальные объекты. При попытке добавления дубликата, он заменит совпадающий объект.
  - В Java (JPA) ее аналог это Set

Map - тоже можно использовать (вот тут неточно!)

```java
@OneToMany()
@JoinColumn(name="A_ID", insertable=false, updatable=false)
@MapKeyColumn(name="B_ID") // вот тут в JPA в отличии от Hibernate немного по другому, посмотреть аннотацию @MapKey
private Map map = new HashMap();
```

## `AttributeOverride`

- `@AttributeOverride` - меняем название столбцов в таблице, если встраиваем одинаковые @Embedded объекты. Чтобы не было конфликта одинаковых имен столбцов. Так можно перекрывать и атрибуты от суперкласса @MappedSuperclass

- `@AttributeOverrides` - массив таких аннотаций, пара для каждого столбца
  
  ```java
  class User {
    String name;
  
    @Embedded
    @AttributeOverrides ({
        @AttributeOverride (
            name = "street", //старое имя столбца
            column = @Column(name = "homeStreet") //новое имя столбца
        ),            
        @AttributeOverride (
            name = "city",
            column = @Column(name = "homeCity")
        ),            
        @AttributeOverride (
            name = "phone",
            column = @Column(name = "homePhone")
        )
    })
    Address homeAdress;
  
    @Embedded
    Address workAdress;
  }
  ```
  
# Persistence Context

## Hibernate Entity Lifecycle

Методы `Session` меняют состояние связей Entity с `persistence context`.

Говорят что entity ассоциирован с `persistence context` или ассоциирован с `Session`. Когда он связан с сессией и его изменения синхронизируются с DB.

`persistence context` - мето где `data` конвертируется в `entity`. Это implementation of **Unit of Work** паттерна, отслеживает загруженные в него данные и синхронизирует с DB. `Session` это implementation of `persistence context`.  
**lifecycle** - это то как entity связан с `persistence context`.  
Unit of Work - еше называют "business transaction".

- **Состояния Entity:**
  - **transient** - id обычно не назначен, объект ВНЕ (ДО открытия) сессии (объект никогда не был связан с сессией). В transient переходят и объекты после удаления delete() внутри сессии (потому что они уже не в БД)
  - managed, or **persistent** - id назначен, связан с сессией (ВНУТРИ неё), в том числе если он взят из базы через get().
    - **Переходит в этот state после методов:** save()/persist(), saveOrUpdate(), update()/merge(), lock(), get(), load()
  - **detached** - id назначен, объект ПОСЛЕ session.close() или evict() (hibernate больше не связан с сессией)
  - **removed** - id назначен, связан с сессией, но в очереди на удаление из DB

```java
session.openTransaction();
session.save(user); // user перешел в состояние persistent и его изменения отслеживаются
session.setName("my"); //если внутри сессии, автоматически выполнится UPDATE
session.setName("my 2"); // вызван будет только "my 2" UPDATE, то есть повторно не вызывается (кэширование L1)
session.getTransaction().commit();
```

## Методы Session (включая JPA vs Hibernate методы)

**JPA vs Hibernate методы** - изначально в Hibernate и JPA были свои методы. Затем в Hibernate добавили методы JPA, для большей совместимости. Некоторые методы похожи по функционалу, но их работа в может отличаться.

- **save vs persist**
  - **save** генерирует и возвращает id, **persist** ничего не возвращает и может создать id когда захочет до flush(). При типе генерации id `Identity`, вне transaction или с `Flush.MANUAL` для **persist** Hibernate задержит выполнение insert и создаст временный id, но для **save** выполнит insert сразу и получит id из DB.
- **update vs merge**
- **saveOrUpdate**
- **replicate** — преобразует объект из detached в persistent, при этом у объекта обязательно должен быть заранее установлен Id. Данный метод предназначен для сохранения в БД объекта с заданным Id, чего не позволяют сделать persist() и merge(). Если объект с данным Id уже существует в БД, то поведение определяется согласно правилу из перечисления `org.hibernate.ReplicationMode`: 
  - `ReplicationMode.IGNORE` — ничего не меняется в базе.
  - `ReplicationMode.OVERWRITE` — объект сохраняется в базу вместо существующего.
  - `ReplicationMode.LATEST_VERSION` — в базе сохраняется объект с последней версией.
  - `ReplicationMode.EXCEPTION` — генерирует исключение.
- **merge** — преобразует объект из transient или detached в persistent. Если из transient, то работает аналогично persist() (генерирует для объекта новый Id, даже если он задан), если из detached — загружает объект из БД, присоединяет к сессии, а при сохранении выполняет запрос update
- **refresh** — обновляет detached-объект, выполнив select к БД, и преобразует его в persistent
- **persist** — преобразует объект из transient в persistent, то есть присоединяет к сессии и сохраняет в БД. Однако, если мы присвоим значение полю Id объекта, то получим PersistentObjectException — Hibernate посчитает, что объект detached, т. е. существует в БД. При сохранении метод persist() сразу выполняет insert, не делая select.
- **delete** - удаляет объект из БД, иными словами, преобразует persistent в transient. Object может быть в любом статусе, главное, чтобы был установлен Id.
- **save** — сохраняет объект в БД, генерируя новый Id, даже если он установлен. Object может быть в статусе transient или detached
- **update** — обновляет объект в БД, преобразуя его в persistent (Object в статусе detached)
- **refresh**
- **get** - получает из БД объект класса-сущности с определённым Id в статусе persistent
- **scroll**
- **list**
- **iterate**
- **load**
- **evict**
- **find**

# Bootstrap

Тут будут краткие сведения о том как запускается и конфигурируется Hibernate и о классе ServiceRegistry

http://docs.jboss.org/hibernate/orm/current/userguide/html_single/Hibernate_User_Guide.html#bootstrap

# Exceptions и их обработка

**Коротко.** Hibernate оборачивает Exceptions из JDBC в свои Exceptions. Spring в свою очередь оборачивает в свои Exceptions. При этом аннотация `@Repository` добавляет доп. обработку таким исключениям.

***

**1. В Spring для обработки ошибок транзакций.**  **1)** создать спец. контроллер и **2)** указать какое исключение обрабатывает метод:

1. Выбрасываемое исключение **должно быть наследником `RuntimeException` чтобы прервать программу**.
2. Для обработки создать спец. контроллер и указать какое исключение обрабатывает метод (указать тип исключения)ю Доп. инфа о обработчиках ошибок в Spring MVC: см. [@ControllerAdvice, @ExceptionHandler, HandlerExceptionResolver](https://www.journaldev.com/2651/spring-mvc-exception-handling-controlleradvice-exceptionhandler-handlerexceptionresolver)

```java
class RoleNotExistenceExeption extends RuntimeException {}

// этот метод выполнится если внутри @Transaction будет указанное исключение
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(RoleNotExistenceExeption.class)
    public ModelAndView roleNotFoundPage(RoleNotExistenceExeption ex) {
        ModelAndView model = new ModelAndView();
        model.addObject("msg", ex.getMessage());
        model.setViewName("error/error");
        return model;
    }

    //@ExceptionHandler(RuntimeException.class)
    public ModelAndView roleNotFoundPage(RuntimeException ex) {
        ModelAndView model = new ModelAndView();
        model.setViewName("error/some");
        return model;
    }
}
```

***

**2. Поведение при Exception внутри транзакции:**

- **rollbackFor** указывает исключения, при выбросе которых должен быть произведён откат транзакции
- **noRollbackFor**, указывающий, что все исключения, кроме перечисленных, приводят к откату транзакции

```java
@Transactional(propagation = Propagation.REQUIRED, rollbackFor = {ConstraintViolationException.class})
public Long saveTask(Long userId) {
    Session session = sessionFactory.getCurrentSession();
    User user = userDao.getUserByLogin("user1");
    Tasks task = new Tasks();
    task.setName("Задача 1");
    ...
    task.setUser(user);
    session.saveOrUpdate(task);
    return task.getTaskId();
}
```

- При откате транзакции не происходит откат состояния (значение полей) в Entity учавствующих в транзакции? (**проверить**)

# Настройка Hibernate и Spring

## Как Transaction работает внутри

**Кратко.** Spring не управляет транзакциями. Он для описания правил, все управление делегируется к DB (возможно в случае Hibernate делегируется к Hibernate?). Spring внутри запускает AOP и создает proxy бинов для конфигурации. Реализует различные интерфейсы из JTA и свои собственные для декларативного управления. Активно используется класс бин TransactionInterceptor и регистрирует context. Создает TransactionManager, есть различные типы TransactionManager (можно создавать свои с различными правилами кэширования и прочими). SpringTransactionAnnotationParser парсит аннотации транзакций (можно написать свой). Класс PlatformTransactionManager тоже ктивно используется.

## Старый вариант с HibernateUtil

## Вариант с ручным созданием разных Bean в `@Configuration` для Hibernate + Spring

- **Чтобы работали транзакции (настройка транзакций для Spring + Hibernate):**
  1. Создать `DataSource` для JDBC и указываем параметры подключения к DB
  2. Создаем менеджер транзакций (источник бина). **Можно указать несколько и тогда нужно каждой задать имя.** Если transaction manager только один, то имя можно не указывать, Spring сделает Autowired по типу transaction manager. В transaction manager передаем ссылку на `DataSource`
  3. Проставить аннотацию `@Transaction` Можно указать `@Transaction(value = "MyTxName")` имя конкретного менеджера, если их несколько.
  4. Использовать аннотацию `@EnableTransactionManagement` в `@Configuration` чтобы запустить работу с транзакциями (это аналог `<tx:annotation-driven/>` из xml конфигурации)

## Конфигурация в Spring Boot через файл настроек
тут будет описание

# Misc

## Кавычки в именах Entity, использование зарезервированных имен

Нельзя в использовать зарезервированные слова вроде **Order**, будет ошибка. Но можно включить обрамление имен в кавычки через глобальные настройки или указать имя в `@Column(name = "\"Order\"")`

## Тип в котором хранить деньги

**BigDecimal** преобразуется в decimal(19,2) в SQL

```java
@Entity
class Item {
    //преобразуется в decimal(19,2) - в SQL
    private BigDecimal cost;
}
```

## Особенности работы с БД MySQL

Согласно этой статье https://www.thoughts-on-java.org/5-things-you-need-to-know-when-using-hibernate-with-mysql/

1. **По умолчанию hibernate 5 для mysql выбирает стратегию - GenerationType.TABLE**, но она медленная (до этого выбирал GenerationType.IDENTITY и она ОК). Поэтому для скорости НУЖНО указать вручную:
   
   ```java
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
   @GenericGenerator(name = "native", strategy = "native")
   @Column(name = "id", updatable = false, nullable = false)
   private Long id;
   ```

**generator = "native"** - заставляет выбирать стратегию БД по умолчанию (в данном случае GenerationType.IDENTITY)

При этом самой быстрой стратегией считается GenerationType.SEQUENCE, но ее нету в MySQL

**2. Для работы с специфичными для БД типов данных нужно реализовать AttributeConverter**

**3. Диалекты MySQL**  
InnoDB - (рекомендуется) движок поддерживает транзакции и lock on row
MyISAM - не поддерживает транзакции стоит по умолчанию (видимо при использовании не сигнализирует через Hibernate о том что он не поддерживает транзакции и др. технологии)

различия в деталях: https://stackoverflow.com/questions/15678406/when-to-use-myisam-and-innodb

- (Deprecated) MySQLInnoDBDialect - добавляет type=InnoDB к таблицам (использует движок InnoDB). И еще зачем-то добавляет cascade delete по умолчанию
  - MySQLDialect - использует движок по умолчанию MyISAM (видимо обертка для MySQLMyISAMDialect)
  - MySQLMyISAMDialect - видимо диалект для движка MyISAM
  - MySQL57InnoDBDialect и MySQL57Dialect - последние на момент написания версии диалекта, наследующие всех предков

SpatialDialect - группа деалектов для работа с геометрическими типами данных в БД: точки, области и т.д.

ТАКЖЕ!!!
    В Hibernate 5.2 класс MySQL57InnoDBDialect - Deprecated

ТЕПЕРЬ!
    Чтобы выбрать dialect НУЖНО использовать
        1. org.hibernate.dialect.MySQL57Dialect
        2. hibernate.dialect.storage_engine=innodb
источник: https://stackoverflow.com/a/46746865

4. **Если нужно изменить параметры табл, такие как: encoding set, collation и др.**, то - нужно переопределить MySQL57InnoDBDialect и определить метод который создает конфигурационную строку при создании табл.

# Про Hibernate Criteria API vs JPA Criteria API

Есть 2ва варианта Criteria API в Hibernate, **старый** (Hibernate Criteria API) и **новый** (JPA Criteria API). Новый перинят из JPA и считается основным, старый доступен в Hibernate в виде расширения.

Начиная с Hibernate 5.2 спецификация Hibernate Criteria API уже **deprecated**, рекомендуется использовать реализацию из JPA: JPA Criteria API. Версия API от Hibernate будет портирована как extensions to the JPA и доступна в пакете `Criteria.` ( https://stackoverflow.com/a/31202152 )

**Ссылки:**

* [Официальная документация](https://docs.jboss.org/hibernate/entitymanager/3.5/reference/en/html/querycriteria.html)
* [How to query by entity type using JPA Criteria API](https://vladmihalcea.com/query-entity-type-jpa-criteria-api/)
* [Отличие jpa от hibernate для criteria](https://stackoverflow.com/questions/25536868/criteria-distinct-root-entity-vs-projections-distinct)

# JPA Criteria API

## Базовые

* **Root** - корень выбранной табл. или группы табл., если запрос полиморфный
* **CriteriaBuilder** - используется чтобы добавлять условия к исходному Predicate (объединять несколько Predicate через `and()` например)
* **CriteriaQuery** - делает запросы с применением операторов distinct, subquery, having, groupBy etc к указанному Root: ``
* **Predicate** - условие использующиееся в запросе на основе которого строится результат
* Criteria
* Query
* TypedQuery
* **Subquery** - объект для формирования подзапроса, ему передается class другого типа, того чьей сущности принадлежит подзапрос
* Example
* **ParameterExpression** - обертка для разных типов данных, которую потом можно вложить в условие запроса: `q.select(c).where(cb.gt(c.get("population"), cb.parameter(Integer.class)));`

комбинация нескольких Predicate: https://stackoverflow.com/questions/43322504/criteria-query-combine-and-predicates-and-or-predicates-in-where-method

join + predicate (join используя predicate): https://stackoverflow.com/a/42222429

## Как работать с этим в целом (примеры)

1. Получаем **CriteriaBuilder** объект из **EntityManager** или **Session** (**не** создаем через `build()`, а получаем через `getCriteriaBuilder()`)
2. Создаем (как `build()`) объект **CriteriaQuery** из **CriteriaBuilder** через метод `criteriaBuilder.createQuery(User.class);` с указанием в нем класса **Entity** табл. к которой будем делать запросы (или общего класса предка для нескольких **Entity**, тогда запросы будут полиморфными - сразу к нескольким **Entity** наследникам)

**Обычный**

```java
// получаем builder
CriteriaBuilder cb = entityManager.getCriteriaBuilder();

// создаем query, при этом можно указывать класс предка нескольких Entity,
// тогда работать будет с его наследниками (полиморфные запросы)
CriteriaQuery<User> criteriaQuery = cb.createQuery(User.class);

// получаем Root
Root<Topic> root = criteriaQuery.from(Topic.class);

// условия запроса
criteriaQuery.where(
    builder.equal(root.get("owner"), "Vlad")
);

// результат
List<Topic> topics = entityManager
    .createQuery(criteria)
    .getResultList();
```

**Пример Subclass filtering**

```java
// sub class от основного класса (Subclass filtering)

// храним в отдельной переменной
Class<? extends Topic> sublcass = Post.class;

//...

// условие запроса
criteria.where(
    builder.and(
        builder.equal(root.get("owner"), "Vlad"),
        builder.equal(root.type(), sublcass) // проверяем чтобы тип был как у subclass
    )
);
```

**Пример ParameterExpression**

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<Country> q = cb.createQuery(Country.class);
Root<Country> c = q.from(Country.class);
ParameterExpression<Integer> p = cb.parameter(Integer.class); // получаем parameter
q.select(c).where(cb.gt(c.get("population"), p)); // устаналиваем parameter в условие
```

**Пример subquery**

```java
String projectName = "project1";
List<Employee> result = employeeRepository.findAll(
    new Specification<Employee>() {
        @Override
        public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            Subquery<Employee> sq = query.subquery(Employee.class);
            Root<Project> project = sq.from(Project.class);
            Join<Project, Employee> sqEmp = project.join("employees");
            sq.select(sqEmp).where(cb.equal(project.get("name"),
                    cb.parameter(String.class, projectName)));
            return cb.in(root).value(sq);
        }
    }
);
```

аналогично sql запросу

```sql
SELECT e FROM Employee e WHERE e IN (
    SELECT emp FROM Project p JOIN p.employees emp WHERE p.name = :projectName
)
```

**Пример Example**

```java
// тут будет пример
```

**Пример Join**

```java
CriteriaBuilder cb = em.getCriteriaBuilder();
CriteriaQuery<BookWithAuthorNames> cq = cb
        .createQuery(BookWithAuthorNames.class);
// Define FROM clause
Root<Book> root = cq.from(Book.class);
Join<Book, Author> author = root.join(Book_.author);

// Define DTO projection
cq.select(cb.construct(
        BookWithAuthorNames.class,
        root.get(Book_.id),
        root.get(Book_.title),
        root.get(Book_.price),
        cb.concat(author.get(Author_.firstName), ' ',
                author.get(Author_.lastName))));

// Define WHERE clause
ParameterExpression<String> paramTitle = cb.parameter(String.class);
cq.where(cb.like(root.get(Book_.title), paramTitle));

// Execute query
TypedQuery<BookWithAuthorNames> q = em.createQuery(cq);
q.setParameter(paramTitle, "%Hibernate Tips%");
List<BookWithAuthorNames> books = q.getResultList();

for (BookWithAuthorNames b : books) {
    log.info(b);
}
```

# Старый Hibernate Criteria API

## Пример старого Hibernate Criteria API (чтобы отличать его на глаз)

```java
Criteria criteria = session.createCriteria(User.class);

criteria
    .add(Restriction.eq("name", "ivan"))
    .add(Restriction.or(Restriction.between(12, 13), Restriction.between(2, 6)))
    .add(Restriction.like("name", "%van"));

Criteria criteria = session.createCriteria(User.class)
    .setProjection(Projections.property("userId")); //берем только одно поле из запроса, тип String

Criteria criteria = session.createCriteria(User.class)
    .setProjection(Projections.max("userId")); //максимальное из свойства userId

Criteria criteria = session.createCriteria(User.class)
    .setProjection(Projections.count("userId")); //подсчитать userId
    .addOrder(Order.desc("userId")); //сортировать результат
```

Пример с **Example** объектом и Criteria API

```java
//работа с Example классом - как шаблон
User exampleUser = new User();
//exampleUser.setId(5); - установка id для Example ни на что не влияет
exampleUser.setName("ivan");
Example example = Example.create(exampleUser); //шаблон
Criteria criteria = session.createCriteria(User.class)
    .add(example); //вернет всех пользователей с именем ivan

//исключить запись с конкретным свойством из выборки
Example example = Example.create(exampleUser).excludeProperty("ivan_2");

//использование совпадение строки Like в шаблоне
exampleUser.setName("%van");

Example example = Example.create(exampleUser).enableLike("%van");
```

# Specification (и ее связь с Criteria API), использование с find методом из Spring Data JPA

**Specification** - это interface который получает как параметры **Root**, **CriteriaQuery**, **CriteriaBuilder** внутри метода реальзуется алгоритм Criteria API и метод возвращает predicate, который используется какой-либо функцией в запросе. Основное метод это **toPredicate()** и возвращает **Predicate**

Например в Spring можно передавать объект **Specification** в метод `find(Specification s)`. Т.е. сами методы `find(Predicate p)` принимают predicate, который возвращается методом `toPredicate()`

```java
public interface Specification<T> {
  Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
            CriteriaBuilder builder);
}
```

В **Spring Data JPA** нужно наследовать **Repository** интерфейс **JpaSpecificationExecutor** чтобы использовать **Specification** в методах `find()`

```java
public interface PersonRepository extends CrudRepository<Person, Long>, JpaSpecificationExecutor<Person> { }
```

На практике в коде приложения можно выделить отдельный package для **Specification** который использовать для реализации разных алгоритмов поиска для REST API разных сущностей.

**Полный пример Specification**

```java
public class AccessTemplateSearchSpecification implements Specification<AccessTemplate> {
    private final AccessTemplateSearchCriteria accessTemplateSearchCriteria;

    @Override
    public Predicate toPredicate(Root<AccessTemplate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.and();

        if (accessTemplateSearchCriteria == null) {
            return predicate;
        }
        if (StringUtils.hasText(accessTemplateSearchCriteria.getComment())) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get(AccessTemplate_.comment),
                            LikeQueryWrapperUtil.wrapForDb(accessTemplateSearchCriteria.getComment())));
        }
        if (accessTemplateSearchCriteria.getStartDate() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.greaterThanOrEqualTo(root.get(AccessTemplate_.startDate),
                            accessTemplateSearchCriteria.getStartDate()));
        }
        if (accessTemplateSearchCriteria.getEndDate() != null) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.lessThanOrEqualTo(root.get(AccessTemplate_.endDate),
                            accessTemplateSearchCriteria.getEndDate()));
        }

        return predicate;
    }
}
```

# specification-arg-resolver

**specification-arg-resolver** это неофициальная библиотека для описания **Specification** через аннотацию и альтернативное API.

```java
@RequestMapping("/customers")
public Object findByName(
        @And({
            @Spec(path="registrationDate", params="registeredBefore", spec=DateBefore.class),
            @Spec(path="lastName", spec=Like.class)}) Specification<Customer> customerSpec) {
    return customerRepo.findAll(customerSpec);
}
```

Подробнее [тут](https://blog.tratif.com/2017/11/23/effective-restful-search-api-in-spring/)

# Еще про транзакции (из блога Vlad Mihalcea)

## Блокировки и то как они относятся к транзакциям

В concurrency theory понятие lock это защита целостности (integrity) общих (shared) данных.

Приложение может делегировать работу с lock базе данных. Это упрощает приложение. DB может сама решать проблемы deadlock, находя такие проблемы и делая случайное отпускание (releasing) одной из блокировок (competing locks).

**implicit locking** (неявная блокировка) - спрятана за **transaction isolation level**, т.е. она включается неявно сама при установке isolation level. **isolation level** это предопределенные схемы блокировки (predefined locking scheme). Каждая isolation level защищает (preventing) от определенного набора аномалий целостности данных (integrity anomalies).

**explicit locking** (явная блокировка) - делится на **pessimistic** (physical) and **optimistic** (logical) locks.

**database locking** - это locks реализованные в DB, т.е. это **pessimistic locks**. Они подходят для групп операций с данными как целым (**batch processing systems**). Но в реальном приложении операция может распостранятся на несколько транзакция в DB (**multi-request web flow**). В случае если нужно за одну операцию выполнить несколько transaction в DB нужен logical (optimistic) lock. Например, само приложение может выполнить одну транзакцию и ожидать действий пользователя, чтобы выполнить уже другую с учетом первой транзакции, но с новыми данными - это называется **long conversations** (и так для множества транзакций и множества действий пользователя). **optimistic lock** выполняется на уровне приложения и ее рекомендуется сочетать с **repeatable reads** isolation level для реализации (implementing) **logical transactions** (логической транзакции) без потери производительности (в отличии от некоторых других подходов).

**Другими словами**, logical locks и logical transaction (логические блокировка и транзакция) выполняются самим приложением, потому что DB не может узнать когда они начинаются и заканчиваются, а не может она это узнать потому что пользователь может выполнить операции с данными в любой момент времени. Эти данные нужно будет изменить в DB, но наиболее эффективно менять все данные DB с которыми работал пользователь в один момент времени в конце всех операций. А не каждый раз когда пользователь что-то сделал с данными. Т.е. эффективнее одни или несколько транзакций, чем много транзакций на каждое действие пользователя. Поэтому приложения имеют специальный кэш операций, которые выполняет пользователь. Перед commit логической транзакции происходит очистка этого кэша операций от лишних операций, которые перекрываются другими операциями. На этом же этапе происходит оптимизация запросов. И уже после этого все операции пользователя объединенные в один запрос или в batch операций отправляются в DB и выполняются там в **Physical transaction**.

**Physical locks** (pessimistic locking) - делится на **shared** и **exclusive** locks. При этом они могут делать lock как на отдельных **rows** так и на целых **tables**. При этом в JPA можно сделать только **одну блокировку** за раз, если это невозможно выбросится **PersistenceException**.

Не совсем ясная инфа из статьи: **READ COMMITTED** использует query-level shared locks and exclusive locks for the current transaction modified data. **REPEATABLE READ** and **SERIALIZABLE** use transaction-level shared locks when reading and exclusive locks when writing. 

**shared lock** (read lock) - блокированные данные можно читать, но нельзя модифицировать

**exclusive lock** (write lock) - блокированные данные нельзя читать или модифицировать

**logical locks** (optimistic locking) - блокировка данных выполняется самим приложением, внутри кода различных библиотек и прочего. При этом **persistence providers** может (а может и не поддерживать? в оригинале неясно) поддерживать **optimistic locking** для entities у которых нет **@Version** поля, но **лучше** всегда указывать такой атрибут при работе с optimistic locking. В JPA для Entity с version атрибутом optimistic locking включена **by default**, но можно и явно указать блокировку в методах JPA для работы с Entity (пример ниже). В JPA при обнаружении конфликта выбрасывается OptimisticLockException и transaction помечается для rollback.

**Прим.** Не совсем по теме, но существует библиотека **com.vladmihalcea:db-util:0.0.1** у которой есть разные механизмы анализа SQL запросов и аннотация `@Retry` которая позволяет выполнить запросы повтора автоматически в случае `OptimisticLockException`

Для обработки **OptimisticLockException** в JPA рекомендуется получить Entity через reloading или refreshing, предпочтительно в новой transaction. И попытаться выполнить операцию снова.

**implicit locking** vs **Explicit locking** - для большинства приложений **implicit locking** (т.е. установка transaction isolation level) лучший выбор. Но иногда необходимо более fine grained (точное) управление блокировками. Большинство DB поддерживают **query-time exclusive locking directives** такие как **SELECT FOR UPDATE** и **SELECT FOR SHARE**. При этом можно использовать **более низкий уровень изоляции** транзакции (напр. READ COMMITTED) совместно с использованием **share** or **exclusive** locks.

**SELECT … FOR UPDATE** это exclusive lock, блокирует row таблицы. Применяется когда ... 

```sql
-- пример
-- все другие транзакции которые хотят сделать select for update будут блокированы
BEGIN;

SELECT *
  FROM player
 WHERE id = 42
   FOR UPDATE;

-- some kind of game logic here

UPDATE player
   SET score = 853
 WHERE id = 42;

COMMIT;
```

Несовсем ясная информация из статьи: Большинство реализаций **optimistic locking** проверяет только измененные данные. Но JPA имеет также **explicit optimistic locking**.

**JPA позволяет устанавливать** режимы блокировки для операций (т.е. устанавливать их передачей дополнительного параметра этим методам):

* **finding** an entity
* **locking** an existing persistence context entity
* **refreshing** an entity
* **querying** - в методах JPQL, Criteria или native queries

**Hibernate в дополнение позволяет устанавливать** режимы lock в методах:

* **getting** an entity
* **loading** an entity

```java
// Для @Version в Entity блокировка включена по умолчанию, но можно и указать явно в методах
// Пример установки блокировки в методе lock
session.lock(role.getUser(), LockModeType.OPTIMISTIC_FORCE_INCREMENT);
entityManager.find(Student.class, studentId, LockModeType.OPTIMISTIC);
entityManager.refresh(student, LockModeType.READ);

@NamedQuery(name="optimisticLock",
  query="SELECT s FROM Student s WHERE s.id LIKE :id",
  lockMode = WRITE)

@Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
@Query("SELECT c FROM Customer c WHERE c.orgId = ?1")
public List<Customer> fetchCustomersByOrgId(Long orgId);

// lock timeout
@Lock(LockModeType.PESSIMISTIC_READ)
@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
public Optional<Customer> findById(Long customerId);
```

**Типы Explicit lock в JPA**

* **NONE** - если не указано **Explicit lock** (не используется), то используется implicit locking (optimistic or pessimistic)
* **OPTIMISTIC** (READ) - применяет optimistic read lock ко всем Entity содержащим @Version атрибут. Когда используется OPTIMISTIC lock, то **persistence provider** защищает даные от **dirty reads** и **non-repeatable reads** (другими словами в этом случае persistence provider проверяет чтобы любая transaction упала (fails) при commit любых данных, которые **другая** параллельная транзакция: **1)** updated или deleted, но НЕ commited или **2)** updated или deleted и успешно commited)
* **OPTIMISTIC_FORCE_INCREMENT** (WRITE) - как OPTIMISTIC, но номер version увеличивается даже если никаких изменений внесено не было. В спецификации не указано должно это быть сделано сразу после commit или после flush.
* **PESSIMISTIC_READ** - делает shared lock, предотвращает updated и deleted. Используется когда в процессе работы с данными не может встретится **dirty reads**, которая может испортить данные. В некоторых DB не поддерживается, там есть только **PESSIMISTIC_WRITE**
* **PESSIMISTIC_WRITE** - делает exclusive lock, предотвращает read, updated и deleted. **Прим.** Некоторые DB реализуют **multi-version concurrency control**, который позволяет делать read данных на которых сделан lock. **Прим 2.** т.е. запрещает другим transaction делать PESSIMISTIC_READ и PESSIMISTIC_WRITE на тех же заблокированных элементах.
* **PESSIMISTIC_FORCE_INCREMENT** - как PESSIMISTIC_WRITE, но при этом увеличивает version атрибут для Entity с version атрибутом. Если работа идет с Entity у которой есть version атрибут, то использовать нужно этот lock. Некоторые persistence provider не поддерживают работы PESSIMISTIC_FORCE_INCREMENT с Entity без версий, тогда будет **PersistanceException**.

Существуют **Hibernate LockMode** и **JPA LockModeType** - в Hibernate режимов больше.

* **UPGRADE_NOWAIT** и **UPGRADE_SKIPLOCKED** - некий аналог **PESSIMISTIC_WRITE**. Эти режимы используют Oracle-style запросы `select for update nowait` и `select for update skip locked`
* Остальные режимы в Hibernate аналогичны режимам JPA

**Exceptions для pessimistic lock**:

* **PessimisticLockException**
* **LockTimeoutException**
* **PersistanceException**

**Pessimistic Locking vs. Optimistic Locking** - **Optimistic Locking** полезна когда есть много reads и мало updates или deletes. Optimistic Locking также полезна когда entities на какое-то время нужно сделать detached и Pessimistic locks не могут быть применены. **pessimistic locking** делает lock на уровне DB. pessimistic locking в целом лучше защищает целостность данных. **Другими словами** optimistic locking  убеждается, что updates or deletes не будет overwritten or lost молча. В отличии от Pessimistic Locking оптимистичная блокировка не делает lock на DB и не может вызвать deadlock.

В разных движках внутри блокировки работают по разному, например в InnoDB блокируются не сами записи, а **индексы** и есть блокировки:

* **record lock** — блокировка записи индекса
* **gap lock** — блокировка промежутка между, до или после индексной записи
* **next-key lock** — блокировка записи индекса и промежутка перед ней
* **range locks** 
* **predicate locking**

**Про разные типы блокировок в InnoDB хорошо тут:**

* https://habr.com/ru/post/238513/
* [REPEATABLE-READ and READ-COMMITTED Transaction Isolation Levels](https://www.percona.com/blog/2012/08/28/differences-between-read-committed-and-repeatable-read-transaction-isolation-levels/) (про InnoDB)

## Lock Scope

https://www.baeldung.com/jpa-pessimistic-locking

**Lock scope** - это параметр, который передается в методы JPA для работы и определяет поведение lock со связями (relationship) с Entity. Т.к. lock влияет на  joined inheritance и secondary tables. Не все persistence providers поддерживают lock scopes.

```java
Map<String, Object> properties = new HashMap<>();
map.put("javax.persistence.lock.scope", PessimisticLockScope.EXTENDED);
entityManager.find(Student.class, 1L, LockModeType.PESSIMISTIC_WRITE, properties);
```

**Типы Lock scope:**

* **PessimisticLockScope.NORMAL** (default) - блокирует только Entity с которой идет работа, при использовании `@Inheritance(strategy = InheritanceType.JOINED)` блокирует и придков класса Entity. Т.е. lock делается на many-to-one and one-to-one foreign keys, но lock на other side parent associations не делается.
* **PessimisticLockScope.EXTENDED** - как Normal, но в дополнение блокирует связные Entity в **"join table"** (@ElementCollection or @OneToOne, @OneToMany etc. with @JoinTable). Другими словами lock распостраняется и на **junction tables** (связь many-to-many). Этот тип lock (**прим.** видимо! имеется ввиду explicit lock с использованием данного scope) полезен только для защиты против удаления дочерних (children), но разрешает phantom reads и изменение children entity states.

```sql
-- PessimisticLockScope.NORMAL
-- Если есть два класса PERSON и EMPLOYEE, то lock будет выглядеть так, как я понял!
SELECT t0.ID, t0.DTYPE, t0.LASTNAME, t0.NAME, t1.ID, t1.SALARY 
FROM PERSON t0, EMPLOYEE t1 
WHERE ((t0.ID = ?) AND ((t1.ID = t0.ID) AND (t0.DTYPE = ?))) FOR UPDATE

-- PessimisticLockScope.EXTENDED запрос будет видимо выглядеть так
SELECT CUSTOMERID, LASTNAME, NAME
FROM CUSTOMER WHERE (CUSTOMERID = ?) FOR UPDATE

SELECT CITY, COUNTRY, Customer_CUSTOMERID 
FROM customer_address 
WHERE (Customer_CUSTOMERID = ?) FOR UPDATE
```

## Lock Timeout

Можно устаналивать Lock Timeout. По истечении выбросит **LockTimeoutException**. Если поставить значение ноль, то это равносильно **no wait**, но не все DB поддерживают такое.

```java
Map<String, Object> properties = new HashMap<>(); 
map.put("javax.persistence.lock.timeout", 1000L); 

entityManager.find(
  Student.class, 1L, LockModeType.PESSIMISTIC_READ, properties);
```

## Phisical and logical transaction

**phisical transaction** - выполняется для каждого отдельного запроса, если сделана на уровне DB (т.е. сделана SQL запросом), даже если границы transaction не объявлены явно (BEGIN/COMMIT/ROLLBACK). Это поведение согласно **ACID**.

**logical transaction** - это **application-level unit of work**, которая распостраняется на множество **physical** (database) **transactions**.

Удержание соединения с DB в течении нескольких user requests, пока user думает это anti-pattern. **database server** может поддерживать ограниченное число соединений, обычно каждое из этих соединений переиспользуется (используется одно соединение на несколько users) через **connection pooling**. Удержание limited ресурсов ограничивает scalability. Поэтому database transactions должны быть как можно **короткими**, чтобы освободить соединение как можно **быстрее**.

Web application влечет использовыание **read-modify-write conversational** паттерна. Web conversation (обмен данными) состоит из множества user requests, все эти операции logicaly связаны с одной и той же **application-level transaction** (т.е. с logical transaction). Такая transaction тоже должна соблюдает ACID, т.к. другие users могут изменить Entities еще до того как shared lock будет released (отпущен). **database transaction ACID** могут предотвратить **lost updates** только в границах **одной** physical transaction. Чтобы предотвратить **lost updates** в границах logical transaction нужно чтобы она тоже соблюдала ACID.

Чтобы предовтратить **lost updates** для application level transaction (logical transaction) нужно поставить ей **repeatable reads** isolation level.

Spring предоставляет (provide) только logical transaction manager, а physical JTA transaction manager такие как Bitronix, Atomikos, RedHat Narayana нужно подключать вручную.

В разных DB операции delete, update, insert могут вести себя немного по разному при использовании locks.

JTA не поддерживает **transaction-scoped isolation levels**, поддерживает только `resource local` isolation levels (т.е. только для простого подключения к DB, не JTA), поэтому Spring предоставляет `IsolationLevelDataSourceRouter`, чтобы передавать transaction isolation levels.
**logical transaction** (e.g. @Transactional) isolation level в Spring проверяется **IsolationLevelDataSourceRouter** и connection отправляется к определенной реализации DataSource, которая обслуживаются определенным JDBC Connection со своим transaction isolation level.

**Spring transaction manager** это просто facade для `resource local` или `JTA transaction managers`. И вместе с этим используется AOP, поэтому преход с **resource local** на **XA** (JTA) transactions.

## Использование блокировок и транзакций на практике

Согласно [этому](https://stackoverflow.com/questions/35068324/why-should-i-need-application-level-repeatable-reads#comment57934910_35105898) ответу **optimistic lock** + **READ_COMMITED** предотвращает **lost updates**. При этом **Repeatable-Read** предотвращает **read and write skew** в отличии от **READ_COMMITED**.

isolation level выше чем read uncommited это pessimistic locking (реализован через него), т.е. сам lock делается на используемом в transaction момент ресурсе.

Если в DB высокая конкуренция transactions и fail при optimistic lock происходят часто, то будет высокий failure rate. Pressimistic lock это **queuing mechanism** (ставит операции в очередь) и тоже затратен, т.к. другие операции ждут своей очереди.

**READ_COMMITTED** стоит по умолчанию во многих DB (в MySQL стоит **REPEATABLE_READ**). Она эффективна, но для некоторых случаев нужно ее менять.

В Oracle уровень изоляции Serializable при использовании MVCC (Multi-Version Concurrency Control) на самом деле это Snapshot Isolation level.

При **read only** транзакции не проходят dirty checking в кэше и для них не работает **transaction log** (по некоторым статьям). Поэтому такие транзакции быстрее.

## Entity state transitions

**Состояния:**

* **New** (Transient) - созданный новый объект, который не был ассоциирован с Hibernate Session (a.k.a Persistence Context) и не был mapped к какой-либо DB row.
* **Persistent** (Managed) - ассоциирован с DB row и управляется Persistence Context'ом. Любое изменение в этом состоянии будет detected и propagated на DB (во время **Session flush-time**, в самый последний момент, т.е. в конце logic transaction). В это состояние Entity переходит при `EntityManager#persist`
* **Detached** - когда Persistence Context is closed все entities становятся detached. Изменения Утещешуы больше не отслеживаются и не будет синхронизированы с DB.
* **Removed** - JPA требует, чтобы Entities могли быть только **removed**, но в Hibernate можно на самом деле удалять Entities (**delete detached entities**), но только через `Session#delete`. В JPA Entity которая **removed** только ставится в очередь (scheduled) на удаление, а само удаление из DB командой `DELETE` будет только во время **Session flush-time**.

Чтобы заново **associate** entity которые **detached** есть несколько способов:

* **Reattaching** (Hibernate but not JPA 2.1) - метод `Session#update`, Hibernate Session может associate только **один** объект Entity к DB row. Потому что Persistence Context ведет себя как in-memory cache (first level cache) и только одно value (entity) ассоциировано с ключем (по которому из кэша достаются Entities). Entity может быть reattached только если нет другого JVM object, который mapped к той же самой DB row уже **associated** Hibernate Session.
* **Merging** - копирует **detached** entity state (source) в **managed** entity instance (destination). Если entity которая мержится не имеет эквивалента в Session, то он будет fetched из DB.

Состояние меняется через интерфейсы **Session** и **EntityManager**, при этом **Session** включает в себя все методы **EntityManager**.

Говорят, что flush-time state трансформируется в DB DML statement (состояние сущности преобразуется в SQL запросы).

## flush strategies

Этот раздел **ВИДИМО** о функциях Hibernate которые **в Spring и EJB обернуты в AOP и паттерны** и пользователь с ними сталкивается **по минимуму**. Т.е. инфа скорее позновательна.

**Long conversations** - это все время пока user "думает" между операциями с данными, которые обернуты в транзакции. Сами Entity и Session при этом могут вести по разному.

**single conversation** обычно распостранена на нсколько DB transaction. При этом только последняя **physical transaction** (в рамках обертки **application-level transaction** вокруг нее) должна выполнять операции модификации данных (DML) иначет **application-level transaction** не будет **Unit of Work** (т.е. не будет atomic). Есть несколько Hibernate features которые это реализуют:

* **Automatic Versioning** - automatic optimistic concurrency control, автоматически проверяет если было параллельное изменение данных во время **user think time**, эта проверка происходит вконце conversation.
* **Detached Objects** - это происходит при использовании **session-per-request** pattern, все entities будут detached во время **user think time**. При этом Hibernate позволяет сделать **reattach** этих entities и persist. Этот паттерн называется **session-per-request-with-detached-objects**. Automatic versioning при этом используется для изоляции параллельных изменений.
* **Extended (or Long) Session (persistence context)** - при этом подходе **Session** может быть **disconnected** от нижележащего **JDBC connection** (т.е. connection к DB будет closed), после того как DB transaction уже **committed** и будет **reconnected** после того как новый запрос будет отправлен клиентом (**т.е.** будет переиспользована старая **original** Session). Этот паттерн называется **session-per-conversation** и делает **reattachment** ненужным. Automatic versioning используется при этом и flush **не будет** сделан автоматически, только явно.

**session-per-request-with-detached-objects** и **session-per-conversation** паттерны - оба имеют преимущества и недостатки.

При использовании **Extended persistence context** чтобы выключить **persistence** во время **application-level transaction** есть несколько вариантов:

1. **disable automatic flushing** установкой **Session FlushMode** в **MANUAL**, при этом в конце обязательно вызвать **Session#flush()**
2. Или отметить все кроме последней transaction как **read-only**, для таких transaction выключится **dirty checking** и **automatic flushing**. При этом **read-only** распостраняется и в **JDBC Connection** и вызывает DB оптимизации. При этом последняя transaction должна быть **writeable**, чтобы сделать **flushed** и **committed**.

При **Extended persistence context** entities остаются attached между разными requests. Из недостатков - расход памяти и чем больше **persistence context** тем медленнее **dirty checking** оптимизация кэша.

Все **extended persistence context** устанавливают **default transaction propagation** в **NOT_SUPPORTED** which makes it uncertain if the queries are enrolled in the context of a local transaction or each query is executed in a separate database transaction.

В различных приложениях используются разные удобные механизмы чтобы работать с **Extended persistence context**, например Java EE может использоваться **@Stateful Session Beans** с **EXTENDED PersistenceContext**

При **Detached objects** подходе используется **intermediate physical transaction**. Чтобы сделать Entities опять **managed** есть несколько вариантов:

1. В Hibernate (не в JPA) есть **Session.update()**, если attached entity уже есть, то будет exception т.к. Session не можут иметь больше одной reference на entity 
2. **Detached objects** может быть **merged** со своим **persistent object equivalent**, если такой entity нет, то Hibernate сделает ее load (fetch) из DB. Сам по себе **Detached object** сделать **managed** нельзя. Этот паттерн может вызвать **lost update** если объект с которым мы делаем **merge** был изменен нами, изменения затрутся.

**Detached entities storage** - так можно назвать место, где хранятся **detached entities** во время **long conversation**. Это **stateful context** в котором можно найти те же самые **detached entities** (т.е. видимо те entities которые мы сделали detached). В Java EE есть встроенный функционал **Stateful Session Beans**, в других приложениях таких как Spring используется объект **HttpSession** для хранения данных (он с его методами get/set **не thread safe**), кроме этого используются third party решения такие как **Spring Web Flow** и **Seam**.

**Важная инфа:** В [источнике](https://vladmihalcea.com/preventing-lost-updates-in-long-conversations/) не совсем ясно написано, но как я понимаю, автор советует использовать **Optimistic locking** ( `@Version` ) **для всех Entities** чтобы убрать. Потому что само по себе **long conversation** перемещает границы транзакции с DB в application и чтобы сделать **application-level repeatable reads** (т.е. установить этот isolation level), но там отсутствует database locking и используется механизм concurrency control самого приложения. Само **Optimistic locking** работает и для DB, и для application (всмысле решает их проблемы? в источнике неясно написано).

**Persistence Context flush.** Hibernate задерживает **Persistence Context flush** до самого последнего момента. Эта стратегия называется **transactional write-behind** и эта одна из стратегий кэширования в программировании вообще, а не только для транзакций. **transactional write-behind** больше относится к самому Hibernate, а не к logical or physical transaction, это потому что **flush** может случится несколько раз за время какой-либо transaction. Операции **flush** видны только **текущей** транзакции, другим transactions они видны только после **commit**

**persistence context** (a.k.a. first level cache) ведет себя как buffer между entity state transitions и DB

В [теории кэширования](https://en.wikipedia.org/wiki/Cache_(computing)#WRITE-BEHIND) **write-behind** использует кэш для хранения синхронизированных данных (entities), и эти данные рано или поздно будут записаны в реальное хранилище (flushed).

**Reducing lock contention.** DML statement выполняются в transaction, в зависимости от transaction isolation level и locks (shared or explicit), делается lock на определенных rows. Уменьшение времени lock уменьшает вероятность **dead-lock** и согласно [scalability theory](https://vladmihalcea.com/the-simple-scalability-equation/) это увеличивает throughput. Locks всегда вызывают serial executions (последовательное выполнение) операций. И согласно Закону Амдала max скорость программы обратно пропорциональна самому медленному последовательному куску программы (т.е. чем меньше serial кусок кода, тем меньше замедленее, т.к. скорость работы всей программы не может быть меньше скорости работы самого медленного ее последовательного куска).

Даже при **READ_COMMITTED** операции UPDATE and DELETE захватывают locks чтобы не дать параллельным transactions изменять данные. Так что задержка (откладывание на последний момент) lock операций (e.g. UPDATE and DELETE) может увеличить производительность, но нужно удостоверится что не будет нарушена consistency.

**Batching.** Откладывание операций на последний момент (т.е. последний момент logic transaction) также плюс в том, что операции SQL можно выполнить за одну (Batching) и Hibernate использует **JDBC batching optimization.** (группировка multiple DML statements into в одну операцию) это упеньшает **database round-trips**

**Read-your-own-writes consistency.**  У flush есть режимы **Scope.** Scope для flush это **Persistence Context** (если используется Session) или **Query** (если используется **Query**, **Criteria** или **TypedQuery** )

**Таблица режимов flush** (**AUTO** стоит в default)
| JPA FlushModeType | Hibernate FlushMode | Hibernate implementation details                                                                                                                                       |
| ----------------- | ------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| AUTO              | AUTO                | The Session is **sometimes** flushed before query execution.                                                                                                               |
| COMMIT            | COMMIT              | The Session is **only** flushed prior to a transaction commit.                                                                                                             |
|                   | ALWAYS              | The Session is **always** flushed before query execution.                                                                                                                  |
|                   | MANUAL              | The Session can **only** be manually flushed.                                                                                                                              |
|                   | ~~NEVER~~               | **Deprecated.** Use MANUAL instead. This was the original name given to manual flushing, but it was misleading users into thinking that the Session won’t ever be flushed. |

Автор блога [рекомендует](https://vladmihalcea.com/how-does-the-auto-flush-work-in-jpa-and-hibernate/) выбирать режим flush **ALWAYS**, т.к. с **AUTO** есть очень много проблем. При этом **ALWAYS** в Hibernate работает примерно как **AUTO** в JPA (по крайней мере на момент написания). Как я понял причина в том, что отследить flush в случае **ALWAYS** легче, чем в случае **ALWAYS** когда некоторые запросы **могут** вызвать неожиданные flush по мере работы приложения. Одна из причин этого в том, что для **native SQL** (и `@NamedNativeQueries`) Hibernate не может распознать что делать, т.к. в него встроена только ограниченная поддержка SQL, а не специфичная для всех DBs.

В режиме flush **AUTO** можно указать Hibernate какую таблицу синхронизировать перед выполнением **native SQL,** запроса это также полезно для **Second Level Cache**

```java
session.createSQLQuery("select id from product").addSynchronizedEntityClass(Product.class).uniqueResult();
```

## dirty checking mechanism

Тут будет описан dirty checking mechanism
https://vladmihalcea.com/the-anatomy-of-hibernate-dirty-checking/

**Если коротко:** hibernate в кэше содержит и копию оригинального объекта, и ту что меняется. Поэтому на каждый объект нужно в 2 раза больше памяти. Если хотя бы одно поле Entity менялось Hibernate всеравно отслеживает объект целиком. Отслеживается каждое поле каждого объекта. На большое количество объектов может загрузить CPU. Можно включить экспериментальный режим с использованием проверки через Bytecode, начиная с Hibernate 5 его работа улучшена.

## Isolation level

Все уровни изоляции кроме **SERIALIZABLE** могут приводить к **data anomalies** (phenomena).

Таблица того что предотвращает каждый из уровней изоляции
| Isolation Level  | Dirty read | Non-repeatable read | Phantom read |
| ----------------- | ----------- | -------------------- | ------------ |
| READ_UNCOMMITTED | allowed    | allowed             | allowed      |
| READ_COMMITTED   | prevented  | allowed             | allowed      |
| REPEATABLE_READ  | prevented  | prevented           | allowed      |
| SERIALIZABLE     | prevented  | prevented           | prevented    |

**Феномены Из SQL стандарта:**

* **Dirty read** (при **READ_UNCOMMITTED**) - случается когда транзакция позволяет read uncommitted (еще незакомиченные данные) другим транзакциям, т.е. прочитанные данные потом могут откатиться. Это случается потому что не сделан lock на данных. Большинство DBs имеют by default **более высокий уровень изоляции,** (чем READ_UNCOMMITTED) т.к. этот плохо влияет на целостность данных. Чтобы установить этот уровень изоляции DB прячет uncommitted changes ото всех других транзакций и каждая из транзакций видит только свои изменения данных. Если DB использует **2PL**, то uncommitted rows защищены через сделанный на них write lock (предотвращает операции read этих rows пока данные не commited). Если DB использует **MVCC**, то движок DB может использовать **undo log** который содержит uncommitted record каждой прошлой версии данных, чтобы восстановить прошлое значение данных для запросов сделанных другими транзакциями (т.е. если данные будет читать параллельная транзакция, то она получит старое значение, а не то что в процессе изменения текущей транзакцией). Т.к. этот механизм (видимо имеется ввиду **undo log**) используется всеми другими уровнями изоляции, и он имеет какие-то оптимизации связанные с восстановление образов прошлых данных. **(прим.** Данный уровень изоляции можно использовать например если нужно посмотреть прогресс batch запроса SQL как я понял на реальных данных, когда видно как он меняет данные в самой DB.)
* **Non-repeatable read** (или **fuzzy reads**) - при повторном чтении в рамках одной транзакции ранее прочитанные данные оказываются изменёнными. Происходит если какая-либо транзакция читает DB row без shared lock на новых полученных (fetched) record (записях), тогда параллельная транзакция может изменить эту row до того как первая транзакция завершена. **Другими словами** начинается первая транзакция по чтению записи (record), данные читаются; теперь начинается вторая (параллельная) транзакция, которая изменяет данные; первая транзакция перечитывает данные тем же SQL запросом и получает уже другой результат. **Это проблема,** потому что бизнес логика может принимать решение на основе самого **первого** результата чтения record до ее изменения в параллельной транзакции (т.е. например количество товара в магазине уже может быть равным нулю, хотя на момент первого чтения было больше). **Как это предотвращается на уровне DB.** При **2PL** делается shared lock при каждом чтении (read) и этот phenomenon будет предотвращен т.к. параллельные транзакции не смогут сделать exclusive lock на той же DB record. Большинства DB используют **MVCC** и shared lock больше необязательна для предотвращения **non-repeatable reads**, в этом случае делается проверка row version и если полученная (fetched) record до этого была изменена (между началом и канцом текущей параллельной транзакции), то текущая транзакция прервется (откатится). **Repeatable Read** and **Serializable** предотвращают **Non-repeatable read** по умолчанию. **Read Committed** может предотвратить этот phenomenon на уровне DB при использовании **shared locks** (e.g `SELECT ... FOR SHARE`) или **exclusive lock** (если ваша DB не поддерживает shared locks, e.g. Oracle). Некоторые ORM frameworks (e.g. JPA/Hibernate) имеют application-level repeatable reads (т.е. предотвращают repeatable reads на уровне приложения). Получается это потому что первая копия (snapshot) полученной entity попадает в кэш L1 (Persistence Context) и любой успешный запрос вернувший **туже DB row** будет использовать ту же Entity из кэша L1 (т.е. так предотвращается Non-repeatable read т.к. каждое чтение возвращает повторяющиеся данные, т.е. REPEATABLE READ, **Прим.** в статье написано МОЖЕТ но там не указано нужно что-то дополнительное для этого делать или так работает по умолчанию).
* **Phantom read** - когда одна транзакция начитала данные по одному критерию, другая делает insert или delete этих данных (добавляет или удаляет подходящие под критерий) и мы работаем с неактуальными данными **Note.** это как **Non-repeatable read**, но вместо изменения ранее прочитанных данных происходит вставка новых данных или удаление существующих подходящих под тот же критерий (predicate) по которому данные были прочитаны. Разница в том, что раз менялись не данные, а добавлялись или удалялись записи, то lock нужно делать уже по другому. Это проблема т.к. бизнес логика может работать на основе полученных ранее данных, хотя они уже другие. Например, покупатель может принимать решение о выборе покупке уже загруженного списка товаров не зная, что лучших товар был добавлен сразу после получения (fetching) списка товаров. **2PL** предотвращает **Phantom read** используя **predicate locking** (один из видов lock как видно из названия видимо используется в нем условие по которому делался `SELECT` строк), **MVCC** обрабатывает **Phantom read** путем возвращения **consistent snapshots** (т.е. как я понимаю валидных копий записей с которыми происходит работа). Не смотря на это результат работы **MVCC** с **Phantom read** может всеравно вернуть старые записи (т.е. не предотвратить **Phantom read**). Даже если движок DB делает introspects (проверку) списка работы транзакций (transaction schedule) результат может быть не таким как у **2PL** (например, когда вторая транзакция делает insert без чтения того же набора records как у первой транзакции - цитата из статьи, не совсем ясная). Чтобы предотвратить **Phantom read** нужно использовать **Serializable** или использовать **share lock** или **exclusive range lock** (прим. тут **range lock** это вид lock на определенном наборе строк, как я понимаю). **Другими словами**, если верить vladmihalcea.com то предотвратить **Phantom read** при использовании **MVCC** трудно, по крайней мерер на уровне DB (не JPA) и это можно сделать например через явный pessimistic lock строк. Не нужно путать **Phantom Read** с **Write Skew**, т.к. **Write Skew** не подразумевает проблемы (феномены) с **range of records**.

**Феномены Не из SQL стандарта:**

* **Lost Updates** - когда одна транзакция меняет данные, а другая параллельная сразу их затирает новыми (как присваивание переменной в коде два раза подряд). Если не предусмотреть ситуацию когда **lost update** может случиться, то он может случиться (т.е. при написании программы нужно предусмотреть эту ситуацию)! Hibernate включает все строки изменения данных в один UPDATE (т.е. как я понимаю строит их последовтельно, что и может вызвать затирание и как результат lost update), эту стратегию можно изменить черех `@DynamicUpdate`, но это решение не всегда срабатывает эффективно. Очистка ненужных операций (т.е. оптимизация, которая называется persistence context write-behind policy) используется, чтобы уменьшить время на которое делается lock, но она увеличивает время между read и write (т.к. хранит данные в себе все время до того как сделан flush) и чем больше это время тем больше вероятность lost update. В vladmihalcea.com написано, что чтобы предотвратить его нужно делать `optemistic lock` в jpa (рекомендовано в блоге vladmihalcea) или `pessimistic lock` или `SELECT FOR UPDATE` или `Repeatable Read` (или `Serializable`). Причем **Repeatable Read** предотвращает **lost update** как и в случае с **read commited** которая может предотвратить repeatable reads это происходит благодоря кэшу L1. Причем `pessimistic lock` в этом случае подойдет только если SELECT и UPDATE выполнены в одном запросе (т.е. обе **transactions** имеют `... FOR UPDATE`).
* **Read Skew** - Это когда есть несколько связанных Entities, одна транзакция читает одну Entity, вторая параллельная транзакция меняет обе Entities, первая транзакция читает вторую Entity. И получается что одна Entity начитана со старыми данными, а вторая Entity с новыми. Это ломает **consistency**.
* **Write Skew** - Есть несколько Entities. Обе параллельные транзакции их начитывают. Первая транзакция меняет данные одной Entity, вторая второй Entity. **(на самом деле** это определение может быть неточным, см. картинку в источнике) Т.е. нарушается синхронизация между несколькими связанными Entities. Для Oracle на момент написания предотвратить это можно только через optimistic или pessimistic lock (в том числе через `SELECT FOR UPDATE`), остальные DB вроде бы справляются в том числе в режиме MVCC.

Даже если движок конкретной DB может предотвращать при более низком уровне isolation проблемы более высокого уровня, не смотря на то что SQL стандарт говорит, что каком-то конкретном уровне изоляции проблема не может быть предотвращена, то **это не проблема.**. Источник [тут](https://stackoverflow.com/a/53468822)

## Transaction pitfalls (возможные проблемы)

Источник [тут](https://www.ibm.com/developerworks/library/j-ts1/index.html) статья старая, но может быть частично актуальна. Ответ влада о том что `@Transactional` нужна даже если в методе сервиса просто выполняется чтение [тут](https://stackoverflow.com/a/26327536)

1. ORM фреймворк выполняет операции через методы **только внутри транзакции**, если транзакция не была начата, то SQL не сгенерируется и запрос в DB не выполнится. Операции будут выполнены над logic transaction (т.е. физическая транзакция в DB выполнена не будет), старт транзакции требуется для запуска синхронизации между кэшем Hibernate и DB. Транзакция требуется для integrity и consistency (I и C из ACID)

   ```java
   public class TradingServiceImpl {
       @PersistenceContext(unitName="trading") EntityManager em;
    
       public long insertTrade(TradeData trade) throws Exception {
          em.persist(trade);
          return trade.getTradeId();
       }
   }
   ```

2. **@Transactional annotation pitfalls.** В Spring нужно не просто использовать аннотацию, а включить менеджер транзакций в конфигурации. При этом propagation level по умолчанию REQUIRED, read only стоит в false, а isolation level тот что в DB по умолчанию (обычно READ_COMMITTED).

3. **@Transactional read-only flag pitfalls.** **Note!** Начиная с версии Spring 5.1 исправлен баг с пробросом **readOnly** флага в Session. До этого только устанавливался `FlushType.MANUAL` и поэтому **только** отключался **automatic dirty checking mechanism**. Сама операция чтения данных это извлечение данных из `ResultSet` (наз. hydration и она нужна для dirty checking mechanism), она нужна чтобы сравнить текущее состояние Entity (snapshot) чтобы знать нужно ли делать `UPDATE` во время flush-time. Также detached state (видимо имеется ввиду оригинал Entity в кэше, который используется для сравнения) используется механизмом **versionless optimistic locking** (тут речь видимо о механизме optimistic locking, который не использует поле version, а реализован как-то иначе) для построения `WHERE`. Поэтому detached state хранится в Hibernate `Session` если только не использован **read-only** mode. Как результат экномится много памяти, т.к. состояние entity не хранится все время жизни Persistence Context (кэша L1). **Другими словами**, инфа ниже о **readOnly** может быть неактуальная. При этом **read only** означает только **загрузку Entities в read only** режиме, и их **можно удалять** через методы session из JPA.
   1. При `readOnly = true` и `Propagation.SUPPORTS` транзакция все равно выполнится без ошибок. Т.к. при `SUPPORTS` транзакция не будет запущена и выполнится local (database) transaction (т.е. на уровне DB, а не logic), `readOnly = true` применяется только если транзакция стартовала, а раз она не стартовала, то он проигнорирован.
   2. В этом же случае, но при `REQUIRED` транзакция стартует и поэтому будет `Exception` при попытке изменения данных внутри запущенной с флагом `readOnly` транзакции. **Note!** В примере ниже  в транзакции Spring выполняется JDBC код, не JPA.
   3. Т.к. внутри `@Transactional` выполняется JDBC код, а не JPA, то **транзакция** тут не нужен вообще для выполнения **readOnly** операций. Он может вызвать overhead и ненужный shared read locks (в зависимости от того какой isolation level стоит по умолчанию).
   4. Если внутри `@Transactional` с **readOnly** выполнится JPA код, то этот флаг может быть или проигнорирован и запрос выполнится (TopLink), или флаг проигнорирован и ошибки не будет, но запрос не выполнится (Hibernate). Для Hibernate будет установлен flush mode == `MANUAL` и insert не произойдет. Суть в том, что JPA не гарантирует строгое поведение флага **readOnly** 
   5. При использовании JPA предлагается выполнять запрос вне транзакции таким образом `@Transactional(readOnly = true, propagation=Propagation.SUPPORTS)`, т.е. выставив `SUPPORTS`. Или вообще не использовать `@Transactional` аннотацию
   6. Противоречие с предыдущей инфой о том, что использовать транзакцию с **readOnly** нужно. Ответ Vlad Mihalcea [тут](https://stackoverflow.com/a/26327536). Далее неточная инфа, уточнить! Использование `@Transactional` использует `TransactionalInterceptor` для правильной привязки Session (т.е. Session будет видна в методе пока не закончит работу метод) к методу для использования`OpenSessionInViewFilter` и поэтому предотвращает lazy loading exceptions. Также использование транзакции создает всего 1 изолированный connection к DB вместо отдельного connection на время всей транзакции, а если такой аннотации не будет, то создастся отдельный connection на каждый запрос (и это плохо). Это происходит в том числе, потому что любая операция выполняется **внутри transaction** и если аннотация `@Transactional` не проставлена включится **autocommit**.
       ```java
       // 1. выполнится без ошибок, т.к. траназкция будет запущена на уровне DB, а не приложения и readOnly будет проигнорирован
       @Transactional(readOnly = true, propagation=Propagation.SUPPORTS)
       public long insertTrade(TradeData trade) throws Exception {
          //JDBC Code...
       }

       // 2. будет Exception т.к. попытка выполнить update внутри транзакции с readOnly
       // 3. При этом для readOnly если внутри транзакции JDBC code транзакцию не нужно стартовать, т.к. это overhead и ненужный shared read locks
       @Transactional(readOnly = true, propagation=Propagation.REQUIRED)
       public long insertTrade(TradeData trade) throws Exception {
          //JDBC code...
       }

       // 4. В JPA при readOnly нет гарантий конкретного поведения
       @Transactional(readOnly = true, propagation=Propagation.REQUIRED)
       public long insertTrade(TradeData trade) throws Exception {
          em.persist(trade);
          return trade.getTradeId();
       }

       // 5. SUPPORTS или отсутствие @Transactional выключает транзакции, рекомендуется для readOnly операций
       @Transactional(readOnly = true, propagation=Propagation.SUPPORTS)
       public TradeData getTrade(long tradeId) throws Exception {
          return em.find(TradeData.class, tradeId);
       }
       ```

4. **REQUIRES_NEW transaction attribute pitfalls**. REQUIRES_NEW всегда стартует новую транзакцию независимо от того есть существующая или нет. Если `updateAcct` выполнится до rollback, то откат `insertTrade` не приведет к откату `updateAcct` и связные данные будут иметь разные значения.

   ```java
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public void updateAcct(TradeData trade) throws Exception {...}
   
   @Transactional(propagation=Propagation.REQUIRES_NEW)
   public long insertTrade(TradeData trade) throws Exception {
      em.persist(trade);
      updateAcct(trade);
      //exception occurs here! Trade rolled back but account update is not!
   }
   ```

5. **Transaction rollback pitfalls**. Если в каком-то месте внутри транзакции случится checked exception, то с начиная с места где оно случилось работа будет остановлена. Но все данные транзакции измененные до этого место будут commited. Чтобы откатить транзакцию нужно указать явно в **rollbackFor** для какого checked исключения ее откатить.

   ```java
   @Transactional(propagation=Propagation.REQUIRED)
   public TradeData placeTrade(TradeData trade) throws Exception {
      try {
         insertTrade(trade);
         updateAcct(trade);
         return trade;
      } catch (Exception up) {
         //log the error
         throw up;
      }
   }
   
   // фиксим вариант который выше, делаем откат в случае checked exception
   @Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)
   public TradeData placeTrade(TradeData trade) throws Exception {
      try {
         insertTrade(trade);
         updateAcct(trade);
         return trade;
      } catch (Exception up) {
         //log the error
         throw up;
      }
   }
   ```

6. Что на самом деле выполнится (с точки зрения транзакций), если вызвать method1()? **Ответ:** «*В связи с тем, что для поддержки транзакций через аннотации 
   используется Spring AOP, в момент вызова method1() на самом деле 
   вызывается метод прокси объекта. Создается новая транзакция и далее 
   происходит вызов method1() класса MyServiceImpl. А когда из method1() 
   вызовем method2(), обращения к прокси нет, вызывается уже сразу метод 
   нашего класса и, соответственно, никаких новых транзакций создаваться не
   будет*». Если для создания обертки-прокси будет использован **JDK dynamic proxy** (создает прокси на основе интерфейсов наследованных классом), то ответ верный, если будет использован **CGLib** (создает прокси для всех методов класса), который создает классы наследники, а не proxy объект (и этот наследник работает в качестве прокси), то ответ тоже верный. Независимо от библиотеки внутри поведение Spring AOP одинаково. При использовании **AspectJ** в режиме связывания **на этапе компиляции** (compile-time weaving) будет **вызвана транзакция внутри транзакции**, при использовании связывания **на этапе загрузки** (Load-time weaving) создадутся те же прокси, только во время загрузки объектов и поведение будет как у **JDK dynamic proxy** и **CGLib**

   ```java
   public class MyServiceImpl {
     @Transactional
     public void method1() {
       //do something
       method2();
     }
   	// для JDK dynamic proxy, CGLib и AspectJ (со связыванием на этапе загрузки) создатуся прокси и вызова транзакции внутри транзаксии НЕ будет
       // для AspectJ со связыванием на этапе компиляции вызовы встроятся в места вызовов и новая транзакция стартует
       @Transactional (propagation=Propagation.REQUIRES_NEW)
       public void method2() {
           //do something
       }
   }
   ```

# Про equals и @NaturalId

https://vladmihalcea.com/the-best-way-to-implement-equals-hashcode-and-tostring-with-jpa-and-hibernate/
https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/

Использовать `@NaturalId` поле в equals и hashCode. Если их нет, то нужно реализовать сравнение на основе id, но при этом добавить проверку `if(id != null)` т.к. пока Entity не начиталась id может быть `null`

Правильная реализация equals и hashCode для Entity не имеющей `@NaturalId`, ключевая особенность это проверка id != null в реализации, т.к. id может быть null пока данные не загружены (31 выбрано как число не делящееся на 2 и поэтому удобнее для хэшей, операции с 31 быстрее на некоторых CPU, занимает 8 бит и нужен только 1 сдвиг для изменения чего-то там. При этом hashCode постоянная, потому что сравнения по id в данном случае достаточно, а когда id == null му можем положиться на сравнение с ссылкой на сам объект):

```java
// правильная реализация
@Entity
public class Book implements Identifiable<Long> {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Book))
            return false;

        Book other = (Book) o;

        return id != null &&
               id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return 31;
    }

    //Getters and setters omitted for brevity
}
```

При этом будут использованы множества buckets в HashSet or HashMap (что в теории плохо). Но подразумевается что при работе с Hibernate число entities ограничивается (разработчиком). И **никогда нельзя** (имеется ввиду вообще, а не только этот случай) загружать тысячи entities `@OneToMany` **Set** т.к. performance penalty on the database side is multiple orders of magnitude higher than using a single hashed bucket.

# Кэширование и регионы кэша
Тут будет описание

# MVCC vs 2PL и про то что MVCC даже с Serializable isolation level может не предотвратить Phantom Read

Источник [тут](https://vladmihalcea.com/a-beginners-guide-to-the-phantom-read-anomaly-and-how-it-differs-between-2pl-and-mvcc/) (с картинками и примерами)

SQL Server - использует **2PL**
Oracle, PostgreSQL, and MySQL InnoDB - **MVCC**

При этом реализация и использование в **MVCC** уровня изоляции **Serializable** трудно.

**2PL** (Two-Phase Locking) - старый, более медленный, но надежный механизм. Использует shared (read) and exclusive (write) locks. Могут быть row-level locks, предотвращают lost updates, read and write skews. И range of rows locks, предотвращают phantom reads.

**MVCC** (Multi-Version Concurrency Control) - построена на идеи: Readers should not block Writers, and Writers should not block Readers. Использует optimistic (похоже на **идеи** optimistic lock в JPA) подход к решению проблем. Как и в 2PL для всех изменяемых данных используется exclusive lock иначе может нарушиться **atomicity.** При использовании MVCC в DB может быть **Serializable**, но на самом деле внутри это **Snapshot** уровень (это уровень присущий MVCC), который просто имеет другое название и всеравно имеет проблемы с предотвращением феноменов. В отличии от 2PL в MVCC нет возможности реализовать isolation level поверх MVCC, поэтому каждая DB использует свои уровни которые пытаются предотвратить так много аномалий как могут. **PostgreSQL** в отличии от других DB реализовала **Serializable Snapshot Isolation** (SSI) level, который лучше чем у других DB и может предотвратить Write Skews (это сложный механизм). Но даже в PostgreSQL это не настоящая Serializability, т.к. она подразумевает, что транзакции выполняются последовательно (эквивалентны одному серийному выполнению всех своих команд). В случае PostgreSQL в реализации механизма задействовано время, что может вызвать какие-то особенности не с точки зрения механизма контроля транзакций DB, но с точки зрения приложения. При этом MySQL предотвращает (как я понимаю) многие проблемы, т.к. использует свой механизм с блокировками.

# Связи

`@ManyToOne` связь рекомендуется ставить **EAGER** почти всегда (в отличии от `@...ToMany` которые **LAZY** по умолчанию).

**bidirectional collections** считаются лучшей практикой чем **unidirectional**

## One-To-Many
Для связи **@OneToMany** нельзя ограничить число связанных элементов. Для многих случаев **@ManyToOne** достаточно и не нужно делать **@OneToMany**. В этом случае одним из вариантов начитки child entities это с использованием `entityManager.createQuery()` (т.е. просто JPQL запроса)

```java
// пример @OneToMany
@Entity
public class PostComment {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "post_id")
    private Post post;
}

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @OneToMany(
        mappedBy = "post",
        cascade = CascadeType.ALL,  // при сохранении текущей сохраняем связанные
        orphanRemoval = true        // удаляем те которые не имеют связей с текущей
    )
    private List<PostComment> comments = new ArrayList<>();

    // добавляем добавляя связь
    public void addComment(PostComment comment) {
        comments.add(comment);
        comment.setPost(this);
    }

    // удаляем удаляя связь
    public void removeComment(PostComment comment) {
        comments.remove(comment);
        comment.setPost(null);
    }
}

// использование
post.addComment(comment);
entityManager.persist(post);
```

## One-To-One
PK and FK обчно индексированы, поэтому с общим ключом Primary Key (аннотация `@MapsId`) будет работать быстрее. Связанная сущность всегда ведет себя как **FetchType.EAGER** что не проставляй. Единственный способ избежать этого включить **Bytecode enhancement**, поставить `@LazyToOne(LazyToOneOption.NO_PROXY)` и **НЕ** использовать `@MapsId`, только тогда **EAGER** заработает.
(**Прим.** не смотря на эту инфу о неэффективности судя по всему использование **EAGER** не так неэффективно, т.к. далее в [источнике](https://vladmihalcea.com/the-best-way-to-map-a-onetoone-relationship-with-jpa-and-hibernate/) используется EAGER и **говорится о эффективности**)

```java
@Entity
public class PostDetails {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId // Primary Key дочерней будет Foreign Key родителькой
    private Post post;
}

@Entity
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(
        mappedBy = "post",
        cascade = CascadeType.ALL,
        orphanRemoval = true,
        fetch = FetchType.LAZY
    )
    private PostDetails details;

    // т.к. связь one to one проверяем что связанная существует
    // т.е. чтобы синхронизировать обе стороны связи
    public void setDetails(PostDetails details) {
        if (details == null) {
            if (this.details != null) {
                this.details.setPost(null);
            }
        }
        else {
            details.setPost(this);
        }
        this.details = details;
    }
}
```

Эффективный способ использования - не использовать **bidirectional**, т.к. обе Entity загружаются за один запрос (проставить связь только с одной стороны), пример:

```java
@Entity
public class PostDetails {
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Post post;
}
```

## Many-To-Many

Методы **add и remove** использованы чтобы синхронизировать **bidirectional** связь. Методы **equals & hashCode** реализовывать **обязательно**, т.к. на них полагаются **add и remove** методы

```java
@Entity
public class Tag {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(mappedBy = "tags")
    private Set<Post> posts = new HashSet<>();

    // equals & hashCode реализовывать обязательно, т.к. на них полагаются **add и remove** методы
}

@Entity(name = "Post")
@Table(name = "post")
public class Post {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany(
        cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
        }
    )
    @JoinTable(name = "post_tag",
        joinColumns = @JoinColumn(name = "post_id"),
        inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<Tag> tags = new LinkedHashSet<>();

    public void addTag(Tag tag) {
        tags.add(tag);
        tag.getPosts().add(this);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
        tag.getPosts().remove(this);
    }

    // equals & hashCode реализовывать обязательно, т.к. на них полагаются **add и remove** методы
}

// используем
Post post1 = new Post("JPA with Hibernate");
Tag tag1 = new Tag("Java");

post1.addTag(tag1); // или post1.removeTag(javaTag);
entityManager.persist(post1);
// post1.removeTag(javaTag); может удалить связь
```

# Как правильно делать merge

Суть проблемы в том, что нужно добавить связные сущности к родительской и родительскую сущность в кажду связную (в цикле) перед merge.

```java
// ошибка
Post post = entityManager.createQuery(
    "select p " +
    "from Post p " +
    "join fetch p.comments " +
    "where p.id = :id", Post.class)
.setParameter("id", 1L)
.getSingleResult();

post.setComments(comments);

// ошибка
Post post = entityManager.createQuery(
    "select p " +
    "from Post p " +
    "join fetch p.comments " +
    "where p.id = :id", Post.class)
.setParameter("id", 1L)
.getSingleResult();

entityManager.detach(post);
post.setComments(comments);
entityManager.merge(post);

// правильно
Post post = entityManager.createQuery(
    "select p " +
    "from Post p " +
    "join fetch p.comments " +
    "where p.id = :id", Post.class)
.setParameter("id", 1L)
.getSingleResult();

entityManager.detach(post);

post.getComments().clear();
for (PostComment comment : comments) {
    post.addComment(comment);
    // тут метод addComment это не set, а реализованный метод
    // public void addComment(PostComment comment) {
    //     comments.add(comment);
    //     comment.setPost(this);
    // }
}

entityManager.merge(post);
```

# ResultTransformer и про то что он делает SQL запросы эффективнее

**Note.** _ВОЗМОЖНО_ это слишком сложно и Spring Data JPA делает не таким нужным использование данного механизма.

**ResultTransformer** трансформирует результат запроса в DTO (это DTO еще может называться неофициально **DTO projections** и оно эффективно только при read only запросах). Т.е. он кастомизирует **ResultSet**. ResultTransformer **делает генерацию SQL запросав эффективнее.**

```java
List<PersonAndCountryDTO> personAndAddressDTOs = entityManager
.createQuery(
    "select p, c.name " +
    "from Person p " +
    "join Country c on p.locale = c.locale " +
    "order by p.id")
.unwrap( org.hibernate.query.Query.class )
.setResultTransformer( // применяем трансформацию, что заставляет Hibernate генерировать SQL запросы эффективнее
    new ResultTransformer() {
        @Override
        public Object transformTuple(
            Object[] tuple,
            String[] aliases) {
            return new PersonAndCountryDTO(
                (Person) tuple[0],
                (String) tuple[1]
            );
        }

        @Override
        public List transformList(List collection) {
            return collection;
        }
    }
)
.getResultList();
```

# DTO projections и Tuple

Это о том как делать начитку данных.

**DTO projections** это просто DTO (Data Transfer Object) с частью только нужных полей. Кроме начитки запросами его можно получить через ConstructorResult или Tuple. **Можно** сказать что DTO может объединять данные нескольких Entities (но не всегда нескольких).

```java
// как сделать DTO

// 1. Через хранимый запрос и ConstructorResult
@NamedNativeQuery(
    name = "PostDTO",
    query =
        "SELECT " +
        "       p.id AS id, " +
        "       p.title AS title " +
        "FROM Post p " +
        "WHERE p.created_on > :fromTimestamp",
    resultSetMapping = "PostDTO"
)
@SqlResultSetMapping(   // указываем какие поля DTO использовать в конструкторе класса PostDTO
    name = "PostDTO",
    classes = @ConstructorResult(
        targetClass = PostDTO.class,
        columns = {
            @ColumnResult(name = "id"),
            @ColumnResult(name = "title")
        }
    )
)

// использование хранимый запрос
List<PostDTO> postDTOs = entityManager
.createNamedQuery("PostDTO")
.setParameter( "fromTimestamp", Timestamp.from(
    LocalDateTime.of( 2016, 1, 1, 0, 0, 0 )
        .toInstant( ZoneOffset.UTC ) ))
.getResultList();

// 2. через JPQL или Native SQL
List postDTOs = entityManager
.createNativeQuery(
    "select " +
    "       p.id as \"id\", " +
    "       p.title as \"title\" " +
    "from Post p " +
    "where p.created_on > :fromTimestamp")
.setParameter( "fromTimestamp", Timestamp.from(
    LocalDateTime.of( 2016, 1, 1, 0, 0, 0 ).toInstant( ZoneOffset.UTC ) ))
.unwrap( org.hibernate.query.NativeQuery.class )
.setResultTransformer( Transformers.aliasToBean( PostDTO.class ) )
.getResultList();

// 3. Через Turple
List<Tuple> postDTOs = entityManager
.createNativeQuery(
    "SELECT " +
    "       p.id AS id, " +
    "       p.title AS title " +
    "FROM Post p " +
    "WHERE p.created_on > :fromTimestamp", Tuple.class)
.setParameter( "fromTimestamp", Timestamp.from(
    LocalDateTime.of( 2016, 1, 1, 0, 0, 0 )
        .toInstant( ZoneOffset.UTC ) ))
.getResultList();

// использование Turple
Tuple postDTO = postDTOs.get( 0 );
(Number) postDTO.get( "id" ).longValue();
```

# StatelessSession

тут будет описание

# Зачем нужно делать Detached объекты в hibernate Session

Т.к. если объектов в hibernate Session много, то может быть `OutOfMemoryException`. Можно делать `clear()` and `evict()`. Это может случится если **hibernate Session** существует пока жива **server session**.

Если нужно работать с большим числом данных, то тоже может понадобится сделать **detached**.

Другой вариант ответа: **detached entities** существует, чтобы **уменьшить количество lock** во время transactions, т.е. уменьшить время выполнения transactions.

Другой вариант ответа: Чтобы **хранить** Entity между несколькими Hibernate Session (между запросами пользователя), паттерн **session-per-request**

# Hypersistence Optimizer

Это неофициальный инструмент проверки того правильно ли используется mapping c Hibernate ORM

# @DynamicUpdate

тут будет описание

# Collection vs Bag vs List vs Map vs Set

https://thoughts-on-java.org/association-mappings-bag-list-set/

**Допустимые типы коллекция в Hibernate:**

* **Collection** - 
* **Set** - рекомендован для `@ManyToMany`, т.к. сам на уровне приложения (не DB) удаляет дубликаты может оказаться полезным там где дубликаты не нужны
* **SortedSet** - 
* **List** - рекомендован для остальных типов связи
* **Bag** - это unordered List, т.е. `List` над которым не стоит `@OrderBy` (при этом в целом, не конкретно тут, `orderBy` лучше чем `sortedBy`)
* **Map** - https://thoughts-on-java.org/map-association-java-util-map/
* **SortedMap** - 
* `org.hibernate.usertype.UserCollectionType` - Можно реализовать свои типы наследуя его, сам по себе не используется

В старых версиях Hibernate коллекции вели себя по другому и были не такими эффективными. Это нужно помнить при чтении документации.

При этом рекомендуется инициализировать коллекции в Entity:

```java
@Entity
class User {
    // Set для @ManyToMany
    @ManyToMany
    private Set<Employee> employees = new HashSet<>();

    // Для сортированного Set нужно использовать SortedSet и @SortNatural
    // внутри используется Comparable
    @OneToMany(cascade = CascadeType.ALL)
    @SortNatural
    // @SortComparator(ReverseComparator.class) - или кстомный Comparator
    private SortedSet<Phone> phones = new TreeSet<>();
}
```

Внутри Hibernate использует не стандартные коллекции, а модифицированные классы коллекций, т.е. `new HashSet()` на самом деле будет не `HashSet`, а объектом другого класса, прокси. В документации написано, что-то про то, что hibernate не может различить коллекцию инициализированную `null` или инициализированную `пустой` коллекцией. На практике в коллекции был `null`, поэтому лучше инициализировать пустой коллекцией явно. 

# Sorting vs Ordering

https://thoughts-on-java.org/ordering-vs-sorting-hibernate-use/

# Типы id и их генерация

# Использование дат в Entity
# Авто подстановка в поле Entity текущей даты при сохранении Entity
