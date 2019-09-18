JUnit, Mock, Cucumber (или др. новые системы), TDD, BDD, пример тестирования в Spring MVC

# TDD
**TDD** (Test Driven Development) - подход к тестированию, можно использовать любую библиотеку (JUnit, ...). Сначало пишется тест, потом реализация.

TDD - это способ разработки (design), в котором думают о реализации до написания кода.

**Цикл разработки с TDD:** (повторяется по кругу, еще называют **Red-Green-Refactor**)
1. Write a test
2. Run all tests
3. Write the implementation code
4. Run all tests
5. Refactor

**Шаги:**
* Add a test
* Run all tests and see if the new one fails
* Write some code
* Run tests
* Refactor code
* Repeat


**Основные принципы в TDD:**
1. если тест трудно написать, то код как правило плохой
2. сначало пишем название функции-тестера с описанием того что она делает (имя функции НЕ название теста, а конкретно ОПИСАНИЕ действия функции)
3. Потом пишем тест, как буд-то функция-сервис уже реализована (заодно видим как мы его будем использовать)
4. Потом пишем реализацию и правим ее пока не будет проходить все тесты

**Принципы тестирования (этот возможно не точный):**
1. не тестируем private методы,

**Плюсы TDD:**
* простота
* всегда рабочий код
* маленькие шаги
* всегда видно плохой дизайн в процессе разработки - ОДИН ИЗ ОСНОВНЫХ ПЛЮСОВ
* тесты со 100% покрытием кода

**Mocking** - подстановка фиктивных реализаций методов и классов вместо реальной, т.к. работает быстрее чем загрузка реальных реализаций со всеми зависимостями.

**Watchers** -  frameworks or tools, наблюдают за кодом и при изменении кода автоматически запускают тесты.

**Documentation** - документация описывает тест, например в аннотациях над тестами.
    
**Пример TDD:**
```java
// сервис (РЕАЛИЗАЦИЮ ПИШЕМ ПОСЛЕ ТЕСТА)
class MySrv {
    public static String convertToRoman(ind d){}
}

// тестер (сначало пишем ЭТОТ ТЕСТ)
class WhenConvertArabicNumToRoman {
    @Test
    public void Convert_Digit_To_Roman() {
        String roman = new MySrv.convertToRoman(4);
        assertEquals("IV", roman);
    }
    
    @Test
    public void whenAddOneOnOneThenResultTwo(){}
}
```

**Запуск тестов без аннотаций:**
```java
public class TestRunner {
    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(TestJunit.class); // запускаем указывая класс с тестами
            
        // читаем ошибки
        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }
            
        System.out.println(result.wasSuccessful());
    }
}
```
# Dummy vs Fake vs Stub vs Mock
* **Dummy** - просто класс созданный для использования в тестах, он никак не связан с самим приложением, а нужен только чтобы не писать какой-то необязательный для теста код и заменить его этим классом (например много ненужных параметров в constructor можно заменить специально созданным объектом)
* **Fake** - реализация какого-то класса с подменой методов взаимодействующих с какой-то частью приложения (например базой) и необходимых для теста, например доступ к базе можно заменить переопределенным методом возвращающим коллекцию
* **Stub** - переопределить некоторые методы тестируемого класса, чтобы они возвращали hard-coded values например тех что работают слишком долго. Его называют `state-based`
* **Mock** - похож на **Stub**, но вместо возврата значений проверяют сам факт вызова методов. Его называют `interaction-based`

**Stub** и **Mock** похожи, это подмножество **Mock**, но служащие для разных целей.

# TDD vs BDD
**TDD vs BDD** - это тесты + документация + примеры в одном месте

**BDD vs TDD** - почти не имеют различий, в BDD ВОЗМОЖНО больше упор на документацию. На stackoverflow написано, разницы никакой.

TDD vs BDD терминология:
* Test → Example
* Assertion → Expectation
* assert → should
* Unit → Behavior
* Verification → Specification
* … and so on

# Spy vs Mock
* **Mock** - создает ненастоящий объект для которого определяется поведение
* **Spy** - создает настоящий объект с реальными методами и для теста мы можем переопределить только несколько, а остальные будут работать как обычные методы реального объекта (т.е. часть объекта это Mock, а часть реальная)
```java

// 1. mock
List<String> mockList = Mockito.mock(ArrayList.class);

// 2. mock
@Mock List<String> mockList;

// 1. spy
List<String> spyList = Mockito.spy(new ArrayList<String>());

// 2. spy
@Spy List<String> spyList;
```
# verify
Проверяет были ли вызовы, их количество и прочее.
```java
List<String> mockedList = mock(MyList.class);
mockedList.size();
mockedList.clear();
verify(mockedList).size();
verifyNoMoreInteractions(mockedList);
```
# doReturn.when vs when.thenReturn


# integration test (it)

# end to end test (e2e)

# JUnit
**JUnit** - lib для тестирования

