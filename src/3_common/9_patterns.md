- [Software design pattern (паттерны Gang of Four (GoF)](#software-design-pattern-паттерны-gang-of-four-gof)
  - [behavioral patterns (Поведенческие)](#behavioral-patterns-Поведенческие)
  - [Creational patterns (Порождающие)](#creational-patterns-Порождающие)
  - [Structural patterns (Структурные)](#structural-patterns-Структурные)
- [Enterprise Integration Patterns](#enterprise-integration-patterns)
- [Functional](#functional)
- [Concurrency](#concurrency)
- [Architectural](#architectural)
  - [Service Locator](#service-locator)
- [Distributed (Cloud)](#distributed-cloud)
- [Other patterns](#other-patterns)
- [SOLID](#solid)
- [is A и Has A](#is-a-и-has-a)
- [Cohesion и Coupling](#cohesion-и-coupling)
- [immutable объект, Создание immutable объекта](#immutable-объект-Создание-immutable-объекта)
- [Dependency Injection](#dependency-injection)
- [SAM Pattern](#sam-pattern)
- [Гексагональная архитектура](#Гексагональная-архитектура)
- [Список ООП vs functional концепций:](#Список-ООП-vs-functional-концепций)

# Software design pattern (паттерны Gang of Four (GoF)

## behavioral patterns (Поведенческие)

* **Observer** - получает оповещения от других объектов (Observables) о изменении их состояния (наблюдает за ними)

* **Iterator** - может обходить связанные объекты не раскрывая их структуры (разные List, Set, Map etc). Выносит методы для обхода коллекций в отдельный класс с методами: `hasNext()`, `next()`, `previous()` etc.  
  * <details>
    <summary>Example</summary>

    **Пример Iterable** (с методами `next()` и `hasNext()` ) и **Пример Iterator** (с методом `iterator()`):
    ```java
    class MySet implements Iterable {
        Iterator iterator() { return new MySetIterator(); }
        class MySetIterator impliments Iterator {
            // ... доступ к внутренностям MySet и возврат ее элементов по next()
            boolean hasNext();
            Object next() { return someMySetEl; };
        }
    }
    ```
    </details>

* **Command** - объект команды содержит само действие (вызов методов других классов) и параметры. Команды в этом паттерне не методы, а сам объект целиком - это команда.

* **Strategy** - содержит ссылку на алгоритм с общим для всех таких же алгоритмов интерфейсом (методом). Устанавливая другие реализации алгоритма можно переключаться между ними. Пример: архиватор с разными методами сжатия.

* **Memento** - хранит состояние объекта и позволяет откатывать к прошлым состояниям

* **Template method** - переопределяет некоторые шаги алгоритмов предка, не меняя остальные шаги (обычное наследование)

* **Chain of responsibility** - 

* **Mediator** - 

* **State** - 

* **Visitor** - 

## Creational patterns (Порождающие)

* **Singleton** - один экзепляр объекта на все приложение
  * <details>
    <summary>Типы реализации Singleton (ВАЖНО!)</summary>

      1. **Eager Singleton**
          ```java
          public class EagerSingleton {
              private static final EagerSingleton instance = new EagerSingleton();
              
              //private constructor to avoid client applications to use constructor
              private EagerSingleton(){}
              public static EagerSingleton getInstance(){ return instance; }
          }
          ```
      2. **Lazy Singleton**
          ```java
          public class LazySingleton {
              private static LazySingleton instance;
              private LazySingleton(){}
              public static LazySingleton getInstance(){
                  return instance == null ? instance = new LazySingleton() : instance;
              }
          }
          ```

      3. **Static Singleton**
          ```java
          public class StaticBlockSingleton {
              private static StaticBlockSingleton instance;
              private StaticBlockSingleton(){}
              
              //static block initialization for exception handling
              static{
                  try{
                      instance = new StaticBlockSingleton();
                  }catch(Exception e){
                      throw new RuntimeException("Exception occured in creating singleton instance");
                  }
              }
              public static StaticBlockSingleton getInstance(){ return instance; }
          }
          ```
      4. **Thread Safe Singleton**, с double check locking в Java для которой выполняется happen before
            ```java
            public class ThreadSafeSingleton {
                private static volatile ThreadSafeSingleton instance; // volatile чтобы взять переменную не из кэша CPU, а актуальную; вызывает happens before
                private ThreadSafeSingleton(){}
                public static synchronized ThreadSafeSingleton getInstance(){
                    if (instance == null) {	// сначало проверяем, только потом захватываем монитор
                        synchronized (DclSingleton.class) {
                            if (instance == null) {
                                instance = new DclSingleton();
                            }
                        }
                    }
                    return instance;
                }
            }
            ```
      5. **Enum Singleton**
            ```java
            public enum EnumSingleton {
                INSTANCE;
                public static void doSomething(){
                    //do something
                }
            }
            ```
    </details>

* **Builder** - создает сложный объект пошагово (инициализируя его). Может использовать один и тот же код для строительства разных объектов (с общим предком).

* **Factory** - еще называют "smart constructor", т.е. просто создает сложный объект, аналог конструктора.  
Самая простая Factory: `class A { make() { return new A(); } }`

* **Abstract Factory** - есть общий интерфес для всех наследников, они его переопределяют и создают каждый разные типы объектов, хотя имя метода то же. Используется в dependency injection/strategy.

* **Factory Method (Virtual Constructor)** - как Factory, только метод фабрики абстрактный и может быть переопределен в наследниках

* **Factory vs Builder** - Factory простая версия Builder. Можно рассматривать ее как обертку вокруг конструктора, с отличием от конструктора в том, что она создает объект за 1ин шаг. Factory создает подтипы одного и того же объекта. Builder сложнее и создает объект по шагам.

* **Prototype** - 

## Structural patterns (Структурные)

* **Proxy** - прокси между объектом к которому нужно получить доступ. Перехватывает вызовы методов объекта изменяет их. (e.g. реализацию отложенной инициализации затратных ресурсов при первом обращении)
  * **Note.** В AOP, если связывание (weaving) сделано через method invocation, то вызов метода proxy внутри другого метода того же proxy не вызовет сквозную задачу, это следствие паттерна proxy. Так работает Spring AOP, но не AspectJ при использовании weaving во время компиляции компилятором AspectJ (для него вызов метода proxy из другого метода proxy тоже будет проксирован).
  * <details>
    <summary>Пример Proxy</summary>

    ```java
    public class Math implements IMath {
        public double add(double x, double y) {
            return x + y;
        }
    }

    public class MathProxy implements IMath {
        private Math math;
        public double add(double x, double y) {
            if(math == null) {
                math = new Math(); // отложенная инициализация для экономии ресурсов
            }
            return math.add(x, y);
        }
    }

    IMath p = new MathProxy();
    ```
    </details>

* **Adapter (Wrapper)** - оборачивает вызов метода в свой метод и перед передачей параметров в обернутый метод "адаптирует" их (меняет формат, напр. xml в json). Может "оборачивать" object или class.  
Имеет общий interface со всеми необходимыми методами РАЗНЫХ объектов, так что наследовать Adapter может любой из них, а не нужный метод интерфейса можно просто не использовать.

* **Decorator** - подключает поведение к целевому объекту. альтернатива наследованию классов. Класс-обертка получающая ссылку на классы того же типа что и он сам (т.е. все получаемые декоратором классы и сам декоратор наследуют общий интерфейс). Вызывает какие-то другие методы "до, во время или после" вызова "обернутого" метода.
  * <details>
    <summary>Пример decorator</summary>

    ```java
    public interface InterfaceComponent {
        void doOperation();
    }

    class MainComponent implements InterfaceComponent {
        @Override
        public void doOperation() {
            System.out.print("World!");
        }	
    }

    abstract class Decorator implements InterfaceComponent {
        protected InterfaceComponent component;
        
        public Decorator (InterfaceComponent c) {
            component = c;
        }
        
        @Override
        public void doOperation() {
            component.doOperation();
        }

        public void newOperation() {
            System.out.println("Do Nothing");
        }
    }

    class DecoratorSpace extends Decorator {
        public DecoratorSpace(InterfaceComponent c) {
            super(c);
        }
        
        @Override
        public void doOperation() {
            System.out.print(" ");
            super.doOperation();
        }
        
        @Override
        public void newOperation() {
            System.out.println("New space operation");
        }
    }

    // использование
    Decorator c = new DecoratorHello(new DecoratorComma(new DecoratorSpace(new MainComponent())));
    ```
    </details>

* **Facade** - скрывает сложность. содержит ссылки на несколько разных объектов. Его метод скрывает вызов нескольких методов этих разных объектов.

* **Bridge** - 

* **Composite** - 

* **Flyweight** - 

# Enterprise Integration Patterns

* **Guarantee Delivery** - 

* **The Outbox Pattern** - используется в распределенных транзакций, например микросервисах чтобы обернуть в транзакцию системы, которые не поддерживают распределенные транзакции (kafka)

# Functional

* **Pure function** - функция которая возвращает одни и те же значения для одних и тех же переменных. Т.е. ее состояние не меняется (например через ссылки на объекты, которые в ней содержатся и влияют на ее работу). И ее выполнение не имеет "side effect" (не изменяются local static variables, non-local variables. references). Это аналог математической функции.

# Concurrency

# Architectural

## Service Locator
**Service Locator** - состоит из:
* **Service** - общий интерфес для все сервисов: execute() и getName()
* **Initial Context** - получаем service по его name
* **Cache** - кэширует сервисы

Service Locator - содержит Initial Context и Cache. Если Service есть в Cache, то возвращает его, иначе достает из Initial Context. Возвращенные из Initial Context сервисы имеют общий метод execute()

**Dependency Injection vs Service Locator** - 

# Distributed (Cloud)

Circuit Breaker https://martinfowler.com/bliki/CircuitBreaker.html 

https://medium.com/@kirill.sereda/%D1%81%D1%82%D1%80%D0%B0%D1%82%D0%B5%D0%B3%D0%B8%D0%B8-%D0%BE%D0%B1%D1%80%D0%B0%D0%B1%D0%BE%D1%82%D0%BA%D0%B8-%D0%BE%D1%88%D0%B8%D0%B1%D0%BE%D0%BA-circuit-breaker-pattern-650232944e37

# Other patterns

* **Delegate** - принимает ссылку на объект, оборачивает вызов методов полученного объекта в свои методы, может наследовать интерфейс с методами. Делегирует свою работу другому объекту.
  * **Delegate vs Facade** - Delegate имеет ссылку только на один объект и он принимает ссылку на целевой объект **при создании Delegate**. А 

# SOLID

principles (принципы):
1. **Single responsibility** - каждый класс должен отвечать за ОДНУ обязанность
2. **Open-closed** - открыты для расширения, закрыты для изменения (наследование)
3. **Liskov substitution** - ссылка базового может указывать на наследника
4. **Interface segregation** (разделения) - разделять универсальные метод на много спец. методов (с меньшим количеством параметров)
5. **Dependency inversion** - модули верхних уровней не зависит от модулей нижнего,
   * т.к. система строится на основе абстракций (Абстракции не должны зависеть от деталей. Детали должны зависеть от абстракций)

# is A и Has A

**is A** (Inheritance) - является ЛИ класс наследником другого, Если есть is A мы делаем extends  
**Has A** (Composition) - содержит ли класс сущность, если есть has a мы делаем переменную класса

`class Mercedes extends Car {} // Mercedes is A Car (use Inheritance)`  
`class Car { Radio radion } // car has a radio (use Composition)`

# Cohesion и Coupling

**Cohesion** - (single responsibility), low Cohesion когда класс должен брать на себя как можно меньше ответственности (реализовывать как можно меньше функций, больше функций выносить в другие классы)

**Coupling** - (low coupling == хорошая инкапсуляция), классы должны как можно меньше знать друг и друге (т.е. должны видеть меньше public переменных друг друга)

# immutable объект, Создание immutable объекта

**Создание immutable объекта:**
1. не делать сетеры
2. Все поля private final
3. Методы final (чтобы не дать override)
4. Все изменения mutable полей возвращают НОВЫЙ объект. immutable поля возвращать как обычно.

Дополнительно: можно сделать private constructor и создавать в factory methods

**Плюсы:**
* могут использоваться в кэшах, работа с кэшированными объектами может быть быстрее (e.g пулы в Java основанные на Flyweight);
* в многопоточности параллельные потоки не могут случайно изменить такие объекты.

**Note.** В некоторых коллекциях (например Set в java) где используется hash code добавленного обьекта изменение полей этого обьекта "испортит" hash code (он перестанет быть таким как при добавлении) и т.к. первоначально поиск идет по hash code, то работа такой коллекции сломается. Чтобы такого не случилось можно добавлять в коллекции immutable объекты.

# Dependency Injection



# SAM Pattern

SAM паттерн (не один из основных, возможно относит к функциональному программированию)
    https://github.com/jeffbski/redux-logic#implementing-sampal-pattern

# Гексагональная архитектура
https://www.google.com/search?q=гексоганальная+архитектура&hl=ru

# Список ООП vs functional концепций:

https://fullstackengine.net/fullstack-javascript-interview-questions/