-> Простое подключение сервлета:
@WebServlet(UrlPatterns = {"/index"}, , loadOnStartup = 1,
    initParams = {
        @WebInitParam(name = "host", value = "port"),
        @WebInitParam(name = "user", value = "email@mail.ru")
})
public class MyServlet extends HttpServlet {doPost(){} doGet(){}}

-> Простое подключение сервлета (xml):
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

-> Простое подключение фильтра:
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

-> Простое подключение фильтра (xml):
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

-> Просто подключение listener из Java EE:
(SessionListenerImpl наследует один из предопределенных классов)

<listener>
    <listener-class>by.bsu.control.listener.SessionListenerImpl</listener-class>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/xyz.xml</param-value>
    </context-param>
</listener

-> Просто подключение listener из Java EE (xml):
    @WebListener
    public class SessionListenerImpl implements HttpSessionAttributeListener {
        // переопределенные методы
    }
    
<error-page>
    <exception-type>javax.servlet.ServletException</exception-type>
    <location>/AppExceptionHandler</location>
</error-page>

ServletContext - один на всю приложенка, в <contex-param>
    через него можно получить доступ ко все сервлетам и программно регистрировать события, добавлять атрибуты, сервлеты
ServletConfig - у каждого сервлета свой, в <init-param>

Структура приложения Java EE:
src/main + java, resources, filters, webapp
src/test + ...
src/it - интеграционные тесты
src/assembly - Assembly descriptors
src/site - site
    
getServletContext().getRealPath(request.getServletPath()) - актуальный путь
getServletContext().getServerInfo() - инфа о сервере
request.getRemoteAddr()

wrappers:
HttpServletRequestWrapper и HttpServletResponseWrapper - расширить эти классы и переопределить только необходимые методы

20. Каков жизненный цикл сервлета и когда какие методы вызываются?
Загрузка класса сервлета — загрузка класса сервлета в память и вызов конструктора без параметров (при запросе или loadOnStratup)
Инициализация класса сервлета — инициализирует ServletConfig и внедряет его через init(). Это и есть место где сервлет класс преобразуется из обычного класса в сервлет.
Обработка запросов — Для каждого запроса клиента сервлет контейнер порождает новую нить (поток) и вызывает метод service() путем передачи ссылки на объект ответа и запроса.
Удаление из Service — контейнер останавливается или останавливается приложение, уничтожает классы сервлетов путем вызова destroy()

CDI — это Contexts and Dependency Injectio
---
    singleton - По умолчанию. Spring IoC контейнер создает единственный экземпляр бина. Как правило, используется для бинов без сохранения состояния(stateless)
    
    prototype - Spring IoC контейнер создает любое количество экземпляров бина. Новый экземпляр бина создается каждый раз, когда бин необходим в качестве зависимости, либо через вызов getBean(). Как правило, используется для бинов с сохранением состояния (stateful)
    
    request - Жизненный цикл экземпляра ограничен единственным HTTP запросом; для каждого нового HTTP запроса создается новый экземпляр бина. Действует, только если вы используете web-aware ApplicationContext
    
    session - Жизненный цикл экземпляра ограничен в пределах одной и той же HTTP Session. Действует, только если вы используете web-aware ApplicationContext
    
    global session - Жизненный цикл экземпляра ограничен в пределах глобальной HTTP Session (обычно при использовании portlet контекста). Действует, только если вы используете web-aware ApplicationContext
    
    application - Жизненный цикл экземпляра ограничен в пределах ServletContext. Действует, только если вы используете web-aware ApplicationContext
        как singleton, но привязан к ApplicationContext, а не ServletContext. Т.к. ApplicationContext может быть несколько на 1но приложение,
            т.к. у каждого DispatcherServlet может быть только 1ин ApplicationContext, но самих DispatcherServlet может быть много в приложении.
            
    websocket - 
    
    prototype vs singleton - @PreDestroy не вызвается для prototype, потому что бин не в контексте.
            Spring не удаляет prototype бины.
        Как вызвать destroy для prototype:
            1. BeanFactoryAware - получить в его методе бин и проверить BeanFactory.isPrototype, использовать в BeanPostProcessor ниже
            2. DisposableBean - реализуем тут destroy() который мы вызовем для уничтожения каждого бина
            3. BeanPostProcessor - в его наследнике можно создать List всех prototype и пройтись по ним уничтожив
            ПОЯСНЕНИЕ: мы наследуем ВСЕ эти интерфесы своему классу, который будет отслеживать prototype бины, добавлять в List и вызывать для каждого destroy()
            
        Пример:
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

--- custom scope, scopeName и proxyMode = ScopedProxyMode
эту тему описать тут

--- Spring Core
Цель - ослабление связей через DI и AOP и уменьшение количества кода. В качестве компонентов испольует POJO или JavaBean.
DI - взаимодействие с объектом через интерфейм, не зная о реализации. IoC один из видом DI, реализован через паттерн ServiceLocator.
Легко тестировать с DI, подставляя фективную реализацию.

