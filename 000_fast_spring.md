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
- [Spring MVC](#spring-mvc)
  - [Spring MVC Interceptor](#spring-mvc-interceptor)

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

# Spring MVC
## Spring MVC Interceptor
**Spring MVC Interceptor** - это аналог Servlet Filter

**Методы: preHandle(), postHandle(), afterCompletion()**
<br>
**Для реализации:**
<br>
1. extends HandlerInterceptor
<br>
2. ИЛИ implements HandlerInterceptorAdapter
    
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