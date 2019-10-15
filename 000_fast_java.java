Fine grained и Coarse grained сервисы и общая архитектура (слои) сервисов в картинке и с пояснением:
https://stackoverflow.com/a/30486061
---
new Double(1/0.).isInfinite(); //true
new Double(0/0.).isNaN(); //true
--
	byte	8		-128..127
	short	16		-32768..32767
	int		32
	long	64
с плавающей точкой
	float	32		1.4e-045..3.4e+038
	double	64
символы (тоже целочисленный)
	char	16		положительные значения
логические
	boolean 1bit (true/false не преобразуются в 1/0)
	
в char хранятся символы Unicode,
ASCII символы 0..127
ISO-Latin-1 - 0..255

16 ричные литералы с плавающей точкой обозначаются p вместо e:
0x12.2P4 == 0x12.2 * (2 * 2 * 2 * 2);
--
литералы - 'x', "hello", 100, используются для постоянных значений
	int x = 07; - восьмиричный
			0xA или 0Xa - шеснадцатиричные (регистр не значит)
--
Порядок инициализации таков:
    Статические элементы родителя
    Статические элементы наследника
    Глобальные переменные родителя
    Конструктор родителя
    Глобальные переменные наследника
    Конструктор наследника
--
int[] x = new int[Integer.MAX_VALUE]; // error
int[] x = new int[Integer.MAX_VALUE - 8]; // правильно, 8 размер спец. заголовков
--
АВТОприводимости В char или boolean не существует
но ИЗ char есть
(не путать с назначением переменной char c = 88, так ОК)

при приведении дробного типа к целому отбросится дробная часть
--
//сравнение дробных чисел
System.out.println("|f3-f4|<1e-6: "+( Math.abs(f3-f4) < 1e-6 ));
1e-6 == 1 / (10)*6; //true
--
Модификаторы классов и полей по умолчанию:

1. class field/method/nested class - package local, non-final, non-static
2. enum and nested enum - package local, final and static
3. interface field - public static final
4. interface method - public abstract
5. nested class in an interface - public static, non-final

Note. Хотя enum в классе неявно static само слово static можно всеравно написать явно перед enum, но хотя оно еще и final само слово final написать явно нельзя.

При это единственное место где нужно использовать static с enum это при import static:
	import static my.embedded.EnumType.SOME_VALUE;
--
в case допустимы byte, short, int, char, enum
	начиная с jdk7 и String (но он медленнее обычных)
	одинаковых значений в case быть не может,
		и там не могут стоять выражения, только готовые значения
--
if, switch (в блоке case), try (включая finally и catch)
	блоки со своей областью видимости
--
try { return 1; }
finally { return 3; } // затрет return 1;

finally НЕ будет вызван есть:
System.exit(), JVM crashes,
infinite loop в try or catch,
во всех случаях когда JVM внезапно завершилась
--
MyEnum mE = MyEnum.A;
mE == MyEnum.A; //true
switch(mE) { //тип перечисления ДОЛЖЕН быть один и тот же
	case A: //ДОЛЖЕН указыватся без имени класса
}

	MyEnum mE = (MyEnum.values())[0]; // == MyEnum.A
public static тип_перечисления valueOf(String) //возвращает константу перечисления с данным именем

члены перечисления - константы перечисляемого типа, они public static final
Перечисление не может быть суперклассом И не может наследовать другой класс.
НО оно МОЖЕТ реализовывать интерфесы.
Конструктор может быть только private или package!
enum My implements MyInteface {
	A(0),B(5), C("str");
	
	private int j;
	
	My(int i){ j = i; }
	My(String str){}
	
	void f1(){}
}
My.A.f1(); //вызов из объекта

== - можно сравнить ссылки на перечисления
final int ordinal() //возвращает порядок константы на которой вызван, номерация с 0
final int compareTo(тип_перечисления enum) //сравнивает порядковые НОМЕРА констант, возвращает -1, 0, 1
equals(Object obj) //возвратит true только если константы совпали

Enumeration - аналог Iterator, не рекумендуется, но иногда используется
    boolean hasMoreElements()
    E nextElement()
Iterator и Enumeration:
    Enumeration в два раза быстрее Iterator и использует меньше памяти.
    Iterator потокобезопасен, т.к. не позволяет другим потокам модифицировать коллекцию при переборе.
    Enumeration можно использовать только для read-only коллекций. Так же у него отсутствует метод remove();
        Enumeration: hasMoreElement(), nextElement()
        Iterator: hasNext(), next(), remove()
--
Доступ к НЕ static членам внешнего класса из вложенного можно получить через имя класса (как для default метода в интерфейсе):
	A.this.x
--
(внутренний)
//к вложенному static доступ через имя
OuterClass.StaticNestedClass nestedObject = new OuterClass.StaticNestedClass();

(вложенные)
class OuterClass {
    class InnerClass {}
}
OuterClass outerObject = new OuterClass();
//но ССЫЛКУ создать можно и без new
OuterClass.InnerClass innerObject = outerObject.new InnerClass();

private класс не виден снаружи и не удастся создать даже ссылку

class C implements A.B {} //использование

Вложенный класс НЕ может иметь static члены, КОТОРЫЕ НЕ final;
Вложенный класс НЕ может иметь static методы.
Вложенный класс может иметь static члены и методы, если он сам static
может иметь доступ только к static членам внешнего, если он сам static
Вложенный класс НЕльзя сериализовать отдельно от его хозяина.
--
 Наследование от внутренних классов
class A { class Inner {} }
class B extends A.Inner {
   // !!! InheritInner() {}  - не скомпилится !!
   B(A a) {
      a.super(); // это обязательно
   }
}
new B(new A()); // создание
---
void f1(String s, int ... arr);
--
static String.valueOf(примитивный_тип) - для примитивных типов строку из них
--
Все строки-константы, которые встречаются В КЛАССЕ (поля класса) автоматически интернированы.
(Зачем: при интернировании строк можно получить преимущество в использовании памяти, т.к. вы храните в ней лишь один экземпляр)
new String("test").intern() == "test"; // ==> true
--
Типы классов в java:
    Top level classes
    Interfaces (не могут быть локальными, т.к. Не могут быть объявлены как private/public/protected или static)
    Enum
    Static nested classes
    Member inner classes
    Local inner classes
    Anonymous inner classes
--
Object - предок всех объектов в Java, в том числе массивов (т.к. это тоже объект)

методы:
protected Object clone() throws CloneNotSupportedException - создаёт клон, клонировать можно только объекты реализующие interface Cloneable
    - protected чтобы в наследниках можно переопределить, но сам метод виден не был
    - чтобы самому определить какие классы можно clone()
boolean equals(Object object) - сравниваем объект
protected void finalize() - вызывается перед удалением объекта
final Class<?> getClass() - класс объекта во время выполнения
int hashCode() - хэш объекта; адрес объекта в памяти, если метод не реализован
String toString() - возвращает строку при выводе в поток
					при выводе в поток берется он вызывается автоматически, строка из этого метода - System.out.println(myObj);

final void notify() - возобновляет работу потока синхронизированного на объекте (или любого одного, если остановленных много)
final void notifyAll() - тоже для всех потоков
final void wait() - останавливает поток на объекте
final void wait(long милисек.) - останавливается, максимальное время остановки
final void wait(long милисек., int наносек)
--
Иерархия исключений:
	Throwable //корень (сам переопределяет Object?)
		Exception //пользовательские, от него наследовать при создании своих исключений
			RuntimeException //(непроверяемое) деление на ноль, выход за границы массива
		Error //(непроверяемое) ошибки среды, не перехватываются как правило
Не обработанные исключения попадают в обработчик по умолнию. Он прерывает работу программы! Который выводит его описание и трассировку стека.
throws НЕ ОБЯЗАТЕЛЬНО ОБЪЯВЛЯТЬ для Error и RuntimeException и их наследников
 из того же метода можно выбросить любого наследника исключений из throws
 throws исключения в переопределенных методах могут только сужаться. Исчезнуть совсем или быть наследниками типа исключения выбрасываемого в переопределяемом методе.
 
В JDK7:
1) try с ресурсами, при этом переменные из try считаются final
	try(FileOutputStream fin = new FileOutputStream("name");
		FileOutputStream fin2 = new FileOutputStream("name")) {
	} catch(Exception e) {}
2) Re-throw, ислючение должно БЫТЬ final ИЛИ ИСПОЛЬЗОВАТЬСЯ КАК final (быть НЕЯВНЫМ)
try{
    throw new IOException(); //Здесь происходит что-то что выкидывает исключение
}catch(Exception e){
    throw e; //Здесь мы ловим что попало, например для логирования. А потом выбрасываем дальше. Это и есть Re-throw.
}
3) многократный перехват, Exception должно быть неявно final
catch(ArithmeticException | ArrayindexOutOfBoundsException e){} //e - final
--
Методы Thread:
Thread(String name)
Thread(Runnable r, String name)
Thread(ThreadGroup tg, Runnable r, String name)
public static Thread currentThread() - ссылка на текущий поток
final String getName()
final void setName(String name)
final Boolean isAlive() //true - поток рабоает, false - завершен
final void setPriority(int level) //установка приоритета потока
final int getPriority()
static void sleep(long ms) throws InterruptedException
static void sleep(long ms, long nano) throws InterruptedException
public void run()
start()
final void join() throws InterruptedException //вызывающий поток ожидает когда указанный поток "присоеденится к нему" (завершится)
join(ms)
interrupt() //прерывает выполнение потока
isInterrupted() // проверяет флаг прерванности

статические методы Thread:
interrupted() //проверяет на прерванность и СБРАСЫВАЕМ ЕГО флаг
currentThread() //
activeCount() //возвращает количество потоков текущей группы
yeild() //приостанавливает текущей поток чтобы позволить работать остальным
sleep()

Thread.State getThreadState() - состояние потока
final ThreadGroup getThreadGroup() - текущая группа
static boolean holdsLock(Object obj) - true если вызывающий поток владеет блокировкой obj

состояния:
	готов к выполнению, когда получил время ЦП (создание Thread?)
	выполняется (запуск start)
	приостановлен (sleep?)
	возобновлен (notify?)
	ожидает освобождения ресурса, заблокирован (wait?)
	прерв, прерванный поток нельзя восстановить (irrupt? или конец метода run())

Обмен сообщениями потоков - это методы для работы с потоками (notify(), wait() и т.д.)
вытесняющая многозадачность - может быть приостановлен jvm на основе приоритета 
Приитет от 1 до 10 (MIN_PRIORITY - MAX_PRIORITY). По умолчанию 5 (в т.ч. для группы)

