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
  - [Обычные Filter из java ee в Spring](#Обычные-filter-из-java-ee-в-spring)
- [AOP](#aop)
  - [Общее](#Общее)
  - [Если нам все-таки надо добиться, что бы в случае Spring AOP код аспекта выполнялся при вызове proxy метода из другого proxy метода](#Если-нам-все-таки-надо-добиться-что-бы-в-случае-spring-aop-код-аспекта-выполнялся-при-вызове-proxy-метода-из-другого-proxy-метода)
- [Spring Boot](#spring-boot)
- [Spring Annotations](#spring-annotations)
  - [Общее](#Общее-1)

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

`@Value` - **для установки значения выражения в переменную (для работы с properties нужно указать @PropertySource):**
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
1. Из системной переменной:
    ```java
    @Value("#{systemProperties['priority']}")
    private String spelValue;
    ```
2.  Для Map:
    ```java
    @Value("#{${valuesMap}}")
    private Map<String, Integer> valuesMap;
    ```

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
* `@Qualifier("main")` - по имени
* `@Required` - к setter, что bean должен быть обязательно
* `@Value("${jdbc.url}")` - внедряет значение (как константа напр. properties)
* `@Resource(name= "map")` - позволяет внедрять коллекции, в отличии от @Autowired, которая вместо коллекций пытается внедрить коллекции бинов из контейнера. **Другими словами:** связывание по имени бина, а не типу (как @Autowired или @Inject).
* `@NonNull` - Spring в случае правильного определения класса, но при ошибках может заинжектить `null`, эта аннотация говорит не использовать `null`, может быть применена на **field**, **method parameter**, **method return value**. С этой аннотацией можно распознать проблемы.
* `@Nullable` - можно применить например чтобы исключить проверку на `null` полей пакета помеченного `@NonNullFields`
* `@NonNullFields` - применяется на всем пакете в файле `package-info.java`, говорит что все поля в пакете неявно `@NonNull`, применяется к **field**
* `@NonNullApi` - как `@NonNullFields`, но применяется к **method parameter**, **method return value**

**@Resource vs @Autowired или @Inject:**
<br>
@Resource сначало связывает по имени, потом по типу. К ней тоже может быть применено ограничение @Qualifier.
@Autowired и @Qualifier часть Spring и не работают с другими фремворками, но это не важно т.к. перейти с 1го фреймворка на 2гой нереально.

**Обработка исключений:**
* `@ControllerAdvice` - контрлллер который перехватывает Exceptino глобально
* `@ExceptionHandler(SQLException.class)` - метод из @ControllerAdvice или @Controller для конкретного типа ошибок
* **Note.** В некоторых случаях рекомендуется возвращать @ResponseStatus(404) вместо прямого перехвата исключения (эта аннотация для метода контроллера).

Аннотации @Autowired, @Inject, @Resource и @Value обрабатываются Spring реализацией BeanPostProcessor, поэтому вы не можете их применять в своих собственных BeanPostProcessor и BeanFactoryPostProcessor, а только лишь явной инициализацией через XML или @Bean метод. 

