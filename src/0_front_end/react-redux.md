# ссылки

* https://devhints.io/react - короткий визуальный synopsis
* https://reactjs.org/docs/hooks-intro.html - хуки

## redux

redux - библиотека реализующая однонаправленный поток данных

состоит из: state tree, reducer function
Actions - вспомогательне объекты, которые определяют как будет обновлено состояние при следующей итерации.

combineReducers - обычно используется для комбинации reducers в один, называют rootReducer

Reducer Decomposition - это разделение state tree на кусочки 

Когда action is dispatched, то  root reducer передает ТОТ ЖЕ action object в all reducers чтобы (они) реагировли независимо. Вместо того чтобы передавать в reducers целое state tree туда передается только нужный кусок (для каждого reducer).

Правильный подход: разделение state tree на маленькие кусочки, тогда приложенка имеет нормальную не сложную архитектуру.

reducers - просто функции, мы можем туда передать ЛЮБОЕ количество параметров 3им аргументом.
  bar(state, action, state.foo)

Варианты работы с reducers:
  1. класть больше данных в action
  2. использовать vertical hierarchy, где reducers более плотно (chain) связаны, т.е. как я понял данные передаются по цепочки. НО! Это увеличивает сложность приложения.

Паттерны работы reducers в общем:
1. каждый reducer получает свой кусок state как 1ый аргумент
2. каждый reducer получает ТОТ ЖЕ action как второй аргумент
3. зависимые reducers получают complete state (state после обработки?) или куски state других reducers как результат (результат обработки в других reducers?)
4. все обновленные state делают merge в single state tree (общий state приложенки)
5. Dependent reducers могут формировать invocation chain (т.е. вызовы reducers проходят по цепочке)
6. Redux uses reducers to perform state manipulations

action & state:
1. action creator это function создающая action object
2. dispatch function получает action object и передает его в store’s root reducer
3. store - это место где живут combined state tree и dispatch function
4. Каждое redux store имеет свой root reducer, который потребляет action object и делает update internal state этого store (обновляет свое состояние)

## docker

docker:
  образ - образ разных систем
  контейнер - (mysql, nodejs), использует ядро хост системы
dockerfile - в нем правила создания образа

### bindActionCreators

**Коротко:** в основном для проброса ownProps компонента в actions. И можно обернуть в dispatch только часть actions, а остальные оставить обычными функциями.

В таком виде можно передать в **bindActionCreators** много **AC** как есть и отдельно передать дополнительные **AC** обернутые в **dispatch**, которые будут использовать **ownProps** или функции которые не надо оборачивать в **dispatch**

```js
const mapDispatchToProps = (dispatch, ownProps) => ({
  ...bindActionCreators(
    {
      ...productsActions,
      ...usersActions,
      ...ordersActions,
    },
    dispatch,
  ),
  () => getSomeOther(ownProps.someProp),
  notNeedsToWrapInDispatch,
})
```
### Начало

1. react
2. redux
3. express
4. сборка react через webpack или starter пакет
5. подключение сторонних модулей к react
6. посмотреть анимации для компонентов
7. посмотреть thunk и sage

css modules настроить отдельно https://blog.pusher.com/css-modules-react/

materialize css - материал набор стилей

https://www.youtube.com/watch?v=-ZB8I2PmiOw&index=7&list=PL4cUxeGkcC9ij8CfkAY2RAGb-tmkNwQHG

259 мин от 7го урока

5 часов

firebase 20 - остановка

import React, {Component} from 'react';
setState({name: 'bla'}) - хорошая практика, менять массив state фциями типа push - плохая
onSubmit() - enter + click the button
onCopy() - copy txt
e.preventDefault() - для prevent submit
npx - установить react начальное приложение
import 'App.css'
className="App"

setState(obj, callbackFn) - callbackFn выполняется после установки state

setState((prevState, props) => {})

import logo from './logo.svg'
<img src={logo} />

<Bla name="bla" age="13">
this.props // выведет name и age переданные внутрь компонента

вывод группы элементов
```jsx
render() {
list = arr.map(el => {
    return (
        <div key={el.id}> // желательно использовать id
            <div>el.name<div>
            <div>el.val<div>
        </div>
    )
})
return(<div>{list}</div>)
}
```

forceUpdate - форсированная перерисовка, например для написания библиотек

container vs ui components (Smart vs Dumb React компоненты?)