RuntimeException можно перехватить в Thread,
если объявить обработчик Thread.setDefaultUncaughtExceptionHandler
(который обрабатывает неперехватываемые RuntimeException)
Как работает:
    1. если обработчик есть в Thread, то перехват
    2. иначе если обработчик есть в ThreadGroup, то перехват
    3. Иначе в jvm

Пример setDefaultUncaughtExceptionHandler:
t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
    public void uncaughtException(Thread t, Throwable e) {            System.out.println("exception " + e + " from thread " + t); }
});

--
Для создания потока нужно создать объект Thread:
1. реализовать интерфейс Runnable и передать его параметром в конструктор new Thread(r).start()
2. Override метод run класса Thread: new Thread(Thread(){start();} run(){...})

Еще один способ создания потока (не Runnable или Thread):
    ExecutorService pool = Executors.newFixedThreadPool(3);
    Callable<Integer> callable = new Callable() { call() { return "OK"; } }; // call() аналог run()
    Future<Integer> future = pool.submit(callable); // submit() аналог start()
    String result = future.get(); // get() == join()
    
    Прим. Есть еще метод Executor.execut(new Runnable(){ run(){} });
        future.get() возвращает значение (ИЛИ null, если используется Runnable, а не Callable)
    
13. Как принудительно остановить поток?
    В Java 8 нет метода, который бы принудительно останавливал поток.
    Класс Thread содержит в себе скрытое булево поле, которое называется флагом прерывания. Установить этот флаг можно вызвав метод interrupt() потока.
    Проверяем флаг прерывания:
        interrupt() - ставит флаг прерывания в true???
        bool isInterrupted() - проверяем флаг прерывания
        Thread.interrupted() - проверяем флаг прерывания и СБРАСЫВАЕМ ЕГО
		
	в java предпочтительнее для прерывания потока использовать свои реализации с проверкой своего флага прерывания
	чтобы прерываемый поток подождал своей блокировки, а не прерывался сразу,
	это нужно например чтобы корректно завершалась работа с ресурсами работу с которыми нельзя прервать, например с InputStream/OutputStream
	Другими словами: нужно реализовать метод с public флагом (или методом) прерывания, чтобы фоновый поток мог управлять дочерним
	реализуется через проверку флага:
	public class ControlSubThread implements Runnable {
		private final AtomicBoolean running = new AtomicBoolean(false);
		private Thread worker;
		public void interrupt() { // может выкинуть InterruptedException при sleep или другой блокировке
			running.set(false);
			worker.interrupt();
		}
		public void run() { 
			running.set(true);
			while (running.get()) {
				try { 
					Thread.sleep(interval); 
				} catch (InterruptedException e){ // может случиться если interrupt() вызван когда Thread блокирован или sleep
					Thread.currentThread().interrupt();
				}
				// тут код потока
			 } 
		}
	}
	
	прерывание в ExecutorService
	    executor.shutdownNow();  // will interrupt the task
		executor.awaitTermination(3, TimeUnit.SECONDS); // timeout
        
У методов, приостанавливающих выполнение потока, таких как sleep(), wait() и join()
есть одна особенность — если во время их выполнения будет вызван метод interrupt() этого потока,
они, не дожидаясь конца времени ожидания, сгенерируют исключение InterruptedException.

Потоки-демоны в Java (копировано с сайтов)
	//true - Thread поток завершится, когда main Thread завершится
	//false - Thread продолжит работу даже если main Thread завершится
	t.setDaemon(true); //устанавливатеся перед start()
	t.start();
	
ThreadGroup(String)
ThreadGroup(ThreadGroup parrent_group, String name)
tg1.list(); - получить список потоков
--
Реализация start/stop:
	static Object sync = new Object();
	boolean started = true;

	static void stop() {
		started = true;
		synchronized(sync) {
			while(started)
				try { sync.wait(); }
				catch(InterruptedException e) {}
		}
	}

	static void start() {
		started = false;
		synchronized(sync) {
				sync.notify();
		}
	}
--
Thread.State getState(); //State это enum
	BLOCKED - ожидает монитор
	NEW - ещё не запущен
	RUNNABLE - выполняется, или будет выполнятся когда наступит его очередь (по приоритету)
	TERMINATED - завершен
	TIMED_WAITING - поток временно преостановлен с таймером (sleep(222), wait(333), join(2222))
	WAITING - wait и join без таймера
--
Пример deadlock; трудно отловаить потому что нужно подгодать когда он будет

new Thread(() -> {
    synchronized (Lock1) {            
        try { Thread.sleep(10); } // ждем пока 2ой поток захватит Lock2
        catch (InterruptedException e) {}            
        synchronized (Lock2) {
            // сюда не войдет никогда, ждет разлока Lock2
        }
    }
}).start();
new Thread(() -> {
    synchronized (Lock2) {
        try { Thread.sleep(10); }
        catch (InterruptedException e) {}
        synchronized (Lock1) {
            // сюда не войдет никогда, ждет разлока Lock1
        }
    }
}).start();

dead lock
    - deadlock может быть представлен как "контур" (на ориентированном графе путь из вершины в саму себя). Поиск deadlock может быть реализован как поиск входа их вершины в саму себя.
    - явление при котором все потоки находятся в режиме ожидания
	- это ultimate form of starvation
	
live lock - threads не могут продолжать работу, чтобы уступить ресурсы друг другу они постоянно посылают сообщения о том что ресурс свободен (в оригинале: посылают друг другу сообщения что другому потоку можно продолжить работу). Разница с dead lock в том, что потоки не блокированы, работают, но результат их работы - бесконечные попытки уступить ресурс.
	- можно представить как попеременное выставление двух флагов-переменных каждый из которых означает, что одному из двух потоков можно продолжить работу. Первый поток устанавливает один флаг, второй поток второй, первый поток видит что второй флаг изменился и устанавливает второй, второй поток делает тоже самое; и так в бесконечном цикле.

Starvation - это случай, когда процесс не получает нужный ему ресурс по какой-либо причине (мешают другие процессы, планировщик процессов системы не выделяет ресурс и прочее)
	- случается когда слишком низкий приоритет не дает работать
	- планировщик случайно выбирает запускаемые процессы и конкретному процессу не выпадает шанс работать
	- не хватает ресурсов системы, чтобы выделить ресурс процессу
	- ошибка работы планировщика

Пример, один из приемов решения deadlock в DB - произвольное отпускание (releasing) одного из процессов.

Double checked locking - Блокировка с двойной проверкой
    - параллельный шаблон проектирования, сначало проверяется флаг (true/false) блокирован или нет, а только потом делается ПОПЫТКА синхронизации
    - На некоторых языках и/или на некоторых машинах невозможно безопасно реализовать данный шаблон. Поэтому иногда его называют анти-паттерном. Такие особенности привели к появлению отношения строгого порядка «happens before» в Java Memory Model и C++ Memory Model.
--
Уровни удержания?		
//java.lang.annotation.RetentionPolicy
	SOURCE - аннотация видна только в исходнике (напр. для аннотации по которой генерируется документация)
	CLASS (по умолчанию, если не указано) - только во время компиляции в файле .class
	RUNTIME - доступны во время компиляции и после неё в программе (файл .class)
	НО аннотации локальных переменных НЕ ВИДНЫ в .class никогда

@Target(перечисление ElementType) - только для других аннотация, показывает на каких элементах может быть применена аннотация
@Target({ElementType.FIELD, ElementType.PACKAGE})
@Target(ElementType.METHOD)

