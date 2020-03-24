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
		
# Spring Security
## Как работает Spring Security
Источники: [легко читаемый источник всего Spring Security в целом](https://ru.wikibooks.org/wiki/Spring_Security/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9_%D0%BE%D0%B1%D0%B7%D0%BE%D1%80_Spring_Security), [официальная документация](https://spring.io/guides/topicals/spring-security-architecture)

**Это Core концепция, которая используется в реализациях таких как те что для Web и основаны на `Servlet Filters`.** Основной интерфейс `AuthenticationManager`, метод `authenticate=true` если аутенцифицироован, exception если нет и null если нельзя определить. `ProviderManager implement AuthenticationManager` делегирует к цепочке состоящей из `AuthenticationProvider` (он как `AuthenticationManager`). ProviderManager может поддерживать отдновременно много разных механизмов. Причем `ProviderManager` может быть родительским для других `ProviderManager` которые тоже указывают на свои версии `AuthenticationProviders` (получатся дерево из `ProviderManager`). Когда user аутенцифицироован дальше происходит авторизация (проверка прав доступа) для этого используется `AccessDecisionManager`, где `ConfigAttribute` это SpEL такие как `hasRole('FOO')`. Чтобы создать свои методы которые можно использовать внутри аннотаций таких как `@PreFilter` нужно **extends** класс `SecurityExpressionRoot` или `SecurityExpressionHandler`.

**Web Security** - один из механизмов для Web, внутри использует `AuthenticationManager`. Основана на **Filters** из JavaEE. Путь request примерно: `User -> Filter -> Filter -> Filter -> Servlet`

```java
// примеры интерфейсов и методов из Spring Security
public interface AuthenticationManager {
  Authentication authenticate(Authentication authentication)
    throws AuthenticationException;
}

public interface AuthenticationProvider {
	Authentication authenticate(Authentication authentication)
			throws AuthenticationException;
	boolean supports(Class<? extends Authentication> authentication); // группа обьектов с разными методами authentication
}

// пример AccessDecisionManager
boolean supports(ConfigAttribute attribute);
boolean supports(Class<?> clazz);
int vote(Authentication authentication, S object,
        Collection<ConfigAttribute> attributes);
```

**Note.** При Spring AOP на этапе вызова используется паттерн proxy и вызов security метода класса другого security метода из того же класса не будет обернут в AOP (это особенность proxy паттерна).

**Note.** По умолчанию Spring SecurityContext is thread-bound, т.е. не распостраняется на дочерние Thread

Включение
```java
@Configuration
@EnableGlobalMethodSecurity(
  prePostEnabled = true, // Spring Security pre/post annotations
  securedEnabled = true, // @Secured
  jsr250Enabled = true) // @RoleAllowed
public class MethodSecurityConfig 
  extends GlobalMethodSecurityConfiguration {
}

// Переопределяем UserDetailsService и его метод loadUserByUserName и в нем читаем пользователя из DB

// устанавливаем если нужно PasswordEncoder бин, например с алгоритмом BCrypt
```
Использование
```java
// returnObject
@PostAuthorize
  ("returnObject.username == authentication.principal.nickName")
public CustomUser loadUserDetail(String username) {
    return userRoleRepository.loadUserByUserName(username);
}

// filterObject и filterTarget (указывает какой аргумент фильтровать)
@PreFilter
  (value = "filterObject != authentication.principal.username",
  filterTarget = "usernames")
public String joinUsernamesAndRoles(
  List<String> usernames, List<String> roles) {
  
    return usernames.stream().collect(Collectors.joining(";")) 
      + ":" + roles.stream().collect(Collectors.joining(";"));
}

// создание custom аннотации
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("hasRole('VIEWER')")
public @interface IsViewer {
}

// исползование custom аннотации
@IsViewer
public String getUsername4() {
    //...
}
```

## Ключевые объекты контекста Spring Security
https://ru.wikibooks.org/wiki/Spring_Security/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9_%D0%BE%D0%B1%D0%B7%D0%BE%D1%80_Spring_Security

## Spring Security Context Propagation with @Async
https://www.baeldung.com/spring-security-async-principal-propagation

## Как работает filter chain ([источник](https://stackoverflow.com/questions/41480102/how-spring-security-filter-chain-works))
Фильтр Spring Security встраивается как **один** фильтр в цепочке наследующий `Filter` из JavaEE, а уже внутри него определены свои наборы фильтров из Spring Security.

**Ключевые фильтры:**
- `SecurityContextPersistenceFilter` (restores Authentication from JSESSIONID)
- `UsernamePasswordAuthenticationFilter` (performs authentication)
- `ExceptionTranslationFilter` (catch security exceptions from FilterSecurityInterceptor)
- `FilterSecurityInterceptor` (may throw authentication and authorization exceptions)

**Порядок фильтров:**
1. `ChannelProcessingFilter`, because it might need to redirect to a different protocol
2. `SecurityContextPersistenceFilter`, so a SecurityContext can be set up in the SecurityContextHolder at the beginning of a web request, and any changes to the SecurityContext can be copied to the HttpSession when the web request ends (ready for use with the next web request)
`ConcurrentSessionFilter`, because it uses the SecurityContextHolder functionality and needs to update the SessionRegistry to reflect ongoing requests from the principal
3. Authentication processing mechanisms - `UsernamePasswordAuthenticationFilter`, `CasAuthenticationFilter`, `BasicAuthenticationFilter` etc - so that the SecurityContextHolder can be modified to contain a valid Authentication request token
4. The `SecurityContextHolderAwareRequestFilter`, if you are using it to install a Spring Security aware HttpServletRequestWrapper into your servlet container
5. The `JaasApiIntegrationFilter`, if a JaasAuthenticationToken is in the SecurityContextHolder this will process the FilterChain as the Subject in the JaasAuthenticationToken
6. `RememberMeAuthenticationFilter`, so that if no earlier authentication processing mechanism updated the SecurityContextHolder, and the request presents a cookie that enables remember-me services to take place, a suitable remembered Authentication object will be put there
7. `AnonymousAuthenticationFilter`, so that if no earlier authentication processing mechanism updated the SecurityContextHolder, an anonymous Authentication object will be put there
8. `ExceptionTranslationFilter`, to catch any Spring Security exceptions so that either an HTTP error response can be returned or an appropriate AuthenticationEntryPoint can be launched
9. `FilterSecurityInterceptor`, to protect web URIs and raise exceptions when access is denied

Получаем фильтры и работаем с ними:
```java
    @Autowired private FilterChainProxy filterChainProxy; // получаем бин

    public void getSecurityFilterChainProxy(){
        for(SecurityFilterChain secfc :  this.filterChainProxy.getFilterChains()){ // в цикле получаем цепочки
            for(Filter filter : secfc.getFilters()){   }
        }
    }
```

## Custom Filter in the Spring Security Filter Chain ([источник](https://www.baeldung.com/spring-security-custom-filter))
Создается реализацией `GenericFilterBean` которая наследует `javax.servlet.Filter`, но является Spring aware.

Можно ставить до или после определенно фильтра:
- `addFilterBefore(filter, class)` – adds a filter before the position of the specified filter class
- `addFilterAfter(filter, class)` – adds a filter after the position of the specified filter class
- `addFilterAt(filter, class)` – adds a filter at the location of the specified filter class
- `addFilter(filter)` – adds a filter that must be an instance of or extend one of the filters provided by Spring Security

```java
// реализуем
public class CustomFilter extends GenericFilterBean {
    @Override
    public void doFilter(
      ServletRequest request, 
      ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
        chain.doFilter(request, response);
    }
}

// регистрируем, добавлем в цепочку
@Configuration
public class CustomWebSecurityConfigurerAdapter
  extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.addFilterAfter(new CustomFilter(), BasicAuthenticationFilter.class); // место в цепочке, до, после, вместо
    }
}
```
<details>
<summary>Регистрация в xml конфигурации</summary>

```xml
<http>
    <custom-filter after="BASIC_AUTH_FILTER" ref="myFilter" />
</http>
<beans:bean id="myFilter" class="org.baeldung.security.filter.CustomFilter"/>
```
**Позиции в xml:**
- `after` – describes the filter immediately after which a custom filter will be placed in the chain
- `before` – defines the filter before which our filter should be placed in the chain
- `position` – allows replacing a standard filter in the explicit position by a custom filter
</details>

## Custom Security Expression ([источник](https://www.baeldung.com/spring-security-create-new-custom-security-expression))
Деляться на 2 типа:
1. Наследованные от готового `PermissionEvaluator` и используемые для проверки прав доступа в своих собственных выражениях, но они немного ограничены в семантике.
2. Полностью свои выражения со своей логикой, наследуются от `SecurityExpressionRoot` и `MethodSecurityExpressionOperations` (с помощью него же можно переопределить готовые выражения и всегда возвращать из них `RuntimeException`, чтобы их отключить).

**1. Пример PermissionEvaluator**
```java
// реализация PermissionEvaluator
public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication auth, Object targetDomainObject, Object permission) {}
    @Override
    public boolean hasPermission(Authentication auth, Serializable targetId, String targetType, Object permission) {}
    private boolean hasPrivilege(Authentication auth, String targetType, String permission) {}
}

// регистрируем PermissionEvaluator
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        DefaultMethodSecurityExpressionHandler expressionHandler = 
          new DefaultMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }
}

// примеры использования аннотация над методами @Controller или других классов
@PostAuthorize("hasAuthority('FOO_READ_PRIVILEGE')")
@PostAuthorize("hasPermission(returnObject, 'read')") // returnObject это возвращаемый методом обьект
@PreAuthorize("hasPermission(#id, 'Foo', 'read')") // #id это имя параметра метода
```

**2. Пример MethodSecurityExpressionOperations**
```java
// реализуем выражение
// реализуем MethodSecurityExpressionOperations, при этом SecurityExpressionRoot это класс содержащий
// некоторые методы, чтобы в него отправить готовый Authentication и использовать this.getPrincipal() и т.д.
public class CustomMethodSecurityExpressionRoot
    extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    
    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }
 
    public boolean isMember(Long OrganizationId) {
        User user = ((MyUserPrincipal) this.getPrincipal()).getUser();
        return user.getOrganization().getId().longValue() == OrganizationId.longValue();
    }
}

// создаем handler выражения
public class CustomMethodSecurityExpressionHandler 
  extends DefaultMethodSecurityExpressionHandler {
    private AuthenticationTrustResolver trustResolver = new AuthenticationTrustResolverImpl();
 
    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      Authentication authentication, MethodInvocation invocation) {
        CustomMethodSecurityExpressionRoot root = 
          new CustomMethodSecurityExpressionRoot(authentication);
        root.setPermissionEvaluator(getPermissionEvaluator());
        root.setTrustResolver(this.trustResolver);
        root.setRoleHierarchy(getRoleHierarchy());
        return root;
    }
}

// регистрируем handler выражения
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        CustomMethodSecurityExpressionHandler expressionHandler = 
          new CustomMethodSecurityExpressionHandler();
        expressionHandler.setPermissionEvaluator(new CustomPermissionEvaluator());
        return expressionHandler;
    }
}

// используем
@PreAuthorize("isMember(#id)") public Organization findOrgById(@PathVariable long id) {}
```
**3. Пример отключения встроенных Expression** (по сути мы просто переопределяем их и всегда возвращаем из них RuntimeException)
```java
// создаем выражение
public class MySecurityExpressionRoot implements MethodSecurityExpressionOperations {
    public MySecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        this.authentication = authentication;
    }
 
    @Override
    public final boolean hasAuthority(String authority) {
        throw new RuntimeException("method hasAuthority() not allowed");
    }
}

// создаем handler и регистрируем его, как в примере выше
```

## Типы аутентификации ([интересные примеры из практики](https://gist.github.com/inozemtsev32/209d12d197d7c1c32952ff73dba61f95))
- HTTP BASIC AUTH - логин и пароль передается в каждом  запросе закодированный в base64, нужно использовать https чтобы не перехватили
- SESSION COOKIE BASED - при логиге формируется сессия и проставляется пользователю в cookies, этот способ плохо масшатабируем
- SERVER SIGNED TOKENS - как с SESSION COOKIE BASED, но формируется токен

## OAUTH2 приминительно к Spring и JWT
https://www.baeldung.com/spring-security-oauth-jwt

## AbstractSecurityWebApplicationInitializer и как он работает
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#using-literal-abstractsecuritywebapplicationinitializer-literal

## Session Fixation Attack Protection (поведение сессии)
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#ns-session-fixation

## AOP Alliance (MethodInvocation) Security Interceptor
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#aop-alliance

## Web Security Expressions
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#el-access-web

## Domain Object Security (ACLs)
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#domain-acls

## Cache Control
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#headers-cache-control

## X-Frame-Options, X-XSS-Protection, CSRF, CSP, Referrer Policy, Feature Policy, CORS
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#headers-cache-control

## Custom Headers
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#headers-cache-control

## Localization
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#headers-cache-control

## WebSocket Security
https://docs.spring.io/spring-security/site/docs/5.2.0.RELEASE/reference/htmlsingle/#headers-cache-control

## Annotations
**Список**
* `@PreAuthorize` vs `@Secured` - в `@PreAuthorize` можно использовать SpEL, получать доступ к свойствам `SecurityExpressionRoot`, получать доступ к параметрам метода (аналогично для `@PostAuthorize`, `@PreFilter`, `@PostFilter`)
    ```java
    @PreAuthorize("#contact.name == principal.name")
    // @PreAuthorize("hasRole('ADMIN OR hasRole('USER')")
    public void doSomething(Contact contact)

    @Secured("ROLE_ADMIN")
    void а1(){}
    ```

## Authentication Basic vs Bearer

Эти схемы не относятся только к Spring, это стандарт источник [тут](https://developer.mozilla.org/en-US/docs/Web/HTTP/Authentication)

* Basic - использует username и password (base64-encoded credentials)
* Bearer - использует token, это лучший выбор для JWT

## Spring Method Security
https://www.baeldung.com/spring-security-method-security