container components (class based components, statefull):
1. contain state (data sources)
2. serve live cycle
3. not contain UI
4. use classes to create 

UI components (functional components, stateless components, dump components)
1. don't contain state
2. recieve data from props
3. contains only UI
4. use function to create (functional components)

container component содержит UI components или другие container components. container имеет метод render, UI components просто возвращают jsx

в UI component передается props как параметр:
```jsx
const MyComp = props => {
    return (<div></div>);
}
```
или
```jsx
const MyComp = {ninjas, age} => {
    return (<div></div>);
}
```

Также props можно получить через this:
this.props

conditional output:
```jsx
if(bla) {
    return (<div>111</div>)
} else return (<div>222</div>)

return bla ? 111 : null;
```

<label htmlFor> - for="" в jsx

**Техника обновления инпута по id поля чтобы каждый раз не писать имена полей:**
```jsx
<input type="text" onChange={this.handleChange} id="age">
handleChange(e) {
    this.setState({
        [e.target.id]: e.target.value
    })
}
```

для формы можно использовать:
    { text: e.currentTarget.value }

в props можно передавать ссылку на функцию. При этом функция получает ссылку на this.state дочернего компонента и использует этот state в родительском чтобы обновить его
```jsx
handleSubmit(e){
  this.props.AddNinja(this.state);
}
```

Техника:
1. делаем копию массива state, добавляем туда новый элемент. обновляем state
```jsx
state = [{age: 1}, {age: 2}, {age: 3}]
this.setState({
    ninjas: [...this.state.ninjas, {age: 4}]
})
```
2. При удалении пересоздаем массив без удаляемого элемента (filter())

# lifecycle

1. **mounting**
   1. **constructor**
   2. **getDerivedStateFromProps** - тригер при первом рендеринге, а потом при каждом получении новых props изменены (уже на шаге update). Когда метод вызван в параметрах получаем props и currentState компонента. И можно сравнить currentState и полученные новые props. Можно вернуть новые state объект или null, если нет изменений. Этот метод используется редко.
   3. **render**
   4. **componentDidMount** - место чтобы получить внешние данные из БД
2. **updating**
   1. **getDerivedStateFromProps**
   2. **shouldComponentUpdate(nextProps, nextState, nextContext)** - получает в параметрах newProps и newStates и можно **сравнить** oldProps с newProps и currentState с newState. Можно вернуть false если нужно предотвратить update компонента и rerendering.
      1. Например можно вернуть true только если newProps и oldProps разные (только тогда нам нужен update и rerender). Альтернатива этому: использовать **poor** компоненты в react
   3. **render** - в нем react готовит код для редеринга, это единственный **обязательный** метод в компоненте
   4. **getSnapshotBeforeUpdate** - получаем доступ к записи в DOM до того как изменения будут внесены в DOM (напр. получить текущую позицию window). Вернутое этим методом значение будет передано в последний шаг (hook) процесса update и там мы его можем использовать, т.е. значение передастся в 
   5. **componentDidUpdate(prevProps, prevState, prevContext)** - вызвано после render шаблона в DOM (НО только при update, ни при первой перересовке!). Тут имеем доступ к DOM. Это хорошее место для получения данных из внешней БД, если надо. Если апдейтить state в этом хуке можно получить бесконечный update
3. **unmounting**
   1. componentWillUnmount
4. **Error Handling** - вызывается когда ошибка в рендеринге, lifecycle методе, или конструкторе дочернего component
   1. **getDerivedStateFromError(error)** - вызывается во _время render_ после того как error выброшено дочерним компонетом, возвращает **state** (на основе которого можно принять решение что показать в родителе)
   2. **componentDidCatch(error, info)** - вызывается во время **“commit” phase**, info - инфа о компоненте который выбросил ошибку

```js
// как использовать, меням состояние и на его основе отображаем что-то
// в компоненте обертке <ErrorBoundary>
componentDidCatch (error, info) {
  this.setState({ error })
}
```

**Суть:** Делаем container component у которого есть state. В него может быть вложено много container component и UI component. UI component не хранит state и только отображает что-то в том числе кнопку с переброшенной в него функцией из container component. UI component просто возвращает jsx.
Формы содержится в container component, объект state в ней содержит как минимум state для каждого input (т.е. не смотря на то что этот компонент больше служит для отображения ему нужен state для полей формы и поэтому он container, а не UI). Если нужно добавить новый item в список, то в функцию add(state) передается state (один элемент массива) из UI компонента во внешний и сам этот state добавляется в state в родительском container компоненте.

