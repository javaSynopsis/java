лучшая статья про микросервисы на eureka https://habr.com/ru/post/280786/

---
web service - программа, идентифицируемая веб-адресом
с характеристиками:
    Функциональная совместимость
    Расширяемость
    Возможность машинной обработки описания

SOAP (Simple Object Access Protocol) - обычно используется этот протокол
---
SOAP vs REST («Простота против Стандарта» (безопасности + безопасности транзакций):
    1. REST легче; считается stateless (но на практике может быть не полностью stateless, см. Eureka микросервисы)
        REST поддерживает различные форматы: text, JSON, XML; SOAP — только XML,
        REST работает только по HTTP(S),
        REST - работает с url (ресурсами), SOAP с операциями (описанными в тегах?)
    2. SOAP на уровне протокола поддерживает security, transactions, обработка ошибок, строгая типизация; считается statefull.
        SOAP может использоваться с любым протоколом прикладного уровня: SMTP, FTP, HTTP, HTTPS и др.
        SOAP на основе чтения не может быть помещена в кэш, а REST в этом случае может быть закэширован,
        SOAP поддерживает SSL и WS-security, в то время как REST — только SSL,
        SOAP поддерживает ACID (Atomicity, Consistency, Isolation, Durability). REST поддерживает транзакции, но ни один из ACID не совместим с двух фазовым коммитом.
---
microservices
    1. каждый сервис имеет свою DB
    2. у каждого сервиса свой процесс
    3. обмениваются данными через messaging

microservice - сервис со своим context и в отдельном потоке (асинхронный)
    
Проблемы microservices:
    4. нужны инструменты для удобного мониторинга большого количества сервисов
    5. должны быть fault tolerant by Design (то есть при подении одного в цепочке 6. остальные завершались корректо)
    7. нужен авто билд и авто деплои многих компонентов сразу
    8. нужен Configuration Management для настройки всех компонетов сразу
    
Решение проблем microservices:
    1. Spring Boot - быстрая разработка, декларативное создание microservices
    2. Spring Cloud - группа проектов
    3. Spring Cloud Netflix - часть Spring Cloud
    4. Eureka - service registry, это микросервис содержащий инфу о зарегистрированных в нем microservices через которых они находят друг друга чтобы общаться
    5. Ribbon - load balancer, прослойка, которая решает к какому сервису обратиться, если есть выбор из нескольких одинаковых сервисов. В отличии от других балансеров Ribbon не централизованный, а имеет копию себя для каждого сервиса (у каждого сервиса своя прослойка Ribbon)
    6. Spring Boot Actuator - используется часто как зависимость, дает инфу о приложении, для мониторинга, можно интегрироваться с другими системами мониторинга
    
Работа по происходящая внутри microservices по вызову друг друга и др. сервисов называется "оркестровка".

circuit breaker - так называются инструменты для работы с ошибками в microservices. Они могут вызывать ошибку по timeout, переключаться на другой сервис и прочее.

В некоторых tutorial встречается Hydrix, но Hystrix видимо новее.
    
Суть создания Spring Boot Cloud Microservice (из видео от Neflix):
    1. Создается пара "сервер для регистрации микросервисов" и сами "микросервисы". Для этого используют Eureka.
        Микросервисы регистрируются в общем для всех сервере и эта инфа обновляется каждые N сек.
    2. Создается reverse прокси сервер (напр. Zuul), прямого доступа к микросервисам нет. Это называется "service client".
        Весь доступ к microservices идет через этот проокси (Zuul), он знает какой url какому microservice
        принадлежит и также для балансировки нагрузки выбирает какому из одинаковых сервисов послать запрос.
    3. Обычно есть кэш микросервисов для снижения нагрузки. Это называется "cache client".
        Запрос с прокси сервера идет в кэш.
        Если в кэше данных нет, то идет прямой вызов микросервиса с сохранением результата в кэш.
    4. "service client" и "cache client" вместе обычно называют "client library".
        "client library" будет вызвана в "Client Application" (встроена внего)
    5. Получившаяся структура не полностью stateless, не смотря на то что в теории miscroservices stateless
    6. Один из вариантов хранить общую конфигурацию для всех сервисов (Configuration Management) в Spring Cloud Config Server
    7. Hystrix библиотека - это circuit breaker. Аннотируя методы можно указать как им себя вести в случае падения, долгого выполнения и прочее. Возвращать им ошибку, переключаться на другой сервис и прочее.

Проблемы и решения (от Netflix):
    1. intra-service request - вызов сервисом других сервисов.
        Проблема: если один упал, могут упасть зависимые.
        Решение: Hystrix который дает timeouts, retries, fallbacks (возврат ошибки пользователю вместо падения).
        Hystrix имеет isolated thread pools и cirtuits. Т.е. если какой-то сервис постоянно падает, то Hystrix перестает его вызывать пока сервис не будет recovered.
    2. FIT Framework (Fault Injection Testing) - для теста делает сентетические транзакции или нагружает сервисы трафиком.
    3. Можно пометить для тестирования только часть сервисов, самые нагруженные.
    4. CAP Theorem - работа с БД это выбор между consistency и availability. Когда сервис должен записать копию данных в несколько разных DB.
        Проблема: что будет если запись в одну из DB с ошибкой?
        Решение: cassandra - делает запись в DB асинхронно. Можно считать транзакцию успешной только при записи в один node, если хотите рискнуть. Или ждать записи для всех node, если нужна надежность. Сервис может сделать запись в один node (сервис или DB???), эта запись автоматически распостранится на другие DB.
    5. Scale - типы сервисов и масштабирование
        1. stateless - не cache или database, сервис не очень важен и его легко заменить или перезапустить и подобное
        2. statefull - cache и database. Проблема: потеря данных и долгое пересоздание упавшего сервиса (восстановление).
        3. hybrid - это и есть описанный тут подоход с Eureka???
    6. Auto-scaling - используется с stateless сервисами, авто создание или удаление копий сервиса. Инструмент "Chous Monkey" для мониторинга какие сервисы упали, а какие работают.
    7. Squid cache - как плохой пример того что: каждый сервис должен иметь свою копию кэша. Иначе падение 1ин неисправный кэш приведет к падению всех сервисов. Это анти-паттерн.
    8. EVCache - wrapper around memcache D. Каждый сервис имеет свои копии кэшей. Противоположность squid cache.
    9. Если EVCache упадет нужно отключать сервис, иначе он не выдержит нагрузки.
        Решения (варианты):
            1. не вызываит постоянно один и тот же сервис (неточно!!!)
            2. кэшировать request вместо того чтобы выполнять его постоянно
            3. Для теста сервисов использовать Chaos tool

    
Spring Cloud модули:
    Dynamic Scale Up and Down:
        Naming Server (Eureka)
        Ribbon (Client Side Load Balancing)
        Feign (Easier REST Clients)
    Visibility and Monitoring with:
        Zipkin Distributed Tracing
        Netflix API Gateway
    Configuration Management with:
        Spring Cloud Config Server
    Fault Tolerance with:
        Hystrix
        
Паттерны microservices:
    https://dzone.com/articles/patterns-for-microservices-sync-vs-async

Цикл с Eureka:
    1. Регистрируем сервис в Eureka. Если нужно распределить нагрузку между 2мя одинаковыми сервисами, то зарегистрировать еще сервис с тем же именем.
    2. Для взаимодействия сервисов друг с другом они ищут партнеров в списке Eureka. Eureka имеет REST API через который сервисы ищут друг друга или регистрируют себя (не используется напрямую, он в /eureka/apps).
    3. К каждом сервису прикреплен Ribbon (балансер). Когда от сервиса исходит запрос к другому сервису Ribbon выбирает к какой копии другого сервиса их отправить (если несколько копий другого сервиса).

Пошаговое создание microservices web application в Spring Boot:
    1. Создать Eureka server rigistry, где будут регистрироваться сервисы.
        Для этого добавить jar: Spring Cloud и Eureka (Spring Cloud Netflix)
    2. Включить аннотацией @EnableEurekaServer
    3. Посмотреть веб интерфейс Eureka в http://localhost:8080
    4. Eureka при запуске пытается найти другие сервера Eureka и зарегистрировать их, сервер Eureka регистрирует и себя же как сервис в других Eureka серверах. Если ничего не найдено каждые 30 сек. в логи пишется ошибка. Для development такое поведение может быть неудобно. Пример отключения такого поведения в application.yml:
        eureka:
            instance:
                hostname: localhost
            client:
                fetch-registry: false
                register-with-eureka: false
                service-url:
                    defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka
    5. Если адрес eureka.instance.hostname не установлен, то он будет взят из environment variables
    6. Указывает искать ли другие Eureka сервера
        fetch-registry: false
        register-with-eureka: false
    7. Отключение сообщения об ошибке, если зарегистрированный сервис не подвердил регистрацию каждые 90 сек (3 периода по 30 сек):
        eureka.server.enable-self-preservation: false
    8. Подключить один Eureka сервер к другому:
        other:
            eureka:
                host:port:eureka1.tacocloud.com
                port:8762
    9. Регистрация microservices (service registry client) на сервере Eureka:
        1. Подключить spring-cloud-starter-netflix-eureka-client.
            Она добавляет все нужное включая: Eureka’s client-side library и Ribbon load balancer
        1.2 Можно настроить Ribbon load balancer отдельно для каждого сервиса, потому что каждый сервис имеет свою копию Ribbon
        2. Объявить приложение как Eureka service registry. Оно попытается найти Eureka сервер с портом 8761 и зарегистрировать там с именем UNKNOWN
        3. Установить имя сервису:
            spring:
                application:
                    name: ingredient-service
        4. Установить случайный порт вместо 8080, чтобы избежать конфликта:
            server:
                port:0 # 0 == случайный порт
        5. Можно зарегистрировать сервис на нескольких серверах. Когда сервис не может зарегистрироваться на 1ом сервере, он регистрируется на 2ом:
            eureka:
                client:
                    service-url:
                        defaultZone:http://eureka1.tacocloud.com:8761/eureka/,
                        http://eureka2.tacocloud.com:8762/eureka/
    10. Сообщение между сервисами Eureka (Consuming services)
        Происходит по именам сервисов зарегистрированным в Eureka сервер.
        Есть варианты:
            1. Чистый RestTemplate и обращаться к сервисам напрямую по URL
                rest.getForObject("http://localhost:8080/ingredients/{id}", Ingredient.class, ingredientId);
            2. Обернутый в Ribbon объект RestTemplate, вызов сервиса по name:
                0. Включить @EnableDiscoveryClient для @Configuration
                1. Создать бин:
                    @Bean
                    @LoadBalanced
                    public RestTemplate restTemplate() {
                        return new RestTemplate();
                    }
                2.  // Инжект в компонент
                    @Component class A {
                        A(@LoadBalanced RestTemplate rest) {
                            this.rest = rest;
                        }
                    }
                    // использование
                    Ingredient getIngredientById(String ingredientId) {
                        return rest.getForObject(
                        "http://ingredient-service/ingredients/{id}",
                        Ingredient.class, ingredientId);
                    }
                3. Можно использовать в реактивном Spring WebFlux (WebClient вместо RestTemplate). Аналогично RestTemplate.
            3. Использовать библиотеку Feign client interface для REST. Похожа на Spring Data REST
                1. Подключить
                    spring-cloud-starter-openfeign
                2. Включить
                    @Configuration
                    @EnableFeignClients
                    public RestClientConfiguration {
                    }
                3. Создать интерфес с таким же методом как у вызываемого сервиса
                    @FeignClient("ingredient-service")
                    public interface IngredientClient {
                        @GetMapping("/ingredients/{id}")
                        Ingredient getIngredient(@PathVariable("id") String id);
                    }
                4. Заинжектить его и использовать:
                    @Autowired
                    public IngredientController(IngredientClient client) {
                        client.getIngredient(id);
                    }

    11. Добавить spring-cloud-starter-netflix-hystrix для работы с ошибками. И отметить аннотациями методы, в аннотациях прописать поведение методов в случае ошибок или timeout.

Load-balanced RestTemplate и Feign поддерживают и могут использовать Ribbon.
    
Zuul - прокси сервер для доступа с клиента на сервисы. Это server side load balancer by Netflix. Плюсы: CORS, authentication, and security can be put into a centralized service. Можно использовать как фильтр для модификации запросов. Использует round-robin algorithm (возможно как вариант можно поставить другой???)

Zuul vs Ribbon - клиет "server side load" не знает список сервисов и посылает все запросы на один endpoint. И уже сам "server side load" определяет какому сервису послать запрос дальше. Клиент "client-side load balancer" знает список сервисов и посылает запрос конкретно какому-то одному по url или имени сервиса (в случае Eureka). Список сервисов может быть получен из конфигов или с Eureka registry сервера.
Zuul использует RibbonRoutingFilter, который направляет запрос конкретному сервису, используя Ribbon чтобы выбрать какому именно сервису послать запрос. Ribbon выбирает из списка сервисов из конфигурации или берет список с Eureka registry сервера. Чтобы Zuul работал как load-balanced reverse proxy ему нужен Ribbon.
Zuul - server side load
Ribbon - client-side load balancer

microservices vs Monolith
    масштабируются
    легко дебажить
    сервисы могут быть на разных языках

Monolith vs microservices
    Large Application Size
    Long Release Cycles
    Large Teams
    Difficult for a debugging
    трудно переносить на другие технологии
---
Простой способ вызывать несколько async сервисов вручную:
1. Поставить @EnableAsync чтобы включить асинхронную работу в @Configuration
2. Над МЕТОДАМИ @Service поставить @Async
2. Можно объявить свою конфигурацию пула Thread для @Async через Executor:
    @Bean(name = "myThreadPool") Executor taskExecutor() {}
    @Async("myThreadPool") doSmth(){}
3. Получение асинхронного ответа: (ожидание):
CompletableFuture<User> page1 = gitHubLookupService.findUser("PivotalSoftware");
CompletableFuture<User> page2 = gitHubLookupService.findUser("CloudFoundry");
CompletableFuture<User> page3 = gitHubLookupService.findUser("Spring-Projects");
CompletableFuture.allOf(page1,page2,page3).join(); // wait

AsyncRestTemplate - async аналог RestTemplate

---
Spring WS (Spring Web Services) - сервисы с SOAP

Spring Boot автоматически:
    1. Настраивает MessageDispatcherServlet (для обмена сообщениями с сервисами)
    2. Сканирует все .wsdl и .xsd
    

---
Spring RESTful service