DI - лучше
DL - нужно использовать, когда невозможно DI, напр. при инициализации через ApplicationContext автономного приложения (в случае MVC он сам все запускает)

1. BeanFactory - ядро DI (в т.ч. зависимости и life cycle). Позволяет РАЗДЕЛИТЬ конфиги и код.
2. ApplicationContext - расширение BeanFactory, кроме того содержит сервисы JMS, AOP, i18n, Event listeners, transactions, properties, MessageSource (для i18n) etc; регистрацию в BeanPostProcessor, BeanFactoryPostProcessor

Spring приложение можно создавать на основе ИНТЕРФЕСОВ и БИНОВ
(Spring Bean, не обязательно следовать JavaBean, особенно если DI через конструктор, т.к. не надо знать set/get)

spring context - конфигурационный файл о среде для Spring.
Spring Context - это IoC контейнер, создает, конфигурит бины читая мета инфу (конфиги).

IoC - это (3и вида внедрения зависимости: конструктор, сетер, интерфес (CDL)):
    1. Dependency lnjection - внедрение зависимостей, напр xml или аннотации; внедряется IoC контейнером автоматически
        1. Constructor Dependency Injection - Внедрение зависимостей через конструктор
        2. Setter Dependency Injection - Внедрение зависимостей через метод установки
    2. Dependency Lookup - поиск зависимостей, когда зависимость вручную достается из контейнера: SpringContext.lookup('Blabla')
        1. Dependency Pull - Извлечение зависимостей вручную (чаще всего, прим. выше для DL)
        2. Contextualized Dependency Lookup (CDL) - реализуем метод из интерфейса контейнера в классе в котором нужна зависимость, этот метод сам вызовется и сделает set() зависимости: прим. https://stackoverflow.com/a/42000709
        
IoC - дает службы для взаимодействия компонентов со своими зависимостями.

BeanFactory нужен если: использовать приложение совместно с либами JDK 1.4 или не поддерживают JSR-250.

Если вместе с BeanFactory нужно использовать AOP и транзакций то при использовании BeanFactory необходимо добавить вручную регистрацию BeanPostProcessor и BeanFactoryPostProcessor. Т.к. они только в ApplicationContext.

Типы ApplicationContext для разных типов конфигов:
    ClassPathXmlApplicationContext - конфиги из classpach
    FileSystemXmlApplicationContext - файл конфигов
    XmlWebApplicationContext - по умолчанию по пути "/WEB-INF/applicationContext.xml", или указать вручную (можно сразу несколько xml) добавив в web.xml слушатель ContextLoaderListener и параметр <context-param> с ключем contextConfigLocation и путями к xml
    
Пример:
    ConfigurableBeanFactory factory = new XmlBeanFactory(...);
    // теперь зарегистрируем необходимый BeanPostProcessor экземпляр
    MyBeanPostProcessor postProcessor = new MyBeanPostProcessor();
    factory.addBeanPostProcessor(postProcessor);
    // запускаем, используя factory
	
FactoryBean - интерфейс, который можно реализовать для конструирования сложных бинов и подключить к Spring.
Нужен в основном для xml конфигурации, для конфигов с аннотациями можно установить зависимые объекты прямо в коде:
	person.setCar(car); // устанавливаем там где инициализируем @Bean вместо конструирования в FactoryBean
---
Шаги реализации фции.: Spring: 1. интерфес с фцией, 2. его реализация, 3. конфиги Spring (e.g. xml), 4. приложение использующее фцию интерфеса
---
life cycle


---
circular dependencies
описать тут
Если коротко: может случится если inject в constructor, тогда связывание и создание происходит на этапе запуска контекста.
Чтобы избежать: связывать fields, а не constructor, или использовать @Lazy: f1(@Lazy MyBean myBean){}, или реструктурировать код так чтобы circular dependencies не было
Название exception которое может выпасть при этой ошибке: 
---
Интернациализация
(файл src/main/resources/messages.properties как пример)
    Добавить бин конфигов CookieLocaleResolver (чтобы локаль сохранялась в куки и передавалась на сервер)

    @Bean
    MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
---
ContextLoaderListener - слушатель из web.xml (или Java Config альтернативы)
который запускает или останавливает WebApplicationContext.

ApplicationContext запускается вручную или через ContextLoaderListener в JavaEE (MVC), с передачей ему пути к конфигурации (xml или @ComponentScan)

ContextLoaderListener из Spring делает:
1. Привязывает lifecycle ApplicationContext к lifecycle ServletContext.
2. Автоматически создает (запускает) ApplicationContext

Создаенный через ContextLoaderListener объект WebApplicationContext
дает доступ к ServletContext через ServletContextAware
и его метод getServletContext()