Перечисление ElementType:
	ANNOTATION_TYPE - аннотация
	CONSTRUCTOR - конструктор
	FIELD - поле
	LOCAL_VARIABLE - локальная переменная
	METHOD - метод: public @Recoпunended Integer fЗ(String str) {}
	PACKAGE - пакет: @PackageAnno package my;
	PARAMETER - параметры метода
	TYPE - класс, интерфейс или перечисление
	
	с JDK8:
		TYPE_PARAMETER - параметр типа (параметр обобщения)
			анотированный параметр обобщения
			class TypeAnnoDemo<@What (description  =  "Данные обобщенного типа" ) Т> {
		TYPE_USE - использование типа (анатируется ТИП), анатировать возвращаемый void нельзя
			 public @A Integer fЗ(String str) {} //тип возврата
			 
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface Inh {} //наследуется

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AContainer.class)
@interface A {
	int val();
	String str();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AContainer {
	A[] value();
}

@A(val = 3, str = "ttt")
@A(val = 7, str = "ttt2")
void f1() {}

Типы которые могут использоваться в аннотациях:
int, double, String, Class, enum, другие аннотации (наследники Annotation?) и все массивы этих типов

Ограничения:
Аннотации не могут наследовать друг друга.
В их методах не может быть параметров.
Аннотации не могут быть обобщенными.
У методов в аннотацих не может быть throws

Hello.class.getMethod("main", Array.newInstance(String.class, 0).getClass()).getReturnType() == Void.TYPE
Также аналог Class для этого типа - void.class

Можно ли получить доступ к private переменным класса и если да, то каким образом?
    Field reflectField = SomeClass.class.getDeclaredField("name");
    reflectField.setAccessible(true); // Если не дать доступ, то будет ошибка
    String fieldValue = (String) reflectField.get(someClass);
    // try{}catch(NoSuchFieldException | IllegalAccessException){}
--
OЬjectOutputStream			Поток вывода объектов
FileOutputStream			Поток вывода, записывающий данные в файл
ВUfferedOutputStream		Буферизированный поток вывода
BufferedWriter
InputStrea.Reade			Поток ввода, преобразующий байты в символы
								(для вкладывания байтовых потоков в символьные)
PrintStreaш
	(наследник OutputStream)	Поток вывода, содержащий методы print ()
								и println ()
PrintWriter					Поток вывода, содержащий методы  print()  и  println  ()
PushЬackReader				Поток ввода, позволяющий возвращать символы обратно
								в поток ввода

PrintStream - добавляет print() метод и не дает Exception, вместо этого флаг checkError
InputStrearnReader(Systern.in) - преобразует Stream в Reader
ReaderInputStream / WriterOutputStream - Reader / Writer в Stream
PrintWriter(OutputStream os) - преобразует Stream в Writer
BufferedReader/ВufferedOutputStream - в них оборачивают чистые Reader/Writer или Stream для буферизации
    методы: br.read(), br.readLine()
FilterReader / FilterWriter - можно переопределить методы read / write как обертки и менять их поведение
StringReader / StringWriter - читае / пишет в строку
ByteArrayOutputStream - содержит байты и методы read()/write()
    new ByteArrayOutputStream().toByteArray(); // == cast to byte[]
    
RandomAccessFile - напоминает использование совмещенных в одном классе потоков
    DataInputStream и DataOutputStream (они реализуют те же интерфейсы DataInput и DataOutput)
    имеет методы seek() - позволяет переместиться к определенной позиции и изменить хранящееся там значение
    содержит методы для чтения и записи примитивов и строк UTF-8.
RandomAccessFile может открываться: чтения («r») или чтения/записи («rw»), «rws» (сразу запись изменений без flush)

8. Какой класс-надстройка позволяет читать данные из входного байтового потока в формате примитивных типов данных?
    DataInputStream - Для чтения байтовых данных
        readByte() - для чтения байтов, не читает EOF байт (не различает)
        available() - сколько байт осталось до конца (т.к. через readByte() это не узнать)
        Конструктор: DataInputStream(InputStream stream)
        Методы: readDouble(), readBoolean(), readInt()
    ByteArrayInputStream - подходящий для передачи в DataInputStream (String можно преобразовать через getBytes() )
    
FilenameFilter:
FilenameFilter с методом accept() работает как фильтр списка файлов:
    String[] myFiles = directory.list(new FilenameFilter() {
    public boolean accept(File directory, String fileName) {
        return fileName.endsWith(".txt");
    }

Входные данные разбиваются регулярками на куски и читаются по очереди методом next()
sc = new Scanner(new FileReader("file.txt"))
Пример: sc.nextDouble() - читает и double и int (т.к. его можно преобразовать)

Читаем через буфер, чтобы не было переполнения!!!:
byte[] buffer = new byte[32768];
fin = new FileInputStream("name.io");
do {
    char c = (char) fin.read(buffer);
} while(i != -1);

Еще один пример чтения:
    FileInputStream fis= new FileInputStream("bla");
    byte[] buf = new byte[1024];
    int len;
    while((len=fis.read(buf))>0){
        zos.write(buf, 0, len);
    }

байтовые:
write(int байт) - зписывает только 8 младших бит int
	int a = 'A';
	System.out.write(a);
	
FileInputStream fi = new FileInputStream("/file.io");
//или так
FileInputStream fi = new FileInputStream(new File("/file.io"));
--
Console c = System.console();
String s = c.readLine(); // и много др. методов
PrintWriter pw = c.writer();
Reader r = c.reader();
--
Для хранения интернацилизаций (строк на разных языках) и переключения между ними:
ResourceBundle - , ListResourceBundle - массив значений, PropertyResourceBundle
ResourceBundle stats = ResourceBundle.getBundle("StatsBundle", currentLocale); // вытаскиваем StatsBundle_ja_JP с суфиксом ja_JP
--
описать методы AtomicReference
compareAndSet() - сравнивает и устанавливает, видимо чтобы проверить не изменилось ли значение
lazySet()

также есть: AtomicInteger, AtomicLong, AtomicIntegerArray etc
--
iOb instanceof Gen<?> - проверка можно ли привести класс к Gen

null instanceof SomeClass == false
--
Ограничивать можно ОДНИМ классом и несколькими интерфейсами
class Gen<T extends MyClass & MyInterface1 & MyInterface2 & MyInterface3> 

class A<T> {
	static T ob; //ошибка, нельзя параметры типа в static
	static T f1(){} //ошибка, нельзя static метод с параметром типа КЛАССА
	static <V>f2(V v){} //правильно, НО если параметр у функции свой отдельный от класса то можно
}
--
@FunctionalInterface - маркер, пишется над интерфейсами. Обозначает что интерфейс функциональный (т.е. содержит только один абстрактный метод). Для лямбда выражений. Не обязательна.
    Иначе в таком интерфесе можно будет писать любые методы.
    default методы быть могут в любом случае, а еще методы наследованные от Object?

Можно использовать готовые интерфейсы:
Function<String,  Integer> func = String::length;
			
Class::staticMethodName		(s) -> String.valueOf(s)
object::instanceMethodName	() -> object.toString()
Class::instanceMethodName	(s, s2) -> s.toString(s2)
ClassName::new				() -> new String()

super::имя_метода - ссылка на метод предка
this::имя_метода
EnclosingClass.this::method - для внутреннего класса можно так
MyClass::new - ссылка на конструктор, какой именно конструктор зависит от контекста
LinkedList::new - В ссылках на методы <> не пишется

ЛОКАЛЬНЫЕ переменные использованные внутри лямбда-ф. становятся неявно final

//конструктор массива
MyArrayClassFactory<A[]> mf = A[]::new;
A[] a = mf.func(2);
a[0] = new A();
a[1] = new A();

//1.
interface A {
	//B - ссылка на объект из которого будет вызов: String s = b.f1(n);
	String f1(B b, int n); //String s = (b, n) -> b.f1(n);
}
//2.
class B {
	String f2(int n) {
		return n + "_1";
	}
}
//3.
A a = B::f2;
String s = a.f1(new B(), 2); //тоже самое что: String s = new B().f2(2);

4. Ссылка на обобщенные методы
interface A<T> {
	int func(T val);
}
class B {
	static <T> int f1(T val) {
		return String.valueOf(val);
	}
}
A<T> mth = B::<String>f1; //тип после :: относится к методу

//5 применяются для фабрик объектов
для обобщенного конструктора:
interface A<T> {
	int func(T[] val);
}
class B<T> {
	B(T val) {
		val.bla();
	}
}
A<Integer> cnstr = B<Integer>::new; //тип перед :: относится к классу
B b = cnstr.func(); //вызов конструктора из ссылки на него
--
Spliterator<A> spl1 = listStream.spliterator();
while( spl1.tryAdvance( (n) -> System.out.println(n) ) ); // hasNext() + next()
Spliterator<A> spl2 = spl1.trySplit(); // получим 2ую половину
spl1.forEachRemaining((n) -> System.out.println(n)); // forEachRemaining() многопоточный и только из spliterator
spl2.forEachRemaining((n) -> System.out.println(n));
--
		cunsumer - то что принимает значения (потребляет) и НЕ ВОЗВРАЩАЕТ результат
			DoubleConsumer: void accept(double d)
		function - то что оперирует РАЗНЫМИ (или одним) объектами и даёт результат
			DoubleFunction: R apply(V v, T t)
		operator - опирирует двумя ОДНОТИПНЫМИ объектами и даёт результат
			DoubleOperator: R apply(Double i1, Double i2)
		predicate - принимает разные объекты и ВОЗВРАЩАЕТ boolean
			DoublePredicate: boolean test(V v)
		supplier - НЕ ПРИНИМАЕТ ПАРАМЕТРЫ (объекты), сам что-то делает и возвращает результат
			DoubleSupplier: T get()
			
		Приставки:
			Bi - если принимает 2 аргумента (BiFunction)
			To - если приобразует объект в другой (ToIntBiFunction)
			Unary - если оперирует только одним параметром (UnaryOperator)
--


5. Через Statement выполняем запрос:
	new Statement().executeQuery("SELECT * FROM users WHERE user='...")
	new Statement().executeUpdate("INSERT, UPDATE, DELETE ...");
	execute("any SQL or procedure"); // любой  SQL или процедура возвращающая много результатов
	
	try {
		Connection con = (Connection) DataSourceSingleton.getInstance();
		try {
			Statement stat = (Statement) con.createStatement();
			try {
				ResultSet res = new Statement().executeQuery("SELECT ...");
				while(res.next()) str = res.getString("filePath");
			} finally { if (res != null) res.close(); }
		} finally {if (stat != null) stat.close();}
	} catch(SQLException e) { e.printStackTrace(); }
	finally { if (con != null) con.close(); }
		
--
batch-команда из PreparedStatement
	Выполняет группу SQL
	conn.setAutoCommit(false); //отключаем автовыполнение запроса сразу?
	Statement st = conn.createStatement();
	st.addBatch("INSERT INTO ...");
	st.addBatch("INSERT INTO...");
	int [] sqlCount = st.executeBatch(); //число строк изменное конкретным запросом
	
	//в цикле, БД в зависимости от настроек может кэшировать PreparedStatement
	PreparedStatement ps = conn.preparedStatement("insert ... values(?,?,?)");
	for( : ) {
		ps.setInt(1, 354);
		ps.setString(2, "bla");
		ps.setInt(3, 1231);
		ps.addBatch(); //добавляем процедуру с новыми значениями
	}
	int rowCount[] = ps.executeBatch();
--
	--CallableStatement (extends PreparedStatement)
		Используется для вызова хранимых процедур БД.
		Может также получать возвращаемые процедурами значения (IN, OUT, INOUT).
		
		CallableStatement cs = conn.prepareCall("{call myProc(?, ?)}");
		cs.setInt(1, 12313);
		cs.registerOutParameter(2, java.sql.Types.VARCHAR); //второй параметр процедуры как return
		cs.execute();
		String lastName = cs.getString(2); //читаем возвращенное значение из второго параметра процедуры 
--
// только COUNT считает NULL, остальные функции NULL игнорируют

// NULL + дубликаты, нельзя использовать с DISTINCT
SELECT COUNT (*) FROM Customers

// считаем не NULL значения
SELECT COUNT ( ALL rating ) FROM Customers;

// еще обходное решение для DISTINCT
SELECT Count(*) AS DistinctCountries
FROM (SELECT DISTINCT Country FROM Customers);
--
ALTER TABLE "Заказ"
CHANGE COLUMN "number" "proj" INT NOT NULL IDENTITY,
ADD PRIMARY KEY("proj");

ALTER TABLE "Заказ"
ADD COLUMN cont VARCHAR(50)
AFTER first_name;
/* BEFORE FIRST LAST */

/* опции ALTER TABLE:
CHANGE - имя и тип столбца
MODIFY - тип и позиция
ADD - добавление + тип, column, constraint, PRIMARY KEY(bla), CHECK etc
DROP - удаление column, constraint, CHECK etc
RENAME TO ropo; - переименование табл.
*/

//добавление нескольких столбцов за 1 команду
alter table
   table_name
add
   (
   column1_name column1_datatype column1_constraint, 
   column2_name column2_datatype column2_constraint,
   column3_name column3_datatype column3_constraint
   );
   
CREATE VIEW [Brazil Customers] AS
SELECT CustomerName, ContactName
FROM Customers
WHERE Country = "Brazil"; 
--
// HAVING используется с SELECT + GROUP BY и агрегатными функциями (выполнено ПОСЛЕ)
// WHERE используется с SELECT, DELETE, UPDATE (выполняется ВО ВРЕМЯ запроса)
SELECT name, SUM(salary)
FROM Employees
GROUP BY name
HAVING SUM(salary) > 1000
--
CREATE TABLE "Отзывы"(
	"Код отзыва" INT NOT NULL IDENTITY,
	"Код диска" INT NOT NULL,
	CONSTRAINT FK_Заказ_Клиенты
	FOREIGN KEY ("Код диска")
	REFERENCES "Клиенты" ("Код клиента");
)
--
SELECT "имя_студента"
FROM "Студент"
WHERE EXISTS (SELECT *
				FROM "Группа"
				WHERE "Группа"."имя_группы" = 'ПМ-21'
				AND "Группа"."номер_группы" = "Студент"."номер_группы")

SELECT name
FROM "table"
WHERE rating > ALL (SELECT rating FROM "table2"
					WHERE rating > 3 AND rating < 7);
--
/*Количество столбцов в группе, NULL не считается*/
SELECT COUNT(sales)
GROUP BY name
FROM cook;
--
/*Количество НЕ повторяющихся столбцов*/
SELECT COUNT(DISTINCT sale)
FROM cook;
--
INSERT INTO "result"(отзыв, дата, имя_клиента)
SELECT "Отзывы".*, NULL /*имя клиента null*/
FROM "Отзывы"
WHERE "Отзывы"."Код клиента" = 3;

SELECT *
INTO Persons_Backup IN 'Backup.mdb'
FROM Persons

CREATE INDEX idx_lastname
ON Persons (LastName); 

DROP INDEX index_name ON table_name;

дубликаты запрещены
CREATE UNIQUE INDEX index_name
ON table_name (column1, column2, ...)
--
поведение табл.-потомков при изменении родительской табл.
FOREIGN KEY (usr_id) REFERENCES usr(usr_id)
  ON UPDATE CASCADE
  ON DELETE RESTRICT,
  
  CASCADE - по цепочке
  SET NULL
  NO ACTION == RESTRICT - тоже самое, что не указывать ON совсем
  SET DEFAULT
--
это называется MERGE в некоторых DB
это когда, если в добавляемой табл уже есть такое же значение в unique или primary столбце, то оно будет заменено на другое по правило которое написано после key update
    INSERT INTO t1 (a,b,c) VALUES (1,2,3)
    ON DUPLICATE KEY UPDATE c=c+1;
	
//вставка нескольких значений в insert into
INSERT INTO MyTable ( Column1, Column2 )
VALUES
	( Value1, Value2 ),
	( Value1, Value2 ),
	('Robert', 'Smith');
--
trigger - хранимая процедура, выполнение которой можно повесить на события. Синтаксис зависит от БД.
Часто события это: INSERT, DELETE, UPDATE. Список событий зависит от БД.

1. Schema-level triggers - запускаются на действия с данными БД
    After Creation
    Before Alter
    After Alter
    Before Drop
    After Drop
    Before Insert
    
2. System-level triggers - на действия самой системой БД: вход, выход, запуск

 CREATE [OR REPLACE] TRIGGER DistrictUpdatedTrigger
 AFTER UPDATE ON district
 BEGIN 
  insert into info values ('table "district" has changed');
 END;
--
b = (byte) i; // b = i % sizeof(byte) если i>byte иначе b = i
если i больше типа byte, то b будет == i % byte (остатку от деления на размер типа)
--
@Override
public boolean equals(Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Student))
        return false;
    if (obj == this)
        return true;
    return this.getId() == ((Student) obj).getId();
}
--
Редирект:
	request.getRequestDispatcher("/jsp/result.jsp").forward(request, response);
	request.getRequestDispatcher("login.html").include(request, response); // как include в jsp, код продолжает работать с места вызова
        Ограничения include(): не может вызывать методы response, которые меняю headers для response, например setCookie()
	response.sendRedirect() - прямая переадрессация
	Отличие: в forward используется тот же самый request и он быстрее
--
из пакета java.util:
    ArrayList — основной список, основан на массиве
    LinkedList — полезен лишь в некоторых редких случаях
    Vector — устарел, синхронный
    