**Для Eclipse:**
Чтобы в Eclipse вручную создать кталог для тестов можно создать например каталог testsrc, кликнуть правой кнопкой и установить "use as source code"
<br>
В Eclipse при создании тестов JUnit можно указать testsrc - как целевой каталог для класса (вверху окна).
<br>
В Eclipse при создании тестов через GUI по Next step появится меню в котором можно выбрать методы классов, которые хотите тестировать (создадутся заглушки)
<br>
В Eclipse через GUI можно создать методы со стандартными именами. Одновременно с этим название этих методов ОБЩЕПРИНЯТОЕ:

**Аннотации** (названия методов общепринятые для IDE, но это не правило)
* `@BeforeEach setUp()` - (@Before в старой) - до каждого теста
* `@BeforeAll setUpBeforeClass()` - 1 раз перед тестами класса (старая `@BeforeClass`)
* `@AfterEach tearDown()` (@After в старой) - после каждого теста
* `@AfterAll tearDownAfterClass()` - 1 раз после тестов класса (старая `@AfterClass`)
* `@Disabled("Disabled because ...")` - выключает тесты помеченные ей (старая `@Ignore`)
* `@Test(timeout = 1000, expected = RuntimeException.class)` - если timeout привышен, то fail
* `@RepeatedTest(12)` - повторяет тест N раз
***
**Дополнительные:**
* `@EnabledOnOs(MAC), @TestOnMac, @EnabledOnJre({JAVA_9, JAVA_10})` - включает или отключает в зависимости от условия
* `@EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")` - в зависимости от системной переменной
* `@DisabledIf("Math.random() < 0.314159")`
* `@EnabledIf("'CI' == systemEnvironment.get('ENV')")`
* `@Order(1)`
* `@ExtendWith` and `@RegisterExtension` - замена старым `@Rule` and `@ClassRule`
***
Объект `TestInfo` содержит инфу о тесте (методе, т.е. проставленную над ним мета инфу) и `@Tag`:
```java
@Test
@DisplayName("TEST 1")
@Tag("my-tag")
void test1(TestInfo testInfo) {
    assertEquals("TEST 1", testInfo.getDisplayName());
    assertTrue(testInfo.getTags().contains("my-tag"));
}
```

# JUnit. Assert
**Методы:**
1. `fail("reason")` - устроить fail в месте вызова, всегда работает
2. `assertEquals("IV", "IV");` - принимает методы разных типов
3. `assertTrue` / `assertFalse`
4. `assertNotNull` / `assertNull`
5. `void assertSame(object1, object2)` / `void assertNotSame(object1, object2)`
6. `void assertArrayEquals(expectedArray, resultArray);`
7. `assertThat("Zero is one", 0, is(not(1)))` - последний аргумент это EL для сравнения, а первый причина
8. `assertThrows(UserNotFoundException.class, () -> userSrv.getUser(null)); // во 2ом параметры вызов Exception` - проверка на совпадение выбрасываемого Exception, true если исключение совпало
9. `assertTrue(name.length == 30)` - проверка на длину
10. `assertAll("person", () -> assertEquals("Jae", user.getName()), () -> assertEquals("Doe", user.getName()));` - выполняет несколько условий в группе и все fail будут показаны вместе
11. `assertTimeout(ofMillis(10), () -> { Thread.sleep(100); });`

# JUnit. Assumptions
Выполнить код только при условии указанном в assumption.
```java
@Test
void testInAllEnvironments() {
    assumingThat("CI".equals(System.getenv("ENV")), () -> {
        // perform these assertions only on the CI server
        assertEquals(2, calculator.divide(4, 2));
    });

    // perform these assertions in all environments
    assertEquals(42, calculator.multiply(6, 7));
}
```

# JUnit. @Suite

# JUnit. @ParameterizedTest
```java
// 1.
@ParameterizedTest
@ValueSource(strings = { "racecar", "radar", "able was I ere I saw elba" })
void palindromes(String candidate) {
    assertTrue(StringUtils.isPalindrome(candidate));
}

// 2.
@ParameterizedTest
@MethodSource("stringProvider")
void testWithExplicitLocalMethodSource(String argument) {
    assertNotNull(argument);
}

static Stream<String> stringProvider() {
    return Stream.of("apple", "banana");
}
```
# JUnit. @Interceptor
# JUnit. @ExtendWith и @RegisterExtension
# JUnit. TestWatcher
# JUnit. @TestTemplate
# Mock
# JUnit и Mock в Spring
# Links
* Тесты, rule, использование аннотаций @Test перед переменными https://garygregory.wordpress.com/2011/09/25/understaning-junit-method-order-execution/
* Про тестирование Spring сервисов с JUnit + mock https://www.youtube.com/watch?v=SJ2hwfdFMxY&index=2&list=PLdW9lrB9HDw0odgQIcyQspcTeQ4HX85I7
* junit 5 https://junit.org/junit5/docs/current/user-guide
* best practise https://technologyconversations.com/2013/12/24/test-driven-development-tdd-best-practices-using-java-examples-2/
* e2e https://medium.com/@giltayar/testing-your-frontend-code-part-iii-e2e-testing-e9261b56475
* Extensions https://www.baeldung.com/junit-5-extensions