Пример передачи функции в дочерний компонент:
    <Add onAddNews={this.handleAddNews} />
вытаскиваем в дочернем:
    this.props.onAddNews(передаем что-то)

React.Fragment - аналог обертки из Angular

**PropTypes библиоткека (можно использовать TypeScript вместо этого?):**
Помечаем свойство компонента как обязательное, react выдаст ошибку если его не будет. Работает только для dev режима (не в проде). Подключать ее отдельно.
News.propTypes = {
  data: PropTypes.array.isRequired // PropTypes (с большой буквы) = библиотека prop-types
}
Список свойств: https://maxfarseer.gitbooks.io/react-course-ru-v2/content/prop-types.html

defaultValue или readOnly в input есть


e.currentTarget vs e.target
  e.target - источник события, которое может "всплыть" до родителя e.currentTarget
  e.currentTarget - элемент к которому на самом деле привязано событие

**Uncontrolled Components** - нету обработчика изменений (handler функций), значит нет вызова setState. Для них можно указать defaultValue при загрузке.

**Пример Uncontrolled Components:**
1. Создаем ссылку в constructor()
    this.input = React.createRef()
2. <input defaultValue='' ref={this.input} />
3. Исползуем
    onBtnClickHandler = () => {
        alert(this.input.current.value)
    },
4. **Как я понял суть:** не нужно постоянно обновлять значение через state по onChange()

В ref можно передавать callbackFn и это считается более гибки:
```jsx
(<input ref={input => ({console.log(input)})></input>)
```

Еще вариант с ref:
```jsx
<input type="text" ref="someThing" />
console.log(this.refs.someThing.value)
```

**техника показа loader:**
перед запросом ставить
    this.setState({ isLoading: true })
после запроса ставим
    this.setState({ isLoading: false })

## routing

react-router-dom

1. Использование:
   1. import {BrowserRouter, Route} from 'react-router-dom'
2. 
   1. <BrowserRouter history={history}> {/* задаем историю? */}
      1. <div>
         1. <Navbar />
            1. <Route path="/" exact component={Home}>
            2. <Route path="/about" component={About}>
            3. <Route path="*" component={NotFound} />
      2. </div>
   2. </BrowserRouter>

использование вместо <a> тега <Link>
1. `import {Link, NavLink} from 'react-router-dom`
2. `<Link to="/">`
3. `<Link to="/home">`
4. NavLink как Link только добавляет css класс active к <a>
   1. `<NavLink to="/home">`

Через кнопку:
`<Button name="Go to About page" link="/about" />`

**програмный redirect**

<Rout> при редиректе добавляет компоненту к которому переправляет доп. инфу в props:

// эти переменные из HOC компонента Route
{history, // length, action(PUSH, REPLACE or POP), location, createHref
location, // pathname, search, hash, state, key
match}    // path (/my/:param), url(/my/param), isExact, params

// 1. match - используется в Link и Route для относительных путей
```jsx
<Link to={`${match.url}/index`}>
```

// 2. location - не изменяется поэтому его используют в lifecycle, data fetching, DOM side effects, warn user, update state etc
// другими словами: использовать в if и switch-case для работы в зависимости от ссылки

// 3. history
// length - количество переходов в истории
// history.push(obj) == <Link to="obj"> - можно передавать объект, например {state, pathname}
// history.replace == <Redirect />
// push(path, [state]), replace(path, [state])
// go(n) - по номеру entry в history
// goBack() == go(-1), goForward() == go(1)
// block(prompt) - prevents navigation

Прим. history.location это location, но это не надо использовать, т.к. он mutable

**Редирект програмный:** this.props.history.push('/about')
Чтоб при редиректе в props попадали все эти объекты для роутинга (т.е. **чтобы роутер работал нужно**: добавить "обертку" к дочернем компоненту, в котором нужно работать с этими навигациями из props и **в котором** нужно выполнить редирект):
```js
import {withRouter} from 'react-router-dom';
const Navbar = props => {}; // теперь тут будет работать this.props.history.push('/about'). И В props будут обхекты навигации не только при первом переходе, а всегда
export withRouter(Navbar);
```