    Переделаны для поддержки Collection:
        Vector, Stack, HashTable

    Хэширование - обеспечивает постоянное время для выполнения add(), remove(), size() даже для больших множеств

    ArrayDeque — основная реализация, основан на массиве
		add/addFirst/addLast
		getFirst/getLast
		removeFirst/removeLast
		offerFirst/offerLast - false при ошибке
		peekFirst/peekLast - null при ошибке
		pollFirst/pollLast - возвращает и удаляет, null при ошибке
		pop - возвращает из головы и удаляет элемент, если очередь пуста - исключение
		push() - вводит элемент в голову
    Stack — устарел
    PriorityQueue — отсортированная очередь
        E element() - вернет первый элемент, если очередь пуста - исключение
        E remove() - возвращает элемент и удаляет его, если очередь пуста - исключение
        boolean offer(E ob) - ПЫТАЕТСЯ ввести в очередь, не получилось - false (для очередей с фиксированным размером)

        E peek() - вернет первый элемент, если очередь пуста - null
        E poll() - возвращает первый элемент и удаляет его, если очередь пуста - null


    HashMap — основная реализация
    EnumMap — enum в качестве ключей
    Hashtable — устарел
    IdentityHashMap — ключи сравниваются с помощью == (использует System.identityHashCode(Object) вместо hashCode)
    LinkedHashMap — сохраняет порядок вставки (можно extends и сделать кэш LRU, переопределив его removeEldestEntries() (вызывается как прокси при put и putAll) для удаления старейшего элемента  и поставив accessOrder=true для группировки старых вначале, а новых вконце)
        linked означает, что внутри LinkedHashMap и LinkedHashSet двух связный список (т.е. добавлены 2 поля before и after)
        если установить параметр конструктора accessOrder = true,
            то при вызове get или put для элемента этот элемент перемещается вконец списка (порядок обращения)
        LRU - last recently used
    TreeMap — сортированные ключи
    WeakHashMap — слабые ссылки, полезно для кешей
        ключи в ней это WeakReference
        если на key не существует ссылок кроме тех что в WeakHashMap,
        то entry из WeakHashMap авто удаляется


    HashSet — основная реализация множества,
        по умолчанию емкость 16, коээфициент_заполнения == 0.75 по умолчанию (от 0.0 до 1.0)
        если количество_элементов > емкость * коээфициент_заполнения --- происходить расширение
    EnumSet — множество из enums, использует биты (bit vector) для хранения,
        операции выполняются быстрее, за O(1); containsAll и retainAll выполняются быстро, ЕСЛИ их аргумент Enum
    BitSet* — множество битов
    LinkedHashSet — сохраняет порядок вставки
    TreeSet — отсортированные set
		E ceiling(E obj) - поиск наименьшего e по критерию e >= obj
		E floor(E obj) - наибольшего по e <= obj
		E lower(E obj) - наименьшего по e < obj
		E higher(E obj) - наибольшего по e > obj
		
		E poolFirst() - возвращает первый и удаляет его из мн-ва
		E poolLast() - последний
		
		NavigableSet<E> descendingSet() - обратный NavigableSet по отношению к вызвавшему NavigableSet
		Iterator<E> descendingIterator() - для перебора с конца
	
	Set & List
	add(el), add(int index, E obj) - добавляет
	set(i, el) - заменяет
	remove(i), remove(ob)
	