(в примере ниже параметр contextConfigLocation используется
чтобы переопределить путь к конфигам по умолчанию)
Пример web.xml переопределения пути к /WEB-INF/applicationContext.xml:
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
---
ServletContainerInitializer (SCI) из Java EE
AbstractAnnotationConfigDispatcherServletInitializer (из Spring, наследует SCI)
и
WebApplicationInitializer (из Spring, наследует SCI)

http://samolisov.blogspot.com/2016/01/spring-framework.html
https://stackoverflow.com/questions/26676782/when-use-abstractannotationconfigdispatcherservletinitializer-and-webapplication/26676881#26676881
https://dzone.com/articles/understanding-spring-web

ServletContainerInitializer (SCI) используется для динамической загрузки
компонентов (альтернатива для web.xml).
Базируется на SPI и имеет метод void onStartup(Set<Class<?>> c, ServletContext ctx).
Чтобы SCI авто подхватилось нужно положить его наследника в txt файл:
META-INF/services/javax.servlet.ServletContainerInitializer
и в этом классе прописать пути к классам SCI (пакет+имя).

Начиная с Servlet 3.0 конфиги web.xml могут быть
META-INF/web-fragment.xml

WebApplicationInitializer можно рассматривать как аналог web.xml
через него можно регестрировать сервлеты.
Его методы: onStartup(ServletContext servletContext)

@HandlesTypes добавляет .class первым параметром onStartup()
через Reflection API можно создать MyClass.class.newInstance()
внутри onStartup(), а потом добавить туда созданный new MyClass();

Пример ServletContainerInitializer (Java EE):
@HandlesTypes({Page.class}) // добавляет этот .class первым параметром onStartup()
public class AppInitializer implements ServletContainerInitializer {
    @Override
    public void onStartup(Set<Class<?>> pageClasses, ServletContext ctx){
        ServletRegistration.Dynamic registration =
                ctx.addServlet("appController", AppController.class);
        pages.forEach(p -> registration.addMapping(p.getPath()));
    }
}

Пример WebApplicationInitializer (Spring):
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
---
Когда использовать WebApplicationInitializer (Spring 3.1+) или AbstractAnnotationConfigDispatcherServletInitializer (Spring 3.2+):
0. SpringServletContainerInitializer находит классы которые implements WebApplicationInitializer
1. WebApplicationInitializer - интерфес, 
2. AbstractAnnotationConfigDispatcherServletInitializer implements WebApplicationInitializer
    - это рекомендуемый путь, через него можно стартовать servlet application context и/или root application context
    - ПРЕИМУЩЕСТВО: не нужно вручную настраивать DispatcherServlet и ContextLoaderListener
    
WebMvcConfigurerAdapter (и прочие) - для конфигурации приложения
    vs AbstractAnnotationConfigDispatcherServletInitializer - для bootstraping (загрузки) приложения

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
---
Spring MVC Interceptor - это аналог Servlet Filter

Методы: preHandle(), postHandle(), afterCompletion()
Для реализации:
    1. extends HandlerInterceptor
    2. ИЛИ implements HandlerInterceptorAdapter
    
preHandle() - вернет true / false (передать запрос дальше или нет)
postHandle(..., Model model) - последний параметр model из view
afterCompletion() - выполняется после всего в том числе работы view

1. Пример создания Interceptor
@Component
public class ProductServiceInterceptor implements HandlerInterceptor {
   @Override
   public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
      return true;
   }
}

2. Пример регистрации Interceptor:
@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AdminInterceptor())
            .addPathPatterns("/admin/*")//
            .excludePathPatterns("/admin/oldLogin");
    }
}
---
Кроме Interceptor в Spring Boot можно регестрировать обычные фильтры

@Component
@Order(1)
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, 
      ServletResponse response, 
      FilterChain chain) throws IOException, ServletException {}
}

// регистрируем
@Bean
public FilterRegistrationBean<MyFilter> loggingFilter(){
    var registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new MyFilter());
    registrationBean.addUrlPatterns("/users/*");
         
    return registrationBean; 
}

// старый способ регистрации в Spring (ИЛИ через web.xml)
public class MyWebInitializer extends
            AbstractAnnotationConfigDispatcherServletInitializer {
	@Override
	protected Filter[] getServletFilters() {
		return new Filter[]{new ErrorHandleFilter()};
	}
}
---
AOP - разбиение программы на модули применимые во многих местах

Spring AOP - proxy-based фреймворк

Как работает: через proxy объект. Если для объекта нужно AOP.
    То Spring создает для него proxy и возвращает его вместо объекта.
    Этот proxy может выполнить Advice перед выполнением метода целевого объекта.
    Поэтому у Spring AOP есть ограничения и он может быть применен только в контексте.
    
Ограничения в Spring AOP: аспекты не применяются к другим аспектам.