redirect в jsx:
if(!auth.id) return <Redirect to='/singin' />

withRouter это higher Order Component

**<Switch>** - останавливает роутинг если найден хотя бы 1 тег с подходящим путем:
```jsx
import {Switch} from 'react-router-dom';
<Switch>
<Rout path="/" exact component={Home}>
<Rout path="/about" component={About}>
</Switch>
```
Без него **на одну** страницу вывелись бы 2 компонента, потому что / и /about подошли бы под путь /

**Параметры в routers:**
1. <Rout path="/:post_id" component={About}>
2. this.props.match.params.post_id

**параметры url:**
<Route name="search" path="?status=:status&page=:page&limit=:limit" handler={SearchDisplay}/>
this.props.location.search
const params = new URLSearchParams(paramsString); 
const tags = params.get('tags');`

**Пример lazy импорта вручную:** (есть пакет react-loadable, который облегчает это, возможно он устарел)
<br>
```js
class MyComponent extends React.Component {
  state = {
    AnotherComponent: null
  };

  componentWillMount() {
    import('./another-component').then(AnotherComponent => {
      this.setState({ AnotherComponent });
    });
  }
  
  render() {
    let {AnotherComponent} = this.state;
    if (!AnotherComponent) {
      return <div>Loading...</div>;
    } else {
      return <AnotherComponent/>;
    };
  }
}
```

**Возможно более новый способ lazy loading**:
<br>
```js
import React, { lazy, Suspense } from 'react'
import { MemoryRouter, Route, Switch } from "react-router-dom";
// import Post from "./post";
const Post = lazy(() => import("./post"));

// если коротко:
<Suspense fallback={<LoadingMessage />}>
    <TestComponent />
</Suspense>

// усложненный вариант с HOC:
function App() {
  return (
    <Router>
      <div className="App">
        <Switch>
          <MemoryRouter path="/" exact component={Home} />
          <MemoryRouter path="/:id" component={WaitingComponent(Post)} />
        </Switch>
      </div>
    </Router>
  );
}

// HOC обертка
function WaitingComponent(Component) {
  return props => (
    <Suspense fallback={<div>Loading...</div>}>
      <Component {...props} />
    </Suspense>
  );
}
```

**<MemoryRouter>** - <Router> хранит history адресов в памяти, не показывает в строке адреса. Для теста и non-browser environments

**react-router-config** - доп. конфиги для routing

## Higher Order Components

(HOC) - это компонент который делает Superchager Component из лообычного (добпавляет обычному компоненту доп. свойства). Т.е. это по сути функция, которая оборачивает компонент и меняет его свойства путем передачи в обернутый компонент параметров, которые этот компонент потом использует. Это похоже на модуль в js, который оборачивает другой модуль.

В таком компоненте мы возвращаем **функцию, которая ВОЗВРАЩАЕТ JSX**.

Пример HOC логирование: export default logProps(FancyButton);

```jsx
// HOC, обертка
const WrapperHOC = WrappedComponent => {
    const myCls = 'bla-bla';
    return props => {
        return (
            <div className="{myCls}">
            <WrappedComponent {...props} />
            </div>
        );
    }
}
export default WrapperHOC;

