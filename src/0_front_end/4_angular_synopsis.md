- [Useful commands](#useful-commands)
  - [Tools](#tools)
  - [npm касательно Angular и обновление](#npm-касательно-angular-и-обновление)
  - [angular](#angular)
- [Angular life cycle](#angular-life-cycle)
- [Components](#components)
- [Directives](#directives)
- [Binding types](#binding-types)
- [Фичи с TypeScript](#Фичи-с-typescript)
- [Ссылки на новые фичи в Angular](#Ссылки-на-новые-фичи-в-angular)

# Useful commands
## Tools
* Angular Console https://angularconsole.com/ - gui консольных команд
* https://angular.io/cli/generate
* https://update.angular.io/ - web страница помогает сделать update проекта на новую версию Angular
## npm касательно Angular и обновление
```bash
npm update                              # обновить пакеты проекта
npm update -D && npm update -S          # обновить пакеты проекта
sudo npm i -g --unsafe-perm npm@latest  # обновить npm если будут проблемы с правами доступа
npm audit fix   # Angular иногда просит набрать ее, чтобы пофиксить уязвимости этой командой
```
## angular
```bash
sudo npm upgrade --unsafe-perm -g @angular/cli      # обновление

# обновление с игнорированием конфликта пакетов
# и игнорированием требования сделать git commit перед обновлением
# после обновления возможно нужно будет установить некоторые зависимости вручную,
# какие именно будет написано в консоли после обновления
ng update @angular/cli @angular/core --allow-dirty

# создание mini проекта, app нужно всегда указывать
ng new my-best-app --style=scss --routing --minimal

ng generate component --skip-tests order-list   # генерация без тестов

# отключаем генерацию тестов для component
ng config schematics.@schematics/angular.component.spec false
ng config schematics.@schematics/angular.module.spec false
ng config schematics.@schematics/angular.service.spec false
ng config schematics.@schematics/angular.directive.spec false

ng update --all # обновляет пакеты самого Angular

# после указания ./ как пути можно будет открывать приложение локально
ng build --progress --base-href ./
```
***
```bash
### Создание Workspace с application:
ng new my-app --createApplication false --minimal   # создаем workspace
ng g application my-app --style=scss --routing --minimal    # создаем application
ng generate library foo-lib --prefix=foo    # создаем library если нужно
ng build foo-lib    # собираем library отдельно, --prod указывать нельзя
ng build my-app --prod  # собираем my-app, можно с --prod
ng serve my-app     # запуск
ng test foo-lib     # тесты
ng test my-app
ng generate component poster --project=foo-lib      # генерация component с указанием в каком application или library генерировать
cd dist/foo-lib && npm publish      # публикация lib в npm репе (сначало нужно создать аккаунт)
```
***
**Отключение тестов вручную в angular.json**
```json
// Отключение тестов для всех в angular.json
// (если workspace пустой доп. атрибуты в этот файл добавятся после создания 1го приложения):
// (ЭТОТ КОНФИГ ДОБАВЛЯТЬ в КАЖДОЕ приложения workspace ИЛИ ГЛОБАЛЬНО для всех)
"schematics": {
        "@schematics/angular": {
            "component": {
                "spec": false
            },
            "module": {
                "spec": false
            },
            "service": {
                "spec": false
            }
        }
}
```
Создать общий каталог assets для всех проектов так (прим. различные библиотеки дополняющие Angular могут иметь эту фичу из коробки)
```json
// Создать общий каталог assets для всех проектов так:
// в /project создать каталог shared-assets и добавить в файл проекта (для КАЖДОГО под проекта):
"assets": [
    "projects/popup-app/src/favicon.ico",
    "projects/popup-app/src/assets",
        {
            "glob": "**/*",
            "input": "./libs/shared-assets/",
            "output": "./assets"
        }
    ],
```
***
Можно переопределить поведение для --prod в angular.json, опции ставить аналогичные тем что в projects.{project-name}.architect.build.options: `projects.{project-name}.architect.build.configurations.production`
***
Сокращения команд
```bash
ng g app # ng g application
ng g lib # ng g library

ng update
ng add

ng g class <name> [options]
ng g interface <name> <type> [options]
ng g library <name> [options]
ng g module <name> [options]
ng g pipe <name> [options]
ng g service <name> [options]
ng g serviceWorker [options]
ng g universal [options]
ng g guard <name> [options]
ng g enum <name> [options]
ng g directive <name> [options]
ng g application <name> [options]
ng g appShell [options]

ng new [name] --minimal # генерирует проект без тестовых файлов и с inline шаблонами (на деле тестов нет только e2e, spec файлы при генерции всеравно есть; inline шаблон только для app компонента)
ng new [name] --style=scss # генерация проекта с scss вместо css
ng new [name] --routing # при создании добавиться routing модуль
ng new [name] --prefix=myapp # меняем prefix с app на что-то другой (нужно при разработки библиотек?)
ng new [name] --skip-git # не создавать git при создании проекта
ng generate module orders --routing
ng generate component orders/order-list
ng generate module heroes/heroes --module app --flat --routing

# не создавать приложение по умолчанию в workspace, чтобы использовать много приложений сгенерированных ng g application my-app
ng new my-app --createApplication false

# создаем с export для component из модуля (import делается по умолчанию)
ng generate component orders/order-list --skip-tests --export=true
```
# Angular life cycle

* **ngOnInit** - выполняется после связи поле с свойствами класса. В нем предлагается fetch data. Выполняется только 1 раз.
  * Использовать fetch data и некоторые subscribe в constructor - плохо.
  * constructor должен только инициализировать переменные.
  * ngOnChanges - вызывается после constructor, но перед ngOnInit (и еще много раз во время работы). В этом месте можно отловить первый раз когда input привязанные к поля устанавливаются.
* **ngOnDestroy** - выполняется до уничтожения directive (или компонета)
  * это место чтобы оповестить другие компоненты приложения, что данный компонент недоступен
  * это место для того чтобы почистить то, что не чистит сборщик мусора автоматически. Отписаться от Observables и DOM events, остановить interval timers. Unregister all callbacks that this directive registered with global or application services.
* **ngOnChanges(changes: SimpleChanges)** - вызывается как только произойдет изменение input у component (или directive)
  * при этом changes содержит инфу о измененных объектах
  * вызывается только для input связанных с полями класса. Если с полем класса связана ссылка на объект и свойство этого объекта изменено, то метод НЕ вызовется
* **ngDoCheck** - вызывается как только Angular заметит любое изменение, в том числе и ссылок на объект (и UI в том числе событий)
  * extends ngOnChanges
  * предлагается использовать его там, где ngOnChanges не подходит (там где Angular не детектит изменения)
  * внутри ngDoCheck нужно написать свою реализацию проверки изменилась ли ссылка (сравнения в if). Т.к. хотя он вызывается при любом изменении, но данных этих изменений не содержит)
  * ngDoCheck вызывается ОЧЕНЬ часто, на любое изменение (в том числе UI) поэтому его нужно использовать осторожно. Нужно использовать легкий код в нем.
* **ngAfterViewInit** - вызывается один раз когда инициализируется дочерний компонент @ViewChild (и directive?). 
  * Вызывается только при первом изменении component (его шаблона)?
  * @ViewChild(ChildViewComponent) viewChild: ChildViewComponent;
  * вызывается после первого ngAfterContentChecked
* **ngAfterViewChecked** - вызывается когда Angular закончил проверку изменений в component и его детях
  * вызывается после ngAfterViewInit и после каждого следующего ngAfterContentChecked
  * вызывается ОЧЕНЬ часто, в нем нужно писать свою логику сравнения на изменения (через if). Нужно использовать легкий код в нем иначе будут тормоза.

* **ngAfterViewInit и ngAfterViewChecked** - вызываются после того как шаблон сформирован (composed). unidirectional связь не дает сделать update после того как View was composed (т.е. нельзя менять уже сформированное View?). Поэтому нужно подождать (например вызовом tick) перед тем как обновить View, в пример это сделано так: this.logger.tick_then(() => this.comment = c);
  * если попытаться обновить View без задержки, то случиться ошибка

* **ngAfterContentInit** - вызывается после того как Angular вставляет внешние данные в component view или directive
  * вызывается 1 раз после ngDoCheck
  * AfterContent - вообще это проброс куска htm из родителя в дочерний компонент и только через него можно получить доступ к:
    ```ts
    @ContentChild(ChildComponent) contentChild: ChildComponent;
    <after-content>
    ```
* **ngAfterContentChecked** - вызывается после того как Angular проверил данные вставленные в component view или directive
  * вызывается после ngAfterContentInit и каждого следующего ngDoCheck
  * тут не нужна задержка как в ngAfterViewChecked, component обновляется сразу

**AfterContent** вызываются до **AfterView** - между вызовом AfterContent и AfterView есть "окно" в котором можно вызывать изменение view

# Components
* **Directive** - содержит логику, но не структуру
* **Component extends Directive** - содержит логику + может содержать много Directive (которые могут указывать на Component)
* **ПОЭТОМУ:** везде где использован Directive может быть использован и Component

1. Компонент принято всегда делать export из их модулей
2. Компонент не должен fetch или save данные напрямую, только через сервисы или предоставлять fake данные. Это должно работать через сервис.
3. template reference - это указатель на component найденный компилятором в шаблоне
4. `constructor(@Optional() private loggerService: LoggerService)` - @Optional() подавляет ошибку, если inject класса не существует, тогда значение будет null
5. emitDecoratorMetadata: true - стоит по умолчанию в tsconfig.json и отвечает за сохранение инфы о типах в TypeScript
6. `providers: [Logger] == [{ provide: Logger, useClass: Logger }]` - useClass указывает какой класс будет использован для inject, например если ссылка на абстрактных класс, то в useClass могут быть его разные реализации
   1. `[{ provide: Logger, useClass: Logger, deps: [Logger, UserService] }]` - одна из форм с зависимостями в deps
   2. `{ provide: OldLogger, useExisting: NewLogger}]` - useExisting в отличии от useClass создает связь не с классом, а с token. Если token не существует, то ошибка.
        ```ts
        providers: [
        {provide: B, useClass: A}, // T[B] => I[A]
        {provide: C, useExisting: B}] // T[C] => T[B] => I[A], связь идет с токеном B, хотя он не класс
        ```
   3. `[{ provide: AppConfig, useValue: HERO_DI_CONFIG })]` - useValue может inject уже созданные переменную, function, object, string напрямую
   4. Factory providers - создание зависимости во время работы.
      1. Нужно для:
          1. Выбора inject класса  во время работы. Например разные реализации UserSrv, для авторизованых и нет (чтобы не усложнять код сервиса авторизационной логикой)
          2. Для создания inject класса, которые не разрабатывался для DI
       1. Как использовать:
            ```ts
            export let heroServiceProvider = { 
                provide: HeroService,
                useFactory: (logger: Logger, userService: UserService) => new HeroService(logger, userService.user.isAuthorized),
                deps: [Logger, UserService]
            };
            @Component({
                selector: 'app-heroes',
                providers: [ heroServiceProvider ],
                template: `
                    <h2>Heroes</h2>
                    <app-hero-list></app-hero-list>
                `
            })
            export class HeroesComponent { }
            ```
7. Поиск injectors идет по иерархии: component => ngModule
8. @Self - поиск зависимости начинатется только с local injector (самого себя): constructor(@Self() public dependency: Dependency)
9. @Host - поиск injector будет вестись пока не будет достигнут родительский component текущего injector: constructor(@Host() public dependency: Dependency)
10. @SkipSelf() - если пометить зависимости так constructor(@SkipSelf() public dependency: Dependency) то поиск зависимостей не будет идти в самом классе, а начнется с родителя
11. Еще один способ сделать inject, прямое создание (из контекста?)
    ```ts
    export const APP_CONFIG = new InjectionToken<AppConfig>('app.config');
    providers: [{ provide: APP_CONFIG, useValue: HERO_DI_CONFIG }]
    constructor(@Inject(APP_CONFIG) config: AppConfig) {} // тут обязательно использовать @Inject ???
    ```
12. Если DI зависимость не будет найдена, то значение DI переменной null
13. Интересный факт, можно добавить localStorage как зависимость
    ```ts
    export const BROWSER_STORAGE = new InjectionToken<Storage>('Browser Storage', {
        providedIn: 'root',
        factory: () => localStorage
    });
    @Injectable({
        providedIn: 'root'
    })
    export class BrowserStorageService {
        constructor(@Inject(BROWSER_STORAGE) public storage: Storage) {}
    }
    ```
14. Привязываем свойство класса к содержимому элемента: `<span [innerHTML]="title"></span>`
15. @Output
    1. EventEmitter используется в общении родительского и дочернего компонентов, для передачи значения из дочернего в родителя по событию.
        В некоторых статьях не советуют использовать его оборачивая в Observable и вообще не использовать ни для чего другого кроме общения родителя и дочернего компонентов.
    2. @Output('myClick') clicks = new EventEmitter<string>();
    3. @Directive({ //длинный вариант
        outputs: ['clicks:myClick']  // propertyName:alias
    })
    1. (без @Output)
        ```js
        <button (click)="onClickMe()">Click me!</button>
        export class ClickMeComponent { onClickMe() {} }
        ```
    1. (без @Output)
        <input (keyup)="onKey($event)">
         onKey(event: any) { this.values += event.target.value + ' | '; }
    2. Пояснение передачи event без @Output
       1.  Передавать event это сомнительная практика!
       2.  `onKey(event: KeyboardEvent) { //` можно так ограничить тип event, но работает не на всех типах event
            ```ts
            this.values += (<HTMLInputElement>event.target).value + ' | '; // приводим к тип event для input
            ```
       3. `<input #box (keyup)="0"> //привязываем 0 к событию, теперь минимальные требования по обработке события выполнены и каждое нажатие Angular будет обновлять компонент`
       4. `<input #box (keyup.enter)="onEnter(box.value)"> // работает только когда нажат Enter и передает только value`
1.  Один из вариантов использования useExisting для inject его component
     1. Старый вариант с forwardRef: `providers: [{ provide: Parent, useExisting: forwardRef(() => AlexComponent) }],`
     2.  Более новый вариант поместить логику в функцию: `const provideParent = (component: any) => { provide: Parent, useExisting: forwardRef(() => component) };`
     3. А потом использовать: `providers:  [ provideParent(AliceComponent) ]`

# Directives
```html
<!-- for -->
<div *ngFor="let hero of heroes" />
<div *ngFor="let item of items; let i = index" />

<!-- since Angular 5 -->
<div *ngFor="let item of items; index as i" />

<!-- есть еще odd, even, last -->
<div *ngFor="let user of userObservable | async as users; index as i; first as isFirst" />

<div *ngFor="let item of items; index as i; trackBy: trackByFn" />

<!-- ng-template и старые вариант for: ngForOf -->
<ng-template ngFor let-item [ngForOf]="items" let-i="index" [ngForTrackBy]="trackByFn">
    <li>...</li>
</ng-template>

<!-- ngSwitch -->
<div [ngSwitch]="hero?.emotion">
    <app-happy-hero    *ngSwitchCase="'happy'"    [hero]="hero"></app-happy-hero>
    <app-sad-hero      *ngSwitchCase="'sad'"      [hero]="hero"></app-sad-hero>
    <app-confused-hero *ngSwitchCase="'app-confused'" [hero]="hero"></app-confused-hero>
    <app-unknown-hero  *ngSwitchDefault           [hero]="hero"></app-unknown-hero>
</div>

<!-- <select> -->
<select [(ngModel)]="hero">
    <ng-container *ngFor="let h of heroes">
        <ng-container *ngIf="showSad || h.emotion !== 'sad'">
        <option [ngValue]="h">{{h.name}} ({{h.emotion}})</option>
        </ng-container>
    </ng-container>
</select>

<!-- two-way binding -->
<input [(ngModel)]="hero.name">

<!-- старый вариант on-click="onSave()" -->
<li (click)="onSelect(hero)">

<!-- if -->
<div *ngIf="selectedHero"></div>

<div [class.selected]="true/false" />

<!-- если class уже объявлен, то [class] перезатирает его -->
<div class="bad curly special" [class]="badCurly" />

<!-- для нескольких классов -->
<div [ngClass]="{'extra-sparkle': isDelightful,'extra-glitter':isGlitter}" />
<div [ngClass]="['first', 'second']" />
<div [ngClass]="myClasses">

<!-- установка атрибута colspan=2 -->
<div [attr.colspan]="1 + 1" />

<!-- camelCase для style тоже валидно -->
<div [style.font-size.em]="isSpecial ? 3 : 1" />
<div [style.font-size.%]="!isSpecial ? 150 : 50" />
<div [ngStyle]="{'max-width.px': widthExp}" />
```

# Binding types
```
One-way
{{expression}}
[target]="expression"
bind-target="expression"

One-way
(target)="statement"
on-target="statement"

Two-way
[(target)]="expression"
bindon-target="expression"
```

# Фичи с TypeScript
```ts
let s = e!.name; // строгая проверка на null в TypeScript
```

# Ссылки на новые фичи в Angular
* https://blog.ninja-squad.com/2017/11/02/what-is-new-angular-5/
* https://blog.ninja-squad.com/2017/12/07/what-is-new-angular-5.1/
* https://blog.ninja-squad.com/2018/01/11/what-is-new-angular-5.2/
* https://blog.ninja-squad.com/2018/05/04/what-is-new-angular-6/
* https://blog.ninja-squad.com/2018/07/26/what-is-new-angular-6.1/
* https://blog.ninja-squad.com/2018/09/07/angular-cli-6.2/
* https://blog.ninja-squad.com/2018/10/18/what-is-new-angular-7/
* https://blog.ninja-squad.com/2018/10/19/angular-cli-7.0/
* https://blog.ninja-squad.com/2018/11/22/what-is-new-angular-7.1/
* https://blog.ninja-squad.com/2018/11/27/angular-cli-7.1/
* https://blog.ninja-squad.com/2019/01/07/what-is-new-angular-7.2/
* https://blog.ninja-squad.com/2019/01/09/angular-cli-7.2/