	Map
	V get(Object k) 
	V put(K k, V v)
	putAll(entries)
	containsKey(k) / containsValue(v)
	Set<Map.Entry<K, V>> entrySet()            
	keySet() - множество ключей
	values() - коллекция Collection (как List) значений
	trimToSize() - уменьшения длины до размера == текущему количеству элементов
    С JDK8:
        ...IfAbsent() - методы применяют операцию если элемент НЕ существует
        compute("myKey", (k,v)->{...}), computeIfAbsent(), computeIfPresent(),
            - как map() только к 1му элементу с совпавшим ключом
        merge("myKey", "newVal", (k, v)-> {...})
            - если k не существует или v == null, то работает как add()
            - иначе заменяет v указанного k на новое (replace())
            - если для k лямбда вернет null, то entry удалется (как remove())
        forEach(), replace(k, (k,v)->{}), replaceAll()
                - принимает указатель на функцию, которая применяется к значению
        default V getOrDefault(Object key, V val) - объект по ключу, если его нет - по умолчанию вернет val
        default boolean remove(Object k, Object v) - если не найдено - false
        default boolean replace(K k, V prev, V new)
                        replace(K k, V v)
---------
TreeSet инкапсулирует в себе TreeMap, который в свою очередь использует сбалансированное бинарное красно-черное дерево для хранения элементов.
O(log(n)) - время add, remove и contains.

Плюсы ArrayList:
Быстрый доступ к элементам по индексу за время O(1);
Доступ к элементам по значению за линейное время O(n);

HashSet реализован на основе хеш-таблицы, а TreeSet — на основе бинарного дерева.

HashSet гораздо быстрее чем TreeSet ( o(1) vs o(log(n)) для add(), remove(), contains() ).
(константное время против логарифмического для большинства операций, таких как add, remove, contains)

В TreeSet и TreeMap не хранится null
Если создать свой Comparator поддерживающий null,
то null можно использовать в TreeSet и TreeMap.

HashMap работает строго быстрее TreeMap (реализован на красно-черном дереве).

HashMap быстрее TreeMap, КРОМЕ ситуаций с вещественными числами,
с ними лучше TreeMap с реализованным компаратором сранивающим числа на равенство (если разница меньше 1e-9).

equals() and hashCode не обязательно реализовывать для TreeSet and TreeMap,
они используют compareTo, но иметь их это хорошая практика.

Хэш код - это int
index = hashcode % table_size (количество buckets)

bucket - это список (массив) в котором index это hashcode.
С технической точки зрения «корзины» — это элементы массива, которые хранят ссылки на списки элементов.
Добавление, поиск и удаление элементов выполняется за константное время.

equals и hashCode обязателен для: HashMap, HashSet и Hashtable

1. В JDK 7 и ниже, для хранения пар в одной корзине используется linked list;
2. В JDK 8 для этой цели используется balanced tree, следовательно, в худшем случае, значение по ключу может быть получено уже за O(log n).

Если bucket становится большой, то оно организуется в Red-Black tree (т.е. bucket может быть списком в виде дерева).

HashSet работает как HashMap:
Внутри HashSet используется private transient HashMap<E,Object> map;
В качестве ключа здесь используется переданный в метод add(...) объект,
а в качестве значения – объект заглушка класса Object.

В HashMap можно использовать неправильный hashCode (с одинаковым значением), но тогда получится аналог List (сравнение по equals).

Примерные размеры структур (ПРИМЕРНО, уточнить!):
    LinkedList == 3 указателя на prev, next, value это 3*8 байт (в 64бит jvm) + размер самого УПАКОВАННОГО В ОБЪЕКТ объекта напр. 8 байт == 32 байта
    ArrayList - основан на массиве, поэтому будет только размер массива + размер УПАКОВАННОГО объекта
    
Arrays.asList({1, 2, 3}) - возвращает List фиксированной длины, в него НЕЛЬЗЯ УДАЛИТЬ или ДОБАВИТЬ элементы
    subList(3, sourceList.size() - 3) - как asList только берет sub list

Как преобразовать одну Collection в другую (set в list etc): в основном передать в конструктор new List(set), для Map возможно иницилизировать в цикле?
---------          
Коллекции из java.util.concurrent (НЕ ВСЕ):
    CopyOnWriteArrayList
        - если много операций read и мало write; (частые чтения редкая запись)
        - изменяющие операции (add, set etc) реализованы через
            пересоздание Array и копирование старого содержимого в новый array
    ArrayBlockingQueue (extends BlockingQueue)
        - извлечение из пустой queue или добавление в полную
            заблокирует поток пока очередь не будет готова выполнить операцию
        - для обмена сообщениями между потоками через queue
    ConcurrentMap
        - с обычным synchronized Map есть вероятность что 2 разных потока
            выполнят put() одновременно и значение затрется
            для решение добавлен метод putIfAbsent()
    SynchronousQueues (extends BlockingQueue)
        - ПУСТАЯ очередь для обмена сообщениями между потоками через нее
        - позволяет вставить элемент в очередь только в том случае,
            если имеется поток, ожидающий поступления элемента для обработки
---------

-----------------------------------------------------------------------------------------------------
Interface	Hash Table	Resizable Array		Balanced Tree	Linked List		Hash Table + Linked List
-----------------------------------------------------------------------------------------------------
Set			HashSet	 						TreeSet	 						LinkedHashSet
List	 				ArrayList	 						LinkedList	 
Deque	 				ArrayDeque	 						LinkedList	 
Map			HashMap	 						TreeMap	 						LinkedHashMap
-----------------------------------------------------------------------------------------------------
List
	ArrayList - основан на array
		add() - O(1) 
		add(index, element) - O(n)
		get() - O(1)
		remove() - O(n) 
		indexOf() - O(n)
		contains() - как indexOf()
	CopyOnWriteArrayList - полезно для работы с многопоточностью когда проходов по коллекции больше чем изменений, thread-safe без необходимости synchronization, при использовании add() or remove() все содержимое копируется в новый array, поэтому можно проходить по этой коллекции даже в многопоточной среде без синхронизации, iterator() возвращает ссылку на immutable snapshot копию
		add() - O(n)
		get() - O(1) 
		remove() - O(n)
		contains() - O(n)
	LinkedList - список, каждый node имеет 2 ссылки, поэтому расход памяти больше чем в ArrayList
		add() – O(1)
		get() – O(n)
		remove(idx) – O(1)
		contains() – O(n)
Map - Key-value pairs хранятся в buckets, каждый bucket хранит array, hashCode это ключ к bucket. Внутри bucket сравнени идет по equals. В bucket хранится оба ключ и значение в Map.Entry. Если мы знаем key, то получение object идет за constant time, O(1). Если добавить null: map.put(null, null); то его hash будет 0 и он добавится первым элементом. Если сделать map.get("key1") и вернет null, то это значит что либо добавлен null либо такого объекта не добавлено вообще. Чтобы понять null добавлен с ключем или объекта правда нет нужно использовать map.containsKey("key"). HashMap, LinkedHashMap в новой версии Java были переписаны с b-tree вместо LinkedList это ускорило их работу при коллизиях с O(n) to O(log(n)).
	HashMap, LinkedHashMap, IdentityHashMap, WeakHashMap, EnumMap and ConcurrentHashMap - если equals() and .hashcode() реализованы правильно, то вероятность коллизии маловероятна
		get - O(1) или O(log(n)) в случае коллизии
		containsKey, get, put, remove - O(1)
	TreeMap and ConcurrentSkipListMap
		put(), get(), remove(), containsKey() - O(log(n))
Set
	HashSet, LinkedHashSet, and EnumSet - основаны на HashMap
		add(), remove() and contains()  - O(1)
	TreeSet - основан на TreeMap
		add(), remove() and contains() - O(log(n))
	ConcurrentSkipListSet - основан на skip list data structure
		add(), remove() and contains() - O(log(n))
	CopyOnWriteArraySet
		add(), remove() and contains() - O(n)
Queue
                          offer    peek poll      size
	PriorityQueue         O(log n) O(1) O(log n) O(1)
	ConcurrentLinkedQueue O(1)     O(1) O(1)     O(n)
	ArrayBlockingQueue    O(1)     O(1) O(1)     O(1)
	LinkedBlockingQueue   O(1)     O(1) O(1)     O(1)
	PriorityBlockingQueue O(log n) O(1) O(log n) O(1)
	DelayQueue            O(log n) O(1) O(log n) O(1)
	LinkedList            O(1)     O(1) O(1)     O(1)
	ArrayDeque            O(1)     O(1) O(1)     O(1)
	LinkedBlockingDeque   O(1)     O(1) O(1)     O(1)
---------
Отношения эквивалентности equals (для НЕНУЛЕВЫХ ссылок):
    симметричность - x.equals(y) == true, то y.equals(x) == true
    рефлексивность - x.equals(x) == true
    транзитивность - x.equals(y) == true и y.equals(z) == true, то x.equals(z)
    Еще свойства: постоянство и неравнество null
---------
iterator.add() - нету, потому что не нужен

Iterator и Enumeration:
    Enumeration в два раза быстрее Iterator и использует меньше памяти.
    Iterator потокобезопасен, т.к. не позволяет другим потокам модифицировать коллекцию при переборе.
    Enumeration можно использовать только для read-only коллекций. Так же у него отсутствует метод remove();
        Enumeration: hasMoreElement(), nextElement()
        Iterator: hasNext(), next(), remove()
        
Iterator и ListIterator:
    ListIterator только для List, есть previous(), add() и remove()
    Iterator для Set, List и Map, нет remove()

fail-fast интератор - ошибка при попытке изменения извне во время перебора (не методом самого интератора)
fail-safe интератор - работает с копией коллекции, ее можно менять во время перебора
    (пример: итераторы CopyOnWriteArrayList и ConcurrentHashMap; ListIterator)
    
Прим. обход iterator:
1. for (Map.Entry<Integer, Integer> entry : map.entrySet()) {}
2. for (Iterator<Map.Entry<Integer, Integer>> entries = map.entrySet().iterator(); entries.hasNext(); ) {
    Map.Entry<Integer, Integer> entry = entries.next();
}
3. map.entrySet().stream().forEach()
4. map.forEach((k, v) -> i[0] += k + v);
5.
    it = map.entrySet().iterator();
    while (it.hasNext()) { v = it.next(); }
	
static переменные Collection.EMPTY_SET, EMPTY_LIST, EMPTY_MAP - они неизменяемы
synchronizedList(), synchronizedSet() - создают потоко-безопасные копии коллекций
static checkedCollection(), checkedList(), checkedSet() и т.д. - создатут коллекции которые автоматически проверяют тип объектов при добавлении (этими методами нужно пользоваться на этапе отладки)
Методы НАЧИНАЮЩИЕСЯ с unmodifiable (unmodifiableList(list)) - возвращают коллекции которые не могут быть изменены.
copyOf(U[] источник, int длина, Class<? extends T[]> результирующий_тип) - вернет подмножество коллекции (копию)

Collections.singletonList() - создаем immutable list с указанным объектом.
    зачем: заменить операции создать, добавить, сделать unmodifiableList()

.stream()
.parallelStream()
removeIf(Predicate)
toArray(T arr[]) - если массив больше, то пустые будут null, если меньше усечется
boolean removeAll(Collection<?> c)
boolean retainAll(Collection<?> c) - удалить все кроме
boolean contains(Object obj) - содержиться или нет
boolean containsAll(Collection<? extends E> c)

аналог find() из js (нужно чтобы stream() был sequential() ):
                .filter(x -> x > 5)
                .findFirst()
                .orElse(null);
--
C JDK8:
    1. reversed() - вернет Comporator с обратным упорядочиванием
    2. reversedOrder() - обратный порядок сортировки
    3. naturalOrder() - естественный порядок сортировки (использует Comparable вместо Comparator?)
    4. nullsFirst(Comporator comp) - вернет Comporator для которых null == min
    5. nullsLast() - null == min
    6. thenComparing() - если первое сравнение дало равно, то после него эта функция может провести второе сравнение (другое)
				также есть спец. варианты: thenComparingDouble, thenComparingInt и др.
        НО если компоратору передаются оба null - то они считаются равными???

//создание сравнения, которое происходит после того как первое дало равенство
Comporator<String> mc2 = mc.thenComparing(new MyComp2);

1. Collections.sort(list, new MyComporator());
2. Создание нового Comporator с thenComparing():
    Comporator<String> mc = (aStr, bStr) -> aStr.compareTo(bStr);
    Comporator<String> mc2 = mc.thenComparing(new MyComp2);
3. Создание нового Comporator с Comparator.comparing():
    Collections.sort(playlist1,
        Comparator.comparing(p1 -> p1.getTitle())
            .thenComparing(p1 -> p1.getDuration())
            .thenComparing(p1 -> p1.getArtist())
    );
--
StringTokenizer (extends Enumeration) - Разделение текста на части (лексемы) по заданному разделителю (или нескольким)

StringTokenizer s = new StringTokenizer("val=5;val2=6;", "=;");
while(s.hasMoreTokens()) {
	String key = s.nextToken();
	String val = s.nextToken();
}
--
Arrays - набор методов для работы с массивами
sort(arr, comporator)
static int binarySearch(Object arr[], Object val) - только для сортированных массивов
static void parallelSort(тип[] arr) - сортирует сначало части массива, а потом целиком. Быстрее.
toString()
hashCode()
deepToString() - для массивов содержащих вложенные массивы
deepHashCode() - для массивов содержащих вложенные массивы
static <T, U> T[] copyOf(U[] источник, int длина, Class<? extends T[]> результирующий_тип)
	ЕСЛИ копия длиннее, то лишние элементы инициализированны по умолчанию
	arr = Arrays.copyOf(arr, N + 1);
--
1. Конвейерные — возвращают другой stream, то есть работают как builder,
2. Терминальные — возвращают другой объект, такой как коллекция, примитивы, объекты, Optional и т.д.

Типы Stream: Stream, IntStream, LongStream, DoubleStream (сделали только важные, наз. 3и стрима примитивов)

Пример (примитивный стрим):
    LongStream.of(3, 4).toArray(); // [3, 4]
    LongStream.of(3, 4).sum(); // 7
    LongStream.rangeClosed(2, 200); // от 1 до 100

filter	Отфильтровывает записи, возвращает только записи, соответствующие условию	collection.stream().filter(«a1»::equals).count()
skip	Позволяет пропустить N первых элементов	collection.stream().skip(collection.size() — 1).findFirst().orElse(«1»)
distinct	Возвращает стрим без дубликатов (для метода equals)	collection.stream().distinct().collect(Collectors.toList())
map	Преобразует каждый элемент стрима	collection.stream().map((s) -> s + "_1").collect(Collectors.toList())
.collect(Collectors.joining(",")) - объединить в одну строку черех ","

min	Возвращает минимальный элемент, в качестве условия использует компаратор	collection.stream().min(String::compareTo).get()
max	Возвращает максимальный элемент, в качестве условия использует компаратор	collection.stream().max(String::compareTo).get()
forEach	Применяет функцию к каждому объекту стрима, порядок при параллельном выполнении не гарантируется	set.stream().forEach((p) -> p.append("_1"));
forEachOrdered	Применяет функцию к каждому объекту стрима, сохранение порядка элементов гарантирует	list.stream().forEachOrdered((p) -> p.append("_new"));
peek() - как forEach(), только не терминальный, в документации он предлагается для debug

forEach vs peek - forEach - терминальный, после его работы stream заканчивается

reduce	Позволяет выполнять агрегатные функции на всей коллекцией и возвращать один результат	collection.stream().reduce((s1, s2) -> s1 + s2).orElse(0)

Stream возвращает Optional на котором можно:
.orElse(null);
.orElseGet(() -> new Employee( 1L, "", ""));


parallel()
sequential()

mapToInt(), flatMapToInt(), ... - преобразуют
anyMatch() / noneMatch() / allMatch() - аналог find()


//full convert through collect(), эта штука внутри collect называется аккамулятор (в данном случае это LinkedList)
List<A> listFullV1 = listStream.collect(
	() -> new LinkedList<>(), //constructor 
	(listA, element) -> listA.add(element), //adder (2 parametrs)
	(listA, listB) -> listA.addAll(listB), //joiner (2 parameters) - нужен для параллельной работы потоков, чтобы join результаты
    StringBuilder::toString // post process, НЕОБЯЗАТЕЛЕН, это то, что преобразует в КОНЕЧНЫЙ результат, как Collectors.toString()
        // (т.е. мы можем что-то поделать с этой своей коллекцией из collect, а потом объект этой коллекции преобразовать во что-то другое)
);
--			
Имеет поля int: DISTINCT, SIZED, STORED, IMMUTABLE, NONNULL, SUBSIZED, ORDERED. Получить характеристику можно с int characteristics().
Имеет вложенный интерфейсы Spliterator.OfDouble, Spliterator.OfInt, Spliterator.OfLong и подчиненный им Spliterator.OfPrimitive
--
расширенный unicode - это int + int == long (64 bit)
int codePoint = Character.codePointAt(new char[] {'a', 'b'}, 2);
--
	BufferedReader reader = 
					new BufferedReader(new InputStreamReader(process.getInputStream()));
--
Optional
	of
	ofNullable - возвращает Optional-объект, а если нет дженерик-объекта, возвращает пустой Optional-объект
	empty - пустой Optional-объект
	isPresent() и ifPresent() -  существует обёрнутый объект или нет
    orElse() — возвращает объект по дефолту.
    orElseGet() — вызывает указанный метод.
    orElseThrow() — выбрасывает исключение
	get() — возвращает объект, если он есть
	map() — преобразовывает объект в другой объект.
	filter() — фильтрует содержащиеся объекты по предикату.
	flatMap() — возвращает множество в виде стрима, если значение не null
--
class C {
  int i = 5;
  void f1() {
	//(про похожий доступ во вложенных классах и простых иерфейсах ниже)
    //C.this.i == 5 работает ТОЛЬКО ВНУТРИ СВОЕГО класса
    //this.i == 5
    
}
--
class A<T> {
	static T ob; //ошибка, нельзя параметры типа в static
	static T f1(){} //ошибка, нельзя static метод с параметром типа КЛАССА
	static <V>f2(V v){} //правильно, НО если параметр у функции свой отдельный от класса то можно
	T f1(){return new T[12]} // ошибка, нельзя создать т.к. массив должен содержать инфу о типе своих объектов в runtime, которых тут нет
        // НО можно создать Object array и преобразовать: (T) new Object[12]
}

A<Integer> a[] = new A<>[10]; //ошибка, создать массив обобщенных объектов нельзя
A<?> a2[] = new A<?>[10]; //правильно, можно создать через метасимвол.

теперь в sameAvg() подходят Gen<Double> и Gen<Integer>
class Gen<T extends Number> { boolean sameAvg(Gen<?> t2) {} }

<S super T> // ошибка, super можно только для <?>
void f1(A<? super Number> a) //правильно

<параметры> возвращаемый_тип имя_метода() - обобщенный метод
class A { <T>A(T []arr) {} }
Gen.<MyClass, String>isIn(new MyClass[] { 1, 2 }, "test"); //вызов

При реализации ОГРАНИЧЕНИЕ (extends и/или super) повторять в интерфейсе не нужно. Причем повторять ограничение и невозможно - ошибка.

Обобшенный класс не может extends Throwable

ArrayList<ArrayList> нельзя использовать т.к. он не подтип ArrayList<List>
--
System
	static void arraycopy(Object arrSource, int begin, Object dest, int beginDest, int quntity)
		- копирует массив
	static long currentTimeMills() - возвращает время в милисекундах с 1 янв 1970 года
	static Map<String, String> getenv() - возвращает переменные среды
	static Properties getProperties() - класс Properties связанные с JVM (описывает JVM)
	static String lineSeparator() - строка символов разделитей строк
	static void load(String имя_файла_библиотеки) - загружает динамическую библиотеку
	static void runFinalization() - вызов finalize() для неиспользуемых, но ещё утилизированных объектов
	static void gc() - вызов сборщика мусора
	static void exit(int code) - закрывает программу, 0 - ошибок нет
	
