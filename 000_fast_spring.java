--- Spring Core
--- Следствие из Spring AOP
    
--- Spring Annotations

Аннотации
    
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
