Масштабирование

Вертикальное — это когда добавляют больше оперативки, дисков и т.д. на уже существующий сервер,
а горизонтальное — это когда ставят больше серверов в дата-центры, и сервера там уже как-то взаимодействуют.

Плюс Горизонтального масштабирования в том, что если система рассчитана на добавления кластеров (нод, серверов), то наращивание мощности горизонтально не требует остановки системы в процессе наращивания. Но сложность работы с такой системой выше. После определенного порога горизонтальное масштабирование становится гораздо дешевле вертикального.

Минус Горизонтального масштабирования - на отказоустойчивость и общение кластеров между собой расходуются ресурсы (CAP теорема).

Минус Вертикального масштабирования - единая точка отказа, если один сервер упал - упало все приложение.

****************************************************
    
 

****************************************************
CSRF/XSRF (Cross-site request forgery) token - token отправляется одним из параметров запроса, у каждого пользователя свой используется чтобы проверить можно обрабатывать запрос на сервер (если token верный) или нет. Угроза: если CSRF token не будет, то любой сможет отсылать value для input на сервер (и др. данные)
    - CSRF token может быть полем форму, частью http referer, содержаться в спец. http header
    - CSRF token не передают в адресе, а только в теле запроса. Следовательно используют только для методов которые изменяют данные на сервере (POST, PUT, DELETE). Методы GET, HEAD не изменяют данные и не имеют тела запроса, для них не нужен token.
    
CSP - запрещает внедрять в страницу inline скрипты, использовать eval(), разрешать запросы для js со страницы только к выбранным доменам.
****************************************************
- BFS можно реализовать рекурсивно или стек+цикл
- DFS можно реализовать рекурсивно или очередь+цикл
****************************************************
Factorial

 static int factorial(int n){    
  if (n == 0)    
    return 1;    
  else    
    return(n * factorial(n-1));    
 }
****************************************************
Fibonacci Series

 static int n1=0,n2=1,n3=0;    
 static void printFibonacci(int count){    
    if(count>0){    
         n3 = n1 + n2;    
         n1 = n2;    
         n2 = n3;    
         System.out.print(" "+n3);   
         printFibonacci(count-1);    
     }    
 }    
 0 1 1 2 3 5 8 13 21 34
 ****************************************************
 Сортировка пузырьком
 
 Массив проходится N-1 раз.
 За каждый проход попарно сраниваются элементы и меняются местами, если один больше другого.
 
 timsort - самая крутая, несколько алгоритмов сортировки использование которых зависит от условий
****************************************************
Поиск зацикленности в списке

есть классический алгоритм, когда по списку пускаем 2 указателя, один за шаг переходит на 1 элемент вперед, 2-й - на 2 элемента вперед.
При проходе 2-м проверяем, если он указывает на тот-же объект, что и первый - значит цикл есть.
****************************************************
Временная сложность алгоритма поиска в дереве поиска

****************************************************
перевод из 2-ой системы в 10-ую: 01101001 = 2^0 + 2^3 + 2^5 + 2^6 (степени)
перевод из 10-ой системы в 2-ую: делим число на 2 и записываем остатки от деления справа на лево (остатки будут 1 или 0). Эти остатки и есть число.

перевод из 10ой в 16ую: делить на 16, записать 10ный остаток в виде 16го числа, повторять пока остаток не станет 0, записать 16ые числа справа на лево
перевод из 16ой в 10ую: заменить числа на десятичные и каждый порядок умножить на 16 в степени равной прядку: E7A916 = 14×16^3+7×16^2+10×16^1+9×16^0

общая формула перевода в систему счисления, где an - первая цифра, a0 - последняя:
    C = an * M^n + an-1 * M^(n-1) + ... + a1 * M^1 + a0 * M^0
****************************************************
1. простой цикл выполняется от O(n)
    Примерами этого являются исчерпывающий поиск, поиск максимального элемента в массиве и генерация контрольной суммы.
3. Алгоритм двоичного поиска O(lg(n))
    КАЖДАЯ ПОЛОВИНА ПРОСМАТРИВАЕТСЯ ОТДЕЛЬНО, если речь о двоичных алгоритмах поиска и подобных
4. Алгоритмы, разбивающие входные данные на разделы, работающие независимо с двумя половинами O(nlg(n))
    Пример: сортировка слиянием, поиск как при бинарном поиске O(log n), но повторяется для каждой половины n раз, общая скорость O(n*log n)
5. Вложенные циклы. O(m*n) - квадратичное время
    пример: пузырьковой сортировки
6. Комбинаторика n! или n^n и больше?
        задача о коммивояжере, об оптимальной упаковке предметов в контейнер, о разделении набора чисел таким образом, что сумма каждого отдельного набора одинакова и т. д
        
O(1) - лучшее время (без циклов)
O(sqrt(n)) - почти как O(log n)
O(e^x) - экспоненциальное время
****************************************************
гексагональная архитектура
****************************************************
Конечный автомат (машина состояний, state mashine) - это компьютерная программа, которая имеет характерные черты:

    В основе лежит модель поведения
    Модель поведения имеет несколько последовательных состояний
    Состояния связаны между собой в виде цепочки переходов, которая может иметь линейную или ветвистую структуру (обычно представляют в виде графов)
    Переходы между состояниями порождают действия
    Цепочка состояний имеет одно начальное состояние, ветвление осуществляется за счет условий переходов
    Условия переходов должны быть описаны таким образом, чтобы из каждого конкретного состояния модель имела лишь одно следующее состояние

Пример:
Множества состояний (START, BOTTOM_FIXED, TOP_FIXED, UNFIXED, FINISH)
Множества событий (каждое событие состоит из набора условий, например, “панель влезает в экран и пересекла точку старта”)
Описания переходов (Событие + Некоторое состояние => Новое состояние)
Действия для каждого состояния (например, “зафиксировать панель сверху” при переходе в состояние TOP_FIXED)
Начального состояния (в нашем случае START)

Самый простой конечный автомат
class Switcher {
    public Boolean state = false; //true - is on, false - is off
    public void next() {
        if (this.state) {
            this.state = false;
        } else {
            this.state = true;
        }
    }
}

****************************************************
refreshtoken
hash table
B-tree
красно черное дерево
Поиск пути дейкстры
Поиск пути A*
элементы дискретной математики