    меняем стандартные потоки:
	static void setErr(PrintStream err)
	static void setIn(InputStream in)
	static void setOut(PrintStream out)
--
Calendar c = Calendar.getInstance();
c.set(Calendar.HOUR);
System.out.println(c.get(Calendar.HOUR));
	//по умолчанию текущая дата, региональные настройки и пояс
	GregorianCalendar c = new GregorianCalendar(2018, 3, 30, 10, 21);
	.getTime();
	.getTimeZone().getDisplayName()
	
java.util.Date - mutable, старая ошибка проектирования
java.time - новый пакет для работы с датами, содержит inmutable классы
--
Pattern pat = Pattern.compile(".+?[\\n\\.\\?\\!]+");
Matcher mat = pat.matcher(text);
while (mat.find()) {
	sentenceArr.add(new Sentence(new StringBuilder(mat.group())));
}
--
java.util.concurrent содержит (альтернативы блокировкам):
	Синхронизаторы - синхронизация и взаимодействие потоков
		Semaphore - семофор (монитор с счетчиком, который синхронизирует ни один поток, а много)
		CountDownLatch - ждет пока не произойдет определенное количество событий,
		а потом срабатывает (запускает свой Thread и при заравершении поток
		уменьшает счетчик внутри себя методом countDown())
		CyclicBarrier - позволяет группе потоков войти в режим ожидания в заданной
		точке выполнения (работать до определенной точки в main())
		Exchanger - Обмен данными между двумя потоками
		Phaser - синхронизирует потоки проходящие через несколько фаз операции.
            Как CyclicBarrier только точек много, а потоки изнутри оповещают Phaser
            о достижении фаз. После приостановки на фазе (точке в коде главного
            потока) главный поток запускает следуюющую фазу.
	Исполнители (Executor) - управление запуском и работай потоков,
        имеет доп. методы
        Цель:
            вместо создания новых потоков переиспользовать старые
            для экономии ресурсов из специального ThreadPool
	для работы с элементами concurrent
		Executor - интерфейс с методом void execute(Runnable r) запускает поток
		ExecutorService - интерфейс расширяет Executor, запускаеть много потоков,
		останавливать, возвращаеть результат работы потоков и т.д.
		ScheduledExecutorService - планирование запуска потоков
		ScheduledThreadPoolExecutor
		Executors - фабричные методы для получения ExecutorService,
		ScheduledExecutorService и т.д.
			Executors.newFixedThreadPool(1).execute(new MyThread());
	Callable, Future - для получения результата в главном потоке потом
	(после того как они появятся)
		1. Реализуем Callable и его метод V call()
		2. В main() делаем ссылку Future<V> f;
		3. запускаем на выполнение поток Callable:
			Future<V> f = Executors.newFixedThreadPool(1).submit(new MyCallable<V>(){...});
        4. CompletableFuture - класс, наследует Future, работает похоже на Promise в js.
        Tip. Ему не назначен Executor, поэтому выполнится в специальном
        ForkJoinPool.commonPool() (если это не поддерживает параллелизм по крайней
        мере 2го уровня, в этом случае new Thread будет создан, чтобы выполнить
        каждую задачу). Интерфейс маркер AsynchronousCompletionTask можно
        использовать для monitoring. Прим. можно вызвать get() или sleep() чтобы
        подождать выполнения методов описанных ниже.
            supplyAsync(callback) - можно вытащить результат работы callback через      
                get() - future.get(),
                Thread будет ждать результат get() как с обычный .join()
            runAsync(callback) - вытащить результат нельзя
            thenAccept() - аналог then() из Promise в js
            thenApply() - в отличии от thenAccept() возвращает результат
            thenApplyAsync() - выполнится не в текущем Thread, а в отдельном
            thenCompose() - можно создать цепочку thenCompose(), результат return попадает в следующий (как для Promise из js)
            thenCombine() - по зовершению 2х задач выполнить 3ю (на самом деле можно комбинировать много задач)
	TimeUnit - enum с методами преобравания единиц времени в другие,
        (используется для timeout потоков)
        используется в concurrent как параметр многих методов
		long t = TimeUnit.convert(long время, TimeUnit.HOURS);
	Параллельные коллекции - НЕ ВСЕГДА просто синхронизированные аналоги обычных, у них специальное поведение
		ArrayBlockingQueue
		ConcurrentHashMap
		ConcurrentLinkedQueue
		ConcurrentLinkedDeque
		•  ConcurrentSkipListMap
		•  ConcurrentSkipListSet
		•  CopyOnWriteArrayList
		•  CopyOnWri teArraySet
		•  DelayQueue
		•  LinkedBlockingDeque
		•  LinkedBlockingQueue
		•  LinkedTransferQueue
		•  PriorityBlockingQueue
		•  SynchronousQueu
	Блокировки (java.util.concurrent.locks.Lock) - аналог synchronized блоков,
        внутри потока вызываются методы:
	1. lock() 2. Доступ к общему ресурсы 3. unlock():
            Lock lock = new ReentrantLock();
            lock.lock();
            // do smth
            lock.unlock();
		Lock - интерфейс
		ReentrantLocks - позволяет повторную блокировку (синхронизацию) тем же
		потоком, сколько было повторов столько надо и методов unlock() вызвать
		ReadWriteLock - блокировка доступа потокам только на чтение
            из общего ресурса
		ReentrantReadWriteLock
		JDK8: появился StampedLock - в книге ничего
	Atomic - классы для доступа к ним разных потоков одновременно (как volatile)
		new AtomicInteger() и др.
		get(), set()
		compareAndSet()
		decrementAndGet()
		getAndSet() - дастает старое и устанавливает новое
		JDK8: 4 класса неблокирующих накопительных операций DoubleAccumulator, DoubleAdder, LongAccumulator, LongAdder
	