// использование HOC (теперь About будет ИЗМЕНЕН перед использованием)
export default WrapperHOC(About);
```

## Redux

Это глобальное store, чтобы не использовать state внутри компонентов. Этот store доступен всем компонентам сразу. На него можно подписываться. В него можно отправлять сообщения. reducers - это функции через которые проходят все сообщения к store. reducers читают actions и в зависимости от действия и содержащегося в них состояния (данных) меняют store.

Чтобы не вызывать вручную .subscribe() и dispatch() используется функция connect(), которая принимает ссылки на функции которые возвращают **другие функции, объекты которых попадают в props из state или меняют сам state** компонента

connect() - обертка для компонента (HOC)

как то так:
export default connect(mapProps, mapDispatch)(Navbar);

Практическая инфа в курсе про firebase начиная с 11 урока, и см. 14 урок

thunk для асинхронных запросов подключается через applyMiddleware. Вообще можно подключать много разных middleware со стороны и кроме thunk.

redux-thunk - одно преимущество явное - не нужно импортировать store в каждом файле.
Это по сравнению с:
  ```js
  export function load(){
      setTimeout(()=>{
          store.dispatch(getProfileSuccess({id: 1}));
      }, 3000)
  }
  ```

Пример thunk и сравнение с обычным кодом на promise (очень полезно для понимания архитектуры и зачем нужен thunk и альтернативы)
  https://stackoverflow.com/a/34599594

Через compose() можно объелинить несколько store. Например сторонних

Через compose() можно объединить и несколько **connect()**

обычно используют **actions** чтобы создать функции action работающие в reducers. Они принимают параметры (dispatch, getState, {bla, bla2})
bla объекты это те которые переданы из middlware через передачу параметров (напр. от firebase можно доп. объекты передавать)

**react-saga** - библиотека для работы транзакционным способом, фиксация действий или откат. Подход из мира destributed transaction, основан на генераторах из js. В генераторах выполнение ункции делается как работа с iterator.

**side effect** - так иногда называют асинхронные действия???

**this.props.children** - содержит массив node переданных внутрь component, в том числе если они не component, а простые теги:
      <Example> <!-- div теги - переданные в компонент Example ноды -->
        <div>1</div>
        <div>2</div>
        <div>3</div>
      </Example>

**ReactDOM.createPortal**
Вот это вставляет children в указанный вторым параметром DOM Node (в Node вне иерархии):
  return ReactDOM.createPortal(
    this.props.children,
    domNode
  );
Это альтернативая для:
    <div>
      {this.props.children}
    </div>
  которая может вставить только внутрь текущего component, но не любого.


  **React.createContext** - создает глобальный для обернутых в него компонентов контекст **(Provider).** Т.е. это глобальные переменные, напр. текущий пользователь (аутентификация).
  ```js
    const ThemeContext = React.createContext('light');
            <ThemeContext.Provider value="dark">
            <Toolbar />
          </ThemeContext.Provider>
  ```
Доступ к глобальному контексту через props: this.props.myGlobalVar


**React.forwardRef** - для передачи (forward) полученно ссылки ref на элемент дальше, в дочерние компоненты. 

К сожалению, refs не прокидываются через обычный HOC. forwardRef API решает эту проблему, предлагая перехватить ref и послать его дальше как обычное свойство.

```js
const FancyButton = React.forwardRef((props, ref) => (
  <button ref={ref} className="FancyButton">
    {props.children}
  </button>
));
// You can now get a ref directly to the DOM button:
const ref = React.createRef();
<FancyButton ref={ref}>Click me!</FancyButton>;
```
Еще пример, **forwardRef** как HOC (как я понял это по сути и есть HOC):
```js
export default React.forwardRef((props, ref) => <ElemComponent innerRef={ref} {...props}/>);

  //  Просим React прокинуть ref в ThemedComponent (через HOC)
  return React.forwardRef(ThemedComponent);
```

Как организовать валидацию форм (варианты):

**React.StrictMode** - оборачиваемые им компоненты показывают потенциальные ошибки (поиск потенциальных ошибок)
```js
      <React.StrictMode>
        <div>
          <ComponentOne />
          <ComponentTwo />
        </div>
      </React.StrictMode>
```

StrictMode
* находить компоненты, использующие небезопасные (старые) lifecycle-методы
* предупреждать об использовании старого API обозначения ref как строки
* выслеживать непредвиденные side-эффекты

**Error Boundaries** - компонент, аналог catch для **дочерних** компонентов, чтобы ошибка в js не валила всю приложенку.
**Что это:** component с методом **componentDidCatch(error, info)** в котором перехватываются ошибки, например вызов метода логирования:
```js
class ErrorBoundary extends React.Component {
  componentDidCatch(error, info) {
  }
}
// оборачиваем:
<ErrorBoundary>
  <MyWidget />
</ErrorBoundary>
```
***
**Middleware** (аналог фильтра) - это не обязательная часть redux, расширяет возможности (extends) добавляя точку между dispatching и action. Можно сделать цепочку (chain) из Middleware.
```jsx
export const formValidationMiddleware = ({getState, dispatch}) => {
    return (next) => {
        return (action) => {
            if (action.type === 'UPDATE_FIELD') {
                if (true)) { // какое-то условие тут и прерываем
                    return dispatch({type: "ERROR_NUMERIC_FIELD_IS_NOT_NUM"});
                }
            }
            return next(action); // иначе передаем по цепочке
        };
    };
};