weaving - вставка аспекта в точку кода.
    Может быть при: компиляции, выполнении, во время загрузки класса load time weaving (LTW) для AspectJ
    (Spring AOP работает только для method invocation типа)
introduction - внедрение
target - изменяемый объект

weaving бывает: compile time (AspectJ compiler), load time, runtime.
Spring AOP использует runtime weaving.

1. @Aspect - класс который будет применен (т.е. его методы)
    (класс со сквазной функциональностью)

Note: @Aspect внутри себя содержит @Pointcut

2. @Pointcut - где будет примен родительский ему @Aspect,
    содержит pattern == класс + методы к которым будет применен аспект
    (к каким пакетам и методам будет применен)
    
Join Point - конкретное место (метод) для которого будет выполняется @Aspect

Note: Join Point это конкретное место, а @Pointcut это набор таких мест

3. Advice - код аспекта, который будет выполняться в местах подключения
    указывает и КОГДА будет выполняться код (after, before, ...)

Основные атрибуты аннотации:
    execution - паттерн методов
    within - к каким типов классов
    target - 
    this - на чем вызывается метод
    args - 
    
@target - ссылка на сам объект (внутри proxy)
this - ссылка на  proxy

advices: @Before, @After, @Around == @Before + @After, @AfterReturning, @AfterThrowing

@Around("trackTimeAnnotation()")
public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
    Object retVal = joinPoint.proceed();
    return retVal; // @Around обязательно вернуть, иначе значение может быть потеряно
}

Пример:
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
--- Следствие из Spring AOP

Пример вызовом методов внутри транзакции с propogation=REQUIRED_NEW:
«В связи с тем, что для поддержки транзакций через аннотации используется Spring AOP,
в момент вызова method1() на самом деле вызывается метод прокси объекта.
Создается новая транзакция и далее происходит вызов method1() класса MyServiceImpl.
А когда из method1() вызовем method2(), обращения к прокси нет,
вызывается уже сразу метод нашего класса и,
соответственно, никаких новых транзакций создаваться не будет».

Пример: если транзакция в том же классе вызывает метод с REQUIRED_NEW,
    то новая транзакция всеравно не создастся, т.к. вызов метода прокси паттерна из него же самого не создаст новый прокси и AOP создающее транзакцию не сработает.
    
То что внутри одного Proxy проксированный метод при вызове другого метода не проксирует этот другой следствие паттерна Proxy.
(т.е. AOP при вызове 2го метода на того же Proxy не сработает)

ИСКЛЮЧЕНИЕ: вызов метода Proxy в другом методе того же Proxy сработает (вызовет цепочку методов Proxy),
    ЕСЛИ включено связывание на этапе компиляции компилятором AcpectJ (Compile-time weaving и AspectJ)

Внутри Spring AOP используется прокси через JDK (JDK dynamic proxy) ИЛИ через CGLIB.
В случае JDK dynamic proxy прокси создается на основе используемых объектом интерфесов.
Кроме этого могут быть включены CTW (compile-time weaving) и LTW (load-time weaving).

JDK dynamic proxy - включается если объект реализовывает хотя бы 1 интерфес.
CGLib proxy - если интерфесы не реализовываются.

JDK dynamic proxy могут перекрывать только public методы. CGLib proxy – кроме public, protected и package-visible методы.
Соответственно, если мы явно не указали для среза (pointcut) ограничение «только для public методов», то потенциально можем получить неожиданное поведение.

Если нам все-таки надо добиться, что бы в рассматриваемом случае код аспекта выполнялся:
1. надо писать код, так что бы обращения проходили через прокси-объект (В документации написано, что это нерекомендуемое решение).
2. заинжектить сервис сам в себя (@Autowired private MyServiceImpl myService;) и использовать метод myService.
    (ТОЛЬКО для scope = singleton, для prototype вызовет БЕСКОНЕЧНО внедрение зависимости и приложенка не запустится)

Внедрений бинов создастся столько сколько instances бинов создано (для singleton 1 раз, для prototype много)
Сам Proxy ничего не вызывает, он содержит цепочка interceptors.

Не зависимо от того, попадает или нет каждый конкретный метод целевого объекта под действие аспекта, его вызов проходит через прокси-объект.
(т.е. AOP будет действовать на все методы цели-класса даже если в аспекте указан только 1ин метод)

Будет создан как минимум один инстанс класса аспекта. (этим можно управлять)
    
--- Spring Boot
Работа Spring Boot начинается с запуска SpringApplication, который запускает ApplicationContext:
    @SpringBootApplication
    public class Application {
        public static void main(String[] args) {
            SpringApplication.run(Application.class, args);
        }
    }
    
--- Spring Annotations

@SpringBootApplication включает: @Configuration, @EnableAutoConfiguration, @EnableWebMvc, @ComponentScan
@EnableAutoConfiguration - пытается угадать и создать конфиги: DataSource, EntityManagerFactory, TransactionManager etc
@ComponentScan - импортит все @Configuration классы своего пакета + указанные внутри нее, минус (excluce = Blabla.class)