	Fork / Join Framework - облегченный тип потоков (классов) по сравнению с Thread. Выполняет задачи меньшим количеством потоков, что эффективнее (видимо от того, что у CPU немного ядер?)
		ForkJoinTask<V> - задачи
		ForkJoinPool (implements Executor, ExecutorService) - потоки выполняющие задачи
		RecursiveAction<V> расширяет ForkJoinTask, для задач не возвращающих значения
		RecursiveTask<V> - тоже для возвращающих
--
Иерархия классов окон AWT:
			Component
			|		\
		Container	Canvas
			/	\
	Windows		Panel
		/
	Frame

Component - цвет фона, шрифт, события (так как в AWT источник и обработчик событий сам Component) и т.д.
Container - можно вкладывать в него другие Component
Canvas - пустое окно в котором можно рисовать
Panel - есть метод add() для добавления, простое окно без рамки, строк и т.д.
	После добавления в него компонентов их можно менять
		setLocation ( ), setSize ( ), setPreferredSize ( ) или setBounds ( )
Windows - окно верхнего уровня, оно ни где не содержится
Frame - полноценное окно, именного его наследовать чтобы создавать прилржение

class MyFrame extends Frame {
	//если объявить метод в него передасться объект для рисования
	public void paint(Graphics g) {
		g.drawString("Hello", 10, 40);
		g.drawLine(0, 0, 100, 90);
		//установка цвета
		g.setColor(new Color(100, 100, 255));
		super.paint(g);//кое где надо так из-за вызова не зависимых от ОС компоентов
	}
}

--
Random r = new Random();
r.nextInt(); // nextLong(), nextDouble(), nextBoolean()
--
StringBuilder/StringBuffer имеют буфер 16, если не указа
ensureCapacity() - задать емкость
capacity() - объем памяти
trimToSize() - обрезает буфер чтобы он по размеру был ближе к текущему количеству символов	
---
Компилятор неявно импортирует во все программы import java.lang.*
---
public interface A {
	//неявно public final static и ДОЛЖНА быть инициализированна
	public final static int MY_MY = 3; //в подклассе static переменная с таким же именем ПЕРЕКРОЕТ эту???
	//неявно public abstract
	public void f1();
}
---
transient int i = 1; - при сохранении объекта (например сериализации?) переменная не сохранится (т.е. её значение?)
volatile int a; - модификатор доступа, только для переменных (следует использовать только с базовыми типами или ссылками на Enum)
---
java.nio.*
java.nio.channel - каналы
java.nio.charset - набор символов

Асинхронная передача данных в отличии от io:
1. Буфер - хранит данные
2. Канал - соединение с устройством ввода вывода
4. Набор символов - способ сопастовления байтов с символами
    Кодер - кодирует символ в байт
    Декодер - декодирует
5. Селектор - можно выполнять операторы ввода вывода для нескольких каналов одновременно

Суть: создаем channel, привязываем этот канал к буферу
    читаем / пишем через буфер,
    тогда не нужно вызывать rewind() или следить за буфером.

Суть Selector: регистрируем НЕСКОЛЬКО chanel в селектор,
    когда произойдет операция чтение / записи селектор сработает,
    кроме того селектор может "ожидать" ответа от потока (напр. сетевого).
    ГЛАВНОЕ ОТЛИЧИЕ ОТ io: для мониторинга используется ОДИН Thread.

Метод .rewind(); вызывается после каждого put() (изменения)
т.е. перед использованием любого write() (см. прим).
НО при чтении через buffer из nio, который связан с каналом,
НЕ НУЖНО вручную вызывать .rewind()

Прим.: ByteBuffer, MappedByteBuffer и прочие из пакета java.nio.channel

Пример 1 (прямо в канал):
    try(FileChannel ch = (FileChannel) File.newByteChannel(Path.get("file.txt"),
    StandardOpenOption.WRITE, StandardOpenOption.CREATE)){
        ByteBuffer buf = ByteBuffer.allocate(26);
        buf.forEach((v,k) -> buf.put(k));
        // ИЛИ for(int i=0; i<b.capacity(); i++){}
        buf.rewind(); // обнулить позицию на начало
        ch.write(buf);
    } catch(IOException e){}
    
Пример 2 (через буфер, который запишет все сам):
    try(FileChannel ch = (FileChannel) File.newByteChannel(Path.get("file.txt"),
    StandardOpenOption.WRITE, StandardOpenOption.READ,
    StandardOpenOption.CREATE)){
        MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_WRITE,0,26);
        channel.forEach((v,k) -> buf.put(k)); // пишет напрямую в файл через буфер
    } catch(IOException e){}

(чтение из канала до тех пор пока в файле не останется байтов)
Пример 3 (чтение из канала через SeekableFileChannel):
    try(SeekableFileChannel ch = Files.newByteChannel(Paths.get("file.txt"))){
        ByteBuffer buf = ByteBuffer.allocate(26); int length;
        do {
            length = channel.read(buf);
            if(length != -1){ // если в буфере что-то есть
                buf.rewind(); // перемотка на начало буфера
                for(int i=0;i<length;i++){
                    System.out.print((char)buf.get());
                }
            }
        } while{length != -1} // пока файл не законфился
    } catch(IOException e){}

Пример 4 (чтение из канала, через буфер):
    try(FileChannel ch = (FileChannel) Files.newByteChannel(Paths.get("file.txt"))){
        MappedByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY,0,26);
        channel.forEach((v,k) -> buf.put(k)); // читаем файл через буфер
    } catch(IOException e){}
    
Пример 5 (копирование файла):
    try {
        Path in = Paths.get("io.txt"), Paths.get("out.txt");
        Files.copy(in, out, StandardCopyOption.REPLACE_EXISTING);
    } catch(IOException e) {}
    
Пример 6 (запись через поток в nio):
    byte[] bs = "Hello".getBytes();
    try(OutputStream os = new BufferOutputStream(Files.newOutputStream(Paths.get("file.txt")))) {
        bs.forEach(v -> os.write(v));
    } catch(IOException e) {}
    
Пример 6 (чтение через поток в nio):
    int content;
    try(InputStream is = new BufferInputStream(Files.newInputStream(Paths.get("file.txt")))) {
        bs.forEach(v -> is.write(v));
        do {
            content = is.read();
            if(content != -1) {
                System.out.print((char)content);
            }
        } while(content != -1); // пока не останется символов в is
    } catch(IOException e) {}
    
Пример 7 (перемещение файла):
    try {
        Path in = Paths.get("io.txt"), Paths.get("out.txt");
        Files.move(in, out, StandardCopyOption.REPLACE_EXISTING);
    } catch(IOException e) {}
    
Пример 8 (свойства файла):
    Files.exists(path);
    Files.isHidden(path);
    Files.isWritable(path);
    Files.isReadable(path);
    BasicFileAttributes fa = Files.readAttributes(path, BasicFileAttributes.class);
    fa.isDirectory();
    fa.isRegularFile();
    fa.isSymbolicLink();
    fa.size();
    fa.lastModifiedTime();
    
Пример 9 (селектор):

Пример 10 (отслеживание событий файловой системы):
---
ThreadLocal - контейнер (похож на Map), который виден во всех частях ОДНОГО Thread, его часто используют напр. для Connection или Transaction в JavaEE
    initialValue() - важный метод для инициализации исходным значением
    остальные методы похожи на Map
---
mvn archetype:generate - команда показывает список всех архетипов в официальном репозитории

--Сборка проекта:
	mvn validate - проверка данных на корректность
	mvn compile - компилирует данные
	mvn test - выполняет тесты
	mvn package - создание архива (jar, war). То есть записывает готовые архив в target
	mvn install - переносить готовый скомпиленный архив проекта в локальный репозиторий maven (которые потом можно использовать)
	mvn deploy - помещает скомпиленный архив в заданный контейнер сервлетов???
	
    mvn clean package - тоже что и mvn package, только сначало удаляет предыдущий скомпиленный проект (мусор)
	mvn clean install - аналогично только для install
	
	mvn help:help
	mvn site - создать документацию на базе javadoc
		(результат будет в папке проека в подкаталоге site)
	mvn pdf:pdf - создать доку в формате pdf
		(результат будет в папке проека в подкаталоге pdf)
	//есть команды на выполнения только одного конкретного теста, а не всех
	
Все scopes <scope>test</scope>:
    compile: default, зависимости в classpath + эти же зависимости распостраняются на зависимые проекты
    provided: как compile, но говорит проекту, что зависимости будут вставлены JDK или container в runtime (во время работы?)
        например: чтобы использовать Servlet API контейнер должен вставить его сам когда приложение УЖЕ будет в контейнере, поэтому ставим ему provided
        Этот scope доступен только compilation и test classpath
    runtime: зависимость не нужна для компиляции, но нужна во время работы и тестов
    test: зависимость нужна только для компиляции и исполнения тестов
    system: аналог provided, но зависимость не из репозитория maven, а указывается вручную в теге <systemPath>
    import: начиная с Maven 2.0.9 or later,
        используется в дочерних pom.xml с типом <type>pom</type> чтобы указать, что зависимость будет взята из <dependencyManagement>
        само по себе import влияния не оказывает
        
Разница между <dependencies> и <dependencyManagement>:
    В дочернем pom.xml <dependencies> зависимость будет включена ВСЕГДА
    В дочернем pom.xml <dependencyManagement> зависимость будет включена ТОЛЬКО если она будет объявлена в <dependencies> самом по себе (а не в <dependencies> родительского?)
