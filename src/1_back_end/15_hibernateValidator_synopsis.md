Источники: [документация](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single)

Hibernate Validator - библиотека для валидации, имплементация Bean Validation API (JSR-380)

# Что можно делать

1. Проставлять аннотации над полями DTO и Entity (хотя использовать Entity для отправки на front-end не всегда хорошая практика) с правилами валидации
2. Использовать спец. выражения для описания сообщения валидации (`formatter` как `String.format` или `Formatter`, имена полей аннотации, слова `value` и `validatedValue`). Язык описания называется [Jakarta Expression Language](https://projects.eclipse.org/projects/ee4j.el), можно подключить использование [Spring SpEl](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-script-evaluator-factory)
    ```java
    @Size(
        min = 2,
        max = 14,
        message = "The license plate '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String licensePlate;

    @DecimalMax(value = "100000", message = "Price must not be higher than ${value}")
    private BigDecimal price;

    @DecimalMax(
            value = "350",
            message = "The top speed ${formatter.format('%1$.2f', validatedValue)} is higher than {value}"
    )
    private double topSpeed;
    ```
3. Менять порядок выполнения валидации или делать вызов разных типов валидации на разные типы запросов (POST, GET, PUT etc) через [groups](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-groups). **Note.** если назначить group для которой не указано когда работать, то эта правило перестанет работать это можно использовать как hack для отключения некоторых правил (назначая им несуществующие группы)
    ```java
    @NotEmpty(groups = PutValidation.class)
    private String field1;

    @Size(min = 3, groups = PutValidation.class)
    @Size(min = 5, groups = PostValidation.class)
    private String field2;

    @Min(value = 18, groups = {PostValidation.class,PutValidation.class,Default.class})
    private int age;

    @PostMapping
    public ResponseEntity<String> save(@Validated(PostValidation.class) @RequestBody  Person person) {}

    @PutMapping
    public ResponseEntity<String> update(@Validated(PutValidation.class) @RequestBody Person person) {}

    // задаем порядок
    @GroupSequence({ Default.class, CarChecks.class, DriverChecks.class })
    public interface OrderedChecks {
    }
    ```
4. Можно проставлять аннотации валидации над RequestParams и PathVariables ([пример](https://www.baeldung.com/spring-validate-requestparam-pathvariable)). **Note.** рекомендуется считать null валидным значением, а для его проверки ставить отдельно `@NotNull`
    ```java
    @GetMapping("/valid-name/{name}")
    public void createUsername(@PathVariable("name") @NotBlank @Size(max = 10) String username) {
        // ...
    }
    ```
5. Можно делать свои валидаторы (пары класс и аннотация) для сложных валидаций
    ```java
    // при этом groups() и payload() обязательные поля (по крайней мере лучше не удалять, они могут быть зарезервированы)

    // 1. делаем аннотацию в пару
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
    @Retention(RUNTIME)
    @Constraint(validatedBy = CheckCaseValidator.class)
    @Documented
    @Repeatable(List.class)
    public @interface CheckCase {
        // закоментил эту строку т.к. парсер VSCode неправильно отрабатывает формат
        // String message() default "{org.hibernate.validator.referenceguide.chapter06.CheckCase.message}";

        Class<?>[] groups() default { };
        Class<? extends Payload>[] payload() default { };
        CaseMode value();

        @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
        @Retention(RUNTIME)
        @Documented
        @interface List {
            CheckCase[] value();
        }
    }

    // 2. делаем валидатор
    public class CheckCaseValidator implements ConstraintValidator<CheckCase, String> {
        private CaseMode caseMode;

        @Override
        public void initialize(CheckCase constraintAnnotation) {
            this.caseMode = constraintAnnotation.value();
        }

        @Override
        public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
            if ( object == null ) {
                return true;
            }
            if ( caseMode == CaseMode.UPPER ) {
                return object.equals( object.toUpperCase() );
            }
            else {
                return object.equals( object.toLowerCase() );
            }
        }
    }
    ```
6. Можно задать список значений, например список максимально возможных
7. С Bean Validation 2.0
   1. single-parameter constraints - валидация одиночных параметров
        ```java
        public void createReservation(@NotNull @Future LocalDate begin,
        @Min(1) int duration, @NotNull Customer customer) {

            // ...
        }

        public class Customer {
 
            public Customer(@Size(min = 5, max = 200) @NotNull String firstName, 
            @Size(min = 5, max = 200) @NotNull String lastName) {
                this.firstName = firstName;
                this.lastName = lastName;
            }
        
            // properties, getters, and setters
        }
        ```
   1. cross-parameter - можно обьявить валидацию над constructor или method, а потом обработать массив полей в Object[]
        ```java
        @ConsistentDateParameters // применяем
        public void createReservation(LocalDate begin, 
        LocalDate end, Customer customer) {
            // ...
        }
        // создаем, еще надо создать аннотацию в пару
        @SupportedValidationTarget(ValidationTarget.PARAMETERS)
        public class ConsistentDateParameterValidator 
        implements ConstraintValidator<ConsistentDateParameters, Object[]> {
            @Override
            public boolean isValid(
            Object[] value, 
            ConstraintValidatorContext context) {
                if (value[0] == null || value[1] == null) {
                    return true;
                }
                if (!(value[0] instanceof LocalDate) 
                || !(value[1] instanceof LocalDate)) {
                    throw new IllegalArgumentException(
                    "Illegal method signature, expected two parameters of type LocalDate.");
                }
                return ((LocalDate) value[0]).isAfter(LocalDate.now()) 
                && ((LocalDate) value[0]).isBefore((LocalDate) value[1]);
            }
        }
        ```
   2. return constraints - валидирует значение когд оно возвращается каким-то методом
        ```java
        @NotNull
        @Size(min = 1)
        public List<@NotNull Customer> getAllCustomers() {
            return null;
        }
        // на конструкторе тоже можно
        public class Reservation {
            @ValidReservation
            public Reservation(LocalDate begin, LocalDate end, Customer customer, int room) {}
        }
        ```
    3. Cascaded Validation - валидация вложенных друг в друга объектов (графа объектов) аннотацией `@Valid`
        ```java
        public class Reservation {
            @Valid // поле
            private Customer customer;
        }

        public void createNewCustomer(@Valid Reservation reservation) {} // параметр

        @Valid
        public Reservation getReservationById(int id) { return null; } // returned значение
        ```
8. Можно менять текст сообщения об ошибке с помощью своих реализаций [MessageInterpolator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#chapter-message-interpolation)
9.  Можно использовать payload поле аннотации - полезную нагрузку которую потом использовать внутри валидатора (доп. инфа для валидации)
10. Менять дефолтные сообщения об ошибках с помощью спец. свойств
    ```properties
    org.hibernate.validator.referenceguide.chapter06.CheckCase.message=Case mode must be {value}.
    ```
11. Automatic Validation
    1.  Automatic Validation With Spring Пометить класс бина который создаем `@Validated` (в нем на полях должны быть использованы аннотации валидации)
        ```java
        @Validated // включаем валидацию
        public class ReservationManagement {
            public void createReservation(@NotNull @Future LocalDate begin, 
            @Min(1) int duration, @NotNull Customer customer){
                // ...
            }
            
            @NotNull
            @Size(min = 1)
            public List<@NotNull Customer> getAllCustomers(){
                return null;
            }
        }

        // еще нужно зарегистрировать
        @Configuration
        @ComponentScan({ "org.baeldung.javaxval.methodvalidation.model" })
        public class MethodValidationConfig {
            @Bean
            public MethodValidationPostProcessor methodValidationPostProcessor() {
                return new MethodValidationPostProcessor();
            }
        }
        ```
    2. Automatic Validation With CDI (JSR-365) - если приложение запущено в Jakarta EE container, то бины будут валидироваться автоматически на этапе вызова (видимо такой аналог Spring Framework), а Jakarta означает что поддерживается стандарт Bean Validation
12. Programmatic Validation - запускаем валидацию вручную, вытаскиваем список ошибок и обрабатываем Note. можно подключить его через AOP см. [ExecutableValidator](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#section-validating-executable-constraints)
    ```java
    ReservationManagement object = new ReservationManagement();
    Method method = ReservationManagement.class.getMethod("createReservation", LocalDate.class, int.class, Customer.class);
    Object[] parameterValues = { LocalDate.now(), 0, null };
    Set<ConstraintViolation<ReservationManagement>> violations = executableValidator.validateParameters(object, method, parameterValues);
    ```
13. TraversableResolver - описать тут

# VS
* **@Size vs @Min vs @Max** - в @Size поля min и max это **int** и по умолчанию min=0 и max=Integer.MAX_VALUE, а @Min и @Max имеют тип **long** и по умолчанию ничего.
* **@Validated vs @Valid** - их действие отличается, @Validated можно проставить над всем классом например `@RepositoryRestController` чтобы валидация заработала в Spring Data REST