Stereotyping Annotations это: @Component, @Controller, @Repository, @Service

@Value для установка значения выражения в переменную (для работы с properties нужно указать @PropertySource):
1. Выражение
    @Value("${systemValue}")
    private String systemValue;
2. Строка
    @Value("string value")
    private String stringValue;
3. Значение по умолчанию в случае ошибки:
    @Value("${unknown.param:some default}")
    private String someDefault;
4. Из системной переменной:
    @Value("#{systemProperties['priority']}")
    private String spelValue;
5.  Для Map:
    @Value("#{${valuesMap}}")
    private Map<String, Integer> valuesMap;

Аннотации
    JSR-250 (для JSE и JEE)
    vs
    JSR-299 (из Contexts and Dependency lnjection for the Java ЕЕ Platform)
    vs
    JSR-330 (javax.inject.*)
    
    
    ----
    JSR-250: @Resource, @PreDestroy, @PostConstruct, @RolesAllowed, @PermitAll, @DenyAll etc
    ----
    сравнение vs JSR-330 (javax.inject.*)
    ----
    @Autowired vs @Inject - @Inject не имеет параметра required
    @Component == @Named
    @Scope("singleton") == @Singleton - в JSR-330 по умолчанию prototype
    @Qualifier == @Named
    @Value, @Required, @Lazy - в JSR-330 нет аналога
    
    ----
    
    @Bean({"name1", "name2"})
    @PropertySource("classpath:app.properties") - properties; переменная: @Autowired Environment env;
    @Conditional - по выражениею с true/false в нем включает или выключает @Bean
    @Profile({"p1", "!p2"}) - над методами, задает профиль
    @Scope("область_видимости") - отдельные варианты: @SessionScope, @RequestScope, @ApplicationScope
        (других отдельных напр. для singleton нету, другие scope задаются параметрами @Scope аннотации)
    @Import(AnotherConfiguration.class)
    @ImportResource("classpath:/lessons/xml-config.xml") - конфиги бинов из xml
    @Lazy - по умолчанию eager для singleton, остальное lazy; рекомендуют eager т.к. ошибки видны сразу (а не через дни...)
    @Autowired(required = false) - рекомендуется @Required вместо этого
    @Qualifier("main") - по имени
    @Required - к setter, что bean должен быть обязательно
    @Value("${jdbc.url}") - внедряет значение (как константа напр. properties)
    @Resource(name= "map") - позволяет внедрять коллекции, в отличии от @Autowired, которая вместо коллекций пытается внедрить коллекции бинов из контейнера. Другими словами: связывание по имени бина, а не типу (как @Autowired или @Inject).
    
    @Resource vs @Autowired или @Inject:
    @Resource сначало по имени, потом по типу. К ней тоже может быть применено ограничение @Qualifier.
    @Autowired и @Qualifier часть Spring и не работают с другими фремворками, но это не важно т.к. перейти с 1го фреймворка на 2гой нереально.
    
    @ControllerAdvice - контрлллер который перехватывает Exceptino глобально
    @ExceptionHandler(SQLException.class) - метод из @ControllerAdvice или @Controller для конкретного типа ошибок
    В некоторых случаях рекомендуется возвращать @ResponseStatus(404) вместо прямого перехвата исключения (эта аннотация для метода контроллера).
    
Игнорит null???
void f1(@NonNull var event) {}

Аннотации @Autowired, @Inject, @Resource и @Value обрабатываются Spring реализацией BeanPostProcessor, поэтому вы не можете их применять в своих собственных BeanPostProcessor и BeanFactoryPostProcessor, а только лишь явной инициализацией через XML или @Bean метод. 
---
Путь запроса Spring MVC:
    браузер > DispatcherServlet (сервлет, наз. front controller) >
    Controller (возвращает имя view прикрепленное к model, в model можно сетать данные) >
    ViewResolver (сюда request доставляет model из Controller) >
    Сформированный view (страницу jsp или др.) и упаковать ее с model (шаблонизировать)
    
View класс - обертка вокруг шаблонизатора добавляющая в страницу ссылки на служебные бины + model (bean, cookie, request etc)

CommonsMultipartResolver (или др. реализация MultipartResolver) - extends ViewResolver, нужно для upload файлов
Сам класс MultipartResolver, а не сторонние реализации доступен с Servlet 3.0. Его можно объявить как @Bean:
    <bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"></bean>
Для отправки файла использовать:
    <form method="post" action="/form" enctype="multipart/form-data">
        <input type="file" name="file"/>
И перехват (для StandardServletMultipartResolver из Spring):
    @RequestMapping(value="/someUrl", method = RequestMethod.POST)
    public String onSubmit(@RequestPart("meta-data") MetaData metadata,
        @RequestPart("file-data") MultipartFile file) {}