// применяем
const store = createStore(
    rootReducer,
    initialState,
    applyMiddleware(formValidationMiddleware, thunk),
);
```
***
**Hooks** - опциональная функция, совместима со старым кодом. Для чего: чтобы уменьшить обертывание компонентов в HOC. **Точнее:** переиспользовать **логику** работы со state в **разных** компонентах.

Обычно для решение проблемы используют: **HOC** и **props render**

Hooks нацелены на решение всех этих проблем, позволяя вам писать функциональные компоненты, которые имеют доступ к state, context, методам жизненного цикла, ref и т. д., без написания классов.

**Пример** (при изменении state вызывается метод useEffect, как жизненный цикл компонентов componentDidMount и componentDidUpdate):
```js
import React, { useState, useEffect } from 'react';

function Example() {
  const [count, setCount] = useState(0);

  // Similar to componentDidMount and componentDidUpdate:
  useEffect(() => {
    // Update the document title using the browser API
    document.title = `You clicked ${count} times`;
  });

  return (
    <div>
      <p>You clicked {count} times</p>
      <button onClick={() => setCount(count + 1)}>
        Click me
      </button>
    </div>
  );
}
```

При этом можно создать свой **custom** hook обернув этот стандартный в функцию (это то как понимаю Я). И потом его можно переиспользовать как функцию в **разных** компонентах.
 
***

Web Components - совместимы с React, можно использовать совместно.

***
**Lifting State Up** - это передача состояний дочерними компонентами в родительский через методы переданные им в props родителем (т.е. через вызов методов по ссылкам на методы родителя с параметрами в виде value). Т.е. передача state, чтобы родитель его использовал.
***
**React.PureComponent** - как React.Component только implements shouldComponentUpdate()
Т.е. он САМ проверяет изменилось ли состояние и обновляется ТОЛЬКО если **prop** и **state** изменен.
Экономит время т.к. не нужно самому писать код проверяющий изменилось ли состояние чтобы отменить render компонента, если state остался прежним.

ВАЖНО: PureComponent в методе shouldComponentUpdate проверяет только ПОВЕРХНОСТНО изменения объектов. Если объект сложный сравнение может сработать не правильно (т.к. проверятся только свойства объекта первых уровней). НО можно использовать forceUpdate() если точно известно, что надо обновиться.

ВАЖНО: не проверяет изменения дочерних компонентов. ПОЭТОМУ они тоже должны быть pure components (т.е. сами проверять изменения???)
***
**redux-logic** - библиотека для бизнес логики, **посмотреть**, возможно будущая замена thunk и saga? https://github.com/jeffbski/redux-logic#comparison-summaries

**redux-observable** - для использования RxJS (redux-logic позиционируется и против него)
***
При использовании **redux-thunk** бизнес логика в action creators (если вдруг это неочевидно)

При любом подходе логика может быть в middleware (паттерн **SAM** (State-Action-Model) ). Используется для: validation, verification, authentication. И выбора нового action после обновления model. Но реализовать SAM нужно вручную.

redux-logic также помогает реализовать **SAM / PAL pattern**
***
**redux-promise** - простая поддержка Promise в actionCreator путем их возврата, thunk и saga варианты сложнее.

**redux-thunk** - может encapsulate сложность внутри actionCreator и отправить несколько promise разным reducers.
***

JWT token: <header>.<payload>.<signature>
Header - алгоритм хэша
payload - User Id, Username, Email, Image URL, roles
signature

**Для авторизации:** (проверка прав)
1. Создать HOC например RequireAuth
2. Хранить isAuthenticated в redux store и проверять ее в этом HOC
3. Обернуть в него components на которые редиректят Route и там проверять
   1. `<Route exact path='/' component={RequireAuth(Home)} />`

```js
function RequireAuth(Component) {
  return !!authed
    ? <Component {...props} />
    : <Redirect to='/login' />
}
```

Общая схема простановки jwt токена в запрос к серверу:
1. Выносим бизнес логику из action creator в services слой, это по сути просто функции
2. к каждому запросу к серверу добавляем jwt токен

Выносить логику в отдельный слой services это норма, этот слой просто функции.

Аналогично слою services выделяют слой helpers, это просто разные функции с не бизнес логикой, которые можно переиспользовать.

CSRF token - защита от CSRF, отправляется в header или cookie, хранится в localStorage (это безопасно не смотря на некоторые статьи и видно только своему домену). Это короткоживущий объект поэтому его кража не так страшна.

Вариант использования CSRF token, HOC добавляющий token в форму:
```js
const CSRFToken = () => {
    return (
        <input type="hidden" name="csrfmiddlewaretoken" value={csrftoken} />
    );
};
export default CSRFToken;

