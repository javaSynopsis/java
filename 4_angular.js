@ViewChild - чтобы получить доступ к дочернему компоненту
@ContentChild - чтобы получить доступ к переменной из дочернего компонента, когда сама переменная передана из родителя в дочерний.
    - в дочерний компоненту из родителя можно передать html (тип ElementRef) или directive (тип класса дерективы)
    @ContentChild(#name) - по id
    @ContentChild(ChildDirective) - по классу
@ViewChildren - привязывает массив компонентов, QueryList as an observable collection, выполняется когда что-то добавляется или удаляется из коллекции
    @ViewChildren(Pane) panes !: QueryList<Pane>;
@ContentChildren - тоже что и @ViewChildren только для @ViewChild

https://medium.com/@tkssharma/understanding-viewchildren-viewchild-contentchildren-and-contentchild-b16c9e0358e
    
5. Providers
    providedIn - лучший вариант потому что включает tree-shaking (очистку не используемых частей)
    Инжектить сервисы через shared module плохо!
    Note: If you provide your service within a sub-module, it will by available application wide unless the sub-module is lazy loaded.
    
    1. @Injectable({
            providedIn: 'root', // рекомендуется
        })
        export class UserService {}
    2. @Injectable({
            providedIn: UserModule, // используется в основном для разработки библиотек, не работает с lazy модулями
        })
        export class UserService {}
    3. @NgModule({
            providers: [UserService], // старый способ (можно использовать в lazy модулях); переопределяет сервисы объявленные в providedIn?
        })
        export class UserModule {}
    4. @Component({
            providers: [UserService]
                //инжектим сервис только в конкретный компонент (можно использовать в lazy модулях)
                //при этом service НЕ singleton и может использоваться как кэш (напр. если он нужен разным компонентам с разными сохраненными данными. В некоторых статьях советуют использовать такой подход в качестве кэша. Сервис уничтожится когда уничтожится компонент - экономия памяти?
        })
        export class UserComponent {}
        
    constructor(private heroService: HeroService) { } - инжектим в конкретный класс (иначе сервис недоступен)

6.  Примеры кода

    1. Root Component
        AppComponent - объявлен в declarations и в bootstrap чтобы инжектится в index.html
        
        @NgModule({ // УСЛОВНО модули делятся на 2 типа:
            1. с сервисами [+ компоненты] - импортируется в root (core module). Нельзя импортировать повторно в lazy модули.
            2. с компонентами - импортируются в каждый модуль (shared module). Если такой модуль импортирован повторно, то Angular авто очистит дубль (т.е. все ОК, так можно и это встречается например при импорте в core module (root) модуля shared)
        declarations: [ // объявить все components, directives and pipes модуля. Каждый из них может быть объявлен только в одном declarations. Их нужно re-export потому что они private scope.
            AppComponent
        ],
        imports: [ // импортим модули чей функционал нужен (модули которые нужны в нескольких модулях, а не только конкретном засовываем в shared module и импортим его)
            BrowserModule,
            FormsModule,
            HttpClientModule
        ],
        providers: [], //импорт модулей с компонентам (модули с сервисами provided в root), их НЕ нужно re-export потому что часто они global scope (root)
            - если это обычный module, то сервисы в нем доступны всему приложению?
            - если это lazy module, то сервисы доступны только в нем?
            - forRoot() - возвращает instance класса в обертке ModuleWithProviders (ngModule + provider) чтобы импортировать в core: components + services
            - forChild() - импортирует только components без сервисов (чтобы импортировать в components отличные от core без сервисов)
        bootstrap: [AppComponent]
        })
        export class AppModule { }
        
7.  entryComponents (дополнить) - на практике объявляются при использовании сторонних библиотек, чтобы загрузить их компоненты.
    Это динамически добавляемые модули (во время выполнения программы) через ViewContainerRef.createComponent(). Свойство entryComponents говорит компилятору компилировать их и создать factories для них.
    
    Если не добавить динамический component в entryComponents, то получится ошибка. Т.е. Angular не сможет скомпилить их.

    - это компонент который грузится по требованию (imperatively) в DOM. Компонент который грузится через вставку его селектора в шаблон (declaratively) в DOM это НЕ entryComponents.
    - AppComponent это entryComponents, потому что index.html это не шаблон (и следовательно загрузки компонентов по селектору через него нет)
    - большинство случаев не требует entryComponents
    - компонент в route definitions это entryComponents. Потому что route ссылается на компоненты по типу класса (а не селектору шаблона). Он загружается в RouterOutlet
    - 2 типа entryComponents: bootstrapped root component, A component you specify in a route definition
    - Свойство @NgModule.bootstrap загружает объявленное в нем как entryComponents (просто аналог entryComponents). Объявлять одни и те же components одновременно в @NgModule.bootstrap и entryComponents - плохо!
    - другие виды entryComponents загружаются другими средствами, например через router.
    - Компилятор сам добавляет "router component" в entryComponents из route или bootstrap. Если использовать ручную загрузку компонента, то придется добавить его в entryComponents
    - загрузка компонента bootstrap вручную
        class AppModule implements DoBootstrap {
            ngDoBootstrap(appRef: ApplicationRef) {
                appRef.bootstrap(AppComponent); // Or some other component
            }
        }
    - entryComponents нужны чтобы работал tree shaking, он удаляет все неиспользуемое и может удалить компоненты загруженные вручную (но то что в entryComponents не трогает) 

    The entryComponents array is used to define only components that are not found in html and created dynamically with ComponentFactoryResolver. Angular needs this hint to find them and compile. All other components should just be listed in the declarations array.
    
8.  viewProviders - видны в component и его view child, но не видны в content child. А provider видны везде.
        - нужно например чтобы ограничить инжекс сервисов в библиотеках

    @Component({
        viewProviders: [
            MySrv
        ],
    })
    
9.  События инициализации (дописать!!!)

Predefined tokens - это предопределенные токены (имена: PLATFORM_INITIALIZER и прочие) для provider атрибута.
Их можно использовать для изменения поведения приложения.

    // то есть эта штука объявляется на модуле и действует внутри него???
    export const APP_TOKENS = [
        { provide: PLATFORM_INITIALIZER, useFactory: platformInitialized, multi: true    }, - callback загрузки приложенки
        { provide: APP_INITIALIZER, useFactory: delayBootstrapping, multi: true },  - callback перед инициализацией приложенки
        { provide: APP_BOOTSTRAP_LISTENER, useFactory: appBootstrapped, multi: true }, - callback загрузки каждого компонента
    ];
    
NG_VALIDATORS - используется для кастомного валидатора template driven form

Можно объявить много (multi) провайдеров (providers) для одного и того же токена.
Например так можно для NG_VALIDATORS объявить несколько кастомных валидаторов.
Для этого используется свойство multi: true

Пример (в заинжекшеном сервисе получим массив классов если имя одно и тоже):
    providers: [
        { provide: 'SuperProvider', useClass: class A {}, multi: true },
        { provide: 'SuperProvider', useClass: class B {}, multi: true}]

    constructor(@Inject('SuperProvider') private testInjection) {

ROUTES - тоже токен который как-то используется внутри, чтобы комбинировать sets of routes в одно значение.
    
    В этом месте можно зарегистрировать кастомный валидатор форм NG_VALIDATORS (есть еще NG_ASYNC_VALIDATORS).
    Для этого еще нужно implements AsyncValidator
    
    @Directive({
    selector: '[customValidator]',
    providers: [{provide: NG_VALIDATORS, useExisting: CustomValidatorDirective, multi: true}]
    })
    class CustomValidatorDirective implements Validator {
    validate(control: AbstractControl): ValidationErrors | null {
        return { 'custom': true };
    }
    
10. HostListener - для связи СОБЫТИЙ с методами дерективы
        и
    HostBinding - для связи со СВОЙСТВАМИ (т.е. свойствами объекта js привязанного к тегу в DOM) компонента на котором проставлена деректива (т.е. их изменения)
    Они работают для Directive и Component

    Если из HostListener return false; то это тоже что и preventDefault

    https://angular.io/api/core/HostListener
    https://angular.io/api/core/HostBinding
    
    @Directive({
        selector: '[tohValidator]'
    })
    export class ValidatorDirective {
        @HostBinding('attr.role') role = 'button';
        @HostBinding('class.pressed') isPressed: boolean;
        @HostBinding("style.cursor") get getCursor(){ return "pointer"; }
        @HostListener('mouseenter') onMouseEnter() {
            // do work
        }
        @HostListener('mousedown') hasPressed() {
            this.isPressed = true;
        }
    }
    
    // длинный вариант
    @Directive({
        selector: '[tohValidator2]',
        host: {
            '[attr.role]': 'role',
            '(mouseenter)': 'onMouseEnter()'
        }
    })
    export class Validator2Directive {
        role = 'button';
        onMouseEnter() {
            // do work
        }
    }
    
    Передача параметров через @HostListener:
        @HostListener('click', ['$event.target'])
        onClick(btn) { console.log(btn); }

11. Inject DOM Element

    @Directive({
    selector: '[appHighlight]'
    })
    export class HighlightDirective {
    private el: HTMLElement;
    
    constructor(el: ElementRef) {
        this.el = el.nativeElement;
    }
    
12. Router. Общий процесс, когда маршруты добавляются создается набор сервисов и компонентов, который можно использовать через DI
    import route module дожен быть последним, если точнее перед module на который он действует. Это нужно потому что иерархия роутеров такая же как у модулей, иначе совпадет не тот путь.
    
    Жизненный цикл маршрута:
        Parse: разбирает  сроку запроса
        Redirect: выполняет перенаправление (если нужно)
        Identify: определяет стейт
        Guard: запускает гарды конкретного стейта
        Resolve: резолвит данные стейта
        Activate: активирует компонент
        Manage: слушает изменения, чтобы повторить процесс сначала
        
    Router events - события которые выполняются при проходе жизненного цикла маршрута:
        (это не полный список без описания, полный тут https://angular.io/guide/router )
        NavigationStart
        RouteConfigLoadStart
        RouteConfigLoadEnd
        GuardsCheckStart
        ChildActivationStart
        ActivationStart
        GuardsCheckEnd
        ResolveStart
        NavigationCancel
        NavigationError
        Scroll
        
        Для чего: например для показа loader или spinner при открытии страницы и его прятании при завершении навигации или работы resolver.
        
    ParamMap API, params.get('id')
        has(name)
        get(name)
        getAll(name) - массив значений параметра с одним именем
        keys - массив имен параметров
        
    Если при переходе component уже активирован, то он будет переиспользован

    
    0. вот так параметры можно брать только если component никогда не будет переиспользован (на него никто не перейдет повторно),
        иначе нужно подписываться на ActivatedRoute и брать параметр оттуда.
        Почему?  https://angular.io/guide/router#snapshot-the-no-observable-alternative
        ngOnInit() {
            // ДОСТАЕМ ПАРАМЕТР СПОСОБ 1
            let id = this.route.snapshot.paramMap.get('id'); // этот id не изменится даже при повторном переходе в тот же component, использовать осторожно
        }
        
        берем параметр из Route
        При этом switchMap при переходе по другому маршруту этого же component завершает предыдущий запрос в service отменяя старый, если старый запрос все еще весит (это его отличие от flatMap). Видимо (!) тут завершается действие предыдущего слушателя события?
        ngOnInit() {
            // ДОСТАЕМ ПАРАМЕТР СПОСОБ 2
            this.hero$ = this.route.paramMap.pipe(
                switchMap((params: ParamMap) =>
                    this.service.getHero(params.get('id'))) // подписка тут т.к. ngOnInit активируется 1 раз, а подписка на paramMap вечна (напр при переиспользовании component, когда мы переходим по одному и тому же component, но с разными данными, переоткрываем)
            );
        }
        
    matrix URL notation - это когда параметры разделены ;
        localhost:4200/heroes;id=15;foo=foo
    
    Router маршруты принято выносить в отдельный модуль.
    В простых приложенках можно оставить routers в component
    
    baseUrl в tsconfig.json задает корень для абсолютного пути, еще можно указать его командой Angular при сборке
    
    pathMatch: 'full' - только полное совпадение ссылки
    pathMatch: 'prefix' - выберется первый url префикс которого совпадет
    
    (относится к Angular 6.1)
    Запоминать позицию скролинга при возврате Назад. Работает только со static страницам.
    Для запоминания динамического скролинга можно использовать ViewportScroller для управления скроллом: https://stackoverflow.com/a/51713404
        RouterModule.forRoot(routes, {
            scrollPositionRestoration: 'enabled'
        })
    
    ExtraOptions - объект параметров передающийся в RouterModule.forRoot(routes, { СЮДА }
    несколько примеров ниже
    
    Включаем скролинг к якорю при переходе.
        @NgModule({
        imports: [
            RouterModule.forRoot(routes, {
            scrollPositionRestoration: 'enabled',
            anchorScrolling: 'enabled', // включаем скроллинг к якорю при переходе (для скролинга нужно передать название якоря в параметре?)
            scrollOffset: [0, 64] // [x, y] //смещение относительноя якоря к которому скролим
            })
        ],
        exports: [RouterModule]
        })
        export class AppRoutingModule {}
        
    NavigationExtras - объект содержащий разные параметры передаваемые при переходе вручную.
    (якорь в Angular называют fragment или anchor)
        let navigationExtras: NavigationExtras = {
            queryParams: { page: 2 }, // просто передача параметра /results?page=2
            fragment: 'anchor', // якорь в html к которому будет скролинг
            { preserveQueryParams: true }, // сохраняет параметры при переходе: /results?page=1 to /view?page=1
            { preserveFragment: true }, // сохраняет якорь html при переходе
            { skipLocationChange: true }, //навигация не попадает в историю браузера
            { replaceUrl: true }, //навигация с перезаписью истории браузера
            queryParamsHandling: "merge", //установка стратегии обработки параметры, merge объединяет текущие с новыми: from /results?page=1 to /view?page=1&page=2
            { relativeTo: this.route } // переход будет относительно адреса
        };

        // Navigate to the login page with extras
        this.router.navigate(['/login'], navigationExtras);
        
    Способы скролинга к якорю (авто переход к якорю будет выполнен только если установить anchorScrolling: 'enabled' в Routers):
    <a [routerLink]="['somepath']" fragment="Test">Jump to 'Test' anchor </a>
    this._router.navigate( ['/somepath', id ], {fragment: 'test'});

    1. Пример Router
        const appRoutes: Routes = [
            { path: 'crisis-center', component: CrisisListComponent },
            { path: 'hero/:id',      component: HeroDetailComponent },
            {
                path: 'heroes',
                component: HeroListComponent,
                data: { title: 'Heroes List' } //передача данных
            },
            { path: '',
                redirectTo: '/heroes', //переадресация
                pathMatch: 'full' //только полное совпадение ссылки, нужно для пустой строки
            },
            { path: '**', component: PageNotFoundComponent }
        ];

        @NgModule({
        imports: [
            RouterModule.forRoot(
                appRoutes,
                { enableTracing: true } // <-- debugging purposes only
            )
            // other imports here
        ],
        ...
        })
        export class AppModule { }
        
    2.  Пример 2 (с дочерними компонентами)
        const crisisCenterRoutes: Routes = [
        {
            path: 'crisis-center',
            component: CrisisCenterComponent,
            children: [
            {
                path: '',
                component: CrisisListComponent,
                children: [
                {
                    path: ':id',
                    component: CrisisDetailComponent,
                    canDeactivate: [CanDeactivateGuard],
                    resolve: {
                    crisis: CrisisDetailResolverService
                    }
                },
                {
                    path: '',
                    component: CrisisCenterHomeComponent
                }
                ]
            }
            ]
        }
        ];

        @NgModule({
        imports: [
            RouterModule.forChild(crisisCenterRoutes)
        ],
        exports: [
            RouterModule
        ]
        })
        export class CrisisCenterRoutingModule { }

    3. Простой пример
        const routes: Routes = [
            {
                path: '',
                component: CustomerListComponent
            }
        ];

        @NgModule({
            imports: [RouterModule.forChild(routes)],
            exports: [RouterModule]
        })
        export class CustomersRoutingModule { }
        
    4. Переход по маршрутам
        <a routerLink="/crisis-center" routerLinkActive="active">Crisis Center</a>
        <a [routerLink]="[{ outlets: { popup: ['compose'] } }]">Contact</a>
        <router-outlet #routerOutlet="outlet"></router-outlet>
        <router-outlet name="popup"></router-outlet>
        <a routerLink="/user/bob" routerLinkActive="active-link" [routerLinkActiveOptions]="{exact: true}">Bob</a>
        <a routerLink="/user/bob" routerLinkActive="class1 class2">Bob</a>
        <a routerLink="/user/bob" [routerLinkActive]="['class1', 'class2']">Bob</a>
        <a routerLink="/user/bob" routerLinkActive #rla="routerLinkActive">
            Bob {{ rla.isActive ? '(already open)' : ''}}
        </a>
        <div routerLinkActive="active-link" [routerLinkActiveOptions]="{exact: true}">
            <a routerLink="/user/jim">Jim</a>
            <a routerLink="/user/bob">Bob</a>
        </div>
        <a [routerLink]="['/hero', hero.id]">
        
        [routerLinkActive] vs routerLinkActive - 
        [routerLink] vs routerLink - первый для массива, второй для строки адреса
            [routerLink]="[{ outlets: { popup: ['compose'] } }]"
            [routerLink]="['/hero', hero.id]"
            routerLink="/sidekicks"
        
    5. Навигация вручную
        this.router.navigate vs ...
        this.router.navigate(['/heroes', { id: heroId, foo: 'foo' }]); - с передачей параметров
        this.router.navigate(['../', { id: crisisId, foo: 'foo' }], { relativeTo: this.route }); - относительно другой ссылки
        this.router.navigate([{ outlets: { popup: null }}]); - чистка outlet ставя в null
        this.router.navigateByUrl(`${my}/user`); - навигация по ссылке в виде строки, а не массива
        
    5.2 Чтобы вернувшись в MyListComponent из MyDetailComponent увидеть выделанным выбранный пункт нужно при навигации передать параметр на основе которого выделить элемент:
        this.router.navigate(['/heroes', { id: heroId, foo: 'foo' }]); - берем id и выделяем в списке по id
        
    5. Lazy routing
    6. Классы rout - когда роутинг завершается формируется дерево объектов ActivatedRoute. Их можно инжектить через Route сервис
        0. Router.events - содержит много событий по навигации, роутингу, скролингу.
            Эти события логируются, если enableTracing == true
        1. ActivatedRoute - Инжектится в component. Имеет кучу параметров url, params, outlet, children (child routes), routeConfig и т.д.
            Этот класс можно не делать unsubscribe в деструкторе component, он одно из исключений. Чистится сам. Вреда от отписки нет, если хочется можно отписаться вручную.
            1.
            @Component({...})
            class MyComponent {
                constructor(route: ActivatedRoute) {}
            }
            2.
            export class Routable2Component implements OnInit {
                constructor(private activatedRoute: ActivatedRoute) {}
            
                ngOnInit() {
                    this.activatedRoute.url
                        .subscribe(url => console.log('The URL changed to: ' + url));
                }
            }
        2. ActivatedRouteSnapshot - router.snapshot
        2.1 RouterState - router.routerState
        2.2 router.routerState
        3. Router - инжектится в компонент
            (еще тут техника, ловим событие в constructor, а подписываемся в onInit)
            @Component({})
            export class Routable1Component implements OnInit {
            
            navStart: Observable<NavigationStart>;
            
            constructor(private router: Router) {
                // Create a new Observable the publishes only the NavigationStart event
                this.navStart = router.events.pipe(
                    filter(evt => evt instanceof NavigationStart)
                ) as Observable<NavigationStart>;
            }
            
            ngOnInit() {
                this.navStart.subscribe(evt => console.log('Navigation Started!')); //пример подписки на событие старта router
            }
        4. Location - рекомендуется использовать Router, Location рекомендуется только для normalized URLs outside of routing.
            Как я понял этот сервис взаимодействует с самим браузером.
            Кроме того содержит разные методы для работы с url, обрезать, соединять параметры и т.д.
            export class PathLocationComponent {
                location: Location;
                constructor(location: Location) { this.location = location; }
            }
        5. Анимация в при роутинге (ДОПИСАТЬ!!!)
                https://angular.io/guide/router#adding-routable-animations
            1. Подключаем модуль
            @NgModule({
                imports: [
                    BrowserAnimationsModule,
                ],
            })
            
    7.  Preloading - можно задать правило загрузки lazy модулей игнорирующее стандартное поведение.
        После каждой успешной навигации router проверяет конфиги незагруженных модулей и если там есть правило Preloading использует его.
        Например можно загрузить lazy модуль вместе с приложением, но с задержкой в 1 минуту после загрузки приложения.
        
        CanLoad блокирует preload стратегию, т.е. даже поставив PreloadAllModules автоматом lazy модули не загрузятся.
        В этом случае нужно использовать просто canActivate()
        
        // по умолчанию PreloadAllModules загружает все lazy модули
        RouterModule.forRoot(
            appRoutes,
            {
                enableTracing: true, // <-- debugging purposes only
                preloadingStrategy: PreloadAllModules // ставим стратегию
            }
        )
        
        Реализация своей стратегии:
        1. Установить data: { preload: true }, это не что-то встроенное, просто данные, которые потом используем в кастомной стратегии
            {
                path: 'crisis-center',
                loadChildren: './crisis-center/crisis-center.module#CrisisCenterModule',
                data: { preload: true }
            },
        2. Создать service (custom pre loading strategy это service)
            2.1 ng generate service selective-preloading-strategy
            2.2 Реализовать
                @Injectable({
                    providedIn: 'root',
                })
                export class SelectivePreloadingStrategyService implements PreloadingStrategy {
                    preloadedModules: string[] = [];

                    preload(
                        route: Route, 
                        load: () => Observable<any> // функция загрузчик
                    ): Observable<any> {
                        if (route.data && route.data['preload']) {
                        // add the route path to the preloaded module array
                        this.preloadedModules.push(route.path);

                        // log the route path to the console
                        console.log('Preloaded: ' + route.path);

                        return load();
                        } else {
                        return of(null);
                        }
                    }
                }
            2.3 Импортировать SelectivePreloadingStrategyService в AppRoutingModule (корень?) и подключить в preloadingStrategy.
                И добавить в provide: [SelectivePreloadingStrategyService] чтобы было доступно в других местах.
                RouterModule.forRoot(
                    appRoutes,
                    {
                        enableTracing: true, // <-- debugging purposes only
                        preloadingStrategy: SelectivePreloadingStrategyService // ставим стратегию
                    }
                )
        8. Полезные примеры:
            ngOnInit() {
                // Capture the session ID if available
                this.sessionId = this.route
                .queryParamMap
                .pipe(map(params => params.get('session_id') || 'None')); // берем id сессии если доступно

                // Capture the fragment if available
                this.token = this.route
                .fragment
                .pipe(map(fragment => fragment || 'None')); // берем якоря если есть
            }
            
        9. Migrating URLs with Redirects (перенаправление по URL с сохранением параметров)
        
            (до конца не ясно)
            The Router also supports !!! query parameters and the !!! fragment when using redirects.
                1. Если в absolute redirects использовать query parameters и fragment в redirectTo, то они возьмутся из route config
                2. Если в relative redirects использовать query parameters и fragment, то они возьмутся из source URL
        
            const heroesRoutes: Routes = [
                { path: 'hero/:id', redirectTo: '/superhero/:id' }
            ]
            
        10. Так можно посмотреть в консоли конфиги router для теста
        export class AppModule {
            // Diagnostic only: inspect router configuration
            constructor(router: Router) {
                // Use a custom replacer to display function names in the route configs
                const replacer = (key, value) => (typeof value === 'function') ? value.name : value;

                console.log('Routes: ', JSON.stringify(router.config, replacer, 2));
            }
        }
        
        11. runGuardsAndResolvers - позволяет установить когда будут вызваны guard и resolver
            при изменении: path parameter, query parameter, matrix parameter
            
            Значения:
                paramsChange (default) - для path, matrix
                paramsOrQueryParamsChange - для path, query, matrix
                always - для всех
                pathParamsChange - для path
                
        12. Типы параметров в Angular
                matrix parameter
                path parameter
                query parameter
        
13. Типы Feature Modules
    Domain - 
    Routed - это top level модули на которые указывают Routing модули
    Routing - содержат и экспортируют роутинг
    Service - содержат и экспортируют сервисы
    Widget - экспортируют components, directives, and pipes для внешних модулей

14  Attribute Directives - меняет вид или поведение element, component или directive. Можно применить МНОГО раз к элементу
    Structural Directives - меняет DOM удаляя или добавляя element, component или directive. Можно применить только ОДИН раз к элементу
        
    microsyntax - это промежуточный синтаксис, валидный html в который преоразуется template.
        let-hero, let-i etc
    
    Имя дерективы рекомендуется использовать с prefix: appHiglight вместо highlight.
    Использовать ng в префиксе нельзя.
    
    В отличии от component писать @Input перед свойствами класса directive обязательно.
    Правило:
        1. Если справо от = в выражении, то принадлежит этому component и не нужно @Input
        2. Если внутри [] слево от = то свойство принадлежит другому directive или component. Использовать @Input обязательно.
    
    1. Attribute Directives
        Создание:
            @Directive({
                selector: '[appHighlight]'
            })
            export class HighlightDirective {
                @Input('appHighlight') highlightColor: string; // чтобы было красивое имя используем alias (см. Использование 2)
                // @Input highlightColor: string; //а это если деректива и ее свойство не совпадают (см. Использование 1)
                // @Input() defaultColor: string; //см. Использование 3
                
                constructor(el: ElementRef) {
                    el.nativeElement.style.backgroundColor = 'yellow';
                }
                
                @HostListener('mouseenter') onMouseEnter() {
                    this.highlight('yellow');
                }
                
                private highlight(color: string) {
                    this.el.nativeElement.style.backgroundColor = color;
                }
            }
            
        Можно использовать дерективу 2мя способами. Деректива и ее атрибут в ОДНОМ атрибуте. И деректива и ее атрибут в РАЗНЫХ атрибутах.
            Использование 1 (деректива и свойство ДЕРЕКТИВЫ в разных атрибутах):
                <p appHighlight [highlightColor]="color">Highlighted with parent component's color</p> // тут деректива appHighlight имеет свойство highlightColor
            Использование 2 (деректива и свойство дерективы в ОДНОМ атрибует):
                <p [appHighlight]="color">Highlight me!</p>
            Использование 3 (тоже что и 2, но + атрибут по умолчанию)
                <p [appHighlight]="color" defaultColor="violet">
                
        // получаем ссылку на input внутри Directive
        // в этом примере устанавливаем css класс в зависимости от того валидный или нет input
        // Шаблон: <input [(ngModel)]="prop">
        @Directive({selector: '[ngModel]'})
        class NgModelStatus {
            constructor(public control: NgModel) {} // control это ссылка на ngModel
            @HostBinding('class.valid') get valid() { return this.control.valid; }
            @HostBinding('class.invalid') get invalid() { return this.control.invalid; }
        }
        
                
    2. Structural Directives
    
        Звездочка * в Structural Directives означает обернуть элемент с дерективой в <ng-template> и использовать директиву на этом <ng-template>, а не самом элементе.
        Пример:
            <div *ngIf> === <ng-template [ngIf]="hero"><div>
        
        Проблема: если стоит несколько структурных деректив, то какая главнее в случае конфликта? Поэтому нельзя использовать несколько Structural Directives на одном теге?
        Но в конце концов <ng-template> это промежуточный элемент и в итоговом DOM его нет (после компиляции):
            <ng-template ngFor let-hero [ngForOf]="heroes" let-i="index" let-odd="odd"
            
        Другими словами: можно использовать ТОЛЬКО ОДНУ Structural Directive на одном теге.
        Обходное решение: использовать ng-container
        
        Интересный факт: в конструкции NgSwitch-ngSwitchCase-ngSwitchDefault деректива NgSwitch это attribute directive, которая связана с Structural Directives и контролирует их поведение.
        
        template input variable - это переменные после слова let, например: odd, i, hero. Они видны только внутри тега.
            Пример: <div *ngFor="let hero of heroes; let i=index; let odd=odd; trackBy: trackById"
        template reference variable - это переменна вида <div #myVar>. Они видны повсюду (можно использовать в class)
        
        Создаем свою directive:
            1. Как обычную директиву, только со *
            2. Инжектим TemplateRef и ViewContainerRef чтобы управлять шаблоном
            3. Создаем EmbeddedViewRef через viewContainer.createEmbeddedView(this.templateRef) и делаем с ним что-то
        
            @Directive({ selector: '[appUnless]'})
            export class UnlessDirective {
                private hasView = false;

                constructor(
                    private templateRef: TemplateRef<any>,
                    private viewContainer: ViewContainerRef) { }

                @Input() set appUnless(condition: boolean) {
                    if (!condition && !this.hasView) {
                        this.viewContainer.createEmbeddedView(this.templateRef);
                        this.hasView = true;
                    } else if (condition && this.hasView) {
                        this.viewContainer.clear();
                        this.hasView = false;
                    }
                }
            }
            Применение:
            <p *appUnless="condition" class="unless a">
        
15. ng-container vs ng-template
    ng-container - невидимый элемент, который рекомендуется использовать там, где нужно проставить несколько Structural Directive, но этого нельзя сделать потому что 1 тег может иметь только 1 Structural Directive. Это НЕ directive, component, class, or interface
    ng-template - элемент который невидим после компиляции и в коде заменен на комментарий. Раньше применялся чаще и можно встретить в старом коде.
        Пример:
            <p>Hip!</p>
            <ng-template>
                <p>Hip!</p>
            </ng-template>
            <p>Hooray!</p>
        Отобразиться: Hip! Hooray!
    
    <ng-container *ngFor="let i of items">
        <div *ngIf="i.id"> {{i.name}} </div>
    </ng-container>

Использование ngTemplateOutlet для показа одного шаблона в нескольких местах:
<div #loading>Loading...</div>
<ng-container *ngTemplateOutlet="loading"></ng-container>
<ng-container *ngTemplateOutlet="loading"></ng-container>

15.2 *ngTemplateOutlet
Получение ссылок ngTemplateOutlet внутри родительского компонента:
    // 1. создаем компонент в который передадим другие шаблоны
    // ТО ЕСТЬ: мы можем использовать в ngTemplateOutlet указатели на переданные шаблоны извне помеченные @Input()
    @Component({
        selector: 'project-content',
        template: `
            <div #defaultTmpl>bla0</div>
            <ng-container *ngTemplateOutlet="headerTmpl ? headerTmpl : defaultTmpl"></ng-container>
            <ng-container *ngTemplateOutlet="footerTmpl ? footerTmpl : defaultTmpl"></ng-container>
    `})
    export class AppComponent {
        @Input() headerTmpl: TemplateRef<any>;
        @Input() footerTmpl: TemplateRef<any>;
    }

    // 2. создаем шаблоны
    <ng-template #headerTmpl> <div>bla1</div> </ng-template>
    <ng-template #footerTmpl> <div>bla2</div> </ng-template>

    // 3. передаем
    <project-content [headerTmpl]="headerTmpl" [footerTmpl]="footerTmpl"></project-content>

Template Partial @Inputs - это передача ссылок на шаблоны внутрь компонента (те что выше - @Input() TemplateRef).
    Потом их можно использовать для подстановки в ngTemplateOutlet
    
ngTemplateOutlet vs ng-content - в отличии от ng-content деректива ngTemplateOutlet позволяет подставлять шаблон в зависимости от условий
    (тринарный оператор или др.)
    
16 Пример ContentChild + ng-content

ng-content - показывает куда вставить код переданный из родителя в дочерний компонент.

Shadow DOM - так называют "скрытый" от пользователя DOM (т.е. пользователь использует готовый компонент не зная что внутри)
Light DOM - то что пользователь компонента передает внутрь вашего компонента (открытый для него, на свету)

// Shadow DOM
@Component({
  selector: 'some-component',
  template: `
    <h1>I am Shadow DOM!</h1>
    <h2>Nice to meet you :)</h2>
    <ng-content></ng-content>
  `;
})
class SomeComponent { /* ... */ }

// Light DOM
@Component({
  selector: 'another-component',
  directives: [SomeComponent],
  template: `
    <some-component>
      <h1>Hi! I am Light DOM!</h1>
      <h2>So happy to see you!</h2>
    </some-component>
  `
})
class AnotherComponent { /* ... */ }

Если переданных в дочерний компонент тегов несколько, то можно использовать несколько ng-content 
в каждый из которых вставить свой тег:

<my-comp>
    <span />
    <div />
</my-comp>

<!-- используем, select выбирает какой тег отобразить -->
<ng-content select="span"></ng-content>
<ng-content select="div"></ng-content>

17. Применение ExceptionService

    //создаем сервис с методами обработчиками ошибок
    @Injectable()
    export class ExceptionService {
        constructor(private toastService: ToastService) { }

        catchBadResponse: (errorResponse: any) => Observable<any> = (errorResponse: any) => {
            let res = <Response>errorResponse;
            let err = res.json();
            let emsg = err ?
            (err.error ? err.error : JSON.stringify(err)) :
            (res.statusText || 'unknown error');
            this.toastService.activate(`Error - Bad Response - ${emsg}`);
            return Observable.of(false);
        };
    }
    
    //вызов метода обработчика исключения из места где произошла ошибка
    @Injectable()
    export class VehicleService {
          addVehicle(vehicle: Vehicle) {
                let body = JSON.stringify(vehicle);
                this.spinnerService.show();
                return <Observable<Vehicle>>this.http
                .post(`${vehiclesUrl}`, body)
                .map(res => <Vehicle>res.json().data)
                .catch(this.exceptionService.catchBadResponse) //тут вызов метода (по ссылке на метод) с передачей объекта исключения
                .finally(() => this.spinnerService.hide());
            }
    }
18. Guard - возвращают true/false для разрешения/запрета навигации, могут спросить при выходе (отменить ли изменения или сохранить?), могут сделать redirect/
    Могут вернуть Observable содержащие 
    
    Интерфейсы guard для router:
        CanActivate to mediate navigation to a route.
        CanActivateChild to mediate navigation to a child route.
        CanDeactivate to mediate navigation away from the current route.
        Resolve to perform route data retrieval before route activation.
        CanLoad to mediate navigation to a feature module loaded asynchronously.
    
    CanDeactivate and CanActivateChild - проверяются с самого глубокого
    CanActivate - проверяется с самого верхнего в глубину
    CanLoad - если module lazy, то проверяется первым до загрузки модуля. Иначе даже если стоит CanActivate в false модуль всеравно загрузится.
    
    1. Как обычно проверяем
        @Injectable({providedIn: 'root'})
        export class AuthGuard implements CanActivate, CanActivateChild {
            constructor(private authService: AuthService, private router: Router) {}

            canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
                let url: string = state.url;

                return this.checkLogin(url);
            }

            canActivateChild(
                route: ActivatedRouteSnapshot,
                state: RouterStateSnapshot): boolean {
                return this.canActivate(route, state); // canActivateChild ставим зависимым от canActivate
            }
            
            canLoad(route: Route): boolean {
                let url = `/${route.path}`;

                return this.checkLogin(url);
            }
        }
        
        const adminRoutes: Routes = [
            {
                path: 'admin',
                component: AdminComponent,
                canActivate: [AuthGuard],
                children: [
                    {
                        path: '',
                        canActivateChild: [AuthGuard],
                        children: [
                            { path: 'crises', component: ManageCrisesComponent },
                            { path: 'heroes', component: ManageHeroesComponent },
                            { path: '', component: AdminDashboardComponent }
                        ]
                    },
                    {
                        path: 'admin',
                        loadChildren: './admin/admin.module#AdminModule',
                        canLoad: [AuthGuard]
                    }
                ]
            }
        ];
    
19. Pipe

    Функция transform внутри pipe должна возвращать всегда один вывод для одного ввода. Иначе будет ошибка.
    
    В Anular нету FilterPipe or OrderByPipe потому что они мешают minification и замедляют работу (компилятор не может их собрать нормально).
    Если написать самому такие функции, то они скорее всего замедлят приложение.

     DatePipe, UpperCasePipe, LowerCasePipe, CurrencyPipe, and PercentPipe,
     JsonPipe (для вывода json в том числе тестового)
     
     {{ birthday | date:"MM/dd/yy" }} - передача параметров
     
     Как создать:
        1. наследуем PipeTransform и переопределяем transform.
        2. Первый параметр transform это значение, остальные это параметры pipe
        3. Добавить в declarations модуля (AppModule?)
        
    Технически PipeTransform можно не наследовать.
    Angular выбирает быстрейший алгоритм детекта изменений сам.
    Рекомендуется чтобы связи между component и pipe не было.
    
    Pure pipe - детектит изменения String, Number, Boolean, Symbol или reference (Date, Array, Function, Object)
    impure pipe - детектит любое изменения массива или свойств объекта. Выполняется на любой event и очень затратен. Использовать можно только с простыми функциями.
    
    Другими словами: если нужно детектит change массива или объекта, то их нужно пересоздавать, чтобы pipe понял, что они изменились.
    
    AsyncPipe - это async для разворачивания Observable
    
    @Pipe({
        name: 'flyingHeroesImpure',
        pure: false
    })
    export class FlyingHeroesImpurePipe extends FlyingHeroesPipe {}
    
    Интересный пример кэширующего pipe.
    Проверяет внутри url, если для него уже выполнялся, то ничего не делает и возвращает кэшированные данные. Иначе делает запрос:
        <div *ngFor="let hero of ('assets/heroes.json' | fetch) ">

    Пример:
    // {{ 2 | exponentialStrength:10 }}
    @Pipe({name: 'exponentialStrength'})
    export class ExponentialStrengthPipe implements PipeTransform {
        transform(value: number, exponent: string): number {
            let exp = parseFloat(exponent);
            return Math.pow(value, isNaN(exp) ? 1 : exp);
        }
    }
     
20. Resolver - начитка данных перед загрузкой страницы (задерживает рендер компонента пока данные не загружены).
    Вообще всю загрузку данных в компонент нужно делать через resolver. Resolver обращается к сервисам, а затем достает из resolver все загруженные через service данные.
    Заметь, resolver делается provide в Route модуль с маршрутами.
    Атрибут data содержит titles, breadcrumb text, and other read-only, static. Его можно использовать в resolve guard (resole формально считается guard, хотя в примерах применяется по другому).
    Файл Resolver это сервис: CrisisDetailResolverService, crisis-detail-resolver.service
    Если данные из service не начитались, то можно вернуть EMPTY список (из RxJs) перед этим сделать redirect: this.router.navigate(['/crisis-center']); на предыдущую страницу
    
    В примерах кода Resolver считается service (напр. CrisisDetailResolverService implements Resolve<Crisis>)
    
    Как это работает:
        вызывается resolver, начитывает данные, данные помещаются в переменную сервиса ActivatedRoute.data из которого их можно вытащить сделав inject
    
    Resolver ОБЯЗАТЕЛЬНО должен быть complete, в данном пример для завершения Observable используется take:
          resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Crisis> | Observable<never> {
            let id = route.paramMap.get('id');
        
            return this.cs.getCrisis(id).pipe(
                take(1), // Observable завершится после начитки первого значения из service
                mergeMap(crisis => {
                    if (crisis) {
                        return of(crisis);
                    } else { // id not found
                        this.router.navigate(['/crisis-center']); //в случае ошибки перенаправляем на другую страницу
                        return EMPTY;
                    }
                })
            );
        }
    
    1. Способ 1. Прямое указание в router
        export const AppRoutes: Routes = [
            ...
            { 
                path: 'contact/:id',
                component: ContactsDetailComponent,
                resolve: {
                    contact: 'contact' // указываем данные
                }
            }
        ];
    2. Способ 2. Создание класса
        @NgModule({
            imports: [
                RouterModule.forRoot([
                    {
                        ...
                        resolve: {
                            team: ContactResolve
                        }
                    }
                ])
            ],
            providers: [ContactResolve] // инжектим resolver в модуль
        })
        
        @Injectable()
        export class ContactResolve implements Resolve<Contact> {

            constructor(private contactsService: ContactsService) {}

            resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Contact[]> {
                return this.contactsService.getContact(route.paramMap.get('id')); //вытаскиваем данные из service
            }
        }
    3. Способ 3. resolver как функция (сервис) в provider
        @NgModule({
            RouterModule.forRoot([
                {
                    path: 'team/:id',
                    component: TeamCmp,
                    resolve: {
                        contact: 'contact' //добавляем resolver
                    }
                }
                ])
            ],
            ...
            providers: [
                ContactsService,
                {
                    provide: 'contact', //имя resolver по которому его вытаскивать
                    
                    //объявляем resolver
                    useValue: (route: ActivatedRouteSnapshot, state: RouterStateSnapshot) => {
                        ...
                }
            ]
        })
        export class AppModule {}
    3. Достаем данные из resolver
        @Component()
        export class ContactsDetailComponent implements OnInit {

            contact;

            constructor(private route: ActivatedRoute) {}

            ngOnInit() {
                this.contact = this.route.snapshot.data['contact'];
            }
        }
21. Установка HashLocation (использование # в ссылках)
22. В Angular предпочтительно не использовать большие буквы для имен констант, чтобы не сломать такие же в либах
        export const mockHeroes   = ['Sam', 'Jill']; // prefer
23. Form

    Reactive Forms - для сложных, синхронные. Построены через функции js.
        Более эффективно детектит изменения (скорее всего имеется ввиду более быстро). Потому что one-way data binding при каждый раз новом DataModel (т.е. пересоздается объект, а изменение детектится по смене ссылки на объект в отличии от полной проверки всех переменных DataModel на изменение?).
        FormControl всегда возвращает новое значение.
    Template-driven Forms - для простых случаев. Асинхронные. Построены через directives.
        Менее эффективно детектит изменения. Потому что two-way data binding для update существующей DataModel.
        FormControl всегда обновляет существующее значение.
    
    Ключевые классы:
        FormControl tracks the value and validation status of an individual form control.
        FormGroup tracks the same values and status for a collection of form controls.
        FormArray tracks the same values and status for an array of form controls.
        ControlValueAccessor creates a bridge between Angular FormControl instances and native DOM 
        
23.2 Reactive Form
    
    1.
        Использование:
            1. Импортировать ReactiveFormsModule
            2. Создать свойство в component
                export class NameEditorComponent {
                    name = new FormControl('');
                }
            3. Зарегистрировать на форме
                <input type="text" [formControl]="name">
            3.2 Или создаем группу
                Создаем:
                    profileForm = new FormGroup({
                        firstName: new FormControl(''),
                        lastName: new FormControl(''),
                    });
                Связываем:
                    <form [formGroup]="profileForm">
                        <input type="text" formControlName="firstName">
                        <input type="text" formControlName="lastName">
                        
            4. Действия:
                Value: {{ name.value }}
                this.name.setValue('Nancy');
                this.profileForm.value
                
    1.2 Отправка форм
        Связываем:
            <form [formGroup]="profileForm" (ngSubmit)="onSubmit()">
                <input type="text" formControlName="firstName">
                <input type="text" formControlName="lastName">
                <button type="submit" [disabled]="!profileForm.valid">Submit</button> // работает в паре с ngSubmit?
    
    3и метода создания форм: control(), group(), and array()
    
    2. Можно создать группу в группе.
        export class ProfileEditorComponent {
            profileForm = new FormGroup({
                firstName: new FormControl(''),
                lastName: new FormControl(''),
                address: new FormGroup({
                    street: new FormControl(''),
                    city: new FormControl(''),
                    state: new FormControl(''),
                    zip: new FormControl('')
                })
            });
        }
        
    2. this.profileForm.patchValue(...) vs this.profileForm.setValue(...)
                1. patchValue - можно обновить только часть полей Control. (чаще придется использовать это)
                2. setValue - нужно перечислить все поля обновляемого Control, иначе ошибка
        
        Пример:
        this.profileForm.patchValue({
            firstName: 'Nancy',
            address: {
                street: '123 Drew Street'
            }
        });
        
    3. Создание форму через FormBuilder удобнее
        export class ProfileEditorComponent {
            profileForm = this.fb.group({
                firstName: [''],
                lastName: [''],
                address: this.fb.group({
                    street: [''],
                    city: [''],
                    state: [''],
                    zip: ['']
                }),
            });
            
            constructor(private fb: FormBuilder) { }
        }
        
    4. Простая validation.
        HTML5 содержит набор встроенных правил (required, minlength, and maxlength) РЕКОМЕНДУЕТСЯ использовать их СОВМЕСТНО.
        СОВМЕСТНО это вместо novalidate: <form novalidate>, которая отключает HTML5 валидацию.
        
            profileForm = this.fb.group({
                firstName: ['', Validators.required],
                lastName: [''],
                address: this.fb.group({
                    street: [''],
                    city: [''],
                    state: [''],
                    zip: ['']
                }),
            });
            <input type="text" formControlName="firstName" required>
            
    5. Статус форм хранится в переменной (валидна или невалидна)
            {{ profileForm.status }}
            
    6. FormArray аналог FormGroup только без свойств с именами. Используется когда число полей формы заранее неизвестно.
            Пример:
                profileForm = this.fb.group({
                    firstName: ['', Validators.required],
                    lastName: [''],
                    address: this.fb.group({
                        street: [''],
                        city: [''],
                        state: [''],
                        zip: ['']
                    }),
                    aliases: this.fb.array([
                        this.fb.control('')
                    ])
                });
                
                <div formArrayName="aliases">
                    <div *ngFor="let address of aliases.controls; let i=index">
                        <input type="text" [formControlName]="i">
            
            Берем в коде:
                  return this.profileForm.get('aliases') as FormArray; // нужно преобразовывать потому что по умолчанию тип AbstractControl
                  this.aliases.push(this.fb.control('')); // вставляем
                  this.parentForm.controls['question1'].valueChanges.subscribe() //выполняется после изменения value в FormControl, но до того как оно всплывет в предках элемента
                  
23.3 Template-driven Forms
        Это простое связываение через [(ngModel)]="model.name"
        Нужно связать form с переменной. Это связывает с дерективой ngForm, которая имеет разные переменные (состояния, свойства etc для формы):
            <form #heroForm="ngForm">
            
        Для input обязательно нужно использовать атрибут name="blabla"
        
    Свойства формы связаны с CSS классами (можно использовать в выражениях доставая из className):
        ng-touched 	ng-untouched
        ng-dirty 	ng-pristine
        ng-valid 	ng-invalid
        
    Можно дать переменную для input
        <input #spy>
        {{spy.className}}
        
    Использование переменной не простой, а с ngModel для показа сообщения об ошибке:
        <input required [(ngModel)]="model.name" name="name" #name="ngModel">
        <div [hidden]="name.valid || name.pristine">error msg</div>
    ngModel тут чтобы сказать через exportAs ангулару, как именно variable должна быть привязана к directive
    
    Чтобы сбросить форму в pristine и обойти проблему когда после добавления нового объекта форму touched нужно:
        <button type="button" class="btn btn-default" (click)="newHero(); heroForm.reset()">
        
    Привязка submit действия к кнопке (когда нажата кнопка вызовется метод привязанный к форме):
        <form (ngSubmit)="onSubmit()" #heroForm="ngForm">

23.4 Form Validation (не дописано, схематично)
    1. Template-driven validation - делается с помощью директив HTML (required, maxlength etc и кастомных)
        Angular использует directives чтобы связать эти атрибуты со своими функциями валидации (т.е. required, maxlength - это directives?).
        
        Список ошибок null если форма valid, если форма invalid, то их можно достать:
            <input id="name" name="name" [(ngModel)]="hero.name" #name="ngModel">
            <div *ngIf="name.invalid && (name.dirty || name.touched)">
            <div *ngIf="name.errors.required">
            <div *ngIf="name.errors.minlength">
            
    2. Reactive form validation - validators добавляются внутри component в виде ссылок на функции
    
        Бывают validators:
            1. Sync validators - выполняются сразу, передаются ВТОРЫМ аргументом. Возвращают errors или null.
            2. Async validators - возвращает Promise or Observable, позже выполняется emit c errors или null. Передаются ТРЕТЬИМ аргументом.
            
        Async validators запускаются ТОЛЬКО после того как все Sync validators пройдены.
        
        Набор готовых validators: https://angular.io/api/forms/Validators
    
        Пример:
            this.heroForm = new FormGroup({
                'name': new FormControl(this.hero.name, [
                    Validators.required,
                    Validators.minLength(4),
                    forbiddenNameValidator(/bob/i) // <-- Here's how you pass in the custom validator.
                ]),
                'alterEgo': new FormControl(this.hero.alterEgo),
                'power': new FormControl(this.hero.power, Validators.required)
            });
            
            Использование шаблона:
                1. Берем control (а не переменную как в template driven!)
                    get name() { return this.heroForm.get('name'); }
                2. Используем:
                    <div *ngIf="name.errors.required">
                    
        Можно не использовать required атрибут (и др.?) в template, но можно оставить для понятности или чтобы цеплять CSS по атрибуту.
        
    3. Custom validation - примеры реализации грамоздкие, лучше смотреть в документации.
        Есть 2 интерфеса для custom validators:
            1. ValidatorFn - это тип фукции, которая используется в Reactive validator
            2. Validator = это тип дерективы (интерфейс), который наследуется и переопределяется. Такую директиву нужно регистрировать в provider: []

    3.1 Reactive custom validator - создать метод типа ValidatorFn, подключить к control
    3.2 Template-driven custom validator - создать директиву наследовав Validator, добавить для <input>
    3.3 сами validators возвращают при неудаче value которое содержит описание ошибки, иначе возвращают null
    3.4 sync валидаторы возвращают значение сразу, asyn возвращают Observable
    3.5 Cross field validation - предлагается делать через кастомные validators для reactive и через сравнение в шаблоне с помощью #var для template.
        Или валидация при которой нужно сравнить значения нескольких разных input в одной форме.
    
        // валидатор сразу на несколько полей
        const heroForm = new FormGroup({
            'name': new FormControl(),
            'alterEgo': new FormControl(),
            'power': new FormControl()
        }, { validators: identityRevealedValidator });
        
        // применяем к целой form? внутри берем дочерние control у formGroup и проверяем их
        <form #heroForm="ngForm" appIdentityRevealed>
    
    В async validator можно например делать запрос на сервер.
    Наверное так можно реализовать связь между валидациями на сервере и клиенте, чтобы не повторять их там и там.
    Реализация: Т.е. на сервере будут хранится или функции, которые возвращают true/false/<ошибка N>. Или регулярки, которые отправляются на клиент и используются.
    
        
23.5 Dynamic Forms
24. Dynamic Component Loader https://angular.io/guide/dynamic-component-loader

25. Техника показа loader - ловим событие перехода, показываем по началу перехода, скрываем когда переход закончен. Скрывать показывать нужно component с loader.
    class MyComponent {
        ngAfterViewInit() {
            this.router.events
                .subscribe((event) => {
                    if(event instanceof NavigationStart) {
                        this.loading = true;
                    }
                    else if (
                        event instanceof NavigationEnd || 
                        event instanceof NavigationCancel
                        ) {
                        this.loading = false;
                    }
                });
        }
    }
    
    // тоже самое, но используя новые события старта и остановки маршрутизации:
    //      GuardsCheckStart, ChildActivationStart, ActivationStart, GuardsCheckEnd, ResolveStart, ResolveEnd, ActivationEnd, ChildActivationEnd
    class MyComponent {
    constructor(public router: Router, spinner: Spinner) {
        router.events.subscribe(e => {
        if (e instanceof ChildActivationStart) {
            spinner.start(e.route);
        } else if (e instanceof ChildActivationEnd) {
            spinner.end(e.route);
        }
        });
    }
    }
    
26. Анимация в Angular

27. Как сделать подтверждение выхода:
    return window.confirm('Do you really want to cancel?'); внутри деструктора component, вернет true если пользователь нажал Да. Возможно не подойдет для закрытия вкладки.
    или canDiactivate?

28. Один из паттернов создания component:
        1. Создаем component обертку который только начитывает данные из resolver/service
        2. Создаем дочерний к нему component, который ничего не читает, а получает данные от родителя
        3. Так логика начитки отделена от логики работы. Без понятия насколько такой паттерн правилен
        
        Прим.
            Это паттерн из react + redux, обертка это stateful компонент и содержат начитку данных,
            а внутренние это stateless и отвечают только за отображение
        
29. Можно использовать в Component переопределенные get/set
@Component({
  selector: 'app-name-child',
  template: '<h3>"{{name}}"</h3>'
})
export class NameChildComponent {
  private _name = '';
 
  @Input()
  set name(name: string) {
    this._name = (name && name.trim()) || '<no name set>';
  }
 
  get name(): string { return this._name; }
}

30. Можно не писать @Input внутри component т.к. component и template доверяют друг другу. Но в directive писать @Input обязательно.

31. Создание нескольких приложений в одном проекте: ng generate application
    Начиная с Angular 6 файл angular.json это workspace который может содержать много проектов.
    
    По умолчанию уже есть 2: main и e2e тесты
    
31.2
    Чтобы сделать /assets каталог видимым в разных проектах (workspace).
    Ниже прим. для библиотеки Nrwl Nx, возможно существует и для Angular см. https://stackoverflow.com/a/46982534
    ( Да, попробовал, работает если создать shared-assets в /projects )
    
    Источник: https://medium.com/@nit3watch/angular-shared-assets-with-multiple-apps-nrwl-nx-b4801c05c771
    Создаем каталог shared-assets в дерриктории libs (не библиотеку!) и в ней /assests, который потом делаем видимым в проектах. Теперь пробрасываем в workspace:
    "assets": [
        "apps/app-one/src/favicon.ico",
        "apps/app-one/src/assets",
        {
            "glob": "**/*",
            "input": "./libs/shared-assets/",
            "output": "./assets"
        }
    ]
    
    Положение /assests в конфиге важно. Если расопложить строку "apps/app-one/src/assets" ниже, то shared-assets затрет совпавшие файлы /assets

32. Создание библиотеки в Angular (library)
    lib это набор components, directives, pipes and services
    
    Команда: ng generate library
    Созданная библиотека будет в каталоге: projects
    Внутри эта команда использует ng-packagr
    Чтобы опубликовать можно использовать: npm publish
    
    Если в приложении есть библиотека my-lib, и несколько application, то lib можно импортировать в несколько application
    Anuglar вставляет в tsconfig.json путь к либам
    Например:
        import { SharedService } from 'my-lib';
        export class AppComponent {
            constructor(sharedService: SharedService) {}
        }
    
    Для angular 6 команда ng build не ребилдит lib автоматом. Нужно вручную.
    
    В module для lib-my нужно добавить imports: [CommonModule]
    
    Создание с префиксом, по умолчанию ПРЕФИКС у библиотек - lib-
        ng generate library tvmaze --prefix tm
    
    Создаем component с указанием проекта (библиотеки):
        ng generate component poster --project=lib-my
        
    Билдим либу вручную:
        ng build lib-my или ng build tvmaze --prod

33. Создание нескольких приложений в одном проекте: ng generate application
    Много аналогий с library

    Создание component в application:
        ng g c my-new-component --project application-name
        
    Запуск с указанием приложенки:
        ng serve application-name --aot

34. AppShell в Angular эта штука которая позволяет кэшировать что-то, чтобы работало быстро и даже без сети.
    Кэшируется только часть данных. Кажется она работает в паре с Service Worker
    
35. ngModelOptions можно менять поведение ngModel
    1. <input [(ngModel)]="name" [ngModelOptions]="{updateOn: 'blur'}">
    2. new FormControl('', {updateOn: 'blur'});
    
    <input [ngModelOptions]="{standalone: true}"> отвязывает control от form (от формы)
    
36. serviceWorker в Angular, тут только суть

    Чистые serviceWorker работают как proxy, могут перехватывать запросы/ответы и менять их. Простое применение это кэш.
    serviceWorker могут использоваться для создания PWA (Progressive Web App). PWA - приложенки на js, serviceWorker в них используются для в основном для кэша и возможно ускорения за счет выполнения в отдельном от js потоке.
    
    Использование:
        1. Добавить serviceWorker в Angular: ng add @angular/pwa --project *project-name*
        2. Использовать класс SwUpdate со встроенными инструментами
        
    Чтобы протестировать работу serviceWorker:
        1. Запустить отдельным сервером: http-server -p 8080 -c-1 dist/<project-name>
        2. Использовать инструменты браузера для теста serviceWorker
        
37. Предупреждение при закрытии
    class MyComponent {
        @HostListener('window:beforeunload')
        doSomething() {}
    }
    
38. Класс Renderer используется для изменения элемента по ссылке. В tutorial не нашел таких примеров, возможно метод устарел.

    export class ChangeBgColorDirective {
        constructor(private el: ElementRef, private renderer: Renderer) {
            // this.ChangeBgColor('red');
        }
        @HostListener('mouseleave') onMouseLeave() {
            this.renderer.setElementStyle(this.el.nativeElement, 'color', 'black');
        }
    }
    
    UPD. Источник: https://alligator.io/angular/using-renderer2/
    Класс Renderer2 это srvice, который используется вместре с directives чтобы изменить элемент: addClass(), addStyle(), removeAttr() etc
    Например он используется чтобы инициализировать textarea плагином CodeMirror для подсветки синтаксиса.
    Использовать ElementRef для прямого доступа к элементу НЕ РЕКОМЕНДУЕТСЯ!
        Tip: Renderer2 может использоваться СОВМЕСТНО с ElementRef (ссылка на ElementRef передается в сервис Renderer2)
        
    UPD2. Renderer класс deprecated, Renderer3 пока в разработке???
    
    Тут обсуждение темы: https://www.reddit.com/r/Angular2/comments/8n78av/angular_material_library_not_using/
        упоминают проект Domino, который позволяет изменять DOM без Renderer2
        
    Пример с reddit (т.е. Renderer3 прослойка для удобного изменения DOM без сервиса Renderer2):
        //render2
        renderer.setAttribute(someElement, 'foo', 'bar');

        //render3
        someElement.setAttribute('foo', 'bar');
    
39. properties vs attributes

Interpolation ( {﻿{...}} ) работает только с attributes, не с properties.

Ошибка, colspan это property и Interpolation вызовет ошибку:
    <tr><td colspan="{{1 + 1}}">Three-Four</td></tr>
    
Правильно, устанавливаем атрибут:
    <tr><td [attr.colspan]="1 + 1">One-Two</td></tr>
    
40. Cast в any тип через $any()
<div>
  The hero's marker is {{$any(hero).marker}}
</div>

41. Проверка переменной на null и undefined через !. Зависит от флага --strictNullChecks в конфиге TypeScript.

Если переменную в этом режиме оставить неинициализированной или присвоить ей null или undefined, то будет ошибка.
Но если в описании типа будет null или undefined, то можно???

<div>
  The hero's name is {{hero!.name}}
</div>

42. trackBy - по умолчанию при использовании *ngFor и др. Angular проверяет не изменились ли все references объекта. trackBy позволяет отслеживать изменения только указанного атрибута. Остальные ссылки будут проигнорированы и *ngFor не сделает update модели.

    1. <div *ngFor="let hero of heroes; trackBy: trackByHeroes">
    2. trackByHeroes(index: number, hero: Hero): number { return hero.id; }

43. Как работать с HAL (HATEOAS - Hypermedia As The Engine Of Application State)
    
    Один из вариантов сделать класс обертку на TypeScript, одно из свойств будет самим объектом, остальное meta инфа.
    https://stackoverflow.com/questions/45442927/angular-4-httpclient-with-hateoas-rest-server
    
44. Подключение кастомных плагинов и типов js переменных

    Лучше использовать type definition чем declare. Это позволяет использовать фичи TypeScript
    
    Способы:
    1.
        declare var Slick: any;
    2.
        declare var Slick: any;
        Object.defineProperty(window, 'Slick', {value: Slick});
        
    3.
        import * as Slick from 'Slick';
        (window as any).Slick = Slick;
        
    4. Свои типы (ПРАВИЛЬНЫЙ вариант)
        custom-typings/slick.d.ts
        
    5. Расширяем jQuery доп. свойствами, которых может не быть в стандартном jQuery
        declare global {
            interface JQuery {
                (selector: string): JQuery;
                imgViewer2(): JQuery;
            }
        }
        

    Источник с лучшими примерами: https://github.com/thymikee/jest-preset-angular/issues/81#issuecomment-337282824
    
45. Интересное решение с тем чтобы переключаться с mock сервиса при разработке на prod сервис в production
    Если коротко: пишем имя service в environment и в prodvider делаем выбор что inject
    
    Чтобы использовать в providedIn можно так:
        @Injectable({providedIn: 'root', useClass: ExtendedClassService})
        export abstract class AbstractClassService {}
        
    NOTE: при этом constructor() для mock сервиса и обычного должны совпадать. Или будет нужно вручную указать сервисы в свойстве deps: [mySrv]
            иначе сервисы не будут inject в constructor
        
    Еще грубый пример (но лучше использовать factory метод):
        @Injectable({
            providedIn: 'root', useClass: !environment.production && environment.optUpdMockService !== undefined
                ? environment.optUpdMockService : OptUpdService
        })
        export class OptUpdService {}

    https://stackoverflow.com/a/44389899
    
46. [ngValue] vs [value]
    Как минимум [ngValue] сохраняет тип значения (number и т.д.), [value] поставит типом строку
    
47. Передача данных между компонентами через route
        https://stackoverflow.com/a/53426887
        https://angular.io/api/router/Resolve
        
        // 1. через общий service, содержащий глобальную переменную
        
        // все что ниже это передача в параметре data: маршрута???
        Пример:
            const appRoutes: Routes = [
                {
                    path: 'heroes',
                    component: HeroListComponent,
                    data: { title: 'Heroes List' } //передача данных
                }
            ];
        
        // (Through Children Router Resolve)
        // 2. через дочерний component
        this.data = this.route.snaphsot.data['dataFromResolver'];
        
        // (Through Parent Router Resolver)
        // 3. через родительский component, если в дочернем нужны те же самые данные что в родителе
        this.data = this.route.parent.snaphsot.data['dataFromResolver'];
        
        // 4. начиная с Angular 7, через объект NavigationExtras преданный 2ым параметром
        // https://stackoverflow.com/a/54365098
        this.router.navigate(['action-selection'], { state: { example: 'bar' } }); // отправляем в 
        constructor(private router: Router) {
            console.log(this.router.getCurrentNavigation().extras.state.example); // получаем
        }
        
        // 5. через параметры (примеры ниже)
        // для их получения используется: private route: ActivatedRoute и private router: Router
        // https://stackoverflow.com/a/44865817
        
        // 5.1 Routing Parameters,
        // вид ссылки: http://bla.com/user/123
        {path: 'user/:id', component: UserComponent} // конфиги рутера
        <a [routerLink]="['/user', user.id]"> // передаем
        this.router.navigate(['/user', this.user.id]); // передаем
        this.router.snapshot.paramMap.get('id'); // получаем
        
        // 5.2 Optional Parameters,
        // вид ссылки: http://bla.com/user;title=ring;si=true
        {path: 'user', component: UserComponent}
        <a [routerLink]="['/user', {title: ring, si: true}]">
        this.router.navigate(['/user', {title: ring, si: true}]);
        this.router.snapshot.paramMap.get('title');
        
        // 5.3 Query Parameters,
        // вид ссылки: http://bla.com/user?title=ring&si=true
        {path: 'user', component: UserComponent}
        <a [routerLink]="['/user']" [queryParams]="{title: ring, si: true}">
        this.router.navigate(['/user'], {queryParams: {title: 'ring', si: true}});
        this.router.snapshot.queryParamMap.get('title'); // получение
        
48. Использовать EventEmit в качестве Subject при взаимодействии между components нельзя!
    
999. Angular Universal: server-side rendering
999. Принципы тестирования
999. Принципы создания mock api (чтобы не делать свой сервер)

-------------------------
Установка

1. Официальная статья
    https://github.com/angular/angular-cli

2. Установка Angular Material
    https://material.angular.io/guide/getting-started
    
3. Установка Covalent
    https://teradata.github.io/covalent/#/docs
    
3. Установка primeng
    https://www.primefaces.org/primeng/#/setup

4. Установка в Ubuntu требует дополнительных прав
        sudo npm i --unsafe-perm -g @angular/cli
    Источник:
        https://github.com/nodejs/node-gyp/issues/454#issuecomment-331103194
    

-------------------------

Посмотреть по Anghular
renderer
async pipe

использовать HAL вместо json?
    http://stateless.co/hal_specification.html
    https://stackoverflow.com/questions/17877220/how-should-hateoas-style-links-be-implemented-for-restful-json-collections
    https://habrahabr.ru/company/aligntechnology/blog/281206/
    https://habrahabr.ru/company/aligntechnology/blog/281206/#comment_8847890

Заметки по работе с данными

1. Создаем событие в потомке. Выполняем и внутрь него передаем данные (объект). В родителе к этому событию привязываем метод, он выполняется.
    (общие для потомков методы по сути находятся в component родителя и вызовы передаются туда)
    
    наследник:
        <button (click)="delete()">Delete</button>
        
        @Output() deleteRequest = new EventEmitter<Hero>();
        deleteHero() {
          this.deleteRequest.emit(this.hero);
        }
        
    родитель:
        <hero-detail (deleteRequest)="deleteHero($event)" [hero]="currentHero"></hero-detail>
    
2. Two-way binding ( [(...)] )
    [] - связываем свойство
    () - связываем событие
    
    1. способ - тут просто создаем свойство size, а событие xChange выполняется автоматически
        <my-sizer [(size)]="fontSizePx"></my-sizer>
    
    2. способ - тут создаем свое событие xChange и по его выполнению меняем переменную size.
            (т.е. атрибут и событие с одинаковыми именами и постфиксом Change объединяются подобно ngModel в атрибут с одним именем [size] + (sizeChange) == [(size)] )
        (fontSizePx=$event - магия, Angular достаем .emit() значение и назначает свойству fontSizePx)
        <my-sizer [size]="fontSizePx" (sizeChange)="fontSizePx=$event"></my-sizer>
    
        export class SizerComponent {
          @Input()  size: number | string;
          @Output() sizeChange = new EventEmitter<number>();
          
          //this.sizeChange.emit(this.size); - вызываем по клику
        }
        
3. Встроенные дерективы (событие). Дополнить!!!

    1. attribute directives
        1 NgClass == [class.special]="isSpecial"
        
            [NgClass] = "currentClasses"
            this.currentClasses =  {
                'saveable': this.canSave,
                'modified': !this.isUnchanged,
                'special':  this.isSpecial
              };
        
        NgStyle - указывает на js объект со стилями
            <some-element [ngStyle]="{'font-style': styleExp}">
        
        NgModel
    
    2. структурные
    
        clickable - по элементу можно кликнуть
            <div (myClick)="clickMessage=$event" clickable>click with myClick</div>
			
Заметки по работе с данными

1. Создаем событие в потомке. Выполняем и внутрь него передаем данные (объект). В родителе к этому событию привязываем метод, он выполняется.
    (общие для потомков методы по сути находятся в component родителя и вызовы передаются туда)
    
    наследник:
        <button (click)="delete()">Delete</button>
        
        @Output() deleteRequest = new EventEmitter<Hero>();
        delete() {
          this.deleteRequest.emit(this.hero);
        }
        
    родитель:
        <hero-detail (deleteRequest)="deleteHero($event)" [hero]="currentHero"></hero-detail>
        
        
1.2 Можно внутри click написать операцию (как в eval())
        1. class MyComponent { color : string }
        2. (click)="color='lightgreen'"
    
2. Two-way binding ( [(...)] )
    [] - связываем свойство
    () - связываем событие
    
    1. способ - тут просто создаем свойство size, а событие xChange выполняется автоматически
        <my-sizer [(size)]="fontSizePx"></my-sizer>
    
    2. способ - тут создаем свое событие xChange и по его выполнению меняем переменную size.
        (fontSizePx=$event - магия, Angular достаем .emit() значение и назначает свойству fontSizePx)
        <my-sizer [size]="fontSizePx" (sizeChange)="fontSizePx=$event"></my-sizer>
    
        export class SizerComponent {
          @Input()  size: number | string;
          @Output() sizeChange = new EventEmitter<number>();
          
          //this.sizeChange.emit(this.size); - вызываем по клику
        }
        
3. Встроенные дерективы (событие). Дополнить!!!

    1. attribute directives
        1	Связываем классы, можно в зависимости от условий.
			NgClass == [class.special]="isSpecial"
        
            [NgClass] = "currentClasses"
            this.currentClasses =  {
                'saveable': this.canSave,
                'modified': !this.isUnchanged,
                'special':  this.isSpecial
              };
        
        2	Связываем стили, можно в зависимости от условий.
			NgStyle == [style.font-size]="isSpecial ? 'x-large' : 'smaller'"
		
			[ngStyle]="currentStyles"
			
			  this.currentStyles = {
				'font-style':  this.canSave      ? 'italic' : 'normal',
				'font-weight': !this.isUnchanged ? 'bold'   : 'normal',
				'font-size':   this.isSpecial    ? '24px'   : '12px'
			  };
		
        3	NgModel. Связываем input и свойства класса (и select).
			Чтобы использовать это с чем-то другим нужно написать свой вариант класса адаптера ControlValueAccessor
			
				<input [(ngModel)]="currentHero.name">
		
			Аналог:
				1. value и input это свойство и событие самого <input> тега
				<input [value]="currentHero.name" (input)="currentHero.name=$event.target.value" >
	   
				2. (событие ngModelChange, прячет сложность версии из примера выше)
                    Т.е. тут по ngModelChange событию происходит установка свойства.
				<input [ngModel]="currentHero.name" (ngModelChange)="currentHero.name=$event">
				
				3. версия выше, только можно переписать вот так, совместив ngModel и ngModelChange:
				<input [(ngModel)]="currentHero.name">
				
		4. Одно из встроенных событий?
		<input
		  [ngModel]="currentHero.name"
		  (ngModelChange)="setUppercaseName($event)">
		  
		5
			NgIf - это НЕ тоже самое что прятать элемент
			<hero-detail *ngIf="isActive"></hero-detail>
			
		6
			NgFor - можно применять и к компонетам
			<hero-detail *ngFor="let hero of heroes" [hero]="hero"></hero-detail>
			
			можно взять индекс (а также last, even, odd)
			<div *ngFor="let hero of heroes; let i=index">{{i + 1}} - {{hero.name}}</div>
			
			trackBy - идентификатор x.id, который будет указывать что элемент с ид=1 в одном списке, это тот же элемент в другом списке если у него ид=1 (не будет повторного перестроения списка?)
			
			<div *ngFor="let hero of heroes; trackBy: trackByHeroes">
			
			trackByHeroes(index: number, hero: Hero): number { return hero.id; }
			
		7
			NgSwitch
			
		8
			ngForm - слишком развита, можно описать отдельно
    
    2. структурные
    
        clickable - по элементу можно кликнуть
            <div (myClick)="clickMessage=$event" clickable>click with myClick</div>

4. microsyntax

5. Template reference variables

	<input #phone placeholder="phone number">
	<button (click)="callPhone(phone.value)">Call</button>
	
	Если ссылка будет на элемент у которого нет атрибута value, то значением будет HTML элемент (код HTML)
	НО! heroForm.form.x - имеются ссылки на разные свойства
	<form #heroForm="ngForm"> //ТАК получается ссылка!
	<input #phonePrice="ngModel" /> //или так
	
	Аналог определения
	ref-fax === #fax
	<input ref-fax placeholder="fax number">
	
6 input и output
	input - привязываются только свойства компонентов
	output - только ловятся события выходящие их дочерних компонентов
	
	Вместо аннотация можно использовать:
	@Component({
	  inputs: ['hero'],
	  outputs: ['deleteRequest'],
	})
	
	Можно задать имя АТРИБУТА В РОДИТЕЛЕ отдельно отлично от 
	@Output('myClick') clicks = new EventEmitter<string>(); //
	
	Или задать имя атрибу отдельно, но не через аннотацию:
	@Directive({
	  outputs: ['clicks:myClick']  // propertyName:alias
	})
	
	Можно привязывать к set(). Например можно заменять пустые строки на значение по умолчанию.
	  @Input()
	  set name(name: string) {
		this._name = (name && name.trim()) || '<no name set>';
	  }
	  
	 ngOnChanges() - метод component, вызывается при каждом изменении компонента. Т.е. при каждом вводе в input, если он связан через {{}} с дочерним компонентом будет выполняться этот метод компонента. Т.к. дочерний компонент перерисовывается, то этот метод родителя постоянно вызывается. Это можно использовать (хотя непонятно зачем).
	
7 pipe

	Формат даты
	<div>Birthdate: {{currentHero?.birthdate | date:'longDate'}}</div>
	
	Вывод в виде JSON, удобно для дебага
	<div>{{currentHero | json}}</div>
	
8 В TypeScript нельзя просто так сделать методы для Model (UserModel).
    https://stackoverflow.com/questions/39433286/method-format-within-angular2-model-class

9 Привязка дочернего компонента к свойству родителя.

  @ViewChild(CountdownTimerComponent)
  private timerComponent: CountdownTimerComponent;

10 Стили Angular
    
    1 к элементы родителю текущего компонента
    :host {
      display: block;
      border: 1px solid black;
    }

    2 :host(.active) {} //если элементе имеет класс .active

    3 :host-context(.theme-light) h2 {} // применяется ко всем h2 компонента, если хотя бы 1 предок (в родительских компонентах) имеет класс .theme-light

    4. стили применются только к их компонентах. 
        НО /deep/, >>>, and ::ng-deep - могут применить стиль родителя ко всем компонентам-потомкам
        ВНИМАНИЕ! Это устаревшие свойства, они будут удалены в будущем.

        :host /deep/ h3 {
          font-style: italic;
        }

    5 Можно использовать ссылки link и @import

        1
        template: `
            <link rel="stylesheet" href="app/hero-team.component.css">
            <h3>Team</h3>`

        2
        @import 'hero-details-box.css';

    6 View encapsulation - изоляция стилей имеет режимы
        Native - спользует встроенные механизмы, работает только в новых браузерах
        Emulated - по умолчанию
        None - нету изоляции

        encapsulation: ViewEncapsulation.Native

11 Статья о том, как использовать дерективы. Перечитать, не описал
    https://angular.io/guide/dynamic-component-loader
    
    По таймеру обновляется содержимое компонента (баннер). Данные подгружаются в него по таймеру.
    Место в которое подгружать данные отмечено дерективой и данные обновляются через неё.
----------------------
Что выучить:
ViewContainer
ng-container
ngZone
	https://stackblitz.com/edit/ng-component-state?file=app%2Fapp.component.ts
trackBy
ContentChild ContentChildren


1.
чтио бы повторно не создавать слушателя на страницы храним его  в isLoggedIn
<md-toolbar color="primary" *ngIf="isLoggedIn$ | async as isLoggedIn">
  <!-- more HTML template code -->
  <button md-button (click)="onLogout()" *ngIf="isLoggedIn">Logout</button>
</md-toolbar>

2. способы скрыть куски страницы от не зарегистрированных
    1. деректива (не забыть отписаться от сервиса внутри дерективы???)
        https://github.com/gothinkster/angular-realworld-example-app/blob/master/src/app/shared/show-authed.directive.ts
    2. устанавливать сервис и вызывать его переменную isAuth в if для проверки
        https://loiane.com/2017/08/angular-hide-navbar-login-page/
    2.2 более короткий и понятный вариант (не забыть отписаться в component???)
        https://stackoverflow.com/questions/43143429/hiding-links-based-on-authentication-angular-2

3. Наборы готовых компонентов
    https://material.angular.io
    https://www.primefaces.org/primeng
    https://vmware.github.io/clarity
    https://teradata.github.io/covalent
    https://ng-bootstrap.github.io

4.  (это называется evil notation???)
    <div *ngIf="sv?.name?.notExist?.testUndefined">

5. Проверка на null и undefined одновременно
    if (x == null) {

6. Пример отображение сообщения "Загрузка"
        <div *ngIf="userList | async as users; else loading"> 
        <user-profile *ngFor="let user of users; count as count" [user]="user">    
        </user-profile> 
        </div> 
        <ng-template #loading>Loading...</ng-template>


7. pipe

// изменения отслеживается только в простых типах и Object у на которые сменилась ссылка
@Pipe({
    name: 'factorial'
})
export class FactorialPipe implements PipeTransform {
  transform(value: number, args?: any): number {}
}

// изменения отслеживается еще и в объектах полей значения которых изменили (то есть везде)
@Pipe({
    name: 'join',
    pure: false
})

8 Понятный пример подключения и использования js библиотек (jquery, underscore)
    http://jasonwatmore.com/post/2017/01/24/angular-2-custom-modal-window-dialog-box

9 Баг в Covalent https://github.com/Teradata/covalent/issues/885
    Жду исправления

10
	APP_INITIALIZER - можно объявить его для загрузки до загрузки сервисов (типа инициализация)
	Использовать как init? Или не возможно?
	
11.
	Как создать конфиги и загрузить до загрузки приложенки (например ip).
	Это не точно, может есть лучший вариант. Это вместо чтение локального property
		https://medium.com/@hasan.hameed/reading-configuration-files-in-angular-2-9d18b7a6aa4
		
12. Использование iframe в Angular
	https://stackoverflow.com/a/38457886
	
13. как добавить куски формы разных типов в цикле внутрь основной формы
	https://stackoverflow.com/questions/38007236/how-to-dynamically-add-and-remove-form-fields-in-angular-2

14. Почитать как сделать валидацию полей, если на проверить поле на правильноесть может только сервер.
    Т.е. с сервера должны прийти ошибка и ее описание

    Какой-то пример
    https://www.puzzle.ch/blog/articles/2017/01/18/server-side-validations-with-angular-2
	
15		Открыть содержимое в новом окне (вкладке)
	https://stackoverflow.com/a/48039944

16. в SSR может не работать обращение к localStorage, window, document.
    Хотя по идеи прокты типа domino должны решать часть проблем.
    
    Тут много нюансов с подменой объектов заглушками, возможно в будущем все будет проше:
        https://mean-dev.info/angular-universal/

Проверка в SSR браузер это или нет
    import { PLATFORM_ID } from '@angular/core';
    import { isPlatformBrowser } from '@angular/common';
    @Component({
        selector: 'mySpecial',
        templateUrl: './mySpecial.component.html'
    })
    export class MySpecialComponent {
        isBrowser: boolean;

        constructor( @Inject(PLATFORM_ID) platformId: Object) {
            this.isBrowser = isPlatformBrowser(platformId);
        }
    }
    // использование
    <md-select *ngIf="isBrowser" placeholder="Favorite food" name="food">
    
Чтобы убрать мерцание нужно:
// app.module.ts
  imports: [
    BrowserModule.withServerTransition({appId: 'my-app-id'})
    ...
    
@NgModule({
  imports: [RouterModule.forRoot(routes, {
    initialNavigation: 'enabled'
  })]
})

17. Работа с JWT

https://www.youtube.com/watch?v=F1GUjHPpCLA