И перехват (для javax.servlet.http.Part из Java EE):
    @RequestMapping(value = "/form", method = RequestMethod.POST)
    public String handleFormUpload(@RequestParam("name") String name,
        @RequestParam("file") Part file) {}
---
HandlerMapping - наследники этого класса использует DispatcherServlet
чтобы решить к какому Controller (и методу) отправить запрос

HandlerMapping vs ViewResolver
    - HandlerMapping привязывает к ссылке контроллер,
        а ViewResolver привязывает View (страницу)

Пример 1:
@Bean("/welcome")
public BeanNameHandlerMappingController beanNameHandlerMapping() {
    return new BeanNameHandlerMappingController();
}

Пример 2 (более полный конфиг): см. ниже


Пример 3 (конфиги путей через Adapter, ВОЗМОЖНО не HandlerMapping):
@Configuration
@EnableWebMvc
public class AppConfig implements WebMvcConfigurer{
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(false);
    }
}
---
Чтобы работать с событиями нужно:
    1. Создать событие MySpringEvent extends ApplicationEvent
    2. заинжектить ApplicationEventPublisher и опубликовать
        @Autowired var applicationEventPublisher;
        applicationEventPublisher.publishEvent(mySpringEvent);
    2. Альтернатива: implements ApplicationEventPublisherAware
    3. ИЛИ слушатель должен реализовать ApplicationListener
            и метод onApplicationEvent(MySpringEvent event)
    3. ИЛИ иметь аннтацию (в новых версиях)
        @EventListener(condition = "#event.success")
        
Пример ApplicationEvent:
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
---
Существует много готовых событий:
ContextRefreshedEvent, ContextStartedEvent, RequestHandledEvent etc

Пример:
    public class ContextRefreshedListener 
    implements ApplicationListener<ContextRefreshedEvent> {
        @Override
        public void onApplicationEvent(ContextRefreshedEvent cse) {
            System.out.println("Handling context re-freshed event. ");
        }
    }

Привязываем событие к транзакции:
@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
public void handleCustom(CustomSpringEvent event) {
    System.out.println("Handling event inside a transaction BEFORE COMMIT.");
}
---
Spring security:
    0. SecurityContext сделан через ThreadLocal
    1. добавить @EnableWebSecurity class Cfg extends WebSecurityConfigurerAdapter {}
        альтернатива: подключить и настроить:
            <filter-name>springSecurityFilterChain</filter-name>
            <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class> 
    1.2 @EnableGlobalMethodSecurity(prePostEnabled = true) включение AOP для Spring Data Rest (PreAuthorize/PreFilter/etc, JSR-250)
            @EnableWebSecurity включает http.hasRole(...) и подобное
    2. Реализовать из Cfg:
        protected void configure(HttpSecurity http) {
            http.csrf().disable().authorizeRequests().antMatchers("/", "/list").hasRole("ADMIN")
                .and().formLogin().successHandler(mySuccessHandler).failureHandler(myFailureHandler)
                .and().logout().anyRequest().authenticated();
        }
        варианты configure(...):
            configure(AuthenticationManagerBuilder) - можно добавить users и их пароли в in memory БД
            configure(HttpSecurity) - для http
            configure(WebSecurity) - глобально
    3. Реализовать UserDitailsService и переопределить в нем loadUserByUsername(username):
        в нем вытащить: user = usrRepository.get(username)
        и установить алгоритм хэширования: builder.password(new BCryptPasswordEncoder().encode(user.getPassword()));
        
        Пример (прим. возможно как вариант можно просто создать @Bean UserDetailsService):
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
    4. GrantedAuthority это тоже что и Role, его часто используют чтобы взять role (Set<GrantedAuthorities> roles = userDetails.getAuthorities() )
    5. Взять данные о user (др. назв. principle): 
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String currentPrincipalName = authentication.getName();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // берем все данные
            SecurityContextHolder.getContext().getAuthentication(); // из контекста
            Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            // можно инжектить в метод Controller:
            @ResponseBody public String currentUserName(@Param() String param, Authentication authentication) {}
    6. Проверка прав (аннотации для методов сервиса или контроллера):
        @PostFilter/@PreFilter                  - допускает вызов метода, но фильтрует результат (напр отдадим админу только сообщение с матами)
        @PreAuthorize("hasRole('ROLE_ADMIN')")  - можно вызвать, если указанное выражение в ней true
        @PostAuthorize                          - можно вызвать, но если вернет false, то исключение
        @Secured("ROLE_SPITTER") == @RolesAllowed({"SPITTER"})
        @PostAuthorize("returnObject.spitter.username == principal.username") - проверка на доступа к данным только пользователя username
        @PreAuthorize ("#book.owner == authentication.name")
		
		Другой спосб: сделать отдельный repository для работы с пользователями в Data REST (export = false), но не делать его публичным, а использовать как внутренний для других repository (как обертки для него). И в них проверять права. (ЭТО ДОГАДКА)