После этого можно пропустить scope/version в дочернем pom.xml, потому что они уже объявдены в родительских

Наследование:
    Можно разделить 1 POM на несколько, для этого:
    Создать pom.xml, в зависимом pom.xml указать родителя:
        <parent>
            <groupId>com.sample</groupId>
            <artifactId>parent</artifactId>
            <version>1.0-SNAPSHOT</version>
        </parent>
    и в дочернем pom.xml указать тип pom (а НЕ типы war, jar, ...)
        <packaging>pom</packaging>
ПО умолчанию дочерний POM ищется в корне проекта, потом в удаленном репе, можно указать путь вручную <relativePath>../baseapp/pom.xml</relativePath>

Выбрать версию jvm для компиляции или включить зависимые jar в итоговый jar можно через плагины.

Задание сторонних репозиторие:
</build>
<repositories> - между </build> и <dependencies>
	<repository>
		<release> - способ работы с репом (только на скачивание или нет и т.д.)
			...
		<snapshots> - статус работы
			<enabled>true - в релизе или нет
			<updatePolicy>never - когда обновлять
							always
							daily
							interval:124 - минуты
		<url>http://blbbla.com - адрес стороннего репа		
		
Запуск с указанием профиля:
mvn profile-1,profile-2

<profiles>
    <profile>
        <id>debug</id>
        …
        <dependencies>
            <dependency>…</dependency>
        </dependencies>
        …
        <activation> <!-- описывает в каких случаях профиль станет активным -->
            <activeByDefault>true</activeByDefault>
            <jdk>[1.3,1.6)</jdk>
            <os>
                <name>Windows XP</name>
                <family>Windows</family>
                <arch>x86</arch>
            </os>
            <file>
                <!-- выполнится профиль если этого файла нет -->
                <missing>target/generated-sources/axistools/wsdl2java/org/apache/maven</missing>
            </file>
        </activation>
        <dependencies> ... </dependencies>
    </profile>
    <profile>
        <id>release</id>
        …
        <dependencies>
            <dependency>…</dependency>
        </dependencies>
        …
    </profile>
</profiles>

Служит для запуска целей плагинов:
mvn <PLUGIN>:<GOAL>

Привязка плагина к цели:
<plugins>
    <plugin>
        <artifactId>maven-failsafe-plugin</artifactId>
        <version>${maven.failsafe.version}</version>
        <executions>
            <execution>
                <goals>
                    <goal>integration-test</goal>
                    <goal>verify</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
---
fast forward - это просто перемещение указателя с одной ветки на другую вместо merge. Оно не будет отмечено как слияние и не прорисуется на диаграмме как слияние.
Можно его отменить через -no-ff если надо увидеть в истории.

3 step way merge - алгоритм, сливаются блоки обоих веток начиная от общего предка, если изменились обе версии кода, то конфликт

Стратегии merge:
1. resolve - Нужно использовать для 2х веток, если у них не более 1го общих ветки-предка.
2. recursive (по умолчанию), бывает: ours, theirs; исходной веткой выбирается виртуальная по алгоритму (эффективнее) и происходит умное слияние всех кандидатов в общего предка с приоритетом у изменений кода сделанных позже
3. octopus - выбирается автоматически в случае merge нескольких веток
4. ours - не делает merge, фактически merge только отображается в истории, чтобы эту историю обновить. Например для merge старой ветки от которой нужна только история.
5. subtree - делает merge совершенно другого под-проекта в текущий проект сливая его git с текущим

resolve vs recursive - recursive стратегия решает проблемы стратегии resolve. Она так же реализует трехстороннее слияние, но в качестве предка используется не реальный, а «виртуальный» предок, который конструируется по следующему условному алгоритму:
    1. проводится поиск всех кандидатов на общего предка,
    2. по цепочке проводится слияние кандидатов, в результате чего появляется новый «виртуальный» предок, причем более свежие коммиты имеют более высокий приоритет, что позволяет избежать повторного проявления конфликтов.
    
Значимы только ours и subtree стратегии. Остальные стратегии выбирает git сам автоматически.

rebase - еще называют fast-forward merge
recursive - еще называют non-fast-forward merge (команда -no-ff)

git format-patch 1b6d..HEAD^^ > test.patch
$ git apply < мой.patch

pull == fetch + merge

Просто fetch без указания ветки получает список всех веток с сервера

Команда:
    git pull --rebase
Это аналог команды:
    git fetch
    git rebase origin/master

rebase - взять все изменения, которые попали в коммиты на одной из веток, и повторить их на другой.
Вообще команда rebase может менять историю commits, может разбить историю на 2 репозитория, слить в один и т.д.
    
git cherry-pick - берет отдельный commit (даже из другой ветки) и применяет его на текущей
git rebase — это "автоматизированный" cherry-pick, делает тоже, но не с 1им commit, а с цепочкой.
git revert — противоположность git cherry-pick, создает commit отменяющий отдельный commit
--------------
Основные типы JOIN

Заметки:
1. inner join это синоним where, inner join считается лучше читаемым
2. просто join это синоним для inner join
3. left inner join и right inner join ПОХОЖЕ не существует
4. cross join не бесполезен (как некоторые пишут), он может быть использован например для заполнение сетки значениями из табл. Для примера две табл size и color одежды, результат все размеры и цвета.
5. self join применим когда табл. ссылается сама на себя (будет работать быстрее чем group by). Самого оператора self join нету, нужно писать реализацию через: where a.id <> b.id

Типы join:
1. inner join - пересечение
2. left (outer) join - пересечение, плюс все строки из 1ой (левой) табл, если on не выполняется для строк то пустые ячейки заполняются null
3. right (outer) join - пересечение, плюс все строки из 2ой (правой) табл, если on не выполняется для строк то пустые ячейки заполняются null
4. full (outer) join - как left outer join и right outer join одновременно, если on не выполняется для строк то пустые ячейки заполняются null
5. cross join - все строки обоих табл. со всеми (декартово произведение). Отличается от full outer join тем, что соединение с пустой табл. тоже пусто (в full outer join соединение с пустой табл. будет иметь строки только одной из табл). Для cross join не нужно указывать условие (то что после on), пример:
    select *
    from a cross join b
6. self join - пересечение табл с самой собой. Полезно например, чтобы выбрать из одной табл. уникальные пары значений из столбцов без group by. Т.е. self join применим когда табл. ссылается сама на себя. Пример (в self join неравенство вместо равенства в where):
    SELECT b.name, b.name
    FROM b
    WHERE b.id <> b.id
    AND b.City = b.City
    ORDER BY b.City;

Дополнительные варианты:
1. вариант left (outer) join, только исключая само пересечение, результат только левая часть строк. АНАЛОГИЧНО и для right (outer) join
    select *
    from a
    left join b
    on a.id = b.id
    where b.id is null;
2. вариант full (outer) join, только исключая само пересечение, результат только левая и правая часть строк
    SELECT a.name, b.name
    FROM a
    FULL OUTER JOIN a
    ON a.id = b.id;
    where a.id is null or b.id is null

Пример нескольких inner join сразу:
    select *
    from a
    inner join b on a.id = b.id
    inner join c on b.id = c.id
    
Пример inner join через where:
    select *
    from a, b, c
    where a.id = b.id and b.id = c.id
---
JNDI (Java Naming and Directory Interface) - это Java API, просто набор интерфейсов для получения данных из БД, properies, xml и т.д.
new InitialContext(props)..lookup("*name of resource* (DB)");
---
С Java 9 для некоторых программ при компиляции нужно включить модули:
javac --add-modules java.xml.ws UCPublisher.java
--------------------------------
JMX - Java Management Extensions
MBean - Managed Bean
	http://alvinalexander.com/blog/post/java/source-code-java-jmx-hello-world-application
	https://en.wikipedia.org/wiki/Java_Management_Extensions
	
	(потом надо описать?) Различают:
		Standard MBeans
		Dynamic MBeans
		Open MBeans
		Model MBeans
		MXBeans
		
	Как использовать:
	1. Создаем Bean
		class HelloMBean {
			String val;
			//getters, setters here
		}
	2. Регистрируем его в спец. сущности (созданное через фабрику)
		ManagementFactory.getPlatformMBeanServer().registerMBean(
			new HelloMBean(), new ObjectName("FOO:name=HelloBean")
		);
	3. Запускаем нашу программу со спец. командой в jconsole
		java -Dcom.sun.management.jmxremote \
			 -Dcom.sun.management.jmxremote.port=1617 \
			 -Dcom.sun.management.jmxremote.authenticate=false \
			 -Dcom.sun.management.jmxremote.ssl=false \
			 SimpleAgent
	4. Теперь видим значения MBean в специальном веб интерфейсе (и можем менять значения?)
	
	Для чего: для мониторинга программы?
--------------------------------
JavaBean - чтобы DI и др. генераторы и пр. находили сеттеры по соглашению имен.

LDAP (Lightweight Directory Access Protocol) - коротко:
    протокол для любой БД, сама БД видна как набор папок с данными (аналог Map, внутри сделана через дерево)
--------------------------------
Function<Integer, Integer> times2 = e -> e * 2;
Function<Integer, Integer> squared = e -> e * e;
times2.compose(squared).apply(4); // Returns 32, times2(squared(4))
times2.andThen(squared).apply(4); // Returns 64, squared(times2(4))
 
(String::trim).andThen(String::toLowerCase)
    .andThen(StringBuilder::new)
    .andThen(StringBuilder::reverse)
    .andThen(StringBuilder::toString).apply(" BLA "); // ALB

userList.sort(Comparator.comparing(User::getName)

// partitioningBy разделяет коллекцию на две части по условию

// groupingBy
Map<String, List<Worker>> map1 = workers.stream()
       .collect(Collectors.groupingBy(Worker::getPosition)); // просто группировка
Map<String, Long> map3 = workers.stream() // название позиции (группы) и их количество
       .collect(Collectors.groupingBy(Worker::getPosition, Collectors.counting())); // подсчет для групп
Map<String, Map<Integer, List<Worker>>> collect = workers.stream()
       .collect(Collectors.groupingBy(Worker::getPosition, // название позиции + число лет + сами объекты группы
              Collectors.groupingBy(Worker::getAge))); // группировка по нескольким полям
--------------------------------
Objects - некоторые методы
	deepEquals(Object a, Object b)
	hash(Object... values) - один hash для нескольких Objects
	isNull(Object obj)
	hashCode(Object o) - вернет 0 для null
	requireNonNull(o) - проверка на null
--------------------------------
