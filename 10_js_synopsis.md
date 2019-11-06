- [Учебники и статьи](#Учебники-и-статьи)
  - [Основные](#Основные)
  - [Второстепенные](#Второстепенные)
  - [Полноценные учебники](#Полноценные-учебники)
- [Regular Expression](#regular-expression)
- [Список самых частых функций](#Список-самых-частых-функций)
- [Про Promise и очереди задач](#Про-promise-и-очереди-задач)
- [MediaStream](#mediastream)
- [Мои заметки](#Мои-заметки)
  - [Сложение через битовые операции](#Сложение-через-битовые-операции)
  - [Деление/умножение через битовые операции](#Делениеумножение-через-битовые-операции)
  - [Проверка на существование переменной](#Проверка-на-существование-переменной)
  - [Выполнение операции при условии выполнения другой операции](#Выполнение-операции-при-условии-выполнения-другой-операции)
- [Подключение js](#Подключение-js)
- [В JS на самом деле все выполняется в 1ном потоке, поэтому все отложенные операции типа таймеров выполнятся после обычных](#В-js-на-самом-деле-все-выполняется-в-1ном-потоке-поэтому-все-отложенные-операции-типа-таймеров-выполнятся-после-обычных)
- [fetch vs XMLHttpRequest](#fetch-vs-xmlhttprequest)
- [HOC - Hight Order Component](#hoc---hight-order-component)
- [Навигация в js (location, history)](#Навигация-в-js-location-history)
- [Кратко о событиях](#Кратко-о-событиях)
- [documentFragment - lightweight document](#documentfragment---lightweight-document)
- [reflow vs repaint](#reflow-vs-repaint)
- [Типы node (node types)](#Типы-node-node-types)
- [CDATA - character data](#cdata---character-data)
- [Entity символы](#entity-символы)
- [Разные теги html](#Разные-теги-html)
- [Реализация bind через apply](#Реализация-bind-через-apply)
- [Техника использования сортировки [].sort()](#Техника-использования-сортировки-sort)
- [attribute vs property в js](#attribute-vs-property-в-js)
- [callstack в js](#callstack-в-js)
- [mixin в js](#mixin-в-js)
- [Важные js библиотеки, движки, frameworks](#Важные-js-библиотеки-движки-frameworks)
- [Раздел про CSS и его некоторые особенности которые нужно запомнить](#Раздел-про-css-и-его-некоторые-особенности-которые-нужно-запомнить)
- [strict mode](#strict-mode)
- [новое на очереди в добавление](#новое-на-очереди-в-добавление)
- [Tuples (кортежи) в js (они же используются как immutabable структуры данных)](#tuples-кортежи-в-js-они-же-используются-как-immutabable-структуры-данных)
- [Неочевидные особенности JS с которыми можно столкнуться](#Неочевидные-особенности-js-с-которыми-можно-столкнуться)
  - [Выпадение переменных в ES5](#Выпадение-переменных-в-es5)
- [Деструкция](#Деструкция)
- [Операторы spread и rest и работа с ними](#Операторы-spread-и-rest-и-работа-с-ними)
- [Tagged Template (Тегированные шаблонные строки)](#tagged-template-Тегированные-шаблонные-строки)

# Учебники и статьи
## Основные

* [JavaScript Garden](http://shamansir.github.io/JavaScript-Garden/) - основы для старой версии js, работает везде и часто используется
* [Airbnb JavaScript Style Guide() {](https://github.com/airbnb/javascript) - примеры плохого и хорошего кода, **решение некоторых проблем**
* [Современный DOM: полифиллы](https://learn.javascript.ru/dom-polyfill) - полифилы это реализация новых стандартов js для старых браузеров (подключил - получил новые функции в старом браузере)
* [Regular Expressions](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Guide/Regular_Expressions) - от Mozilla, использование и особенности регулярных выражений в js
* [regexper.com](https://regexper.com/#%2Fhttps%3F%5C%3A%5C%2F%5C%2Fmy%5C.com%5C%2Fplayer%5C%2Fmovie%5Cd%2F) - графическое представление регулярных выражений в js
* [Closure Compiler](https://closure-compiler.appspot.com/home) - сжимает js заменяя имена на короткие и удаляя лишние отступы, также может оптимизировать
* [Introduction to Object-Oriented JavaScript](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Introduction_to_Object-Oriented_JavaScript) - пример реализации ООП (классов) на старой распространенной версии js. В новой появился class и ООП, только модификаторов доступа вроде private, protected нету. **Сейчас не используется**, новая версия js автоматически преобразуется в старую.
* [описание Shadow DOM, Virtual DOM, Style encapsulation](https://stackoverflow.com/questions/36012239/is-shadow-dom-fast-like-virtual-dom-in-react-js)
* [finished-proposals.md](https://github.com/tc39/proposals/blob/master/finished-proposals.md) - список того нового что добавили в js разбито по годам и версиям ES, версия на русском с инфой которой нет по ссылке от 2018 года [Обзор новшеств ECMAScript 2016, 2017, и 2018 с примерами](https://habr.com/ru/company/ruvds/blog/353174/)

## Второстепенные
* [Mastering the Module Pattern](https://ultimatecourses.com/blog/mastering-the-module-pattern#augmenting-modules) - старый, но до сих пор актуальный вариант модулей на js, о том что это и как их писать
* [Замыкания](https://developer.mozilla.org/ru/docs/Web/JavaScript/Closures) - до сих пор актуальный паттерн в js
* [What the f*ck JavaScript?](https://github.com/denysdovhan/wtfjs) - список неочевидных трюков на js, полезно ознакомиться
* [Introducing JavaScript objects](https://developer.mozilla.org/en-US/docs/Learn/JavaScript/Objects) - статья от Mozilla о том как писать классы на старом варианте js, без слова class
* [https://learn.javascript.ru/promise](https://learn.javascript.ru/promise) - на пальцах о том что такое Promise и как его использовать
* [асинхронный конструктор в js](https://stackoverflow.com/questions/43431550/async-await-class-constructor)
* [Data URLs](https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/Data_URIs) - статья о Data URLs, это по сути преобразование любых данных (файлы, изобращения, текст) в base64, причеи картинки и текст можно открывать вставив этот base64 в строку адреса браузера; можно вставлять этот base64 в `src` атрибуты **html** или **CSS** и картинки или текст будут отображаться
* [AJAX Getting Started](https://developer.mozilla.org/en-US/docs/AJAX/Getting_Started) - статья от Mozilla, работа с AJAX на чистом js
* [Sending forms through JavaScript](https://developer.mozilla.org/en-US/docs/Learn/HTML/Forms/Sending_forms_through_JavaScript) - статья от Mozilla о том как отправлять данные форм через XMLHttpRequest (ajax)
* [Пишем качественный код на jQuery](https://frontender.info/writing-better-jquery-code/) - примеры хорошего и плохого кода на jQuery
* [большой список готовых вставок скрипта для XSS аттак](https://pastebin.com/48WdZR6L)

## Полноценные учебники
* [Выразительный JavaScript](http://habrahabr.ru/post/240219/) - цикл статей учебника на русском языке с хабра
* [You Don't Know JS (book series)](https://github.com/getify/You-Dont-Know-JS) - цикл статей, учебник по js

# Regular Expression
* [Полезная статья о том как правильно использовать reg exp](https://www.regular-expressions.info/javascript.html)
* методы
  * match, matchAll, exec, search, test

1) при использовании reg ex в js нужно использовать `//` если используются строки `${}` т.к. один слэш исчезает при вставке
```js
const r3s = `\\/(${w})*(?![^(]*\\))`;
const r3 = new RegExp(r3s, 'uig');
```

2) паттерны символов различных алфовитов в js т.к. обычное `\w` (слово) совпадает только с латинским алфавитом https://stackoverflow.com/a/37668315

2) генератор unicode наборов символов для regexp чтобы не искать в табл https://apps.timwhitlock.info/js/regex#

# Список самых частых функций

* [encodeURI()](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURI) - кодирование ссылок в base64, чтобы можно было использовать спец. символы. Можно кодировать картинки и вставлять потом их в CSS стили в виде base64. Есть версия [encodeURIComponent](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/encodeURIComponent) с исправленными недостатками.
* **encodeURI vs encodeURIComponent** - 
* [JSON.stringify()](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/JSON/stringify) - перевод объекта JSON в строка, для передачи по AJAX.
* [EventTarget.addEventListener()](https://developer.mozilla.org/en-US/docs/Web/API/EventTarget/addEventListener) - добавление слушателей событий
* [Sending forms through JavaScript](https://developer.mozilla.org/en-US/docs/Web/Guide/HTML/Forms/Sending_forms_through_JavaScript) - отправка формы через js
* [void operator](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Operators/void) - использовался чтобы отменить действия по клику на ссылки. Встречается и сейчас, хотя устарел. <sub>Часто используется для bookmarklet - вставок js в закладки браузера</sub>
* [Window.frames](https://developer.mozilla.org/en-US/docs/Web/API/Window/frames) - получение frame/iframe и работа с ними. <sub>Если будут проблемы нужно обернуть работу в ```try{ ... }catch(e){}```</sub>
* [try...catch](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Statements/try...catch) - работа с try...catch и собственными Exception (можно писать просто строку `throw 'My Exception'`)
* [Document.querySelector()](https://developer.mozilla.org/en-US/docs/Web/API/Document/querySelector) - новая замена getDocumentBy() и подобному
* [MutationObserver](https://developer.mozilla.org/en-US/docs/Web/API/MutationObserver) - новый слушатель событий изменения элементов страницы. Можно использовать старый способ с событиями [Mutation events](https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Mutation_events)
* [Document.documentElement](https://developer.mozilla.org/en-US/docs/Web/API/Document/documentElement) - NOD элемент Document, через него нужно работать с document
* [createTextNode]() - создает text тег, который просто текст: `f.append(document.createTextNode(url.protocol + ' URL not supported'));`
* [Document.cookie](https://developer.mozilla.org/en-US/docs/Web/API/Document/cookie) - работа с cookie
  * `httpOnly` параметр в cookie делает недоступным cookie для js
  * `secure` параметр делает доступным cookie только при использовании https
* [Node.textContent](https://developer.mozilla.org/en-US/docs/Web/API/Node/textContent) - получить только текстовое содержимое элементов
* **innerText** vs **textContent** - коротко, innerText вернет только текст видимый на странице (не скрытый CSS или блоками html), поэтому он **медленный** (чтобы понять видим текст или нет ему его с начало нужно прорисовать), textContent - вернет весь текст, он **быстрее.**
  * https://stackoverflow.com/questions/35213147/difference-between-textcontent-vs-innertext
* [Window.getComputedStyle()](https://developer.mozilla.org/en-US/docs/Web/API/Window/getComputedStyle) - получить CSS элемента уже после его применения <sub>(только те стили, которые имеют эффект, а не потерялись при наследовании от других элементов)</sub>
* [getting all selected values of a multiple select box](http://stackoverflow.com/questions/5866169/getting-all-selected-values-of-a-multiple-select-box-when-clicking-on-a-button-u) - взять значения ```<select>```
  ```html
  <select multiple>
    <option>opt 1 text
    <option value="opt 2 value">opt 2 text
  </select>
  ```
  ```js
  var el = document.getElementsByTagName('select')[0];
  select.options[0];
  select.options[0].selected;
  select.options[0].value;
  select.options[0].text;
  ```
* циклы
  * **for in** - перебирает свойства объекта, внутри нужно использовать `hasOwnProperty` для проверки, что свойство не принадлежит предку Object
  * **for of** - более новая альтернатива, лучше использовать ее в том числе вместно `for in`
  * **for-await-of** - итерирует Promise на каждом шаге извлекая значения (работая как `await`), на самом деле она сделана для мест которые трудно итерировать при асинхронности, например генераторов
    ```js
    for await (const line of readLines(filePath)) {
      console.log(line);
    }
    ```
  * **forEach** - stream, в нем нельзя сделать `break`, как и во всех stream
* Ф-ции передачи контекста:
  * [bind](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Function/bind) - передает свой параметр как контекст выполнения объекту: `f1.bind(this); // f1 выполнится в контексте this`. В отличии от call и apply метод bind выполнится _отложено_ (т.е. **bind** создает **ссылку** на ф-цию, которую можно выполнить потом, а **call & apply** сразу in place **выполняют на месте** своего вызова)
  * **call** - передает не только контекст, но и параметры: `f1.call(this, param1, param2);`
  * **apply** - как call, но параметры передает как array: `f1.apply(this, [param1, param2]);`
* методы Object
    * **create** - создает объект наследуя свойства из указанного в нем параметра: `Object.create(obj.prototype);`. Можно создать объект без наследованных свойств: `Object.create(null);`
    * **freeze** - делает объект const (не изменяемым), может заморозить и array, при этом скорость операций `frozen.indexOf(v), frozen.includes(v), fn(...frozen), fn(...[...frozen]) и fn.apply(this, [...frozen])` может быть быстрее
    * **preventExtensions** - запрещает "наследование" (Extensions)
    * **assign** - копирует свойства одного объекта в другой, можно так создать копию: `Object.assign({}, obj);`
    * **entries** - array пар `{key: val}` или `Map`
    * **keys** - keys для пар `{key: val}` или `Map`
    * **values** - values для пар `{key: val}` или `Map`
    * **fromEntries** - reverses Object.entries
    * **defineProperty / defineProperties** - можно добавлять properties в объект
    * **is** - сравнивает 2ва объекта: `Object.is(window, window); //true`
    * **seal** - запрещает добавлять или удалять свойство и менять существующее (но не их значения): `Object.seal(object1.property1);`
    * **getOwnPropertyDescriptors** - возвращает все собственные (не унаследованные) properties обьекта (в отличии от `Object.assign({}, MyObj))` способ с `Object.defineProperty({}, Object.getOwnPropertyDescriptors(MyObj)))` копирует getters и setters
* [Web Workers API](https://developer.mozilla.org/en-US/docs/Web/API/Web_Workers_API) - это js выполняемый в фоне в отдельном контексте. Они должны быть в отдельном js файле и создаваться: `new Worker('task.js');` Т.к. js выполняется последовательно долгие операции могут замедлять страницу. В отдельном контексте скрипт выполняется в отдельном потоке обмениваясь сообщениями со страницей через `navigator.serviceWorker.addEventListener('message', event => {});` и `postMessege` или через `BroadcastChannel`. scope это url одного и того же домена, все вкладки с одинаковым адресом имеют доступ к одним и тем же ServiceWorkers. WW это не часть js, а часть браузера. Web Worker имеют доступ к объектам: **navigator, location, XMLHttpRequest, setTimeout()/clearTimeout() и setInterval()/clearInterval(), Cache API, importScripts(), могут создавать другие Workers**; не имеют доступ к: **DOM** (потоконебезопасно), **window, document, parent**
  * **Dedicated Workers** - обмениваться с ним может только создавший процесс
  * **Shared Worker** - с этим worker могут связываться разные вкладки (а не одна)
  * **Service Worker** - может реагировать на события (`addEventListener('fetch', event => {})`) и работать как прокси для открытой страницы, (напр. чтобы кэшировать данные, в Angular это его основное назначение). Часто используют для создания PWA храня данные в IndexedDB к которому есть async доступ.
* ServiceWorker и работа с ним:
  * регистрируем serviceWorker
    ```js
    navigator.serviceWorker.register('./sw.js')
      .then(() => navigator.serviceWorker.ready.then((worker) => {
        worker.sync.register('syncdata');
      }))
      .catch((err) => console.log(err));
    ```
  * События в `./sw.js`
    ```js
    self.addEventListener('install', (event) => {
        console.log('Установлен');
    });

    self.addEventListener('activate', (event) => {
        console.log('Активирован');
    });

    self.addEventListener('fetch', (event) => {
        console.log('Происходит запрос на сервер');
    });
    ```
  * использование **event.waitUntil** и **event.respondWith** для **Cache API** кэша
    ```js
    self.addEventListener('install', (event) => {
        event.waitUntil(
            caches.open(CACHE).then((cache) => {
                return cache.addAll(['/img/background']);
            })
        );
    });
    // при событии fetch, мы и делаем запрос, но используем кэш, только после истечения timeout.
    self.addEventListener('fetch', (event) => {
        event.respondWith(fromNetwork(event.request, timeout)
          .catch((err) => {
              console.log(`Error: ${err.message()}`);
              return fromCache(event.request);
          }));
    });
    ```
  * остановка
    * с главной страницы метода `worker.terminate()`
    * внутри воркера `self.close()`
* [window.postMessage](https://developer.mozilla.org/en-US/docs/Web/API/Window/postMessage) - позволяет разным вкладкам и iframe обмениваться сообщениями (для использования **указывается домен которому отправить сообщение**)
* [BroadcastChannel](https://developer.mozilla.org/en-US/docs/Web/API/BroadcastChannel) - создает именованный канал, через который можно обмениваться сообщениями разным вкладкам и iframe
* [The structured clone algorithm](https://developer.mozilla.org/en-US/docs/Web/API/Web_Workers_API/Structured_clone_algorithm) - список типов которые могут быть clone(), ими можно обмениваться в сообщениях через postMessege()
* [IndexedDB](https://developer.mozilla.org/en-US/docs/Glossary/IndexedDB) - БД в браузере, можно зранить разные данные, поддерживает транзакции (для работы с ним часто используют библиотеки)
* [Cache API](https://developers.google.com/web/fundamentals/instant-and-offline/web-storage/cache-api) - позволяет хранить пары `Response/Request` запрошенных файлов для кэширования
* [Browser storage limits and eviction criteria](https://developer.mozilla.org/en-US/docs/Web/API/IndexedDB_API/Browser_storage_limits_and_eviction_criteria) - ограничения размера и др. встроенных в браузер хранилищ IndexedDB, Cache API, Cookies
* [Uint8Array](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Uint8Array) - используется для работы с бинарными файлами в js. Часто в паре с [ArrayBuffer](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/ArrayBuffer) и [Blob](https://developer.mozilla.org/en-US/docs/Web/API/Blob) для преобразования в др. тип. В **Blob** есть  методы `text(), arrayBuffer() и stream()` для чтения определённых типов данных
* [WebSocket](https://learn.javascript.ru/websockets)
* [Proxy](https://learn.javascript.ru/proxy) - можно ставить перехватчики на изменение/добавление/удаление свойств и создание объектов через new, и еще много чего
* [Custom Elements](https://learn.javascript.ru/webcomponent-core) - создание тегов наследованием стандартных, фреймворки типа Angular тоже могут создавать такие при компиляции и такие компоненты могут работать без фреймворков в браузерах или с другими фреймворками. Для старых браузеров нужно использовать полифил
* [insertAdjacentHTML()](https://developer.mozilla.org/en-US/docs/Web/API/Element/insertAdjacentHTML) - как **innerHTML** только для вставки тегов в текстовом виде `<div>bla</div>` с указанием позиции и работает быстрее чем **innerHTML**. Но `insertAdjacentElement()` и `insertAdjacentText()` не быстрее?
* [Element.getBoundingClientRect()](https://developer.mozilla.org/en-US/docs/Web/API/Element/getBoundingClientRect) - размер о положение элемента относительно viewport (страницы), можно использовать его и похоже функции для расположения элементов относительно других элементов по координатам
* [Viewport](https://developer.mozilla.org/en-US/docs/Glossary/viewport) - видимая область, она содержит `<html>` элемент
    * Viewport == visual viewport + layout viewport
    * visual viewport - кусок Viewport, который видим сейчас. Уменьшается при zoom и может быть меньше чем layout viewport
    * layout viewport - кусок Viewport, который видим сейчас
* [Intersection Observer API](https://developer.mozilla.org/en-US/docs/Web/API/Intersection_Observer_API) - вызывает callback когда какой-то элемент пересекает указанный или viewport. Это некоторая альтернатива **getBoundingClientRect()**
* [Visual Viewport API](https://developer.mozilla.org/en-US/docs/Web/API/Visual_Viewport_API) - 
* Координаты - бывают относительно viewport, window, `<html>` (он по высоте как страница). Функции c `offset` представляют позицию относительно них.
    * pageX/Y gives the coordinates relative to the <html> element in CSS pixels.
    * clientX/Y gives the coordinates relative to the viewport in CSS pixels.
    * screenX/Y gives the coordinates relative to the screen in device pixels.
* [Streams API](https://developer.mozilla.org/en-US/docs/Web/API/Streams_API)
* [URL](https://developer.mozilla.org/en-US/docs/Web/API/URL/URL) - объект представляет url, а его свойства это части ссылки (port, protocol, host, pathname), одно из его свойств это **URLSearchParams**: `new URL('https://developer.mozilla.org')`
* [URLSearchParams](https://developer.mozilla.org/en-US/docs/Web/API/URLSearchParams) - можно передать в него список параметров и работать потом с ним через `for...of` или как с `Map`
  ```js
  var searchParams = new URLSearchParams("q=URLUtils.searchParams&topic=api");
  searchParams.has("topic") === true; // true
  searchParams.get("topic") === "api"; // true
  searchParams.getAll("topic"); // ["api"]
  searchParams.append("topic", "webdev");
  searchParams.delete("topic");

  // берем host ссылки
  new URL('https://www.google.com').host
  ```
* **split, slice, splice, join, substr, substring, concat**
  * **split (separator, limit)** - split String
  * **slice(from, until)** - copy part of array or String, return new instance
    * `'abc'.slice(0, -1)` - remove last symbol
  * **splice(idx, length)** - remove elements from that array
  * **substr** - `"abc".substr(1,2) // returns "bc"`
    * `'abc'.substr(1)` - remove first symbol
  * **substring** - `"abc".substring(1,2) // returns "b"`
  * **join** - `[12, 23].join('') // 1223` - join array
  * **concat** - для String or Array:
    * `hello.concat('Кевин', ', удачного дня.')`
    * `array1.concat(array2)`
* [FormData](https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects) - можно класть данные как в Map для отправки через `XMLHttpRequest`
  ```js
  var formData = new FormData();
  formData.append("username", "Groucho");
  formData.append("userfile", fileInputElement.files[0]);
  var request = new XMLHttpRequest();
  request.open("POST", "http://foo.com/submitform.php");
  request.send(formData);
  ```
* [form.requestSubmit()](https://www.chromestatus.com/feature/6097749495775232) - инициирует программную отправку данных формы по аналогии с кликом на кнопку отправки данных. Функция может применяться при разработке собственных кнопок отправки формы, для которых вызова form.submit() недостаточно из-за того, что он не приводит к интерактивной проверке параметров, генерации события 'submit' и передаче привязанных к кнопке отправки данных;
* [String.prototype.localeCompare()](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/localeCompare) - сравнивает строки и возвращает число -1, 0, 1 для использования в функции сортировки, может сравнивать с учетом языка (locale), регистра символов и прочего
  * ```js
    var a = 'réservé'; // with accents, lowercase
    var b = 'RESERVE'; // no accents, uppercase

    console.log(a.localeCompare(b));
    // expected output: 1
    console.log(a.localeCompare(b, 'en', {sensitivity: 'base'}));
    // expected output: 0
    ```
* [Window.open](https://developer.mozilla.org/en-US/docs/Web/API/Window/open) - через функцию можно открывать новое окно или вкладку `var window = window.open(url, windowName, [windowFeatures]);` при этом родительское окно по ссылке будет иметь доступ к открытому (возможно с некоторыми ограничениями в зависимости от браузера и его функций безопасности)
* [HTMLIFrameElement](https://developer.mozilla.org/en-US/docs/Web/API/HTMLIFrameElement) - класс iframe, отличается от обычного окна в основном тем, что доступ к **document** и **window** нужно получать через свойства **contentDocument** и **contentWindow**
* [Media events](https://developer.mozilla.org/en-US/docs/Web/Guide/Events/Media_events) - список событий тега `<video>`
* [ResizeObserver](https://developer.mozilla.org/en-US/docs/Web/API/ResizeObserver) - API позволяющее отслеживать изменения размера элементов. Ключевое отличие нового API от **window.onresize** и **CSS Media Queries**, заключается в том, что можно определить факт изменения конкретного элемента на странице, а не всей видимой области, что позволяет отреагировать изменением только этого элемента без изменения всего видимого содержимого; 
* [Page Visibility API](https://developer.mozilla.org/en-US/docs/Web/API/Page_Visibility_API) - api для определения режимов браузера: полный экран, минимизировано ли окно etc

# Про Promise и очереди задач

Тут будет описание Promise, в том числе Promise.all(), Promise.race(), Promise.resolve(value), Promise.reject(), WindowOrWorkerGlobalScope.queueMicrotask()

# MediaStream
тут будет про MediaStream

# Мои заметки

## Сложение через битовые операции
```javascript
(function() {
	const i = 7;

	//bad (unexplicit code)
	console.log(-~i); //=> 8
	console.log(~-i); //=> 6

	//good (explicit code)
	console.log(i + 1); //=> 8
	console.log(i - 1); //=> 6

})();
```

## Деление/умножение через битовые операции
```javascript
(function() {

  //good
  console.log(4 / 2); //=> 2
  //bad
  console.log(4 >> 1); //=> 2

  //good
  console.log(4 * 2); //=> 8
  //bad
  console.log(4 << 1); //=> 8

})();
```

## Проверка на существование переменной
<sub>Если объект существует, то он приравнивается ```y```. Иначе y станет пустым объектом ```{}``` (основано на том, что если выражение перед ```||``` будет ```true```, то оно сразу возвращается как результат операции без проверки выражения после ```||```)  </sub>
```javascript
function (x) {
	let y = x || {}; // y == x если x определено, иначе y == {} (пустому объекту)
}
```

## Выполнение операции при условии выполнения другой операции
Если `f1()` вернет true (а в js это **не** `undefined`, `false`, `null`), то `f2()` выполнится.
```js
return f1() && f2() && f3();
```

# Подключение js
https://developer.mozilla.org/en-US/docs/Web/HTML/Element/script
<br>
https://www.internet-technologies.ru/articles/zhiznennyy-cikl-stranicy.html
<br>
По умолчанию подключаемый скирпт **sync,** можно установить **async.** Создаваемый тег `<script>` через `document.createElement()` по умолчанию **async.** (можно установить `async="false"`)
```html
<!-- HTML4 -->
<script type="text/javascript" src="javascript.js"></script>

<!-- HTML5 -->
<script src="javascript.js"></script>

<script type="module" src="main.mjs"></script>
<script nomodule src="fallback.js"></script>


<!--
Атрибуты async и defer в window onload JavaScript используются только для внешних скриптов.
  Они игнорируются, если нет подключения через src.

defer - после того как document готов (как с DOMContentLoaded), но выполнится перед DOMContentLoaded
и DOMContentLoaded будет отложен пока весь скрипт не загрузится и не выполнится
DOMContentLoaded не ждет подгрузки stylesheets, images, and subframes

module - если скрипт это ES2015 module, то нужно использовать этот атрибут (модули с import и export)

crossorigin - Этот атрибут определяет, используется ли CORS при загрузке
  такой же атрибут есть для <img>
  Источник: https://developer.mozilla.org/en-US/docs/Web/HTML/CORS_settings_attributes

  use-credentials - Перед загрузкой скрипта выполняется кросс-доменный запрос (Origin: HTTP header) с
    указанием параметров доступа (в виде - cookie, сертификата или пары логин/пароль). В ответе сервера
    должен присутствовать заголовок Access-Control-Allow-Origin: HTTP header, иначе, использование
    изображения в <canvas> ограничивается.
    Если данный атрибут не задан, то CORS при загрузке скрипта не используется (отсутствует заголовок
    Origin: HTTP header).

  anonymous - Перед загрузкой скрипта выполняется кросс-доменный запрос (Origin: HTTP header),
    при этом не передаются параметры доступа (такие как: cookie, сертификат X.509, логин/пароль для
    базовой аутентификации по HTTP). В ответе сервера должен присутствовать заголовок
    Access-Control-Allow-Origin: HTTP header.

integrity - хэш сумма js файла

nonce - как crsf token, но для скрипта, скрипт будет работать если токен совпал 
-->
<script src="javascript.js" async="true" defer="true" type="module"
  integrity="sha384-oqVuAfXRKap"
  crossorigin="anonymous"
  nonce="Xiojd98a8jd3s9kFiDi29Uijwdu"></script>

<link type="text/css" rel="stylesheet" href="style.css">
<script>
  // сработает после загрузки style.css
</script>
```
```js
// браузер загрузил все ресурсы (изображения, стили и т. д.);
window.addEventListener('load', (event) => {
    log.textContent = log.textContent + 'load\n';
});

//  readystatechange - событие запускается при изменении состояния
// document.readyState - содержит state
//
// «loading» — документ загружается;
// «interactive» — документ полностью считан;
// «complete» — документ полностью считан и все ресурсы (например, изображения) загружены.
document.addEventListener('readystatechange', (event) => {
    log.textContent = log.textContent + `readystate: ${document.readyState}\n`;
});

// браузер полностью загрузил HTML-код страницы и построил дерево DOM. Но внешние ресурсы, такие как изображения <img> и таблицы стилей, могут все еще загружаться;
document.addEventListener('DOMContentLoaded', (event) => {
    log.textContent = log.textContent + `DOMContentLoaded\n`;
});

// window.onload vs document.onload
// https://stackoverflow.com/a/7371558


// аналог из jquery
$( document ).ready(function() {
   console.log( "ready!" );
});
$(function() {
   console.log( "ready!" );
});
$(window).on("load", function() {
     // Executes when complete page is fully loaded, including
     // all frames, objects and images
     alert("Window is loaded");
});
```

**6) Функция sleep(), можно использовать для теста:**
```js
await (new Promise(resolve => setTimeout(resolve, 2000)));
```

# В JS на самом деле все выполняется в 1ном потоке, поэтому все отложенные операции типа таймеров выполнятся после обычных

```js
// вывод: 5 5 5 5 5
for (var i = 0; i < 5; i++) {
  setTimeout(function() { console.log(i); }, i * 1000 );
}
```

# fetch vs XMLHttpRequest

Можно XMLHttpRequest прервать `xhr.abort()`, fetch **пока** прервать нельзя (возможно в будущем будет можно).

```js
var xhr = new XMLHttpRequest();
var body = 'name=' + encodeURIComponent(name) +
  '&surname=' + encodeURIComponent(surname);
xhr.open("POST", '/submit', true);
xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
xhr.onreadystatechange = ...;
xhr.send(body);
```

Пересылка данных формы разделенные границами (boundary)
```html
<form action="/submit" method="POST" enctype="multipart/form-data">
  <input name="name" value="Виктор">
  <input name="surname" value="Цой">
</form>
```

# HOC - Hight Order Component

HOC - это функция или модуль в js (т.к. модуль это по сути функция), которая оборачивает (содержит) другую функцию (модуль). Она преобразовывает передаваемые внутрь вложенного в нее модуля данные или возвращаемые вложенным модулем данные. Похоже на паттерн Adapter или Proxy из GoF.

# Навигация в js (location, history)

```js
// 1. через document.location
// https://developer.mozilla.org/en-US/docs/Web/API/Location
/*
Можно получить куски адреса:
  Location.href - полная ссылка
  Location.protocol
  Location.host - имя домена + номер порта
  Location.hostname - имя домена без протокола
  Location.port
  Location.pathname - будь без домена
  Location.search - параметры запроса
  Location.hash - якорь
  Location.username
  Location.password
  Location.origin - домен
*/
location.reload(); // перезагрузить
location.reload(true); // без кэша
document.location.assign('https://developer.mozilla.org'); // redirect
document.location.replace('https://developer.mozilla.org'); // redirect без сохранения в History
window.location.href = "http://stackoverflow.com";
$(location).attr('href', 'http://stackoverflow.com'); // для jquery
window.navigate("page.html"); // Same as window.location="url"

// 2. через history нельзя делать делать redirect, только менять историю и текущий адрес в строке

// history.pushState() и history.replaceState() не приводят к вызову этого события. Только нажатие кнопок вперед/назад в браузере, либо вызов history.back()
window.addEventListener('popstate', function(e) { });

window.history.replaceState(data, title, [, url]);
window.history.pushState({param: 'Value'}, '', 'myurl.html');
history.pushState(null, null, 'hello'); // можно использовать null
history.pushState(null, null, 'https://twitter.com/hello'); // SOP не позволит поменять полный адрес, это против мошенничества
history.back();
history.forward();
history.go(-1);
```

# Кратко о событиях
<br>
https://learn.javascript.ru/event-bubbling

* Две стадии - стадия захвата и всплытия.
  * **capturing stage** (Захват): window -> document -> html -> body -> ... -> к элементы в event.target (целевому)
  * **target stage:** 
  * **bubbling stage** (всплытия): захват с проверкой ВСЕХ родителей есть ли в них это событие, тогда выполнится

* `addEventListener('click', document, true)` - true делает обработку события а стадии захвата

* `event.cancelable` - true/false, отменяемое событие или нет
* `event.eventPhase` - фаза: 1, 2, 3

**Остановка:**
* `stopPropagation` препятствует продвижению события дальше, но на текущем элементе **все** обработчики отработают.
* `event.stopImmediatePropagation()` - чтобы полностью остановить обработку, Он не только предотвращает всплытие, но и останавливает обработку событий на текущем элементе.

**Элементы:**
* `event.target` – это исходный элемент, на котором произошло событие, в процессе всплытия он неизменен.
* `this` (= `event.currentTarget`): – это текущий элемент, до которого дошло всплытие, на нём сейчас выполняется обработчик.

# documentFragment - lightweight document
**documentFragment** - пустой элемент, чтобы например добавить туда в цикле элементы, а потом добавить сам этот элемент за один раз через `body.appendChild(dF);`
```js
fragment = document.createDocumentFragment();
document.body.appendChild(fragment);
```

# reflow vs repaint

* **reflow** - _лучше,_ происходит изменение видимости без изменения layout, например для: `outline, visibility, background, color`
* **repaint** - _хуже,_ ресурсоемкий, изменение layout всей страницы или ее куска, например для: `width, height, font-family, font-size`

# Типы node (node types)

Типы: element, text, comment, document, document_type, document_fragment, Attr, CDATASection

# CDATA - character data

Это текстовые данные в XML. Не нужно экранировать `<`, `&` etc.
```
<![CDATA[Within this Character Data block I can]]>
```

# Entity символы
Entity символы (напр. в CSS) - те которые нужно экранировать

`«<»` и `«&»` будут представлены как `«&lt;»` и `«&amp;»`

# Разные теги html

`<dl>, <dt>, <dd>`

# Реализация bind через apply
Реализация bind через apply - тупой вопрос для собеседования, но может встретиться
```js
if (!Function.prototype.bind) {
  Function.prototype.bind = function (context /* ...args */) {
    var fn = this;
    var args = Array.prototype.slice.call(arguments, 1);

    if (typeof(fn) !== 'function') {
      throw new TypeError('Function.prototype.bind - context must be a valid function');
    }

    return function () {
      return fn.apply(context, args.concat(Array.prototype.slice.call(arguments)));
    };
  };
}
```

# Техника использования сортировки [].sort()
Техника использования сортировки [].sort() - разница это значение которое `0, >0, <0` (меньше, больше, равно)
```js
return questions.sort((a, b) => a.order - b.order);
```


# attribute vs property в js
Чаще синхронизация – односторонняя: **свойство зависит от атрибута, но не наоборот**.

как понял я: брать значение лучше по property, а сетать по attribute,
но иногда для универсальности лучше использовать что-то одно из этого, т.к. имена property и attribute могут не совпадать

При этом CSS стили нужно менять специальными функциями
```js
node.style.setProperty(name, value, 'important'); // только так inline стилю можно установить important
node.style.getPropertyValue(name);
```

Источники:
https://lucybain.com/blog/2014/attribute-vs-property/
https://learn.javascript.ru/attributes-and-custom-properties

**23) в Stream API создается новый массив (т.е. перебор массива в Stream API оператора это fail safe операция)**
```js
let arr = [1, 2];
arr.forEach(e => {console.log(e); arr = null;}); // 1, 2 (т.е. будет работать хотя arr == null)
```
**24) техника использования reduce в качестве map + filter**  
Инициализуем пустым array который на каждой итерации будет наполняться фильтрованными по условию значениями и вернется в конце работы reduce
```js
const filtredArray = arr.reduce((acc, el) => {
  if(el.isSomeCondition) {
    acc.push(el);
  }

  return acc;
}, []); // инициализуем пустым array
```
**25) длинна массива в js в цикле for(;;) проверяется каждую итерацию, поэтому для микрооптимизации нужно сохранять ее в переменную** [источник](https://stackoverflow.com/a/8452333)
```js
for (A; B; C)

A - Executed before the enumeration
B - condition to test
C - expression after each enumeration (so, not if B evaluated to false)

// два эквивалента
// 1.
for (var i=0; i<array.length; i++) { ... }
// 2.
var i = 0; while (i < array.length) { i++; }

// оптимизированный вариант хранит переменную внутри блока вызывающегося 1ин раз
for (var i = 0, len = items.length; i < len; i++) {}
```

1)  в js часто может понадобится техника запуска скрипта вроде
```js
// в js часто может понадобится техника запуска скрипта вроде
// Примеры: https://stackoverflow.com/questions/39993676/code-inside-domcontentloaded-event-not-working
'use strict';
var dclhandler = false;
if (document.readyState !== 'loading') {
  start();
} else {
  dclhandler = true;
  document.addEventListener('DOMContentLoaded', start);
}
function start() {
  if (dclhandler) { document.removeEventListener('DOMContentLoaded', start); }
  console.log('Start the site`s JS activities');
}
```

28)
```js
// в js reduce = map + filter только для аникальных элементов
const attributeFilter = this.cfg.mutators.reduce((attributeFilter, mutator) => {
    mutator.attrs.forEach(({name}) => {
        if (attributeFilter.indexOf(name) < 0) {
            attributeFilter.push(name);
        }
    });

    return attributeFilter;
}, []);
```

29) в js проверка существует ли свойство в объекте
```js
'loading' in HTMLImageElement.prototype; // true / false
```

30)
```js
// В js добавляемый тег <script> создаваемый через сам js по умолчанию async, т.е. другие скрипты не ждут пока он загрузится.
// Можно поставить ему script.async = false чтобы сделать sync
```

31)
```js
// IIFE (Immediately Invoked Function Expression) - это функция которая вызывается сразу в месте ее объявления
(function () {
    statements
})();

// async вариант
(async () => {

})();
```

32) Способ создания (вызова) своих событий
```js
// добавить dispatchEvent и CustomEvent в описание событий js
// способ trigger события в js
const mouseoverEvent = new Event('mouseover');
whateverElement.dispatchEvent(mouseoverEvent);
```

33) в js чтобы использовать `<input type=file>` нужно событие change, а не click

# callstack в js

Статья про все это в js [тут](https://habr.com/ru/company/ruvds/blog/340508/)

**Кратко о Event Loop в js:** (по порядку)
1. Сперва выполняется текущий синхронный код (callstack),
2. далее очередь микрозадач (Promise)
3. если нет другого синхронного кода - очередь макрозадач (готовый для исполнения код, обернутый функциями setTimeout() и setInterval() или AJAX-запросы)
4. Также отдельно имеется очередь для задач, которые должны выполниться сразу перед следующим циклом перерисовки контента.

**Promise** события как бы прикрепляются сами после ближайшего обычного события в callstack, поэтому они выполняются почти сразу.

**Web Workers** - настоящая многопоточность в js, они именно отдельный потоки со своим callstack и код может быть в отдельном файле, обмен данными между потоками через сообщения

# mixin в js
например DocumentOrShadowRoot

# Важные js библиотеки, движки, frameworks
* **Движки js**
  * **QuickJS** - быстрый js движок от создателя QEMU, умеет компилировать в native исполняемые файлы, без DOM
  * **Hermes** - быстрый движок от facebook, оптимизированный для запуска **React Native**
* **Frameworks**
  * Angular 2+
  * React - на самом деле это библиотека, но ее чаще всего используют с redux, redux-logic, redux-observables, mobx и получается framework; Последние версии React поддерживают hooks - замену HOC, что делает react пригодным для использования без библиотек.
  * React Native - для разработки кросс плотформенных приложений, не использует DOM, исползует средства системы, по сути реализация native приложения вместо использования движка js (на самом деле используется встроенный свой движок)
* WebTorrent - торрент клиент на js который можно встраивать на страницы или в приложения на js
* **template engines** - шаблонихаторы, в настоящее время шаблонизаторы используются реже, вместо них используют Virtual DOM или другие системы детекции (ту что в Angular например)
  * [Nunjucks](https://mozilla.github.io/nunjucks/) - от Mozilla
  * [Dust.js](https://www.dustjs.com/) - от LinkedIn, считается одним из самых быстрых
* [multi.js]() - продвинутый вариант `<select>`
* [FileSaver.js]() - можно сохранять сгенерированные внутри браузера файлы на диск, показывает окошко сохранения и имеет некоторый контроль над процессом сохранения, но из-за ограничений браузера контроль не полный и нельзя сделать нормальный revoke ссылки на файл (по крайней мере пока что)
* [StreamSaver.js](https://github.com/jimmywarting/StreamSaver.js) - как **FileSaver.js**, но работает асинхронно и дает сохранять в виртуальную файловую систему (не в chromes sandboxed file system!)
* [hyperHTML](https://github.com/WebReflection/hyperHTML) - интересный проект, пока я считаю его сырым, это легкая альтернатива Virtual DOM (т.е. react и прочим рисовальщикам страниц)
* [jQuery](https://jquery.com/) - популярный плагин для работы с DOM плюс набор дополнительных функциями
* [jQuery UI](https://jqueryui.com/) - дополнение к jQuery для построения UI и работы с его событиями, например с анимациями
* [jQuery Mask Plugin](https://igorescobar.github.io/jQuery-Mask-Plugin/) - плагин на jQuery для масок полей ввода
* [jQuery Validation Plugin](https://jqueryvalidation.org/) - плагин для валидации полей ввода
* [winston](https://github.com/winstonjs/winston) - logger для js
* **системы сборки и оптимизации** - основная проблема современных компилятор и оптимизаторов js в том, что они перекрывают возможности друг друга и не всегда понятно что есть что. Например **webpack.js** это больше **компилятор и оптимизатор** к которому можно подключать плагины, а **gulp.js** это больше **система сборки** к которой можно подключить **webpack.js**
  * [https://webpack.js.org/]() - собирает файлы в 1ин или несколько js файлов, оптимизирует, может извлекать их них или встраивать в них html, css, другие ресурсы, может подключать локлаьный веб сервер для быстрой авто перезагрузки страницы и авто rebuild при любом изменении файла, подключать компилятор TypeScript и другие и еще много других функций.
  * [rollup.js](https://rollupjs.org/guide/en) - система сборки похожая на webpack
  * [gulp.js](https://gulpjs.com/) - система сборки, в отличии от **webpack.js** можно писать гибкие этапы компиляции, использует **webpack.js** как компилятор и оптимизатор
  * [Babel](https://babeljs.io/) - компилятор который можно подключать к системам сборки (webpack, gulp). Компилирует новые версии js, react и других языков (над множеств js) в обычный js (в указанную версию) с добавлением полифилов на js если нужно.
* [lodash](https://lodash.com) - много готовых функций для работы с arrays и objects, как RxJs основанных на теории кортежей (подобно SQL); кроме этого много и других функций, например для **deep clone** объектов в js. Сами функции можно импортировать по отдельности от всей библиотеки.
* [zone.js](https://www.npmjs.com/package/zone.js) - библиотека, часть Angular, добавляет обертки вокруг async функций и позволяет отслеживать их и результат их работы, т.к. в стандартном js для этого механизмов нету (async, setTimeout и прочее)
* **JS библиотеки для графики**
  * [D3.js](https://d3js.org/) - визуализация разным структур данных: графики, графы, деревья и прочее
  * [Chart.js](https://www.chartjs.org/) - построение графиков
  
# Раздел про CSS и его некоторые особенности которые нужно запомнить

Добавлено CSS-свойство "white-space:break-spaces", определяющее, что любая последовательность пробелов, приводящая к переполнению строки, должна быть разорвана; 

# strict mode

* Коротко:
  * Если есть class, то он включен по умолчанию
  * вместо ничего будут Exception
  * Меняет поведение на предсказуемое
  * дает браузеру лучше оптимизировать работу

# новое на очереди в добавление
https://habr.com/ru/company/ruvds/blog/353174/

# Tuples (кортежи) в js (они же используются как immutabable структуры данных)
Кортежи (Tuples) также, как и массивы, представляют набор элементов, для которых уже заранее известен тип. На практике он похож на Enum, но тип переменных может быть разный и он имеет фиксированный размер в отличие от array.

Note. В TypeScript есть тип Tuples.  
Note. Хотя пример ниже с виду нормальный, возможно его реализация не до конца верна и тут он как просто как заметка что такое может быть.
```js
const tuple = (...args) => Object.freeze(args);
const tup = tuple ( 1, 2, 3, 4 );
tup[0] = 13; // ничего не произойдет
```

# Неочевидные особенности JS с которыми можно столкнуться
## Выпадение переменных в ES5
Если в ES5 использовать var внутри блока, то она выпадет во внешний блок, операторы let и const устраняют это поведение в ES5+, в старой версии js чтобы такого не было можно использовать модули **IIFE**.
```js
// ES5
{
  var private = 1;
}

console.log(private); // 1
```

# Деструкция
**Переименование.** Возьмет до двоеточие объекта свойство по имени и переименует его в то имя, что после двуеточие.
```js
// rename
const o1 = {
  a : 'b',
  c: {
    d: 'e'
  }
};
const {a: a2} = o1;
console.log(a2); // 'b'
```

**Получить вложенные (sub property) свойства.**
```js
// sub property
const o1 = {
  a : 'b',
  c: {
    d: 'e'
  }
};
const {c: {d}} = o1;
console.log(d); // 'e'
```

# Операторы spread и rest и работа с ними

**spread** и **rest** - выглядят как три точки.
* `spread` - Оператор `spread` используется в **правой** части выражения со знаком присваивания. Позволяет **извлекать** свойства объекта, которые пока из него не извлечены, можно использовать для удаления свойств создавая через него копию обьекта, но уже без ненужных свойств.
* `rest` - Оператор `rest` используется в **левой** части выражения. Используется для **создания** новых объектов.
```js
// Оператор rest. Извлечение из объекта только необходимых свойств
// все свойства не попавшие в явно указанные переменные попадут в k
// при этом k не нужно обьявлять заранее где-то еще
let {i, j, ...k} = {i: 1, j: 2, m: 3, h: 4}; // i == 1, j == 2, k == {m:3, h: 4}

// Оператор rest. Удаление ненужных свойств объектов (через КЛОНИРОВАНИЕ обьекта)
// создадим обьект m без свойства g
let {g, ...m} = {g: 3, k: 5, d: 34}; // получим m == {k:5, d:34} при этом свойство g в клонированном не будет (будет удалено)

// Оператор spread.
// Извлекает свойства из обьектов, работает как flatMap(1)
let obj = {i:1, y:3, ...{t: 3, h: 5}}; // obj == { i: 1, y: 3, t: 3, h: 5 }

// Оператор spread.
// Это аналог Object.assign
// содержимое obj1 как у obj2
const data = {r: 3};
const obj1 = Object.assign({}, data); // {r: 3}
const obj2 = { ...data }; // {r: 3}

// Оператор spread.
// Действие с массивом.
const arr1 = [1, 2, 3];
const arr2 = [...arr1, 4, 5, 6];

// Оператор spread.
// Передача array как отдельных параметров.
const arr1 = [1, 2, 3];
f1(...arr1); // == f1(1, 2, 3);

// Оператор rest. 
// Называется rest parameter. Прием параметров в функции в одну переменную.
const f1 = (...args) => {return args[2];};
f1(1, 2, 3); // == return 3;

// Оператор spread.
// Можно использовать вместо Array.from чтобы превратить коллекции обьектов таких как массив DOM элементов в обычный массив,
// чтобы с ними можно было использовать Stream API. Но это медленнее.
const arr = [1, 2];
const arr1 = [...arr]; // == [1, 2]
const arr2 = Array.from(arr); // == [1, 2]
```

# Tagged Template (Тегированные шаблонные строки)
Это функция **парсер** которому передаются Template Literals (строки в обратных кавычках). Строка после вызова функции передается как параметр. `strings` - список строк разделенных местами вставки значений, `keys` - список переданных для вставки значений. Предполагается, что разработчик сам склеит строки и сделает **return** результата.
```js
function template(strings, ...keys) {
}
template`asdasdasdasd    ${0}${1}${0}`;
// strings == Array(4) [ "asdasdasdasd    ", "", "", "" ]
// keys == Array(3) [ 0, 1, 0 ]

// Можно использовать String.raw внутри Tagged Template чтобы получить оригинальную переданную строку
function tag(strings) {
  console.log(strings.raw[0]);
}
tag`string text line 1 \n string text line 2`; // string text line 1 \n string text line 2
```

[String.raw](https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/String/raw) - пред определенный Tagged Template
```js
String.raw`Hi\n${2+3}!`; // // "Hi\n5!"
String.raw({ raw: 'test' }, 0, 1, 2); // 't0e1s2t'
```