Spring security настройка сессии:
    always – a session will always be created if one doesn’t already exist
    ifRequired – a session will be created only if required (default)
    never – the framework will never create a session itself but it will use one if it already exists
    stateless – no session will be created or used by Spring Security

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
    
    Timeout сессии:
    1. server.servlet.session.timeout=60s из Spring Boot
    2. другие способы сводятся к получению session из ServletContext
        и прямой установке getSession().setMaxInactiveInterval(15);
        или в web.xml:  <session-config>
                            <session-timeout>20</session-timeout>
                        </session-config>
        получить можно:
            implements HttpSessionListener, и различных Handler
    
Spring MVC:
    1. добавить (@EnableWebMvc устанваливает DispatcherServlet): 
        @EnableWebMvc
        class WebSecurityConfig extends WebSecurityConfigurerAdapter {}
    2. Выбрать ViewResolver - соотв. url и шаблонов (путь к файлу), в том числе для BlaBlaMulrtipartBla для upload бинарных файлов
    3. Выбрать View подходящий к шаблонизатору
    4. @EnableSpringDataWebSupport включает Spring HATEOAS
    5. Включить транзакции:
        1. @EnableTransactionManagement
        2. @Bean PlatformTransactionManager transactionManager(){} // конфиги транзакции
        3. @Bean sessionFactory() // из hibernate, если нужно для transactionManager
        4. @Bean DataSource dataSource(){}
    6. Над методами @Controller поставить @RequestMapping или аналог @PostMapping/@GetMapping/etc
    7. Для проверки на ошибки можно использовать Hibernate Validator:
        String addSpitterFromForm(@Valid Spitter spitter, BindingResult bindingResult) {
            if(bindingResult.hasErrors()) { // Проверка ошибок
                return "spitters/edit";
            }
            // Сохранить объект Spitter
            spitterService.saveSpitter(spitter);
            // Переадресовать
            return "redirect:/spitters/" + spitter.getUsername(); 
        }
        
    Note: можно вернуть:
        new ResponseEntity(mRs, HttpStatus.OK)
        - где первый параметр это Entity, а второй это status запроса
        
    Аннотации:
    1. @ModelAttribute - доступ к элементу который УЖЕ в model в @Controller
        1.2 НАД методом @Controller и тогда return значение попадает в model
            @ModelAttribute("vehicle") Vehicle getVehicle() {}
        1.3 Перед ПАРАМЕТРОМ метода @Controller, если у него кастомное имя
            void post(@ModelAttribute("vehicle") Vehicle vehicleInModel) {}
    2. @CrossOrigin - для настройки CORS
       
    ModelAndView использует ModelMap, которая использует Map.
    Этот объект передается во View и генерирует ключи Map сам, на основе имени добавленного объекта.
    
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
---
HttpMessageConverter - его наследники конвертят body запроса в параметры сервлета (из и в).
Напр. MappingJackson2HttpMessageConverter для Jackson

@Configuration
@EnableWebMvc
public class ApplicationConfig extends WebMvcConfigurerAdapter { 
 @Override
 public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
    // конфиги всех converters
 } 
}
---
Spring REST:
    1. @RequestBody добавляет авто deserialization HttpRequest body в Java object:
        @PostMapping("/request")
        public ResponseEntity post(@RequestBody LoginForm loginForm) {
            exampleService.fakeAuthenticate(loginForm);
            return ResponseEntity.ok(HttpStatus.OK);
        }
    2. @ResponseBody делает обратное и кладет в HttpResponse body:
            @PostMapping("/response")
            @ResponseBody
            public ResponseTransfer get() {
                return new ResponseTransfer("Thanks For Posting!!!");
            }
    3. @RestController == @Controller + @ResponseBody, после этой аннотации МЕТОДЫ контроллера не нужно помечать как @ResponseBody,, можно просто return myObj и myObj сам преобразуется в JSON
    