// ...
        return (
                 <form action="/endpoint" method="post">
                        <CSRFToken />
                        <button type="submit">Send</button>
                 </form>
        );
```

CSRF token также можно проставить в куки скриптом через document.cookies объект. Так по умолчанию умеет делать ангулар.

## Применение HOC

**Список применний HOC:**
* Loader
* Error
* Default
* Data - внутри него ajax запросом грузим данные, принимает url на данные и передает загруженные данные в обернутый component, для разных компонентов меняется только ссылка на endpoint
* MockData
* Logger
* Props
* Timeouts
* Container
* List

```jsx
// hasLogger
const hasLogger = (prefix = '') => WrappedComponent => {
  const HasLogger = props => {
    console.log(`${prefix}[Props]:`, props)
    return <WrappedComponent {...props} />
  }
  
  return HasLogger
}

// branch, branch(testFunction, Component1, Component2)
const branch = (test, ComponentOnPass, ComponentOnFail) => props => test
  ? <ComponentOnPass {...props} />
  : ComponentOnFail
    ? <ComponentOnFail {...props} />
: null

// hasError, hasError(CustomErrorComponent)(Component)
const hasError = (ErrorComponent = Error) => WrappedComponent => {
  const HasError = props =>
    branch(props.hasError, ErrorComponent, WrappedComponent)(props)

  return HasError
}

// hasData, 
const hasData = ({url, params, loadingMessage}) => WrappedComponent => {
  class HasData extends React.Component {
    state = {
      data: [],
      hasError: false,
      error: {
        title: 'Cannot retrieve Real Posts',
        message: 'Could not retrieve Real Posts from supplied API.'
      },
      useDefault: false,
      loading: false,
      loadingMessage
    }

    componentDidMount() {
      this.setState({loading: true})

      axios.get(url, { params })
      .then(({data}) => {
        this.setState({
          data,
          loading: false,
          hasError: false,
          useDefault: data.length === 0
        })
      })
      .catch(error => {
        console.log(error)
        this.setState({
          hasError: true,
          loading: false
        })
      })
    }

    render() {
      return (
        <WrappedComponent 
          {...this.state}
          {...this.props}
        />
      )
    }
  }

  return HasData
}

// применение hasData
hasData({
  url: 'https://jsonplaceholder.typicode.com/posts',
  params: {
    _limit: 10,
    page: 2
  },
  loadingMessage: 'Loading posts from JSON Placeholder...'
})(Component)
```

**Применение HOC через compose** (compose(func1, func2, func3, func4) == func1(func2(func3(func4)))))
```jsx
compose(
  hasLogger(),
  hasMockData(mockData, delay),
  hasError(ErrorComponent),
  hasDefault(DefaultComponent)
)
```

***
**React.Children.map** - т.к. props.children может быть объектом или массивом. (есть еще forEach, count etc)
```js
React.Children.map(props.children, () => ), а не props.children.map(() => )
```

***
**SyntheticEvent** - так называются обертки react вокруг браузерных событий, которые делают их кросс браузерными

**маленький список событий react**
onClick: a mouse button was pressed and released. Called before onMouseUp.
onContextMenu: the right mouse button was pressed.
onDoubleClick
 onMouseDown: a mouse button is depressed
 onMouseEnter: the mouse moves over an element or its children
 onMouseLeave: the mouse leaves an element
 onMouseMove
 onMouseOut: the mouse moves off of an element, or onto one of its children
 onMouseOver: the mouse moves directly over an element
 onMouseUp: a mouse button was released
onScroll

 onKeyPress Vs. onKeyUp and onKeyDown - в теории onKeyPress представляет нажатый символ, но в разных браузерах сделано по разному

 onKeyUp - когда ввод завершен
 ***
 в голом react нужно установить NODE_ENV = production
***

pipe() - как compose() наоборот


recompose - старая библиотека готовых HOC для разных случаев. Сейчас вместо нее рекомендуется Hooks из react js.
  https://habr.com/ru/company/raiffeisenbank/blog/354768/
recompact - аналог recompose


Relay framework - для создания приложений на react, например для лучшей работы react с GraphQL