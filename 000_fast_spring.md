Источники на очереди:
* https://www.baeldung.com/category/spring/tag/spring-core-basics/
* https://www.baeldung.com/category/spring/tag/spring-di/
* https://www.baeldung.com/category/spring/tag/spring-annotations/
* https://www.baeldung.com/category/spring/tag/spring-5/
* https://www.baeldung.com/category/spring/tag/spring-mvc-basics/

- [Простое подключение сервлета](#Простое-подключение-сервлета)
- [Обработка ошибок в Java EE сервлете](#Обработка-ошибок-в-java-ee-сервлете)
- [ServletConfig vs ServletContext](#servletconfig-vs-servletcontext)
- [Структура приложения Java EE](#Структура-приложения-java-ee)
- [Как получить пути сервлета](#Как-получить-пути-сервлета)
- [HttpServletRequestWrapper и HttpServletResponseWrapper](#httpservletrequestwrapper-и-httpservletresponsewrapper)
- [Каков жизненный цикл сервлета и когда какие методы вызываются?](#Каков-жизненный-цикл-сервлета-и-когда-какие-методы-вызываются)
- [CDI — это Contexts and Dependency Injection](#cdi--это-contexts-and-dependency-injection)
- [Scope](#scope)
- [Как вызвать destroy для prototype бинов](#Как-вызвать-destroy-для-prototype-бинов)
- [custom scope, scopeName и proxyMode = ScopedProxyMode и где используется](#custom-scope-scopename-и-proxymode--scopedproxymode-и-где-используется)
- [Spring Core](#spring-core)
  - [Общая инфа](#Общая-инфа)
  - [BeanFactory vs FactoryBean](#beanfactory-vs-factorybean)
  - [Шаги реализации функционала в Spring](#Шаги-реализации-функционала-в-spring)
  - [life cycle](#life-cycle)
  - [bean circular dependencies](#bean-circular-dependencies)
  - [Интернациализация](#Интернациализация)
  - [ContextLoaderListener](#contextloaderlistener)
  - [ServletContainerInitializer и как он взаимодействует со Spring](#servletcontainerinitializer-и-как-он-взаимодействует-со-spring)
  - [Когда использовать WebApplicationInitializer или AbstractAnnotationConfigDispatcherServletInitializer](#Когда-использовать-webapplicationinitializer-или-abstractannotationconfigdispatcherservletinitializer)
  - [События](#События)
  - [Spring Aware Interfaces](#spring-aware-interfaces)
  - [Выполнение операций при деплое приложения Spring (метод run())](#Выполнение-операций-при-деплое-приложения-spring-метод-run)
- [Spring MVC](#spring-mvc)
  - [Spring MVC Interceptor](#spring-mvc-interceptor)
  - [Обычные Filter из java ee в Spring](#Обычные-filter-из-java-ee-в-spring)
  - [Spring MVC](#spring-mvc-1)
  - [Spring MVC](#spring-mvc-2)
- [AOP](#aop)
  - [Общее](#Общее)
  - [Если нам все-таки надо добиться, что бы в случае Spring AOP код аспекта выполнялся при вызове proxy метода из другого proxy метода](#Если-нам-все-таки-надо-добиться-что-бы-в-случае-spring-aop-код-аспекта-выполнялся-при-вызове-proxy-метода-из-другого-proxy-метода)
- [Spring Boot](#spring-boot)
- [Spring Annotations](#spring-annotations)
  - [Общее](#Общее-1)
- [Spring Security](#spring-security)
  - [Общее](#Общее-2)
  - [AuthenticationManagerBuilder vs HttpSecurity vs WebSecurity](#authenticationmanagerbuilder-vs-httpsecurity-vs-websecurity)
- [Spring Data REST](#spring-data-rest)
- [Spring Data JPA + REST](#spring-data-jpa--rest)
- [SpEL](#spel)
- [Resource из Spring](#resource-из-spring)
- [---------- Ниже новое ----------](#-----------Ниже-новое-----------)
- [Spring Core](#spring-core-1)
  - [Почему нужно выбрать Spring?](#Почему-нужно-выбрать-spring)
  - [Annotations](#annotations)
  - [getBean()](#getbean)
  - [What is a Spring Bean?](#what-is-a-spring-bean)
- [Spring DI](#spring-di)
  - [FactoryBean](#factorybean)
- [Spring MVC](#spring-mvc-3)

# Простое подключение сервлета
**Аннотации**
```java
@WebServlet(UrlPatterns = {"/index"}, , loadOnStartup = 1,
    initParams = {
        @WebInitParam(name = "host", value = "port"),
        @WebInitParam(name = "user", value = "email@mail.ru")
})
public class MyServlet extends HttpServlet {doPost(){} doGet(){}}
```
```xml
<servlet>
    <display-name>FirtsServlet</display-name>
    <servlet-name>mvc1</servlet-name>
    <servlet-class>com.dome.MyServlet</servlet-class>
    <init-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:spring/mvc1-config.xml</param-value>
    </init-param>
    <load-on-startup>1</load-on-startup>
</servlet>
<servlet-mapping>
    <servlet-name>FirstServletName</servlet-name>
    <url-pattern>/main.jsp</url-pattern>
</servlet-mapping>
```

**подключение фильтра**
```java
@WebFilter(urlPatterns = {"/*"},
    initParams = {
        @WebInitParam(name = "encoding",
            value = "UTF-8",
            description = "Encoding param")
})
public class MyFilter implements Filter {
    public void init(FilterConfig fc) throws ServletException {}
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) {}
    public void destroy(){}
}
```
```xml
<filter>
    <filter-name>encodingfilter</filter-name>
    <filter-class>by.bsu.sample.filter.EncodingFilter</filter-class>
    <init-param>
        <param-name>encoding</param-name>
        <param-value>UTF-8</param-value>
    </init-param>
</filter>
<filter-mapping>
    <filter-name>encodingfilter</filter-name>
    <url-pattern>/myurl</url-pattern>
</filter-mapping>
```

**Просто подключение listener из Java EE (xml)**
**SessionListenerImpl**, как пример, наследует один из предопределенных классов
```java
@WebListener
public class SessionListenerImpl implements HttpSessionAttributeListener {
    // переопределенные методы
}
```
```xml
<listener>
    <listener-class>by.bsu.control.listener.SessionListenerImpl</listener-class>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/xyz.xml</param-value>
    </context-param>
</listener>
```

# Обработка ошибок в Java EE сервлете
```xml
<error-page>
    <exception-type>javax.servlet.ServletException</exception-type>
    <location>/AppExceptionHandler</location>
</error-page>
```

# ServletConfig vs ServletContext
**ServletContext** - один на всю приложенку, его параметры обьявляются в `<contex-param>`, через него можно получить доступ ко всем сервлетам приложения и программно регистрировать события, добавлять атрибуты, сервлеты etc

**ServletConfig** - у каждого сервлета свой, его параметры обьявляются в `<init-param>`

# Структура приложения Java EE
Ниже список каталогов и подкаталогов.
```
src/main + java, resources, filters, webapp
src/test + java, resources, filters, webapp
src/it - интеграционные тесты
src/assembly - Assembly descriptors
src/site - site
```

# Как получить пути сервлета
Берем context сервлета и достаем из него инфу.

`getServletContext().getRealPath(request.getServletPath())` - актуальный путь

`getServletContext().getServerInfo()` - инфа о сервере

`request.getRemoteAddr()`

# HttpServletRequestWrapper и HttpServletResponseWrapper
**HttpServletRequestWrapper** и **HttpServletResponseWrapper** - можно расширить эти классы и переопределить только необходимые методы и получим свои реализации.

# Каков жизненный цикл сервлета и когда какие методы вызываются?
1. **Загрузка класса сервлета** — загрузка класса сервлета в память и вызов конструктора без параметров (при запросе или `loadOnStratup`)
2. **Инициализация класса сервлета** — инициализирует `ServletConfig` и внедряет его через `init()`. Это и есть место где сервлет класс преобразуется из обычного класса в сервлет.
3. **Обработка запросов** — Для каждого запроса клиента сервлет контейнер порождает новую нить (поток) и вызывает метод `service()` путем передачи ссылки на объект ответа и запроса.
4. **Удаление из Service** — контейнер останавливается или останавливается приложение, уничтожает классы сервлетов путем вызова `destroy()`

# CDI — это Contexts and Dependency Injection

# Scope
1. **singleton** - По умолчанию. Spring IoC контейнер создает единственный экземпляр бина. Как правило, используется для бинов без сохранения состояния (stateless)
2. **prototype** - Spring IoC контейнер создает любое количество экземпляров бина. Новый экземпляр бина создается каждый раз, когда бин необходим в качестве зависимости, либо через вызов getBean(). Как правило, используется для бинов с сохранением состояния (stateful)
3. **request** - Жизненный цикл экземпляра ограничен единственным HTTP запросом; для каждого нового HTTP запроса создается новый экземпляр бина. Действует, только если вы используете web-aware `ApplicationContext`
4. **session** - Жизненный цикл экземпляра ограничен в пределах одной и той же HTTP Session. Действует, только если вы используете web-aware `ApplicationContext`
5. **global session** - Жизненный цикл экземпляра ограничен в пределах глобальной HTTP Session (обычно при использовании portlet контекста). Действует, только если вы используете web-aware `ApplicationContext`
6. **application** - Жизненный цикл экземпляра ограничен в пределах ServletContext. Действует, только если вы используете web-aware `ApplicationContext`. Как **singleton**, но привязан к `ApplicationContext`, а не `ServletContext`. Т.к. `ApplicationContext` может быть несколько на 1но приложение, т.к. у каждого `DispatcherServlet` может быть только 1ин `ApplicationContext`, но самих `DispatcherServlet` может быть много в приложении.
7. **websocket** - 

**prototype vs singleton** - `@PreDestroy` не вызвается для prototype, потому что бин не в контексте. Spring не удаляет prototype бины.

# Как вызвать destroy для prototype бинов
1. `BeanFactoryAware` - получить в его методе бин и проверить BeanFactory.isPrototype, использовать в BeanPostProcessor ниже
2. `DisposableBean` - реализуем тут destroy() который мы вызовем для уничтожения каждого бина
3. `BeanPostProcessor` - в его наследнике можно создать List всех prototype и пройтись по ним уничтожив
**ПОЯСНЕНИЕ**: мы наследуем ВСЕ эти интерфесы своему классу, который будет отслеживать prototype бины, добавлять в List и вызывать для каждого destroy() в цикле.

**Пример:**
```java
@Component
public class DestroyPrototypeBeansPostProcessor implements BeanPostProcessor, BeanFactoryAware, DisposableBean {
    private BeanFactory beanFactory;
    private final List<Object> prototypeBeans = new LinkedList<>();
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException { }
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException { }
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException { }
    @Override
    public void destroy() throws Exception { synchronized (prototypeBeans) { for(;;) { prototypeBean.destroy(); } } }
}
```

# custom scope, scopeName и proxyMode = ScopedProxyMode и где используется
эту тему описать тут

# Spring Core
## Общая инфа
* **Цель** - ослабление связей через DI и AOP и уменьшение количества кода. В качестве компонентов испольует POJO или JavaBean.
* **DI** - взаимодействие с объектом через интерфейм, не зная о реализации. IoC один из видом DI, реализован через паттерн ServiceLocator.
* Легко тестировать с DI, подставляя фективную реализацию. 

**DI** - лучше
<br>
**DL** (Dependency Lookup) - нужно использовать, когда невозможно DI, **e.g.** при инициализации через `ApplicationContext` автономного приложения (в случае MVC он сам все запускает)

1. **BeanFactory** - ядро DI (в т.ч. зависимости и life cycle). Позволяет РАЗДЕЛИТЬ конфиги и код.
2. **ApplicationContext** - расширение BeanFactory, кроме того содержит сервисы JMS, AOP, i18n, Event listeners, transactions, properties, MessageSource (для i18n) etc; регистрацию в BeanPostProcessor, BeanFactoryPostProcessor

Spring приложение можно создавать на основе **ИНТЕРФЕСОВ** и **БИНОВ** (в Spring Bean, не обязательно следовать JavaBean, особенно если DI через конструктор, т.к. не надо знать set/get)

**Spring Context** - конфигурационный файл о среде для Spring.
<br>
**Spring Context** - это **IoC** контейнер, создает, конфигурит бины читая мета инфу (конфиги).

* **IoC** - это (3и вида внедрения зависимости: конструктор, сетер, интерфес (CDL)):
    1. **Dependency lnjection** - внедрение зависимостей, напр xml или аннотации; внедряется IoC контейнером автоматически
        1. **Constructor Dependency Injection** - Внедрение зависимостей через конструктор
        2. **Setter Dependency Injection** - Внедрение зависимостей через метод установки
    2. **Dependency Lookup** - поиск зависимостей, когда зависимость вручную достается из контейнера: SpringContext.lookup('Blabla')
        1. **Dependency Pull** - Извлечение зависимостей вручную (чаще всего, прим. выше для DL)
        2. **Contextualized Dependency Lookup** (CDL) - реализуем метод из интерфейса контейнера в классе в котором нужна зависимость, этот метод сам вызовется и сделает set() зависимости: [пример](https://stackoverflow.com/a/42000709)

**IoC** - дает службы (сервисы) для взаимодействия компонентов со своими зависимостями.

**BeanFactory нужен если:** использовать приложение совместно с либами JDK 1.4 или не поддерживают JSR-250.

**Если вместе с BeanFactory нужно использовать AOP и транзакций** то при использовании **BeanFactory** необходимо добавить вручную регистрацию **BeanPostProcessor** и **BeanFactoryPostProcessor**. Т.к. они только в **ApplicationContext**.

**Типы ApplicationContext для разных типов конфигов:**
* **ClassPathXmlApplicationContext** - конфиги из classpach
* **FileSystemXmlApplicationContext** - файл конфигов
* **XmlWebApplicationContext** - по умолчанию по пути `/WEB-INF/applicationContext.xml`, или указать вручную (можно сразу несколько xml) добавив в web.xml слушатель ContextLoaderListener и параметр `<context-param>` с ключем `contextConfigLocation` и путями к xml

**Пример:**
```java
ConfigurableBeanFactory factory = new XmlBeanFactory(...);
// теперь зарегистрируем необходимый BeanPostProcessor экземпляр
MyBeanPostProcessor postProcessor = new MyBeanPostProcessor();
factory.addBeanPostProcessor(postProcessor);
// запускаем, используя factory
```

## BeanFactory vs FactoryBean
**FactoryBean** - интерфейс, который можно реализовать для конструирования сложных бинов и подключить к Spring. Нужен в основном для xml конфигурации, для конфигов с аннотациями можно установить зависимые объекты прямо в коде:
```java
person.setCar(car); // устанавливаем там где инициализируем @Bean вместо конструирования в FactoryBean
```

## Шаги реализации функционала в Spring
1. Создать интерфес с ф-цией
2. Создать его реализация
3. Создать конфиги Spring (e.g. xml или аннотации)
4. приложение использующее ф-цию интерфеса

## life cycle
Note: есть еще интерфейс BeanFactoryAware, но он немного не то.

Note: у бинов prototype НЕ вызывается метод с анотацией @PreDestroy. НО вызывает @PostConstruc

**BeanPostProcessor** interface часть жизненного цикла, но используется и чтобы расширить функциональность самих модулей. Нужно наследовать и переопределить метод. напр.:
* CommonAnnotationBeanPostProcessor
* RequiredAnnotationBeanPostProcessor
* AutowiredAnnotationBeanPostProcessor

**BeanPostProcessor** - то место где бины связываются
    (напр @Autowired через AutowiredAnnotationBeanPostProcessor)

**BeanPostProcessor vs BeanFactoryPostProcessor**
1. **BeanFactoryPostProcessor** - вызывает переопределлный метод postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory), когда все определения бина загружены, но сам он не создан. Можно перезаписывать properties бина даже если бин eager-initializing. В этом случае есть доступ ко все бинам из контекста.
2. **BeanPostProcessor** - вызывается когда все определения бина уже загружены и сам бин только что создан Spring IoC -ом. (он наследуется самим классом бина???)

**Последовательность вызовов:**
1. `BeanPostProcessor.postProcessBeforeInitilazation()`
2. ` @PostConstruct or InitializingBean.afterPropertiesSet() or init-method`
3. `BeanPostProcessor.postProcessAfterInitialization()`
4. `@PreDestroy or DisposibleBean.destroy() or destroy-method`

**@PostConstruct** - в отличии от конструктора вызван когда зависимости заинжекчены.
**afterPropertiesSet** и destroy более завязаны на Spring в отличии от...

**Вызовы init:**
1. constructor
2. @PostConstruct
3. InitializingBean.afterPropertiesSet()
4. initMethod() аннотации @Bean

**Вызовы destroy:**
1. @PreDestroy
2. DisposibleBean.destroy()
3. destroyMethod() аннотации @Bean

**Реализация своего life cycle:**
 1. Наследовать Lifecycle и/или Phased
 2. Переопределить методы


```java
public interface Lifecycle {
    void start();
    void stop();
    boolean isRunning();
}

public interface LifecycleProcessor extends Lifecycle {
    void onRefresh();
    void onClose();
}

public interface SmartLifecycle extends Lifecycle, Phased {
    boolean isAutoStartup();
    void stop(Runnable callback);
}
```

## bean circular dependencies

**bean circular dependencies** - когда бины инжектятся бесконечно и рекурсивно, может случится если inject в **constructor**, тогда связывание и создание происходит на этапе запуска контекста Spring.

**Чтобы избежать, варианты:**
1. связывать fields, а не constructor
2. использовать @Lazy: `f1(@Lazy MyBean myBean){}`
3. реструктурировать код так чтобы circular dependencies не было

Название exception которое может выпасть при этой ошибке:

## Интернациализация
файл конфигурации интернациализации по умолчанию `src/main/resources/messages.properties`

Добавить бин конфигов **CookieLocaleResolver**, чтобы локаль сохранялась в куки и передавалась на сервер.
```java
@Bean
MessageSource messageSource() {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setBasename("messages");
    return messageSource;
}
```

## ContextLoaderListener
**ContextLoaderListener** - слушатель из `web.xml` (или Java Config альтернативы), который запускает или останавливает **WebApplicationContext** если Spring используется в качестве веб приложения.

**ApplicationContext** запускается вручную или через **ContextLoaderListener** в JavaEE (MVC), с передачей ему пути к конфигурации (xml или `@ComponentScan`)

**ContextLoaderListener из Spring делает:**
1. Привязывает **lifecycle ApplicationContext** к **lifecycle ServletContext**.
2. Автоматически создает (запускает) **ApplicationContext**

Создаенный через **ContextLoaderListener** объект **WebApplicationContext**
дает доступ к **ServletContext** через **ServletContextAware**
и его метод `getServletContext()`

**Пример web.xml переопределения пути к /WEB-INF/applicationContext.xml:** (в примере параметр `contextConfigLocation` используется чтобы переопределить путь к конфигам по умолчанию):
```xml
<listener>
    <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
</listener>

<context-param>
    <param-name>contextConfigLocation</param-name>
    <param-value>
        /WEB-INF/application-dao.xml
        /WEB-INF/application-web.xml
    </param-value>
</context-param>
```

## ServletContainerInitializer и как он взаимодействует со Spring
* **ServletContainerInitializer** (SCI) из Java EE
* **AbstractAnnotationConfigDispatcherServletInitializer** (из Spring, наследует SCI)
* **WebApplicationInitializer** (из Spring, наследует SCI)

**ServletContainerInitializer** (SCI) используется для динамической загрузки
компонентов (альтернатива для web.xml).
Базируется на SPI и имеет метод `void onStartup(Set<Class<?>> c, ServletContext ctx)`.
<br>
**Чтобы SCI авто подхватилось** нужно положить его наследника в txt файл:
`META-INF/services/javax.servlet.ServletContainerInitializer`
и в этом классе прописать пути к классам SCI (пакет+имя).

Начиная с Servlet 3.0 конфиги `web.xml` могут быть в `META-INF/web-fragment.xml` при использовании web fragments (для работы без `web.xml`)

**WebApplicationInitializer** можно рассматривать как аналог web.xml через него можно регистрировать сервлеты.
Его методы: `onStartup(ServletContext servletContext)`

**@HandlesTypes** добавляет .class первым параметром onStartup()
через Reflection API можно создать `MyClass.class.newInstance()`
внутри `onStartup()`, а потом добавить туда созданный `new MyClass();`

**Пример ServletContainerInitializer (Java EE):**
```java
@HandlesTypes({Page.class}) // добавляет этот .class первым параметром onStartup()
public class AppInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> pageClasses, ServletContext ctx){
        ServletRegistration.Dynamic registration =
                ctx.addServlet("appController", AppController.class);
        pages.forEach(p -> registration.addMapping(p.getPath()));
    }
}
```

**Пример WebApplicationInitializer (Spring):**
```java
public class SpringWebAppInitializer implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext appContext = new AnnotationConfigWebApplicationContext();
        appContext.register(ApplicationContextConfig.class);
 
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("SpringDispatcher",
                new DispatcherServlet(appContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }
}
```

**Источники:**
* http://samolisov.blogspot.com/2016/01/spring-framework.html
* https://stackoverflow.com/questions/26676782/when-use-abstractannotationconfigdispatcherservletinitializer-and-webapplication/26676881#26676881
https://dzone.com/articles/understanding-spring-web

## Когда использовать WebApplicationInitializer или AbstractAnnotationConfigDispatcherServletInitializer
**Когда использовать WebApplicationInitializer (Spring 3.1+) или AbstractAnnotationConfigDispatcherServletInitializer (Spring 3.2+):**
1. SpringServletContainerInitializer находит классы которые implements WebApplicationInitializer
2. WebApplicationInitializer - интерфес, 
3. AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer
    - это рекомендуемый путь, через него можно стартовать servlet application context и/или root application context
    - ПРЕИМУЩЕСТВО: не нужно вручную настраивать DispatcherServlet и ContextLoaderListener
    
**WebMvcConfigurerAdapter** (и прочие `...Adapter`) - для конфигурации приложения
<br>
**vs AbstractAnnotationConfigDispatcherServletInitializer** - для bootstraping (загрузки) приложения

```java
public class MyWebApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfig.class};
    }
    protected Class<?>[] getServletConfigClasses()  {
        return new Class[] {WebConfiguration.class};
    }
    protected String[] getServletMappings() {
        return new String[] {"/"};
    }
}
```

## События
**Чтобы работать с событиями нужно:**
1. Создать событие MySpringEvent extends ApplicationEvent
2. заинжектить ApplicationEventPublisher и опубликовать
   @Autowired var applicationEventPublisher;
   applicationEventPublisher.publishEvent(mySpringEvent);
3. Альтернатива: implements ApplicationEventPublisherAware
4. ИЛИ слушатель должен реализовать ApplicationListener
       и метод onApplicationEvent(MySpringEvent event)
5. ИЛИ иметь аннтацию (в новых версиях)
   @EventListener(condition = "#event.success")

**Пример ApplicationEvent:**
```java
// 1. Создаем:
public class CustomSpringEvent extends ApplicationEvent {
    private String message;

    public CustomSpringEvent(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}
// 2. публикуем
@Component
public class AnnotationDrivenContextStartedListener {
    // @Async
    @EventListener(condition = "#event.success")    // только для успешных событий
    public void handleContextStart(ContextStartedEvent cse) {
        System.out.println("Handling context started event.");
    }
}
```

Существует много готовых событий: ContextRefreshedEvent, ContextStartedEvent, RequestHandledEvent etc

**Пример:**
```java
public class ContextRefreshedListener 
implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent cse) {
        System.out.println("Handling context re-freshed event. ");
    }
}
```

**Привязываем событие к транзакции:**
```java
@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
public void handleCustom(CustomSpringEvent event) {
    System.out.println("Handling event inside a transaction BEFORE COMMIT.");
}
```

## Spring Aware Interfaces

**Spring Aware Interfaces** - наследуем и переопределяем метод нужного события, касается напр. модулей для чьей конфигурации нужно наследовать Aware и переопределить.

**Суть:** в реализованные методы этих интерфейсов передаются разные объекты,
    связанные с контекстом: имя бинов, фабрика которая создает конкретный бин и пр.
    
**Примеры:** BeanNameAware, BeanFactoryAware, ApplicationContextAware etc

## Выполнение операций при деплое приложения Spring (метод run())
Выполнение операций при деплое приложения Spring (метод run()):
```java
@Component
public class AppRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {}
}
```

# Spring MVC
## Spring MVC Interceptor
**Spring MVC Interceptor** - это аналог Servlet Filter

**Методы: preHandle(), postHandle(), afterCompletion()**
<br>

**Для реализации нужно:**
1. extends HandlerInterceptor
2. ИЛИ implements HandlerInterceptorAdapter

**Методы:**
* **preHandle()** - вернет true / false (передать запрос дальше или нет)
* **postHandle(..., Model model)** - последний параметр model из view
* **afterCompletion()** - выполняется после всего в том числе работы view

**1. Пример создания Interceptor**
```java
@Component
public class ProductServiceInterceptor implements HandlerInterceptor {
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      return true;
   }
}
```

**2. Пример регистрации Interceptor:**
```java
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
            .addPathPatterns("/admin/*")
            .excludePathPatterns("/admin/oldLogin");
    }
}
```

## Обычные Filter из java ee в Spring
Кроме Interceptor в Spring Boot можно регестрировать обычные фильтры

```java
@Component
@Order(1)
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {}
}
```

```java
// регистрируем
@Bean
public FilterRegistrationBean<MyFilter> loggingFilter(){
    var registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new MyFilter());
    registrationBean.addUrlPatterns("/users/*");
         
    return registrationBean; 
}
```

```java
// старый способ регистрации в Spring (ИЛИ через web.xml)
public class MyWebInitializer extends
            AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{new ErrorHandleFilter()};
	}
}
```

## Spring MVC

**Путь запроса Spring MVC:**
    браузер > DispatcherServlet (сервлет, еще называется front controller) >
    Controller (возвращает имя view прикрепленное к model, в model можно сетать данные) >
    ViewResolver (сюда request доставляет model из Controller) >
    Сформированный view (страницу jsp или др.) и упаковать ее с model (шаблонизировать)

**View** класс - обертка вокруг шаблонизатора добавляющая в страницу ссылки на служебные бины + model (bean, cookie, request etc)

**Resolvers:**
* **CommonsMultipartResolver** (или др. реализация MultipartResolver) - extends ViewResolver, нужно для upload файлов
* Сам класс **MultipartResolver**, а не сторонние реализации доступен с Servlet 3.0. Его можно объявить как @Bean:
  * `<bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"></bean>`
  
**Для отправки файла использовать:**
```html
<form method="post" action="/form" enctype="multipart/form-data">
    <input type="file" name="file"/>
</form>
```
И перехват (для StandardServletMultipartResolver из Spring):
```java
@RequestMapping(value="/someUrl", method = RequestMethod.POST)
public String onSubmit(@RequestPart("meta-data") MetaData metadata,
    @RequestPart("file-data") MultipartFile file) {}
```
И перехват (для javax.servlet.http.Part из Java EE):
```java
@RequestMapping(value = "/form", method = RequestMethod.POST)
public String handleFormUpload(@RequestParam("name") String name,
    @RequestParam("file") Part file) {}
```

**HandlerMapping** - наследники этого класса использует **DispatcherServlet**
чтобы решить к какому Controller (и методу) отправить запрос

**HandlerMapping vs ViewResolver**
    - HandlerMapping привязывает к ссылке контроллер,
        а ViewResolver привязывает View (страницу)

**Пример 1:**
```java
@Bean("/welcome")
public BeanNameHandlerMappingController beanNameHandlerMapping() {
    return new BeanNameHandlerMappingController();
}
```
**Пример 2 (более полный конфиг): см. ниже**
<br>
**Пример 3 (конфиги путей через Adapter, ВОЗМОЖНО не HandlerMapping):**
```java
@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer{
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
```

## Spring MVC
**Порядок шагов включения:**
1. добавить (`@EnableWebMvc` устанваливает DispatcherServlet): 
    @EnableWebMvc
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {}
2. Выбрать ViewResolver - соотв. url и шаблонов (путь к файлу), в том числе для BlaBlaMulrtipartBla для upload бинарных файлов
3. Выбрать View подходящий к шаблонизатору
4. `@EnableSpringDataWebSupport` включает Spring HATEOAS
5. Включить транзакции:
    1. `@EnableTransactionManagement`
    2. `@Bean PlatformTransactionManager transactionManager(){} // конфиги транзакции`
    3. `@Bean sessionFactory() // из hibernate, если нужно для transactionManager`
    4. `@Bean DataSource dataSource(){}`
6. Над методами @Controller поставить @RequestMapping или аналог @PostMapping/@GetMapping/etc
7. Для проверки на ошибки можно использовать Hibernate Validator:
    ```java
    String addSpitterFromForm(@Valid Spitter spitter, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) { // Проверка ошибок
            return "spitters/edit";
        }
        // Сохранить объект Spitter
        spitterService.saveSpitter(spitter);
        // Переадресовать
        return "redirect:/spitters/" + spitter.getUsername(); 
    }
    ```

**Note:** можно вернуть:
    new ResponseEntity(mRs, HttpStatus.OK)
    - где первый параметр это Entity, а второй это status запроса

**Аннотации:**
1. `@ModelAttribute` - доступ к элементу который УЖЕ в model в @Controller
    1. НАД методом @Controller и тогда return значение попадает в model
        @ModelAttribute("vehicle") Vehicle getVehicle() {}
    2. Перед ПАРАМЕТРОМ метода @Controller, если у него кастомное имя
        void post(@ModelAttribute("vehicle") Vehicle vehicleInModel) {}
2. @`CrossOrigin` - для настройки CORS

**ModelAndView** использует ModelMap, которая использует Map.
<br>
Этот объект передается во View и генерирует ключи Map сам, на основе имени добавленного объекта.

```java
// объявление статических ресурсов
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/public-resources/").setCachePeriod(31556926);
    }
    @Override // аналог ParameterizableViewController
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
    }
}

// установка view resolver через ViewResolverRegistry
@Configuration
@EnableWebMvc
public class MvcWebConfig implements WebMvcConfigurer {
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.jsp("/WEB-INF/views/", ".jsp");
    }
}

// ParameterizableViewController (создание @Controller вручную)
@Configuration
@SpringBootApplication
public class Main {
    @Bean
    public ParameterizableViewController myViewController () {
        ParameterizableViewController c = new ParameterizableViewController();
        c.setViewName("myView");
        c.setStatusCode(HttpStatus.OK);
        return c;
    }
    
    @Bean
    public HandlerMapping myHandlerMapping () {
        SimpleUrlHandlerMapping m = new SimpleUrlHandlerMapping();
        Map<String, Object> map = new HashMap<>();
        map.put("/test", myViewController());
        m.setUrlMap(map);
        m.setOrder(1);
        return m;
    }
}

// создание ViewResolver вручную
@Configuration
public class MvcConfiguration {
    @Bean
    public InternalResourceViewResolver getViewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/views/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }
}
```

**HttpMessageConverter** - его наследники конвертят body запроса в параметры сервлета (из и в).
<br>
Напр. MappingJackson2HttpMessageConverter для Jackson

```java
@Configuration
@EnableWebMvc
public class ApplicationConfig extends WebMvcConfigurerAdapter { 
 @Override
 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // конфиги всех converters
 } 
}
```

# AOP
## Общее
**AOP** - разбиение программы на модули применимые во многих местах

**Spring AOP** - proxy-based фреймворк

**Как работает:** через proxy объект. Если для объекта нужно AOP. То Spring создает для него proxy и возвращает его вместо объекта. Этот proxy может выполнить Advice перед выполнением метода целевого объекта. Поэтому у Spring AOP есть ограничения и он может быть применен только в контексте.

**Ограничения в Spring AOP:** аспекты не применяются к другим аспектам.

**Понятия Spring AOP:**
* **weaving** - вставка аспекта в точку кода.
    * Может быть при: компиляции, выполнении, во время загрузки класса load time weaving (LTW) для AspectJ
    (Spring AOP работает только для method invocation типа)
* **introduction** - внедрение
* **target** - изменяемый объект

**типы weaving в AOP:**
1. **compile time** (AspectJ compiler) - вызовы создаются на этапе компиляции, это дает вызов функции прокси даже если его делает другая функция прокси из того же класса (в отличии от случая с **runtime**)
2. **load time** (AspectJ compiler)
3. **runtime** (CGlib или JDK dynamic proxy) - прокси паттерн в который обернуты **вызовы** функций, особенность: если вызвана функция прокси объекта и внутри нее вызвана другая функция прокси объекта, то эта другая функция не вызовется, т.к. вызов будет происходить внутри прокси и следовательно другая функция вызовется из самого объекта, а не прокси и обернута в AOP не будет (**это следствие из самого паттерна proxy**)

* **JDK dynamic proxy** (по умолчанию в старых версиях Spring) - работает если объект реализовывает хотя бы 1 интерфейс у объекта, прокси создается на основе используемых объектом интерфесов. Могут перекрывать только public методы. Проксирует только **public** методы.
* **CGLib proxy** (по умолчанию в новых версиях Spring) - если интерфесы не реализовываются. Проксирует **public**, **protected** и **package** методы. Если мы явно не указали для среза (pointcut) ограничение «только для public методов», то потенциально можем получить неожиданное поведение.

**Spring AOP** - использует **runtime weaving**.

**Понятия классов и средств:**
1. **@Aspect** - класс который будет применен (т.е. его методы)
    (класс со сквазной функциональностью)
    * **Note:** @Aspect внутри себя содержит @Pointcut
2. **@Pointcut** - где будет примен родительский ему @Aspect,
    содержит pattern == класс + методы к которым будет применен аспект
    (к каким пакетам и методам будет применен)
    * **Join Point** - конкретное место (метод) для которого будет выполняется @Aspect
    * **Note:** Join Point это конкретное место, а @Pointcut это набор таких мест
3. **Advice** - код аспекта, который будет выполняться в местах подключения
    указывает и КОГДА будет выполняться код (after, before, ...)

**Основные атрибуты аннотации @Pointcut:**
* `execution` - паттерн методов
* `within` - к каким типов классов
* `target` - 
* `this` - на чем вызывается метод
* `args` - 

**@target** - ссылка на сам объект (внутри proxy)
<br>
**this** - ссылка на AOP proxy в который обернут объект

**Типы advices:** `@Before`, `@After`, `@Around` == @Before + @After, `@AfterReturning`, `@AfterThrowing`

**Внедрений бинов создастся столько сколько instances бинов создано** (для singleton 1 раз, для prototype много)
<br>
Сам Proxy ничего не вызывает, он содержит цепочка interceptors.

Не зависимо от того, попадает или нет каждый конкретный метод целевого объекта под действие аспекта, его вызов проходит через прокси-объект.
<br>
(**т.е.** AOP будет действовать на все методы цели-класса даже если в аспекте указан только 1ин метод)

Будет создан как минимум один инстанс класса аспекта. (этим можно управлять)

**Пример:**
```java
@Around("trackTimeAnnotation()")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
    Object retVal = joinPoint.proceed();
    return retVal; // @Around обязательно вернуть, иначе значение может быть потеряно
}
```
```java
@Aspect // методы этого класса будут применены
@Component
class A {
    @Pointcut("execution(* *.f2(..))") // к чему применить
    private void f1(){} // должен быть void???
}

class B {
    @Before("f1()") // advice, когда и что применить
    void f2(JoinPoint joinPoint) {} // JoinPoint joinPoint НЕ ОБЯЗАТЕЛЕН
    
    // получаем вернутое значение
    @AfterReturning(poincut = "f1()", returning="retVal")
    void f2(Object retVal) {} // retVal НЕ ОБЯЗАТЕЛЕН
}
```

## Если нам все-таки надо добиться, что бы в случае Spring AOP код аспекта выполнялся при вызове proxy метода из другого proxy метода
**Варианты:**
1. надо писать код, так что бы обращения проходили через прокси-объект (В документации написано, что это нерекомендуемое решение).
2. заинжектить сервис сам в себя (@Autowired private MyServiceImpl myService;) и использовать метод myService.
    (ТОЛЬКО для scope = singleton, для prototype вызовет БЕСКОНЕЧНО внедрение зависимости и приложенка не запустится)

# Spring Boot
Работа Spring Boot начинается с запуска SpringApplication, который запускает ApplicationContext:
```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

# Spring Annotations
## Общее
* **@SpringBootApplication включает:** @Configuration, @EnableAutoConfiguration, @EnableWebMvc, @ComponentScan
* **@EnableAutoConfiguration** - пытается угадать и создать конфиги: DataSource, EntityManagerFactory, TransactionManager etc
* **@ComponentScan** - импортит все @Configuration классы своего пакета + указанные внутри нее, минус (exclude = Blabla.class)

**Stereotyping Annotations это:** @Component, @Controller, @Repository, @Service

**Стандартны:**
* JSR-250 - для JSE и JEE, такие как: : `@Resource, @PreDestroy, @PostConstruct, @RolesAllowed, @PermitAll, @DenyAll` etc
* JSR-299 - из Contexts and Dependency lnjection for the Java ЕЕ Platform 
* JSR-330 - из пакета `javax.inject.*`

**Spring annotation vs JSR-330 (javax.inject.*)**
* @Autowired vs @Inject - @Inject не имеет параметра required
* @Component == @Named
* @Scope("singleton") == @Singleton - в JSR-330 по умолчанию prototype
* @Qualifier == @Named
* @Value, @Required, @Lazy - в JSR-330 нет аналога

**Список популярных аннотаций:**
* `@Bean({"name1", "name2"})`
* `@PropertySource("classpath:app.properties")` - properties; переменная: @Autowired Environment env;
* `@Conditional` - по выражениею с true/false в нем включает или выключает @Bean
* `@Profile({"p1", "!p2"})` - над методами, задает профиль
* `@Scope("область_видимости")` - отдельные варианты: @SessionScope, @RequestScope, @ApplicationScope
    (других отдельных напр. для singleton нету, другие scope задаются параметрами @Scope аннотации)
* `@Import(AnotherConfiguration.class)`
* `@ImportResource("classpath:/lessons/xml-config.xml")` - конфиги бинов из xml
* `@Lazy` - по умолчанию eager для singleton, остальное lazy; рекомендуют eager т.к. ошибки видны сразу (а не через дни...)
* `@Autowired(required = false)` - рекомендуется @Required вместо этого
* `@Qualifier("main")` - по имени, если типы совпадают чтобы не получить `NoUniqueBeanDefinitionException`, если стоит над классом, то работает как назначение имени, прим: `@Component("fooFormatter")`
* `@Required` - к setter, что bean должен быть обязательно
* `@Value("${jdbc.url}")` - внедряет значение (как константа напр. properties)
* `@Resource(name= "map")` - позволяет внедрять коллекции, в отличии от @Autowired, которая вместо коллекций пытается внедрить коллекции бинов из контейнера. **Другими словами:** связывание по имени бина, а не типу (как @Autowired или @Inject).
* `@NonNull` - Spring в случае правильного определения класса, но при ошибках может заинжектить `null`, эта аннотация говорит не использовать `null`, может быть применена на **field**, **method parameter**, **method return value**. С этой аннотацией можно распознать проблемы.
* `@Nullable` - можно применить например чтобы исключить проверку на `null` полей пакета помеченного `@NonNullFields`
* `@NonNullFields` - применяется на всем пакете в файле `package-info.java`, говорит что все поля в пакете неявно `@NonNull`, применяется к **field**
* `@NonNullApi` - как `@NonNullFields`, но применяется к **method parameter**, **method return value**
* `@Primary` - если типы 2х бинов совпадают, то отмеченный этой аннотацией свжется by default и не будет ошибки (остальные `@Qualifier`). Если стоит обе аннотации, то у `@Qualifier` приоритет. Может быть проставлена и в **место инжекта**, и **над классом компонента** (чтобы сделать компонент инжекшеным by default)

**Note.** (@Qualifier vs Autowiring by Name) Если задать имя класса в camelCase, то при конфликте связывание аннотацией @Autowired будет по имени класса совпавшего с именем поля.

**@Resource vs @Autowired или @Inject:**
<br>
@Resource сначало связывает по имени, потом по типу. К ней тоже может быть применено ограничение @Qualifier.
@Autowired и @Qualifier часть Spring и не работают с другими фремворками, но это не важно т.к. перейти с 1го фреймворка на 2гой нереально.

**Обработка исключений:**
* `@ControllerAdvice` - контрлллер который перехватывает Exceptino глобально
* `@ExceptionHandler(SQLException.class)` - метод из @ControllerAdvice или @Controller для конкретного типа ошибок
* **Note.** В некоторых случаях рекомендуется возвращать @ResponseStatus(404) вместо прямого перехвата исключения (эта аннотация для метода контроллера).

Аннотации @Autowired, @Inject, @Resource и @Value обрабатываются Spring реализацией BeanPostProcessor, поэтому вы не можете их применять в своих собственных BeanPostProcessor и BeanFactoryPostProcessor, а только лишь явной инициализацией через XML или @Bean метод. 

# Spring Security
## Общее
SecurityContext сделан через ThreadLocal

Добавить `@EnableWebSecurity class Cfg extends WebSecurityConfigurerAdapter {}` альтернатива: подключить и настроить:
```xml
<filter-name>springSecurityFilterChain</filter-name>
<filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
```

`@EnableGlobalMethodSecurity(prePostEnabled = true)` включение AOP для Spring Data Rest (PreAuthorize/PreFilter/etc, JSR-250)
<br>
`@EnableWebSecurity` включает http.hasRole(...) и подобное


Настройка прав доступа:
```java
protected void configure(HttpSecurity http) {
    http.csrf().disable().authorizeRequests().antMatchers("/", "/list").hasRole("ADMIN")
        .and().formLogin().successHandler(mySuccessHandler).failureHandler(myFailureHandler)
        .and().logout().anyRequest().authenticated();
}
```

**Реализовать UserDitailsService и переопределить в нем loadUserByUsername(username):**
* в нем вытащить: user = usrRepository.get(username)
* и установить алгоритм хэширования: builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));

**Пример (прим. возможно как вариант можно просто создать @Bean UserDetailsService):**
```java
@Override
protected void configure(AuthenticationManagerBuilder auth)
throws Exception {
    auth.authenticationProvider(authenticationProvider()); // регистрируем DaoAuthenticationProvider
}

@Bean
public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService); // добавляем userDetailsService
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
}
```

**GrantedAuthority** это тоже что и Role, его часто используют чтобы взять role (`Set<GrantedAuthorities> roles = userDetails.getAuthorities()` )

**Взять данные о user (др. назв. principle):**
```java
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
String currentPrincipalName = authentication.getName();
UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // берем все данные
SecurityContextHolder.getContext().getAuthentication(); // из контекста
Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
// можно инжектить в метод Controller:
@ResponseBody public String currentUserName(@Param() String param, Authentication authentication) {}
```

**Проверка прав (аннотации для методов сервиса или контроллера):**
* `@PostFilter/@PreFilter` - допускает вызов метода, но фильтрует результат (напр отдадим админу только сообщение с матами)
* `@PreAuthorize("hasRole('ROLE_ADMIN')")` - можно вызвать, если указанное выражение в ней true
* `@PostAuthorize` -- можно вызвать, но если вернет false, то исключение
* `@Secured("ROLE_SPITTER")` == @RolesAllowed({"SPITTER"})
* `@PostAuthorize("returnObject.spitter.username == principal.username")` - проверка на доступа к данным только пользователя username
* `@PreAuthorize ("#book.owner == authentication.name")`

**Другой спосб:** сделать отдельный repository для работы с пользователями в Data REST (export = false), но не делать его публичным, а использовать как внутренний для других repository (как обертки для него). И в них проверять права. (ЭТО ДОГАДКА)

**Spring security настройка сессии:**
* **always** – a session will always be created if one doesn’t already exist
* **ifRequired** – a session will be created only if required (default)
* **never** – the framework will never create a session itself but it will use one if it already exists
* **stateless** – no session will be created or used by Spring Security

**Пример:**
```java
@Override
protected void configure(HttpSecurity http) {
    http
        // other config goes here...
        .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(2) // max одновременных сессий
            .expiredUrl("/sessionExpired.html") // on session timeout
            .invalidSessionUrl("/invalidSession.html") // wrong session
            .sessionRegistry(sessionRegistry());
}
@Bean // бин конфигов
public SpringSessionBackedSessionRegistry<S> sessionRegistry() {
    return new SpringSessionBackedSessionRegistry<>(this.sessionRepository);
}
```

**Timeout сессии:**
1. `server.servlet.session.timeout=60s` из Spring Boot
2. другие способы сводятся к получению session из ServletContext
    и прямой установке `getSession().setMaxInactiveInterval(15);`
    или в `web.xml`:
    ```xml
    <session-config>
        <session-timeout>20</session-timeout>
    </session-config>
    ```
    получить можно:
        `implements HttpSessionListener`, и различных Handler

## AuthenticationManagerBuilder vs HttpSecurity vs WebSecurity

configure(AuthenticationManagerBuilder) - можно добавить users и их пароли в in memory БД
configure(HttpSecurity) - для http
configure(WebSecurity) - глобально

# Spring Data REST
**Принципы:**
1. **@RequestBody** добавляет авто deserialization HttpRequest body в Java object:
    ```java
    @PostMapping("/request")
    public ResponseEntity post(@RequestBody LoginForm loginForm) {
        exampleService.fakeAuthenticate(loginForm);
        return ResponseEntity.ok(HttpStatus.OK);
    }
    ```
2. **@ResponseBody** делает обратное и кладет в HttpResponse body:
    ```java
    @PostMapping("/response")
    @ResponseBody
    public ResponseTransfer get() {
        return new ResponseTransfer("Thanks For Posting!!!");
    }
    ```
3. **@RestController** == @Controller + @ResponseBody, после этой аннотации МЕТОДЫ контроллера не нужно помечать как @ResponseBody, можно просто return myObj и myObj сам преобразуется в JSON

# Spring Data JPA + REST
Базовый путь: `spring.data.rest.basePath=/api`

**Настройка через одно из:**
1. `@Bean public RepositoryRestConfigurer repositoryRestConfigurer() {}`
2. Или реализацию класса:
```java
@Component
public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {}
```

**Spring Data REST не знает о abstract классах и interface, поэтому нужно их конфигурить через (это часть jackson):**
```java
configureJacksonObjectMapper(ObjectMapper objectMapper) {
    objectMapper.registerModule(new SimpleModule("MyCustomModule") {}
```

**Можно подключить Serializers**, если нужно сериализировать или десериализировать по особому:
    `public void setupModule(SetupContext context) {}`

**Классы repository:**
* `CrudRepository`
* `PagingAndSortingRepository<T, ID>`
* `JpaRepository`

```java
// делаем невидимым по ссылка, напр. чтобы защитить важную инфу. и используем в др. repo
@RepositoryRestResouce(exported = false)
```

```java
// объявление и подключение
@Projection(name = "inlineAddress", types = { Person.class }) 
interface InlineAddress {
    String getFirstName();
    String getLastName();
    Address getAddress(); 
}
```

```java
@RepositoryRestResource(excerptProjection = InlineAddress.class) // привязываем
interface PersonRepository extends CrudRepository<Person, Long> 
```

```java
// События привязанные к действиям над Entity:
@RepositoryEventHandler(Author.class) 
```
```
// @NoRepositoryBean Проставляется над базовым классом CrusRepository, чтобы не создалась его сущность
```
```java
// 1. помечаем repository
// Отмечает repository:
//        collectionResourceRel   - имя сгенерированных links (users)
//        path                    - путь в url (users)
//        itemResourceRel         - имя 1ой сущности (user)
@RepositoryRestResource(collectionResourceRel = "article", path = "article")

// 2. помечаем метод repository
@RestResource(path = "nameStartsWith", rel = "nameStartsWith")

// 3. если нужно создаем Controller
@RepositoryRestController
@RequestMapping(value = "/article")

// 4. метод Controller
@Transactional
@PostMapping(
        value = "/batch",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaTypes.HAL_JSON_VALUE
)
public @ResponseBody
ResponseEntity<?> createBatch(
        @RequestBody T entities[],
        Pageable pageable,
        PersistentEntityResourceAssembler assembler
) {}
```

# SpEL
```java
${...} is the property placeholder syntax. It can only be used to dereference properties.
#{...} is SpEL syntax, which is far more capable and complex. It can also handle property placeholders, and a lot more besides.
Both are valid, and neither is deprecated.
```

```java
#{5}
#{spitter.name}
#{songSelector.selectSong()?.toUpperCase()}     // если null, то просто не выполнится без ошибки
#{T(java.lang.Math).PI}   // T() возвращает Class и можно использовать его методы
#{circle.radius ^ 2} // степень
#{admin.email matches '[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.com'}     // регулярки
#{cities[2]}
#{cities['Dallas']} // из Map
#{settings['twitter.accessToken']}  // properties из <util:properties id="settings" location="classpath:settings.properties"/>
#{systemEnvironment['HOME']}    // системная переменная
#{systemProperties['application.home']} // параметры запущенной JVM из командной строки
#{cities.?[population gt 100000]}   // создает новую коллекцию из элементов проходящих по условию
#{cities.^[population gt 100000]}   // первый элемент из выборки
#{cities.$[population gt 100000]}   // последний элемент из выборки
<property name="cityNames" value="#{cities.![name]}"/>  // массив свойств с именем name будут присвоены переменной cityNames
<property name="cityNames" value="#{cities.![name + ', ' + state]}"/>   // тоже только группа:  «Chicago, IL», «Atlanta, GA» и «Dallas, TX»
<property name="cityNames" value="#{cities.?[population gt 100000].![name + ', ' + state]}"/>   // можно объединить 2е операции
```

# Resource из Spring
Прим. не разобрал тему, дописать

**Resource из Spring** - это то что можно вытащить из ServletContext или classpath для обычных сервлетов, низкоуровневый доступ из Spring к сервлетам
(напр. InputStreamReader для загрузки/выгрузки файла?)

`interface ResourceLoaderAware` обеспечивает объект ссылкой на `ResourceLoader`

Можно получить: `ResourceLoader.getResource(String location)`

1. UrlResource - является оберткой для java.net.URL
2. ClassPathResource - представляет ресурс, который может быть получен из classpath и поддерживает ресурсы как java.io.File
3. FileSystemResource - реализация для обработки java.io.File
4. ServletContextResource - реализация для обработки ServletContext ресурсов относительно корневой директории web-приложения.
5. InputStreamResource - реализация для обработки InputStream
6. ByteArrayResource - реализация для обработки массива байтов

# ---------- Ниже новое ----------

# Spring Core
## Почему нужно выбрать Spring?
**Spring** - начинался как Inversion of Control и сейчас используется в большей степени для этого.

**Плюсы:**
* Фреймворк помогает избавиться от повторяющегося кода
* Использует проверенные годами паттерны
* экосистема с готовыми компонентами

**Минусы:**
* Заставляет писать код в определенной манере
* Ограничивает версии языка и библиотек
* Дополнительное потребление ресурсов

**Модули:**
* Core - DI, Internationalisation, Validation, AOP
* Data Access - поддержка доступа к данным через JTA (Java Transaction API), JPA (Java Persistence API), and JDBC (Java Database Connectivity)
* Web - поддержка Servlet API (Spring MVC) и Reactive API (Spring WebFlux) и дополнительно поддерживаются WebSockets, STOMP, and WebClient
* Integration - поддержка интеграции с Enterprise Java через JMS (Java Message Service), JMX (Java Management Extension), and RMI (Remote Method Invocation)
* Testing - unit and integration testing через Mock Objects, Test Fixtures, Context Management, and Caching

**Spring Projects:**
* **Boot** - template код для быстрого разворачивания приложения в том числе с встроенными контейнерами приложений
* **Cloud** - легкая разработка с использованием паттернов распределенных систем (distributed system patterns) service discovery, circuit breaker, and API gateway
* **Security** - authentication and authorization, защита от session fixation, click-jacking, and cross-site request forgery
* **Mobile** - обнаружение и поддержка мобильных устройств, в том числе особая работа с view (видимо из MVC)
* **Batch**

## Annotations
**stereotype** - так еще называют аннотации Spring

**Note.** Под **конфликтом связывания** бинов понимается, когда или **типы бинов совпадают**, или связывание происходит с полем **ссылкой типа общего предка** (и тогда Spring не знает что выбрать). Чтобы решить такие конфликты используется: @Autowired вместе с @Qualifier, @Primary для связывания **по типу** или связывание **по имени** с @Autowired и именем поля как у класса.

Аннотации в пакетах `org.springframework.beans.factory.annotation` и `org.springframework.context.annotation` 

**Core annotations:**
* **DI-Related Annotations**
  * `@Component("fooFormatter")` - помечает класс как бин инстанс которого нужно создать, @Service и @Repository наследники @Component и Spring не смотрит на них самих, а только на @Component, когда регестрирует в ApplicationContext
  * `@ComponentScan("com.baeldung.autowire.sample")`
    * **в xml** можно использовать `<context:annotation-config/>` вместо этого
  * `@Service` - ничего не делает, просто отмечает бин как бизнес логику
  * `@Repository` - ловит persistence exceptions и делает rethrow их как Spring unchecked exception, для этого используется PersistenceExceptionTranslationPostProcessor (т.е. добавляется AOP обработчика исключений к бинам с @Repository)
  * `@Qualifier("main")` - связывание по **name** или **id** бина (видимо id это имя генерируемое автоматически, а name заданное), используется как пара к `@Autowired`, если типы совпадают чтобы не получить `NoUniqueBeanDefinitionException`, если стоит над классом, то работает как назначение имени, аналогично: `@Component("fooFormatter")` тоже что и `@Qualifier("fooFormatter")` над **классом**. Можно применять в **constructor параметре**, **setter параметре**, **над setter**, **над field**. Можно создать **свой вариант аннотации @Qualifier** проставив `@Qualifier` над созданной аннотацией.
    * `@Autowired Biker(@Qualifier("bike") Vehicle vehicle) {}`
    * `@Autowired void setVehicle(@Qualifier("bike") Vehicle vehicle) {}` - параметр set
    * `@Autowired @Qualifier("bike") void setVehicle(Vehicle vehicle) {}` - над методом set
    * `@Autowired @Qualifier("bike") Vehicle vehicle;`
  * `@Autowired` - Отмечает зависимость которую Spring будет resolve. Связывание **по типу по умолчанию**. `NoUniqueBeanDefinitionException` будет, если есть больше 1го кандидата на связывание и нет @Qualifier или @Primary. Если поле класса отмеченное @Autowired имеет имя такое как у связываемого бины, но в camelCase, то конфликта тоже не будет и связывание произойдет **по имени** (это fallback поведение Spring). Применяется к **constructor**, **setter**, or **field**. При использовании **constructor injection** (над конструктором) все аргументы конструктора обязательны. Начиная с Spring 4.3 ставить **@Autowired над constructors не обязательно**, но обязательно если конструкторов **больше 1го**, в классах @Configuration в этом случае constructor тоже может быть пропущен.
    * `@Autowired(required = true)` - атрибут **required = true** стоит by default, если зависимости нет при запуске Spring будет exception, если поставить в false, то exception не будет
    * `@Autowired Car(Engine engine) {}` - constructor
    * `@Autowired void setEngine(Engine engine) {}` - setter
    * `@Autowired Engine engine;` - field
  * `@Primary` - отмечает бин который будет выбран для авто связывания по умолчанию в случае конфликта. Если есть и @Qualifier, и @Primary, то **у @Qualifier приоритет**. Можно ставить рядом с @Bean или @Component (для класса)
  * `@Bean` - отмечает factory methode который создает bean. Это метода вызывается когда **bean зависимость запрошена** другим бином, имя бина такое как имя у factory method или указанное как `@Bean("engine")`. Все методы отмеченные `@Bean` должны быть в `@Configuration` классе. Если `@Bean` проставлена над конструктором, то inject происходит во время запуска контекста, а не по запросу этого бина как зависимости.
    * `@Bean({"name1", "name2"})`
    * `@Bean(name = "name1")`
    * `@Bean(value = "name1")`
  * `@Required void setColor(String color) {}` - отмечает зависимость (e.g. проставляется над set) которая описана в xml, без нее будет BeanInitializationException
  * `@Value` - делает inject файла или переменной property в поле бина. Применяется на **constructor**, **setter**, и **field**. Внутри можно использовать SpEL (выражения начинающиеся не с `$`, а с `#`). **для установки значения выражения в переменную (для работы с properties нужно указать @PropertySource):**
    * `Engine(@Value("8") int cylinderCount) {}`
    * `@Autowired void setCylinderCount(@Value("8") int cylinderCount) {}`
    * `@Value("8") void setCylinderCount(int cylinderCount) {}`
    * `@Value("8") int cylinderCount;`
    * `@Value("${engine.fuelType}") String fuelType;` - для файла `engine.fuelType=petrol`
    1. Выражение
        ```java
        @Value("${systemValue}")
        private String systemValue;
        ```
    2. Строка
        ```java
        @Value("string value")
        private String stringValue;
        ```
    3. Значение по умолчанию в случае ошибки:
        ```java
        @Value("${unknown.param:some default}")
        private String someDefault;
        ```
    4. Из системной переменной:
        ```java
        @Value("#{systemProperties['priority']}")
        private String spelValue;
        ```
    5.  Для Map:
        ```java
        @Value("#{${valuesMap}}")
        private Map<String, Integer> valuesMap;
        ```
  * `@DependsOn` - в ней можно указать имя зависимости бина, чтобы он загрузился до загрузки зависимого бина
    * `@DependsOn("engine") class Car implements Vehicle {}` - над классом указываем зависимость, которую нужно загрузить до класса. **Нужно** когда зависимость неявная, например JDBC driver loading или static variable initialization. В обычном случае Spring сам оприделяет последовать создания зависимостей.
    * `@Bean @DependsOn("fuel") Engine engine() {}` - над factory method зависимого бина.
    * `@Lazy` - отмечаем бины которые нужно создать lazily во время первого обращения к этому bean, by default они создаются eager во время запуска application context (**Note.** тут есть нюанс, что для field и setter они создаются при первом вызове? т.к. при constructor они создаются сразу при запуске контекста). Можно отмечать как `@Lazy(false)` чтобы переопределить глобальные значения и отключить lazy.
      * над `@Bean factory method` - влияет на этот метод
      * над `@Configuration class` - влияет на все методы класса
      * над `@Component class` - влияет на создание этого bean
      * над `@Autowired constructor, setter, field` - влияет на зависимость (via proxy)
    * `@Lookup` - 
    * `@Scope("prototype")` - обьявление scope над `@Component` или `@Bean`
* **Context Configuration Annotations** - конфигурирование application context
  * `@Profile("sportDay")` - отмечаем @Component или @Bean только если хотим, чтобы они создавались при определенном Spring профиле
  * `@Import(VehiclePartSupplier.class)` - отмечаем `@Configuration` и указываем внутри другой класс `@Configuration` чтобы импортировать один в другой
  * `@ImportResource("classpath:/annotations.xml")` - отмечаем `@Configuration`, импорт xml конфигов
  * `@PropertySource` - отмечаем `@Configuration`, после этого можно использовать property внутри класса и в аннотации `@Value`. Начиная с Java 8 эта аннотация Repeatable и можно над классом ставить несколько таких аннотаций.
  * `@PropertySources({@PropertySource("classpath:/annotations.properties"), @PropertySource("classpath:vehicle-factory.properties")})` - можно указать массив аннотаций `@PropertySource` внутри `@PropertySources`

**Spring MVC аннотации**
* `@RequestParam` - извлекает **parameters**, файлы etc из request
  * `@RequestParam(name = “id”) fooId` или `@RequestParam(value = “id”) fooId` или `@RequestParam(“id”) fooId` или `@RequestParam String fooId` - разные варианты
  * `@RequestParam(required = false) String id` - **required = false** чтобы не получить ошибку если параметра нет в request, если параметра нет, то будет установлен **null**
  * `@RequestParam(defaultValue = "test") String id` - установка значение по умолчанию, после этого **required=false** необязательно
  * `@RequestParam Map<String,String> allParams` - маппинг всех параметров
  * `@RequestParam List<String> id` - Multi-Value Parameter, когда один параметр может иметь несколько значений, например `http://localhost:8080/api/foos?id=1,2,3` **или** `http://localhost:8080/api/foos?id=1&id=2`
* `@PathVariable` - извлекает **URI path parameters** из request, при этом метод или класс `@Controller` должен быть отмечен `@GetMapping("/foos/{id}")` и адрес `http://localhost:8080/foos/abc`
  * `@PathVariable(required = false) String id` - аналогично как в `@RequestParam`, если параметра нет, то не будет ошибки и переменная будет **null**. **Note:** если использовать **required = false**, то **могут** возникнуть конфликты в путях
* `@PathVariable` vs `@RequestParam` - в адресе `@RequestParam` параметры **URL encoded**, т.е. спец. символы экранируются и после преобразования в String исчезнут, например `/foos?id=ab+c` будет `ab c`

## getBean()
**Имеет 5 сигнатур:**
* `(Tiger) context.getBean("lion");` - by name, нужно вручную использовать приведение типа
* `context.getBean("lion", Lion.class);` - by Name and Type
* `context.getBean(Lion.class);` - by Type
* `(Tiger) context.getBean("tiger", "Siberian");` - by Name with Constructor Parameters, 2ой параметр передается в конструктор
* `context.getBean(Tiger.class, "Shere Khan");` - by Type With Constructor Parameters

## What is a Spring Bean?
Spring bean - обьект который управляется Spring IoC container, создается им и управляется

**Inversion of Control** - процесс при котором обьект обьявляет свои зависимости без их явного создания (и IoC контейнер сам их подставляет)

Когда Spring создает обьекты они вызываются в порядке их обьявления в конфигурации.

Этапы создания spring beans:
1. Обьявить bean (**bean defenition**)
    ```java
    @Component
    public class Company {
        // this body is the same as before
    }
    ```
2. Создать **конфигурацию** бина (bean metadata)
    ```java
    @Configuration
    @ComponentScan(basePackageClasses = Company.class)
    public class Config {
        @Bean
        public Address getAddress() {
            return new Address("High Street", 1000);
        }
    }
    ```
3. Создать instance of **ApplicationContext**
    ```java
    ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
    Company company = context.getBean("company", Company.class); // проверяем
    ```

# Spring DI
## FactoryBean
Есть два типа бинов в Spring bean container, обычные бины и **factory beans**. **factory beans** могут создаваться сами, а не автоматически Spring фреймворком. Создать такие бины можно реализуя `org.springframework.beans.factory.FactoryBean`. **Используется** чтобы инкапсулировать сложную логику создания объекта.

```java
// интерфейс
public interface FactoryBean {
    T getObject() throws Exception;
    Class<?> getObjectType();
    boolean isSingleton();
}

// пример
public class ToolFactory implements FactoryBean<Tool> {
    @Override
    public Tool getObject() throws Exception {
        return new Tool(toolId);
    }
 
    @Override
    public Class<?> getObjectType() {
        return Tool.class;
    }
 
    @Override
    public boolean isSingleton() {
        return false;
    }
}
```
**Регистрируем через xml**
```xml
<beans ...>
    <bean id="tool" class="com.baeldung.factorybean.ToolFactory">
        <property name="factoryId" value="9090"/>
        <property name="toolId" value="1"/>
    </bean>
</beans>
```
**Регистрируем через аннотации**
```java
@Configuration
public class FactoryBeanAppConfig {
    @Bean(name = "tool")
    public ToolFactory toolFactory() {
        ToolFactory factory = new ToolFactory();
        factory.setFactoryId(7070);
        factory.setToolId(2);
        return factory;
    }
 
    @Bean
    public Tool tool() throws Exception {
        return toolFactory().getObject();
    }
}
```
Если нужно исполнить какие-то действия до `getObject()`, но после `FactoryBean`. Тогда нужно использовать `InitializingBean` или `@PostConstruct`.

**AbstractFactoryBean** - более удобный класс для реализации FactoryBean
```java
public class NonSingleToolFactory extends AbstractFactoryBean<Tool> {
    public NonSingleToolFactory() {
        setSingleton(false);
    }
 
    @Override
    public Class<?> getObjectType() {
        return Tool.class;
    }
 
    @Override
    protected Tool createInstance() throws Exception {
        return new Tool(toolId);
    }
}
```
Регистрация **AbstractFactoryBean**
```xml
<beans ...>
    <bean id="nonSingleTool" class="com.baeldung.factorybean.NonSingleToolFactory">
    </bean>
</beans>
```

# Spring MVC