Spring Data JPA + REST:
    Базовый путь: spring.data.rest.basePath=/api

    Настройка через одно из:
        1. @Bean public RepositoryRestConfigurer repositoryRestConfigurer() {
        2. Или реализацию класса:
            @Component
            public class CustomizedRestMvcConfiguration extends RepositoryRestConfigurerAdapter {
        
    Spring Data REST не знает о abstract классах и interface, поэтому нужно их конфигурить через (это часть jackson):
        configureJacksonObjectMapper(ObjectMapper objectMapper) {
            objectMapper.registerModule(new SimpleModule("MyCustomModule") {
            
    Можно подключить Serializers, если нужно сериализировать или десериализировать по особому:
        public void setupModule(SetupContext context) {
            
    Классы repository:
        CrudRepository
        PagingAndSortingRepository<T, ID>
        JpaRepository
    
    // делаем невидимым по ссылка, напр. чтобы защитить важную инфу. и используем в др. repo
    @RepositoryRestResouce(exported = false)
    
    // объявление и подключение
    @Projection(name = "inlineAddress", types = { Person.class }) 
    interface InlineAddress {
        String getFirstName();
        String getLastName();
        Address getAddress(); 
    }
    @RepositoryRestResource(excerptProjection = InlineAddress.class) // привязываем
    interface PersonRepository extends CrudRepository<Person, Long> 

    События привязанные к действиям над Entity:
    @RepositoryEventHandler(Author.class) 
    
    @NoRepositoryBean Проставляется над базовым классом CrusRepository, чтобы не создалась его сущность
    
    // 1. помечаем repository
    Отмечает repository:
            collectionResourceRel   - имя сгенерированных links (users)
            path                    - путь в url (users)
            itemResourceRel         - имя 1ой сущности (user)
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
---
Spring life cycle делятся на 2 вида (ПРЕДПОЛОЖЕНИЕ!):

1.  
    1. интерфесы:
        InitializingBean / DisposableBean с их методами afterPropertiesSet() / destroy()
    2. аннотации: @PostConstruct, @PreDestroy
    3. методы указанные в: init-method, destroy-method
        @Bean(initMethod = "initMethod", destroyMethod = "destroyMethod")

Note: есть еще интерфейс BeanFactoryAware, но он немного не то.

Note: у бинов prototype НЕ вызывается метод с анотацией @PreDestroy. НО вызывает @PostConstruct
        
2. BeanPostProcessor interface часть жизненного цикла, но используется чтобы расширить функциональность самих модулей. Нужно наследовать и переопределить метод.
    напр.:
        CommonAnnotationBeanPostProcessor, 
        RequiredAnnotationBeanPostProcessor, 
        AutowiredAnnotationBeanPostProcessor
        
BeanPostProcessor - то место где бины связываются
    (напр @Autowired через AutowiredAnnotationBeanPostProcessor)
        
BeanPostProcessor vs BeanFactoryPostProcessor
    1. BeanFactoryPostProcessor - вызывает переопределлный метод postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory), когда все определения бина загружены, но сам он не создан. Можно перезаписывать properties бина даже если бин eager-initializing. В этом случае есть доступ ко все бинам из контекста.
    2. BeanPostProcessor - вызывается когда все определения бина уже загружены и сам бин только что создан Spring IoC -ом. (он наследуется самим классом бина???)
    
Последовательность вызовов:
1) BeanPostProcessor.postProcessBeforeInitilazation()
2) @PostConstruct or InitializingBean.afterPropertiesSet() or init-method
3) BeanPostProcessor.postProcessAfterInitialization()
4) @PreDestroy or DisposibleBean.destroy() or destroy-method

@PostConstruct - в отличии от конструктора вызван когда зависимости заинжекчены.
afterPropertiesSet и destroy более завязаны на Spring в отличии от

Вызовы init:
0. constructor
1. @PostConstruct
2. InitializingBean.afterPropertiesSet()
3. initMethod() аннотации @Bean

Вызовы destroy:
1. @PreDestroy
2. DisposibleBean.destroy()
3. destroyMethod() аннотации @Bean
---
Реализация своего life cycle:
    1. Наследовать Lifecycle и/или Phased
    2. Переопределить методы
    
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
---
Spring Aware Interfaces - наследуем и переопределяем метод нужного события, касается напр. модулей для чьей конфигурации нужно наследовать Aware и переопределить.

Суть: в реализованные методы этих интерфейсов передаются разные объекты,
    связанные с контекстом: имя бинов, фабрика которая создает конкретный бин и пр.
    
Примеры: BeanNameAware, BeanFactoryAware, ApplicationContextAware etc
---
Прим. не разобрал тему, дописать

Resource из Spring - это то что можно вытащить из ServletContext или classpath для обычных сервлетов, низкоуровневый доступ из Spring к сервлетам
(напр. InputStreamReader для загрузки/выгрузки файла?)

interface ResourceLoaderAware обеспечивает объект ссылкой на ResourceLoader

Можно получить: ResourceLoader.getResource(String location)

1. UrlResource - является оберткой для java.net.URL
2. ClassPathResource - представляет ресурс, который может быть получен из classpath и поддерживает ресурсы как java.io.File
3. FileSystemResource - реализация для обработки java.io.File
4. ServletContextResource - реализация для обработки ServletContext ресурсов относительно корневой директории web-приложения.
5. InputStreamResource - реализация для обработки InputStream
6. ByteArrayResource - реализация для обработки массива байтов
---
Выполнение операций при деплое приложения Spring (метод run()):
@Component
public class AppRunner implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {}
}
--- SpEL
${...} is the property placeholder syntax. It can only be used to dereference properties.
#{...} is SpEL syntax, which is far more capable and complex. It can also handle property placeholders, and a lot more besides.
Both are valid, and neither is deprecated.

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
