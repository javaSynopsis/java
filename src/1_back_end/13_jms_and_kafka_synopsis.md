- [JMS](#jms)
  - [Использование JMS](#Использование-jms)
  - [Фильтры в JMS (с примером)](#Фильтры-в-jms-с-примером)
  - [Interceptor в JMS (с примером)](#interceptor-в-jms-с-примером)
  - [Spring JMS](#spring-jms)
- [Kafka](#kafka)
  - [Общее](#Общее)
  - [Пример использования](#Пример-использования)
  - [Interceptor](#interceptor)
  - [Kafka Streams](#kafka-streams)
  - [Connector API](#connector-api)
- [kafka tool](#kafka-tool)
- [jms vs kafka](#jms-vs-kafka)
- [Apache Kafka vs. Integration Middleware (MQ, ETL, ESB)](#apache-kafka-vs-integration-middleware-mq-etl-esb)

Источники:
* общее о том как сервисы взаимодействуют и некоторые формулы расчета нагрузки [тут](https://dzone.com/articles/tens-of-thousands-of-socket-connections-in-java)
* общий состав элементов jms [тут](https://www.javacodegeeks.com/jms-tutorials)

# JMS
Источники: [тут](https://www.javacodegeeks.com/jms-tutorials)

**Java Message Service** (JMS) - это одно из MOM (message-oriented middleware API), для пересылки сообщений между двумя и более клиентами. Это реализация (implementation) для работы с producer–consumer problem. JMS часть Java EE. JMS это стандарт обмена сообщениями, который дает Java EE приложениям create, send, receive, and read messages. Позволяет обмен сообщениями между разными компонентами слабосвязно (loosely coupled), надежно (reliable), асинхронно.

Начиная с Java EE 1.4 JMS provider должен быть реализован всеми Java EE application servers (серверами приложений), JMS реализованы через Java EE Connector Architecture.

**Elements**
* **JMS provider** - реализация JMS на Java **или** реализация JMS в виде адаптера (прослойки) если реализация не на Java (т.е. провайдеры это ActiveMQ, RabbitMQ etc, т.е. провайдерами называют реализации JMS)
* **JMS client** - приложение или процесс, которые делают publishe/subscribe
* **JMS producer/publisher** - клиент который отсылает сообщения (в том числе и просто функции-отправители в рамках одного приложения)
* **JMS consumer/subscriber** - клиент который получает сообщения (в том числе и просто функции-получатели в рамках одного приложения)
* **JMS message** - объект обертка вокруг отправляемых или получаемых данных, т.е. это сообщения с содержимым
* **JMS queue** - структура (коллекция) содержащая полученные сообщения и ожидающие обработки, очередь не гарантирует порядок сообщений в том же порядке в котором они были отправлены, она гарантирует только то что сообщение будет обработано только один раз
* **JMS topic** - аналогично queue, но сообщения может получить множество разных получателей

**topic** еще могут называть **log**

**JMS broker** - это по сути JMS provider, точнее это приложения для управления **queue** или **topic** сообщениями, можно сказать это процесс содержащий коллекции (topic, queue) сообщений. Если используется конвертер сообщений, то брокер может выполнять и функции конвертации при получении/отправке причем конвертацию можно описывать на разных спец. языках (как SQL например).

**Models**
* **Point-to-point** (queue, one to one) - представлен через **queue**, сообщения обрабатывает только один получатель
  * сообщение может быть восстановлено (вернуто) в очередь
  * только один получатель может получить сообщение
  * сильная coupling между отправителем и получателем
  * сложность работы с ней ниже
* **Publish-and-subscribe** (topic, one to many) - представлен через **topic**, сообщения обрабатывает множество получателей
  * слабая связанность (Decoupling)
  * сообщение не может быть восстановлено
  * сложность работы с ней выше

**Note.** Даже если listener для queue запущен в разных копиях одной и той же приложенки, то сообщение обработает только одна копия.

## Использование JMS
Сервера приложений Java EE используют одну из реализация JMS (JMS Provider). Чтобы использовать JMS в приложениях развернутых на этих серверах нужно создать через UI этого сервера или объявить в конфигурации этого сервера **Queue Factory** или **Topic Factory** с определенным именем. В самом приложении из JNDI (из контекста) по имени получить эту Factory, создать connection из нее, создать **session** из **connection**, создать объект **queue** или **topic** и связать с session. После этого можно отправлять или получать сообщения.
```java
InitialContext ctx=new InitialContext();
QueueConnectionFactory f=(QueueConnectionFactory)ctx.lookup("myQueueConnectionFactory"); // достаем Factory по имени
QueueConnection con=f.createQueueConnection(); // connection
con.start();
QueueSession ses=con.createQueueSession(false, Session.AUTO_ACKNOWLEDGE); // session
Queue t=(Queue)ctx.lookup("myQueue"); // queue или topic

// в классе receiver
QueueReceiver receiver=ses.createReceiver(t); 
receiver.setMessageListener(new MessageListener {
     public void onMessage(Message m) {
     }
});

// в классе sender
QueueSender sender=ses.createSender(t);
TextMessage msg=ses.createTextMessage();
msg.setText("bla");
```

## Фильтры в JMS (с примером)
пока пусто

## Interceptor в JMS (с примером)
пока пусто

## Spring JMS
пока пусто

# Kafka
## Общее
Источники: [понятное введение на рус.](https://habr.com/ru/company/piter/blog/352978/)

Kafka был разработан в компании LinkedIn в 2011 году.

**Note.** Kafka иногда называют (можно запомнить к собеседованию) распределенный горизонтально масштабируемый отказоустойчивый журнал коммитов.

**Плюсы kafka:**
* Decoupling of Data Streams.
* Real time instead of batch processing. (работа с сообщениями сразу вместо ожидания пока они накопятся, а потом проведение операций сразу с группой)

**Note.** kafka также как и JMS называют middle layer (но будет ли она им зависит от того как ее использовать)

* **Broker** - сервер или несколько серверов (тогда между ними разделяется нагрузка). Если вы соеденены с одним Broker, то вы соеденены со всеми (со всем кластером, т.е. можно не думать о ручном соединении с каждым сервером и просто работать с ними как с одним). Если 1ин Broker падает, то данные **replicated** (будут получены) из другого.
* **Record**
* **Topic** (log) - место где хранятся сообщения
* **Partition** - каждый topic разбит на Partitions. Копии Partitions разбросаны по разным копиям Brokers (если Brokers несколько). В каждом Broker есть свой **leader** (лидер) и **ISRs** (in sync replicas, ведомые), т.е. главный и второстепенные Partitions. Записать в **topic** идет через **leader**.
* **Offset** - сообщение в Partition расположено со смещением (offset) относительно чего-то
* **Producer** - приложение или кусок приложения посылающий сообщения в Kafka (в брокер(ы)) **Note.** (неважная инфа) Producer иногда называют **генератором**.
* **Consumer** - приложение или кусок приложения слушающий сообщения. **Note.** (неважная инфа) Producer иногда называют **потребителями**.
* **Consumer Group**
* **Zookeeper** - это DB, которая работает как Map (ключ-значение), оптимизировано для чтения и медленнее для записи. Она нужна для работы Kafka как зависимость и ее нужно разворачивать как отдельный сервер. Брокеры Kafka подписываются на события изменения данных в **Zookeeper** (в том числе на событие изменения **leader** брокера на другого **leader**). Zookeeper **отказоустойчив**.
  * **Он используется для хранения всевозможных метаданных, в частности:**
    * Смещение групп потребителей в рамках секции (хотя, современные клиенты хранят смещения в отдельной теме Kafka)
    * ACL (списки контроля доступа) — используются для ограничения доступа /авторизации
    * Квоты генераторов и потребителей — максимальные предельные количества сообщений в секунду 
    * Ведущие секций и уровень их работоспособности

**Журнал коммитов** («журнал опережающей записи», «журнал транзакций») – это долговременная упорядоченная структура данных, причем, данные в такую структуру можно только добавлять. Записи из этого журнала нельзя ни изменять, ни удалять. Информация считывается слева направо; таким образом гарантируется правильный порядок элементов.  
**Note.** Т.е. это что-то вроде stack структуры, но удалять из нее нельзя.  
**Note.** `o(1)` (если известен ID записи) - временная сложность для чтения и записи (быстрее чем у структуры дерево).  
**Note.** Операции считывания и записи не влияют друг на друга (операция считывания не блокирует операцию записи и наоборот, чего не скажешь об операциях со сбалансированными деревьями).

**Особенности**
* Если данные записаны в Partition они не могут быть изменены (Immutability)
* Данные в Partition хранятся ограниченный срок (в настройках установлен limit по времени).
* **leader** это тот Broker где лежит файл log (с сообщениями), остальные серверы это те куда log копируется для надежности. log файл распределенный т.к. лежит на нескольких носителях (там лежат копии log файла).

**Note.** Разнесения Brokers по разным компьютерам называют горизонтальным маштабированием. А сами брокеры иногда называют узлами.

## Пример использования
Источники: [тут пример по шагам на русском](https://habr.com/ru/post/440400/), [тут разные примеры](https://github.com/confluentinc/kafka-streams-examples#examples-apps)

## Interceptor
пока пусто

## Kafka Streams
https://kafka.apache.org/documentation/streams/

## Connector API
http://kafka.apache.org/documentation.html#connect

The Connector API allows building and running reusable producers or consumers that connect Kafka topics to existing applications or data systems. For example, a connector to a relational database might capture every change to a table.


# kafka tool
# jms vs kafka
Источник: [тут](https://stackoverflow.com/questions/42664894/jms-vs-kafka-in-specific-conditions), [тут](https://stackoverflow.com/questions/30453882/is-apache-kafka-another-api-for-jms)

* kafka отличается от jms (т.е. это не jms provider).  Т.е. код и спецификации jms и kafka отличается.
* kafka имеет меньше features (функционала) чем jms
* использует не jms протокол и она ориентирована на performances.
* kafka не использует pont-to-point, только Publish-and-subscribe. 
* kafka лучше для scalability (разнесения по разным узлам) потому что она разработана как partitioned topic log (копии файлов с содержимым topic могут быть разнесены на разные сервера). 
* kafka может разделять поток сообщений на группы. Поэтому kafka имеет лучший ACLs (access control level, установку прав доступа на данные).
* consumer решает какое сообщений потребить на основе offset (смещения в topic относительно чего-то), это снижает сложность написания producer.

# Apache Kafka vs. Integration Middleware (MQ, ETL, ESB)
https://medium.com/@megachucky/apache-kafka-vs-integration-middleware-mq-etl-esb-friends-enemies-or-frenemies-ab02f6f2617b
