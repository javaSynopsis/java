JAX-WS (Java API for XML Web Services)

Это часть Java EE

типы: Spring WS для SOAP, REST для JSON

До Java SE 6 была только реализация в контейнере Java EE, теперь есть и в jdk. Java EE чем jdk для разработки предпочтительнее потому что там есть система мониторинга, security etc.
C jdk 6 добавлены annotations, tools, lightweight HTTP server для быстрой разработки.

JAX-WS - SOAP сервис из Java EE
JAX-RS - REST сервис из Java EE

Если веб сервис изменен, то можно поддерживать в работе несколько сервисов: старый и новый, пока не завершится переход зависимых сервисов на новую версию. Но это затратно.

WSDL - Web Services Description Language

WSDL - contract definition, использует xsd

types - xsd описывающая типы сообщения
message - тег xml описывающая само сообщение input or output
portType - (операции) тег xml содержащий название сервиса и метода к которым отправлен message
binding - тег xml описание протокола и типа данных для каждого portType
service и port теги - описывают соотв. сервис и порт (ссылка на сервис и номер порта)


contract-first - сначала создается contract (wsdl), и по ней генерируются Java классы сообщения

contact-last - сначало создаются Java классы сообщений, а по ним генерируется contract (wsdl) через утилиты (напр. wsimport)

Создание SOAP сервиса по шагам (2 способа):
    1. генерировать Java классы сервиса (сообщения) на клиенте через утилиту wsimport
    2. Создать SOAP клиент который:
        1. Сериализует параметры сервиса в XML
        2. Вызывает @WebMethod сервиса через HTTP
        3. Парсит полученный xml с сервиса в Java object
        
    Для способ 1 (генерируем java object <-> xml утилитой):
        1. Нужно вручную редактировать WSDL на клиент при каждом изменении сервиса
        2. ИЛИ перегенерировать классы каждый раз через wsimport
    
    Для способ 2 (парсим java object <-> xml через xml парсеры):
        1. Чтобы вызвать такой сервис нужно использовать:
            1.2 SAAJ (SOAP with Attachments API for Java) framework (из jdk 6)
            1.3 или вручную через java.net.HttpUrlconnection
        2.1 Преобразовать полученный XML обратно в Java object через OXM (Object to XML Mapping) framework (напр. JAXB)
        2.2 ИЛИ вручную парсить XML (полезно если изменений в данных мало)

Пример генерации wsdl:
    wsimport -s . -p com.baeldung.jaxws.server.topdown employeeservicetopdown.wsdl

При генерации Java object из wsdl через wsimport имя пакета будет как пространство имен наоборот из wsdl.

Сгенерированные по wsdl классы на клиенте называются Stub классами.

Аннотация @WebServiceRef по умолчанию ищет wsdl в каталоге: WEB-INF/wsdl (прим. ниже)
    
Пример простого сервиса JAX-WS:
    // интерфейс
    @Local // default
    // @Remote
    @WebService
    public interface WebserviceSEI {
        @WebMethod // имена методов в wsdl
        String testService();

        @WebMethod
        String sayHelloTo(@WebParam(name = "text") String text);

        @WebMethod
        Goods getGoods();
    }
    
    // реализация
    // (можно сразу делать реализацию без interface)
    @Stateless // не обязательно раз SOAP считается statefull???
    @WebService(
        // portName = "CalculatorPort", // необязательна
        // targetNamespace = "http://superbiz.org/wsdl", // необязательна
        endpointInterface = "ru.javastudy.ws.soap.WebserviceSEI",
        serviceName = "HelloSoap")
    public class HelloSoap implements WebserviceSEI {
    
        @Override
        public String testService() {
            return "Hello from SOAP Webservice!";
        }
    
        @Override
        public String sayHelloTo(String text) {
            return "Hello to " + text;
        }
    
        @Override
        public Goods getGoods() {
            return new Goods();
        }
    }
    
    // создаем class который публикует сервис (это часть того же сервиса)
    public class EmployeeServicePublisher {
        public static void main(String[] args) {
            Endpoint.publish("http://localhost:8080/employeeservice", 
            new EmployeeServiceImpl());
        }
    }
    
    // 0. вызов сервиса на клиенте через сгенерированные класс сервиса
    // EmployeeService_Service - сгенерированный класс по wsdl
    // на самом деле Port тут это название класса proxy для сервиса
    URL url = new URL("http://localhost:8080/employeeservice?wsdl");
    EmployeeService_Service srv = new EmployeeService_Service(url);
    EmployeeService srvPort = srv.getEmployeeServiceImplPort();
    List<Employee> allEmployees = srvPort.getAllEmployees();
    
    // 1. вызов сервиса на клиенте (простой)
    @WebServiceRef(wsdlLocation = 
      "META-INF/wsdl/localhost_8080/helloservice/HelloService.wsdl")
    // @WebServiceRef(HelloMessengerService.class) // или с указанием класса
    private static HelloService srvPort; // привязываем сервис
    helloservice.endpoint.Hello port = service.getHelloPort();
    port.sayHello(arg0); // вызов
    
    // 2. вызов сервиса вручную
    // через HttpUrlconnection и URL банально посылается
    // голый XML перед этим нужно установить Http headers
    // и настр адрес и порт
    
    // 3. SAAJ API - для обмена сообщениями JMS или JAXM с сервисом
    // Пример большой, поэтому тут только суть. Похоже это вариант XML парсера.
    // создаем MessageFactory.newInstance(); из javax.xml.soap.*
    // вытаскиваем куски сообщений и header через
    // soapMessage.getSOAPPart(); soapPart.getEnvelope(); soapEnvelope.getBody()
    SOAPElement soapElement = soapBody.addChildElement("getWelcomeMsg", "end");
    SOAPConnection connection = soapConnectionFactory.createConnection();
    URL endpoint = new URL("http://wombat.ztrade.com/quotes");
    SOAPConnection soapConnection = soapConnectionFactory.createConnection();
    SOAPMessage response = connection.call(message, endpoint); // send
    
    // 4. еще вариант вызова сервиса с клиента через @WebServiceClient
    // 4.2 создаем сервис
    @WebServiceClient(name = "ServerInfoService", 
        targetNamespace = "http://ws.mkyong.com/", 
        wsdlLocation = "http://localhost:8888/ws/server?wsdl")
    public class ServerInfoService extends Service{
        // тут реализация разных методов (создается автоматически???)
    }
    // 4.3 вызываем
    String name = new ServerInfoService().getServerInfoPort().getServerName();

Назначение обработчика wsdl через @HandlerChain. В handler-chain.xml мы указываем class, он будет вызван для обработки xml сообщения сервиса при получении / отправки. Аналог Servlet Filters.
@WebServiceRef(HelloMessengerService.class)
@HandlerChain(file="handler-chain.xml")
private HelloMessenger port;


Аннотации:
@Oneway - метод @WebMethod не возвращает результат
@WebResult - меняет привязку возвращаемого значения к wsdl???

Утилиты:
1. schemagen - генерирует xsd по Java классам
2. wsgen - передаем endpoints, она генерирует artifacts (классы?) для сервисов, чтобы их можно было вызвать на клиенте. Она не нужна, если используется Endpoint.publish(), которая сама генерирует WSDL/schema. С флагом -wsdl может генерировать WSDL/schema.
3. wsimport - генерирует классы по WSDL
4. xjc - генерирует классы по xsd
---