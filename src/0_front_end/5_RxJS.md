- [Что такое RxJS](#Что-такое-rxjs)
  - [Определение](#Определение)
  - [Почему нужно использовать Reactive Programming (RP)](#Почему-нужно-использовать-reactive-programming-rp)
  - [Отдельно про LINQ](#Отдельно-про-linq)
- [Cold vs Hot vs Warm Observable](#cold-vs-hot-vs-warm-observable)
  - [Pull (Imperative) vs Push (Reactive) архитектуры (стратегии)](#pull-imperative-vs-push-reactive-архитектуры-стратегии)
- [Операторы](#Операторы)
  - [Кратко о всех](#Кратко-о-всех)
- [Примеры subscribe](#Примеры-subscribe)
- [asObservable](#asobservable)
- [toPromise](#topromise)
- [projection function](#projection-function)
- [Популярные операторы RxJS в Angular](#Популярные-операторы-rxjs-в-angular)
- [Flattening Strategy (concatMap vs mergeMap vs switchMap vs exhaustMap)](#flattening-strategy-concatmap-vs-mergemap-vs-switchmap-vs-exhaustmap)
- [throttleTime, debounceTime, auditTime, bufferTime, windowTime](#throttletime-debouncetime-audittime-buffertime-windowtime)
- [Scheduler](#scheduler)
- [Subjects](#subjects)
- [Важные примеры использования](#Важные-примеры-использования)
  - [Возвращаемое значение Observable.never()](#Возвращаемое-значение-observablenever)
  - [Возвращаемое значение Observable.empty()](#Возвращаемое-значение-observableempty)
  - [Пример цепочки вызовов](#Пример-цепочки-вызовов)
  - [switchMap при переходе по ссылкам (e.g. Angular)](#switchmap-при-переходе-по-ссылкам-eg-angular)
  - [switchMap и отмена предыдущих событий на примере click](#switchmap-и-отмена-предыдущих-событий-на-примере-click)
  - [Кэш на RxJs (аналог redux, который часто используется с React) и немного про NgRx](#Кэш-на-rxjs-аналог-redux-который-часто-используется-с-react-и-немного-про-ngrx)
  - [Аутенфикация на RxJs и скрытие тегов для неавторизованных (e.g. Angular)](#Аутенфикация-на-rxjs-и-скрытие-тегов-для-неавторизованных-eg-angular)
  - [Отписка от событий в деструкторе компонента (e.g. Angular) и от чего можно не отписываться](#Отписка-от-событий-в-деструкторе-компонента-eg-angular-и-от-чего-можно-не-отписываться)
    - [Отписка от событий в деструкторе компонента](#Отписка-от-событий-в-деструкторе-компонента)
    - [От чего можно не отписываться (Angular делает отписка сам)](#От-чего-можно-не-отписываться-angular-делает-отписка-сам)
  - [take(1) и first() для инициализации (выполнения чего-либо только один раз при запуске)](#take1-и-first-для-инициализации-выполнения-чего-либо-только-один-раз-при-запуске)
  - [Type-ahead suggestions (autocomplete, авто комплит)](#type-ahead-suggestions-autocomplete-авто-комплит)
  - [Exponential backoff (повтор запроса каждые N сек, с увеличением времени между повторами по экспоненте)](#exponential-backoff-повтор-запроса-каждые-n-сек-с-увеличением-времени-между-повторами-по-экспоненте)
  - [Показ progress для request (прогресса выполнения запроса) в Angular](#Показ-progress-для-request-прогресса-выполнения-запроса-в-angular)
  - [Обработка jwt и refreshToken, временная блокировка всех запросов пока новый jwt не запрошен через refreshToken (e.g. Angular)](#Обработка-jwt-и-refreshtoken-временная-блокировка-всех-запросов-пока-новый-jwt-не-запрошен-через-refreshtoken-eg-angular)
- [Старые операторы, которые могут встретиться](#Старые-операторы-которые-могут-встретиться)
- [lodash как пример похожих функций, но для массивов, а не событий, в качестве более понятного примера](#lodash-как-пример-похожих-функций-но-для-массивов-а-не-событий-в-качестве-более-понятного-примера)
- [Материалы (удобнее пояснено, часть на английском)](#Материалы-удобнее-пояснено-часть-на-английском)

# Что такое RxJS

## Определение
**Note.** Некоторые определения ниже переведены дословно, чтобы не утерять смысл. Они точны согласно статьям и не смотря на схожесть.

**RxJS** (Reactive Extensions for JavaScript) - Это набор библиотек позволяющих создавать **asynchronous** и **event-based** программы используя **observable sequences** и **fluent query operators**.

Rx использует **теорию монад**.

**Коротко:** Reactive programming is programming with asynchronous data streams.

**stream** - это последовательность событий происходящих во времени и расположенных во времени (ongoing events ordered in time)

**Захват** (обработка, capturing) - этих событий может быть только **asynchronously** путем объявления функции <sub>(подписки на событие)</sub>, которая будет исполнена **только** когда произойдет выброс (emit): **value**, **error**, **"completed"** (при завершении потока).

**Могут** быть пропущены **error** и **"completed"** при объявлении функции обработчика событий в методе **subscribe** (как в **Promise** из js).

Назначение функции обработчика называется **subscribing**.

**multicasting** - это когда несколько Observer подписаны на один **Observable**.

**Observable** - это "улучшенный" **Promise**.

Можно преобразовать **Promise** в **Observable**: `var stream = Rx.Observable.fromPromise(promise)`

**Observable** - не следует [Promises/A+](https://stackoverflow.com/a/36192729) спецификации, где все **Promises** это: 1) **thenable** (имеют метод then), 2) имеют **state** (pending, fulfilled, rejected), 3) могут быть **resolve()**. <sub>Это краткий свой вариант спецификации, но длинный не сильно отличается</sub>

**Rx streams** в отличии от **Observable** (которые как **Promises**) могут возвраща не одно, а много значений.

**observable** также называют **stream**

**observable** по умолчаню **unicast**, каждый **subscribe** вызывается независимо. В противоположность **multicasted observable** - отправляет одни и те же данные всем своим подписчикам. Можно преобразовать **observable** в **multicasted observable** через операторы: **multicast**, **publish**, **share**. Т.е. **multicasted observable** это **Subject** или **hot Observable** (который внутри использует Subject как прослойку).

**Observable** принято записывать с `$` в конце: `obs$`.

***

**Пример диаграмы в ASCII:**
```
--a---b-c---d---X---|->

a, b, c, d are emitted values
X is an error
| is the 'completed' signal
---> is the timeline
```

***

**Т.е.** это набор библиотек для асинхронного и событийного программирования использующего наблюдателей (паттерн наблюдатель) и операторы для преобразования этих асинхронных запросов (изменение, фильтрацию и пр.).

**Т.е.** работа идет с коллекциями событий или наборами данных за период времени.

Определение RxJs **от Microsoft**: **Rx = Observables + LINQ + Schedulers**

**Observable** – коллекция, источник данных, который генерирует события и отправляет их подписчикам.

**Observer** – подписчик, который и будет обрабатывать сигналы от источника.

***

**LINQ** (Language-Integrated Query) - язык запросов к источнику данных.

**Источники данных LINQ могут быть:** Object (коллекции, массивы), **DataSet**, **XML** и пр.

**Операторы LINQ:** Select (map), Where (Filter), Sum / Min / Max / Average, Count и пр.

**Если коротко:** **LINQ** напоминает **SQL**.

## Почему нужно использовать Reactive Programming (RP)

RP позволяет сфокусироваться на interdependence (взаимной зависимости) событий, которые содержает business logic. Вместо концентрации на деталях реализации. Код более краткий.

## Отдельно про LINQ

Чтобы Object можно было использовать с LINQ он **должен реализовывать спец. интерфейс**, e.g. IEnumerable из C#

**Типы LINQ:**
1. **LINQ to Objects**: применяется для работы с массивами и коллекциями
2. **LINQ to Entities:** используется при обращении к базам данных через технологию Entity Framework
3. **LINQ to Sql:** технология доступа к данным в MS SQL Server
4. **LINQ to XML:** применяется при работе с файлами XML
5. **LINQ to DataSet:** применяется при работе с объектом DataSet
6. **Parallel LINQ (PLINQ):** используется для выполнения параллельной запросов

# Cold vs Hot vs Warm Observable

**Прим.** Добавить пример warm Observable

**Cold Observables** - данные производятся самим **Observable** на который делаем **subscribe**. **Т.е.** когда делаем **subscribe** вызывается функция внутри **Observable**, которая производит данные и отправляет подписчикам. При каждом новом вызове **subscribe** функция внутри **Observable** вызовется заново и отправит новые данные новому подписчику.

**Hot Observables** - данные производятся во вне **Observable**. **Т.е.** есть общий источник событий (e.g. **DOM event**), который обернут в наш **Observable**. И при каждом срабатывании этого **event** (e.g. click по кнопке) вызовется метод **subscribe** у всех подписчиков одновременно и отправит им **одни и те же данные** в один и тот же момент времени.

**Warm Observables** - когда данные начинают испускаться после первого вызова subscribe, но рассылаются всем подписчикам одинаковые данные одновременно, как в hot Observable.

**Когда использовать:**
1. **cold** - обычно нормальный выбор
2. **hot** - когда должны быть получены одни и те же данные. Или например чтобы не пересоздавать при каждом **subscribe** новое WebSocket соединение.

**Пример:**
```js
import * as Rx from "rxjs";

// 1. cold (источник данных внутри поэтому каждая подписка вызовет его срабатывание и данные каждый раз будут разные)
const observable = Rx.Observable.create((observer) => {
    observer.next(Math.random());
});

// subscription 1
observable.subscribe((data) => {
  console.log(data); // 0.24957144215097515 (random number)
});

// subscription 2
observable.subscribe((data) => {
   console.log(data); // 0.004617340049055896 (random number)
});
// -------------------
// 2. hot (источник данных во вне)
const random = Math.random()

const observable = Rx.Observable.create((observer) => {
    observer.next(random);
});

// subscription 1
observable.subscribe((data) => {
  console.log(data); // 0.11208711666917925 (random number)
});

// subscription 2
observable.subscribe((data) => {
   console.log(data); // 0.11208711666917925 (random number)
});
// -------------------
// 3. hot (с внешним источником данных в виде внешнего event)
const observable = Rx.Observable.fromEvent(document, 'click');

// subscription 1
observable.subscribe((event) => {
  console.log(event.clientX); // x position of click
});

// subscription 2
observable.subscribe((event) => {
   console.log(event.clientY); // y position of click
});
```
***
**Hot Observable** vs **Subject** - в **hot** между источником события и **subscribers** стоит *прослойка* **Subject**, который в примере ниже создается автоматически через **hot.connect()**. Он гарантирует, что **subscriber** выполнится только **1ин** раз для каждого подписчика.

**Преобразование cold в hot** через **publish**:
```js
const cold = new Observable(observer => {
  observer.next(Math.random());
  observer.complete();
});
const hot = cold.publish();

hot.subscribe((num) => console.log('sub 1:', num));
hot.subscribe((num) => console.log('sub 2:', num));
hot.connect(); // все subscribe выполнятСЯ только после вызова этого метода и им приходят одни и те же данные
// > sub 1: 0.249848935489
// > sub 2: 0.249848935489
```
***
**multicast** как **publish**, но может указать что именно использовать в качестве *прослойки* для **hot Observable** (Subject, ReplaySubject etc). Указанный в **multicast** параметр будет использован, когда вызовется **connect**.
```js
const source = interval(2000).pipe(take(5));
const multi = example.pipe(multicast(() => new Subject()));
const subscriberOne = multi.subscribe(val => console.log(val));
const subscriberTwo = multi.subscribe(val => console.log(val));
multi.connect(); // работает как для publish
```
***
**observable.publish().refCount()** как **publish** + **connect** сразу. Слово `Count` в названии оператора означает, что есть счетчик ссылок для **Subject,** который используется внутри для **publish**. Счетчик увеличивается когда происходит **subscribe** и уменьшается для **unsubscribe**. Когда количество ссылок равно 0, то внутренний **Subject** _(который работает как прослойка или можно еще сказать мост)_ отписывается от источника событий к которому был привязан и для которого был _прослойкой_ делающий **hot Observable** из **cold Observable**.

**observable.share()** - сокращеное имя для **observable.publish().refCount()**

Кроме этого, есть несколько вариантов **publish** и **share** операторов, например: **publishReplay**, **publishBehavior**, **shareReplay** etc. Они работают согласно их названиям.

Для **refCount** есть разные варианты **авто unsubscribe**, посмотреть хорошую статью про refCount с примерами можно [тут](https://blog.angularindepth.com/rxjs-how-to-use-refcount-73a0c6619a4e)

```js
// Пример refCount
const published = source.publish();
const a = published.take(1).subscribe(val => {console.log(val)});
const b = published.take(1).subscribe(val => {console.log(val)});
const subscription = published.connect();

const a = Rx.Observable.create().share(); // Hot Observable
```

## Pull (Imperative) vs Push (Reactive) архитектуры (стратегии)
Обычный код с последовательным выполнением это **Imperative** (Pull архитектура), реактивный код это **Reactive** (Push архитектура).

* **Pull** (Imperative) - мы **ожиданием события** (например пользователь кликнул) и вызываем последовательность **действий с данными**. Мы делаем **pull** данных (т.е. подгрузку к нам, fetching) откуда-то по событию.
* **Push** (Reactive) - мы ожидаем **события изменения самих данных** (т.е. обработчик события подписан на событие изменения данных) и вызываем обработчики, которые запустятся по событию изменения данных (например если имя пользователя изменилось в хранилище, то запустится событие которое изменит объект user и на странице отобразится другое имя пользователя). Делается **push** данных (т.е. `publish`) в observables (в наблюдателей).

# Операторы
## Кратко о всех

* **Creation**
  * <u>**create**</u> - создает Observable из ф-ции и передает туда observer (внутри можно вызвать **observer.complete** для завершения, **observer.next** для передачи значения подписчикам)
  * _**of**_ - отправляет подписчикам все перечисленные значения и объекты (e.g. `obs.of({name: 'blah'}, 1, 2, 'bla')`)
  * _**from**_ - создает Observable из др. источников в том числе из **Promise**, строки, массива, Map etc: `from(new Promise(resolve => resolve('Hello World!')))`
  * _**of** vs **from**_ - `from` создаем новый Observable из **каждого** элемента переданного array, string etc. Т.ч. при **subscribe** он выполнится для всех этих элементов **по очереди несколько раз** и выведет что-то вроде: `1, 2, 3, 4, ...` для переданного `from([1, 2, 3, 4])`. Для `of` метод **subscribe** выполнится только **1ин раз** и отправит все элементы сразу, если они в одном массиве, **а не перечислены через запятую** (иначе это будет последовательность Observable).
  * _**fromEvent**_ - создает Observable из event: `const source = fromEvent(document, 'click');`
  * _**interval**_ - emit новый индекс (бесконечную последовательность чисел) через заданный промежуток времени: `const source = interval(1000);`
  * _**range**_ - как **interval**, но задает ограниченный промежуток: `const source = range(1, 10);`
  * _**empty**_ - возвращает пустой Observable, для него выполнится сразу и _только_ **completed** ф-ция: `Rx.Observable.empty().subscribe(onComplete:()=>{})`
  * _**never**_ - создает Observable, который не делает emit и не завершается.
  * _**defer**_ (задерживать) - как **of,** но функция создания или объект выполняется только при **subscribe**, а не сразу при объявлении внутри **defer.** Например, это вернет дату созданную при вызове **subscribe**: `const s2 = defer(() => of(new Date()));`
  * _**timer**_ - как **interval,** но ждет перед началом выполнения заданное время <sub>(1 сек. в этом случае)</sub>: `const source = timer(1000, 2000);`
  * _**throwError**_ - создает **Observable,** который при **subscribe** делает emit ошибки попадающей в **catchError**. Как `Promise.reject()` из js
  * _**ajax**_ - для ajax запросов http
* **Combination**
  * _**forkJoin**_ - как `Promise.all` из js
  * _**concat**_ - связывает **obs1,** **obs2,** ... Когда _предыдущий_ **obs1** выполнился (completed), то делает **авто subscribe** на следующий связанный с ним **obs2** etc: `const example = sourceOne.pipe(concat(sourceTwo))`
  * _**concatAll**_ (`map + concatAll == concatMap`) - как **concat**, только не нужно вручную делать вызов **concat** чтобы соединить Observable, соединяет все Observable потока и делает subscribe на них.
  * _**merge**_ - объединяет **observables** в один **single Observable**
  * _**mergeAll(number)**_ (`map + mergeAll == mergeMap`) - похож на **flat** из js, используется когда нужно подписаться на массив Observable внутри которых Observable **(подписаться на inner Observable).** Поэтому часто используется **в связке с map,** чтобы слить все внутренние **Observables** и подписаться сразу на все. Например, когда есть массив ссылок каждая из которых должна выполниться: `from([1,2,3,4]).pipe(map(param => getData(param)), mergeAll()).subscribe(val => console.log(val));`
  * _**merge vs mergeAll**_ - как Я понимаю: **merge** выполнит **один subscribe** на всех Observable, а **mergeAll** имеет **отдельные subscribe** для каждого Observable.
  * combineAll
  * _**combineLatest**_ - можно комбинировать несколько Observable и подписаться на них **в одном subscribe,** в **subscribe** передастся **array последних значений** для каждого Observable. Т.е. независимо от того какой **(один)** из комбинированных Observable выполнится **все** их **последние значения** будут переданы массивом в **subscribe**: `combineLatest(timerOne, timerTwo, timerThree).subscribe(([timerValOne, timerValTwo, timerValThree]) => {});`
  * _**pairwise**_ - в **subscribe** попадет **2 значения, текущее и прошлое** (как в reduce из js): `interval(1000).pipe(pairwise(), take(5)).subscribe(console.log);`
  * startWith
  * _**withLatestFrom**_ - добавляет к value исходного Observable значение (value) какого-нибудь другого Observable заменяя передачу value на передачу array из нескольких value в виде: `[value, value2, ...]`. Пример: `source.pipe(withLatestFrom(source2), map(([first, second]) => {});`
  * _**zip**_ - комбинирует все Observable и **subscribe** ждет пока не выполнится **каждый из этих Observable**, array результатов передастся в **subscribe:** `zip(sourceOne, sourceTwo).subscribe(vals => console.log(vals));`
  * _**race**_ - **subscribe** выполнится **только для одного выполнившегося** _**первым**_ Observable: `Rx.Observable.race(obs1, obs2, obs3).subscribe(val => console.log(val));`
* **Conditional**
  * _**every**_ - работает как **everу** в js для Stream, на входе может быть **несколько** Observable, если **все** значения true, то в **subscribe** передастся true.
  * _**defaultIfEmpty**_ - если ничего не было emit, то при complete будет emit указанного значения: `empty().pipe(defaultIfEmpty('Observable.empty()!'));`
* **Filtering**
  * _**debounce**_ - как **debounceTime**, но можно передать функцию, которая вычисляет время задержки как параметр.
  * _**debounceTime**_ - пропускает emit (и его values как результат), если оно выполнилось меньше чем указанное время назад: `example.pipe(debounceTime(500)).subscribe(console.log);`
  * _**throttle(fn)**_ - emit последние значения (values), но подавляет новые пока функция переданная в **throttle** не выполнится: `source.pipe(throttle(val => interval(2000)));`
  * _**throttleTime(time)**_ - тоже что и **throttle**, но с таймером.
  * _**throttle vs debounce**_ - **debounce** игнорирует values заданное время и делает emit только **1го последнего value** результирующего (e.g набор текста Search bar и emit целого набранного текста), **throttle** _полностью игнорирует_ новые emit заданное время, никакое из них выполнено не будет никогда (e.g нажатие курка пистолета и игнорирование нажатий пока не заряжен).
  * _**distinctUntilChanged(fn)**_ - выполнится только если новое значение **отличается** от прошлого, **можно** передать свою function для сравнения как параметр: `source.pipe(distinctUntilChanged()).subscribe(console.log);`
  * filter
  * _**take(num)**_ - взять только N событий потока.
  * _**takeUntil**_ - когда переданный параметром Observable **выполнится,** то выполнит **complete** для всех Observable потока.
  * _**single**_ - как find из js, делает emit только 1го значения.
  * skip
  * _**last**_ - берем последний Observable который **completed**: `source.pipe(last());`
* **Transformation**
  * map
  * _**mapTo**_ - просто подменяет возвращаемое каждым Observable значение на свое: `pipe(mapTo('bla'))`
  * _**mergeMap**_ (flatMap) - это **mergeAll** + **map**. **flatMap** это тоже что и **mergeMap**, т.к. **mergeAll** это как **flat**.
  * switchMap
  * _**scan**_ - как **reduce** из js, выполняется, если есть подписка **subscribe**. **Каждая** итерация цикла **scan** попадает как **value** в  (т.е. **subscribe** выполнится для каждого Observable и в **subscribe** попадет результат **scan)**: `source.pipe(scan((acc, curr) => acc + curr, 0)).subscribe(val => console.log(val));`
  * _**reduce**_ - как **scan,** но делает emit результата только один раз в конце своей работы.
  * _**pluck**_ - как **filter,** но фильтрует возвращаемые Observable объекты **по именам** свойств объектов: `from([{ name: 'Joe', age: 30, job: { title: 'Developer', language: 'JavaScript' } }]).pipe(pluck('job', 'title'));` (берем только объекты со свойством `job`, внутри которого свойство `title`)
  * _**concatMap**_ - это **map + concatAll** 
  * _**buffer**_ - помещает в буфер все значения stream до того как сработает указанное в нем событие. После срабатывания указанного события (bufferBy) все значения из буфера передаются в **subscribe:** `myInterval.pipe(buffer(bufferBy));`
  * _**bufferTime**_ - тоже что и **buffer**, но собирает в буфер до наступления указанного времени.
  * toArray
  * _**groupBy**_ - группирует значения событий по указанному свойству объекта **в отдельные** Observable значение **каждого** из которых передастся в **subscribe,** если значения указанного свойства совпадает, то объект добавляется в группу (группа это array значений): `source.pipe(groupBy(person => person.age));`
* **Multicasting** - операторы описаны ниже отдельно, т.к. они сложны
  * share
  * publish
  * observable.publish().refCount()
  * multicast
* **Utility**
  * _**tap**_ - как forEach из js, просто выполняет действие, например logging (похоже на сквозную задачу из AOP - aspect-oriented programming). Дополнительные действия еще называют side effect.
  * _**delay**_ - задерживает выполнения функции объявленной в `Object.create(() => {})` которая создает событие. Т.е. задерживает **само** выполнение subscribe, а не создание Observable.
* **Error Handling**
  * _**catchError**_ - перехват ошибки и выброс вместо нее чего-то, т.е. catchError в pipe и работает **как map** и может вместо throw ошибки отдать Observable: `return of(error)`
  * throwError
  * _**retry(num)**_ - как **retryWhen**, но указываем только количество раз которое повторится retry (т.е. выполнение всей **sequence** заново) при выбросе ошибки.
  * _**retryWhen**_ - в случае throw принимает Observable с value содержащим **описание ошибки** (то value что было в throw), когда **блок retryWhen завершен** делает пере выброс **всей** последовательности заново (правильные value и то что было throw). Т.е. обработка **sequence** событий внутри **pipe** и **subscribe** происходит заново **с самого начала**, как если бы ошибки не было и так будет повторяться пока есть хотя бы 1на throw error в потоке: `source.pipe(retryWhen(errors => errors.pipe(tap(console.log), delayWhen(val => timer(val * 1000)))));` Можно реализовать свою функцию **стратегии** выброса ошибок с указанием max количества раз повторения etc [пример](https://www.learnrxjs.io/operators/error_handling/retrywhen.html)

***

# Примеры subscribe
```js
var source = Rx.Observable.empty();

// 1. через функции
var subscription = source.subscribe(
  function (x) {
    console.log('Next: %s', x);
  },
  function (err) {
    console.log('Error: %s', err);
  },
  function () {
    console.log('Completed');
  });
  
// => Completed

// 2. через объект
source.subscribe({
  next: onNextFn,
  error: onErrorFn,
  complete: onCompleteFn
});

// 2. через ф-ции с именами
source.subscribe({
  next(x) {
    if (count++ % n === 0) observer.next(x);
  },
  error(err) { observer.error(err); },
  complete() { observer.complete(); }
})

// 3. Вызов методов вручную из subscribe
var observer = Rx.Observer.create(
  x => console.log(`onNext: ${x}`),
  e => console.log(`onError: ${e}`),
  () => console.log('onCompleted')
);

// вызываем с передачей им значений
observer.onNext(42);
observer.onError(new Error('error!!'));
observer.onCompleted();
```

# asObservable
**subject.asObservable();** - преобразует subject в Observable, например чтобы в него нельзя было сделать `subject.next(myVal)` (неточная аналогия: как инкапсуляция private метода). В этом случае вызов **next** вызовет **error.**

# toPromise
**toPromise** - преобразует Observable в Promise из js, не поддерживает **pipe.**
```js
Observable.of('foo').toPromise(); // this
Observable.of('foo').pipe(toPromise()); // ошибка
Observable.of('foo').pipe(take(1)).toPromise(); // после pipe работает
```

# projection function

**projection function** - это функция, которая может передаваться как параметр операторам и влиять на значения, которые попадают в subscribe. Например он может сложить 2ва значения. Наверное можно назвать это посто процессор результата.

Методы с которыми используется: **combineLatest,** **withLatestFrom,** **expand**, **map** (mergeMap, concatMap, switchMap, exhaustMap)

**Пример:**
```js
const combinedProject = combineLatest(obs1, obs2, obs3,
  (one, two, three) => one + two + three;
);
const subscribe = combinedProject.subscribe(latestValuesProject =>
  console.log(latestValuesProject) // 'onetwothree'
);
```

# Популярные операторы RxJS в Angular
Это список популярных по мнению авторов Angular операторов [тут](https://angular.io/guide/rx-library#common-operators).

| Area           | Operators                                                     |
| -------------- | ------------------------------------------------------------- |
| Creation       | from,fromEvent, of                                            |
| Combination    | combineLatest, concat, merge, startWith , withLatestFrom, zip |
| Filtering      | debounceTime, distinctUntilChanged, filter, take, takeUntil   |
| Transformation | bufferTime, concatMap, map, mergeMap, scan, switchMap         |
| Utility        | tap                                                           |
| Multicasting   | share                                                         |

# Flattening Strategy (concatMap vs mergeMap vs switchMap vs exhaustMap)
Источник с пояснением [тут](https://medium.com/@shairez/a-super-ninja-trick-to-learn-rxjss-switchmap-mergemap-concatmap-and-exhaustmap-forever-88e178a75f1b) и [тут](https://blog.angular-university.io/rxjs-higher-order-mapping/) и [тут](https://stackoverflow.com/a/49701944) (лучше), а [тут](https://netbasal.com/understanding-mergemap-and-switchmap-in-rxjs-13cf9c57c885) понятнее про merge и switch

**mergeAll, concatAll, switchAll, exhaust** - работают **как flat** из js, т.е. принимают number как параметр, который показывает насколько глубоко раскрыть array и сделать его flat (плоским, одномерным). В случае Rx array это sequence последовательность событий Observable или stream. **flat** в данном случае означает **subscribe** на каждый Observable **внутри** внешнего **subscribe** (т.е. **subscribe** на **inner** Observable) mergeAll это можно сказать чистый flat, остальные варианты делают дополнительную работу **до** или **после** (как AOP).

```js
// пример mergeMap вручную
// т.е. это ПЕРЕподписка внутри подписки
observable.pipe(
  map(framework => getAgency(framework) ),
  map(agency => agency.getRecruitsObservable() )
).subscribe( recruitsObservable => {
  recruitsObservable.subscribe(recruit => console.log(recruit));
});

// Простая реализация mergeMap
// возвращаем Observable внутри которого subscribe на переданный inner Observable
function myMergeMap(innerObservable) {
  /** the click observable, in our case */
  const source = this; 
  
  return new Observable(observer => {
    source.subscribe(outerValue => {
      // для switchMap тут стояло бы unsubscribe для предыдущего Observable
      // innerObservable.unsubscribe(); // вот так

      /** innerObservable — the interval observable, in our case */
      innerObservable(outerValue).subscribe(innerValue => {
        observer.next(innerValue);
      });
   });
  });
 }
Observable.prototype.myMergeMap = myMergeMap;
```

**mergeMap (flatMap)** (`map + mergeAll`) - подписывается на все **inner** Observable, **сразу**. Делает **merge** значение которое emit из **inner** Observable в **outer** Observable.

**concatMap** (`map + concatAll`) - подписывается на все **inner** Observable по очереди, **когда предыдущий completed**.

**switchMap** (`map + switchAll`) - подписывается на все **inner** Observable по очереди, **отменяет предыдущий запрос** (`switch to a new observable`).

**exhaustMap** (`map + exhaust`) - подписывается на все **inner** Observable, только когда текущий Observable **завершил выполнение** (emit), пока emit не завершен новые Observable **игнорируются** (`don’t interrupt me`) 

_**В реальной жизни:**_
* **flatMap** (mergeMap) - например есть список фильмов, у каждого объекта есть ссылка на рейтинг, можно преобразовать каждый такой объект с инфой о фильме в http запрос
* **concatMap** - есть список фильмов с рейтигом, если мы хотим запросить их рейтинг с другого сайта по порядку и тем самым сохранить порядок загрузки
* **switchMap** - отменяет старый запрос, если сделан новый, наприме для AutoComplete или для переключения вкладок (отменяем прежний запрос на начитку данных)
* **exhaustMap** - например пользователь кликает кнопку логина много раз, чтобы игнорировать все клики (обработку событий) пока не выполнится первый клик (не будет complete)

# throttleTime, debounceTime, auditTime, bufferTime, windowTime

_**В реальной жизни:**_
* **throttleTime** - пропускает все события заданное время и никогда не выполнится для них (например нажатие курса сработает только когда пистолет перезарядится)
* **debounceTime** - выполнится после заданного времени для результирующего значения (последнего). Например при вводе в поиск, если пользователь ничего не вводил заданное время. Если пользователь что-то ввел, то таймер сбрасывается опять.
* **auditTime** - как debounceTime, но таймер не сбрасывается, т.е. поисковые предложения при вводе в поиск будут показываться во время ввода.
* **bufferTime** - копит буфер заданное время, а после делает emit всех значений
* **windowTime** - открывает "окно" в промежутке времени которого создается Observable с values собранными за этот промежуток. Т.е. выброшенные запросы поделятся на группы, каждая группа будет в своем окне: (1, 2, 3) (4, 5, 6) (7, 8, 9) (отделены друг от друга)

# Scheduler

**Schedulers** - это способ контролировать **timing strategy** для tasks, т.е. время выполнения задач в js, т.к. выполнение задач в js построено на очередях задач, а не является полностью параллельным.

**Кратко о Event Loop в js:** (по порядку)
1. Сперва выполняется текущий синхронный код (callstack),
2. далее очередь микрозадач (Promise)
3. если нет другого синхронного кода - очередь макрозадач (готовый для исполнения код, обернутый функциями setTimeout() и setInterval() или AJAX-запросы)
4. Также отдельно имеется очередь для задач, которые должны выполниться сразу перед следующим циклом перерисовки контента.

**Кратко о типы RxJS Schedulers:**
1. queue - добавляет операцию в callstack;
2. asap - регистрирует операцию в очереди микрозадач;
3. async - регистрирует операцию в очереди макрозадач;
4. animationFrame - отвечает за действия, выполняемые перед перерисовкой.

Можно сказать, что в RxJs стратегия **Asap использует Promises**, а стратегия **Async использует setTimeout()**

```js
// Пример использования
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/observeOn';
import { async } from 'rxjs/scheduler/async';

var observable = Observable.create(function (observer) {
  observer.next(1);
  observer.next(2);
  observer.next(3);
  observer.complete();
}).observeOn(async);

// пример 2
of(9).pipe(
  observeOn(async)
).subscribe(
  vl => console.log('Value is: ', vl)
);
```

**Типы Scheduler:**
| Scheduler                   | Purpose                                                                                                                                                                        |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **null**                    | By not passing any scheduler, notifications are delivered synchronously and recursively. Use this for constant-time operations or tail recursive operations.                   |
| **queueScheduler**          | Schedules on a queue in the current event frame (trampoline scheduler). Use this for iteration operations.                                                                     |
| **asapScheduler**           | Schedules on the micro task queue, which is the same queue used for promises. Basically after the current job, but before the next job. Use this for asynchronous conversions. |
| **asyncScheduler**          | Schedules work with setInterval. Use this for time-based operations.                                                                                                           |
| **animationFrameScheduler** | Schedules task that will happen just before next browser content repaint. Can be used to create smooth browser animations.                                                     |
| **VirtualTimeScheduler**    | Will execute everything synchronous ordered by delay and mainly used in testing                                                                                                |

# Subjects

**Subject** - это вид Observable, который разделяет **(share)** выбрасываемое значение с несколькими Observable (в отличии от обычного Observable, который отправляет value только одному Observer). value рассылается многим **(multicast)** наблюдателям сразу.

**Типы:**
* _**Subject**_ - нет **init value,** не отправляет старые значения новым подписчикам (т.е. нет **replay behavior)**
* _**ReplaySubject(bufSize)**_ - как **Subject,** но все новые подписчики **(Observers)** получают все отправленные до этой новой подписки **(subscribe)** старые значения. Конструктор принимает размер буфера, количества **последних** старых значений, которые отправятся новым подписчикам.
* _**BehaviorSubject(initVal)**_ - как **ReplaySubject**, _требует_ **инициализирующее** (intial value) значение при создании. Буфер событий, которые отправятся новым **subscribers** равен **1му.**
* _**AsyncSubject**_ - отправляет value в subscribe (делает emit) только **последнего** значения и только когда **complete** уже произошел.

**Пример:**
```js
// Subject
const sub = new Subject();
sub.subscribe(console.log);
sub.next(2); // OUTPUT => 2

// ReplaySubject
const sub = new ReplaySubject(3);
sub.next(1);
sub.next(2);
sub.subscribe(console.log); // OUTPUT => 1,2

// BehaviorSubject
const subject = new BehaviorSubject(123);
subject.subscribe(console.log); // 123
subject.next(456);
subject.subscribe(console.log); // 456

// AsyncSubject
const sub = new AsyncSubject();
sub.subscribe(console.log);
sub.next(123); //nothing logged
sub.subscribe(console.log);
sub.next(456); //nothing logged
sub.complete(); //456, 456 logged by both subscribers
```

# Важные примеры использования
## Возвращаемое значение Observable.never()
Оно может быть `Observable<never>` и возможно нужно использовать `Observable.never<never>()`. Это может касаться **TypeScript** и других языков. Это не точно!

## Возвращаемое значение Observable.empty()
В некоторых языках типа TypeScript можно использовать `Observable.empty<User>();` чтобы уточнить тип возвращаемого.

```js
// еще вариант
import { EMPTY } from 'rxjs'
return EMPTY;
```
```js
// еще вариант (создание empty Observable)
import { empty } from 'rxjs';
const subscribe = empty().subscribe({
  next: () => console.log('Next'),
  complete: () => console.log('Complete!')
});
```

## Пример цепочки вызовов
```js
// цепочка вызовов на чистом js
getPromise()
.then(function(result) {
   // do something
   return promise;
})
.then(function(result) {
   // do something
   return promise;
})
.then(function(result) {
   // do something
   return promise;
})
.catch(function(err) {
    // handle error
});

// на RxJS
// (flatMap это тоже что и mergeMap)
import {from} from 'rxjs/observable/fromPromise';
import {catchError, flatMap} from 'rxjs/operators';

from(promise).pipe(
   flatMap(result => {
       // do something
   }),
   flatMap(result => {
       // do something
   }),
   flatMap(result => {
       // do something
   }),
   catchError(error => {
       // handle error
   })
)
```
## switchMap при переходе по ссылкам (e.g. Angular)
```ts
// Подписываемся на изменение параметров ссылки с отменой предыдущего события
ngOnInit() {
  this.hero$ = this.route.paramMap.pipe(
    switchMap((params: ParamMap) =>
      this.service.getHero(params.get('id')))
  );
}
```
**Примечание:** в Angular если изменений ссылки нет, то параметр можно доставать без subscribe статически:
```js
ngOnInit() {
  // ДОСТАЕМ ПАРАМЕТР СПОСОБ 1
    // этот id не изменится даже при повторном переходе в тот же component,
    // использовать осторожно
  let id = this.route.snapshot.paramMap.get('id');
}
```

## switchMap и отмена предыдущих событий на примере click
Каждый click запускает timer, каждый новый click останавливает предыдущий timer и начинает новый. Без switchMap каждый click запускал бы еще один новый timer не останавливая прошлый.
```js
import { interval, fromEvent } from 'rxjs';
import { switchMap } from 'rxjs/operators';

fromEvent(document, 'click')
  .pipe(
    // restart counter on every click
    switchMap(() => interval(1000))
  )
  .subscribe(console.log);

// Пояснение 1 (как работает mergeMap)
// 1. Создаем таймер на каждый клик подменяя возврат в map
// console.log выведет не values, а объекты Observable
const click$ = Observable.fromEvent(button, 'click');
const interval$ = Observable.interval(1000);
const clicksToInterval$ = click$.map(event => { return interval$; });
clicksToInterval$.subscribe(intervalObservable => console.log(intervalObservable));
// 2. Делаем subscribe внутри subscribe,
// т.к. mergeAll делает merge значения inner Observable в outer Observable
const click$ = Observable.fromEvent(button, ‘click’);
const interval$ = Observable.interval(1000);
const observable$ = click$.map(event => { return interval$; });
observable$.mergeAll().subscribe(num => console.log(num));
// 3. Заменяем на shortcut: mergeMap = map + mergeAll
const click$ = Observable.fromEvent(button, ‘click’);
const interval$ = Observable.interval(1000);
const observable$ = click$.mergeMap(event => { return interval$; });
observable$.subscribe(num => console.log(num));
// 4. switch - как mergeMap, но отменяет прошлый subscription на inner Observable,
// когда новый есть emit
const observable$ = click$.map(event => { 
   return interval$;
});
observable$.switch().subscribe(num => console.log(num));
```

## Кэш на RxJs (аналог redux, который часто используется с React) и немного про NgRx
Сразу чтобы было понятно, **redux это по сути Subject из RxJs**, только чуть более продвинутый, с возможностью подключения фильтров и проброса разных функций в него. Часто используют название **Store** для обозначения хранилища данных внутри redux, хотя по сути этот **Store и является тем же Subject**. Все эти определения **однонаправленный поток данных** (unidirectional data flow) и прочее это по сути просто красивые слова описывающие этот чуть более продвинутый вариант `Subject` с обертками в виде **hight order function** или **proxy** паттернами вокруг него.

Суть в том, что redux работает как **одно общее хранилище** данных для компонентов. И в отличии от Angular не нужно создавать много отдельных Subject объектов для каждого компонента или группы компонентов. Это бывает очень удобно, если нужно переиспользовать **одни и те же данные** в **разных компонентах**.

**В отличии от redux стандартный подход в Angular это:** В документации Angular написано, что данные нужно передавать или через `@ViewChild myComponent;` или через свойство в теге `<bla [(myData)]="blabla"><bla>` которое делает `Emit Event` (передача данных между компонентами через специальное событие Angular), это если передача **через связь родитель-дочерний**. Или через глобальный объект, который сервис и который имеет переменную `Subject` объект (который тоже глобальный т.к. сервис в котором он находится глобальный) - это в документации называется **сервис-мост**.

Существует уже готовый аналог redux для Angular, это [NgRx](https://ngrx.io)

**Пример кэша, упрощенный аналог redux:**
```ts
// Кэш на RxJs

// Service (кэш)
@Injectable()
export class DataService {
    private dataObs$ = new ReplaySubject(1);

    constructor(private http: HttpClient) { }

    getData(forceRefresh?: boolean) {
        // If the Subject was NOT subscribed before OR if forceRefresh is requested 
        if (!this.dataObs$.observers.length || forceRefresh) {
            this.http.get('http://jsonplaceholder.typicode.com/posts/2').subscribe(
                data => this.dataObs$.next(data),
                error => {
                    this.dataObs$.error(error);
                    // Recreate the Observable as after Error we cannot emit data anymore
                    this.dataObs$ = new ReplaySubject(1);
                }
            );
        }

        return this.dataObs$;
    }
}

// использование в Component
@Component({
  selector: 'my-app',
  template: `<div (click)="getData()">getData from AppComponent</div>`
})
export class AppComponent {
  constructor(private dataService: DataService) {}

  getData() {
      this.dataService.getData().subscribe(
          requestData => {
              console.log('ChildComponent', requestData);
          },
          // handle the error, otherwise will break the Observable
          error => console.log(error)
      );
  }
}
```
## Аутенфикация на RxJs и скрытие тегов для неавторизованных (e.g. Angular)
**Суть:** делаем _Subject_ хранящий _true/false_ для зарегистрированный/незарегестрированный user. Подписчики,например Directives, могут подписываться и получать эти значения при входе/выходе. Components и Directives могут при получении этих значений скрывать, показывать, менять элементы.

Кроме true/false можно хранить объект User с его ролями и др. инфой.

```ts
// этот пример СХЕМАТИЧНЫЙ, нужно использовать особенности Angular

// простой пример
export class AuthService {
  private BehaviourSubject auth = new BehaviourSubject<User>(new User('Guest'));
  enter() {
    User user = this.getUser();
    if(user) {
      this.user.next(user);
      return user;
    }
  }
  exit() {
    this.user.next(null);
  }
}

// directive, использование для аутентификации
export class isAuth {
  constructor(el, public AuthService authService) {
    this.el = el;
  }
  onInit() {
    authService.subscribe(user => {
      if() {
        el.nativeElement.style.display = 'none';
      } else {
        el.nativeElement.style.display = 'block';
      }
    });
  }
}


// directive, использование для авторизации
export class hasRole {
  constructor(el, public AuthService authService) {
    this.el = el;
  }
  onInit() {
    authService.subscribe(user => {
      if(['user', 'admin'].includes(user.role)) {
        el.nativeElement.style.display = 'none';
      } else {
        el.nativeElement.style.display = 'block';
      }
    });
  }
}
```

## Отписка от событий в деструкторе компонента (e.g. Angular) и от чего можно не отписываться
### Отписка от событий в деструкторе компонента
**Суть:** **subscribe** работает до момента когда выполнится событие в **takeUntil**, которое завершит stream (т.е. можно сказать уничтожит объект или сделает unsubscribe, чтобы он не занимал место в памяти), вызываем его в **ngOnDestroy** и все подписки уничтожаются.
```ts
class myComponent {
  private destroyed$: ReplaySubject<boolean> = new ReplaySubject(1);

  constructor(
    private serviceA: ServiceA,
    private serviceB: ServiceB,
    private serviceC: ServiceC) {}

  ngOnInit() {
    this.serviceA
      .pipe(takeUntil(this.destroyed$)) // подписка работает до того как выполнится событие this.destroyed$
      .subscribe({next(){}});
  }

  // выполняем событие this.destroyed$ и это влечет отписку
  ngOnDestroy() {
    this.destroyed$.next(true);
    this.destroyed$.complete();
  }
}
```

В Angular не все объекты требуют unsubscribe, но если для них его сделать, то ничего плохого не будет.

### От чего можно не отписываться (Angular делает отписка сам)
**1. ActivatedRoute - не требует unsubscribe**. Note, есть [баг](https://github.com/angular/angular/issues/16261) согласно которому это не работает, хотя в официальной документации написано, что это работает.
```ts
// ActivatedRoute не требует unsubscribe
export class Routable2Component implements OnInit {
    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.url
            .subscribe(url => console.log('The URL changed to: ' + url));
    }
}
```

**2. Async pipe - не требует unsubscribe**
```html
<todos [todos]="todos$ | async"></todos>
```

**3. @HostListener - не требует unsubscribe**
```ts
export class TestDirective {
  @HostListener('click')
  onClick() {}
}
```

**4. Конечные обозреваемые последовательности - не требует unsubscribe** (например методы модуля **http** такие как `get()`, `post()` etc или `timer()`)
```ts
Observable.timer(1000).subscribe(console.log);
this.http.get('http://api.com').subscribe(console.log);
```

**5. take(1) и first() - не требует unsubscribe**. Тут есть исключение, когда отписка требуется, данные операторы выполняются **1-ин раз** и сразу делаются **complete** (подписка удаляется). Но если observable на который был subscribe никогда не выполнится, то **unsubscribe делать нужно**. Случай когда **не нужно** делать **unsubscribe** для `take(1) и first()` - это например когда они используются для **инициализации** (например в `ngOnInit()`) и тогда они точно выполнятся хотя бы один раз.
```ts
@Component
export class MyComponent{
  ngOnInit() {
    this.search$
      .pipe(take(1))  // выполнится сразу и только один раз, unsubscribe не нужно делать
      .subscribe(search => doSmth(search.query));
  }
}
```

## take(1) и first() для инициализации (выполнения чего-либо только один раз при запуске)
`take(1)` и `first()` выполнится сразу и только один раз, unsubscribe не нужно делать. `first()` это как `filter` и `take(1)`, возможно использовать `first()` это лучшая практика.

```ts
@Component
export class MyComponent{
  ngOnInit() {
    this.search$
      .pipe(take(1))  // выполнится сразу и только один раз, unsubscribe не нужно делать
      .subscribe(search => doSmth(search.query));
  }
}
```

## Type-ahead suggestions (autocomplete, авто комплит)
Пример autocomplete который:
* конвертирует событие в value (достает value из событие когда это событие выполняется, т.е. работает как фильтр)
* срабатывает только если длина строки > 2
* срабатывает если пользователь ничего не набрал 10 сек, иначе сбрасывает таймер и опять ждет 10 сек
* срабатывает только если value изменилось
* отменяет запрос если результат стал другим (изменился при следующем запросе)
```js
import { fromEvent } from 'rxjs';
import { ajax } from 'rxjs/ajax';
import { map, filter, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';

// вариант 1
const searchBox = document.getElementById('search-box');
  
const typeahead = fromEvent(searchBox, 'input').pipe(
  map((e: KeyboardEvent) => e.target.value),
  filter(text => text.length > 2),
  debounceTime(10),
  distinctUntilChanged(),
  switchMap(() => ajax('/api/endpoint'))
);
  
typeahead.subscribe(data => {
  // Handle the data from the API
});

// вариант 2, с вызовом service поиска в Angular
this.packages$ = this.searchText$.pipe(
  debounceTime(500),
  distinctUntilChanged(),
  switchMap(packageName =>
    this.searchService.search(packageName, this.withRefresh))
);
```

## Exponential backoff (повтор запроса каждые N сек, с увеличением времени между повторами по экспоненте)
Повторяет запрос N раз с увеличением времени между повторами по экспоненте пока не наступит limit повторов.
```js
import { pipe, range, timer, zip } from 'rxjs';
import { ajax } from 'rxjs/ajax';
import { retryWhen, map, mergeMap } from 'rxjs/operators';
  
function backoff(maxTries, ms) {
  return pipe(
    retryWhen(attempts => zip(range(1, maxTries), attempts)
      .pipe(
        map(([i]) => i * i),
        mergeMap(i =>  timer(i * ms))
      )
    )
  );
}
  
ajax('/api/endpoint')
  .pipe(backoff(3, 250))
  .subscribe(data => handleData(data));
  
function handleData(data) {
  // ...
}
```

## Показ progress для request (прогресса выполнения запроса) в Angular
```js
// включаем поддержку progress для request
const req = new HttpRequest('POST', '/upload/file', file, {
  reportProgress: true
});

// функция getEventMessage в зависимости от типа event показывает сообщение
// и прогресс, last берет последний завершенный (completed)
return this.http.request(req).pipe(
  map(event => this.getEventMessage(event, file)),
  tap(message => this.showProgress(message)),
  last(), // return last (completed) message to caller
  catchError(this.handleError(file))
);

private getEventMessage(event: HttpEvent<any>, file: File) {
  switch (event.type) {
    case HttpEventType.Sent:
      return `Uploading file "${file.name}" of size ${file.size}.`;
    case HttpEventType.UploadProgress:
      // Compute and show the % done:
      const percentDone = Math.round(100 * event.loaded / event.total);
      return `File "${file.name}" is ${percentDone}% uploaded.`;
    case HttpEventType.Response:
      return `File "${file.name}" was completely uploaded!`;
    default:
      return `File "${file.name}" surprising upload event: ${event.type}.`;
  }
}
```

## Обработка jwt и refreshToken, временная блокировка всех запросов пока новый jwt не запрошен через refreshToken (e.g. Angular)
**Суть:** когда jwt становится **invalid,** т.е. его срок действия прошел или он изменен на неверное значение, тогда сервер отвечает **error** или **401.** В этом случае `TokenInterceptor` перехватчик запускает обновление jwt через отправку **refreshToken.** `TokenInterceptor` останавливает все запросы, отправляет **refreshToken**, в ответ получает jwt который опять сохраняет в **authService** и начинает добавлять к каждому запросу.
<br>
**Ключевое касательно RxJS:** это способ блокировки параллельных запросов пока jwt не вернется в ответ, чтобы запросы происходящие когда запрос на получение **jwt** через **refreshToken** уже отправлен, но еще не получен. 
```ts
// auth.service.ts
// login и сохранение jwt и refreshToken
login(): Observable {
  return this.http.post(`${url}/login`, user).pipe(
    tap(tokens => {
      this.loggedUser = user.username;
      localStorage.setItem('JWT_TOKEN', tokens.jwt);
      localStorage.setItem('REFRESH_TOKEN', tokens.refreshToken);
    })
    .mapTo(true),
    catchError(error => {alert(error); return of(error)});
  );
}
logout() {
  // посылаем refreshToken чтобы его удалили
  return this.http.post(`${url}/logout`, {'refreshToken': this.getRefreshToken()})
    .pipe(
      tap(() => {
        this.loggedUser = null;
        localStorage.removeItem(tokens.jwt);
        localStorage.removeItem(tokens.refreshToken);
      })
      .mapTo(true),
      catchError(error => {alert(error); return of(error)});
    );
}
// обновление jwt через refreshToken
refreshToken() {
  // получаем все токены
  return this.http.post(url, {'refreshToken', this.getRefreshToken()})
    // сохраняем только jwt
    .pipe(tap(tokens: Tokens) => this.storeJwtToken(tokens.jwt));
}

// tokenInterceptor
// проверяет все исходящие запросы исходящие от HttpClient
// В Angular: этот Interceptor должен быть зарегистрировать в auth.module.ts например или в core module чтобы влиять на HttpClient
@Injectable()
export class TokenInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>();
  constructor(public authService: AuthService) { }
  intercept(req, next): Observable<> {
    if(this.authService.getJwtToken()) {
      // прикрепляем jwt к каждому request
      req = this.addToken(req, this.authService.getJwtToken());
    }
    return next.handler.(req)
      .pipe(catchError(err => { // перехватываем ошибку, невалидности jwt
        if(err instanceof HttpErrorResponse && err.status === 401) {
          return this.handle401Error(req, next); // обработка невалидного jwt
        } else {
          return throwError(error); // если дело не в jwt, то просто throw error
        }
      }));
  }
  // устанавливает http header с token
  private addToken(req, token) {
    return rew.clone({setHeaders: 'Authorization': `Bearer ${token}`});
  }
  // Этот метод касается RxJS техник.
  //  Суть: выполняем else блок, если isRefreshing == true,
  //    в нем next выполняется только после того как в refreshTokenSubject попадет jwt
  private handle401Error(req, next) {
    // запускаем обновление jwt по refreshToken
    if(!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);
      return this.authService.refreshToken().pipe(switchMap(token => {
        this.isRefreshing = false;
        this.refreshTokenSubject.next(token.jwt);
        return next.handle(this.addToken(req, token.jwt));
      }));
    // если обновление jwt уже запущено, но еще не завершено,
    //    то берем 1ин выполнившийся запрос через take(1),
    //    отменяем все прошлые запросы через switchMap,
    //    а если в запросе есть jwt, то добавляем его к запросу
    } else {
      return this.refreshTokenSubject.pipe(
        filter(token => token != null),
        // трансформируем в Observable который будет completed после того как возьмем 1ин event из этого Subject
        take(1),
        // req будет next (продолжен) только когда в refreshTokenSubject придет jwt
        switchMap(jwt => next.handle(this.addToken(req, jwt)))
      );
    }
  }
}


```

# Старые операторы, которые могут встретиться

Некоторые новые версии операторов работают внутри **pipe()**.

Нужно помнить, что некоторые пути для **import** операторов и объектов заменены в новых версиях RxJs.

Рекомендуется использовать **pipe()**:
```js
import { map, switchMap, throttle } from 'rxjs/operators';

myObservable
  .pipe(map(data => data * 2), switchMap(...), throttle(...))
  .subscribe(...);
```

**Список:**
* **just** заменен на **of**
* **do** при использовании pipe() заменен на **tap**
* **catch** заменен на **catchError**
* **finally** заменен на **finalize**
* **switch** заменен на **switchAll**
* **throw** заменен на **throwError**
* **fromPromise** рекомендуется заменить на **from** (он автоматически определяет что преобразовать)
* **let** устарел с приходом **pipe**

# lodash как пример похожих функций, но для массивов, а не событий, в качестве более понятного примера

[lodash](https://lodash.com) - библиотека различных готовых функций на JavaScript, много из которых похожи на RxJs (подобны SQL) и имеют теже названия. Они могут быть использованы в качестве более понятного примера, чтобы понять как работают RxJs функции.

# Материалы (удобнее пояснено, часть на английском)

* [Что такое Rx](https://gist.github.com/staltz/868e7e9bc2a7b8c1f754)
* В Rx используется LINQ и его операторы (как SQL только запросы к объектам), ознакомится можно [тут](https://en.wikipedia.org/wiki/Language_Integrated_Query)
* Hot vs Cold vs Warm Observables: [ноль](https://medium.com/@luukgruijs/understanding-hot-vs-cold-observables-62d04cf92e03), [раз](https://medium.com/@benlesh/hot-vs-cold-observables-f8094ed53339), [два](https://github.com/ReactiveX/rxjs/issues/2604#issuecomment-302047113), [тут упоминается warm](https://alligator.io/rxjs/hot-cold-observables/)
* [Список операторов](https://www.learnrxjs.io/operators/) (не всех), [Six Operators That you Must Know](https://netbasal.com/rxjs-six-operators-that-you-must-know-5ed3b6e238a0), [Eight Operators Worth Getting to Know](https://netbasal.com/rxjs-eight-operators-worth-getting-to-know-2b6c18e601d), [популярные операторы из списка Angular](https://angular.io/guide/rx-library#common-operators)
* [Что такое Subject](https://www.learnrxjs.io/subjects/)
* [switchMap, mergeMap, concatMap and exhaustMap (Flattening Strategy)](https://medium.com/@shairez/a-super-ninja-trick-to-learn-rxjss-switchmap-mergemap-concatmap-and-exhaustmap-forever-88e178a75f1b)
* [schedulers in RxJS](https://blog.strongbrew.io/what-are-schedulers-in-rxjs/), [scheduler в официальной документации](https://github.com/ReactiveX/rxjs/blob/master/docs_app/content/guide/scheduler.md)
* [устаревшее, но понятное краткое описание операторов](https://angularfirebase.com/lessons/rxjs-quickstart-with-20-examples/)
* [throttleTime, debounceTime, auditTime](https://medium.com/@jvdheijden/rxjs-throttletime-debouncetime-and-audittime-explained-in-examples-c393178458f3)

**Интересные примеры использования:**
* [цепочка последовательности вызовов как в Promise](https://stackoverflow.com/a/34523396)
* [When to use asObservable() in rxjs?](https://stackoverflow.com/a/36989035)
* [кэш http запросов в Angular на rxjs](https://stackoverflow.com/a/36413003) (некий аналог Redux)